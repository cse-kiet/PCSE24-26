package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AllProduct extends AppCompatActivity {
    RecyclerView recyclerView;
    AllProductAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore Store;
    FirebaseAuth Auth;
    String UserID,SeeAll,ProductID;
    LoadingDialog progressBar;
    ArrayList<String> PList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        recyclerView = findViewById(R.id.AllProduct_RecyclerView);
        progressBar=new LoadingDialog(this);
        progressBar.startLoadingDialog();

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        Auth = FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();
        UserID = Objects.requireNonNull(Auth.getCurrentUser()).getUid();
        DocumentReference documentReference = Store.collection("Users").document(UserID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if (value.getString("SeeAll") != null) {
                    SeeAll = Objects.requireNonNull(value.getString("SeeAll")).trim();
                }
            }
        });
        fillRecyclerView();
        new CountDownTimer(10000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if (recyclerView.getChildCount()!=0){
                    progressBar.dismissDialog();
                    cancel();
                }
                else{
                    fillRecyclerView();
                }
            }

            @Override
            public void onFinish() {
                if (recyclerView.getChildCount()!=0){
                    progressBar.dismissDialog();
                    cancel();
                }
                else{
                    progressBar.dismissDialog();
                    Toast.makeText(AllProduct.this, "No Internet, check your Connection and Restart FoodyHome", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();


    }

    private void fillRecyclerView() {
        if (SeeAll != null) {

            FirebaseRecyclerOptions<AllProductModel> options =
                    new FirebaseRecyclerOptions.Builder<AllProductModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("HomeCategory").startAt(SeeAll).endAt(SeeAll+"\uf8ff"), AllProductModel.class)
                            .build();
            adapter = new AllProductAdapter(options);
            layoutManager = new GridLayoutManager(AllProduct.this, 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.startListening();
            adapter.setOnItemCLickListener(new AllProductAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DataSnapshot dataSnapshot, int position) {
                    ProductID= Objects.requireNonNull(dataSnapshot.getKey());
                    Map<String, Object> user = new HashMap<>();
                    user.put("HomePID", ProductID);
                    String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    DocumentReference documentReference=Store.collection("Users").document(UserId);
                    documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            PList.add(ProductID);
                            SaveSharedPreferences();
                            startActivity(new Intent(AllProduct.this,Individual_Product.class));
                        }
                    });
                }
            });
        }
    }

    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(PList);
        editor.putString("PList",json);
        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillRecyclerView();
    }
}