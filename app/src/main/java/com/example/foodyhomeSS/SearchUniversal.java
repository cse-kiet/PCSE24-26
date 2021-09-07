package com.example.foodyhomeSS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SearchUniversal extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<String> SpecificTags=new ArrayList<>();
    ArrayList<String> CategoryTags=new ArrayList<>();
    String FilteredTag;
    RecyclerView recyclerView;
    AllProductAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_universal);
        toolbar=findViewById(R.id.Toolbar_Universal_Search);
        setSupportActionBar(toolbar);
        recyclerView=findViewById(R.id.RecyclerViewSearchUniversal);

        FirebaseDatabase.getInstance().getReference().child("Tags")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.child("Specific").getChildren()) {
                            String item= Objects.requireNonNull(snapshot.getValue()).toString();
                            if (item!=null) {
                                SpecificTags.add(item);
                            }
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("Category").getChildren()) {
                            String item= Objects.requireNonNull(snapshot.getValue()).toString();
                            if (item!=null) {
                                CategoryTags.add(item);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SearchUniversal.this, "Error!!!", Toast.LENGTH_SHORT).show();
                    }
                });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.universalsearchmenu,menu);
        MenuItem item=menu.findItem(R.id.UniversalSearchMenu);
        SearchView Searchview=(SearchView)item.getActionView();
        Searchview.setSubmitButtonEnabled(true);
        Searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                for (String element : CategoryTags){
                    if (element.contains(s.toLowerCase())) {
                        FilteredTag = element;
                        type="Category";
                        fillRecyclerView();
                        break;
                    }
                    else {
                        FilteredTag=null;
                    }
                }
                if (FilteredTag==null){
                    for (String element : SpecificTags){
                        if (element.contains(s.toLowerCase())) {
                            FilteredTag = element;
                            type="Tag";
                            fillRecyclerView();
                            break;
                        }
                        else {
                            FilteredTag=null;
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                for (String element : CategoryTags){
                    if (element.contains(s.toLowerCase())) {
                        FilteredTag = element;
                        type="Category";
                        fillRecyclerView();
                        break;
                    }
                    else {
                        FilteredTag=null;
                    }
                }
                if (FilteredTag==null){
                    for (String element : SpecificTags){
                        if (element.contains(s.toLowerCase())) {
                            FilteredTag = element;
                            type="Tag";
                            fillRecyclerView();
                            break;
                        }
                        else {
                            FilteredTag=null;
                        }
                    }
                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fillRecyclerView() {
        FirebaseRecyclerOptions<AllProductModel> options =
                new FirebaseRecyclerOptions.Builder<AllProductModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild(type).startAt(FilteredTag).endAt(FilteredTag+"\uf8ff"), AllProductModel.class)
                        .build();
        adapter = new AllProductAdapter(options);
        layoutManager = new GridLayoutManager(SearchUniversal.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}