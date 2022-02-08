package com.shorsam.foodyhomeSS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

public class Shops_Activity extends AppCompatActivity {
    ShopsActivityAdapter adapter;
    RecyclerView recyclerview;
   FirebaseFirestore store;
    FirebaseAuth auth;

    String userid,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_);
        recyclerview=findViewById(R.id.Shops_Recycler_View);
        auth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();
        userid= Objects.requireNonNull(auth.getCurrentUser().getUid());
        DocumentReference documentReference=store.collection("Users").document(userid);
        ListenerRegistration listenerRegistration = documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if (value.getString("category") != null) {
                    category = Objects.requireNonNull(value.getString("category")).trim();
                }
            }
        });
        fillRecyclerView();


    }
    private void fillRecyclerView() {


            FirebaseRecyclerOptions<CategoryModelTop> options =
                    new FirebaseRecyclerOptions.Builder<CategoryModelTop>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("PizzaTreat"), CategoryModelTop.class)
                            .build();
            adapter = new ShopsActivityAdapter(options);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Shops_Activity.this, 2);
            recyclerview.setLayoutManager(layoutManager);
            recyclerview.setAdapter(adapter);
            adapter.startListening();


    }
}