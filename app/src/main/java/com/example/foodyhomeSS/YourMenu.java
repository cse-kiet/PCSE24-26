package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    TextView TotalPayment;
    Button PlaceOrder;
    Integer TotalPay=0;
    ArrayList<YourMenuPayModel> DataList;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_menu);
        TotalPayment=findViewById(R.id.TotalPayment_YourMenu_TextView);
        PlaceOrder=findViewById(R.id.YourMenu_Place_Order_Button);
        DataList=new ArrayList<>();
        progressBar=findViewById(R.id.progressBar_YourMenu);

        recyclerView=findViewById(R.id.YourMenu_RecyclerView);
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        fillRecyclerView();
        adapter.startListening();

       updatePayment();
       PlaceOrder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               updatePayment();
               if (TotalPay>=200){
                   SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
                   @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
                   editor.putInt("TotalPay",TotalPay);
                   editor.apply();
                   startActivity(new Intent(YourMenu.this,Payment_Activity.class));
               }
               else{
                   Toast.makeText(YourMenu.this, "Total Amount should be greater than 300", Toast.LENGTH_SHORT).show();
               }

           }
       });

    }

    private void DownloadDataList() {
        progressBar.setVisibility(View.VISIBLE);
        TotalPayment.setVisibility(View.GONE);
        DataList.clear();

        Store.collection("Users")
                .document(UserId)
                .collection("YourMenu")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            YourMenuPayModel obj=d.toObject(YourMenuPayModel.class);
                            DataList.add(obj);
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void checkTotalPayment() {

        TotalPay=0;
        for(int i=0;i<DataList.size();i++) {
            int qty=Integer.parseInt(DataList.get(i).getQTY());
            String Ori = DataList.get(i).getPrice().replace("Rs ", "").replace("/-", "");
            TotalPay += (qty*Integer.parseInt(Ori));
        }
        TotalPayment.setText("Rs  "+TotalPay.toString()+"/-");
        TotalPayment.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
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
        AdapterSetOnClick();
    }

    private void AdapterSetOnClick() {
        adapter.setOnItemClickListener(new YourMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onIncreaseClick(DocumentSnapshot documentSnapshot, int position) {
                updatePayment();
            }

            @Override
            public void onDecreaseClick(DocumentSnapshot documentSnapshot, int position) {
                updatePayment();

            }

            @Override
            public void onRemoveClick(DocumentSnapshot documentSnapshot, int position) {
                updatePayment();

            }
        });

    }

    private void updatePayment() {
        DownloadDataList();
        new CountDownTimer(20000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if (DataList.size()!=0){
                    checkTotalPayment();
                    loadingDialog.dismissDialog();
                    cancel();
                }

            }

            @Override
            public void onFinish() {
                loadingDialog.dismissDialog();
                Toast.makeText(YourMenu.this, "Total Payment is not Updated !!!!" +
                        "please reopen YourMenu and Check your Connection", Toast.LENGTH_LONG).show();

            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        AdapterSetOnClick();
    }
}