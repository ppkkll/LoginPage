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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText etPass;
    String Pass,ConfirmPass =  "";
    EditText etConfirmPass;
    Button btnSubmit;
    String emailID="";
    String serverIP1="10.6.35.144";

    String empty="e231";
    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_resetpassword );

            btnSubmit = findViewById(R.id.FP_Submit);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             etPass = findViewById(R.id.FP_NewPass);
             Pass = etPass.getText().toString();

             etConfirmPass = findViewById(R.id.FP_ConfirmPass);
             ConfirmPass = etConfirmPass.getText().toString();

             if (Pass.equals(String.valueOf("")) || ConfirmPass.equals(String.valueOf(""))){
                 Toast.makeText(ResetPasswordActivity.this, "Please enter both password and confirm password", Toast.LENGTH_SHORT).show();
                 Log.d("Backend", "Password Entered "+ Pass);
                 Log.d("Backend", "Confirm Password Entered "+ ConfirmPass);
             }
             else if (!Pass.equals(String.valueOf(ConfirmPass))) {
                 Toast.makeText(ResetPasswordActivity.this, "Two passwords did not match", Toast.LENGTH_SHORT).show();
                 Log.d("Backend", "Password Entered "+ Pass);
                 Log.d("Backend", "Confirm Password Entered "+ ConfirmPass);
             }

             else {
                 Intent intent = new Intent( ResetPasswordActivity.this, LoginActivity.class);
                 startActivity(intent);
                 Log.d("Backend", "Password Entered"+Pass);
                 Log.d("Backend", "Confirm Password Entered"+ConfirmPass);
                 new ResetPasswordActivity.CallForOTP().execute();

                }
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
                String URL1="http://"+serverIP1+":8080/Updatepassword?emailID="+emailID;

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
