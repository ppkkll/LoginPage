package com.fantasy_travel.loginpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AppCompatActivity {
    EditText etUsername, etPassword,etAge,etSex,etPhoneNumber;
    Button registerBtn;
    String   phoneNumber,password,age,sex,emailID;
    //shadjahdkshjhdkajs
String serverIP="10.6.48.1";
    String serverIP1="10.6.35.144";
   // Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


         registerBtn = (Button) findViewById(R.id.signupSubmitBtn);



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Register.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Register.this, "Processing", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Backend", "button pressed");
                //new Call().execute();
                Log.d("Backend", "call executed ");

                etUsername = (EditText) findViewById(R.id.Reg_Email);
                emailID = etUsername.getText().toString();
                // Log.d()
                etPassword = (EditText) findViewById(R.id.Reg_Password);
                password = etPassword.getText().toString();

                etAge = (EditText) findViewById(R.id.Reg_Age);
                age = etAge.getText().toString();

                etSex = (EditText) findViewById(R.id.Reg_Sex);
                sex = etSex.getText().toString();

                etPhoneNumber = (EditText) findViewById(R.id.Reg_phoneNumber);
                phoneNumber = etPhoneNumber.getText().toString();

                new Call().execute();

            }
        });

       // registerBtn.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {
              //  new Call().execute();


             //   Intent intent = new Intent( Register.this, OTP.class);
            //    startActivity(intent);
        //    }
       // });
    }

    class Call extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            try {

                String URL1="http://"+serverIP+":8080/InsertDataEntry?phoneNumber="+phoneNumber+"&emailID="+emailID+"&age="+age+"&sex="+sex+"&name=shubham&password="+password;
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
                    if(output.contains("200"))
                    {  Log.d("Backend", "Contains 200");
                        new CallForOTP().execute();
                        //new Call().execute();

                    }
                   else if(output.contains("NumberAlreadyExists")) {
                        Log.d("Backend", "NumberAlreadyExists");
                        //Toast.makeText(LoginActivity.this,"Invalid Creadentials",Toast.LENGTH_SHORT);


                        Register.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Register.this, "Number Already Exists", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(output.contains("EmailIDAlreadyExists")) {
                        Log.d("Backend", "EmailID Already Exists");
                        //Toast.makeText(LoginActivity.this,"Invalid Creadentials",Toast.LENGTH_SHORT);


                        Register.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Register.this, "Email ID Already Exists", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    { Log.d("Backend", "Does not contain Contains 200");
                        //Toast.makeText(LoginActivity.this,"Invalid Creadentials",Toast.LENGTH_SHORT);


                        Register.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Register.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

                conn.disconnect();

            } catch (Exception e) {


                Log.d("Backend", "exception in HTTP");
                e.printStackTrace();

            }

            return null;
        }
    }


    class CallForOTP extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            try {

                String URL1=Misc.Url1+"/otpGenerate?phoneNumber="+phoneNumber;
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
                    final JSONObject obj = new JSONObject(output);
                    int otp=obj.getInt("otp");
                    Log.d("Backend", "OTP: "+obj.getInt("otp"));

                    SharedPreferences preferences =
                            getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);

//Save it
                    preferences.edit().putString("emailID",emailID ).commit();
                    preferences.edit().putInt("OTP",otp ).commit();
                    Intent intent = new Intent( Register.this, OTP.class);
                    startActivity(intent);
                }


            conn.disconnect();

        } catch (Exception e) {


            Log.d("Backend", "exception in HTTP");
            e.printStackTrace();

        }

            return null;
    }
}
//    Button signupSubmitBtn = findViewById(R.id.signupSubmitBtn);

//    signupSubmitBtn =
}
       /* getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();




        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
//        signupBtm  = findViewById(R.id.signupBtm);

        signupBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg("Login buttom hit!");
            }
        });




    }
    private void msg(String s)
    {
        Log.d("My App" , "LoginActivity" + "#######" + s);

    }
}
*/