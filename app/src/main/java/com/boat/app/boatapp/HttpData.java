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

             //TODO: send data back and put in listview!

            return result;

        } catch (IOException e){
            e.printStackTrace();

            Log.d(InternetConnection.LOG_TAG ,  e.getMessage());
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

    public String JSONtoData(String jsonString){
        try {
            JSONObject reader = new JSONObject(jsonString);

            JSONObject p = (JSONObject) ((reader.getJSONArray("packages"))).get(0);

            return p.getString("name");
        } catch (JSONException e ){
            e.printStackTrace();

            Log.d("JSON", "DECODE FAILED");

            return "decode Failed";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d("Result" , result);
    }
}
