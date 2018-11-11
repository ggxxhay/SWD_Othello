/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othellogame;

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
                // read data from server send
                line = is.readLine();
                // Nếu người dùng gửi tới surrender (Muốn đầu hàng game).
                if (line.equals(StaticVariables.message_surrender)) {
                    break;
                }
                // Nếu người dùng gửi tới quit (Muốn kết thúc game).
                if (line.equals(StaticVariables.message_quit)) {
                    break;
                }
                StaticVariables.movePosition = line;
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
        ClientSide client = new ClientSide("localhost", 4321);
        client.makeConnectToServer();
        Thread u = new Thread() {
            public void run() {
                client.receiveDataFromServer();
            }
        };
        u.start();
    }
}
