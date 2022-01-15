package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectOrder2 extends AppCompatActivity {
    RecyclerView SelectStoreRV;
    SelectStoreAdapter adapter;
    LoadingDialog loadingDialog;
    String Store;
    FirebaseAuth Auth;
    String DateCode,Date;
    ArrayList<YourMenuPayModel> DataList;
    FirebaseFirestore FStore;
    String UserId,Address,Name,Phone,Email,shop,OrderId;
    Integer TotalPay=0;
    ArrayList<YourMenuModel> DataList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_order2);
        SelectStoreRV=findViewById(R.id.SelectStore2_RecyclerView);
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        fillSelectStoreRV();
        loadingDialog=new LoadingDialog(this);

        DataList=new ArrayList<YourMenuPayModel>();
        Auth=FirebaseAuth.getInstance();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FStore=FirebaseFirestore.getInstance();
        LoadSharedPreferences();
        DataList2=new ArrayList<YourMenuModel>();
        DownloadDataList();
        OrderId=UserId.substring(0,3).toUpperCase()+getDateTime();
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
                Store= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
                SaveSharedPreferences();
                SetDataListToYourOrders();
            }
        });
    }
    private void DownloadDataList() {
        DataList2.clear();
        DataList.clear();
        FStore.collection("Users")
                .document(UserId)
                .collection("YourMenu")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            YourMenuModel obj2=d.toObject(YourMenuModel.class);
                            DataList2.add(obj2);
                            YourMenuPayModel obj=d.toObject(YourMenuPayModel.class);
                            DataList.add(obj);
                        }
                    }
                });
    }
    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        TotalPay=sharedPreferences.getInt("RazorPayTP",0);
        Address=sharedPreferences.getString("Address","");
        Name=sharedPreferences.getString("Name","");
        Phone=sharedPreferences.getString("Phone","");
        Email=sharedPreferences.getString("Email","");
        shop=sharedPreferences.getString("Store","");
    }
    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Store",Store);
        editor.apply();
    }
    private void SetDataListToYourOrders(){
        loadingDialog.startLoadingDialog();
        FirebaseFirestore Store1;
        DateCode=getDateTime();
        Date=getDate();
        Store1=FirebaseFirestore.getInstance();
        Map<String, Object> user1 = new HashMap<>();
        DocumentReference documentReference=Store1
                .collection("Users")
                .document(UserId)
                .collection("YourOrders")
                .document(DateCode);
        DocumentReference documentReference2=Store1.collection("AllOrders")
                .document(OrderId);
        DocumentReference documentReference1=   Store1.collection("Store")
                .document(shop)
                .collection("Orders")
                .document(DateCode);
        CollectionReference collectionReference=
                documentReference
                        .collection(DateCode);
        CollectionReference collectionReference1=
                documentReference1
                        .collection(DateCode);
        CollectionReference collectionReference2=documentReference2.collection(DateCode);
        for (int i=0;i<DataList2.size();i++){

            Map<String, Object> user = new HashMap<>();
            user.put("Name", DataList2.get(i).getName());
            user.put("Price", DataList2.get(i).getPrice());
            user.put("Image", DataList2.get(i).getImage());
            user.put("MRP", DataList2.get(i).getMRP());
            user.put("QTY",DataList2.get(i).getQTY());
            user.put("Size",DataList2.get(i).getSize());
            if (DataList2.get(i).getAddOn0()!=null){
                user.put("AddOn0",DataList2.get(i).getAddOn0());
            }
            if (DataList2.get(i).getAddOn0()!=null){
                user.put("AddOn1",DataList2.get(i).getAddOn1());
            }
            if (DataList2.get(i).getAddOn0()!=null){
                user.put("AddOn2",DataList2.get(i).getAddOn2());
            }
            if (DataList2.get(i).getAddOn0()!=null){
                user.put("AddOn3",DataList2.get(i).getAddOn3());
            }
            collectionReference
                    .document(String.valueOf(i))
                    .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(@NonNull Void aVoid) {


                }
            });

            collectionReference1.document(String.valueOf(i))
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {

                        }
                    });
            collectionReference2.document(String.valueOf(i))
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {

                        }
                    });


        }
        if (Address!=null){
            user1.put("Address",Address);
        }
        user1.put("Date",Date);
        user1.put("TotalPay",TotalPay);
        user1.put("Code",DateCode);
        user1.put("Store",shop);
        user1.put("Email",Email);
        user1.put("Status","Pending");
        user1.put("UserId",UserId);
        user1.put("OrderId",OrderId);
        user1.put("Type","POD");

        documentReference
                .set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {

            }
        });
        documentReference2
                .set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                loadingDialog.dismissDialog();
            }
        });
        documentReference1.set(user1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void aVoid) {

                        startActivity(new Intent(SelectOrder2.this,YourOrder_1.class));
                        finish();
                    }
                });
    }
    private String getDateTime() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        java.util.Date date = new Date();
        return dateFormat.format(date);
    }
    private String getDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }
}