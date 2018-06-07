package com.boat.app.boatapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rvoor on 15-5-2018.
 */


public class UnlockLock extends AsyncTask<String, Void, String> {

    View view;
    //getting the context
    UnlockLock(View v){this.view = v;}
    protected String doInBackground(String... arg0) {

        Log.d("STRING", arg0[0]);

        String link = " http://vps1.nickforall.nl:6123/packages/" + arg0[0]+ "/unlock";

        Log.d("url" , "url" +  link);
        try {
            URL url = new URL(link);

            //setting up connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //headers
            conn.setRequestProperty("Accept" , "application/json");
            conn.setRequestProperty("Content-Type" , "application/json");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(7000); //set the timeout in milliseconds
            conn.setDoInput(true);
            conn.setDoOutput(true);

            JSONObject postDataParams = new JSONObject();
            //userId of the user

            Log.e("params",postDataParams.toString());
            //stream output
            Log.i("JSON", postDataParams.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(postDataParams.toString());

            os.flush();
            os.close();


            int responseCode=conn.getResponseCode();

            Log.d("BOATAI" , "RESULT:" + conn.getResponseMessage() + conn.getResponseCode() + HttpURLConnection.HTTP_OK);

            //handling the response code from my request
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while(null != (line = in.readLine())) {

                    sb.append(line);
                    break;
                }
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

    }
    //fixing the json so that data gets send to the server
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Snackbar.make(this.view, R.string.locker_text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}