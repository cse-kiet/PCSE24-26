package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Individual_Product extends AppCompatActivity {
    ImageView PImage;
    TextView PName,PPRice,PDescription;
    String SName,SPrice,SDescription,SImage,UserID,HomePID;
    FirebaseFirestore Store;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual__product);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Products");
        Auth = FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();
        UserID = Objects.requireNonNull(Auth.getCurrentUser()).getUid();
//        loadingDialog.startLoadingDialog();
        DocumentReference documentReference = Store.collection("users").document(UserID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if (value.getString("HomePID") != null) {
                    HomePID = Objects.requireNonNull(value.getString("HomePID")).trim();
                    Toast.makeText(Individual_Product.this, ""+HomePID, Toast.LENGTH_SHORT).show();
                }
            }
        });

        PImage=findViewById(R.id.Individual_Product_Activity_Product_Image);
        PPRice=findViewById(R.id.Individual_Product_Activity_Product_Price);
        PName=findViewById(R.id.Individual_Product_Activity_Product_Name);
        PDescription=findViewById(R.id.Individual_Product_Activity_Product_Description);
        fillDetails();

        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
             fillDetails();
            }
        }.start();

    }

    private void fillDetails() {
        if (HomePID!=null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Products");
            myRef.child(HomePID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SName = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                    SPrice = Objects.requireNonNull(snapshot.child("Price").getValue()).toString();
                    SDescription = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                    SImage = Objects.requireNonNull(snapshot.child("Image").getValue()).toString();
                    PName.setText(SName);
                    PDescription.setText(SDescription);
                    PPRice.setText(SPrice);
                    Glide.with(PImage.getContext()).load(SImage).into(PImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}