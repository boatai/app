package com.boat.app.boatapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void qrCodePage(View v){

        Intent intent = new Intent(this, Qrcode.class);

        startActivity(intent);
    }

    public void InternetPage(View v){

        Intent intent = new Intent(this, InternetConnection.class);

        startActivity(intent);
    }
}
