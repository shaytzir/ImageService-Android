package com.example.yuvalhoch.android;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private final int REQUEST_PERMISSION = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        if	(ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_PERMISSION);
        } else {
            Button startBtn = (Button) findViewById(R.id.startBtn);
            Button stopBtn = (Button) findViewById(R.id.stopBtn);
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startService(view);
                }
            });
            stopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopService(view);
                }
            });
        }


    }
    /*public void DisplayNotification(View view) {
        final int notify_id = 1;
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i;
                    for(i = 0; i < 100; i+=5) {
                        builder.setProgress(100, i, false);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        int storagePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (storagePermission == PackageManager.PERMISSION_GRANTED) {
            finish();
            startActivity(getIntent());
        } else {
            finish();
        }
    }

    public void startService(View view) {
        Intent intent = new Intent(this, ImageService.class);
        startService(intent);
    }
    public void stopService(View view){
        Intent intent = new Intent(this, ImageService.class);
        stopService(intent);
    }

}
