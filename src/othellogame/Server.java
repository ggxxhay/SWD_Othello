/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othellogame;

import UI.PlayGround;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
class ServerSide {

    ServerSocket server = null;
    BufferedReader is;
    BufferedWriter os;
    DataInputStream in;
    DataOutputStream ou;
    Socket client = null;
    PlayGround playGround;

//    private String host;
    private int port;

    public ServerSide(String host, int port) {
//        this.host = host;
        this.port = port;
    }

    public ServerSide(int port) {
        this.port = port;
        playGround = new PlayGround(1);
        playGround.setVisible(true);
        playGround.setTitle("Server");
        makeConnectToClient();
        Thread u = new Thread() {
            public void run() {
                receiveDataFromClient();
            }
        };
        u.start();
    }

    void makeConnectToClient() {
        server = null;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error on port: " + port + " + e");
            System.exit(1);
        }
        System.out.println("Server already setup and waiting for client connection ...");

        client = null;
        try {
            client = server.accept();
            playGround.controllers.ou = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
            System.out.println("Acept client");
        } catch (IOException e) {
            System.out.println("Did not accept connection: " + e);
            System.exit(1);
        }
    }

    void receiveDataFromClient() {
        boolean done = false;
        String line = "";
        try {
            while (!done) {
                line = in.readLine();
                System.out.println(",mm" + line);
                if (line.equalsIgnoreCase(".bye")) {
                    done = true;
                } else {
//                    if (line.equals(StaticVariables.message_surrender)) {
//
//                    }
//
//                    if (line.equals(StaticVariables.message_quit)) {
//
//                    }

//                    // When player make a move.
//                    System.out.println(StaticVariables.movePosition + "move");
//                    if (StaticVariables.movePosition != null) {
//                        ou.writeBytes(StaticVariables.movePosition + "\n");
//                        // Reset the position.
//                        StaticVariables.movePosition = "";
//                    }

                    // When opponent move.
                    int[] movePos = StaticVariables.ConvertMovePos(line);
                    if (movePos != null) {
                        System.out.println("server");
                        playGround.controllers.playerTurn = -1;
                        playGround.controllers.checkValidMove();
                        playGround.controllers.makeMove("server", movePos[0], movePos[1]);
                        playGround.controllers.playerTurn = 1;
                        System.out.println("server Player turn: " + playGround.controllers.playerTurn);
                        playGround.controllers.checkValidMove();

                    }
                }
            }

        } catch (IOException e) {
            System.out.println("IO Error in streams " + e);
        }
    }

//    public void finalize() {
//        try {
//            in.close();
//            client.close();
//            server.close();
//        } catch (IOException e) {
//            System.out.println("IO Error in streams " + e);
//        }
//    }
}

public class Server {
    public static void main(String[] args) {
        ServerSide sv = new ServerSide(9999);
    }
}
