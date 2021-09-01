package com.example.foodyhomeSS;

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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Login_1 extends AppCompatActivity {
    ImageButton  GoogleSignIN,EYE ;
    Integer RC_SIGN_IN=1;
    FirebaseAuth Auth;
    GoogleSignInClient mGoogleSignInClient;
    Button Login,Forget_password,NotRegistered;
    EditText Email;
    ProgressBar progressBar;
    EditText Password;
    String Email_string;
    String Password_string;
    Integer test;
    Integer eyeTimes=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_1);
        createRequest();
        Auth=FirebaseAuth.getInstance();
        EYE=findViewById(R.id.Password_Email_LogIn_Visibility_BUtton);
        Login=findViewById(R.id.SignIn_With_Email_Login_Button);
        Email=findViewById(R.id.Email_Login_EditText);
        progressBar=findViewById(R.id.Progress_Bar_Login_With_Email);
        Password=findViewById(R.id.Password_Email_LogIn);
        Forget_password=findViewById(R.id.Forget_Password_LogIn_Email);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email_string=Email.getText().toString();
                Password_string=Password.getText().toString();
                test=1;
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
                    Login.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    Auth.signInWithEmailAndPassword(Email_string,Password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Login_1.this, "Welcome FOODY", Toast.LENGTH_SHORT).show();
                                Intent i= new Intent(Login_1.this,MainActivity.class);
                                startActivity(i);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(Login_1.this, "Error! "+ Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Login.setVisibility(View.VISIBLE);
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

        GoogleSignIN = findViewById(R.id.sign_in_with_google);
        createRequest();
        GoogleSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
}

    private void createRequest() {
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [START config_signin]
        // Configure Google Sign In


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        Auth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login_1.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login_1.this, MainActivity.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login_1.this, "Error!! "+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}