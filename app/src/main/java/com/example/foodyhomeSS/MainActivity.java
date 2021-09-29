package com.example.foodyhomeSS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
    String SeeAll,ProductID;
    FirebaseFirestore Store;
    RecyclerView MostPopular,PizzaTreatRV,BurgerTreatRV,ComboForFamilyRV,BeveragesRV,DifferentWorldRV;
    AllProductAdapter MostPopularAdapter;
    IndividualCategoryAdapter PizzaTreatAdapter,BurgerTreatAdapter,ComboForFamilyAdapter,BeveragesAdapter,DifferentWorldAdapter;
    //See All TextView of all Topics
    TextView FoodySeeAll,PizzaSeeAll,BurgerSeeAll,FamilyComboSeeAll,MostPopularSeeAll,BeveragesSeeAll,DifferentWorldSeeAll;
    private NavigationBarView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String formattedDate = getDateTime();
        // RecyclerViews Definitions


        MostPopular=findViewById(R.id.Most_Popular_Treat_RecyclerView_Home);
        PizzaTreatRV=findViewById(R.id.Pizza_Treat_RecyclerView_Home);
        BurgerTreatRV=findViewById(R.id.Burger_Treat_RecyclerView_Home);
        ComboForFamilyRV=findViewById(R.id.Family_Treat_RecyclerView_Home);
        BeveragesRV=findViewById(R.id.Beverages_and_IceCreams_RecyclerView_Home);
        DifferentWorldRV=findViewById(R.id.Different_World_Treat_RecyclerView_Home);

        // RecyclerViews Definitions

        progressBar=new LoadingDialog(this);
        progressBar.startLoadingDialog();
        //Filling RecyclerViews

        fillMostPopular();
        fillPizzaTreat();
        fillBurgerTreat();
        fillComboForFamily();
        fillBeverages();
        fillDifferentWorld();
        new CountDownTimer(10000,10){

            @Override
            public void onTick(long millisUntilFinished) {
                if (DifferentWorldRV.getChildCount()!=0){
                    progressBar.dismissDialog();
                    Toast.makeText(MainActivity.this, formattedDate, Toast.LENGTH_LONG).show();
                    cancel();
                }

            }

            @Override
            public void onFinish() {
                progressBar.dismissDialog();
                Toast.makeText(MainActivity.this, "No Internet, check your Connection and Restart FoodyHome", Toast.LENGTH_SHORT).show();
            }
        }.start();


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
                        Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
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
                        Intent call=new Intent(Intent.ACTION_CALL);
                        call.setData(Uri.parse("tel:" +"9410264395"));
                        startActivity(call);
                        break;

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



                    case R.id.Drawer_Chat_with_us: {
                        Intent Query=new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+"8218655014"));
                        startActivity(Query);
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
                        finish();
                        break;
                    }


                }
                return true;
            }
        });

        // TextView for See All of all Topics
        FoodySeeAll=findViewById(R.id.Foody_Special_Treat_See_All);
        PizzaSeeAll=findViewById(R.id.Pizza_Treat_See_All);
        BurgerSeeAll=findViewById(R.id.Burger_Treat_See_All);
        FamilyComboSeeAll=findViewById(R.id.Combo_for_Family_See_All);
        BeveragesSeeAll=findViewById(R.id.Beverages_and_IceCreams_See_All);
        MostPopularSeeAll=findViewById(R.id.Most_Popular_Treat_See_All);
        DifferentWorldSeeAll=findViewById(R.id.Different_World_See_All);

        //Calling Onclick Method for See All of all Topics

        FoodySeeAll.setOnClickListener(this);
        PizzaSeeAll.setOnClickListener(this);
        BurgerSeeAll.setOnClickListener(this);
        FamilyComboSeeAll.setOnClickListener(this);
        BeveragesSeeAll.setOnClickListener(this);
        MostPopularSeeAll.setOnClickListener(this);
        DifferentWorldSeeAll.setOnClickListener(this);









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
            public void onSuccess(Void aVoid) {
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
            case R.id.Foody_Special_Treat_See_All:{
                break;
            }
            case R.id.Most_Popular_Treat_See_All:{
                progressBar.startLoadingDialog();
                SeeAll="most popular";
                SendSeeAllIntent();
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
        }
    }

    private void SendSeeAllIntent() {
        Map<String, Object> user = new HashMap<>();
        user.put("SeeAll", SeeAll);
        String UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference documentReference=Store.collection("Users").document(UserId);
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.dismissDialog();
                startActivity(new Intent(MainActivity.this,AllProduct.class));
            }
        });
    }
    private String getDateTime() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}