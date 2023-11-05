package me.despical.pingpong.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    public static GamePanel INSTANCE;

    // Pre-defined constant values
    public static final int SCREEN_WIDTH = 1000, SCREEN_HEIGHT = 555, BALL_DIAMETER = 20, PADDLE_WIDTH = 25, PADDLE_HEIGHT = 100;
    public static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    public static JCheckBoxMenuItem allowBot1 = new JCheckBoxMenuItem("Allow Bot 1"),
                                    allowBot2 = new JCheckBoxMenuItem("Allow Bot 2");

    private final Thread gameThread;
    private final Random random;
    private final Score score;
    private Image image;
    private Graphics graphics;
    public Paddle firstPaddle, secondPaddle;
    private Ball ball;

    public GamePanel() {
        INSTANCE = this;

        this.random = new Random();
        this.createPaddles();
        this.createBall();
        this.score = new Score();

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

        Toolkit.getDefaultToolkit().sync();
    }

    public void move() {
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
            this.createPaddles();
            this.createBall();
            this.score.secondPlayerScore = score.firstPlayerScore = 0;
        }

        if (event.getKeyCode() == KeyEvent.VK_T) {
            try {

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void botMovement1() {
        if (ball.x > SCREEN_WIDTH / 2 + random.nextInt(60, 150)) {
            return;
        }

        // larger the values better the accuracy until a very large point
        if (firstPaddle.y < ball.y) firstPaddle.y += random.nextInt(3, 7);
        if (firstPaddle.y > ball.y) firstPaddle.y -= random.nextInt(3, 7);
    }

    public void botMovement2() {
        if (ball.x < SCREEN_WIDTH / 2 - random.nextInt(60, 150)) {
            return;
        }

        // larger the values better the accuracy until a very large point
        if (secondPaddle.y < ball.y) secondPaddle.y += random.nextInt(3, 7);
        if (secondPaddle.y > ball.y) secondPaddle.y -= random.nextInt(3, 7);
    }

    public void checkCollision() {
        // Check if paddles moves out of the window
        if (firstPaddle.getY() < 0) firstPaddle.y = 0;
        if (firstPaddle.getY() >= SCREEN_HEIGHT - PADDLE_HEIGHT) firstPaddle.y = SCREEN_HEIGHT - PADDLE_HEIGHT;

        if (secondPaddle.getY() < 0) secondPaddle.y = 0;
        if (secondPaddle.getY() >= SCREEN_HEIGHT - PADDLE_HEIGHT) secondPaddle.y = SCREEN_HEIGHT - PADDLE_HEIGHT;

        // Ball collision
        if (ball.getY() < 0 || ball.getY() > SCREEN_HEIGHT - BALL_DIAMETER) ball.bound(false);
        if (ball.getX() < 0 || ball.getX() > SCREEN_WIDTH - BALL_DIAMETER) ball.bound(true);

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
            score.secondPlayerScore++;

            this.createPaddles();
            this.createBall();

            System.out.println("Player 2 scored!");
        }

        if (ball.getX() >= SCREEN_WIDTH - BALL_DIAMETER) {
            score.firstPlayerScore++;

            this.createPaddles();
            this.createBall();

            System.out.println("Player 1 scored!");
        }
    }

    @Override
    public void paint(Graphics g) {
        this.image = createImage(getWidth(), getHeight());
        this.graphics = image.getGraphics();

        draw(graphics);

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