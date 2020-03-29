package com.example.jepapp.Activities.Users;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Users.FoodListAdapter;
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


        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");


        final ProgressDialog progressDialog1 = new ProgressDialog(getApplicationContext());
        progressDialog1.setMessage("Getting My Orders");
        progressDialog1.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem lunchDetails = dataSnapshot.getValue(FoodItem.class);

                    lunchItemList.add(lunchDetails);

                }

                adapter.notifyDataSetChanged();

                progressDialog1.cancel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog1.cancel();

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.justcart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.justcart:
                // Open cart page
                Intent intent = new Intent(this, Cart.class);
                startActivity(intent);
                break;

        }
        return false;
    }
}
