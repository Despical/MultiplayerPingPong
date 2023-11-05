package me.despical.pingpong.game;

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
        y += velocityY;
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