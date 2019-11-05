package com.example.jepapp.Activities.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.RecyclerViewAdaptertest;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectMenuItems extends AppCompatActivity {


    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<Admin_Made_Menu> list = new ArrayList<Admin_Made_Menu>();
    private ArrayList<String> arrayListTitles, arrayListQuantities, allitemsArray;



    RecyclerView recyclerView;

    RecyclerViewAdaptertest adapter ;
    private LinearLayoutManager linearLayoutManager;
    private Button breakfastbtn, lunchbtn;
    private CheckBox checkboxes;
    private EditText quantityfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        breakfastbtn= findViewById(R.id.save_breakfast);
        lunchbtn = findViewById(R.id.save_lunch);
//        checkboxes = findViewById(R.id.checkbox1);
//        quantityfield = findViewById(R.id.quantity);

        recyclerView = (RecyclerView) findViewById(R.id.checkboxrecycler);
        list=new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setHasFixedSize(true);

      //  recyclerView.setLayoutManager(new LinearLayoutManager(SelectMenuItems.this));
        adapter = new RecyclerViewAdaptertest(getApplicationContext(), list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(SelectMenuItems.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("MenuItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu studentDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    list.add(studentDetails);
                   // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
                }

//                adapter = new RecyclerViewAdaptertest(SelectMenuItems.this, list);
//
//                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
                allitemsArray = new ArrayList();
                for (int i=0; i <list.size();i++){
                    // int h = list.size();
                     allitemsArray.add(list.get(i).getTitle());
//                   Log.e("allitemsarray", allitemsArray.get(i));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });


        breakfastbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayListQuantities = adapter.getArrayListQuantity();
//                Log.e("arraylistquantities",arrayListQuantities.get(0));
                arrayListTitles = adapter.getArrayListTitle();


                /*
                To be done : Iterate through the array list named list until a specific
                title is found. When the title is found , create a Admin_Made_Menu object using
                that list index object.
                */
               // Log.e("arraylisttitles", String.valueOf(arrayListTitles.size()));
               // for (int i=0; i<arrayListTitles.size(); i++){
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("BreakfastMenu");
                dbref.removeValue();
                for (int i=0;i<arrayListTitles.size();i++){
                    int a=i;
                    while (a<allitemsArray.size()){
                        if (allitemsArray.get(a)==arrayListTitles.get(i)) {
                            //int i = a;
                            String title = list.get(a).getTitle();
                            String quantity = arrayListQuantities.get(i);
                            String ingredients = list.get(a).getIngredients();
                            String id = list.get(a).getId();
                            Float price = list.get(a).getPrice();
                            String image = list.get(a).getImage();
                            Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image);
                           String key = databaseReference.child("BreakfastMenu").push().getKey();
                            databaseReference.child("BreakfastMenu")
                                    .child(key)
                                    .setValue(mItems);

                            Log.d("Start Adding", "START!");
                        }
                        a++;
                        }


                    }

                Log.e("do them","Done");
                onBackPressed();

            }
        });


        lunchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayListQuantities = adapter.getArrayListQuantity();
                arrayListTitles = adapter.getArrayListTitle();


                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Lunch");
                dbref.removeValue();
                for (int i=0;i<arrayListTitles.size();i++){
                    int a=i;
                    while (a<allitemsArray.size()){
                        if (allitemsArray.get(a)==arrayListTitles.get(i)) {
                            //int i = a;
                            String title = list.get(a).getTitle();
                            String quantity = arrayListQuantities.get(i);
                            String ingredients = list.get(a).getIngredients();
                            String id = list.get(a).getId();
                            Float price = list.get(a).getPrice();
                            String image = list.get(a).getImage();
                            Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image);
                            String key = databaseReference.child("Lunch").push().getKey();
                            databaseReference.child("Lunch")
                                    .child(key)
                                    .setValue(mItems);
                            Log.d("Start Adding", "START!");
                        }
                        a++;
                    }


                }

                Log.e("do them","Done");
                onBackPressed();

            }
        });

    }




}