package com.fantasy_travel.loginpage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    EditText etUsername, etPassword;
    Button loginBtn, registerBtn;
    String userName, password;
    Boolean save_login;
    CheckBox save_login_checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Backend", "button pressed");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText) findViewById(R.id.loginID);
        etPassword = (EditText) findViewById(R.id.LoginPassword);
        loginBtn = (Button) findViewById(R.id.Login_loginButton);
        save_login_checkbox = (CheckBox) findViewById(R.id.checkBox);
        mSharedPreferences = getSharedPreferences("UserName", MODE_PRIVATE);
        save_login_checkbox = (CheckBox) findViewById(R.id.checkBox);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

                LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Processing", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Backend", "button pressed");
                //new Call().execute();
                Log.d("Backend", "call executed ");

                Intent intent = new Intent(LoginActivity.this, Maps.class);
                startActivity(intent);
                //new Call().execute();
            }
        });

        registerBtn = (Button) findViewById(R.id.Login_RegisterButton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });

        save_login = mSharedPreferences.getBoolean("SaveLogin", true);
        if (save_login) {
            etUsername.setText(mSharedPreferences.getString("UserName", null));
            etPassword.setText(mSharedPreferences.getString("Password", null));

        }

    }

    class Call extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d("Backend", "insideCall");
            try {


                String URL1 = Misc.Url + "Login?emailID=" + userName + "&password=" + password;
                Log.d("Backend", URL1);
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


                    if (output.contains("200")) {
                        Log.d("Backend", "Contains 200");
                        Intent intent = new Intent(LoginActivity.this, Maps.class);
                        startActivity(intent);
                    } else if (output.contains("500")) {
                        Log.d("Backend", "Contains 200");
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Account Not Active", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.d("Backend", "Does not contain Contains 200");
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


    public void login() {
        userName = etUsername.getText().toString();
        password = etPassword.getText().toString();
        if (save_login_checkbox.isChecked()) {
            mEditor = mSharedPreferences.edit();
            mEditor.putBoolean("SaveLogin", true);
            mEditor.putString("UserName", userName);
            mEditor.putString("Password", password);
            mEditor.apply();
        }
    }
}