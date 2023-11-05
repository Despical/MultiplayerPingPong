package me.despical.pingpong.game;

import java.awt.*;

public class Score extends Rectangle {

    private final static Font TEXT_FONT = new Font("Consolas", Font.PLAIN, 60);

    int firstPlayerScore = 0, secondPlayerScore = 0;

    public void draw(Graphics graphics) {
        int halfWidth = GamePanel.SCREEN_WIDTH / 2;

        graphics.setColor(Color.white);
        graphics.setFont(TEXT_FONT);
        graphics.drawLine(halfWidth, 0, halfWidth, GamePanel.SCREEN_HEIGHT);

        graphics.drawString(Integer.toString(firstPlayerScore / 10) + firstPlayerScore % 10, halfWidth - 85, 50);
        graphics.drawString(Integer.toString(secondPlayerScore / 10) + secondPlayerScore % 10, halfWidth + 20, 50);

        graphics.setFont(TEXT_FONT.deriveFont(Font.PLAIN, 20));
        graphics.setColor(Color.RED);
        graphics.drawString("Press R to restart manually", 10, 25);
    }
}