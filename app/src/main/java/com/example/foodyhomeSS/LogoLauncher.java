package com.example.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogoLauncher extends AppCompatActivity {
    Button Login,SignUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_launcher);
        Login=findViewById(R.id.Login_logo_launcher);
        SignUP=findViewById(R.id.SignUp_logo_launcher);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogoLauncher.this,Login_1.class);
                startActivity(intent);
            }
        });
        SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogoLauncher.this,Sign_up_activity.class);
                startActivity(intent);
            }
        });
    }
}