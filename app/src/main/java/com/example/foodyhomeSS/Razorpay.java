package com.example.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Razorpay extends AppCompatActivity  implements PaymentResultListener {
    TextView Paytext;
    FirebaseAuth Auth;
    String currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);
        Checkout.preload(getApplicationContext());
        Paytext=findViewById(R.id.textview);
        startPayment();
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
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "foody home");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "50000");//pass amount in currency subunits
            options.put("prefill.contact", "8218305987");
            options.put("prefill.Email", "shoryagarg69@gmail.com");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
Paytext.setText("successful payment id" +s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Paytext.setText("Failed and cause is" +s);
    }
}