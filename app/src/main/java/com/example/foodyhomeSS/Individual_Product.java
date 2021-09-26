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
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Individual_Product extends AppCompatActivity {
    ImageView PImage,Star1,Star2,Star3,Star4,Star5;
    TextView PName,PPRice,PDescription,MRP,Discount;
    String SName;
    String SPrice;
    String SDescription;
    String SImage;
    String UserID;
    String HomePID;
    String sMRP;
    String RecommendedProductID;
    String SRating;
    Float FRating;
    FirebaseFirestore Store;
    FirebaseAuth Auth;
    IndividualCategoryAdapter Cheesing;
    RecyclerView CheesingRV,RecommendedRV;
    AllProductAdapter RecommendedAdapter;
    View MRPView;

    ArrayList<String> PList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual__product);
        //Loading dialog
        LoadingDialog loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Products");
        Auth = FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();
        UserID = Objects.requireNonNull(Auth.getCurrentUser()).getUid();
        LoadSharedPreferences();
        DocumentReference documentReference = Store.collection("Users").document(UserID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if (value.getString("HomePID") != null) {
                    HomePID = Objects.requireNonNull(value.getString("HomePID")).trim();
                }
            }
        });

        PImage=findViewById(R.id.Individual_Product_Activity_Product_Image);
        PPRice=findViewById(R.id.Individual_Product_Activity_Product_Price);
        PName=findViewById(R.id.Individual_Product_Activity_Product_Name);
        PDescription=findViewById(R.id.Individual_Product_Activity_Product_Description);
        CheesingRV = findViewById(R.id.Individual_Product_Activity_AddOn_RV);
        MRP=findViewById(R.id.Individual_Product_Activity_Product_MRP);
        Discount=findViewById(R.id.Individual_Product_Activity_Product_Discount);
        RecommendedRV=findViewById(R.id.Individual_Product_Recommended_Product_RecyclerView);
        MRPView=findViewById(R.id.MRP_Individual_Product_View);
        Star1=findViewById(R.id.Individual_Product_Star_1);
        Star2=findViewById(R.id.Individual_Product_Star_2);
        Star3=findViewById(R.id.Individual_Product_Star_3);
        Star4=findViewById(R.id.Individual_Product_Star_4);
        Star5=findViewById(R.id.Individual_Product_Star_5);
        fillDetails();

        new CountDownTimer(10000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                fillDetails();
                fillRecommendedRV();
                if (!TextUtils.isEmpty(SName)){
                    loadingDialog.dismissDialog();
                    if (sMRP.equals(SPrice)){
                        MRP.setVisibility(View.GONE);
                        MRPView.setVisibility(View.GONE);
                    }
                    cancel();
                }

            }

            @Override
            public void onFinish() {
                Toast.makeText(Individual_Product.this, "Please Check your Connection and Restart FoodyHome", Toast.LENGTH_SHORT).show();

            }
        }.start();


    }

    private void fillRecommendedRV() {
        FirebaseRecyclerOptions<AllProductModel> options =
                new FirebaseRecyclerOptions.Builder<AllProductModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products"), AllProductModel.class)
                        .build();
        RecommendedAdapter = new AllProductAdapter(options);
        RecommendedRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        RecommendedRV.setAdapter(RecommendedAdapter);
        RecommendedAdapter.startListening();
        RecommendedAdapter.setOnItemCLickListener(new AllProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                LoadingDialog loadingDialog2=new LoadingDialog(Individual_Product.this);
                loadingDialog2.startLoadingDialog();
                RecommendedProductID= Objects.requireNonNull(dataSnapshot.getKey());
                Map<String, Object> user = new HashMap<>();
                user.put("HomePID", RecommendedProductID);
                String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                DocumentReference documentReference=Store.collection("Users").document(UserId);
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingDialog2.dismissDialog();
                        Toast.makeText(Individual_Product.this, HomePID+" is added", Toast.LENGTH_SHORT).show();
                        PList.add(HomePID);
                        SaveSharedPreferences();
                        startActivity(new Intent(Individual_Product.this,Individual_Product.class));
                    }
                });
            }
        });
    }

    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(PList);
        editor.putString("PList",json);
        editor.apply();

    }
    private void LoadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson=new Gson();
        String jsonString=sharedPreferences.getString("PList","");
        Type type=new TypeToken<ArrayList<String>>() {}.getType();
         PList=gson.fromJson(jsonString,type);
        if (PList==null){
            PList=new ArrayList<>();
        }
    }


    private void SendProductID() {

    }


    private void fillDetails() {
        if (HomePID!=null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Products");
            myRef.child(HomePID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SName = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                    SPrice = Objects.requireNonNull(snapshot.child("Price").getValue()).toString();
                    SDescription = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                    SImage = Objects.requireNonNull(snapshot.child("Image").getValue()).toString();
                    sMRP= Objects.requireNonNull(snapshot.child("MRP").getValue()).toString();
                    Discount.setText(Objects.requireNonNull(snapshot.child("Discount").getValue()).toString());
                    SRating= Objects.requireNonNull(snapshot.child("Rating").getValue()).toString();
                    SRating=SRating.replace("+","");
                    FRating=Float.parseFloat(SRating);
                    MRP.setText(sMRP);
                    PName.setText(SName);
                    PDescription.setText(SDescription);
                    PPRice.setText(SPrice);
                    Glide.with(PImage.getContext()).load(SImage).into(PImage);
                    SelectRating();

                    fillCheesing();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void SelectRating() {
        if (FRating<=0.5){
            Star1.setImageResource(R.drawable.ic_baseline_star_half_24);
            Star1.setVisibility(View.VISIBLE);
        }
        if (FRating<=1.0 && FRating>0.5){
            Star1.setVisibility(View.VISIBLE);
        }
        if (FRating<=1.5 && FRating>1.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star2.setImageResource(R.drawable.ic_baseline_star_half_24);
        }
        if (FRating<=2.0 && FRating>1.5){
            Star2.setVisibility(View.VISIBLE);
            Star1.setVisibility(View.VISIBLE);
        }
        if (FRating<=2.5 && FRating>2.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star3.setImageResource(R.drawable.ic_baseline_star_half_24);
        }
        if (FRating<=3.0 && FRating>2.5){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
        }
        if (FRating<=3.5 && FRating>3.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.VISIBLE);
            Star4.setImageResource(R.drawable.ic_baseline_star_half_24);
        }
        if (FRating<=4.0 && FRating>3.5){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.VISIBLE);
        }
        if (FRating<=4.5 && FRating>4.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.VISIBLE);
            Star5.setVisibility(View.VISIBLE);
            Star5.setImageResource(R.drawable.ic_baseline_star_half_24);
        }

    }

    private void fillCheesing() {


            FirebaseRecyclerOptions<IndividualCategoryModel> options =
                    new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Cheesing"), IndividualCategoryModel.class)
                            .build();
            Cheesing = new IndividualCategoryAdapter(options);
            CheesingRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
            CheesingRV.setAdapter(Cheesing);
            Cheesing.startListening();

                }

    @Override
    protected void onResume() {
        super.onResume();
        LoadSharedPreferences();

        if (PList!=null) {
                try {
                    HomePID=PList.get(PList.size()-1);
                    Toast.makeText(this, HomePID, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

        }
        clearRating();
        fillDetails();
        fillRecommendedRV();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (PList.size()>=1) {
            Toast.makeText(this, PList.size() - 1 + " is Removed", Toast.LENGTH_SHORT).show();
            int index = PList.size() - 1;
            PList.remove(index);
            if (PList != null) {
                SaveSharedPreferences();
            }
        }
    }

    private void clearRating() {
        Star1.setVisibility(View.GONE);
        Star2.setVisibility(View.GONE);
        Star3.setVisibility(View.GONE);
        Star4.setVisibility(View.GONE);
        Star5.setVisibility(View.GONE);
        Star1.setImageResource(R.drawable.ic_baseline_star_24);
        Star2.setImageResource(R.drawable.ic_baseline_star_24);
        Star3.setImageResource(R.drawable.ic_baseline_star_24);
        Star4.setImageResource(R.drawable.ic_baseline_star_24);
        Star5.setImageResource(R.drawable.ic_baseline_star_24);
    }
}



