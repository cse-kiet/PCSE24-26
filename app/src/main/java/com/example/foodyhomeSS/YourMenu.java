package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YourMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<YourMenuModel> DataList;
    FirebaseFirestore Store;
    String UserId;
    YourMenuAdapter adapter;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_menu);
        DataList=new ArrayList<>();
        recyclerView=findViewById(R.id.YourMenu_RecyclerView);
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new YourMenuAdapter(DataList,this);
        recyclerView.setAdapter(adapter);
        AdapterSetOnClick();
        Store=FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Store.collection("Users")
                .document(UserId)
                .collection("YourMenu")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d:list){
                            YourMenuModel obj=d.toObject(YourMenuModel.class);
                            DataList.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                        AdapterSetOnClick();
                        loadingDialog.dismissDialog();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismissDialog();
                Toast.makeText(YourMenu.this, "Please check your connection" +
                        "       or" +
                        "Restart the App", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void AdapterSetOnClick() {
        adapter.setOnItemClickListener(new YourMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onIncreaseClick(int position) {


            }

            @Override
            public void onDecreaseClick(int position) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        AdapterSetOnClick();
    }
}