package com.steven.idlost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView conte,iyandikishe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conte=(TextView) findViewById(R.id.conte);
        iyandikishe=(TextView) findViewById(R.id.iyandikishe);
        conte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(MainActivity.this,Login.class);
                startActivity(login);
                finish();
            }
        });

        iyandikishe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg=new Intent(MainActivity.this,Register.class);
                startActivity(reg);
                finish();
            }
        });
    }
}