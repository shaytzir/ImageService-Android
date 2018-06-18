package com.example.yuvalhoch.android;

import java.io.*;
import java.net.*;

class TcpClient {
    public void StartTcpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sentence;
                //String modifiedSentence;
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                try {
                    InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
                    Socket clientSocket = new Socket(serverAddr, 8006);
                    OutputStream out = clientSocket.getOutputStream();
                    //DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    sentence = inFromUser.readLine();
                    //out.writeBytes(sentence + '\n');
                } catch (Exception e) {

                }
                //modifiedSentence = inFromServer.readLine();
                //clientSocket.close();
            }
        }).start();
    }
}