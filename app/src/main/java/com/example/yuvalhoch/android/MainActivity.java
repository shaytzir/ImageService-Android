package com.example.yuvalhoch.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startBtn = (Button)findViewById(R.id.startBtn);
        Button stopBtn = (Button)findViewById(R.id.stopBtn);
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
    public void startService(View view) {
        Intent intent = new Intent(this, ImageService.class);
        startService(intent);
    }
    public void stopService(View view){
        Intent intent = new Intent(this, ImageService.class);
        stopService(intent);
    }

}
