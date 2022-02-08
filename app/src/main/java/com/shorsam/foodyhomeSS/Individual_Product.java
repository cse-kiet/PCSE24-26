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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Individual_Product extends AppCompatActivity {
    ImageView PImage,Star1,Star2,Star3,Star4,Star5;
    TextView PName,PPRice,PDescription,MRP,Discount,AddOnTV;
    String SName;
    String SPrice;
    String SDescription;
    String SImage,RMLKey,Size;
    String UserID;
    String HomePID;
    String sMRP;
    String RecommendedProductID;
    String SRating;
    Float FRating;
    FirebaseFirestore Store;
    FirebaseAuth Auth;
    IndividualCategoryAdapter Cheesing;
    RMLAdapter AdapterRML;
    RecyclerView CheesingRV,RecommendedRV,RMLRecyclerView;
    AllProductAdapter RecommendedAdapter;
    View MRPView;
    LoadingDialog loadingDialog;
    ArrayList<String> PList,AddOns=new ArrayList<>();
   Integer TAddOn=0,temp=0;
    Button AddToMenuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual__product);
        //Loading dialog
        loadingDialog=new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        AddToMenuButton=findViewById(R.id.Add_to_Menu_Individual_Product);
        AddOns.clear();
        RMLKey="01";
        Size="Regular";
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
        RMLRecyclerView=findViewById(R.id.RML_RecyclerView);
        MRPView=findViewById(R.id.MRP_Individual_Product_View);
        AddOnTV=findViewById(R.id.TV_Choices_of_AddOn_IndividualActivity);
        Star1=findViewById(R.id.IndividualProduct2_Star_1);
        Star2=findViewById(R.id.IndividualProduct2_Star_2);
        Star3=findViewById(R.id.IndividualProduct2_Star_3);
        Star4=findViewById(R.id.IndividualProduct2_Star_4);
        Star5=findViewById(R.id.IndividualProduct2_Star_5);
        RMLRecyclerView.setVisibility(View.VISIBLE);
        fillDetails();


        new CountDownTimer(10000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                fillDetails();
                fillCheesing();
                fillRecommendedRV();
                fillRMLRecyclerView();
                if (!TextUtils.isEmpty(SName)){
                    loadingDialog.dismissDialog();
                    new CountDownTimer(5000,1000){

                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (CheesingRV.getChildCount()==0){
                                AddOnTV.setVisibility(View.VISIBLE);

                            }

                            else{
                                AddOnTV.setVisibility(View.GONE);
                            }
                            if (RMLRecyclerView.getChildCount()==1){
                                RMLRecyclerView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFinish() {
                            if (CheesingRV.getChildCount()==0){
                                AddOnTV.setVisibility(View.VISIBLE);
                            }
                            else{
                                AddOnTV.setVisibility(View.GONE);
                            }
                            if (RMLRecyclerView.getChildCount()==1){
                                RMLRecyclerView.setVisibility(View.GONE);

                            }
                            else{
                                RMLRecyclerView.setVisibility(View.VISIBLE);
                                fillRMLRecyclerView();
                            }

                        }
                    }.start();
                    AddToMenuButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         if (!TextUtils.isEmpty(SName)) {
                             AddToMenu();
                        }

                           // Toast.makeText(Individual_Product.this, "Clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (sMRP.equals(SPrice)){
                        MRP.setVisibility(View.GONE);
                        MRPView.setVisibility(View.GONE);
                    }
                    cancel();
                }

            }

            @Override
            public void onFinish() {
                loadingDialog.dismissDialog();
                Toast.makeText(Individual_Product.this, "Please Check your Connection and Restart FoodyHome", Toast.LENGTH_SHORT).show();

            }
        }.start();

    }

    private void fillRMLRecyclerView() {
        if (HomePID!=null) {
            FirebaseRecyclerOptions<RegularModel> options =
                    new FirebaseRecyclerOptions.Builder<RegularModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").child(HomePID).child("RML"), RegularModel.class)
                            .build();
            AdapterRML = new RMLAdapter(options);
            RMLRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            RMLRecyclerView.setAdapter(AdapterRML);
            AdapterRML.startListening();
            AdapterRML.setOnItemCLickListener(new RMLAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DataSnapshot dataSnapshot, int position) {
                    RMLKey = dataSnapshot.getKey();
                    Size= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
                    fillDetails();
                }
            });
//            checkRMLRecyclerView();
        }
    }

//    private void checkRMLRecyclerView() {
//        new CountDownTimer(10000,1000){
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                if (RMLRecyclerView.getChildCount()==0){
////                    Toast.makeText(Individual_Product.this, "GOne", Toast.LENGTH_SHORT).show();
//                    RMLRecyclerView.setVisibility(View.GONE);
//                }
//                else{
//                    RMLRecyclerView.setVisibility(View.VISIBLE);
//                }
//                Toast.makeText(Individual_Product.this, ""+RMLRecyclerView.getChildCount(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFinish() {
//                if (RMLRecyclerView.getChildCount()==0){
//                    RMLRecyclerView.setVisibility(View.GONE);
//                }
//                else{
//                    RMLRecyclerView.setVisibility(View.VISIBLE);
//                }
//            }
//        }.start();
//
//    }


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
                    public void onSuccess(@NonNull Void aVoid) {
                        loadingDialog2.dismissDialog();
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
        String json2=gson.toJson(AddOns);
        String json=gson.toJson(PList);
        editor.putString("PList",json);
        editor.putString("AddOns",json2);
        editor.apply();

    }
    private void LoadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson=new Gson();
        String jsonString=sharedPreferences.getString("PList","");
        String jsonString2=sharedPreferences.getString("AddOns","");
        Type type=new TypeToken<ArrayList<String>>() {}.getType();
         PList=gson.fromJson(jsonString,type);
         AddOns=gson.fromJson(jsonString2,type);
         if (AddOns==null){
             AddOns=new ArrayList<>();
         }
        if (PList==null){
            PList=new ArrayList<>();
        }
    }





    private void fillDetails() {
        if (HomePID!=null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Products");
            myRef.child(HomePID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SName = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();

                    SDescription = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                    SImage = Objects.requireNonNull(snapshot.child("Image").getValue()).toString();

                    SRating= Objects.requireNonNull(snapshot.child("Rating").getValue()).toString();
                    SRating=SRating.replace("+","");
                    FRating=Float.parseFloat(SRating);
                    PName.setText(SName);
                    PDescription.setText(SDescription);
                    Glide.with(PImage.getContext()).load(SImage).into(PImage);
                    SelectRating();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            myRef.child(HomePID).child("RML").child(RMLKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SPrice = Objects.requireNonNull(snapshot.child("Price").getValue()).toString();
                    sMRP= Objects.requireNonNull(snapshot.child("MRP").getValue()).toString();
                    PPRice.setText(SPrice);
                    MRP.setText(sMRP);
                    Discount.setText(Objects.requireNonNull(snapshot.child("Discount").getValue()).toString());
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
        AddOns.clear();
        SaveSharedPreferences();
            FirebaseRecyclerOptions<IndividualCategoryModel> options =
                    new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").child(HomePID).child("cheesing"), IndividualCategoryModel.class)
                            .build();
            Cheesing = new IndividualCategoryAdapter(options);
            CheesingRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
            CheesingRV.setAdapter(Cheesing);
            Cheesing.startListening();

            Cheesing.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {


                String s= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
                if (AddOns.contains(s)) {
                    AddOns.remove(s);
                    SaveSharedPreferences();
                }
                else{
                    String tAO= Objects.requireNonNull(dataSnapshot.child("Price").getValue()).toString()
                            .replace("Rs ","").replace("/-","");
                    temp=Integer.parseInt(tAO);
                    TAddOn+=temp;
                    AddOns.add(s);
                    SaveSharedPreferences();

                }

            }
            });

                }

    @Override
    protected void onResume() {
        super.onResume();
        AddOns.clear();
        TAddOn=0;
        LoadSharedPreferences();

        if (PList!=null) {
                try {
                    HomePID=PList.get(PList.size()-1);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

        }
        SaveSharedPreferences();
        clearRating();
        fillDetails();
        fillRecommendedRV();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        AddOns.clear();
        TAddOn=0;

        if (PList.size()>=1) {

            int index = PList.size() - 1;
            PList.remove(index);
            SaveSharedPreferences();
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
    private void AddToMenu(){
        loadingDialog.startLoadingDialog();
        FirebaseFirestore Store1;
        Store1=FirebaseFirestore.getInstance();
        DocumentReference documentReference=Store1.collection("Users").document(UserID);

//        loadingDialog.startLoadingDialog();
        Map<String, Object> user = new HashMap<>();
        user.put("Name", PName.getText().toString());
        String price=PPRice.getText().toString().replace("Rs ","").replace("/-","");
        temp=Integer.parseInt(price);
        user.put("Image", SImage);
        user.put("Discount",Discount.getText().toString() );
        user.put("MRP", sMRP);
        user.put("Code",getDateTime());
        user.put("QTY","1");
        user.put("Size",Size);
        LoadSharedPreferences();
        if (AddOns!=null){
            for (int i = 0; i < AddOns.size(); i++)
                user.put("AddOn"+i,AddOns.get(i));
        }
        user.put("Price","Rs " +String.valueOf(temp+TAddOn)+"/-");

        documentReference
                .collection("YourMenu")
                .document(getDateTime())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                loadingDialog.dismissDialog();
                startActivity(new Intent(Individual_Product.this,YourMenu.class));
            }
        });

    }
    private String getDateTime() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}



