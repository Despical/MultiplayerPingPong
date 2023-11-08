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
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {

    private final int id;
    public int velocityY;

    public Paddle(int x, int y, int paddleWidth, int paddleHeight, int id) {
        super(x, y, paddleWidth, paddleHeight);
        this.id = id;
    }

    public void keyPressed(KeyEvent event) {
        setVelocity(event.getKeyCode(), -10, 10);
    }

    public void keyReleased(KeyEvent event) {
            setVelocity(event.getKeyCode(), 0, 0);
    }

    public void move() {
        final var multiplayer = GamePanel.INSTANCE.multiplayer;

        if (multiplayer && OpeningScreen.IS_HOST && id == 2) return;
        if (multiplayer && !OpeningScreen.IS_HOST && id == 1) return;

        y += velocityY;

        if (!multiplayer) return;

        String packet = "p:" + y + ":" + id + ":";

        GamePanel.PACKETS.add(packet);
    }

    private void setVelocity(int keyCode, int up, int down) {
        switch (id) {
            case 1 -> {
                switch (keyCode) {
                    case KeyEvent.VK_W -> velocityY = up;
                    case KeyEvent.VK_S -> velocityY = down;
                }
            }

            case 2 -> {
                switch (keyCode) {
                    case KeyEvent.VK_UP -> velocityY = up;
                    case KeyEvent.VK_DOWN -> velocityY = down;
                }
            }
        }
    }

    public void draw(Graphics graphics) {
        graphics.setColor(id == 1 ? Color.red : Color.blue);
        graphics.fillRect(x, y, width, height);
    }
}