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

    /**
     * constructor
     */
    TcpClient() {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        try {
            InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
            this.socket = new Socket(serverAddr, 8007);
            this.out = socket.getOutputStream();
            this.input = socket.getInputStream();
        } catch (Exception e) {
            String shit ="shit";
        }
    }

    /**
     * send given image to the server
     * @param image an image
     */
    public void SendInfoToServer(File image) {
        try {
            FileInputStream fis = new FileInputStream(image);
            Bitmap bm = BitmapFactory.decodeStream(fis);
            byte[] imageBytes = getBytesFromBitmap(bm);
            //send the number of bytes of the given image and it's name
            String numBytes = String.valueOf(imageBytes.length);
            String name  = image.getName();
            String info = numBytes + " " +image.getName();
            out.write(info.getBytes(),0, info.getBytes().length);
            out.flush();
            //waiting for confirmation from the server
            byte[] confirm = new byte[1];
            int readConfirm = input.read(confirm,0,confirm.length);
            if (confirm[0] == 1) {
                out.write(imageBytes,0,imageBytes.length);
                out.flush();
            }
        } catch (Exception e) {}
    }

    /**
     * reading the byets into array
     * @param bitmap input
     * @return a bytes array of the given bitmap
     */
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();
    }

    /**
     * closes the connection
     */
    public void closeConnection() {
        try {
            this.out.close();
            this.input.close();
            this.socket.close();
        }catch (Exception e) {}
    }


}