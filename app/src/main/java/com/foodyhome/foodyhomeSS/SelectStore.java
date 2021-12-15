package com.foodyhome.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class SelectStore extends AppCompatActivity {
    RecyclerView SelectStoreRV;
    SelectStoreAdapter adapter;
    LoadingDialog loadingDialog;
    String Store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        SelectStoreRV=findViewById(R.id.SelectStore_RecyclerView);
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        fillSelectStoreRV();
    }

    private void fillSelectStoreRV() {
        FirebaseRecyclerOptions<SelectStoreModel> options =
                new FirebaseRecyclerOptions.Builder<SelectStoreModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Store"), SelectStoreModel.class)
                        .build();
        adapter = new SelectStoreAdapter(options);
        SelectStoreRV.setLayoutManager(new LinearLayoutManager(this));
        SelectStoreRV.setAdapter(adapter);
        adapter.startListening();
        loadingDialog.dismissDialog();
        adapter.setOnItemCLickListener(new SelectStoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                Store=dataSnapshot.child("Name").getValue().toString();
                SaveSharedPreferences();
                startActivity(new Intent(SelectStore.this,Razorpay.class));
            }
        });
    }
    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Store",Store);
        editor.apply();
    }
}