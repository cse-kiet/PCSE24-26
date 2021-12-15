package com.foodyhome.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TopCategories extends AppCompatActivity {
    ArrayList<String> PList=new ArrayList<>();
    RecyclerView recyclerView;
    AllProductAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore Store;
    FirebaseAuth Auth;
    String UserID,TopCategory,ProductID;
    LoadingDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_categories);
        recyclerView = findViewById(R.id.TopCategories_RecyclerView);
        progressBar=new LoadingDialog(this);
        progressBar.startLoadingDialog();
        Auth = FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();
        UserID = Objects.requireNonNull(Auth.getCurrentUser()).getUid();
        LoadSharedPreferences();
        fillRecyclerView();
    }
    private void fillRecyclerView() {
        if (TopCategory != null) {

            FirebaseRecyclerOptions<AllProductModel> options =
                    new FirebaseRecyclerOptions.Builder<AllProductModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Category").startAt(TopCategory).endAt(TopCategory+"\uf8ff"), AllProductModel.class)
                            .build();
            adapter = new AllProductAdapter(options);
            layoutManager = new GridLayoutManager(TopCategories.this, 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.startListening();
            progressBar.dismissDialog();
            adapter.setOnItemCLickListener(new AllProductAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DataSnapshot dataSnapshot, int position) {
                    progressBar.startLoadingDialog();
                    ProductID= Objects.requireNonNull(dataSnapshot.getKey());
                    Map<String, Object> user = new HashMap<>();
                    user.put("HomePID", ProductID);
                    String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    DocumentReference documentReference=Store.collection("Users").document(UserId);
                    documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            PList.add(ProductID);
                            Toast.makeText(TopCategories.this, ProductID +" is added", Toast.LENGTH_SHORT).show();
                            SaveSharedPreferences();
                            progressBar.dismissDialog();
                            startActivity(new Intent(TopCategories.this,Individual_Product.class));
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
    private void LoadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        TopCategory=sharedPreferences.getString("TopCategory","");
        if (PList==null){
            PList=new ArrayList<>();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PList.clear();
        SaveSharedPreferences();
        fillRecyclerView();
    }
}