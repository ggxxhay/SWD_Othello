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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luxury
 */
class ClientSide {

    Socket client;
    BufferedWriter os;
    BufferedReader is;
    DataOutputStream ou;
    DataInputStream in;
    PlayGround playGround;

    private int port;
    private String host;
    
    public ClientSide(String host, int port) {
        this.host = host;
        this.port = port;
        playGround = new PlayGround(-1);
        playGround.setVisible(true);
        playGround.setTitle("Client");
        makeConnectToServer();
        Thread u = new Thread() {
            public void run() {
                receiveDataFromServer();
            }
        };
        u.start();
    }
    
    void makeConnectToServer() {
        try {
            client = new Socket(host, port);
            playGround.controllers.ou = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println(host + ": unknown host.");
        } catch (IOException e) {
            System.err.println("I/O error with " + host);
        }
    }

    void receiveDataFromServer() {
        boolean done = false;
        String line = "";
        try {
            while (!done) {
                line = in.readLine();
                System.out.println("=="+line);
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

                    // When player make a move.
//                    System.out.println("move client");
//                    if (StaticVariables.movePosition != null) {
//                        ou.writeBytes(StaticVariables.movePosition + "\n");
//                        os.write(StaticVariables.movePosition);
//                        os.newLine();
//                        os.flush();
//                        // Reset the position.
//                        StaticVariables.movePosition = "";
//                    }

                    // When opponent move.
                    int[] movePos = StaticVariables.ConvertMovePos(line);
                    System.out.println("cliii" + movePos[0] + "ccc" + movePos[1]);
                    if (movePos != null) {
                        playGround.controllers.playerTurn = 1;
                        playGround.controllers.makeMove(movePos[0], movePos[1]);
                        playGround.controllers.playerTurn = -1;
                        playGround.controllers.checkValidMove();
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("IO Error in streams " + e);
        }
    }

    public void finalize() {
        try {
            ou.close();
            client.close();
        } catch (IOException e) {
            System.err.println("I/O error with " + host);
        }
    }
}

public class Client {

    public static void main(String[] args) {
        ClientSide client = new ClientSide("localhost", 9999);
    }
}
