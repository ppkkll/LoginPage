package com.fantasy_travel.loginpage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import android.annotation.SuppressLint;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Maps extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private Double startLongitude;
    private  Double startLatitude;
    private  Double endLongitude;
    private  Double endLatitude;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private Marker destinationMarker;
    private static final int request_user_location_code = 99;
    private double start_latitude, start_longitude;
    private double dest_latitude, dest_longitude;
    public String distance, duration;
    private LatLng latLng;
    public PolylineOptions polylineOptions1;
    private Switch mode, sex;
    private String mode_value, sex_value;
    public String server3="";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //public
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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
                                Intent intent_Account = new Intent( Maps.this, AccountActivity.class);
                                startActivity(intent_Account);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Daily_Commute:
                                Intent intent_DC = new Intent( Maps.this, Maps.class);
                                startActivity(intent_DC);
                                Toast.makeText(getApplicationContext(),"DailyCommute",Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Find_Fellow_Traveller:
                                Intent intent_FFT = new Intent( Maps.this, Maps.class);
                                startActivity(intent_FFT);
                                Toast.makeText(getApplicationContext(),"FindFellowTraveller",Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Setting:
                                Toast.makeText(getApplicationContext(),"Setting",Toast.LENGTH_SHORT).show();
                                Intent intent_Setting = new Intent( Maps.this, AccountActivity.class);
                                startActivity(intent_Setting);
                                break;
                            case R.id.nav_LogOut:
                                Toast.makeText(getApplicationContext(),"LouOut",Toast.LENGTH_SHORT).show();
                                finish();
                        }


                        return true;
                    }
                });



        mode = (Switch) findViewById(R.id.switch1);
       sex = (Switch) findViewById(R.id.switch2);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    public void onClick(View v)
    {

        Object dataTransfer[] = new Object[2];
        EditText addressField = (EditText) findViewById(R.id.location_search);
        String address = addressField.getText().toString();

        List<Address> addressList = null;
        MarkerOptions userMarkerOptions = new MarkerOptions();

        switch (v.getId())
        {
            case R.id.search_destination :
                /*EditText addressField = (EditText) findViewById(R.id.location_search);
                String address = addressField.getText().toString();

                List<Address> addressList = null;
                MarkerOptions userMarkerOptions = new MarkerOptions();*/
                if(!TextUtils.isEmpty(address))
                {
                    Geocoder geocoder = new Geocoder(this);
                    try
                    {
                        addressList = geocoder.getFromLocationName(address, 6);
                        if(addressList!=null)
                        {
                            for(int i = 0;i<addressList.size();i++)
                            {
                                Address userAddress = addressList.get(i);
                                dest_latitude = userAddress.getLatitude();
                                dest_longitude = userAddress.getLongitude();
                                LatLng latLng = new LatLng(dest_latitude,dest_longitude);
                                userMarkerOptions.position(latLng);
                                Log.d("Backend","dest_latitude"+dest_latitude);
                                Log.d("Backend","dest_longitude"+dest_longitude);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                if(destinationMarker!=null)
                                {
                                    destinationMarker.remove();
                                }
                                float results[] = new float[10];
                                Location.distanceBetween(start_latitude,start_longitude,dest_latitude,dest_longitude,results);
                                userMarkerOptions.snippet("Distance = "+results[0]);
                                destinationMarker = mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        }
                        else
                        {
                            Toast.makeText(this, "Location Not Found !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(this,"No destination entered !!!",Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.Search_button :
                //Toast.makeText(this,"No functionality currently !!!",Toast.LENGTH_SHORT).show();
               /* dataTransfer = new Object[3];
                String url = getDirectionsUrl();
                GetDirectionsData getDirectionsData = new GetDirectionsData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                dataTransfer[2] = new LatLng(dest_latitude, dest_longitude);
                getDirectionsData.execute(dataTransfer); */

                if(!TextUtils.isEmpty(address))
                {
                    sex_value=sex.getTextOn().toString();
                    if (mode.isChecked())
                    {
                        mode_value = mode.getTextOn().toString();
                        Log.d("backend", "mode :"+mode_value);
                    }
                    else
                    {
                        mode_value = mode.getTextOff().toString();
                        Log.d("backend", "mode :"+mode_value);
                    }

                    new CallForDis().execute();
                    Toast.makeText(this, "processing !!!", Toast.LENGTH_SHORT).show();
                    try {

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(dest_latitude, dest_longitude));
                    //markerOptions.draggable(true);
                    // float results[] = new float[10];

                    SharedPreferences preferences =
                            getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);
                    String duration1= "";
                    duration1=      preferences.getString("duration",duration1);
                    String distance1="";
                    distance1=  preferences.getString("distance",distance1);
                    Log.d("backend", "duration23 :"+duration1);
                    Log.d("backend", "distance23 :"+distance1);
                    String s1= "Duration="+String.valueOf(duration1);
                    String s2= "Distance="+String.valueOf(distance1);
                    Log.d("backend", "duration :"+s1);
                    Log.d("backend", "distance :"+s2);
                    markerOptions.title(s1);
                    markerOptions.snippet(s2);
                    mMap.addMarker(markerOptions);
                    if(mode_value.equals("walking")){
                        polylineOptions1.color(Color.BLUE);
                    }
                    else{
                        polylineOptions1.color(Color.RED);
                    }
                    mMap.addPolyline(polylineOptions1);
                }
                else
                {
                    Toast.makeText(this,"No destination entered !!!",Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.Search_button1 :

                new CallForSettingLocation().execute();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


        }
    }

    /*private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+start_latitude+","+start_longitude);
        googleDirectionsUrl.append("&destination="+dest_latitude+","+dest_longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyAj76-gXJ5gvXrjp12YlLA90xEokK7O234");

        return googleDirectionsUrl.toString();
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_user_location_code);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_user_location_code);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case request_user_location_code :
                if(grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied !!!", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location)
    {
        lastLocation = location;
        if(currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }

        start_latitude = location.getLatitude();
        start_longitude = location.getLongitude();

        LatLng latLng = new LatLng(start_latitude, start_longitude);
        Log.d("Backend","start_latitude"+start_latitude);
        Log.d("Backend","start_longitude"+start_latitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Locations");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentUserLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class CallForDis extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            try
            {
                String URL1="https://maps.googleapis.com/maps/api/directions/json?origin="+start_latitude+","+start_longitude+"&destination="+dest_latitude+","+dest_longitude+"&mode="+mode_value+"&key=AIzaSyAj76-gXJ5gvXrjp12YlLA90xEokK7O234";

                URL url = new URL(URL1);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                // conn.setRequestProperty("Accept", "application/json");
                Log.d("Backend", "request posted successfully");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String out="";
                String output;
                //  Log.d("Output from Server .... \n","new");
                while ((output = br.readLine()) != null) {
                    Log.d("Backend", "output"+output);
                   out=out+output;
                }

                Log.d("Backend", "out"+out);
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
        public void onPostExecute(String s){
            String[] directionsList;
            Dataparser parser = new Dataparser();
            PolylineOptions polylineOptions = new PolylineOptions();
            Log.d("backend","printing s"+ s);
            directionsList = parser.parseDirections(s);
            Log.d("backend", directionsList.toString());
            int count = directionsList.length;
            for(int i=0; i<count; i++)
            {
                Log.d("backend","in polyline"+ count);
                Log.d("backend","in polyline"+ directionsList[i]);
                //PolylineOptions polylineOptions1 = new PolylineOptions();

                polylineOptions.width(10);
                polylineOptions.addAll(PolyUtil.decode(directionsList[i]));
                Log.d("backend","about to add polyline"+ directionsList[i]);
                //mMap.addPolyline(polylineOptions);
                Log.d("backend","added the polyline in map");
            }

            polylineOptions1 = polylineOptions;
            /*duration = directionsList.get("duration");
            distance = directionsList.get("distance");
            Log.d("backend", "duration"+duration);
            Log.d("backend", "distance"+distance);
            SharedPreferences preferences =
                    getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);
            preferences.edit().putString("duration",duration ).commit();
            preferences.edit().putString("distance",distance ).commit();*/
        }

       /* public void displayDirections(String[] directionsList)
        {

        }*/



        /*protected void Abc(String s, GoogleMap mMap1 ){
            HashMap<String, String> directionsList = new HashMap<>();
            Dataparser parser = new Dataparser();

            Log.d("backend","printing s"+ s);
            directionsList = parser.parseDirections(s);
            Log.d("backend", directionsList.toString());
            duration = directionsList.get("duration");
            distance = directionsList.get("distance");

            mMap1.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            //markerOptions.draggable(true);
            markerOptions.title("Duration : "+duration);
            markerOptions.snippet("Distance : "+distance);
            mMap1.addMarker(markerOptions);*/
        }

    class CallForSettingLocation extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                SharedPreferences preferences =
                        getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);


                String    emailID=  "";
                emailID=     preferences.getString("emailID",emailID);
               // String URL1="http://load1-467103352.eu-west-1.elb.amazonaws.com:8080/InsertLocData?id="+emailID+"&startLong="+start_longitude+"&endLong="+dest_longitude+"&startLat="+start_latitude+"&endLat="+dest_latitude;
                String URL1="http://load1-467103352.eu-west-1.elb.amazonaws.com:8080/InsertLocData?id="+emailID+"&startLong="+start_longitude+"&endLong="+dest_longitude+"&startLat="+start_latitude+"&endLat="+dest_latitude+"&preferedMode="+mode_value+"&preferedSex="+sex_value;

                Log.d("Backend",URL1);
                URL url = new URL(URL1);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                // conn.setRequestProperty("Accept", "application/json");
                Log.d("Backend", "request posted successfully");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());

                }
                else
                {
                    new CallForFindTraveller().execute();
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                //  Log.d("Output from Server .... \n","new");
                while ((output = br.readLine()) != null) {
                    Log.d("Backend", output);

                }


                conn.disconnect();

            } catch (Exception e) {


                Log.d("Backend", "exception in HTTP");
                e.printStackTrace();

            }

            return null;
        }
    }



    class CallForFindTraveller extends AsyncTask {
        String output1="";
        @Override
        protected Object doInBackground(Object[] objects) {
int count=0;
            try {
                SharedPreferences preferences =
                        getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);


                String    emailID=  "";
                emailID=     preferences.getString("emailID",emailID);
               // String URL1="http://load1-467103352.eu-west-1.elb.amazonaws.com:8083/checkForGroup?id="+emailID;
                String URL1="http://load1-467103352.eu-west-1.elb.amazonaws.com:8083/checkForGroup?id="+emailID;

                Log.d("Backend",URL1);

                URL url = new URL(URL1);
                while (true) {
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

                    String output;

                    //  Log.d("Output from Server .... \n","new");
                    while ((output = br.readLine()) != null) {
                        Log.d("Backend", output);
                        output1=output1+output;

                    }
                    if(output1.contains("Successfull"))
                    {
                        Maps.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Maps.this, "user Present", Toast.LENGTH_SHORT).show();
                                Object obj;

                                try{
                                       obj = new JSONParser().parse(output1);
                                    JSONObject jo = (JSONObject) obj;
                                    String id=(String)jo.get("id");
                                    String responseCode=(String)jo.get("responseCode");
                                    String responseMessage=(String)jo.get("responseMessage");
                                    JSONArray ja = (JSONArray) jo.get("resultSimilarUsers");

                                    Iterator itr2 = ja.iterator();
                                    int i=1;
                                    while (itr2.hasNext())
                                    {
                                        //   Iterator itr1 = ( (HashMap) itr2.next()).entrySet().iterator();
                                        JSONObject jo1 = (JSONObject) itr2.next();
                                        String userId=(String)jo1.get("id");
                                         startLongitude=(Double)jo1.get("startLongitude");
                                         startLatitude=(Double)jo1.get("startLatitude");
                                         endLongitude=(Double)jo1.get("endLongitude");
                                         endLatitude=(Double)jo1.get("endLatitude");
                                        Log.d("Backend"," User "+i+" userId " + userId+" startLatitude "+startLatitude+" startLongitude "+startLongitude+" endLatitude "+endLatitude+" endLongitude "+endLongitude);
                                        Maps.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                MarkerOptions markerOptions = new MarkerOptions();
                                                markerOptions.position(new LatLng(startLatitude, startLongitude));
                                                mMap.addMarker(markerOptions);
                                                mMap.animateCamera(CameraUpdateFactory.zoomBy(14));
                                            }
                                        });
i++;
                                    }
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else
                    {
                        Maps.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Maps.this, "user Not Present", Toast.LENGTH_SHORT).show();

                            }
                        });
                        count++;
                    }
                    conn.disconnect();
                    Log.d("Backend", "Thread sleeping");
                    Thread.sleep(10000);


                    if(count==10){
                        return null;
                    }
                }


            } catch (Exception e) {


                Log.d("Backend", "exception in HTTP");
                e.printStackTrace();

            }

            return null;
        }
    }


    }

