package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class profile extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore Store;
    String UserId,SName,SPhone,SAddress,SPinCode;
    AllAddressAdapter adapter;
    LoadingDialog loadingDialog;
    Button AddAddress,EditProfile;
    TextView DName,DAddress,DPhone,PName,PEmail,PPhone;
    int count,Selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AddAddress=findViewById(R.id.Add_Address_Profile_Button);
        DName=findViewById(R.id.Name_Default_Address_Profile);
        DPhone=findViewById(R.id.Phone_Default_Address_Profile);
        DAddress=findViewById(R.id.Address_Default_Address_Profile);
        EditProfile=findViewById(R.id.EditProfileButton_Profile);
        PName=findViewById(R.id.Name_Profile);
        PEmail=findViewById(R.id.Email_Profile);
        PPhone=findViewById(R.id.Phone_Profile);
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this, SignUp.class));
            }
        });
        Store= FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        fillDefaultAddress();
        AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this,EditAddress.class));
            }
        });
        recyclerView=findViewById(R.id.Profile_RV_for_Address);


        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        fillItemRV();
    }

    private void fillDefaultAddress() {
        DocumentReference documentReference=Store
                .collection("Users")
                .document(UserId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;

                DName.setText(Objects.requireNonNull(value.get("Name")).toString());
                DPhone.setText(Objects.requireNonNull(value.get("Phone")).toString());
                DAddress.setText(Objects.requireNonNull(value.get("Address")).toString());
                PName.setText(value.getString("Name"));
                PPhone.setText(value.getString("Phone"));
                PEmail.setText(value.getString("Email"));
            }
        });
    }

    private void fillItemRV() {
        FirestoreRecyclerOptions<AllAddressModel> options = new FirestoreRecyclerOptions.Builder<AllAddressModel>()
                .setQuery(Store
                                .collection("Users")
                                .document(UserId)
                                .collection("Address")
                        , AllAddressModel.class)
                .build();
        adapter = new AllAddressAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        loadingDialog.dismissDialog();
        AdapterSetOnClick();
    }
    private void AdapterSetOnClick() {
        adapter.setOnItemClickListener(new AllAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SName=documentSnapshot.getString("Name");
                SPhone=documentSnapshot.getString("Phone");
                SAddress=documentSnapshot.getString("Address");
                SPinCode=documentSnapshot.getString("PinCode");
                setAddress();
                fillDefaultAddress();

            }

            @Override
            public void onRemoveClick(DocumentSnapshot documentSnapshot, int position) {
                Toast.makeText(profile.this, "Removed", Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void setAddress() {
        DocumentReference documentReference=Store
                .collection("Users")
                .document(UserId);
        Map<String, Object> user = new HashMap<>();
        user.put("Name",SName);
        user.put("Phone",SPhone);
        user.put("PinCode",SPinCode);
        user.put("Address",SAddress);
        Toast.makeText(this, "Updating Address", Toast.LENGTH_SHORT).show();
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                Toast.makeText(profile.this, "Address is Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

}