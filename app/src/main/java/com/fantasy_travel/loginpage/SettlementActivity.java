package com.fantasy_travel.loginpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

public class SettlementActivity extends AppCompatActivity {
    TextView username;
    Button button_submit;
    TextView text;
    RatingBar Rating_bar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        linstenerForRatingBar();
        button_submit=(Button) findViewById(R.id.buttonRating);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Call().execute();
                Intent intent = new Intent(SettlementActivity.this, Maps.class);
                startActivity(intent);
            }
        });
    }

    public void linstenerForRatingBar(){
         username = (TextView) findViewById(R.id.loginID);
        Rating_bar = (RatingBar) findViewById(R.id.ratingBar);
        text = (TextView) findViewById(R.id.RatingTextView);
        Rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                text.setText(String.valueOf(rating));
            }
        });

    }
    class Call extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d("Backend", "insideCall");
            try {

                String URL1 = Misc.Url1 + "UpdateRating?emailID=" + "qq" + "rating=" + text;
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
                        Intent intent = new Intent(SettlementActivity.this, Maps.class);
                        startActivity(intent);
                    } else if (output.contains("500")) {
                        Log.d("Backend", "Contains 200");
                        SettlementActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(SettlementActivity.this, "Account Not Active", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.d("Backend", "Does not contain Contains 200");
                        //Toast.makeText(LoginActivity.this,"Invalid Creadentials",Toast.LENGTH_SHORT);


                        SettlementActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(SettlementActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
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
