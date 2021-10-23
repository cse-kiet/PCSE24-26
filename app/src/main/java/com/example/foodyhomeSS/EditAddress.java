package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditAddress extends AppCompatActivity {
    TextView Name,Phone,PinCode,Address1,Address2,AlternatePhone;
    String SName,SPhone,SPinCode,SAddress1,SAddress2,SAlternatePhone,Date,UserId;
    Button Cancel,Save;
    LoadingDialog loadingDialog;
    FirebaseFirestore Store1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        Name=findViewById(R.id.Edit_Address_PersonName);
        Phone=findViewById(R.id.Edit_Address_PersonPhone);
        PinCode=findViewById(R.id.Edit_Address_Person_PinCode);
        Address1=findViewById(R.id.Edit_Address_Address1);
        Address2=findViewById(R.id.Edit_Address_Address2);
        AlternatePhone=findViewById(R.id.Edit_Address_Person_AlternatePhone);
        loadingDialog=new LoadingDialog(this);

        Date=getDateTime();
        Store1=FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


        Cancel=findViewById(R.id.Edit_Address_Cancel);
        Save=findViewById(R.id.Edit_Address_Save_And_continue);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtractString();
                if (TextUtils.isEmpty(SName)){
                    Name.setError("Required");
                }
                if (TextUtils.isEmpty(SPhone)){
                    Phone.setError("Required");
                }
                if (TextUtils.isEmpty(SPinCode)){
                    PinCode.setError("Required");
                }
                if (TextUtils.isEmpty(SAddress1)){
                    Address1.setError("Required");
                }
                else {
                    SetDataToAddress();
                }
            }
        });

    }
    private void SetDataToAddress(){
        loadingDialog.startLoadingDialog();

        CollectionReference collectionReference=Store1
                .collection("Users")
                .document(UserId)
                .collection("Address")
                ;
        Map<String, Object> user = new HashMap<>();
        user.put("Name",SName);
        user.put("Phone",SPhone);
        user.put("PinCode",SPinCode);
        if (TextUtils.isEmpty(SAddress2)){
            user.put("Address",SAddress1);
        }
        if (!TextUtils.isEmpty(SAddress2)){
            user.put("Address",SAddress1+", "+SAddress2);
        }
        if (!TextUtils.isEmpty(SAlternatePhone)){
            user.put("AlternatePhone",SAlternatePhone);
        }

        collectionReference.document(Date).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                loadingDialog.dismissDialog();
                finish();
            }
        });
    }

    private void ExtractString() {
        SName=Name.getText().toString();
        SPhone=Phone.getText().toString();
        SAlternatePhone=AlternatePhone.getText().toString();
        SPinCode=PinCode.getText().toString();
        SAddress1=Address1.getText().toString();
        SAddress2=Address2.getText().toString();
    }
    private String getDateTime() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        java.util.Date date = new Date();
        return dateFormat.format(date);
    }
}