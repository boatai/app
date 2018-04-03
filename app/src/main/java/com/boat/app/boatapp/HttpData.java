package com.boat.app.boatapp;

import android.os.AsyncTask;
import android.util.Log;

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

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                Log.d("Response: ",  line); //here u ll get whole response...... :-)

            }
            //result
            Log.d("JSON RESPONSE" , String.valueOf(buffer));

            String result = buffer.toString();

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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d("Result" , result);
    }
}
