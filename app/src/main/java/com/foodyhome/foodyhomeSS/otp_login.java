package com.foodyhome.foodyhomeSS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class otp_login extends AppCompatActivity {
    EditText OTP;
    Button GetOTP,next;
    String otpid;
    FirebaseAuth Auth;
    String UserPhoneNumber;
    EditText EditTextPhoneNumber;
    ProgressBar progressBar,NextButtonProgress;
    CountryCodePicker countryCodePicker;
    FirebaseFirestore FireStore;
    int timer = 60;
    TextView TVTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_login);


        FireStore=FirebaseFirestore.getInstance();
        OTP = findViewById(R.id.editTextOTP);
        GetOTP = findViewById(R.id.Get_OTP_button);
        TVTimer = findViewById(R.id.Login_WIth_Phone_Timer);
        progressBar = findViewById(R.id.Progress_Bar_Login_With_Phone);
        EditTextPhoneNumber=findViewById(R.id.User_Phone_number_login);
        next=findViewById(R.id.login_1_next_button);
        countryCodePicker=findViewById(R.id.country_code);
        NextButtonProgress=findViewById(R.id.Progress_Bar_Login_With_Phone_Next_Button);
        countryCodePicker.registerCarrierNumberEditText(EditTextPhoneNumber);
        Auth=FirebaseAuth.getInstance();
        GetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditTextPhoneNumber.length() == 0) {
                    EditTextPhoneNumber.setError("Cannot be Empty");
                } else if (EditTextPhoneNumber.length() <= 10) {
                    EditTextPhoneNumber.setError("should be 10 Digit");
                } else {
                    UserPhoneNumber = countryCodePicker.getFullNumberWithPlus().replace(" ", "");
                    initiateOTP();
                    TVTimer.setVisibility(View.VISIBLE);
                    GetOTP.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String string_timer = Integer.toString(timer);
                            TVTimer.setText(string_timer);
                            timer -= 1;
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onFinish() {
                            TVTimer.setVisibility(View.GONE);
                            GetOTP.setVisibility(View.VISIBLE);
                            GetOTP.setText("ReSend");
                            timer=60;
                            progressBar.setVisibility(View.GONE);
                        }
                    }.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OTP.getText().toString().length()==0)
                    Toast.makeText(getApplicationContext(), "Blank field can not be processed", Toast.LENGTH_LONG).show();
                else if (OTP.getText().toString().length() != 6)
                    Toast.makeText(getApplicationContext(), "INVALID OTP", Toast.LENGTH_LONG).show();

                else {
                    next.setVisibility(View.GONE);
                    NextButtonProgress.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credentials= PhoneAuthProvider.getCredential(otpid , OTP.getText().toString());
                    signInWithPhoneAuthCredential(credentials);
                }

            }

        });
    }

    private void initiateOTP() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                String.valueOf(UserPhoneNumber),
                60,
//                 Phone number to verify
                TimeUnit.SECONDS,
                this,// Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpid = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Auth=FirebaseAuth.getInstance();
                            String UserID=Auth.getCurrentUser().getUid();
                            DocumentReference documentReference=FireStore.collection("Users").document(UserID);
                            Map<String,Object> user= new HashMap<>();
                            user.put("Phone",UserPhoneNumber);
                            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(@NonNull Void aVoid) {

                                    Intent intent = new Intent(getApplicationContext(), SignUp.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(otp_login.this, "Welcome FOODY", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // Sign in success, update UI with the signed-in user's information

                            // Update UI
                        }
                        else {
                            next.setVisibility(View.VISIBLE);
                            NextButtonProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Invalid OTP , Try Again", Toast.LENGTH_LONG).show();
                            // The verification code entered was invalid
                        }
                    }

                });

    }
    }



