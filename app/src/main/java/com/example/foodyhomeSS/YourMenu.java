package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YourMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore Store;
    String UserId;
    YourMenuAdapter adapter;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_menu);

        recyclerView=findViewById(R.id.YourMenu_RecyclerView);
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        fillRecyclerView();
        adapter.startListening();
    }

    private void fillRecyclerView() {
        Store=FirebaseFirestore.getInstance();

        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirestoreRecyclerOptions<YourMenuModel> options = new FirestoreRecyclerOptions.Builder<YourMenuModel>()
                .setQuery(Store
                        .collection("Users")
                        .document(UserId)
                        .collection("YourMenu")
                        .orderBy("Code",Query.Direction.DESCENDING), YourMenuModel.class)
                .build();
        adapter = new YourMenuAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        loadingDialog.dismissDialog();
        AdapterSetOnClick();
    }

    private void AdapterSetOnClick() {
        adapter.setOnItemClickListener(new YourMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

            }

            @Override
            public void onIncreaseClick(DocumentSnapshot documentSnapshot, int position) {


            }

            @Override
            public void onDecreaseClick(DocumentSnapshot documentSnapshot, int position) {

            }

            @Override
            public void onBuyNowClick(DocumentSnapshot documentSnapshot, int position) {
                startActivity(new Intent(YourMenu.this,Payment_Activity.class));

            }

            @Override
            public void onRemoveClick(DocumentSnapshot documentSnapshot, int position) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        AdapterSetOnClick();
    }
}