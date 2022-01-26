package com.shorsam.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class category_see_all extends AppCompatActivity {
    RecyclerView recyclerView;
    category_see_all_adapter adapter;

    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_see_all);
        recyclerView = findViewById(R.id.see_all_RecyclerView);
        Auth=FirebaseAuth.getInstance();
        filling();
    }
private void filling() {
    FirebaseRecyclerOptions<Category_See_ALL_Model> options =
            new FirebaseRecyclerOptions.Builder<Category_See_ALL_Model>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("DifferentTreat"), Category_See_ALL_Model.class)
                    .build();
    adapter = new category_see_all_adapter(options);
    LinearLayoutManager layoutManager = new LinearLayoutManager(category_see_all.this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    adapter.startListening();
}
}


