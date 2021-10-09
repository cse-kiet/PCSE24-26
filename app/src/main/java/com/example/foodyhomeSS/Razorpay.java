package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Razorpay extends AppCompatActivity  implements PaymentResultListener {
    TextView Paytext;
    FirebaseAuth Auth;
    String currentuser;
    ArrayList<YourMenuPayModel> DataList;
    FirebaseFirestore Store;
    String UserId,Address;
    LoadingDialog loadingDialog;
    Integer TotalPay=0;
    ArrayList<YourMenuModel> DataList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);
        Checkout.preload(getApplicationContext());
        loadingDialog=new LoadingDialog(this);
        Paytext=findViewById(R.id.textview);
        DataList=new ArrayList<YourMenuPayModel>();
        Auth=FirebaseAuth.getInstance();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Store=FirebaseFirestore.getInstance();
        LoadSharedPreferences();
        loadingDialog.startLoadingDialog();
        DataList2=new ArrayList<YourMenuModel>();
        DownloadDataList();
        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if (TotalPay!=0){
                    TotalPay*=100;
                    loadingDialog.dismissDialog();
                    startPayment();
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                if (TotalPay!=0){
                    TotalPay*=100;
                    loadingDialog.dismissDialog();
                    startPayment();
                    cancel();
                }
                else{
                    Toast.makeText(Razorpay.this, "OOPs !!!!" +
                            "Some Error has occurred Please Go Back and Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();


    }
    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        TotalPay=sharedPreferences.getInt("RazorPayTP",0);
        Address=sharedPreferences.getString("Address","");
    }

    public void startPayment() {
        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_test_LAbzilpeXRuXB9");
        /**
         * Instantiate Checkout
         */

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo_dark);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "FoodyHome");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#F4C886");
            options.put("currency", "INR");
            options.put("amount", TotalPay);//pass amount in currency subunits
            options.put("prefill.contact", "8218305987");
            options.put("prefill.Email", "shoryagarg69@gmail.com");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);
            loadingDialog.dismissDialog();

        } catch (Exception e) {
            Log.e("TAG", "Error in Payment Checkout! Please Try Again", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        SetDataListToYourOrders();
        Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
    }
    private void DownloadDataList() {
        DataList2.clear();
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
                            YourMenuModel obj2=d.toObject(YourMenuModel.class);
                            DataList2.add(obj2);
                            YourMenuPayModel obj=d.toObject(YourMenuPayModel.class);
                            DataList.add(obj);
                        }
                    }
                });
    }
    private void SetDataListToYourOrders(){
        loadingDialog.startLoadingDialog();
        FirebaseFirestore Store1;
        Store1=FirebaseFirestore.getInstance();
        DocumentReference documentReference=Store1.collection("Users").document(UserId);
        Map<String, Object> user = new HashMap<>();
        for (int i=0;i<DataList2.size();i++){
                user.put("Name"+i, DataList2.get(i).getName());
                user.put("Price"+i, DataList2.get(i).getPrice());
                user.put("Image"+i, DataList2.get(i).getImage());
                user.put("MRP"+i, DataList2.get(i).getMRP());
                user.put("QTY"+i,DataList2.get(i).getQTY());
                if (DataList2.get(i).getAddOn0()!=null){
                    user.put("AddOn0"+i,DataList2.get(i).getAddOn0());
                }
                if (DataList2.get(i).getAddOn0()!=null){
                    user.put("AddOn1"+i,DataList2.get(i).getAddOn1());
                }
                if (DataList2.get(i).getAddOn0()!=null){
                    user.put("AddOn2"+i,DataList2.get(i).getAddOn2());
                }
                if (DataList2.get(i).getAddOn0()!=null){
                    user.put("AddOn3"+i,DataList2.get(i).getAddOn3());
                }
        }
            if (Address!=null){
                user.put("Address",Address);
            }

        documentReference
                .collection("YourOrders")
                .document(getDateTime())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                loadingDialog.dismissDialog();
                finishAffinity();
                startActivity(new Intent(Razorpay.this,MainActivity.class));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPaymentError(int i, String s) {

        Paytext.setText("Payment is Failed due to " +s);
    }
    private String getDateTime() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}