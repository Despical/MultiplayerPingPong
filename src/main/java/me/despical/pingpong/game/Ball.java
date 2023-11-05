package me.despical.pingpong.game;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Ball extends Rectangle {

    int xVelocity, yVelocity;

    public Ball(int x, int y, int width, int height) {
        super(x, y, width, height);

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
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;
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
}