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

    List<FoodItem> lunchItemList;
    DatabaseReference databaseReference;
    FoodListAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menusrecycler);
        getSupportActionBar().setTitle("Lunch Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lunchItemList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.menusrecyclerView);
       recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodListAdapter(LunchList.this, lunchItemList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");


        final ProgressDialog LunchDialog = new ProgressDialog(this);
        LunchDialog.setMessage("Getting My Lunch Menu");
        LunchDialog.show();
        lunchItemList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                lunchItemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem lunchDetails = dataSnapshot.getValue(FoodItem.class);

                    lunchItemList.add(lunchDetails);

                }

                adapter.notifyDataSetChanged();

                LunchDialog.cancel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                LunchDialog.cancel();

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

        switch (item.getItemId()) {
            case R.id.justcart:
                // Launches Activity
                Intent intent = new Intent(this, Cart.class);
                startActivity(intent);
                break;

        }
        return false;
    }
}
