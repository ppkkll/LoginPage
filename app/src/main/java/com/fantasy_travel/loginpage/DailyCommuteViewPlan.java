package com.fantasy_travel.loginpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DailyCommuteViewPlan extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_commute_my_plans);

        FloatingActionButton addButton = findViewById(R.id.floatingActionButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyCommuteViewPlan.this, DailyCommuteCreatePlan.class);
                startActivity(intent);
            }
        });

        FloatingActionButton refreshButton = findViewById(R.id.floatingActionButton1);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Call_Retrieve().execute();
            }
        });
    }

    class Call_Retrieve extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d("Backend", "insideCall");
            try {


                String URL1 = "http://10.6.33.190:8080/FindMyPlan?id=qq@gmail.com";
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
                        /*Intent intent = new Intent( DailyCommuteViewPlan.this, DailyCommuteViewPlan.class);
                        startActivity(intent);*/
                    }
                    else if(output.contains("500"))
                    {  Log.d("Backend", "Contains 200");
                        DailyCommuteViewPlan.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(DailyCommuteViewPlan.this, "Account Not Active", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    else
                    { Log.d("Backend", "Does not contain Contains 200");
                        //Toast.makeText(LoginActivity.this,"Invalid Creadentials",Toast.LENGTH_SHORT);


                        DailyCommuteViewPlan.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(DailyCommuteViewPlan.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
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
