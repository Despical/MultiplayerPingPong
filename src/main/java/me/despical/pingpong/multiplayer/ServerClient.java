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

package me.despical.pingpong.multiplayer;

import me.despical.pingpong.game.GamePanel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerClient {

    public ServerClient(GamePanel gamePanel) {
        gamePanel.multiplayer = true;

        try {
            tryToInitialize(gamePanel);
        } catch (IOException e) {
            gamePanel.multiplayer = false;

            e.printStackTrace();
        }
    }

    private void tryToInitialize(GamePanel gamePanel) throws IOException {
        var socket = new DatagramSocket();
        var packet = new DatagramPacket(new byte[1024], 1024);
        packet.setAddress(InetAddress.getByName("localhost"));
        packet.setPort(9999);

        var listenSocket = new DatagramSocket(5555);

        System.out.println("Server is initialized.");

        // Host Client Packet Handler
        new Thread(() -> {
            while (!listenSocket.isClosed()) {
                try {
                    var receivedPacket = new DatagramPacket(new byte[1024], 1024);
                    listenSocket.receive(receivedPacket);

                    var receivedData = new String(receivedPacket.getData());

                    if (Client.checkIfSame(receivedData, "hello")) {
                        System.out.println("we are now connected with the client");

                        packet.setData("startGame".getBytes());
                        socket.send(packet);

                        gamePanel.gameStarted = true;
                        gamePanel.ball.giveDefaultSpeed();
                        continue;
                    }

                    if (receivedData.charAt(0) == 'p') {
                        var split = receivedData.split(":");
                        var y = Integer.parseInt(split[1]);
                        var id = Integer.parseInt(split[2]);

                        if (id == 2) {
                            gamePanel.secondPaddle.y = y;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // Host Client Packet Sender
        new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    var packetContent = GamePanel.PACKETS.poll();

                    if (packetContent == null) continue;

                    packet.setData(packetContent.getBytes());
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}