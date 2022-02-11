package com.shorsam.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class SubCategoryPickYourFavourite extends AppCompatActivity {
AllProductAdapter adapter;
String SubCategory;

RecyclerView SubCategoryRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_pick_your_favourite);
        SubCategoryRV=findViewById(R.id.SubCategory_Pick_Your_Favourite_RV);
        LoadSharedPreferences();
        fillingrecyclerview();

    }
    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        SubCategory= sharedPreferences.getString("category", "");
    }
    private void fillingrecyclerview() {
        if(SubCategory!=" ") {
            FirebaseRecyclerOptions<AllProductModel> options =
                    new FirebaseRecyclerOptions.Builder<AllProductModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("AllProducts").orderByChild("SubCategory").startAt(SubCategory).endAt(SubCategory + "\uf8ff"), AllProductModel.class)
                            .build();
            adapter = new AllProductAdapter(options);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(SubCategoryPickYourFavourite.this, 2);
            SubCategoryRV.setLayoutManager(layoutManager);
            SubCategoryRV.setAdapter(adapter);
            adapter.startListening();
        }
        else{

            {
                FirebaseRecyclerOptions<AllProductModel> options =
                        new FirebaseRecyclerOptions.Builder<AllProductModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("AllProducts"), AllProductModel.class)
                                .build();
                adapter = new AllProductAdapter(options);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(SubCategoryPickYourFavourite.this, 2);
                SubCategoryRV.setLayoutManager(layoutManager);
                SubCategoryRV.setAdapter(adapter);
                adapter.startListening();
            }
        }
    }
}