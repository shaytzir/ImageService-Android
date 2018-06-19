package com.example.yuvalhoch.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.List;

class TcpClient {
    private Socket socket;
    private OutputStream out;
    private InputStream input;
    TcpClient() {
        //String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        try {
            InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
            this.socket = new Socket(serverAddr, 8007);
            this.out = socket.getOutputStream();
            this.input = socket.getInputStream();
        } catch (Exception e) {

        }
    }

    public void SendInfoToServer(File image) {
        try {
            FileInputStream fis = new FileInputStream(image);
            Bitmap bm = BitmapFactory.decodeStream(fis);
            byte[] imageBytes = getBytesFromBitmap(bm);

            String numBytes = String.valueOf(imageBytes.length);
            String info = numBytes + " " +image.getName();
            out.write(info.getBytes(),0, info.getBytes().length);
            out.flush();
            out.write(imageBytes,0,imageBytes.length);
            out.flush();
        } catch (Exception e) {

        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();
    }


}