package com.foodyhome.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    FirebaseAuth Auth;
    FirebaseFirestore FireStore;
    Button Update;
    String UserId,SEmail,SPhone,SPinCode,SAddress,SName;
    EditText Email ,Phone,PinCode,Address,Name;
    ProgressBar progressBar;
    int test=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Auth= FirebaseAuth.getInstance();
        FireStore=FirebaseFirestore.getInstance();
        Email=findViewById(R.id.Email_signUP2);
        Phone=findViewById(R.id.Phone_signUP2);
        PinCode=findViewById(R.id.PinCode_signUP2);
        Address=findViewById(R.id.Address_signUP2);
        Name=findViewById(R.id.Name_signUP2);
        Update=findViewById(R.id.UpdateButton_signUP2);
        progressBar=findViewById(R.id.progressBar_signUP2);
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        fillDefaultAddress();
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SName=Name.getText().toString();
                SEmail=Email.getText().toString();
                SPhone=Phone.getText().toString();
                SAddress=Address.getText().toString();
                SPinCode = PinCode.getText().toString();
                test=1;
                if (TextUtils.isEmpty(SName)){
                    Name.setError("Required");
                    test=0;
                }
                if (TextUtils.isEmpty(SEmail)){
                    Email.setError("Required");
                    test=0;
                }
                if (TextUtils.isEmpty(SPinCode)){
                    PinCode.setError("Required");
                    test=0;
                }
                if (TextUtils.isEmpty(SPhone)){
                    Phone.setError("Required");
                    test=0;
                }
                if (TextUtils.isEmpty(SAddress)){
                    Address.setError("Required");
                    test=0;
                }
                if (SPhone.length()< 10){
                    Phone.setError("Should be 10 digit");
                    test=0;
                }
                if (test==1) {
                    Update.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    DocumentReference documentReference=FireStore.collection("Users").document(UserId);
                    Map<String,Object> user= new HashMap<>();
                    user.put("Email" , SEmail);
                    user.put("PinCode",SPinCode);
                    user.put("Name" , SName);
                    user.put("Address" , SAddress);
                    user.put("Phone" , SPhone);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {


                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

    }
    private void fillDefaultAddress() {
        DocumentReference documentReference=FireStore
                .collection("Users")
                .document(UserId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if (value.getString("Name")!=null){
                    Name.setText(Objects.requireNonNull(value.get("Name")).toString());
                }
                if (value.getString("Phone")!=null){
                    Phone.setText(Objects.requireNonNull(value.get("Phone")).toString());
                }
                if (value.getString("Address")!=null){
                    Address.setText(Objects.requireNonNull(value.get("Address")).toString());
                }
                if (value.getString("PinCode")!=null){
                    PinCode.setText(Objects.requireNonNull(value.get("PinCode")).toString());
                }
                if (value.getString("Email")!=null){
                    Email.setText(Objects.requireNonNull(value.get("Email")).toString());
                }
//




            }
        });
    }
}