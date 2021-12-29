package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
    String DateCode,Date;
    ArrayList<YourMenuPayModel> DataList;
    FirebaseFirestore Store;
    String UserId,Address,Name,Phone,Email,shop,OrderId;
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
        OrderId=UserId.substring(0,3).toUpperCase()+getDateTime();
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
        Name=sharedPreferences.getString("Name","");
        Phone=sharedPreferences.getString("Phone","");
        Email=sharedPreferences.getString("Email","");
        shop=sharedPreferences.getString("Store","");
    }

    public void startPayment() {
        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_live_kXO9LArsRX4SYt");
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
            options.put("description", "Order ID: "+OrderId);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#F4C886");
            options.put("currency", "INR");
            options.put("amount", TotalPay);//pass amount in currency subunits
            options.put("prefill.contact", Phone);
            options.put("prefill.Email", Email);
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
            user1.put("TotalPay",TotalPay/100);
            user1.put("Code",DateCode);
            user1.put("Store",shop);
            user1.put("Email",Email);
            user1.put("Status","Pending");
            user1.put("UserId",UserId);
            user1.put("OrderId",OrderId);

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

                        startActivity(new Intent(Razorpay.this,YourOrder_1.class));
                        finish();
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
    private String getDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }
}