package com.boat.app.boatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        this.packages.add(getResources().getString(R.string.loading));
        this.statusPackages.add(getResources().getString(R.string.loading));

        //getting data in listview
        this.packageList = getView().findViewById(R.id.listview);
        this.CustomAdapter = new customAdapter(this.packages , this.statusPackages, this.lockStatus);
        this.packageList.setAdapter(CustomAdapter );

        packageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView,
                                    int position, long id) {

                if (lockStatus != null) {
                    if (lockStatus[position]) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                R.string.delivered, Toast.LENGTH_SHORT)
                                .show();
                    }else{
                        Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                        intent.putExtra("name", packages.get(position));
                        intent.putExtra("status", statusPackages.get(position));
                        intent.putExtra("lockStatus", lockStatus[position]);
                        startActivity(intent);
                    }
                }
            }
        });
        HttpData httpData = new HttpData(getActivity(), this);
        httpData.execute(GETLINK);
    }

    public void updateList(List<String> name , List<String> status , Boolean lockstatus[]){
        //Logging data we get
        Log.d("DATA" , "NAME"+ String.valueOf(name));

        this.CustomAdapter.refreshList(name , status,  lockstatus);

        this.packages.clear();
        this.packages.addAll(name);

        this.statusPackages.clear();
        this.statusPackages.addAll(status);


        this.lockStatus = new Boolean[0];
        this.lockStatus = lockstatus;

    }

    class customAdapter extends BaseAdapter{

        List<String> packagesName = new ArrayList<>();
        List<String> packagesStatus = new ArrayList<>();
        Boolean lockStatus[];

        public customAdapter (List<String> name , List<String> status, Boolean lock[]){
            packagesStatus = status;
            packagesName = name;
            lockStatus = lock;
        }
        public void refreshList (List<String> name , List<String> status,  Boolean lock[]){
            this.packagesName.clear();
            this.packagesName.addAll(name);

            this.packagesStatus.clear();
            this.packagesStatus.addAll(status);

            this.lockStatus = new Boolean[0];
            this.lockStatus = lock;

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
            return this.packagesName.size();
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            LayoutInflater inflater = getLayoutInflater();

            convertView = inflater.inflate(R.layout.package_listview, null);

            TextView package_name = convertView.findViewById(R.id.tv_name);
            TextView status = convertView.findViewById(R.id.tv_status);

            ImageView status_icon = convertView.findViewById(R.id.ic_status);

            if(this.packagesStatus.get(position).equals("Currently on Boat")){
                status_icon.setImageResource(R.drawable.ic_circle_true);
            }

            package_name.setText(this.packagesName.get(position));
            status.setText(this.packagesStatus.get(position));

            if(this.lockStatus != null) {
                if(this.lockStatus[position]){
                    int textGray = Color.argb(50,0,0, 0);
                    int bgGray = Color.argb(50,0,0, 0);

                    convertView.setBackgroundColor(bgGray);
                    package_name.setTextColor(textGray);
                    status.setTextColor(textGray);

                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);

                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    status_icon.setColorFilter(filter);
                }
            }

            return convertView;
        }
    }
}
