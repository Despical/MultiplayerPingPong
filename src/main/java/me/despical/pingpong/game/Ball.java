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