package com.boat.app.boatapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rvoor on 12-6-2018.
 */

public class GetMapPoints extends AsyncTask <String , String , String> {

    Context context;
    private WeakReference<MapsFragment> mapsFragmentWeakReference;
    JSONArray routePoints;

    GetMapPoints(Context context , MapsFragment mapsFragment){
        this.context = context;
        this.mapsFragmentWeakReference = new WeakReference<MapsFragment>(mapsFragment);
    }
    @Override
    protected String doInBackground(String... strings) {
        String result = null;

        //making connection
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            //connecting to url
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer data = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                data.append(line);
            }

            String json = data.toString();

            //result before parsing
            Log.d("JSON" , String.valueOf(data));

            result = JSONtoData(json);

            return result;

        } catch (IOException e){
            e.printStackTrace();

            Log.d("ERROR" ,  e.getMessage());
            //if there no is no connection stop the whole connection proces
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected String JSONtoData(String jsonString){

        try {
            JSONObject reader = new JSONObject(jsonString);

            routePoints= reader.getJSONArray("route");

            for (int i = 0 ; i < routePoints.length(); i++) {
                Log.d("JSONARRAY" , routePoints.get(i).toString());
            }
            return  null;

        } catch (JSONException e ){
            e.printStackTrace();

            return "decode Failed";
        }
    }


    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);

        this.mapsFragmentWeakReference.get().getRoutePoints(routePoints);

        Log.d("TAGTAG", "onPostExecute: "  + routePoints);
    }
}