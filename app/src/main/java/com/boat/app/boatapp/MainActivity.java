package com.boat.app.boatapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_fedex);
        actionBar.setDisplayUseLogoEnabled(true);
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
