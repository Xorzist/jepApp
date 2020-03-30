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


public class BreakfastList extends AppCompatActivity {

    //a list to store all the products
    List<FoodItem> foodItemList;
    FoodListAdapter adapter;

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;
    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfastmenurecycleer);
        getSupportActionBar().setTitle("Breakfast Menu");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        foodItemList = new ArrayList<>();
        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.breakfastrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodListAdapter(getApplicationContext(), foodItemList);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(BreakfastList.this);


        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
        Runreference();


    }

    private void Runreference() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Getting My Orders");
        progressDialog1.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem breakfastDetails = dataSnapshot.getValue(FoodItem.class);

                    foodItemList.add(breakfastDetails);
                    // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
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
