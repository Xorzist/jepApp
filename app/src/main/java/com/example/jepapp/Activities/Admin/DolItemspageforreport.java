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
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DolItemspageforreport extends AppCompatActivity {

    //a list to store all the products
    List<MItems> itemsList;
    AllitemsAdapter adapter;

    DatabaseReference databaseReference;

    ProgressDialog progressDialog3;
    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfastmenurecycleer);
        getSupportActionBar().setTitle("All items");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemsList = new ArrayList<>();
        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.breakfastrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllitemsAdapter(DolItemspageforreport.this, itemsList,"Report");
        recyclerView.setAdapter(adapter);
//        getBreakfastData();
        progressDialog3 = new ProgressDialog(DolItemspageforreport.this);

        progressDialog3.setMessage("Loading Comments from Firebase Database");

        progressDialog3.show();
        //  itemsList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MItems breakfastDetails = dataSnapshot.getValue(MItems.class);

                    itemsList.add(breakfastDetails);
                    // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
                }


                adapter.notifyDataSetChanged();

                progressDialog3.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog3.dismiss();

            }
        });



    }

}

