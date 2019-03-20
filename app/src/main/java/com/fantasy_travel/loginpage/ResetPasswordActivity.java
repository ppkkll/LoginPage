package com.fantasy_travel.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText etPass;
    String Pass;
    EditText etConfirmPass;
    String ConfirmPass;
    Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_resetpassword );

            btnSubmit = findViewById(R.id.FP_Submit);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             etPass = findViewById(R.id.FP_NewPass);
             Pass = etPass.getText().toString();

             etConfirmPass = findViewById(R.id.FP_ConfirmPass);
             ConfirmPass = etConfirmPass.getText().toString();

             if (Pass == ConfirmPass){
                 Intent intent = new Intent( ResetPasswordActivity.this, LoginActivity.class);
                 startActivity(intent);

             }



                }
            });

    }
}
