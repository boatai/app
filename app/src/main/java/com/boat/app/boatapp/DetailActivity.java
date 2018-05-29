package com.boat.app.boatapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import static java.math.RoundingMode.DOWN;
import static java.math.RoundingMode.UP;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    private String namePackages;
    private String statusPackages;
    private String sizePackage;
    private String weightPackage;
    private String deliveryDate;
    private Boolean statusLock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        //get data from selected packages
        this.namePackages = intent.getStringExtra("packageName");
        this.statusPackages = intent.getStringExtra("packageStatus");
        this.statusLock = intent.getExtras().getBoolean("lockStatus");
        this.sizePackage = intent.getStringExtra("packageSize");
        this.weightPackage = intent.getStringExtra("packageWeight");
        this.deliveryDate = intent.getStringExtra("deliveryData");

        Log.d("INTENTDATA" , this.namePackages + this.statusPackages + this.statusLock + sizePackage + this.weightPackage + this.deliveryDate);

        //get layout
        coordinatorLayout = findViewById(R.id.cl_layout);

        //map
        MapFragment mapFragment = (MapFragment) this.getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Button is working", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //collabsible toolbar
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        collapsingToolbarLayout.setTitle(this.namePackages);

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
