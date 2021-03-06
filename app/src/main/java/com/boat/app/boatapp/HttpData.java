package com.boat.app.boatapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by rvoor on 3-4-2018.
 */

public class HttpData extends AsyncTask<String, String, String> {

    List<String> packagesID = new ArrayList<String>();
    List<String> name_array = new ArrayList<String>();
    List<String> status_array = new ArrayList<String>();
    List<String> packagesSize = new ArrayList<String>();
    List<String> packagesWeight = new ArrayList<String>();
    List<String> deliveryData = new ArrayList<String>();


    Boolean lock[];


    private Context context;
    private WeakReference<OverviewFragment> overviewActivityWeakReference;

    String name;
    String status;
    String size;
    String weight;
    String date;
    String id;

    public HttpData(Context context , OverviewFragment overviewFragment){
        this.context = context;
        this.overviewActivityWeakReference = new WeakReference<OverviewFragment>(overviewFragment);
    }

    protected String doInBackground(String... params) {
        String result = null;

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

            JSONArray p = reader.getJSONArray("packages");

            lock = new Boolean[p.length()];

            for (int i = 0 ; i < p.length(); i++) {

                JSONObject jsonObject = p.getJSONObject(i);
                id = jsonObject.getString("_id");
                name = jsonObject.getString("name");
                status = jsonObject.getString("status");
                lock[i] = Boolean.valueOf(jsonObject.getString("unlocked"));
                size = jsonObject.getString("size");
                weight = jsonObject.getString("weight");
                date = jsonObject.getString("deliveryDate");

                packagesID.add(id);
                status_array.add(status);
                name_array.add(name);
                packagesSize.add(size);
                packagesWeight.add(weight);
                deliveryData.add(date);

                Log.d("JSON ARRAY", String.valueOf(packagesID));
            }

            return name;

        } catch (JSONException e ){
            e.printStackTrace();

            return "decode Failed";
        }
    }

    @Override
    protected void  onPostExecute(String s) {
        super.onPostExecute(s);

        this.overviewActivityWeakReference.get().updateList(name_array , status_array , lock , packagesSize , packagesWeight , deliveryData , packagesID);

//        Toast.makeText(this.context, "Loading is Done",
//                Toast.LENGTH_LONG).show();

    }
    //TODO Make function for detail page
}
