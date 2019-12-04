/*
package com.example.myapplication.connectserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int ServerPort = 9090;
    private int num_client = 0;
    private String msg;
    private DataOutputStream[] dos_arr = new DataOutputStream[10];

    public void go() throws IOException {
        ServerSocket ss = null;
        Socket s = null;
        int i;

        try {
            ss = new ServerSocket(ServerPort);
            System.out.println("S: Server Opend");
            while (true) {
                s = ss.accept();
                for( i = 0 ; i < 10 ; i++ ) {
                    if( dos_arr[i] == null ) {
                        dos_arr[i] = new DataOutputStream(s.getOutputStream());
                        break;
                    }
                }
                if( i == 10 ) {
                    System.out.println("Server is full");
                    continue;
                }
                num_client++;
                ServerThread st = new ServerThread(s, i);
                st.start();
                msg = String.format(Integer.toString(i+1) + "님이 입장했습니다.");
                send(msg);
                System.out.println(msg);
            }
        } finally {
            if (s != null)
                s.close();
            if (ss != null)
                ss.close();
            System.out.println("Server Closed");
        }
    }

    // send to all clients
    void send(String kkk) {
        int i;

        try {
            for (i = 0 ; i < 10; i++) {
                if( dos_arr[i] != null ) dos_arr[i].writeUTF(kkk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Listening thread
    public class ServerThread extends Thread {
        private Socket socket;
        private DataInputStream dis;
        private int id;

        ServerThread(Socket s, int i) {
            socket = s;
            id = i;
        }
        public void run() {
            try {
                service();
            } catch (IOException e) {
                msg = String.format(Integer.toString(id+1) + "님이 연결을 종료했습니다.");
                send(msg);
                System.out.println(msg);
                dos_arr[id] = null;
                num_client--;
            }
        }

        private void service() throws IOException {
            dis = new DataInputStream(socket.getInputStream());

            String str = null;
            while (true) {
                str = dis.readUTF();
                if (str == null) {
                    msg = String.format(Integer.toString(id+1) + "님이 연결을 종료했습니다.");
                    send(msg);
                    System.out.println(msg);
                    dos_arr[id] = null;
                    num_client--;
                    break;
                }
                msg = String.format(Integer.toString(id+1) + "님: " + str);
                send(msg);
                System.out.println(msg);
            }
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
        try {
            s.go();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
