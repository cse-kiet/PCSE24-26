package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class LogoLauncher extends AppCompatActivity {
    Button Login,SignUP;
    FirebaseAuth Auth;
    private ArrayList<String> UserIDs;
    FirebaseFirestore Store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_launcher);
        Login=findViewById(R.id.Login_logo_launcher);
        SignUP=findViewById(R.id.SignUp_logo_launcher);
        Auth=FirebaseAuth.getInstance();
        UserIDs= new ArrayList<>();

        Store = FirebaseFirestore.getInstance();
        if (Auth.getCurrentUser()!=null) {
            Store.collection("Users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    UserIDs.add(document.getId());
                                }
                                CheckUser();
                            } else {
                                Toast.makeText(LogoLauncher.this, "Please Restart the App !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else{
            CheckUser();
        }
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogoLauncher.this,Login_1.class);
                startActivity(intent);
                finish();
            }
        });
        SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogoLauncher.this,Sign_up_activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void CheckUser() {
        Auth=FirebaseAuth.getInstance();
        FirebaseUser CurrentUser=Auth.getCurrentUser();
        new CountDownTimer(1000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (CurrentUser!=null){
                    int test=0;
                    String UserId= Objects.requireNonNull(Auth.getCurrentUser()).getUid().toString();
                    for (String Id : UserIDs){
                        if (Objects.equals(Id, UserId)){
                            test=1;
                            Intent i=new Intent(LogoLauncher.this,MainActivity.class);
                            startActivity(i);

                            finish();
                            break;

                        }
                    }
                    if (test!=1){
                        startActivity(new Intent(LogoLauncher.this,SignUp.class));
                    }


                }
                else {
                    Login.setVisibility(View.VISIBLE);
                    SignUP.setVisibility(View.VISIBLE);
                }

            }
        }.start();
    }
}