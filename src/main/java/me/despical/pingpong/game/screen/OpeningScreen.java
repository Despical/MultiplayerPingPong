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
            gamePanel.ball.giveDefaultSpeed();
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