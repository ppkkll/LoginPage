package com.fantasy_travel.loginpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OTP extends AppCompatActivity {
    EditText OTP;
    String OTP1;
    Button validate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_input);
        validate = findViewById(R.id.validate);



        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        OTP = (EditText) findViewById(R.id.OTP_OTP);
        OTP1 = OTP.getText().toString();
        SharedPreferences preferences =
                getSharedPreferences("com.myOTP.FantasyTravel", Context.MODE_PRIVATE);

        int OTPStored=0;
                OTPStored=     preferences.getInt("OTP",OTPStored);
        Log.d("Backend", "OTP entered"+OTP1);
        Log.d("Backend", "OTP generated"+OTPStored);
        if(OTP1.equals(String.valueOf(OTPStored)))
        {Log.d("Backend", "OTP entered correct");
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
        }

    });


}

}