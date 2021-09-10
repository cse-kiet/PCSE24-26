package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SeeAllCategories extends AppCompatActivity {

    RecyclerView recyclerView;
    IndividualCategoryAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore Store;
    FirebaseAuth Auth;
    String UserID,SeeAll,ProductID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_categories);
        recyclerView = findViewById(R.id.SeeAllCategory_RecyclerView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        Auth = FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();
        UserID = Objects.requireNonNull(Auth.getCurrentUser()).getUid();
//        loadingDialog.startLoadingDialog();
        DocumentReference documentReference = Store.collection("users").document(UserID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if (value.getString("SeeAll") != null) {
                    SeeAll = Objects.requireNonNull(value.getString("SeeAll")).trim();
                }
            }
        });
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (SeeAll != null) {

                    FirebaseRecyclerOptions<IndividualCategoryModel> options =
                            new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("PizzaTreat"), IndividualCategoryModel.class)
                                    .build();
                    adapter = new IndividualCategoryAdapter(options);
                    layoutManager = new GridLayoutManager(SeeAllCategories.this, 2);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                    adapter.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(DataSnapshot dataSnapshot, int position) {

                            SeeAll= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString().toLowerCase();

                            Map<String, Object> user = new HashMap<>();
                            user.put("SeeAll", SeeAll);
                            String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                            DocumentReference documentReference=Store.collection("users").document(UserId);
                            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(SeeAllCategories.this,AllProduct.class));
                                }
                            });
                        }
                    });
                }
            }
        }.start();

        if (SeeAll != null) {
            FirebaseRecyclerOptions<IndividualCategoryModel> options =
                    new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("PizzaTreat"), IndividualCategoryModel.class)
                            .build();
            adapter = new IndividualCategoryAdapter(options);
            layoutManager = new GridLayoutManager(SeeAllCategories.this, 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.startListening();
            adapter.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DataSnapshot dataSnapshot, int position) {
                    SeeAll= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString().toLowerCase();
                    Map<String, Object> user = new HashMap<>();
                    user.put("SeeAll", SeeAll);
                    String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    DocumentReference documentReference=Store.collection("users").document(UserId);
                    documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(SeeAllCategories.this,AllProduct.class));
                        }
                    });
                }
            });
        }
    }
}