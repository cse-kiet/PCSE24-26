package com.shorsam.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import io.grpc.internal.NoopClientStream;

public class YourOrder_1 extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore Store;
    String UserId,key;
    YourOder_1_Adapter adapter;
    LoadingDialog loadingDialog;
    TextView NoOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_1);
        recyclerView=findViewById(R.id.YourOrder1_RecyclerView);
        NoOrder=findViewById(R.id.No_Order_TextView_YourOrder1);
        Store= FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        fillItemRV();
        CheckPopulation();

    }

    private void CheckPopulation() {
        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if (recyclerView.getChildCount()==0){
                    NoOrder.setVisibility(View.VISIBLE);
                }
                else{
                    NoOrder.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFinish() {
                if (recyclerView.getChildCount()==0){
                    NoOrder.setVisibility(View.VISIBLE);
                }
                else{
                    NoOrder.setVisibility(View.INVISIBLE);
                }
            }
        }.start();
    }

    private void fillItemRV() {

        FirestoreRecyclerOptions<YourOrder_1_Model> options = new FirestoreRecyclerOptions.Builder<YourOrder_1_Model>()
                .setQuery(Store
                                .collection("Users")
                                .document(UserId)
                                .collection("YourOrders").orderBy("Code",Query.Direction.DESCENDING)
                        , YourOrder_1_Model.class)
                .build();
        adapter = new YourOder_1_Adapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        loadingDialog.dismissDialog();
        AdapterSetOnClick();
    }


    private void AdapterSetOnClick() {
        adapter.setOnItemClickListener(new YourOder_1_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                key=documentSnapshot.getReference().getId();
                SaveSharedPreferences();
                startActivity(new Intent(YourOrder_1.this,YourOrders.class));
            }

            @Override
            public void onTrackClick(DocumentSnapshot documentSnapshot, int position) {
                key=documentSnapshot.getReference().getId();
                SaveSharedPreferences();
                startActivity(new Intent(YourOrder_1.this,OrderTracking.class));
            }

            @Override
            public void onHelpClick(DocumentSnapshot documentSnapshot, int position) {
                startActivity(new Intent(YourOrder_1.this,ContactUs.class));

            }
        });
    }

    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("KeyYO1",key);
        editor.apply();
    }

}