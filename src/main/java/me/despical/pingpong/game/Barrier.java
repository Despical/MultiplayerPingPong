package me.despical.pingpong.game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Barrier extends Rectangle implements MouseListener, MouseMotionListener {

    private static final GamePanel gamePanel = GamePanel.INSTANCE;

    private boolean intersects;

    public Barrier() {
        super(436, GamePanel.SCREEN_HEIGHT / 2, 40, 125);
        gamePanel.addMouseListener(this);
        gamePanel.addMouseMotionListener(this);

    }

    public void draw(Graphics graphics) {
        graphics.fillRect(x, y, width, height);
    }

    public void checkCollision() {
        var ball = gamePanel.ball;

        if (ball.intersects(this)) {
            ball.bound(true);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.intersects = e.getX() >= x && e.getX() <= x + 40 && e.getY() >= y && e.getY() <= y + 125;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (intersects) {
            intersects = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (intersects) {
            x = e.getX();
            y = e.getY();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}