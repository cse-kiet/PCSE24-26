package com.foodyhome.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AllAddresses extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore Store;
    AllAddressAdapter adapter;
    LoadingDialog loadingDialog;
    String UserId,SName,SPhone,SAddress,SPinCode;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_addresses);
        recyclerView=findViewById(R.id.AllAddress_RecyclerView);
        Store= FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        constraintLayout=findViewById(R.id.ConstraintLayout_All_Address);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllAddresses.this,EditAddress.class));
            }
        });
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
        adapter.setOnItemClickListener(new AllAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SName=documentSnapshot.getString("Name");
                SPhone=documentSnapshot.getString("Phone");
                SAddress=documentSnapshot.getString("Address");
                SPinCode=documentSnapshot.getString("PinCode");
                setAddress();
            }

            @Override
            public void onRemoveClick(DocumentSnapshot documentSnapshot, int position) {

            }
        });
    }
    private void setAddress() {
        DocumentReference documentReference=Store
                .collection("Users")
                .document(UserId);
        Map<String, Object> user = new HashMap<>();
        user.put("Name",SName);
        user.put("Phone",SPhone);
        user.put("PinCode",SPinCode);
        user.put("Address",SAddress);
        Toast.makeText(this, "Updating Address", Toast.LENGTH_SHORT).show();
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                Toast.makeText(AllAddresses.this, "Address is Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillItemRV();
    }
}