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
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OTP extends AppCompatActivity {
    EditText OTP;
    String OTP1;
    Button validate;
    int OTPStored=0;
    String emailID="";
    String serverIP1="10.6.35.144";
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_input);
        validate = (Button) findViewById(R.id.validate);
         preferences =
                getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);


        OTPStored=     preferences.getInt("OTP",OTPStored);
      //  OTP.this.runOnUiThread(new Runnable() {
        //    public void run() {
          //      Toast.makeText(OTP.this, "OTP: "+ OTPStored, Toast.LENGTH_SHORT).show();
           // }
       // });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        OTP = (EditText) findViewById(R.id.OTP_OTP);
        OTP1 = OTP.getText().toString();

        Log.d("Backend", "OTP entered"+OTP1);
        Log.d("Backend", "OTP generated"+OTPStored);


        if(OTP1.equals(String.valueOf(OTPStored)))
        {


            Log.d("Backend", "OTP entered correct");
            Intent intent = new Intent( OTP.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            Log.d("Backend", "OTP entered incorrect");
            OTP.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(OTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            });
        }
                new CallForOTP().execute();

        }

    });


}


    class CallForOTP extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                SharedPreferences preferences =
                        getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);


                emailID=     preferences.getString("emailID",emailID);
                String URL1=Misc.Url1+"/UpdateStatus?emailID="+emailID;

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