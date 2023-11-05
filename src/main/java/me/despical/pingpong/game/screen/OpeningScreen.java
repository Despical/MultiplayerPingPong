package me.despical.pingpong.game.screen;

import me.despical.pingpong.Main;
import me.despical.pingpong.game.GamePanel;
import me.despical.pingpong.game.Score;
import me.despical.pingpong.multiplayer.Client;
import me.despical.pingpong.multiplayer.ServerClient;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class OpeningScreen implements MouseListener {

    public static boolean IS_HOST;

    private final GamePanel gamePanel;
    private final Font font;
    private FontMetrics metrics;

    public OpeningScreen(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.font = Score.TEXT_FONT.deriveFont(Font.PLAIN, 20);

        gamePanel.addMouseListener(this);
    }

    public void paint(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.setFont(font);

        metrics = graphics.getFontMetrics(font);

        graphics.drawString("Create LAN Game", 418, 227);
        graphics.drawString("Create Normal Game", 401, 272);
        graphics.drawString("Join LAN Game", 429, 322);

        graphics.drawRect(409, 210, metrics.stringWidth("Create LAN Game") + 15, 25);
        graphics.drawRect(392, 255, metrics.stringWidth("Create Normal Game") + 15, 25);
        graphics.drawRect(420, 305, metrics.stringWidth("Join LAN Game") + 15, 25);

        if (IS_HOST) {
            graphics.drawString("Waiting for an opponent...", 25, 25);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int x = mouseEvent.getX(), y = mouseEvent.getY();

        if (x >= 409 && x <= 409 + metrics.stringWidth("Create LAN Game") + 15 && y >= 210 && y <= 210 + 15) {
            new ServerClient(gamePanel);

            IS_HOST = true;

            Main.GAME_FRAME.setTitle("Ping Pong by Despical (Server)");
        }

        if (x >= 392 && x <= 392 + metrics.stringWidth("Create Normal Game") + 15 && y >= 255 && y <= 255 + 15) {
            gamePanel.gameStarted = true;
        }

        if (x >= 420 && x <= 420 + metrics.stringWidth("Join LAN Game") + 15 && y >= 305 && y <= 330) {
            new Client(gamePanel);

            Main.GAME_FRAME.setTitle("Ping Pong by Despical (Client)");
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
}