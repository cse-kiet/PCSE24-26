package com.shorsam.foodyhomeSS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class OrderTracking extends AppCompatActivity {
    FirebaseFirestore Store;
    FirebaseAuth Auth;
    String Status,UserID,Key;
    LoadingDialog progressBar;
    ImageView Pending,Packed,Completed,Shipped;
    View VPacked,VPending,VShipped;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);
        progressBar=new LoadingDialog(this);
        progressBar.startLoadingDialog();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Auth = FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();
        UserID = Objects.requireNonNull(Auth.getCurrentUser()).getUid();
        Pending=findViewById(R.id.OrderTracking_OrderPlaced);
        Packed=findViewById(R.id.OrderTracking_OrderPacked);
        Shipped=findViewById(R.id.OrderTracking_OrderShipped);
        Completed=findViewById(R.id.OrderTracking_OrderCompleted);
        VPacked=findViewById(R.id.OrderTracking_Packed_View);
        VPending=findViewById(R.id.OrderTracking_Placed_View);
        VShipped=findViewById(R.id.OrderTracking_Shipped_View);
        LoadSharedPreferences();
        DocumentReference documentReference = Store.collection("Users").document(UserID)
                .collection("YourOrders")
                .document(Key);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if (value.getString("Status") != null) {
                    Status = Objects.requireNonNull(value.getString("Status")).trim();

                }
            }
        });
        new CountDownTimer(10000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if (Status!=null){
                    UpdateStatusToScreen();
                    progressBar.dismissDialog();
                    cancel();
                }

            }

            @Override
            public void onFinish() {
                if (Status!=null){
                    UpdateStatusToScreen();
                    progressBar.dismissDialog();
                    cancel();
                }
                else{
                    Toast.makeText(OrderTracking.this, "Sorry!! " +
                            "Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();

    }

    private void UpdateStatusToScreen() {
        switch (Status){
            case "Packed":{
                VPending.setBackgroundResource(R.color.purple_700);
                Pending.setImageResource(R.drawable.ic_twotone_check_circle_24);
                Packed.setImageResource(R.drawable.ic_twotone_check_circle_24);
                break;
            }
            case "Shipped":{
                VPending.setBackgroundResource(R.color.purple_700);
                VPacked.setBackgroundResource(R.color.purple_700);
                Pending.setImageResource(R.drawable.ic_twotone_check_circle_24);
                Packed.setImageResource(R.drawable.ic_twotone_check_circle_24);
                Shipped.setImageResource(R.drawable.ic_twotone_check_circle_24);
                break;
            }
            case "Completed":{
                VPending.setBackgroundResource(R.color.purple_700);
                VPacked.setBackgroundResource(R.color.purple_700);
                VShipped.setBackgroundResource(R.color.purple_700);
                Pending.setImageResource(R.drawable.ic_twotone_check_circle_24);
                Packed.setImageResource(R.drawable.ic_twotone_check_circle_24);
                Shipped.setImageResource(R.drawable.ic_twotone_check_circle_24);
                Completed.setImageResource(R.drawable.ic_twotone_check_circle_24);
                break;
            }
            default:{
                Pending.setImageResource(R.drawable.ic_twotone_check_circle_24);
                Toast.makeText(this, ""+Status, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Key=sharedPreferences.getString("KeyYO1","");
    }
}