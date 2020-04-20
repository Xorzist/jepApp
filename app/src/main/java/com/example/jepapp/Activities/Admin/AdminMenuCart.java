package com.example.jepapp.Activities.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Adapters.Admin.adminCartAdapter;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.Models.Cart;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;


public class AdminMenuCart extends AppCompatActivity {
    adminCartAdapter breakfastadapter, lunchadapter;
    private DatabaseReference myDBRef,databaseReferencelunch;
    Button breakfastcheckout,lunchcheckout;
    private ArrayList<com.example.jepapp.Models.Cart> breakfastcart;
    private ArrayList<com.example.jepapp.Models.Cart> lunchcart;
    RecyclerView breakfastrecycler,lunchrecycler;
    private DatabaseReference databaseReferencebreakfast;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menucart);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Select Items");
        breakfastcart = new ArrayList<>();
        lunchcart = new ArrayList<>();
        breakfastcheckout = findViewById(R.id.breakfastcheckout);
        lunchcheckout = findViewById(R.id.lunchcheckout);
        breakfastrecycler = findViewById(R.id.cartbreakfastlist);
        lunchrecycler = findViewById(R.id.cartlunchlist);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(breakfastrecycler.getContext(), linearLayoutManager.getOrientation());
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(lunchrecycler.getContext(), linearLayoutManager.getOrientation());
        breakfastadapter = new adminCartAdapter(AdminMenuCart.this,breakfastcart);
        lunchadapter = new adminCartAdapter(AdminMenuCart.this,lunchcart);
        breakfastrecycler.setLayoutManager(linearLayoutManager2);
        lunchrecycler.setLayoutManager(linearLayoutManager);
        breakfastrecycler.addItemDecoration(dividerItemDecoration);
        lunchrecycler.addItemDecoration(dividerItemDecoration2);
        breakfastrecycler.setAdapter(breakfastadapter);
        lunchrecycler.setAdapter(lunchadapter);
        ProgressDialog adminMenCartprogress = new ProgressDialog(AdminMenuCart.this);
        adminMenCartprogress.setMessage("Setting up Cart");
        adminMenCartprogress.show();

        //dbreference for breakfast cart table
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastCart").child("Admin Menu");
        //Method to get all items in the breakfast cart
        getbreakfastcart();
        //dbreference for lunch cart table
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchCart").child("Admin Menu");
        //Method to get all items in the lunch cart
        getLunchcart();




        breakfastcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function to handle the breakfast checkout button
                    if (breakfastcart.size()<=0){
                        Toast.makeText(getApplicationContext(),"You have no items in the breakfast cart",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        breakfastcheckingout();
                    }
            }
        });

        lunchcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function to handle the lunch checkout button
               if (lunchcart.size()<=0) {
                        Toast.makeText(getApplicationContext(),"You have no items in the lunch cart",Toast.LENGTH_SHORT).show();
                    }
               else{
                   lunchcheckingout();
            }
            }
        });

        adminMenCartprogress.dismiss();

    }



    //function to add menu items in cart to the Lunch Menu table in firebase
    private void lunchcheckingout() {
       DatabaseReference dbref = myDBRef.child("Lunch");
            //clears the lunch table
            dbref.removeValue();
            //adds each item in the lunch cart to the lunch menu table
            for (int i = 0; i < lunchcart.size(); i++) {
                String title = lunchcart.get(i).getOrdertitle();
                String quantity = lunchcart.get(i).getQuantity();
                String ingredients = lunchcart.get(i).getIngredients();
                String id = lunchcart.get(i).getID();
                Float price = Float.valueOf(lunchcart.get(i).getCost());
                String image = lunchcart.get(i).getImage();
                String type = "Lunch";
                String key = myDBRef.child("Lunch").push().getKey();
                //creates Admin_Made_Menu object to be stored
                Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image, key, type);
                assert key != null;
                myDBRef.child("Lunch")
                        .child(key)
                        .setValue(mItems);

            }
        DatabaseReference ref = myDBRef.child("LunchCart").child("Admin Menu");
            //clears the lunch cart
        ref.removeValue();
        //closes current activity and reloads the cart
        Reloadit();
    }







    private void breakfastcheckingout() {
            DatabaseReference dbref = myDBRef.child("BreakfastMenu");
        //clears the breakfast table
            dbref.removeValue();
        //adds each item in the breakfast cart to the breakfast menu table
            for (int i = 0; i < breakfastcart.size(); i++) {
                String title = breakfastcart.get(i).getOrdertitle();
                String quantity = breakfastcart.get(i).getQuantity();
                String ingredients = breakfastcart.get(i).getIngredients();
                String id = breakfastcart.get(i).getID();
                Float price = Float.valueOf(breakfastcart.get(i).getCost());
                String image = breakfastcart.get(i).getImage();
                String type = "Breakfast";
                String key = myDBRef.child("BreakfastMenu").push().getKey();
                //creates Admin_Made_Menu object to be stored
                Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image, key, type);
                assert key != null;
                myDBRef.child("BreakfastMenu")
                        .child(key)
                        .setValue(mItems);


            }

        DatabaseReference ref = myDBRef.child("BreakfastCart").child("Admin Menu");
            //clears breakfast cart
        ref.removeValue();
        //closes the current activity and reloads the cart
        Reloadit();
        }




    private void getLunchcart() {
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cart lunchitems = dataSnapshot.getValue(Cart.class);


                    lunchcart.add(lunchitems);
                }


                lunchadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getbreakfastcart() {

        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                breakfastcart.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cart breakfastitems = dataSnapshot.getValue(Cart.class);

                    breakfastcart.add(breakfastitems);

                }

                breakfastadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //function to close current activity
    public void Reloadit(){
        finish();
        startActivity(getIntent());
    }


}
