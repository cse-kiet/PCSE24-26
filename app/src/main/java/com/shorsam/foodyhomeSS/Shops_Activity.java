package com.shorsam.foodyhomeSS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
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

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_);
        recyclerview=findViewById(R.id.Shops_Recycler_View);
        auth=FirebaseAuth.getInstance();

        LoadSharedPreferences();
        fillRecyclerView();


    }
    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
         category = sharedPreferences.getString("category", "");

    }
    private void fillRecyclerView() {
if( category!= " ") {

    FirebaseRecyclerOptions<CategoryModelTop> options =
            new FirebaseRecyclerOptions.Builder<CategoryModelTop>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("AllStores").orderByChild("Category").startAt(category).endAt(category+"\uf8ff"), CategoryModelTop.class)
                    .build();
    adapter = new ShopsActivityAdapter(options);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Shops_Activity.this, 2);
    recyclerview.setLayoutManager(layoutManager);
    recyclerview.setAdapter(adapter);
    adapter.startListening();

}
else{
    FirebaseRecyclerOptions<CategoryModelTop> options =
            new FirebaseRecyclerOptions.Builder<CategoryModelTop>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("AllStores"), CategoryModelTop.class)
                    .build();
    adapter = new ShopsActivityAdapter(options);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Shops_Activity.this, 2);
    recyclerview.setLayoutManager(layoutManager);
    recyclerview.setAdapter(adapter);
    adapter.startListening();
adapter.setOnItemCLickListener(new ShopsActivityAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(DataSnapshot dataSnapshot, int position) {
        category = Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
        SaveSharedPreferences();
        startActivity(new Intent(Shops_Activity.this,Alll_Product_2.class));
    }
});
}
    }
    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
//        Gson gson=new Gson();
//        String json=gson.toJson(PList);
        editor.putString("category",category);
//        editor.putString("PList",json);
        editor.apply();

    }
}