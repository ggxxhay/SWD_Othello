/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othellogame;

import UI.PlayGround;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Luxury
 */
class ClientSide {

    Socket client;
    BufferedWriter os;
    BufferedReader is;
    String line;

    private int port;
    private String host;

    public ClientSide(int port) {
        this.port = port;
    }

    public ClientSide(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void makeConnectToServer() {
        try {
            client = new Socket(host, port);
            // Mở luồng vào ra trên Socket tại Client.
            System.out.println("Connecting to server");
            is = new BufferedReader(new InputStreamReader(client.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (UnknownHostException e) {
            System.err.println(host + ": unknown host.");
        } catch (IOException e) {
            System.err.println("I/O error with " + host);
        }
    }

    public void sendDataToServer(String data) {
        try {
            os.write(data);
            os.newLine();
            os.flush();
            // Reset the position.
            StaticVariables.movePosition = "";
        } catch (IOException e) {
            System.err.println("I/O error with " + host);
        }
    }

    public void receiveDataFromServer() {
        try {
            while (true) {
                client = new Socket(host, port);
                // Mở luồng vào ra trên Socket tại Client.
                System.out.println("Connecting to server");
                os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                is = new BufferedReader(new InputStreamReader(client.getInputStream()));

                PlayGround playGround = new PlayGround(-1);
                playGround.setVisible(true);
                
                // Đọc dữ liệu tới server (Do client gửi tới).
                line = is.readLine();
                System.out.println("v" + line);
                // Ghi vào luồng đầu ra của Socket tại Server.
                // (Nghĩa là gửi tới Client).
//                os.write(">> " + line);
//                // Kết thúc dòng
//                os.newLine();
//                // Đẩy dữ liệu đi
//                os.flush();
                if (line.equals(StaticVariables.message_surrender)) {

                }

                // Nếu người dùng gửi tới QUIT (Muốn kết thúc trò chuyện).
                if (line.equals(StaticVariables.message_quit)) {

                    break;
                }

                // When player make a move.
                if (!StaticVariables.movePosition.equals("")) {
                    os.write(StaticVariables.movePosition);
                    os.newLine();
                    os.flush();
                    // Reset the position.
                    StaticVariables.movePosition = "";
                }
                
                // When opponent move.
                int[] movePos = StaticVariables.ConvertMovePos(line);
                if (movePos != null) {
                    System.out.println(line);
                    playGround.controllers.playerTurn = 1;
                    playGround.controllers.makeMove(movePos[0], movePos[1]);
                    playGround.controllers.playerTurn = -1;
                    playGround.controllers.checkValidMove();
                }
            }

        } catch (IOException e) {
            System.out.println("IO Error in streams " + e);
        }
    }

    public void finalize() {
        try {
            os.close();
            client.close();
        } catch (IOException e) {
            System.err.println("I/O error with " + host);
        }
    }
}

public class Client {

    public static void main(String[] args) {
        ClientSide client = new ClientSide("localhost", 9999);
        client.makeConnectToServer();
//        while(true){
            client.receiveDataFromServer();
//        }
//        Thread u = new Thread() {
//            public void run() {
//                client.receiveDataFromServer();
//            }
//        };
//        u.start();
    }
}
