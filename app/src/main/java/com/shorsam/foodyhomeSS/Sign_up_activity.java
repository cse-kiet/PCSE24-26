
package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Sign_up_activity extends AppCompatActivity {
    ImageButton  EYE ;
    SignInButton GoogleSignIN;
    Integer RC_SIGN_IN=1;
    FirebaseAuth Auth;
    GoogleSignInClient mGoogleSignInClient;
    Button Email_SignUp;
    Button AlreadyRegistered;
    String Email_string;
    String Password_string;
    String Name_string;
    String UserID;
    String Address_string;
    FirebaseFirestore FireStore;
    EditText Email;
    EditText Password;
    EditText Address;
    EditText Name;
    Integer test;
    Integer eyeTimes=0;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity);
//        GoogleSignIN = findViewById(R.id.sign_in_with_google);
        Email_SignUp =  findViewById(R.id.Sign_up);
        Email=findViewById(R.id.Register_Email);
        Password=findViewById(R.id.Register_Password_Email);
        Name=findViewById(R.id.Register_Name);
       Address= findViewById(R.id.Register_Address);
        progressBar=findViewById(R.id.progressBar_Register_With_Email);
        EYE=findViewById(R.id.Password_Email_Signup_Visibility_BUtton);
//        OTP=findViewById(R.id.Phone_number_Login_Button_SignUp_Activity);
//        OTP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Sign_up_activity.this,otp_login.class));
//            }
//        });



        AlreadyRegistered=findViewById(R.id.AlreadyRegistered_SignUP);
        Auth= FirebaseAuth.getInstance();
        FireStore=FirebaseFirestore.getInstance();
        Email_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name_string=Name.getText().toString();
                Email_string=Email.getText().toString();
                Password_string=Password.getText().toString();
                Address_string=Address.getText().toString();
                test=1;
                if (TextUtils.isEmpty(Name_string)){
                    Name.setError("Name_string is Required");
                    test=0;
                }
                if (TextUtils.isEmpty(Email_string)){
                    Email.setError("Email is Required");
                    test=0;
                }
                if (TextUtils.isEmpty(Password_string)){
                    Password.setError("Password is Required");
                    test=0;
                }
                if (Password_string.length()< 6){
                    Password.setError("Password > 6 words");
                    test=0;
                }
                if (test==1) {
                    Email_SignUp.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    Auth.createUserWithEmailAndPassword(Email_string,Password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                    UserID=Auth.getCurrentUser().getUid();
                                    DocumentReference documentReference=FireStore.collection("Users").document(UserID);
                                    Map<String,Object> user= new HashMap<>();
                                    user.put("Email" , Email_string);
                                    user.put("Password",Password_string);
                                    user.put("Name" , Name_string);

                                    user.put("Address" , Address_string);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {


                                        @Override
                                        public void onSuccess(@NonNull Void aVoid) {
                                            Toast.makeText(Sign_up_activity.this, "Welcome FOODY", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                            Intent i = new Intent(Sign_up_activity.this, MainActivity.class);


                                            startActivity(i);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }

                            else{
                                Toast.makeText(Sign_up_activity.this, "Error! "+ Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Email_SignUp.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
        EYE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eyeTimes%2==0) {
                    Password.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    Password.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                eyeTimes=eyeTimes+1;

            }
        });

        AlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Sign_up_activity.this,Login_1.class);
                startActivity(i);
                finish();
            }
        });
//        createRequest();
//        GoogleSignIN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signInWithGoogle();
//
//            }
//        });
    }



//    private void createRequest() {
//        // [START config_signin]
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        // [START config_signin]
//        // Configure Google Sign In
//
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        // [END config_signin]
//
//        // [START initialize_auth]
//        // Initialize Firebase Auth
//        Auth = FirebaseAuth.getInstance();
//        // [END initialize_auth]
//    }
//    private void signInWithGoogle() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        Auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            Intent i = new Intent(Sign_up_activity.this, SignUp.class);
//                            startActivity(i);
//                            finish();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(Sign_up_activity.this, "Error!! " +task.getException(), Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                            Email_SignUp.setVisibility(View.VISIBLE);
//                            Email_SignUp.setAlpha(1);
//                        }
//                    }
//                });
//    }
}