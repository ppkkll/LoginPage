package com.fantasy_travel.loginpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AppCompatActivity {
    //EditText etUsername, etPassword;
   // Button registerBtn;
    String userName  ,password;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


         registerBtn = findViewById(R.id.signupButton);


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

                String URL1="http://localhost:8080/InsertDataEntry?phoneNumber=0873516501&emailID=sgyaqq@gmail.com&age=10&sex=male&name=shubham&password=qqqwww";
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