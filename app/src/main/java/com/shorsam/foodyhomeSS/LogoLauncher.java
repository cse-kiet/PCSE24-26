package com.shorsam.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoLauncher extends AppCompatActivity {
    Button Login,SignUP;
    FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_launcher);
        Login=findViewById(R.id.Login_logo_launcher);
        SignUP=findViewById(R.id.SignUp_logo_launcher);
        Auth=FirebaseAuth.getInstance();
        FirebaseUser CurrentUser=Auth.getCurrentUser();
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (CurrentUser!=null){
                   Intent i=new Intent(LogoLauncher.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Login.setVisibility(View.VISIBLE);
                    SignUP.setVisibility(View.VISIBLE);
                }

            }
        }.start();
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