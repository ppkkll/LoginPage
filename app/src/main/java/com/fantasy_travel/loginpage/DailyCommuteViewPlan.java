package com.fantasy_travel.loginpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DailyCommuteViewPlan extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    private DrawerLayout mDrawerLayout;

    List<PlanModel> planModelList;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_commute_my_plans);

        mDrawerLayout = findViewById(R.id.Map_Drawer);
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        int id = menuItem.getItemId();

                        switch (id){
                            case R.id.nav_account:
                                Toast.makeText(getApplicationContext(),"Account",Toast.LENGTH_SHORT).show();
                                Intent intent_Account = new Intent( DailyCommuteViewPlan.this, AccountActivity.class);
                                startActivity(intent_Account);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Daily_Commute:
                                Intent intent_DC = new Intent( DailyCommuteViewPlan.this, DailyCommuteViewPlan.class);
                                startActivity(intent_DC);
                                Toast.makeText(getApplicationContext(),"DailyCommute",Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Find_Fellow_Traveller:
                                Intent intent_FFT = new Intent( DailyCommuteViewPlan.this, Maps.class);
                                startActivity(intent_FFT);
                                Toast.makeText(getApplicationContext(),"FindFellowTraveller",Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Setting:
                                Toast.makeText(getApplicationContext(),"Setting",Toast.LENGTH_SHORT).show();
                                Intent intent_Setting = new Intent( DailyCommuteViewPlan.this, AccountActivity.class);
                                startActivity(intent_Setting);
                                break;
                            case R.id.nav_LogOut:
                                Toast.makeText(getApplicationContext(),"LouOut",Toast.LENGTH_SHORT).show();
                                finish();
                        }
                        return true;
                    }
                });
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        planModelList = new ArrayList<>();
        new fetchingData().execute();
        SharedPreferences preferences =
                getSharedPreferences("com.fantasy_travel.loginpage", Context.MODE_PRIVATE);
        String duration1= "";
        duration1 = preferences.getString("JSON_plan",duration1);
        Log.d("Backend", "JSONresponse"+duration1);




        Object obj = null;
        try {
            obj = new JSONParser().parse(duration1);

        org.json.simple.JSONObject jo = (org.json.simple.JSONObject) obj;
        //String id=(String)jo.get("id");
        String responseCode=(String)jo.get("responseCode");
        String responseMessage=(String)jo.get("responseMessage");
        org.json.simple.JSONArray ja = (JSONArray) jo.get("myplans");


        Iterator itr2 = ja.iterator();
        int i=1;
        while (itr2.hasNext())
        {
            //   Iterator itr1 = ( (HashMap) itr2.next()).entrySet().iterator();
            org.json.simple.JSONObject jo1 = (JSONObject) itr2.next();
            String id=(String)jo1.get("id");
            String time =(String)jo1.get("time");
            String startLong=(String)jo1.get("startLong");
            String startLat=(String)jo1.get("startLat");
            String endLong =(String)jo1.get("endLong");
            String endLat=(String)jo1.get("endLat");
            String preferedMode=(String)jo1.get("preferedMode");
            String preferedSex =(String)jo1.get("preferedSex");
            String myPlanName=(String)jo1.get("myPlanName");
            String startLoc=(String)jo1.get("startLoc");
            String endLoc =(String)jo1.get("endLoc");
            planModelList.add(new PlanModel(id, myPlanName, startLoc, endLoc, time, preferedMode, preferedSex));

            //Log.d("Backend"," User "+i+" userId " + userId+" startLatitude "+startLatitude+" startLongitude "+startLongitude+" endLatitude "+endLatitude+" endLongitude "+endLongitude);

        }
        } catch (ParseException e) {
            e.printStackTrace();
        }




/*
       planModelList.add(new PlanModel("abc.com", "Plan 1", "Trinity", "Airport", "10:00AM", "Walking", "Male"));
        planModelList.add(new PlanModel("abc.com", "Plan 2", "Trinity", "Derry", "11:00AM", "Driving", "Female"));
        planModelList.add(new PlanModel("abc.com", "Plan 3", "Trinity", "Galway", "08:00PM", "Driving", "Female"));
        planModelList.add(new PlanModel("abc.com", "Plan 4", "Trinity", "Drumcondra", "09:00AM", "Walking", "Male"));
        planModelList.add(new PlanModel("abc.com", "Plan 5", "Trinity", "Kerry", "10:00PM", "Driving", "Female"));
*/

        recyclerViewAdapter = new RecyclerViewAdapter(DailyCommuteViewPlan.this, planModelList);
        recyclerView.setAdapter(recyclerViewAdapter);

        FloatingActionButton addButton = findViewById(R.id.floatingActionButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyCommuteViewPlan.this, DailyCommuteCreatePlan.class);
                startActivity(intent);
            }
        });

        FloatingActionButton refreshButton = findViewById(R.id.floatingActionButton1);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyCommuteViewPlan.this, DailyCommuteViewPlan.class);
                startActivity(intent);
                //Toast.makeText(DailyCommuteViewPlan.this, "Nothing to display !!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class fetchingData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            try {

                SharedPreferences preferences1 =
                        getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);
                username = preferences1.getString("emailID",username);
                Log.d("Backend", "username"+username);
                String URL1 = Misc.Url1+"/FindMyPlan?id="+username;
                Log.d("Backend", "request URL"+URL1);

                URL url = new URL(URL1);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                // conn.setRequestProperty("Accept", "application/json");
                Log.d("Backend", "request posted successfully");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String out = "";
                String output;
                //  Log.d("Output from Server .... \n","new");
                while ((output = br.readLine()) != null) {
                    Log.d("Backend", "output" + output);
                    out = out + output;
                }

                Log.d("Backend", "out" + out);
                //Abc(out,mMap);
                onPostExecute(out);

                conn.disconnect();

            } catch (Exception e) {


                Log.d("Backend", "exception in HTTP");
                e.printStackTrace();

            }

            return null;
        }

        //@Override
        public void onPostExecute(String s) {

            SharedPreferences preferences =
                    getSharedPreferences("com.fantasy_travel.loginpage", Context.MODE_PRIVATE);
            preferences.edit().putString("JSON_plan",s ).commit();

        }
    }
}