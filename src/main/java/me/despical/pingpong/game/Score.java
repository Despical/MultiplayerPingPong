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

public class Score extends Rectangle {

    public final static Font TEXT_FONT = new Font("Consolas", Font.PLAIN, 60);

    public int firstPlayerScore = 0, secondPlayerScore = 0;

    public void draw(Graphics graphics) {
        int halfWidth = GamePanel.SCREEN_WIDTH / 2;

        graphics.setColor(Color.white);
        graphics.setFont(TEXT_FONT);
        graphics.drawLine(halfWidth, 0, halfWidth, GamePanel.SCREEN_HEIGHT);

        graphics.drawString(Integer.toString(firstPlayerScore / 10) + firstPlayerScore % 10, halfWidth - 85, 50);
        graphics.drawString(Integer.toString(secondPlayerScore / 10) + secondPlayerScore % 10, halfWidth + 20, 50);

        if (OpeningScreen.IS_HOST) {
            graphics.setFont(TEXT_FONT.deriveFont(Font.PLAIN, 20));
            graphics.setColor(Color.RED);
            graphics.drawString("Press R to restart manually", 10, 25);
        }
    }

    public void updateScore(boolean firstPlayer) {
        if (firstPlayer)
            firstPlayerScore++;
        else
            secondPlayerScore++;

        GamePanel.PACKETS.add("f:%d:%d".formatted(firstPlayerScore, secondPlayerScore));
    }
}