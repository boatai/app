package com.boat.app.boatapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rvoor on 3-4-2018.
 */

public class HttpData extends AsyncTask<String, String, String> {

    protected String doInBackground(String... params) {

        //making connection
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            //connecting to url
            URL url = new URL(params[0]);
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

            String result = JSONtoData(json);
            //result
            Log.d("JSON RESPONSE" , result);

            return result;

        } catch (IOException e){
            e.printStackTrace();

            Log.d(InternetConnection.LOG_TAG ,  e.getMessage());
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

        String name = null;

        try {
            JSONObject reader = new JSONObject(jsonString);

            JSONArray p = reader.getJSONArray("packages");

            Log.d("JSON LENGTH" , String.valueOf(p.length()));

            for (int i = 0 ; i < p.length(); i++) {

                JSONObject jsonObject = p.getJSONObject(i);

                name = jsonObject.getString("name");

                Log.d("JSON NAME", name);
            }

            return name;

        } catch (JSONException e ){
            e.printStackTrace();

            return "decode Failed";
        }
    }
}
