package com.boat.app.boatapp;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.icu.text.LocaleDisplayNames;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class OverviewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String GETLINK = "http://vps1.nickforall.nl:6123/users/5b143789b3e20cd5ef854340/packages";

    public customAdapter CustomAdapter;
    ListView packageList;
    SwipeRefreshLayout swipeRefreshLayout;
    List<String> packages = new ArrayList<String>();
    List<String> statusPackages = new ArrayList<String>();
    List<String> packagesSize = new ArrayList<String>();
    List<String> packagesWeight = new ArrayList<String>();
    List<String> deliveryData = new ArrayList<String>();
    List<String> packagesID = new ArrayList<String>();



    Boolean lockStatus[];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //get the id of the refresh layout
        swipeRefreshLayout = getView().findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        //getting data in listview
        this.packageList = getView().findViewById(R.id.listview);
        this.CustomAdapter = new customAdapter(this.packages , this.statusPackages, this.lockStatus);
        this.packageList.setAdapter(CustomAdapter );

        packageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View convertView,
                                    int position, long id) {

                if (lockStatus != null) {
                    if (lockStatus[position]) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                R.string.delivered, Toast.LENGTH_SHORT)
                                .show();
                    }else{
                        Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                        intent.putExtra("id", packagesID.get(position));
                        intent.putExtra("packageName", packages.get(position));
                        intent.putExtra("packageStatus", statusPackages.get(position));
                        intent.putExtra("lockStatus", lockStatus[position]);
                        intent.putExtra("packageSize" , packagesSize.get(position));
                        intent.putExtra("packageWeight" , packagesWeight.get(position));
                        intent.putExtra("deliveryData" , deliveryData.get(position));
                        startActivity(intent);
                    }
                }
            }
        });
        HttpData httpData = new HttpData(getActivity(), this);
        httpData.execute(GETLINK);
        //counter to push notification
        new CountDownTimer(30000 , 1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                notificatonRoute();
            }
        }.start();
    }
    public void notificatonRoute(){
        //TODO als de notificatie wordt gestuurd moeten we het id weten

        //setup intent
        Intent routeIntent = new Intent(getContext().getApplicationContext(), DetailActivity.class);
        routeIntent.putExtra("id", packagesID.get(0));
        routeIntent.putExtra("packageName", packages.get(0));
        routeIntent.putExtra("packageStatus", statusPackages.get(0));
        routeIntent.putExtra("packageSize" , packagesSize.get(0));
        routeIntent.putExtra("packageWeight" , packagesWeight.get(0));
        routeIntent.putExtra("deliveryData" , deliveryData.get(0));

        //put all in an stack builder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity().getApplicationContext());
        stackBuilder.addNextIntentWithParentStack(routeIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        //this notification is for testing purpuse now
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext() , "Notification")
                .setSmallIcon(R.drawable.ic_marker_boat)
                .setContentTitle("Boat is near you")
                .setContentText("The boat is gonna dock at Wijnhaven 101 with " + packages.get(0)  + " onboard!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("The boat is gonna dock at Wijnhaven 101 with " + packages.get(0)  + " onboard!"))
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent)
                .addAction(R.drawable.ic_lock, getString(R.string.route),
                        resultPendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity().getApplicationContext());
        //push notificatoin
        notificationManager.notify(0 , mBuilder.build());
    }


    public void updateList(List<String> name , List<String> status , Boolean lockstatus[] , List<String> size, List<String> weight , List<String> date , List<String>id){
        //Logging data we get
        Log.d("DATA" , "NAME"+ String.valueOf(name));

        this.CustomAdapter.refreshList(name , status,  lockstatus );

        this.packages.clear();
        this.packages.addAll(name);
        this.statusPackages.clear();
        this.statusPackages.addAll(status);

        this.lockStatus = new Boolean[0];
        this.lockStatus = lockstatus;

        this.packagesSize.addAll(size);
        this.packagesWeight.addAll(weight);
        this.deliveryData.addAll(date);
        this.packagesID.addAll(id);

        swipeRefreshLayout.setRefreshing(false);

        //this is for the test when data is updated user gets update

    }

    @Override
    public void onRefresh() {
        HttpData httpData = new HttpData(getActivity(), this);
        httpData.execute(GETLINK);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
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
        public int getCount() {return this.packagesName.size();}

        @Override
        public Object getItem(int position) {
            return this.packagesName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
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
            try {
                if (this.lockStatus != null) {
                    if (this.lockStatus[position]) {
                        int textGray = Color.argb(50, 0, 0, 0);
                        int bgGray = Color.argb(50, 0, 0, 0);

                        convertView.setBackgroundColor(bgGray);
                        package_name.setTextColor(textGray);
                        status.setTextColor(textGray);

                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);

                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        status_icon.setColorFilter(filter);
                    }
                }
            }catch(Exception e){
                Log.d("ERRORBOOLEANS" , String.valueOf(e));
            }

            return convertView;
        }
    }
}
