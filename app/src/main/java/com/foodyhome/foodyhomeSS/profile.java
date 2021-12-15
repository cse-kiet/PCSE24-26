package com.foodyhome.foodyhomeSS;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        Store= FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.Profile_RV_for_Address);
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this, SignUp.class));
            }
        });

        fillDefaultAddress();
        AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this,EditAddress.class));
            }
        });

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

                if (value.get("Name")!=null){
                    PName.setText(Objects.requireNonNull(value.getString("Name")));
                    DName.setText(Objects.requireNonNull(value.get("Name")).toString());
                }

                if (value.get("Phone")!=null){
                    PPhone.setText(Objects.requireNonNull(value.getString("Phone")));
                    DPhone.setText(Objects.requireNonNull(value.get("Phone")).toString());
                }
                if (value.get("Address")!=null){
                    DAddress.setText(Objects.requireNonNull(value.get("Address")).toString());
                    PEmail.setText(Objects.requireNonNull(value.getString("Email")));
                }


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
                SName=Objects.requireNonNull(documentSnapshot.getString("Name"));
                SPhone=Objects.requireNonNull(documentSnapshot.getString("Phone"));
                SAddress=Objects.requireNonNull(documentSnapshot.getString("Address"));
                SPinCode=Objects.requireNonNull(documentSnapshot.getString("PinCode"));
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