package com.shorsam.foodyhomeSS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class category_see_all extends AppCompatActivity {
    RecyclerView recyclerView;
    category_see_all_adapter adapter;
        String category,UserID;
    FirebaseAuth Auth;

    FirebaseFirestore Store;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_see_all);
        recyclerView = findViewById(R.id.see_all_RecyclerView);
        Auth=FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();
        UserID=Objects.requireNonNull(Auth.getCurrentUser()).getUid();
//        DocumentReference documentReference=Store.collection("Users").document(UserID);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                assert value != null;
//                if (value.getString("category") != null) {
//                    category = Objects.requireNonNull(value.getString("category")).trim();
//                }
//            }
//        });
        filling();
    }
private void filling() {
    FirebaseRecyclerOptions<Category_See_ALL_Model> options =
            new FirebaseRecyclerOptions.Builder<Category_See_ALL_Model>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Categories"), Category_See_ALL_Model.class)
                    .build();
    adapter = new category_see_all_adapter(options);
    LinearLayoutManager layoutManager = new LinearLayoutManager(category_see_all.this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    adapter.startListening();
    adapter.setOnItemCLickListener(new category_see_all_adapter.OnItemClickListener() {
        @Override
        public void onItemClick(DataSnapshot dataSnapshot, int position) {
            category = Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString().toLowerCase();
            Map<String, Object> user = new HashMap<>();
            user.put("category", category);
            String UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            DocumentReference documentReference = Store.collection("Users").document(UserId);
            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(category_see_all.this, Shops_Activity.class));
                }

            });
        }
    });

}

}



