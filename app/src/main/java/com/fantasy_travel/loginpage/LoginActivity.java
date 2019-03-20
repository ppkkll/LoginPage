package com.fantasy_travel.loginpage;

import android.content.Intent;
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
import com.fantasy_travel.loginpage.Misc;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //EditText etUsername, etPassword;
   // Button signupBtm;
    EditText etUsername ;
    EditText etPassword ;
    Button loginBtn;
    Button registerBtn;
    String userName  ,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          Log.d("Backend", "button pressed"); 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         loginBtn = findViewById(R.id.Login_loginButton);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Processing", Toast.LENGTH_SHORT).show();
                    }
                });
                  Log.d("Backend", "button pressed");
                  //new Call().execute();
                  Log.d("Backend", "call executed ");

                etUsername = (EditText) findViewById(R.id.loginID);
                 userName = etUsername.getText().toString();
                // Log.d()
                etPassword = (EditText) findViewById(R.id.LoginPassword);
                 password = etPassword.getText().toString();
                //Intent intent = new Intent( LoginActivity.this, Maps.class);
                //startActivity(intent);
                 new Call().execute();
            }
        });

         registerBtn = findViewById(R.id.Login_RegisterButton);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });


    }

    class Call extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
                       Log.d("Backend", "insideCall");
            try {


                String URL1 = Misc.Url + "Login?emailID=" + userName + "&password=" + password;
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

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                //  Log.d("Output from Server .... \n","new");
                while ((output = br.readLine()) != null) {
                    Log.d("Backend", output);


                    if(output.contains("200"))
                    {  Log.d("Backend", "Contains 200");
                        Intent intent = new Intent( LoginActivity.this, Maps.class);
                        startActivity(intent);
                    }
                   else if(output.contains("500"))
                    {  Log.d("Backend", "Contains 200");
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Account Not Active", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    else
                    { Log.d("Backend", "Does not contain Contains 200");
                        //Toast.makeText(LoginActivity.this,"Invalid Creadentials",Toast.LENGTH_SHORT);


                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
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
}