package com.fantasy_travel.loginpage;

import android.Manifest;
import android.content.Context;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

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
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Maps extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

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
    //public

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mode = (Switch) findViewById(R.id.switch1);
       // sex = (Switch) findViewById(R.id.switch2);

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
                Toast.makeText(this, "Nothing to process right now !!!", Toast.LENGTH_SHORT).show();

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
    }

