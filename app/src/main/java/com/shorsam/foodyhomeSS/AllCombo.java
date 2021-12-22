package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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

public class AllCombo extends AppCompatActivity {

    RecyclerView recyclerView;
    LoadingDialog progressBar;
    AllComboAdapter adapter;
    String Key;
    FirebaseAuth Auth;
    FirebaseFirestore Store;
    ArrayList<String> PList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_combo);
        recyclerView=findViewById(R.id.Combo_RecyclerView);
        progressBar=new LoadingDialog(this);
        Store=FirebaseFirestore.getInstance();
        progressBar.startLoadingDialog();
        PList.clear();
        fillRecyclerView();

    }

    private void fillRecyclerView() {

        FirebaseRecyclerOptions<AllComboModel> options =
                new FirebaseRecyclerOptions.Builder<AllComboModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("SliderHome"), AllComboModel.class)
                        .build();
        adapter = new AllComboAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.setOnItemCLickListener(new AllComboAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                Key= Objects.requireNonNull(dataSnapshot.child("HomePID").getValue()).toString();
                if (Key!=null){
                    SendProductID();
                }

            }
        });
        IsRecyclerViewFilled();
        progressBar.dismissDialog();
    }

    private void IsRecyclerViewFilled() {
        if (recyclerView.getChildCount()!=0){
            progressBar.dismissDialog();
        }

    }
    private void SendProductID() {
        Map<String, Object> user = new HashMap<>();
        user.put("HomePID", Key);
        String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference documentReference=Store.collection("Users").document(UserId);
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                PList.add(Key);
                SaveSharedPreferences();
                progressBar.dismissDialog();
                startActivity(new Intent(AllCombo.this,Individual_Product.class));
            }
        });
    }
    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(PList);
        editor.putString("PList",json);
        editor.apply();

    }
}