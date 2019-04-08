package com.fantasy_travel.loginpage;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccountActivity extends AppCompatActivity{
    EditText etPhone;
    String Phone = "";
    String emailID="";
    Button btnChangePass;
    Button btnSave;
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

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
                                Intent intent_Account = new Intent( AccountActivity.this, AccountActivity.class);
                                startActivity(intent_Account);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Daily_Commute:
                                Intent intent_DC = new Intent( AccountActivity.this, DailyCommuteViewPlan.class);
                                startActivity(intent_DC);
                                Toast.makeText(getApplicationContext(),"DailyCommute",Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Find_Fellow_Traveller:
                                Intent intent_FFT = new Intent( AccountActivity.this, Maps.class);
                                startActivity(intent_FFT);
                                Toast.makeText(getApplicationContext(),"FindFellowTraveller",Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Setting:
                                Toast.makeText(getApplicationContext(),"Setting",Toast.LENGTH_SHORT).show();
                                Intent intent_Setting = new Intent( AccountActivity.this, AccountActivity.class);
                                startActivity(intent_Setting);
                                break;
                            case R.id.nav_LogOut:
                                Toast.makeText(getApplicationContext(),"LouOut",Toast.LENGTH_SHORT).show();
                                finish();
                        }
                        return true;
                    }
                });

        etPhone = (EditText) findViewById(R.id.AC_PhoneNo);
        Phone = etPhone.getText().toString();

        btnChangePass = (Button) findViewById(R.id.btnChangePass);
        btnSave = (Button) findViewById(R.id.savebtn);


        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( AccountActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AccountActivity.CallForSave().execute();
            }
        });
    }
    class CallForSave extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                SharedPreferences preferences =
                        getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);


                emailID = preferences.getString("emailID", emailID);
                String URL1 = Misc.Url1 + "/FindMyDetails?id=" + emailID;

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

}

