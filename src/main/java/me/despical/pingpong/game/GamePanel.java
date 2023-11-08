/*
 * MIT License
 *
 * Copyright (c) 2023 Berke Akçen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.despical.pingpong.game;

import me.despical.pingpong.game.screen.OpeningScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class GamePanel extends JPanel implements Runnable {

    public static GamePanel INSTANCE;
    public static Queue<String> PACKETS;

    // Pre-defined constant values
    public static final int SCREEN_WIDTH = 1000, SCREEN_HEIGHT = 555, BALL_DIAMETER = 20, PADDLE_WIDTH = 25, PADDLE_HEIGHT = 100;
    public static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    public static JCheckBoxMenuItem allowBot1 = new JCheckBoxMenuItem("Allow Bot 1"),
                                    allowBot2 = new JCheckBoxMenuItem("Allow Bot 2"),
                                    createBarrier = new JCheckBoxMenuItem("Create Barrier");

    private final Thread gameThread;
    private final Random random;
    private final OpeningScreen openingScreen;
    private Image image;
    private Graphics graphics;

    public final Score score;

    public Barrier barrier;
    public Ball ball;
    public Paddle firstPaddle, secondPaddle;
    public volatile boolean gameStarted = true, multiplayer;

    public GamePanel() {
        INSTANCE = this;
        PACKETS = new LinkedBlockingQueue<>();

        this.random = new Random();
        this.createPaddles();
        this.createBall();
        this.score = new Score();
        this.openingScreen = new OpeningScreen(this);

        if (createBarrier.getState())
            barrier = new Barrier();

        // JPanel methods
        this.setFocusable(true);
        this.addKeyListener(new GameKeyListener());
        this.setPreferredSize(SCREEN_SIZE);

        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    public void createPaddles() {
        this.firstPaddle = new Paddle(0,(SCREEN_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        this.secondPaddle = new Paddle(SCREEN_WIDTH - PADDLE_WIDTH, (SCREEN_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void createBall() {
        this.ball = new Ball((SCREEN_WIDTH / 2) - (BALL_DIAMETER / 2), SCREEN_HEIGHT / 2, BALL_DIAMETER, BALL_DIAMETER);
    }

    public void draw(Graphics graphics) {
        firstPaddle.draw(graphics);
        secondPaddle.draw(graphics);
        score.draw(graphics);
        ball.draw(graphics);

        if (barrier != null) barrier.draw(graphics);

        Toolkit.getDefaultToolkit().sync();
    }

    public void move() {
        if (!gameStarted) return;

        firstPaddle.move();
        secondPaddle.move();
        ball.move();

        if (allowBot1.getState())
            botMovement1();

        if (allowBot2.getState())
            botMovement2();
    }


    public void onKeyPress(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_R) {
            if (!OpeningScreen.IS_HOST && multiplayer) return;

            this.createPaddles();
            this.createBall();
            this.score.secondPlayerScore = score.firstPlayerScore = 0;

            PACKETS.add("f:0:0");
            PACKETS.add("reset");
        }

        if (event.getKeyCode() == KeyEvent.VK_T) {
            ball.giveDefaultSpeed();
        }
    }

    public void botMovement1() {
        if (ball.x > SCREEN_WIDTH / 2 + random.nextInt(60, 150)) {
            return;
        }

        // larger the values better the accuracy until a very large point
        if (firstPaddle.y < ball.y) firstPaddle.y += random.nextInt(7, 13);
        if (firstPaddle.y > ball.y) firstPaddle.y -= random.nextInt(7, 13);
    }

    public void botMovement2() {
        if (ball.x < SCREEN_WIDTH / 2 - random.nextInt(60, 150)) {
            return;
        }

        // larger the values better the accuracy until a very large point
        if (secondPaddle.y < ball.y) secondPaddle.y += random.nextInt(20, 31);
        if (secondPaddle.y > ball.y) secondPaddle.y -= random.nextInt(20, 31);
    }

    public void checkCollision() {
        if (!gameStarted) return;

        // Check if paddles moves out of the window
        if (firstPaddle.getY() < 0) firstPaddle.y = 0;
        if (firstPaddle.getY() >= SCREEN_HEIGHT - PADDLE_HEIGHT) firstPaddle.y = SCREEN_HEIGHT - PADDLE_HEIGHT;

        if (secondPaddle.getY() < 0) secondPaddle.y = 0;
        if (secondPaddle.getY() >= SCREEN_HEIGHT - PADDLE_HEIGHT) secondPaddle.y = SCREEN_HEIGHT - PADDLE_HEIGHT;

        // Ball collision
        if (ball.getY() < 0 || ball.getY() > SCREEN_HEIGHT - BALL_DIAMETER) ball.bound(false);
        if (ball.getX() < 0 || ball.getX() > SCREEN_WIDTH - BALL_DIAMETER) ball.bound(true);

        if (barrier != null) barrier.checkCollision();

        // Ball collision with paddles
        if (ball.intersects(firstPaddle)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;

            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
        }

        if (ball.intersects(secondPaddle)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;

            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.xVelocity = -ball.xVelocity;
        }

         // Winner - loser algorithm
        if (ball.getX() <= 0) {
            score.updateScore(false);

            this.createPaddles();
            this.createBall();

            System.out.println("Player 2 scored!");
        }

        if (ball.getX() >= SCREEN_WIDTH - BALL_DIAMETER) {
            score.updateScore(true);

            this.createPaddles();
            this.createBall();

            System.out.println("Player 1 scored!");
        }
    }

    @Override
    public void paint(Graphics g) {
        this.image = createImage(getWidth(), getHeight());
        this.graphics = image.getGraphics();

//        if (!gameStarted) {
//            openingScreen.paint(graphics);
//        } else {
            draw(graphics);
//        }

        g.drawImage(image, 0, 0, this);
    }

    @Override
    public void run() {
        // Game loop logic based on 60 ticks
        long lastTime = System.nanoTime();
        double ns = 1000000000 / 60d, delta = 0;

        while (true) {
            long now = System.nanoTime();

            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                move();
                checkCollision();
                repaint();

                delta--;
            }
        }
    }

    public class GameKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            firstPaddle.keyPressed(e);
            secondPaddle.keyPressed(e);

            onKeyPress(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            firstPaddle.keyReleased(e);
            secondPaddle.keyReleased(e);
        }
    }
}