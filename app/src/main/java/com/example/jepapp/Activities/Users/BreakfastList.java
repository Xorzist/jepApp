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


    List<FoodItem> foodItemList;
    FoodListAdapter adapter;

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menusrecycler);
        getSupportActionBar().setTitle("Breakfast Menu");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        foodItemList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.menusrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodListAdapter(BreakfastList.this, foodItemList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
        Runreference();


    }

    private void Runreference() {
        final ProgressDialog BreakfastDialog = new ProgressDialog(this);
        BreakfastDialog.setMessage("Getting My Breakast Menu");
        BreakfastDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem breakfastDetails = dataSnapshot.getValue(FoodItem.class);

                    foodItemList.add(breakfastDetails);
                }
                adapter.notifyDataSetChanged();
                BreakfastDialog.cancel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                BreakfastDialog.cancel();

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
