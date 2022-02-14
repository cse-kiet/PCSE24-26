package com.shorsam.foodyhomeSS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
    String Category;
    FirebaseAuth Auth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_see_all);
        recyclerView = findViewById(R.id.see_all_RecyclerView);
        Auth = FirebaseAuth.getInstance();
        LoadSharedPreferences();
        filling();
//        DocumentReference documentReference = Store.collection("Users").document(UserID);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                assert value != null;
//                if (value.getString("category") != null) {
//                    category = Objects.requireNonNull(value.getString("category")).trim();
//                }
//            }
//        });
//        filling();
    }
    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        Category = sharedPreferences.getString("category", "");
    }
    private void filling() {
        if (Category == " ") {
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
                    Category = Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
                    SaveSharedPreferences();
                    startActivity(new Intent(category_see_all.this ,Shops_Activity.class));

                }
                    });


            }
    }
    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
//        Gson gson=new Gson();
//        String json=gson.toJson(PList);
        editor.putString("category",Category);
//        editor.putString("PList",json);
        editor.apply();

    }
    }
//}


