package com.boat.app.boatapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by rvoor on 3-4-2018.
 */

public class GetData extends AsyncTask<String, Void, Data> {



    private InternetConnection mCallbackMain;

    GetData(InternetConnection callback) {
        setCallback(callback);
    }

    void setCallback(InternetConnection callback) {
        mCallbackMain = callback;
    }


    @Override
    //proces in background
    protected Data doInBackground(String... links) {
        Data result = null;
        //looking if link is full
        if (links !=null && links.length > 0){

            String linkAPi = links[0];

            Log.d(InternetConnection.LOG_TAG, linkAPi);

            try{

                URL url = new URL(linkAPi);

                Log.d(InternetConnection.LOG_TAG, linkAPi);


                String resultString = downloadUrl(url);


                if (resultString != null) {
                    Log.d(InternetConnection.LOG_TAG , "JSON" + resultString);

//                    result = JSONStringToData(resultString);

//                    result = JSONStringToString(resultString);
                }else{
                    throw new IOException("No response Recieved");

                }
            }catch(Exception e) {
                result = new Data("Error", "No superpowers can defeat an IOException");

                Log.d(InternetConnection.LOG_TAG , e.getMessage());
            }
        }
        return null;
    }

    protected void onPostsExecute(Data result) {
        if (result != null && mCallbackMain != null) {

            mCallbackMain.updateData(result);
        }
    }

    private String downloadUrl(URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();

            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream, 50000);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;

    }
    public String readStream(InputStream stream, int maxReadSize)
            throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] rawBuffer = new char[maxReadSize];
        int readSize;
        StringBuffer buffer = new StringBuffer();
        while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
            if (readSize > maxReadSize) {
                readSize = maxReadSize;
            }
            buffer.append(rawBuffer, 0, readSize);
            maxReadSize -= readSize;
        }
        return buffer.toString();
    }
    private Data JSONStringToData(String jsonString) {
        try {
            JSONObject reader = new JSONObject(jsonString);

            // data/results[0]/name + description

            JSONObject d = (JSONObject)((reader.getJSONObject("items")).getJSONArray("results")).get(0);

            return new Data(d.getString("name"), d.getString("description"));

        } catch (JSONException e) {
            e.printStackTrace();

            Log.d(InternetConnection.LOG_TAG, "JSON decode failed");

            return new Data("JSON", "JSON exceptions are my kryptonite");
        }

    }

}
