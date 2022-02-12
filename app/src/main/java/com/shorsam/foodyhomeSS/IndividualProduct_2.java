package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IndividualProduct_2 extends AppCompatActivity implements View.OnClickListener{
    ImageSlider ProductImages;
    List<SlideModel> sliderImages=new ArrayList<SlideModel>();
    FloatingActionButton WishList,Share;
    ImageView Back,Cart,Orders,Star1,Star2,Star3,Star4,Star5,SStar1,SStar2,SStar3,SStar4,SStar5;
    RecyclerView RML_RV,AddOnRV,RecommendedRV;
    RMLAdapter AdapterRML;
    AllProductAdapter RecommendedAdapter;
    AddOnRecyclerViewAdapter AddOnAdapter;
    String HomePID,SName,SDescription,SRating,SPrice,sMRP,SStoreName,SStoreTiming,SStoreDeliveryTime,SStoreRating;
    Float FRating,FStoreRating;
    int minimumDeliveryAMt;
    ArrayList<String> PList,AddOns=new ArrayList<>();
    TextView PName,PDescription,PPrice,MRP,Discount,Delivery_Status,StoreName,StoreTiming ,StoreDeliveryTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product_2);
        ProductImages=findViewById(R.id.SliderView_IndividualProduct2);
        PName=findViewById(R.id.ProductName_IndividualProduct2_TextView);
        PPrice=findViewById(R.id.ProductPrice_IndividualProduct2_TextView);
        PDescription=findViewById(R.id.Description_IndividualProduct2_TextView);
        MRP=findViewById(R.id.MRP_IndividualProduct2_TextView);
        Discount=findViewById(R.id.Discount_IndividualProduct2_TextView);
        Delivery_Status=findViewById(R.id.DeliveryStatus_IndividualProduct2_TextView);
        StoreName=findViewById(R.id.StoreName_IndividualProduct2_TextView);
        StoreTiming=findViewById(R.id.Timing_IndividualProduct2_TextView);
        StoreDeliveryTime=findViewById(R.id.Store_DeliveryTime_IndividualProduct2_TextView);
        Star1=findViewById(R.id.IndividualProduct2_Star_1);
        Star2=findViewById(R.id.IndividualProduct2_Star_2);
        Star3=findViewById(R.id.IndividualProduct2_Star_3);
        Star4=findViewById(R.id.IndividualProduct2_Star_4);
        Star5=findViewById(R.id.IndividualProduct2_Star_5);
        SStar1=findViewById(R.id.Store_Star1_IndividualProduct2_ImageView);
        SStar2=findViewById(R.id.Store_Star2_IndividualProduct2_ImageView);
        SStar3=findViewById(R.id.Store_Star3_IndividualProduct2_ImageView);
        SStar4=findViewById(R.id.Store_Star4_IndividualProduct2_ImageView);
        SStar5=findViewById(R.id.Store_Star5_IndividualProduct2_ImageView);
        WishList=findViewById(R.id.AddToWhilist_IndividualProductFloatingActionButton);
        Share=findViewById(R.id.Share_IndividualProduct_FloatingActionButton);
        Back=findViewById(R.id.BackButton_IndividualProduct_ImageView);
        Cart=findViewById(R.id.Cart_IndividualProduct_ImageView);
        Orders=findViewById(R.id.YourOrders_IndividualProduct_ImageView);
        RML_RV=findViewById(R.id.RML_RecyclerView_IndividualProduct2_ImageView);
        RecommendedRV=findViewById(R.id.RecommendedProduct_IndividualProduct2_RecyclerView);
        AddOnRV=findViewById(R.id.AddOnRecyclerView_IndividualProduct2_ImageView);
        WishList.setOnClickListener(this);
        Share.setOnClickListener(this);
        Back.setOnClickListener(this);
        Cart.setOnClickListener(this);
        Orders.setOnClickListener(this);

        LoadSharedPreferences();


//        new CountDownTimer(1000,100){
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                if (HomePID!=null){
//                    cancel();
//                    fillSlider();
//                    fillRMLRecyclerView();
//                    fillAddOnRV();
////        fillRecommendedRV();
////        fillDetails();
//                }
//
//            }
//
//            @Override
//            public void onFinish() {
//
//
//            }
//        }.start();
        if (HomePID!=null){

            fillSlider();
            fillRMLRecyclerView();
            fillAddOnRV();
        fillRecommendedRV();
        fillDetails();
        }

    }

    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        editor.putString("HomePID",HomePID);
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
        HomePID=sharedPreferences.getString("HomePID","");
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
            DatabaseReference myRef = database.getReference().child("AllProducts");
            myRef.child(HomePID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SName = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                    SStoreName=Objects.requireNonNull(snapshot.child("StoreName").getValue()).toString();
                    SStoreTiming=Objects.requireNonNull(snapshot.child("StoreTiming").getValue()).toString();
                    SStoreDeliveryTime=Objects.requireNonNull(snapshot.child("StoreDeliveryTime").getValue()).toString();
                    SDescription = Objects.requireNonNull(snapshot.child("Description").getValue()).toString();
                    StoreName.setText(SStoreName);
                    StoreTiming.setText(SStoreTiming);
                    StoreDeliveryTime.setText(SStoreDeliveryTime);
                    SStoreRating= Objects.requireNonNull(snapshot.child("StoreRating").getValue()).toString();
                    SStoreRating=SStoreRating.replace("+","");
                    FStoreRating=Float.parseFloat(SStoreRating);
                    SelectStoreRating();
//
                    SRating= Objects.requireNonNull(snapshot.child("Rating").getValue()).toString();
                    SRating=SRating.replace("+","");
                    FRating=Float.parseFloat(SRating);
                    PName.setText(SName);
                    PDescription.setText(SDescription);
                    SelectRating();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            myRef.child(HomePID).child("RML").child("01").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SPrice = Objects.requireNonNull(snapshot.child("Price").getValue()).toString();
                    sMRP= Objects.requireNonNull(snapshot.child("MRP").getValue()).toString();
                    PPrice.setText(SPrice);
                    MRP.setText(sMRP);
                    Discount.setText(Objects.requireNonNull(snapshot.child("Discount").getValue()).toString());
                    String DS= Objects.requireNonNull(snapshot.child("DeliveryAMT").getValue()).toString();
                    if (DS!=null){
                        DS=DS.replace("Rs ","");
                        minimumDeliveryAMt=Integer.parseInt(DS);
                        if (minimumDeliveryAMt==0){
                            Delivery_Status.setVisibility(View.VISIBLE);
                        }
                        else {
                            Delivery_Status.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void SelectStoreRating() {
        if (FStoreRating<=0.5){
            SStar1.setImageResource(R.drawable.ic_baseline_star_half_24);
            SStar1.setVisibility(View.VISIBLE);
        }
        if (FStoreRating<=1.0 && FStoreRating>0.5){
            SStar1.setVisibility(View.VISIBLE);
            SStar1.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FStoreRating<=1.5 && FStoreRating>1.0){

            SStar1.setVisibility(View.VISIBLE);
            SStar1.setImageResource(R.drawable.ic_baseline_star_24);
            SStar2.setVisibility(View.VISIBLE);
            SStar2.setImageResource(R.drawable.ic_baseline_star_half_24);
        }
        if (FStoreRating<=2.0 && FStoreRating>1.5){
            SStar2.setVisibility(View.VISIBLE);
            SStar1.setVisibility(View.VISIBLE);
            SStar1.setImageResource(R.drawable.ic_baseline_star_24);
            SStar2.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FStoreRating<=2.5 && FStoreRating>2.0){

            SStar1.setVisibility(View.VISIBLE);
            SStar2.setVisibility(View.VISIBLE);
            SStar3.setVisibility(View.VISIBLE);
            SStar3.setImageResource(R.drawable.ic_baseline_star_half_24);
            SStar1.setImageResource(R.drawable.ic_baseline_star_24);
            SStar2.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FStoreRating<=3.0 && FStoreRating>2.5){

            SStar1.setVisibility(View.VISIBLE);
            SStar2.setVisibility(View.VISIBLE);
            SStar3.setVisibility(View.VISIBLE);
            SStar1.setImageResource(R.drawable.ic_baseline_star_24);
            SStar2.setImageResource(R.drawable.ic_baseline_star_24);
            SStar3.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FStoreRating<=3.5 && FStoreRating>3.0){

            SStar1.setVisibility(View.VISIBLE);
            SStar2.setVisibility(View.VISIBLE);
            SStar3.setVisibility(View.VISIBLE);
            SStar4.setVisibility(View.VISIBLE);
            SStar4.setImageResource(R.drawable.ic_baseline_star_half_24);
            SStar1.setImageResource(R.drawable.ic_baseline_star_24);
            SStar2.setImageResource(R.drawable.ic_baseline_star_24);
            SStar3.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FStoreRating<=4.0 && FStoreRating>3.5){

            SStar1.setVisibility(View.VISIBLE);
            SStar2.setVisibility(View.VISIBLE);
            SStar3.setVisibility(View.VISIBLE);
            SStar4.setVisibility(View.VISIBLE);
            SStar1.setImageResource(R.drawable.ic_baseline_star_24);
            SStar2.setImageResource(R.drawable.ic_baseline_star_24);
            SStar3.setImageResource(R.drawable.ic_baseline_star_24);
            SStar4.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FStoreRating<=4.5 && FStoreRating>4.0){

            SStar1.setVisibility(View.VISIBLE);
            SStar2.setVisibility(View.VISIBLE);
            SStar3.setVisibility(View.VISIBLE);
            SStar4.setVisibility(View.VISIBLE);
            SStar5.setVisibility(View.VISIBLE);
            SStar5.setImageResource(R.drawable.ic_baseline_star_half_24);
            SStar1.setImageResource(R.drawable.ic_baseline_star_24);
            SStar2.setImageResource(R.drawable.ic_baseline_star_24);
            SStar3.setImageResource(R.drawable.ic_baseline_star_24);
            SStar4.setImageResource(R.drawable.ic_baseline_star_24);
        }
    }

    private void SelectRating() {
        if (FRating<=0.5){
            Star1.setImageResource(R.drawable.ic_baseline_star_half_24);
            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.INVISIBLE);
            Star3.setVisibility(View.INVISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
        }
        if (FRating<=1.0 && FRating>0.5){
            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.INVISIBLE);
            Star3.setVisibility(View.INVISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FRating<=1.5 && FRating>1.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star2.setImageResource(R.drawable.ic_baseline_star_half_24);

            Star3.setVisibility(View.INVISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);

        }
        if (FRating<=2.0 && FRating>1.5){
            Star2.setVisibility(View.VISIBLE);
            Star1.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.INVISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FRating<=2.5 && FRating>2.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star3.setImageResource(R.drawable.ic_baseline_star_half_24);

            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);

        }
        if (FRating<=3.0 && FRating>2.5){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
            Star3.setImageResource(R.drawable.ic_baseline_star_24);

        }
        if (FRating<=3.5 && FRating>3.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.VISIBLE);
            Star4.setImageResource(R.drawable.ic_baseline_star_half_24);

            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
            Star3.setImageResource(R.drawable.ic_baseline_star_24);

        }
        if (FRating<=4.0 && FRating>3.5){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.VISIBLE);

            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
            Star3.setImageResource(R.drawable.ic_baseline_star_24);
            Star4.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FRating<=4.5 && FRating>4.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.VISIBLE);
            Star5.setVisibility(View.VISIBLE);
            Star5.setImageResource(R.drawable.ic_baseline_star_half_24);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
            Star3.setImageResource(R.drawable.ic_baseline_star_24);
            Star4.setImageResource(R.drawable.ic_baseline_star_24);
        }

    }

    private void fillRecommendedRV() {
        if (HomePID!=null) {
            RecommendedRV.setNestedScrollingEnabled(false);
            FirebaseRecyclerOptions<AllProductModel> options =
                    new FirebaseRecyclerOptions.Builder<AllProductModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("AllProducts"), AllProductModel.class)
                            .build();
            RecommendedAdapter = new AllProductAdapter(options);
            RecommendedRV.setLayoutManager(new GridLayoutManager(this, 2));
            RecommendedRV.setAdapter(RecommendedAdapter);
            RecommendedAdapter.startListening();
            RecommendedAdapter.setOnItemCLickListener(new AllProductAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DataSnapshot dataSnapshot, int position) {
                    Toast.makeText(IndividualProduct_2.this, "Changing Details", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fillAddOnRV() {
        if (HomePID!=null) {
            AddOnRV.setNestedScrollingEnabled(false);
            FirebaseRecyclerOptions<AddOnModel> options =
                    new FirebaseRecyclerOptions.Builder<AddOnModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("AllProducts").child(HomePID).child("AddOn"), AddOnModel.class)
                            .build();
            AddOnAdapter = new AddOnRecyclerViewAdapter(options);
            AddOnRV.setLayoutManager(new LinearLayoutManager(this));
            AddOnRV.setAdapter(AddOnAdapter);
            AddOnAdapter.startListening();
//        AddOnAdapter.setOnItemCLickListener(new AllProductAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DataSnapshot dataSnapshot, int position) {
//                Toast.makeText(IndividualProduct_2.this, "Changing Details", Toast.LENGTH_SHORT).show();
////
//            }
//        });
        }
    }

    private void fillRMLRecyclerView() {
        if (HomePID!=null) {
            FirebaseRecyclerOptions<RegularModel> options =
                    new FirebaseRecyclerOptions.Builder<RegularModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("AllProducts").child(HomePID).child("RML"), RegularModel.class)
                            .build();
            AdapterRML = new RMLAdapter(options);
            RML_RV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            RML_RV.setAdapter(AdapterRML);
            AdapterRML.startListening();
            AdapterRML.setOnItemCLickListener(new RMLAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DataSnapshot dataSnapshot, int position) {
//                    RMLKey = dataSnapshot.getKey();
//                    Size= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
//                    fillDetails();
                }
            });
//            checkRMLRecyclerView();
        }
    }
    private void fillSlider() {
        if ((HomePID!=null)) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference Trending_topic_1_reference = database.getReference().child("AllProducts").child(HomePID).child("Image");
            Trending_topic_1_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


//                    SliderDataList.add(Objects.requireNonNull(dataSnapshot.child("HomePID").getValue()).toString());
                        sliderImages.add(new SlideModel(Objects.requireNonNull(dataSnapshot.child("Image").getValue()).toString(), ScaleTypes.FIT));
                    }
                    ProductImages.setImageList(sliderImages, ScaleTypes.FIT);
//                ProductImages.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onItemSelected(int i) {
////
//                    }
//                });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.AddToWhilist_IndividualProductFloatingActionButton:{
                Toast.makeText(this, "WishList", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.Share_IndividualProduct_FloatingActionButton:{
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.BackButton_IndividualProduct_ImageView:{
                finish();
                break;
            }
            case R.id.Cart_IndividualProduct_ImageView:{
                Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.YourOrders_IndividualProduct_ImageView:{
                Toast.makeText(this, "Orders", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}