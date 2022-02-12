
package com.shorsam.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawerLayout;
    FirebaseAuth Auth;
    FirebaseFirestore store;
    NavigationView navigationView;
    ImageView TopBG,TopToggle;
    ImageButton TopShare,TopYourOrders;
    CardView SearchCardView;
    RecyclerView TopCategoryRV,PickYourFavouriteRV,ShopRecyclerView,PickYourCategoryRV;
    CategoryTopAdapter adapter;
    ShopsActivityAdapter adapter3;
    ImageButton All_Shops_See_All;
    ScrollView MainScrollView;
    List<SlideModel> sliderImages=new ArrayList<SlideModel>();
    List<SlideModel> FoodItemImages=new ArrayList<SlideModel>();
    List<SlideModel> CategoryImages=new ArrayList<SlideModel>();
    TextView Pick_Your_Favourite_See_All;
    PickYourFavouriteAdapter adapter2;
    ImageSlider CategorySlider ,FavouriteFoodSlider,FoodSlider;
    ArrayList<String> SliderDataList=new ArrayList<String>();
    ArrayList<String> FoodDataList=new ArrayList<String>();
    ArrayList<String> CategoryDataList=new ArrayList<String>();
    String category,HomePID;
    int Up=0,MenuState=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TopBG = findViewById(R.id.Top_ImageView_MainActivity2);
        TopToggle = findViewById(R.id.Menu_Button_MainActivity2);
        SearchCardView = findViewById(R.id.Search_CardView_MainActivity2);
        TopCategoryRV = findViewById(R.id.TopCategory_RecyclerView_MainActivity2);
        PickYourCategoryRV = findViewById(R.id.PickYourCategory_RecyclerView_MainActivity2);
        MainScrollView = findViewById(R.id.MainScrollView_MainActivity2);
        CategorySlider = findViewById(R.id.SliderView_IndividualProduct2);
        FavouriteFoodSlider=findViewById(R.id.SliderView_FavouriteFood_MainActivity2);
        FoodSlider=findViewById(R.id.SliderView_FoodItems_MainActivity2);
        PickYourFavouriteRV = findViewById(R.id.PickYourFavourite_RecyclerView_MainActivity2);
        drawerLayout = findViewById(R.id.drawer_layout_mainActivity_2);
        All_Shops_See_All=findViewById(R.id.All_Shops_SeeAll);
        Pick_Your_Favourite_See_All=findViewById(R.id.PicK_your_Favourite_See_All);
        navigationView = findViewById(R.id.drawer_navigation_view_mainactivity_2);
        ShopRecyclerView = findViewById(R.id.PickYourStore_RecyclerView_MainActivity2);

        TopYourOrders=findViewById(R.id.TopYourOrdersButton_ImageButton_MainActivity2);
        TopShare=findViewById(R.id.TopShareButton_ImageButton_MainActivity2);

        ShopRecyclerView.setNestedScrollingEnabled(false);
        Auth = FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();
        fillDifferentWorld();
        SetTopContentAnimation();
        // sliders
        fillCategorySlider();
        fillFavouriteFoodSlider();
        fillFoodItemsSlider();
        // Sliders

        //RecyclerViews
        fillPickYourFavouriteRV();
        fillPickYourCategoryRV();
        fillPickYourShopRV();

        //RecyclerViews
        All_Shops_See_All.setOnClickListener(this);
Pick_Your_Favourite_See_All.setOnClickListener(this);


        All_Shops_See_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               All_Shops_See_All.animate().scaleY(0.8f).scaleX(0.8f).setDuration(100).start();
               new CountDownTimer(100,100){

                   @Override
                   public void onTick(long millisUntilFinished) {

                   }

                   @Override
                   public void onFinish() {
                       All_Shops_See_All.animate().scaleY(1f).scaleX(1f).setDuration(100).start();
                   }
               }.start();
//
            }
        });

        TopToggle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @SuppressLint("NonConstantResourceId")


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Drawer_Home: {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    }
                   case R.id.Drawer_Call_us:{
                       HomePID="001";
                       SaveSharedPreferences();
                       startActivity(new Intent(MainActivity2.this,IndividualProduct_2.class));
                       break;
                   }
                    case R.id.Drawer_Chat_with_us: {
//                        Intent i = new Intent(MainActivity2.this, Shops_Activity.class);
//                        startActivity(i);
//                        break;
                    }//                       int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
//                        if (result == PackageManager.PERMISSION_GRANTED) {
//                            Intent call=new Intent(Intent.ACTION_CALL);
//                            call.setData(Uri.parse("tel:" +"9410264395"));
//                            startActivity(call);
//                            break;
//                        }
//                        else {
//                            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CALL_PHONE },1);
//                            break;


                    case R.id.Drawer_share_app: {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey Order Pizza and Burger Now from your bed: https://play.google.com/store/apps/details?id=com.shorsam.foodyhomeSS");
                        sendIntent.setType("text/plain");
                        Intent.createChooser(sendIntent, "Share via");
                        startActivity(sendIntent);
                        break;
                    }
                    case R.id.Drawer_My_Menu: {
                        startActivity(new Intent(MainActivity2.this, YourMenu.class));
                        break;
                    }
                    case R.id.Drawer_logout: {
                        Auth.signOut();
                        Toast.makeText(MainActivity2.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity2.this, LogoLauncher.class);
                        startActivity(i);
                        finish();
                        break;
                    }
                    case R.id.Drawer_Terms_of_Use: {
                        Intent i = new Intent(MainActivity2.this, Termandcondition.class);
                        startActivity(i);
                        break;
                    }
                    case R.id.Drawer_My_WishList: {
                        startActivity(new Intent(MainActivity2.this, YourOrder_1.class));
                        break;
                    }
                    case R.id.Drawer_Profile: {
                        startActivity(new Intent(MainActivity2.this, profile.class));
                        break;
                    }
                    case R.id.Drawer_Refund_Policy: {
                        startActivity(new Intent(MainActivity2.this, RefundPolicy.class));
                        break;
                    }
                    case R.id.Drawer_Privacy_Policy: {
                        startActivity(new Intent(MainActivity2.this, PrivacyPolicy.class));
                        break;
                    }
                }
                return true;
            }
        });
    }
    private void fillPickYourShopRV() {
        FirebaseRecyclerOptions<CategoryModelTop> options =
                new FirebaseRecyclerOptions.Builder<CategoryModelTop>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("DifferentTreat"), CategoryModelTop.class)
                        .build();
        adapter3= new ShopsActivityAdapter(options);
        ShopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ShopRecyclerView.setAdapter(adapter3);
        adapter3.startListening();
    }

    private void fillPickYourCategoryRV() {
        FirebaseRecyclerOptions<CategoryModelTop> options =

                new FirebaseRecyclerOptions.Builder<CategoryModelTop>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("DifferentTreat"), CategoryModelTop.class)
                        .build();
        adapter2= new PickYourFavouriteAdapter(options);
        PickYourCategoryRV.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL,false));
        PickYourCategoryRV.setAdapter(adapter2);
        adapter2.startListening();
    }

    private void fillPickYourFavouriteRV() {
        FirebaseRecyclerOptions<CategoryModelTop> options =
                new FirebaseRecyclerOptions.Builder<CategoryModelTop>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products"), CategoryModelTop.class)
                        .build();
        adapter2= new PickYourFavouriteAdapter(options);
        PickYourFavouriteRV.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL,false));
        PickYourFavouriteRV.setAdapter(adapter2);
        adapter2.startListening();
        adapter2.setOnItemCLickListener(new PickYourFavouriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                category = Objects.requireNonNull(dataSnapshot.child("Category").getValue()).toString();
                SaveSharedPreferences();
                startActivity(new Intent(MainActivity2.this,SubCategoryPickYourFavourite.class));
            }
        });

    }

    private void fillCategorySlider() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Trending_topic_1_reference = database.getReference().child("SliderHome");
        Trending_topic_1_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    CategoryDataList.add(Objects.requireNonNull(dataSnapshot.child("HomePID").getValue()).toString());
                    CategoryImages.add(new SlideModel(Objects.requireNonNull(dataSnapshot.child("Image").getValue()).toString(), ScaleTypes.FIT));
                }
                CategorySlider.setImageList(CategoryImages,ScaleTypes.FIT);
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


//            @SuppressLint("NonConstantResourceId")


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void fillFavouriteFoodSlider(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Trending_topic_1_reference = database.getReference().child("SliderHome");
        Trending_topic_1_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    SliderDataList.add(Objects.requireNonNull(dataSnapshot.child("HomePID").getValue()).toString());
                    sliderImages.add(new SlideModel(Objects.requireNonNull(dataSnapshot.child("Image").getValue()).toString(), ScaleTypes.FIT));
                }
                FavouriteFoodSlider.setImageList(sliderImages,ScaleTypes.FIT);
//                FavouriteFoodSlider.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onItemSelected(int i) {
////                        ProductID= SliderDataList.get(i);
////                        if (ProductID!=null){
////                            SendProductID();
////                        }
//
//                    }
//                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void fillFoodItemsSlider(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Trending_topic_1_reference = database.getReference().child("SliderHome");
        Trending_topic_1_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodDataList.add(Objects.requireNonNull(dataSnapshot.child("HomePID").getValue()).toString());
                    FoodItemImages.add(new SlideModel(Objects.requireNonNull(dataSnapshot.child("Image").getValue()).toString(), ScaleTypes.FIT));
                }
                FoodSlider.setImageList(FoodItemImages,ScaleTypes.FIT);
                FoodSlider.setItemClickListener(new ItemClickListener() {
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


    @SuppressLint("ObsoleteSdkInt")
    private void SetTopContentAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MainScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (oldScrollY-scrollY<-15&& oldScrollY-scrollY<0 && Up==0){
//                        Animation animation= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.tranlate_top_image_view);
//                        TopBG.setAnimation(animation);
//                        TranslateAnimation translateAnimation=new TranslateAnimation(0,0,0,0);
//                        translateAnimation.setDuration(300);
                        TopToggle.animate().translationY(-50).scaleY(0.9f).scaleX(0.9f).setDuration(200).start();
                        SearchCardView.animate().translationY(-200).setDuration(200).start();
                        TopCategoryRV.animate().translationY(-200).setDuration(200).start();
                        TopBG.animate().translationY(-200).setDuration(200).start();
                        MainScrollView.animate().translationY(-220).setDuration(200).start();
                        All_Shops_See_All.animate().translationY(-200).setDuration(200).start();
                        TopShare.animate().translationY(-50).scaleY(0.9f).scaleX(0.9f).setDuration(200).start();
                        TopYourOrders.animate().translationY(-50).scaleY(0.9f).scaleX(0.9f).setDuration(200).start();

//                        translateAnimation.setFillAfter(true);
//                        translateAnimation.setFillEnabled(true);
//                        TopBG.startAnimation(translateAnimation);

//                        Animation animation2= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.translate_up_top_search);
//                        SearchCardView.setAnimation(animation2);
//                        SearchCardView.setTranslationX(50);
//                        SearchCardView.setY(60);
//                        SearchCardView.startAnimation(translateAnimation);
//                        TopCategoryRV.startAnimation(translateAnimation);
//                        Animation animation3= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.translate_up_top_recycler_view);
//
//                       TopCategoryRV.setAnimation(animation3);


                        Up=1;
                    }
                    else if (scrollY<=15 && Up==1 || scrollY==0 &&Up==1){
//                        Animation animation= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.tranlate_down_top_image_view);
//
//                        TopBG.setAnimation(animation);
//                        Animation animation2= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.translate_down_top_search);
////                        int[] locationOnScreentB = new int[2];
////                        SearchCardView.getLocationOnScreen(locationOnScreentB);
////                        Toast.makeText(MainActivity2.this, ""+locationOnScreentB[1] +" , "+locationOnScreentB[0], Toast.LENGTH_SHORT).show();
//                        SearchCardView.setAnimation(animation2);
//                        SearchCardView.setTranslationX(0);
//                        SearchCardView.setTranslationY(0);
//
//                        Animation animation3= AnimationUtils.loadAnimation(MainActivity2.this,R.anim.tranlate_down_top_recycler_view);
//
//                        TopCategoryRV.setAnimation(animation3);
//                        TopCategoryRV.setTranslationY(0);
                        TopToggle.animate().translationY(0).scaleY(1f).scaleX(1f).setDuration(200).start();
                        TopShare.animate().translationY(0).scaleY(1f).scaleX(1f).setDuration(200).start();
                        TopYourOrders.animate().translationY(0).scaleY(1f).scaleX(1f).setDuration(200).start();
                        SearchCardView.animate().translationY(0).translationX(0).start();
                        TopCategoryRV.animate().translationY(0).start();
                        TopBG.animate().translationY(0).start();
                        MainScrollView.animate().translationY(0).setDuration(200).start();
                        All_Shops_See_All.animate().translationY(0).setDuration(200).start();
                        Up=0;
                    }
                }
            });
        }
    }

    private void fillDifferentWorld() {
        FirebaseRecyclerOptions<CategoryModelTop> options =
                new FirebaseRecyclerOptions.Builder<CategoryModelTop>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Categories"), CategoryModelTop.class)
                        .build();
        adapter= new CategoryTopAdapter(options);
        TopCategoryRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        TopCategoryRV.setAdapter(adapter);
        adapter.startListening();
        adapter.setOnItemCLickListener(new CategoryTopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {



            category = Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
                SaveSharedPreferences();
//                Toast.makeText(MainActivity2.this, "" +category, Toast.LENGTH_SHORT).show();
//            Map<String, Object> user = new HashMap<>();
//            user.put("category", category);
//            String UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//            DocumentReference documentReference = store.collection("Users").document(UserId);
//            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(MainActivity2.this, Shops_Activity.class));
//                }
//
//           });
       }
  });

}
    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
//        Gson gson=new Gson();
//        String json=gson.toJson(PList);
        editor.putString("category",category);
        editor.putString("HomePID",HomePID);
//        editor.putString("PList",json);
        editor.apply();

    }
    private void LoadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        String ABC=sharedPreferences.getString("category","");
//        Gson gson=new Gson();
//        String jsonString=sharedPreferences.getString("PList","");
//        String jsonString2=sharedPreferences.getString("AddOns","");
//        Type type=new TypeToken<ArrayList<String>>() {}.getType();
////        PList=gson.fromJson(jsonString,type);
////        AddOns=gson.fromJson(jsonString2,type);
////        if (AddOns==null){
////            AddOns=new ArrayList<>();
////        }
////        if (PList==null){
////            PList=new ArrayList<>();
////        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.All_Shops_SeeAll: {
                category = " ";
                SaveSharedPreferences();
                Intent i = new Intent(MainActivity2.this, Shops_Activity.class);
                startActivity(i);
                break;
            }
            case R.id.PicK_your_Favourite_See_All:{
                category=" ";
                SaveSharedPreferences();
                Intent i = new Intent(MainActivity2.this,SubCategoryPickYourFavourite.class);
                startActivity(i);
                break;
            }
        }
    }
}