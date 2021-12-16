package com.samshor.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class YourOrders extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore Store;
    String UserId,Key;
    YourMenu2Adapter adapter;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);
        recyclerView=findViewById(R.id.YourOrders2_RecyclerView);
        Store= FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        LoadSharedPreferences();
        if (Key!=null){
            fillItemRV();
        }

    }
    private void fillItemRV() {

        FirestoreRecyclerOptions<YourOrder2Model> options = new FirestoreRecyclerOptions.Builder<YourOrder2Model>()
                .setQuery(Store
                                .collection("Users")
                                .document(UserId)
                                .collection("YourOrders")
                                .document(Key)
                                .collection(Key)
                                , YourOrder2Model.class)
                .build();
        adapter = new YourMenu2Adapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        loadingDialog.dismissDialog();
        AdapterSetOnClick();
    }

    private void AdapterSetOnClick() {
    }
    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Key=sharedPreferences.getString("KeyYO1","");
    }
}