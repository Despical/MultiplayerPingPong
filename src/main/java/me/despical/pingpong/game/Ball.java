package me.despical.pingpong.game;

import me.despical.pingpong.game.screen.OpeningScreen;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Ball extends Rectangle {

    int xVelocity, yVelocity;

    public Ball(int x, int y, int width, int height) {
        super(x, y, width, height);

        giveDefaultSpeed();
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;

        updateBallPosition();
    }

    public void bound(boolean x) {
        if (x) {
            xVelocity = -xVelocity;
            return;
        }

        yVelocity = -yVelocity;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.fillOval(x, y, width, height);
    }

    public void giveDefaultSpeed() {
        if (!GamePanel.INSTANCE.gameStarted) return;

        int randomXDirection = ThreadLocalRandom.current().nextInt(2);

        if (randomXDirection == 0) {
            xVelocity = -2;
        } else {
            xVelocity = 2;
        }

        int randomYDirection = ThreadLocalRandom.current().nextInt(2);

        if (randomYDirection == 0) {
            yVelocity = -2;
        } else {
            yVelocity = 2;
        }

        updateBallPosition();
    }

    private void updateBallPosition() {
        if (OpeningScreen.IS_HOST) {
            GamePanel.PACKETS.add("b:%d:%d".formatted(x, y));
        }
    }
}