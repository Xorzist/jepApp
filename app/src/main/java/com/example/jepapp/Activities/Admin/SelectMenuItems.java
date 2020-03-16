package com.example.jepapp.Activities.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Admin.SelectMenuItemsAdaptertest;
import com.example.jepapp.Fragments.Admin.Make_Menu;
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


    DatabaseReference myDBRefMenuItems, myDBRef;

    ProgressDialog progressDialog;

    List<Admin_Made_Menu> list = new ArrayList<Admin_Made_Menu>();
    private ArrayList<String> arrayListTitles, arrayListQuantities, allitemsArray, arrayListChecker;



    RecyclerView recyclerView;

    SelectMenuItemsAdaptertest adapter ;
    private LinearLayoutManager linearLayoutManager;
    private Button breakfastbtn, lunchbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instantiating
        breakfastbtn= findViewById(R.id.save_breakfast);
        lunchbtn = findViewById(R.id.save_lunch);

        recyclerView = (RecyclerView) findViewById(R.id.checkboxrecycler);
        list=new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setHasFixedSize(true);

        adapter = new SelectMenuItemsAdaptertest(getApplicationContext(), list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(SelectMenuItems.this);

        progressDialog.setMessage("Loading Menu Items from Firebase Database");

        progressDialog.show();

        //retrieve menu items from firebase database
        myDBRefMenuItems = FirebaseDatabase.getInstance().getReference().child("JEP").child("MenuItems");
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");

        myDBRefMenuItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu studentDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    list.add(studentDetails);
                }

                // update recycler view
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
                //new list with all item titles
                allitemsArray = new ArrayList();
                for (int i=0; i <list.size();i++){
                     allitemsArray.add(list.get(i).getTitle());


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

        arrayListQuantities = adapter.getArrayListQuantity();
        arrayListTitles = adapter.getArrayListTitle();
        arrayListChecker = adapter.getArrayListChecker();


        breakfastbtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (arrayListChecker.size() > arrayListTitles.size()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectMenuItems.this);
                    builder.setMessage("There seems to be items clicked that were not saved. NB: A saved item is reflected as green")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else{
                // create a table called breakfast menu to store the checked menu items
                DatabaseReference dbref = myDBRef.child("BreakfastMenu");
                dbref.removeValue();
                for (int i = 0; i < arrayListTitles.size(); i++) {
                    int a = i;
                    while (a < allitemsArray.size()) {
                        if (allitemsArray.get(a) == arrayListTitles.get(i)) {
                            // getting the information from the model item to be stored
                            String title = list.get(a).getTitle();
                            String quantity = arrayListQuantities.get(i);
                            String ingredients = list.get(a).getIngredients();
                            String id = list.get(a).getId();
                            Float price = list.get(a).getPrice();
                            String image = list.get(a).getImage();
                            String type = "Breakfast";
                            String key = myDBRef.child("BreakfastMenu").push().getKey();
                            Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image, key, type);
                            myDBRef.child("BreakfastMenu")
                                    .child(key)
                                    .setValue(mItems);
                            Log.d("Start Adding", "START!");
                        }
                        a++;
                    }


                }

                getSupportFragmentManager().beginTransaction().add(android.R.id.content, new Make_Menu()).commit();

                finish();

            }}

            });
         progressDialog.dismiss();



        lunchbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (arrayListChecker.size() > arrayListTitles.size()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectMenuItems.this);
                    builder.setMessage("There seems to be items clicked that were not saved. NB: A saved item is reflected as green")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else
                {
                    // create a table called breakfast menu to store the checked menu items
                    DatabaseReference dbref = myDBRef.child("Lunch");
                    dbref.removeValue();
                    for (int i = 0; i < arrayListTitles.size(); i++) {
                        int a = i;
                        while (a < allitemsArray.size()) {
                            if (allitemsArray.get(a) == arrayListTitles.get(i)) {
                                // getting the information from the model item to be stored
                                String title = list.get(a).getTitle();
                                String quantity = arrayListQuantities.get(i);
                                String ingredients = list.get(a).getIngredients();
                                String id = list.get(a).getId();
                                Float price = list.get(a).getPrice();
                                String image = list.get(a).getImage();
                                String type = "Lunch";
                                String key = myDBRef.child("Lunch").push().getKey();
                                Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image,key, type);
                                myDBRef.child("Lunch")
                                        .child(key)
                                        .setValue(mItems);
                                Log.d("Start Adding", "START!");
                            }
                            a++;
                        }


                    }

                    getSupportFragmentManager().beginTransaction().add(android.R.id.content, new Make_Menu()).commit();

                    finish();

                }}

        });

    }




}
