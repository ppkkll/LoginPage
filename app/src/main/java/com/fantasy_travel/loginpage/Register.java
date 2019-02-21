package com.fantasy_travel.loginpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button signupBtm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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