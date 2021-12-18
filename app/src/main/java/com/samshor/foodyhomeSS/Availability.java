package com.samshor.foodyhomeSS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Availability extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        listView = findViewById(R.id.listView_availability);
        arrayList = new ArrayList<>();
        arrayList.add("Budhana");
        arrayList.add("Bhasaana");
        arrayList.add("chandheri");
        arrayList.add("Maples Academy");
        arrayList.add("Bitawada");
        arrayList.add("Alipur Aterana");
        arrayList.add("Lushana");
        arrayList.add("Sabipur");
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

    }
}