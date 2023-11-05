package me.despical.pingpong.game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        var gamePanel = new GamePanel();
        var menuBar = new BackgroundMenuBar();
        var menu = new JMenu("Settings");
        menu.setForeground(Color.white);
        menu.add(GamePanel.allowBot1);
        menu.add(GamePanel.allowBot2);
        menuBar.add(menu);

        setJMenuBar(menuBar);
        this.add(gamePanel);
        this.setTitle("Ping Pong by Despical");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private static class BackgroundMenuBar extends JMenuBar {

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            var g2d = (Graphics2D) graphics;
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
