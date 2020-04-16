package com.example.jepapp.Activities.Admin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Users.Cart;
import com.example.jepapp.Adapters.Admin.AllitemsAdapter;
import com.example.jepapp.Adapters.Users.FoodListAdapter;
import com.example.jepapp.Models.FoodItem;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SelectMenuItems2 extends AppCompatActivity {

    //a list to store all the products
    List<MItems> foodItemList;
    AllitemsAdapter adapter;

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;
    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_imenu_items);
        getSupportActionBar().setTitle("Breakfast Menu");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        foodItemList = new ArrayList<>();
        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.allmenuitems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllitemsAdapter(getApplicationContext(), foodItemList,"Menu");
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(SelectMenuItems2.this);


        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");
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

                    MItems breakfastDetails = dataSnapshot.getValue(MItems.class);

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
                Intent intent = new Intent(this, AdminMenuCart.class);
                startActivity(intent);
                break;

        }
        return false;
    }
}
