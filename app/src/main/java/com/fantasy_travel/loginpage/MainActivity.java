package com.fantasy_travel.loginpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // init();

        Button signupBtn = (Button) findViewById(R.id.signupButton);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, Register.class);
                startActivity(intent);


            }
        });
        Button loginBtn = (Button) findViewById(R.id.loginButton);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        Button offLineBtn = (Button) findViewById(R.id.OfflineButton);

        offLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, P2PMainActivity.class);
                startActivity(intent);
            }
        });

    }
}



    /*private void init() {
        ;
        sBtm=findViewById(R.id.signupButton);
        lBtm=findViewById(R.id.loginButton);
        sBtm.setOnClickListener(this);
        lBtm.setOnClickListener(this);
    }*/

 /*   public void signUpBtm(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void loginBtm(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @Override
    public void onClick (View view){
        switch (view.getId()){
            case R.id.signupButton:
        }

    }
}*/
