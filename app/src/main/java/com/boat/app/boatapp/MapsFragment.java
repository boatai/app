package com.boat.app.boatapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public JSONArray routePoints;
    private PolylineOptions line = new PolylineOptions();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new GetMapPoints(getActivity() , this).execute("http://vps1.nickforall.nl:6123/route");

        return rootView;
    }
    public void getRoutePoints (JSONArray routePoints)  {
        this.routePoints = routePoints;

        for(int i = 0; i < this.routePoints.length(); i++){
            try {
                JSONArray singlePoint = (JSONArray) this.routePoints.get(i);
                String name = singlePoint.getString(0);
                double lat = (double) singlePoint.get(2);
                double lon = (double) singlePoint.get(3);

                if(i == 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title("Bas Boot")
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker_boat)));
                }else{
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title(name)
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker_pickup)));
                }


                line.add(new LatLng(lat, lon))
                                .width(5).color(R.color.colorPrimary);
                if(i + 1 == this.routePoints.length()){
                    mMap.addPolyline(line);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("ERRORJSON" , String.valueOf(e));
            }

        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.maps_style));
            if (!success) {
                Log.e("ERROR", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("ERROR", "Can't find style. Error: ", e);
        }

        mMap = googleMap;
        mMap.setMinZoomPreference(14);
        mMap.setMaxZoomPreference(14);
    }
}
