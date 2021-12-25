package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Payment_Activity extends AppCompatActivity {
    Integer TotalPay=0,TotalQTY=0;
    RecyclerView ItemRV;
    FirebaseFirestore Store;
    String UserId;
    PaymentItemAdapter adapter;
    LoadingDialog loadingDialog;
    Button ChangeAddress,PlaceOrder,B10,B25,B50,B75,POD;
    TextView NameAS,AddressAS,PhoneAS,TotalItem,TotalPrice,OtherTaxes,SpecialDiscount,GrandTotal;
    String Info0,Name0,Phone0,Email0;
    int ITotal=0;
    ArrayList<YourMenuPayModel> DataList;
    Spinner spinnerSelectStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_);
        Store= FirebaseFirestore.getInstance();
        DataList=new ArrayList<YourMenuPayModel>();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        ItemRV=findViewById(R.id.Payment_Cart_item_RecyclerView);
        ChangeAddress=findViewById(R.id.Change_Address_Address_Section_Payment_Activity);
        NameAS=findViewById(R.id.Name_Address_Section_Activity_Payment);
        AddressAS=findViewById(R.id.Address_Address_Section_Activity_Payment);
        PhoneAS =findViewById(R.id.Phone_Address_Section_Activity_Payment);
        PlaceOrder=findViewById(R.id.Place_order_Payment_Activity_PayOnline);
        TotalItem=findViewById(R.id.Total_ItemsTV_Payment_Activity);
        TotalPrice=findViewById(R.id.Total_Price_TV_Payment_Activity);
        OtherTaxes=findViewById(R.id.Other_Taxes_TV_Payment_Activity);
        SpecialDiscount=findViewById(R.id.FoodyHome_Special_DiscountTV_Payment_Activity);
        GrandTotal=findViewById(R.id.Grand_TotalTV_DiscountTV_Payment_Activity);
        POD=findViewById(R.id.Place_order_Payment_Activity_POD);

        ChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Payment_Activity.this,AllAddresses.class));
            }
        });
        POD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(NameAS.getText().toString())){
                    NameAS.setError("Required");
                    Toast.makeText(Payment_Activity.this, "Name is Required", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(PhoneAS.getText().toString())){
                    PhoneAS.setError("Required");
                    Toast.makeText(Payment_Activity.this, "Phone Number is Required", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(AddressAS.getText().toString())){
                    AddressAS.setError("Required");
                    Toast.makeText(Payment_Activity.this, "Address is Required", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(NameAS.getText().toString()) && !TextUtils.isEmpty(PhoneAS.getText().toString()) && !TextUtils.isEmpty(AddressAS.getText().toString()))
                    if (Info0!=null){
                        Info0=Name0+Info0+Phone0;
                        SaveSharedPreferences();
                        startActivity(new Intent(Payment_Activity.this,SelectOrder2.class));
                    }
            }
        });
        PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(NameAS.getText().toString())){
                    NameAS.setError("Required");
                    Toast.makeText(Payment_Activity.this, "Name is Required", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(PhoneAS.getText().toString())){
                    PhoneAS.setError("Required");
                    Toast.makeText(Payment_Activity.this, "Phone Number is Required", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(AddressAS.getText().toString())){
                    AddressAS.setError("Required");
                    Toast.makeText(Payment_Activity.this, "Address is Required", Toast.LENGTH_SHORT).show();
                }
               if (!TextUtils.isEmpty(NameAS.getText().toString()) && !TextUtils.isEmpty(PhoneAS.getText().toString()) && !TextUtils.isEmpty(AddressAS.getText().toString()))
                    if (Info0!=null){
                        Info0=Name0+Info0+Phone0;
                        SaveSharedPreferences();
                        startActivity(new Intent(Payment_Activity.this,SelectStore.class));
                    }
            }
        });
        DownloadDataList();

        new CountDownTimer(10000,1000){

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onTick(long millisUntilFinished) {
                if (DataList!=null){
                    checkTotalPayment();
                    if (TotalPay!=0){
                        TotalPrice.setText(TotalPay.toString());
                        TotalItem.setText(TotalQTY.toString()+" Only");
                        double tax=(0.05*((float) TotalPay));
                        OtherTaxes.setText("+"+String.format("%.1f",tax));
                        double Discount=(0.05*((float) TotalPay));
                        SpecialDiscount.setText("-"+String.format("%.1f",Discount));
                        double Total=TotalPay;
                        ITotal = (int) Total;
                        GrandTotal.setText("Rs  "+ITotal);
                    }
                }

            }

            @Override
            public void onFinish() {

            }
        }.start();


        SetAddress();

        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        fillItemRV();
    }



    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("RazorPayTP",ITotal);
        editor.putString("Name",Name0);
        editor.putString("Phone",Phone0);
        editor.putString("Email",Email0);
        if (Info0!=null){
            editor.putString("Address",Info0);
        }
        else{
            editor.putString("Address","Address");
        }

        editor.apply();

    }



    private void DownloadDataList() {
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
        TotalQTY=0;
        for(int i=0;i<DataList.size();i++) {
            int qty=Integer.parseInt(DataList.get(i).getQTY());
            String Ori = DataList.get(i).getPrice().replace("Rs ", "").replace("/-", "");
            TotalPay += (qty*Integer.parseInt(Ori));
            TotalQTY+=qty;
        }
    }
    private void SetAddress() {
        DocumentReference documentReference=Store
                .collection("Users")
                .document(UserId);
        documentReference.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                Name0=value.getString("Name");
                NameAS.setText(Name0);
                Phone0=value.getString("Phone");
                Email0=value.getString("Email");
                PhoneAS.setText(Phone0);
                Info0=value.getString("Address")+", "+value.getString("PinCode")+ ", ";
                AddressAS.setText(Info0);
            }
        });
    }




    private void fillItemRV() {

        FirestoreRecyclerOptions<PaymentItemModel> options = new FirestoreRecyclerOptions.Builder<PaymentItemModel>()
                .setQuery(Store
                        .collection("Users")
                        .document(UserId)
                        .collection("YourMenu")
                        .orderBy("Code", Query.Direction.DESCENDING), PaymentItemModel.class)
                .build();
        adapter = new PaymentItemAdapter(options);
        ItemRV.setHasFixedSize(true);
        ItemRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        ItemRV.setAdapter(adapter);
        adapter.startListening();

        IsItemRecyclerViewFilled();
        AdapterSetOnClick();
    }

    private void AdapterSetOnClick() {
    }

    private void IsItemRecyclerViewFilled() {
        new CountDownTimer(10000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if (ItemRV.getChildCount()!=0){
                    loadingDialog.dismissDialog();
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                if (ItemRV.getChildCount()!=0){
                    loadingDialog.dismissDialog();
                }
                else{

                    Toast.makeText(Payment_Activity.this, "Check Your Connection and Restart FoodyHome", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

}