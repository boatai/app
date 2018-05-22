package com.boat.app.boatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OverviewFragment extends Fragment {
    private static final String GETLINK = "http://vps1.nickforall.nl:6123/users/5afa9d69b3e20cd5ef85433e/packages";

    public customAdapter CustomAdapter;
    ListView packageList;

    List<String> packages = new ArrayList<String>();
    List<String> statusPackages = new ArrayList<String>();
    Boolean lockStatus[];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //setting starting values
        this.packages.add("Loading...");
        this.statusPackages.add("Loading...");

        //getting data in listview
        this.packageList = getView().findViewById(R.id.listview);
        this.CustomAdapter = new customAdapter(this.packages , this.statusPackages);
        this.packageList.setAdapter(CustomAdapter );

        packageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(getActivity().getApplicationContext() , DetailActivity.class);
                intent.putStringArrayListExtra("name" , (ArrayList<String>) packages);
                intent.putStringArrayListExtra("status" , (ArrayList<String>) statusPackages);
                intent.putExtra("lockStatus" , lockStatus);
                startActivity(intent);
            }
        });
        HttpData httpData = new HttpData(getActivity(), this);
        httpData.execute(GETLINK);
    }

    public void updateList(List<String> name , List<String> status , Boolean lockstatus[]){
        //Logging data we get
        Log.d("DATA" , "NAME"+ String.valueOf(name));

        this.CustomAdapter.refreshList(name , status);

        this.packages.clear();
        this.packages.addAll(name);

        this.statusPackages.clear();
        this.statusPackages.addAll(status);

        this.lockStatus = lockstatus;

    }

    class customAdapter extends BaseAdapter{

        List<String> packagesName = new ArrayList<>();
        List<String> packagesStatus = new ArrayList<>();

        public customAdapter (List<String> name , List<String> status){
            packagesStatus = status;
            packagesName = name;
        }
        public void refreshList (List<String> name , List<String> status){
            this.packagesName.clear();
            this.packagesName.addAll(name);

            this.packagesStatus.clear();
            this.packagesStatus.addAll(status);

            Log.d("CustomAdapter" , "Wordt de data geupdate" + name);

            this.notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return this.packagesName.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            LayoutInflater inflater = getLayoutInflater();

            convertView = inflater.inflate(R.layout.package_listview, null);

            TextView pacakage_name = convertView.findViewById(R.id.tV_name);
            TextView status = convertView.findViewById(R.id.tv_status);

            pacakage_name.setText(this.packagesName.get(position));
            status.setText(this.packagesStatus.get(position));



            return convertView;
        }
    }
}
