package com.fantasy_travel.loginpage;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class AccountActivity extends AppCompatActivity{
    EditText etUsername;
    EditText etPhone;

    Button btnChangePass;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);


        btnChangePass = findViewById(R.id.btnChangePass);
        btnSave = findViewById(R.id.savebtn);


        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( AccountActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( AccountActivity.this, ResetPasswordActivity.class);
                startActivity(intent);


            }
        });





    }


}
