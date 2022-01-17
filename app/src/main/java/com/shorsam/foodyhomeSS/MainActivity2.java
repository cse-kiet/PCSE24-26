package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    ImageView TopBG,TopToggle;
    CardView SearchCardView;
    RecyclerView recyclerView,PickYourFavouriteRV;
    CategoryTopAdapter adapter;
    ScrollView MainScrollView;
    List<SlideModel> sliderImages=new ArrayList<SlideModel>();
    PickYourFavouriteAdapter adapter2;
    ImageSlider CategorySlider;
    ArrayList<String> SliderDataList=new ArrayList<String>();
    int Up=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TopBG=findViewById(R.id.Top_ImageView_MainActivity2);
        TopToggle=findViewById(R.id.Menu_Button_MainActivity2);
        SearchCardView=findViewById(R.id.Search_CardView_MainActivity2);
        recyclerView=findViewById(R.id.Category_RecyclerView_MainActivity2);
        MainScrollView=findViewById(R.id.MainScrollView_MainActivity2);
        CategorySlider=findViewById(R.id.SliderView_Category_MainActivity2);
        PickYourFavouriteRV=findViewById(R.id.PickYourFavourite_RecyclerView_MainActivity2);
        fillDifferentWorld();
        SetTopContentAnimation();
        fillSlider();
        fillPickYourFavouriteRV();
        fillPickYourCategoryRV();
        fillPickYourShopRV();
    }

    private void fillPickYourShopRV() {
    }

    private void fillPickYourCategoryRV() {
    }

    private void fillPickYourFavouriteRV() {
        FirebaseRecyclerOptions<CategoryModelTop> options =
                new FirebaseRecyclerOptions.Builder<CategoryModelTop>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("DifferentTreat"), CategoryModelTop.class)
                        .build();
        adapter2= new PickYourFavouriteAdapter(options);
        PickYourFavouriteRV.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL,false));
        PickYourFavouriteRV.setAdapter(adapter2);
        adapter2.startListening();
    }

    private void fillSlider() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Trending_topic_1_reference = database.getReference().child("SliderHome");
        Trending_topic_1_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    SliderDataList.add(Objects.requireNonNull(dataSnapshot.child("HomePID").getValue()).toString());
                    sliderImages.add(new SlideModel(Objects.requireNonNull(dataSnapshot.child("Image").getValue()).toString(), ScaleTypes.FIT));
                }
                CategorySlider.setImageList(sliderImages,ScaleTypes.FIT);
                CategorySlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
//                        ProductID= SliderDataList.get(i);
//                        if (ProductID!=null){
//                            SendProductID();
//                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void SetTopContentAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MainScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (oldScrollY-scrollY<-15&& oldScrollY-scrollY<0 && Up==0){
                        Animation animation= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.tranlate_top_image_view);
                        TopBG.setAnimation(animation);

                        Animation animation2= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.translate_up_top_search);
                        SearchCardView.setAnimation(animation2);
                        SearchCardView.setTranslationX(50);
                        SearchCardView.setY(60);
                        Animation animation3= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.translate_up_top_recycler_view);

                        recyclerView.setAnimation(animation3);
                        recyclerView.setY(220);

                        Up=1;
                    }
                    else if (scrollY<=50 && Up==1){
                        Animation animation= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.tranlate_down_top_image_view);

                        TopBG.setAnimation(animation);
                        Animation animation2= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.translate_down_top_search);
//                        int[] locationOnScreentB = new int[2];
//                        SearchCardView.getLocationOnScreen(locationOnScreentB);
//                        Toast.makeText(MainActivity2.this, ""+locationOnScreentB[1] +" , "+locationOnScreentB[0], Toast.LENGTH_SHORT).show();
                        SearchCardView.setAnimation(animation2);
                        SearchCardView.setTranslationX(0);
                        SearchCardView.setTranslationY(0);

                        Animation animation3= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.tranlate_down_top_recycler_view);

                        recyclerView.setAnimation(animation3);
                        recyclerView.setTranslationY(0);



                        Up=0;
                    }
                }
            });
        }
    }

    private void fillDifferentWorld() {
        FirebaseRecyclerOptions<CategoryModelTop> options =
                new FirebaseRecyclerOptions.Builder<CategoryModelTop>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("DifferentTreat"), CategoryModelTop.class)
                        .build();
        adapter= new CategoryTopAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
//        adapter.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DataSnapshot dataSnapshot, int position) {
//
//            }
//        });
    }
}