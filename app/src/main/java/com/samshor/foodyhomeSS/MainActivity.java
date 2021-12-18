package com.samshor.foodyhomeSS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    LoadingDialog progressBar;
    FirebaseAuth Auth;
    LinearLayout layout;
    String SeeAll,ProductID,TopCategory;
    FirebaseFirestore Store;
    ArrayList<String> PList=new ArrayList<>();
    ArrayList<String> SliderDataList=new ArrayList<String>();
    RecyclerView MostPopular,PizzaTreatRV,BurgerTreatRV,ComboForFamilyRV,BeveragesRV,DifferentWorldRV;
    AllProductAdapter MostPopularAdapter;
    IndividualCategoryAdapter PizzaTreatAdapter,BurgerTreatAdapter,ComboForFamilyAdapter,BeveragesAdapter,DifferentWorldAdapter;
    //See All TextView of all Topics
    TextView PizzaSeeAll,BurgerSeeAll,FamilyComboSeeAll,MostPopularSeeAll,BeveragesSeeAll,DifferentWorldSeeAll,FoodySpecialSeeAll;
    private NavigationBarView bottomNavigationView;
    ImageButton PizzaIB,BurgerIB,PastaIB,IceCreamIB,FoodyOffersIB;
    List<SlideModel> sliderImages=new ArrayList<SlideModel>();
    ImageSlider top_sliderView;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    LocationRequest locationRequestG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PList.clear();


        //Asking user to turn on location if it is not
        // RecyclerViews Definitions


        MostPopular=findViewById(R.id.Most_Popular_Treat_RecyclerView_Home);
        PizzaTreatRV=findViewById(R.id.Pizza_Treat_RecyclerView_Home);
        BurgerTreatRV=findViewById(R.id.Burger_Treat_RecyclerView_Home);
        ComboForFamilyRV=findViewById(R.id.Family_Treat_RecyclerView_Home);
        BeveragesRV=findViewById(R.id.Beverages_and_IceCreams_RecyclerView_Home);
        DifferentWorldRV=findViewById(R.id.Different_World_Treat_RecyclerView_Home);


        // RecyclerViews Definitions

        //Top Category Image Button Definitions
        PizzaIB=findViewById(R.id.PizzaTopCategoryImageButton);
        PastaIB=findViewById(R.id.PastaTopCategoryImageButton);
        BurgerIB=findViewById(R.id.BurgersTopCategoryImageButton);
        IceCreamIB=findViewById(R.id.BeveragesTopCategoryImageButton);
        FoodyOffersIB=findViewById(R.id.FoodyOfferTopCategoryImageButton);
        top_sliderView = findViewById(R.id.Top_Slider_View);
        //Top Category Image Button Definitions

        progressBar=new LoadingDialog(this);
        progressBar.startLoadingDialog();
        //Filling RecyclerViews

        fillMostPopular();
        fillPizzaTreat();
        fillBurgerTreat();
        fillComboForFamily();
        fillBeverages();
        fillDifferentWorld();
        fillSlider();
        new CountDownTimer(10000,10){

            @Override
            public void onTick(long millisUntilFinished) {
                if (DifferentWorldRV.getChildCount()!=0){
                    progressBar.dismissDialog();
                    cancel();
                }

            }

            @Override
            public void onFinish() {
                progressBar.dismissDialog();
                Toast.makeText(MainActivity.this, "No Internet, check your Connection and Restart FoodyHome", Toast.LENGTH_SHORT).show();
            }
        }.start();

//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//
//        } else {
//
//
//            getCurrentLocation();
//
//
//        }

        //Filling RecyclerViews

        //Initialising FirebaseFireStore

        Store=FirebaseFirestore.getInstance();

        //Initialising FirebaseFireStore


        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        Auth=FirebaseAuth.getInstance();
        layout=findViewById(R.id.AllCategories);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.drawer_navigation_view);
        toggle=new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        toggle.getDrawerArrowDrawable().setSpinEnabled(true);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){


                    case R.id.Toolbar_Search:
                        Intent intent=new Intent(MainActivity.this,SearchUniversal.class);
                        startActivity(intent);
                        break;
                    case R.id.Toolbar_Notification_bell:
                        startActivity(new Intent(MainActivity.this,YourOrder_1.class));
                        break;
                    case R.id.Toolbar_share:

                        layout.setVisibility(View.VISIBLE);
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,"Hello ! we are foody home and we are providing you a platform which will deliver item to you within 1 hour");
                        sendIntent.setType("text/plain");
                        Intent.createChooser(sendIntent,"Share via");
                        startActivity(sendIntent);
                        break;
                }

                return true;
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Drawer_Home:{
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    }
                    case R.id.Drawer_Call_us: {
                        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
                        if (result == PackageManager.PERMISSION_GRANTED) {
                            Intent call=new Intent(Intent.ACTION_CALL);
                            call.setData(Uri.parse("tel:" +"9410264395"));
                            startActivity(call);
                            break;
                        }
                        else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CALL_PHONE },1);
                            break;
                        }


                    }
                    case R.id.Drawer_share_app: {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello ! we are foodyhome and we are providing you a platform which will deliver item to you within 1 hour");
                        sendIntent.setType("text/plain");
                        Intent.createChooser(sendIntent, "Share via");
                        startActivity(sendIntent);
                        break;
                    }
                    case R.id.Drawer_My_Menu:{
                        startActivity(new Intent(MainActivity.this,YourMenu.class));
                        break;
                    }


                    case R.id.Drawer_Chat_with_us: {

//                        Intent Query=new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+"8218655014"));
//                        startActivity(Query);
                        break;
                    }
                    case R.id.Drawer_logout: {
                        Auth.signOut();
                        Toast.makeText(MainActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, LogoLauncher.class);
                        startActivity(i);
                        finish();
                        break;
                    }
                    case R.id.Drawer_Terms_of_Use: {
                        Intent i = new Intent(MainActivity.this, Termandcondition.class);
                        startActivity(i);
                        break;
                    }
                    case  R.id.Drawer_My_WishList: {
                        startActivity(new Intent(MainActivity.this,YourOrder_1.class));
                        break;
                    }
                    case R.id.Drawer_Profile: {
                        startActivity(new Intent(MainActivity.this, profile.class));
                        break;
                    }
                    case R.id.Drawer_Refund_Policy:{
                        startActivity(new Intent(MainActivity.this,RefundPolicy.class));
                        break;
                    }
                    case R.id.Drawer_Privacy_Policy:{
                        startActivity(new Intent(MainActivity.this,PrivacyPolicy.class));
                        break;
                    }
                }
                return true;
            }
        });

        // TextView for See All of all Topics
        PizzaSeeAll=findViewById(R.id.Pizza_Treat_See_All);
        BurgerSeeAll=findViewById(R.id.Burger_Treat_See_All);
        FamilyComboSeeAll=findViewById(R.id.Combo_for_Family_See_All);
        BeveragesSeeAll=findViewById(R.id.Beverages_and_IceCreams_See_All);
        MostPopularSeeAll=findViewById(R.id.Most_Popular_Treat_See_All);
        DifferentWorldSeeAll=findViewById(R.id.Different_World_See_All);
        FoodySpecialSeeAll=findViewById(R.id.SeeAll_FoodySpecialTreat);

        //Calling Onclick Method for See All of all Topics
        PizzaSeeAll.setOnClickListener(this);
        BurgerSeeAll.setOnClickListener(this);
        FamilyComboSeeAll.setOnClickListener(this);
        BeveragesSeeAll.setOnClickListener(this);
        MostPopularSeeAll.setOnClickListener(this);
        DifferentWorldSeeAll.setOnClickListener(this);
        FoodySpecialSeeAll.setOnClickListener(this);
        PizzaIB.setOnClickListener(this);
        PastaIB.setOnClickListener(this);
        BurgerIB.setOnClickListener(this);
        IceCreamIB.setOnClickListener(this);
        FoodyOffersIB.setOnClickListener(this);

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
                top_sliderView.setImageList(sliderImages,ScaleTypes.FIT);
                top_sliderView.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                        progressBar.startLoadingDialog();
                        ProductID= SliderDataList.get(i);
                       if (ProductID!=null){
                           SendProductID();
                       }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fillDifferentWorld() {
        FirebaseRecyclerOptions<IndividualCategoryModel> options =
                new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("DifferentTreat"), IndividualCategoryModel.class)
                        .build();
        DifferentWorldAdapter= new IndividualCategoryAdapter(options);
        DifferentWorldRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        DifferentWorldRV.setAdapter(DifferentWorldAdapter);
        DifferentWorldAdapter.startListening();
        DifferentWorldAdapter.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                progressBar.startLoadingDialog();
                ProductID= Objects.requireNonNull(dataSnapshot.child("PID").getValue()).toString();
                SendProductID();
            }
        });
    }

    private void fillBeverages() {
        FirebaseRecyclerOptions<IndividualCategoryModel> options =
                new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Beverages"), IndividualCategoryModel.class)
                        .build();
        BeveragesAdapter = new IndividualCategoryAdapter(options);
        BeveragesRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        BeveragesRV.setAdapter(BeveragesAdapter);
        BeveragesAdapter.startListening();
        BeveragesAdapter.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                progressBar.startLoadingDialog();
                ProductID= Objects.requireNonNull(dataSnapshot.child("PID").getValue()).toString();
                SendProductID();
            }
        });
    }

    private void fillComboForFamily() {
        FirebaseRecyclerOptions<IndividualCategoryModel> options =
                new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("ComboForFamily"), IndividualCategoryModel.class)
                        .build();
        ComboForFamilyAdapter = new IndividualCategoryAdapter(options);
        ComboForFamilyRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        ComboForFamilyRV.setAdapter(ComboForFamilyAdapter);
        ComboForFamilyAdapter.startListening();
        ComboForFamilyAdapter.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                progressBar.startLoadingDialog();
                ProductID= Objects.requireNonNull(dataSnapshot.child("PID").getValue()).toString();
                SendProductID();
            }
        });
    }

    private void fillBurgerTreat() {
        FirebaseRecyclerOptions<IndividualCategoryModel> options =
                new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("BurgerTreat"), IndividualCategoryModel.class)
                        .build();
        BurgerTreatAdapter = new IndividualCategoryAdapter(options);
        BurgerTreatRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        BurgerTreatRV.setAdapter(BurgerTreatAdapter);
        BurgerTreatAdapter.startListening();
        BurgerTreatAdapter.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                progressBar.startLoadingDialog();
                ProductID= Objects.requireNonNull(dataSnapshot.child("PID").getValue()).toString();
                SendProductID();
            }
        });
    }

    private void SendProductID() {
        Map<String, Object> user = new HashMap<>();
        user.put("HomePID", ProductID);
        String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference documentReference=Store.collection("Users").document(UserId);
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                PList.add(ProductID);
                SaveSharedPreferences();
                progressBar.dismissDialog();
                startActivity(new Intent(MainActivity.this,Individual_Product.class));
            }
        });
    }

    private void fillPizzaTreat() {

        FirebaseRecyclerOptions<IndividualCategoryModel> options =
                new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PizzaTreat"), IndividualCategoryModel.class)
                        .build();
        PizzaTreatAdapter = new IndividualCategoryAdapter(options);
        PizzaTreatRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        PizzaTreatRV.setAdapter(PizzaTreatAdapter);
        PizzaTreatAdapter.startListening();
        PizzaTreatAdapter.setOnItemCLickListener(new IndividualCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                progressBar.startLoadingDialog();

                SeeAll= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString().toLowerCase();
                SendSeeAllIntent();

            }
        });
    }


    private void fillMostPopular() {

        FirebaseRecyclerOptions<AllProductModel> options =
                new FirebaseRecyclerOptions.Builder<AllProductModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products"), AllProductModel.class)
                        .build();
        MostPopularAdapter = new AllProductAdapter(options);
        MostPopular.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        MostPopular.setAdapter(MostPopularAdapter);
        MostPopularAdapter.startListening();
        MostPopularAdapter.setOnItemCLickListener(new AllProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                progressBar.startLoadingDialog();
                ProductID= Objects.requireNonNull(dataSnapshot.getKey());
                SendProductID();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_options,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.Most_Popular_Treat_See_All:{
                progressBar.startLoadingDialog();
                SeeAll="";
                SendSeeAllIntent();
                break;
            }
            case R.id.SeeAll_FoodySpecialTreat:{
                startActivity(new Intent(MainActivity.this,AllCombo.class));
                break;
            }
            case R.id.Pizza_Treat_See_All:{
                startActivity(new Intent(MainActivity.this,SeeAllCategories.class));
                break;
            }
            case R.id.Burger_Treat_See_All:{
                progressBar.startLoadingDialog();
                SeeAll="burger treat";
                SendSeeAllIntent();
                break;
            }
            case R.id.Combo_for_Family_See_All:{
                progressBar.startLoadingDialog();
                SeeAll="combo for family";
                SendSeeAllIntent();
                break;
            }
            case R.id.Different_World_See_All:{
                progressBar.startLoadingDialog();
                SeeAll="different treat";
                SendSeeAllIntent();
                break;
            }
            case R.id.Beverages_and_IceCreams_See_All:{
                progressBar.startLoadingDialog();
                SeeAll="beverages";
                SendSeeAllIntent();
                break;
            }
            case R.id.BeveragesTopCategoryImageButton: {
                progressBar.startLoadingDialog();
                TopCategory="beverages";
                sendTopCategory();
                break;
            }
            case R.id.BurgersTopCategoryImageButton: {
                progressBar.startLoadingDialog();
                TopCategory="burger";
                sendTopCategory();
                break;
            }
            case R.id.FoodyOfferTopCategoryImageButton: {
                progressBar.startLoadingDialog();
                startActivity(new Intent(MainActivity.this,AllCombo.class));
                break;
            }
            case R.id.PastaTopCategoryImageButton: {
                progressBar.startLoadingDialog();
                TopCategory="pasta";
                sendTopCategory();
                break;
            }
            case R.id.PizzaTopCategoryImageButton: {
                progressBar.startLoadingDialog();
                TopCategory="pizza";
                sendTopCategory();
                break;
            }
        }
    }

    private void sendTopCategory() {
        SaveSharedPreferences();
        progressBar.dismissDialog();
        startActivity(new Intent(MainActivity.this,TopCategories.class));
    }

    private void SendSeeAllIntent() {
        Map<String, Object> user = new HashMap<>();
        user.put("SeeAll", SeeAll);
        String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference documentReference=Store.collection("Users").document(UserId);
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                progressBar.dismissDialog();
                startActivity(new Intent(MainActivity.this,AllProduct.class));
            }
        });
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(PList);
        editor.putString("TopCategory",TopCategory);
        editor.putString("PList",json);
        editor.apply();

    }
//    private void getCurrentLocation() {
//
//
//
////        LocationRequest locationrequest = new LocationRequest();
////        locationrequest.setInterval(10000);
////        locationrequest.setFastestInterval(2000);
////        locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
////        locationRequestG=LocationRequest.create();
////        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder()
////                .addLocationRequest(locationRequestG);
////        builder.setAlwaysShow(true);
////        Task<LocationSettingsResponse> result=LocationServices.getSettingsClient(getApplicationContext())
////                .checkLocationSettings(builder.build());
////        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
////            @Override
////            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
////                try {
////                    LocationSettingsResponse response=task.getResult(ApiException.class);
////                } catch (ApiException e) {
////                    switch (e.getStatusCode()){
////                        case LocationSettingsStatusCodes
////                                .RESOLUTION_REQUIRED:
////                            try {
////                                ResolvableApiException resolvableApiException=(ResolvableApiException)e;
////                                resolvableApiException.startResolutionForResult(MainActivity.this,REQUEST_LOCATION_PERMISSION);
////                            } catch (IntentSender.SendIntentException sendIntentException) {
////                                sendIntentException.printStackTrace();
////                            }
////                            break;
////                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
////                            break;
////                    }
////                }
////            }
////        });
////
////
//
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////
////            return;
////        }
////        LocationServices.getFusedLocationProviderClient(MainActivity.this)
////                .requestLocationUpdates(locationrequest, new LocationCallback() {
////                    @SuppressLint("SetTextI18n")
////                    @Override
////                    public void onLocationResult(LocationResult locationResult) {
////                        super.onLocationResult(locationResult);
////                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
////                                .removeLocationUpdates(this);
////                        if (locationResult != null && locationResult.getLocations().size() > 0) {
////                            int LatestLocationIndex = locationResult.getLocations().size() - 1;
////                            double latitude = locationResult.getLocations().get(LatestLocationIndex).getLatitude();
////                            double longitude = locationResult.getLocations().get(LatestLocationIndex).getLongitude();
//////
//////                            Toast.makeText(MainActivity.this,  String.format(
//////                                    "Latitude: %s\nLongitude: %s",
//////                                    latitude,
//////                                    longitude
//////                            ), Toast.LENGTH_SHORT).show();
////                            latitude*=100;
////                            longitude*=100;
////                            if ((latitude >= 2925) && (latitude <= 2930) && (longitude >= 7743) && (longitude <= 7750)){
////                                AvailTV.setText("Congratulations, We are working at full potential in your Area");
////                                new CountDownTimer(5000,1000){
////
////                                    @Override
////                                    public void onTick(long millisUntilFinished) {
////
////                                    }
////
////                                    @Override
////                                    public void onFinish() {
////                                        AvailTV.setVisibility(View.GONE);
////                                    }
////                                }.start();
////
////                            }
////                            else {
////                                AvailTV.setText("Sorry!! But our Services are not Available at your current location");
////                                AvailTV.setVisibility(View.VISIBLE);
////                            }
////
////
////
////                        }
////
////                    }
////                }, Looper.getMainLooper());
//
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                getCurrentLocation();
//            } else {
//                Toast.makeText(this, "Permisssion Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==REQUEST_LOCATION_PERMISSION){
//            switch (resultCode){
//                case Activity
//                        .RESULT_OK:
//
//                break;
//                case Activity.RESULT_CANCELED:
//
//            }
//        }
//    }
}