package com.example.jepapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jepapp.Adapters.BreakfastListAdapter;
import com.example.jepapp.Models.BreakfastItem;

import java.util.ArrayList;
import java.util.List;


public class BreakfastList extends AppCompatActivity {

    //a list to store all the products
    List<BreakfastItem> breakfastItemList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfastmenurecycleer);
        getSupportActionBar().setTitle("Breakfast Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        breakfastItemList = new ArrayList<>();


        //adding some items to our list
        breakfastItemList.add(
                new BreakfastItem(
                        1,
                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000,
                        R.drawable.user_profile_image_background));

        breakfastItemList.add(
                new BreakfastItem(
                        1,
                        "Dell Inspiron 7000 Core i5 7th Gen - (8 GB/1 TB HDD/Windows 10 Home)",
                        "14 inch, Gray, 1.659 kg",
                        4.3,
                        60000,
                        R.drawable.user_profile_image_background));

        breakfastItemList.add(
                new BreakfastItem(
                        1,
                        "Microsoft Surface Pro 4 Core m3 6th Gen - (4 GB/128 GB SSD/Windows 10)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000,
                        R.drawable.user_profile_image_background));

        //creating recyclerview adapter
        BreakfastListAdapter adapter = new BreakfastListAdapter(this, breakfastItemList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
