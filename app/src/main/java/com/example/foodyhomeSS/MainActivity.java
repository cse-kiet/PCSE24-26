package com.example.foodyhomeSS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    FirebaseAuth Auth;
    LinearLayout layout;
    String SeeAll;
    FirebaseFirestore Store;
    RecyclerView MostPopular,PizzaTreatRV,BurgerTreatRV,ComboForFamilyRV,BeveragesRV,DifferentWorldRV;
    AllProductAdapter MostPopularAdapter;
    IndividualCategoryAdapter PizzaTreatAdapter,BurgerTreatAdapter,ComboForFamilyAdapter,BeveragesAdapter,DifferentWorldAdapter;
    //See All TextView of all Topics
    TextView FoodySeeAll,PizzaSeeAll,BurgerSeeAll,FamilyComboSeeAll,MostPopularSeeAll,BeveragesSeeAll,DifferentWorldSeeAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // RecyclerViews Definitions

        MostPopular=findViewById(R.id.Most_Popular_Treat_RecyclerView_Home);
        PizzaTreatRV=findViewById(R.id.Pizza_Treat_RecyclerView_Home);
        BurgerTreatRV=findViewById(R.id.Burger_Treat_RecyclerView_Home);
        ComboForFamilyRV=findViewById(R.id.Family_Treat_RecyclerView_Home);
        BeveragesRV=findViewById(R.id.Beverages_and_IceCreams_RecyclerView_Home);
        DifferentWorldRV=findViewById(R.id.Different_World_Treat_RecyclerView_Home);

        // RecyclerViews Definitions

        //Filling RecyclerViews

        fillMostPopular();
        fillPizzaTreat();
        fillBurgerTreat();
        fillComboForFamily();
        fillBeverages();
        fillDifferentWorld();

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
                    case R.id.Drawer_Call_us: {
                        Toast.makeText(MainActivity.this, "Call Us", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.Drawer_Chat_with_us: {
                        Toast.makeText(MainActivity.this, "Chat with Us", Toast.LENGTH_SHORT).show();
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Category1").startAt("Pizza Treat").endAt("Pizza Treat"+"\uf8ff"), IndividualCategoryModel.class)
                        .build();
        DifferentWorldAdapter= new IndividualCategoryAdapter(options);
        DifferentWorldRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true));
        DifferentWorldRV.setAdapter(DifferentWorldAdapter);
        DifferentWorldAdapter.startListening();
    }

    private void fillBeverages() {
        FirebaseRecyclerOptions<IndividualCategoryModel> options =
                new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Category1").startAt("Pizza Treat").endAt("Pizza Treat"+"\uf8ff"), IndividualCategoryModel.class)
                        .build();
        BeveragesAdapter = new IndividualCategoryAdapter(options);
        BeveragesRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true));
        BeveragesRV.setAdapter(BeveragesAdapter);
        BeveragesAdapter.startListening();
    }

    private void fillComboForFamily() {
        FirebaseRecyclerOptions<IndividualCategoryModel> options =
                new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Category1").startAt("Pizza Treat").endAt("Pizza Treat"+"\uf8ff"), IndividualCategoryModel.class)
                        .build();
        ComboForFamilyAdapter = new IndividualCategoryAdapter(options);
        ComboForFamilyRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true));
        ComboForFamilyRV.setAdapter(ComboForFamilyAdapter);
        ComboForFamilyAdapter.startListening();
    }

    private void fillBurgerTreat() {
        FirebaseRecyclerOptions<IndividualCategoryModel> options =
                new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Category1").startAt("Pizza Treat").endAt("Pizza Treat"+"\uf8ff"), IndividualCategoryModel.class)
                        .build();
        BurgerTreatAdapter = new IndividualCategoryAdapter(options);
        BurgerTreatRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true));
        BurgerTreatRV.setAdapter(BurgerTreatAdapter);
        BurgerTreatAdapter.startListening();
    }

    private void fillPizzaTreat() {
        FirebaseRecyclerOptions<IndividualCategoryModel> options =
                new FirebaseRecyclerOptions.Builder<IndividualCategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Category1").startAt("Pizza Treat").endAt("Pizza Treat"+"\uf8ff"), IndividualCategoryModel.class)
                        .build();
        PizzaTreatAdapter = new IndividualCategoryAdapter(options);
        PizzaTreatRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true));
        PizzaTreatRV.setAdapter(PizzaTreatAdapter);
        PizzaTreatAdapter.startListening();
    }

    private void fillMostPopular() {
        FirebaseRecyclerOptions<AllProductModel> options =
                new FirebaseRecyclerOptions.Builder<AllProductModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Category1").startAt("Most Popular").endAt("Most Popular"+"\uf8ff"), AllProductModel.class)
                        .build();
        MostPopularAdapter = new AllProductAdapter(options);
        MostPopular.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true));
        MostPopular.setAdapter(MostPopularAdapter);
        MostPopularAdapter.startListening();
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
                SeeAll="Foody_Special";
                SendSeeAllIntent();
                Toast.makeText(this, "Foody Special Treat", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.Most_Popular_Treat_See_All:{
                SeeAll="Most_Popular";
                SendSeeAllIntent();
                Toast.makeText(this, "Most Popular Treat", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.Pizza_Treat_See_All:{
                SeeAll="Pizza_Treat";
                SendSeeAllIntent();
                Toast.makeText(this, "Pizza Treat", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.Burger_Treat_See_All:{
                SeeAll="Burger_Treat";
                SendSeeAllIntent();
                Toast.makeText(this, "Burger Treat", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.Combo_for_Family_See_All:{
                SeeAll="Combo_For_Family";
                SendSeeAllIntent();
                Toast.makeText(this, "Family Combo Treat", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.Different_World_See_All:{
                SeeAll="Different_World_Treat";
                SendSeeAllIntent();
                Toast.makeText(this, "Different World Treat", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.Beverages_and_IceCreams_See_All:{
                SeeAll="Beverages_and_IceCreams";
                SendSeeAllIntent();
                Toast.makeText(this, "Beverages Treat", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void SendSeeAllIntent() {
        Map<String, Object> user = new HashMap<>();
        user.put("SeeAll", SeeAll);
        Store.collection("users").document("user")
                .update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "DataBase is Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}