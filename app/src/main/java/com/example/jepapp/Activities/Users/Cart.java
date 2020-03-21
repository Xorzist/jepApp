package com.example.jepapp.Activities.Users;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jepapp.Adapters.Users.cartAdapter;
import com.example.jepapp.Models.FoodItem;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    cartAdapter breakfastadapter, lunchadapter;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    Button breakfastcheckout,lunchcheckout;
    private ArrayList<com.example.jepapp.Models.Cart> breakfastcart;
    private ArrayList<com.example.jepapp.Models.Cart> lunchcart;
    RecyclerView breakfastrecycler,lunchrecycler;
    private LinearLayoutManager linearLayoutManager,linearLayoutManager2;
    private DividerItemDecoration dividerItemDecoration;
    private DividerItemDecoration dividerItemDecoration2;
    private DatabaseReference databaseReferencebreakfast;
    private String email;
    private DatabaseReference databaseReferencelunch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_cart);
        breakfastcart = new ArrayList<>();
        lunchcart = new ArrayList<com.example.jepapp.Models.Cart>();
        breakfastcheckout = findViewById(R.id.breakfastcheckout);
        lunchcheckout = findViewById(R.id.lunchcheckout);
        breakfastrecycler = findViewById(R.id.cartbreakfastlist);
        lunchrecycler = findViewById(R.id.cartlunchlist);
         mAuth = FirebaseAuth.getInstance();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        dividerItemDecoration = new DividerItemDecoration(breakfastrecycler.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration2 = new DividerItemDecoration(lunchrecycler.getContext(), linearLayoutManager.getOrientation());
        breakfastadapter = new cartAdapter(getApplicationContext(),breakfastcart);
        lunchadapter = new cartAdapter(getApplicationContext(),lunchcart);

        breakfastrecycler.setLayoutManager(linearLayoutManager2);
        lunchrecycler.setLayoutManager(linearLayoutManager);
        breakfastrecycler.addItemDecoration(dividerItemDecoration);
        lunchrecycler.addItemDecoration(dividerItemDecoration2);
        breakfastrecycler.setAdapter(breakfastadapter);
        lunchrecycler.setAdapter(lunchadapter);

        email = mAuth.getCurrentUser().getEmail().replace(".","");
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastCart").child(email);
       getbreakfastcart();
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchCart").child(email);
        getLunchcart();

    }

    private void getLunchcart() {
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Cart lunchitems = dataSnapshot.getValue(com.example.jepapp.Models.Cart.class);

                    lunchcart.add(lunchitems);
                }


                breakfastadapter.notifyDataSetChanged();
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

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Cart breakfastitems = dataSnapshot.getValue(com.example.jepapp.Models.Cart.class);

                    breakfastcart.add(breakfastitems);
                }


                lunchadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
