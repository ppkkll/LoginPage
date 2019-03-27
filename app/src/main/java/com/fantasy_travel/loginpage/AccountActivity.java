package com.fantasy_travel.loginpage;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
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


                new AccountActivity.CallForOTP().execute();
            }
        });





    }
    class CallForOTP extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                SharedPreferences preferences =
                        getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);


                emailID = preferences.getString("emailID", emailID);
                String URL1 = Misc.Url + "/UpdatePhoneNo?emailID=" + emailID;

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
