package com.example.foodyhomeSS;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.drawer_navigation_view);
        toggle=new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Toolbar_Search:
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Toolbar_Notification_bell:
                        Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Toolbar_share:
                        Toast.makeText(MainActivity.this, "share", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.Drawer_Call_us: {
                        Toast.makeText(MainActivity.this, "Call Us", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.Drawer_Chat_with_us: {
                        Toast.makeText(MainActivity.this, "Chat with Us", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_options,menu);
        return true;
    }
}