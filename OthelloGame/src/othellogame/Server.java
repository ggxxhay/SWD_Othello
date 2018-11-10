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
import java.net.ServerSocket;
import java.net.Socket;

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
        try {
            listener = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
//            System.exit(1);
        }

        try {
            System.out.println("Server is waiting to accept user...");

            // Chấp nhận một yêu cầu kết nối từ phía Client.
            // Đồng thời nhận được một đối tượng Socket tại server.
            socketOfServer = listener.accept();
            System.out.println("Accept a client!");

            // Mở luồng vào ra trên Socket tại Server.
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
            // Nhận được dữ liệu từ người dùng và gửi lại trả lời.
            while (true) {
                // Đọc dữ liệu tới server (Do client gửi tới).
                line = is.readLine();

                // Ghi vào luồng đầu ra của Socket tại Server.
                // (Nghĩa là gửi tới Client).
//                os.write(">> " + line);
//                // Kết thúc dòng
//                os.newLine();
//                // Đẩy dữ liệu đi
//                os.flush();
                

                if(line.equals(StaticVariables.message_surrender)){
                    
                }
                
                // Nếu người dùng gửi tới QUIT (Muốn kết thúc trò chuyện).
                if (line.equals(StaticVariables.message_quit)) {
                    
                    break;
                }
                
                // When player make a move.
                if(!StaticVariables.movePosition.equals("")){
                    os.write(StaticVariables.movePosition);
                    os.newLine();
                    os.flush();
                    // Reset the position.
                    StaticVariables.movePosition = "";
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
