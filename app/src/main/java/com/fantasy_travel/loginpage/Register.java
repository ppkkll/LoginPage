package com.fantasy_travel.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button CallOTPBtn = findViewById(R.id.signupSubmitBtn);
        CallOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Register.this, OTP.class);
                startActivity(intent);
            }
        });
    }


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