package com.example.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

public class AllAddresses extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore Store;
    String UserId;
    AllAddressAdapter adapter;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_addresses);
        recyclerView=findViewById(R.id.AllAddress_RecyclerView);
        Store= FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        fillItemRV();
    }
    private void fillItemRV() {

        FirestoreRecyclerOptions<AllAddressModel> options = new FirestoreRecyclerOptions.Builder<AllAddressModel>()
                .setQuery(Store
                        .collection("Users")
                        .document(UserId)
                        .collection("Address")
                        , AllAddressModel.class)
                .build();
        adapter = new AllAddressAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        loadingDialog.dismissDialog();
        AdapterSetOnClick();
    }

    private void AdapterSetOnClick() {
    }
}