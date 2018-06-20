package com.example.yuvalhoch.android;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageService extends Service {
    private BroadcastReceiver myReceiver;
    public ImageService(){}
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
        theFilter.addAction("android.net.wifi.STATE_CHANGE");
        this.myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        //get the different network states
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            showSuccessfulBroadcast();
                            //Starting the Transfer
                            sendImages();
                        }
                    }
                }
            }
        };
        // Registers the receiver so that your service will listen for broadcasts
        this.registerReceiver(this.myReceiver, theFilter);
    }
    public int onStartCommand(Intent intent, int flag, int startId) {
        Toast.makeText(this,"Service starting...", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
    public void onDestroy() {
        Toast.makeText(this,"Service ending...", Toast.LENGTH_SHORT).show();
    }
    private void showSuccessfulBroadcast() {
        Toast.makeText(this, "There is a wifi connection", Toast.LENGTH_LONG).show();
    }

    private void sendImages() {
        final List<File> images = getDCIMimages();
        final int notify_id = 1;
        final NotificationManagerCompat NM = NotificationManagerCompat.from(this);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default");
        mBuilder.setContentTitle("Picture Transfer").setContentText("Transfer in progress").setSmallIcon(R.mipmap.ic_launcher).setPriority(NotificationCompat.PRIORITY_LOW);
        mBuilder.setProgress(100, 0, false);
        NM.notify(notify_id, mBuilder.build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1, inc = 1, j;
                if (images.size() != 0) {
                    inc =  100/images.size();
                }
                TcpClient client = new TcpClient();
                for(File image:images) {
                    client.SendInfoToServer(image);
                    j = i * inc;
                    mBuilder.setProgress(100, j, false);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                    try {
                        NM.notify(notify_id, mBuilder.build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // At the End
                mBuilder.setContentText("Download complete").setProgress(0, 0, false);
                try {
                    NM.notify(notify_id, mBuilder.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<File> getDCIMimages() {
        List<File> images = new ArrayList<File>();
        // Getting the Camera Folder
        File dcim = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Camera");
        if (dcim == null) {
            return null;
            //return all photos in DCIM
        }
        extractRecursively(dcim,images);
        return  images;
    }

    private void extractRecursively(File dcim,List<File> images) {
        File[] files = dcim.listFiles();
        if (files!=null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    //the list returns suppose to contain only images - not directories!
                    extractRecursively(file, images);
                    //debug should we change the else to check it?
                } else {
                    images.add(file);
                }
            }
        }

    }

}