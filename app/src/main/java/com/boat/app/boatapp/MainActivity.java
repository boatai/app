package com.boat.app.boatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boat.app.boatapp.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.boat.app.boatapp.R.layout.activity_internet_connection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int BARCODE_READER_REQUEST_CODE = 1;

    public ListView packagesList;
    customAdapter customAdapter;

    String[] packages =null;
    String[] statusPackages = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        //setting up the custom layout of for the listview
        String link = "http://vps1.nickforall.nl:6123/packages";

        new HttpData().execute(link);

        if (packages == null){
            Toast.makeText(this, "Loading....",
                    Toast.LENGTH_SHORT).show();
        }else{
            Log.d("DATA" , "DATA IS ER NOG NIET");

            Toast.makeText(this, "Done",
                    Toast.LENGTH_SHORT).show();
        }

//        packagesList = findViewById(R.id.listview);
////
//        customAdapter = new customAdapter();
//
//        packagesList.setAdapter(customAdapter);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);

                break;
        }
    }
    public void updateList(List<String> name , List<String> status){
        packages = name.toArray(new String[name.size()]);

        statusPackages = status.toArray(new String[status.size()]);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {

                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Toast.makeText(this, barcode.displayValue,
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Denk het niet Job",
                            Toast.LENGTH_SHORT).show();

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class customAdapter extends BaseAdapter{
        @Override
            public int getCount() {
                return packages.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @SuppressLint("ViewHolder")
            @Override
            public View getView(int position, View convertView, ViewGroup arg2) {

                LayoutInflater inflater = getLayoutInflater();

                convertView = inflater.inflate(R.layout.package_listview, null);

                TextView pacakage_name = convertView.findViewById(R.id.tV_name);
                TextView status = convertView.findViewById(R.id.tv_status);

                pacakage_name.setText(packages[position]);
                status.setText(statusPackages[position]);

                return convertView;
        }
    }
}
