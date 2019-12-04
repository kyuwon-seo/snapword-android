

////////////////////////////////////////////////////roomActivity/////////////////////////////////////
/*
package com.example.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RoomActivity extends AppCompatActivity {

    private String html;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private String ip = "172.30.1.23";
    private int port = 9090;

    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        final EditText et = (EditText) findViewById(R.id.EditText01);
        Button btn = (Button) findViewById(R.id.Button01);
        Log.d("ROOMAC", "ROOM START");
        // Socket making thread
        new Thread(new Runnable() {
            public void run() {
                try {
                    setSocket(ip, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final TextView tv = (TextView) findViewById(R.id.TextView01);
                final ScrollView sv = (ScrollView) findViewById(R.id.roomScroll);
                Log.w("ChattingStart", "Start Thread");
                tv.append("--채팅 시작--\n");
                while (true) {
                    try {
                        html = dis.readUTF();
                        if (html != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.append(html + "\n");
                                    sv.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // "send" button listener
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (et.getText().toString() != null && !et.getText().toString().equals("")) {
                    String return_msg = et.getText().toString();
                    et.setText(""); // clear edit text view
                    try {
                        dos.writeUTF(return_msg);
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    public void setSocket(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}*/
