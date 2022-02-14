package com.shorsam.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Alll_Product_2 extends AppCompatActivity {
    AllProductAdapter adapter;
    String Shop_Name;
    RecyclerView All_Product_2_RV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alll__product_2);
        All_Product_2_RV = findViewById(R.id.All_Product2_Recycler_View);
        LoadSharedPreferences();
        FillingRecyclerView();
    }
    private void FillingRecyclerView() {
        if (Shop_Name != null) {
            FirebaseRecyclerOptions<AllProductModel> options =
                    new FirebaseRecyclerOptions.Builder<AllProductModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("AllProducts").orderByChild("StoreName").startAt(Shop_Name).endAt(Shop_Name + "\uf8ff"), AllProductModel.class)
                            .build();
            adapter = new AllProductAdapter(options);
            All_Product_2_RV.setLayoutManager(new LinearLayoutManager(this));
            All_Product_2_RV.setAdapter(adapter);
            adapter.startListening();

        }
    }

    private void LoadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Shop_Name=sharedPreferences.getString("category","");
    }
}