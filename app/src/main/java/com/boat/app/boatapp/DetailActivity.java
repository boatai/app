package com.boat.app.boatapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.nio.channels.Channel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.math.RoundingMode.DOWN;
import static java.math.RoundingMode.UP;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    private String packageID;
    private String namePackages;
    private String statusPackages;
    private String sizePackage;
    private String weightPackage;
    private String deliveryDate;
    private Boolean statusLock;
    private Toolbar toolbar;

    private FloatingActionButton fab;


    @Override
    protected void onStart() {
        super.onStart();
        //collabsible toolbar
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(this.namePackages);
        //setting text size
        TextView size = findViewById(R.id.packageSize_tv);
        size.setText(this.sizePackage);
        //setting text weight
        TextView weight = findViewById(R.id.packageWeight_tv);
        weight.setText(this.weightPackage);
        //setting text status
        TextView status = findViewById(R.id.packageStatus_tv);
        status.setText(this.statusPackages);
        //setting delivery date
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss:SSS'Z'" , Locale.FRENCH);
        try {
            Date newDate = spf.parse(this.deliveryDate);
            spf = new SimpleDateFormat("MM-dd" , Locale.FRENCH);

            String date = spf.format(newDate);
            Log.d("DATUMIS" , date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("error" , String.valueOf(e));
        }
        TextView date = findViewById(R.id.packageDate_tv);
        date.setText(this.deliveryDate);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        //get data from selected packages
        this.packageID = intent.getStringExtra("id");
        this.namePackages = intent.getStringExtra("packageName");
        this.statusPackages = intent.getStringExtra("packageStatus");
        this.statusLock = intent.getExtras().getBoolean("lockStatus");
        this.sizePackage = intent.getStringExtra("packageSize");
        this.weightPackage = intent.getStringExtra("packageWeight");
        this.deliveryDate = intent.getStringExtra("deliveryData");

        Log.d("INTENTDATA" , this.namePackages + this.statusPackages + this.statusLock + sizePackage + this.weightPackage + this.deliveryDate + this.packageID);

        //get layout
        coordinatorLayout = findViewById(R.id.cl_layout);

        //map
        MapFragment mapFragment = (MapFragment) this.getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UnlockLock(view).execute(packageID);
            }
        });

        // change FloatingActionButton to a normal button in the toolbar
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
                float scrolling = (appBarLayout.getY() + appBarLayout.getTotalScrollRange());
                if(scrolling <= 0){
                    fab.animate().translationY(-(toolbar.getHeight() / 2)).translationX(50);
                    fab.setCompatElevation(12);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                }else{
                    fab.animate().translationY(0).translationX(0);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng amsterdam = new LatLng(52.36848, 4.894690);
//        mMap.addMarker(new MarkerOptions().position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(amsterdam));
        mMap.setMinZoomPreference(14);
        mMap.setMaxZoomPreference(14);
    }
}
