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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ThangHQ
 */
public class Server {

    ServerSocket listener = null;
    String line;
    BufferedReader is;
    BufferedWriter os;
    Socket socketOfServer = null;

//    private String host;
    private int port;

    public Server(String host, int port) {
//        this.host = host;
        this.port = port;
    }

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        listener = null;
        try {
            listener = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
//            System.exit(1);
        }
        socketOfServer = null;
        try {
            System.out.println("Server is waiting to accept user...");

            // Chấp nhận một yêu cầu kết nối từ phía Client.
            // Đồng thời nhận được một đối tượng Socket tại server.
            socketOfServer = listener.accept();
            System.out.println("Accept a client!");

            // Mở luồng vào ra trên Socket tại Server.
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));

            PlayGround playGround = new PlayGround(1);
            playGround.setVisible(true);

            System.out.println("while true");
            // Nhận được dữ liệu từ người dùng và gửi lại trả lời.
            Thread thread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
//                            line = is.readLine();
//                            if (line.equals(StaticVariables.message_surrender)) {
//
//                            }
//
//                            if (line.equals(StaticVariables.message_quit)) {
//
//                            }

                            // When player make a move.
                            System.out.println(StaticVariables.movePosition + "move");
                            if (StaticVariables.movePosition != null) {
                                System.out.println(StaticVariables.movePosition);
                                os.write(StaticVariables.movePosition);
                                os.newLine();
                                os.flush();
                                // Reset the position.
                                StaticVariables.movePosition = "";
                            }

                            // When opponent move.
                            int[] movePos = StaticVariables.ConvertMovePos(line);
                            if (movePos != null) {
                                playGround.controllers.playerTurn = -1;
                                playGround.controllers.makeMove(movePos[0], movePos[1]);
                                playGround.controllers.playerTurn = 1;
                                playGround.controllers.checkValidMove();
                            }
                            this.wait();
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            thread.start();

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server sv = new Server(9999);
        sv.run();
    }
}
