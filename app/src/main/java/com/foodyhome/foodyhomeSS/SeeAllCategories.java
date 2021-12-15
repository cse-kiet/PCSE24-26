package com.foodyhome.foodyhomeSS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SeeAllCategories extends AppCompatActivity {

    RecyclerView recyclerView;
    IndividualCategoryAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore Store;
    FirebaseAuth Auth;
    String UserID,SeeAll,ProductID;
    LoadingDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_categories);
        recyclerView = findViewById(R.id.SeeAllCategory_RecyclerView);
        progressBar=new LoadingDialog(this);
        progressBar.startLoadingDialog();

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        Auth = FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();
        UserID = Objects.requireNonNull(Auth.getCurrentUser()).getUid();
//        loadingDialog.startLoadingDialog();
        DocumentReference documentReference = Store.collection("Users").document(UserID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if (value.getString("SeeAll") != null) {
                    SeeAll = Objects.requireNonNull(value.getString("SeeAll")).trim();
                }
            }
        });
        fillRecyclerView();
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if (recyclerView.getChildCount()!=0){
                    progressBar.dismissDialog();
                    cancel();
                }
                else{
                    fillRecyclerView();
                }
                

            }

            @Override
            public void onFinish() {
                if (recyclerView.getChildCount()!=0){
                    progressBar.dismissDialog();
                    cancel();
                }
                else{
                    progressBar.dismissDialog();
                    Toast.makeText(SeeAllCategories.this, "No Internet, check your Connection and Restart FoodyHome", Toast.LENGTH_SHORT).show();
                }

            }
        }.start();


    }

    private void fillRecyclerView() {
        if (SeeAll != null) {

            FirebaseRecyclerOptions<IndividualCategoryModel> options =
                    new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("PizzaTreat"), IndividualCategoryModel.class)
                            .build();
            adapter = new IndividualCategoryAdapter(options);
            layoutManager = new GridLayoutManager(SeeAllCategories.this, 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.startListening();
            adapter.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DataSnapshot dataSnapshot, int position) {
                    progressBar.startLoadingDialog();
                    SeeAll= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString().toLowerCase();

                    Map<String, Object> user = new HashMap<>();
                    user.put("SeeAll", SeeAll);
                    String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    DocumentReference documentReference=Store.collection("Users").document(UserId);
                    documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressBar.dismissDialog();
                            startActivity(new Intent(SeeAllCategories.this,AllProduct.class));
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillRecyclerView();
    }
}