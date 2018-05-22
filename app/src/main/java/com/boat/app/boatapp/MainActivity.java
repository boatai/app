package com.boat.app.boatapp;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boat.app.boatapp.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int BARCODE_READER_REQUEST_CODE = 1;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.app_bar_layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);


        // Create the adapter that will return a fragment
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {

            // can't remove onPageScrolled & onPageScrollStateChanged functions
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                double offsetWidth = (positionOffsetPixels + 0.00) / toolbar.getWidth();
                double offsetHeight = offsetWidth * toolbar.getHeight();

                if(offsetHeight <= toolbar.getHeight() && positionOffsetPixels != 0){
                    appBarLayout.setTranslationY((float) -offsetHeight);
                    mViewPager.setTranslationY((float) -offsetHeight);
                }
            }

            // check if second tab is active
            @Override
            public void onPageSelected(int tab) {
                if ( tab != 0 ){
                    appBarLayout.setTranslationY(-toolbar.getHeight());
                    mViewPager.setTranslationY(-toolbar.getHeight());
                }
                else{
                    appBarLayout.setTranslationY(0);
                    mViewPager.setTranslationY(0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}

        });

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    /**
     * Returns the right fragment
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            OverviewFragment overview = new OverviewFragment();
            MapsFragment maps = new MapsFragment();

            switch(position){
                case 0:
                    return overview;

                case 1:
                    return maps;

                default:
//                    Because otherwise android will kill himself
                    return overview;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {

                if (data != null) {
                    String barcode = data.getStringExtra(BarcodeCaptureActivity.BarcodeObject);
                    Toast.makeText(this, barcode,
                            Toast.LENGTH_SHORT).show();

                    new SendPostRequest(this).execute(barcode);

                } else {
                    Toast.makeText(this, "Denk het niet Job",
                            Toast.LENGTH_SHORT).show();

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
