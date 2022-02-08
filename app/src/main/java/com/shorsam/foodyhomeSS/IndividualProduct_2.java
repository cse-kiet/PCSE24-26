package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IndividualProduct_2 extends AppCompatActivity implements View.OnClickListener{
    ImageSlider ProductImages;
    List<SlideModel> sliderImages=new ArrayList<SlideModel>();
    FloatingActionButton WishList,Share;
    ImageView Back,Cart,Orders;
    RecyclerView RML_RV,AddOnRV,RecommendedRV;
    RMLAdapter AdapterRML;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product_2);
        ProductImages=findViewById(R.id.SliderView_IndividualProduct2);
        WishList=findViewById(R.id.AddToWhilist_IndividualProductFloatingActionButton);
        Share=findViewById(R.id.Share_IndividualProduct_FloatingActionButton);
        Back=findViewById(R.id.BackButton_IndividualProduct_ImageView);
        Cart=findViewById(R.id.Cart_IndividualProduct_ImageView);
        Orders=findViewById(R.id.YourOrders_IndividualProduct_ImageView);
        RML_RV=findViewById(R.id.RML_RecyclerView_IndividualProduct2_ImageView);
        WishList.setOnClickListener(this);
        Share.setOnClickListener(this);
        Back.setOnClickListener(this);
        Cart.setOnClickListener(this);
        Orders.setOnClickListener(this);


        fillSlider();
        fillRMLRecyclerView();
    }
    private void fillRMLRecyclerView() {
//        if (HomePID!=null) {
            FirebaseRecyclerOptions<RegularModel> options =
                    new FirebaseRecyclerOptions.Builder<RegularModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").child("035").child("RML"), RegularModel.class)
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
//        }
    }
    private void fillSlider() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Trending_topic_1_reference = database.getReference().child("SliderHome");
        Trending_topic_1_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

//                    SliderDataList.add(Objects.requireNonNull(dataSnapshot.child("HomePID").getValue()).toString());
                    sliderImages.add(new SlideModel(Objects.requireNonNull(dataSnapshot.child("Image").getValue()).toString(), ScaleTypes.FIT));
                }
                ProductImages.setImageList(sliderImages,ScaleTypes.FIT);
                ProductImages.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
//
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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