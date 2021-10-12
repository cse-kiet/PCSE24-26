package com.example.foodyhomeSS;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
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

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Payment_Activity extends AppCompatActivity {
    Integer TotalPay=0,TotalQTY=0;
    RecyclerView ItemRV;
    FirebaseFirestore Store;
    String UserId;
    PaymentItemAdapter adapter;
    LoadingDialog loadingDialog;
    Button ChangeAddress,PlaceOrder,B10,B25,B50,B75;
    TextView NameAS,AddressAS,PhoneAS,TotalItem,TotalPrice,OtherTaxes,SpecialDiscount,GrandTotal;
    String Info0,Info1,Info2,Info3,Info4,Phone0;
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
        PlaceOrder=findViewById(R.id.Place_order_Payment_Activity);
        TotalItem=findViewById(R.id.Total_ItemsTV_Payment_Activity);
        TotalPrice=findViewById(R.id.Total_Price_TV_Payment_Activity);
        OtherTaxes=findViewById(R.id.Other_Taxes_TV_Payment_Activity);
        SpecialDiscount=findViewById(R.id.FoodyHome_Special_DiscountTV_Payment_Activity);
        GrandTotal=findViewById(R.id.Grand_TotalTV_DiscountTV_Payment_Activity);
        spinnerSelectStore = findViewById(R.id.spinner_Payment_Activity);
        setInfoString();
        setSpinnerAdapter();
        ChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Payment_Activity.this,AllAddresses.class));
            }
        });
        PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(NameAS.getText().toString())){
                    NameAS.setError("Required");
                }
                if(TextUtils.isEmpty(PhoneAS.getText().toString())){
                    PhoneAS.setError("Required");
                }
                if(TextUtils.isEmpty(AddressAS.getText().toString())){
                    AddressAS.setError("Required");
                }
               if (!TextUtils.isEmpty(NameAS.getText().toString()) && !TextUtils.isEmpty(PhoneAS.getText().toString()) && !TextUtils.isEmpty(AddressAS.getText().toString()))
                    if (Info0!=null){
                        Info0=Info0+Phone0;
                        SaveSharedPreferences();
                        startActivity(new Intent(Payment_Activity.this,Razorpay.class));
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
                        double Discount=(0.01*((float) TotalPay));
                        SpecialDiscount.setText("-"+String.format("%.1f",Discount));
                        double Total=TotalPay+tax-Discount;
                        ITotal = (int) Total+1;
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

    private void setSpinnerAdapter() {

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Select_Store, R.layout.color_spinner);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerSelectStore.setPrompt("Select a Store");
        spinnerSelectStore.setAdapter(adapter);
        spinnerSelectStore.setPrompt("Select Store");
        spinnerSelectStore.setSelection(0);
        spinnerSelectStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:{
                        Toast.makeText(Payment_Activity.this, "Pizza Buzzer", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 2:{
                        Toast.makeText(Payment_Activity.this, "The Pizza Station", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("RazorPayTP",ITotal);
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
                NameAS.setText(value.getString("Name"));
                PhoneAS.setText(value.getString("Phone"));
                Info0=value.getString("Address")+", "+value.getString("PinCode")+ ", ";
                AddressAS.setText(Info0);
            }
        });
    }

    private void setInfoString() {

        DocumentReference documentReference=Store
                                            .collection("Users")
                                            .document(UserId);
        documentReference.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;


                    Info1=value.getString("Name")+", "+value.getString("Phone")+", "+value.getString("Address")+", "+value.getString("PinCode");
                    Phone0=value.getString("Phone");
                    Info2=value.getString("Name1")+", "+value.getString("Phone1")+", "+value.getString("Address1")+", "+value.getString("PinCode1");

                if (value.getString("Address2")!=null&& value.getString("Phone2")!=null&&value.getString("PinCode2")!=null){
                    Info3=value.getString("Name2")+", "+value.getString("Phone2")+", "+value.getString("Address2")+", "+value.getString("PinCode2");

                }
                if (value.getString("Address3")!=null&& value.getString("Phone3")!=null&&value.getString("PinCode3")!=null){
                    Info4=value.getString("Name3")+", "+value.getString("Phone3")+", "+value.getString("Address3")+", "+value.getString("PinCode3");

                }
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