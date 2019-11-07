package com.example.jepapp.Fragments.User;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.FoodListAdapter;
import com.example.jepapp.Models.FoodItem;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LunchList extends AppCompatActivity {

    //a list to store all the products
    List<FoodItem> lunchItemList;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FoodListAdapter adapter;
    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfastmenurecycleer);
        getSupportActionBar().setTitle("Lunch Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lunchItemList = new ArrayList<>();
        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.breakfastrecyclerView);
       recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodListAdapter(getApplicationContext(), lunchItemList);
        //initializing the productlist

        recyclerView.setAdapter(adapter);


        //adding some items to our list
        progressDialog = new ProgressDialog(LunchList.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem lunchDetails = dataSnapshot.getValue(FoodItem.class);

                    lunchItemList.add(lunchDetails);
                    // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
                }

//                adapter = new RecyclerViewAdaptertest(SelectMenuItems.this, list);
//
//                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

    }
}
