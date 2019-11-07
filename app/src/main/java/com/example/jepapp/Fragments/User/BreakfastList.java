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
//        getBreakfastData();
        progressDialog = new ProgressDialog(BreakfastList.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();
      //  foodItemList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem breakfastDetails = dataSnapshot.getValue(FoodItem.class);

                    foodItemList.add(breakfastDetails);
                    // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
                }


                adapter.notifyDataSetChanged();

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });




//    @Override
//    public void onItemClick(int position) {
//        // to get the position of the item selected in the adapter
//        breakfastItemList.get(position);
//        //start order intent
//        Intent intent = new Intent(this, OrderPageActivity.class);
//        startActivity(intent);
//    }
    }

    private void getBreakfastData() {

//        progressDialog = new ProgressDialog(BreakfastList.this);
//
//        progressDialog.setMessage("Loading Data from Firebase Database");
//
//        progressDialog.show();
//        foodItemList = new ArrayList<>();
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    FoodItem breakfastDetails = dataSnapshot.getValue(FoodItem.class);
//
//                    foodItemList.add(breakfastDetails);
//                    // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
//                }
//
//
//                adapter.notifyDataSetChanged();
//
//                progressDialog.dismiss();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                progressDialog.dismiss();
//
//            }
//        });
//
//    }
    }
}
