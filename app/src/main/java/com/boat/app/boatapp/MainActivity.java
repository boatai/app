package com.boat.app.boatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.AdapterView;
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
import java.util.Objects;

import static com.boat.app.boatapp.R.layout.activity_internet_connection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    private static final int BARCODE_READER_REQUEST_CODE = 1;

    public customAdapter CustomAdapter;
    ListView packageList;

    List<String> packages = new ArrayList<String>();
    List<String> statusPackages = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //setting starting values
        this.packages.add("Loading...");
        this.statusPackages.add("Loading...");

        //getting data in listview
        this.packageList = findViewById(R.id.listview);
        this.CustomAdapter = new customAdapter(this.packages , this.statusPackages);
        this.packageList.setAdapter(CustomAdapter );

        packageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(getApplicationContext() , detailActivity.class);
                startActivity(intent);

            }
        });
        //getting data from API
        String link = "http://vps1.nickforall.nl:6123/users/5afa9d69b3e20cd5ef85433e/packages";

        HttpData httpData = new HttpData(this , this);
        httpData.execute(link);
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
        //Logging data we get
        Log.d("DATA" , "NAME"+ String.valueOf(name));

        this.CustomAdapter.refreshList(name , status);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {

                if (data != null) {
                    String barcode = data.getStringExtra(BarcodeCaptureActivity.BarcodeObject);
                    Toast.makeText(this, barcode,
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

        List<String> packagesName = new ArrayList<>();
        List<String> packagesStatus = new ArrayList<>();

        public customAdapter (List<String> name , List<String> status){
            packagesStatus = status;
            packagesName = name;
        }
        public void refreshList (List<String> name , List<String> status){
            this.packagesName.clear();
            this.packagesName.addAll(name);

            this.packagesStatus.clear();
            this.packagesStatus.addAll(status);

            Log.d("CustomAdapter" , "Wordt de data geupdate" + name);

            this.notifyDataSetChanged();
        }
        @Override
            public int getCount() {
                return this.packagesName.size();
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

                pacakage_name.setText(this.packagesName.get(position));
                status.setText(this.packagesStatus.get(position));



                return convertView;
        }
    }
}
