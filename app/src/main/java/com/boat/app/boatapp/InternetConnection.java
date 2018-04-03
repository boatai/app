package com.boat.app.boatapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class InternetConnection extends AppCompatActivity {

    public static final String LOG_TAG = "Link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_connection);
    }
    public void TestInternet(View v) throws ExecutionException, InterruptedException {
        Log.d("test", "Button is working");

        String link =  "http://vps1.nickforall.nl:6123/packages";

        new HttpData().execute(link);

    }
    public void updateData(Data d) {
        ((TextView)findViewById(R.id.textView_data)).setText(d.getName());
//        ((TextView)findViewById(R.id.textView_heroDescription)).setText(d.getDescription());
    }
}

