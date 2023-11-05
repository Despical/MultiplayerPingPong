package me.despical.pingpong.multiplayer;

import me.despical.pingpong.game.GamePanel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    public Client(GamePanel gamePanel) {
        try {
            tryToInitialize(gamePanel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void tryToInitialize(GamePanel gamePanel) throws IOException {
        var socket = new DatagramSocket(9999);

        System.out.println("Client is initialized.");

        // Client Packet Handler
        new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    var receivedPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(receivedPacket);

                    String receivedData = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                    if (checkIfSame(receivedData, "startGame")) {
                        gamePanel.gameStarted = true;
                    }

                    if (receivedData.charAt(0) == 'r') {
                        gamePanel.createPaddles();
                    }

                    if (receivedData.charAt(0) == 'p') {
                        var split = receivedData.split(":");
                        var y = Integer.parseInt(split[1]);
                        var id = Integer.parseInt(split[2]);

                        if (id == 1) {
                            gamePanel.firstPaddle.y = y;
                        }
                    }

                    if (receivedData.charAt(0) == 'b') {
                        var split = receivedData.split(":");
                        var x = Integer.parseInt(split[1]);
                        var y = Integer.parseInt(split[2]);

                        gamePanel.ball.x = x;
                        gamePanel.ball.y = y;
                    }

                    if (receivedData.charAt(0) == 'f') {
                        var split = receivedData.split(":");
                        var firstPlayerScore = Integer.parseInt(split[1]);
                        var secondPlayerScore = Integer.parseInt(split[2]);

                        gamePanel.score.firstPlayerScore = firstPlayerScore;
                        gamePanel.score.secondPlayerScore = secondPlayerScore;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // Client Packet Sender
        new Thread(() -> {
            boolean sentHelloPacket = false;

            try {
                var packet = new DatagramPacket(new byte[1024], 1024);
                packet.setAddress(InetAddress.getByName("localhost"));
                packet.setPort(5555);

                while (!socket.isClosed()) {
                    if (!sentHelloPacket) {
                        packet.setData("hello".getBytes());
                        socket.send(packet);

                        sentHelloPacket = true;
                        continue;
                    }

                    var packetContent = GamePanel.PACKETS.poll();

                    if (packetContent == null) continue;

                    packet.setData(packetContent.getBytes());
                    socket.send(packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static boolean checkIfSame(String receivedData, String toCheck) {
        var array = toCheck.toCharArray();

        for (int i = 0; i < array.length; i++) {
            if (toCheck.charAt(i) == receivedData.charAt(i)) continue;

            return false;
        }

        return true;
    }
}