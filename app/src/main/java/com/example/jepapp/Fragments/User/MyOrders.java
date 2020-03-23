package com.example.jepapp.Fragments.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Users.MyOrdersAdapter;
import com.example.jepapp.Adapters.Users.MyOrdertitlesAdapter;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.Ordertitle;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.example.jepapp.SwipeController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyOrders extends Fragment {
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView1,recyclerView2;
    DatabaseReference databaseReference, myDBRef;

    ProgressDialog progressDialog;

    SwipeController swipeControl = null;
    List<Orders> myOrderslist =new ArrayList<>();
    ArrayList<ArrayList<String>> myordertitles =new ArrayList<ArrayList<String>>();


    MyOrdertitlesAdapter ordertitlesadapter ;
    public MyOrdersAdapter adapter;
    private SimpleDateFormat SimpleDateFormater;
    private Date datenow;
    private String email;
    private DatabaseReference databaseReferencebreakfast;
    private DatabaseReference databaseReferencelunch;
    private DatabaseReference databaseReferenceusers;
    private ArrayList<String> alluseremail;
    private String username;
    //private ArrayList<ArrayList<String>> myOrdertitles;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.customer_orders, container, false);
        recyclerView1 = rootView.findViewById(R.id.customerordersrecycler);
//        recyclerView2 = recyclerView1.findContainingItemView(rootView).findViewById(R.id.customerorderitems);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();
        SimpleDateFormater = new SimpleDateFormat("dd/MM/yyyy");
        datenow = new Date();
        myOrderslist = new ArrayList<>();
        alluseremail = new ArrayList<>();
        myordertitles = new ArrayList<ArrayList<String>>();

        //ordertitlesadapter = new MyOrdertitlesAdapter(myordertitles,getContext());



        adapter = new MyOrdersAdapter(getContext(),myOrderslist);


        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView1.getContext(), linearLayoutManager.getOrientation());
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView1.setAdapter(adapter);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
//        recyclerView2.setLayoutManager(linearLayoutManager);
//        recyclerView2.setAdapter(ordertitlesadapter);

        email = mAuth.getCurrentUser().getEmail();
        //Method to get the username
        DoUsernamequery();


        //dbreference for breakfast orders
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
        DoBreakfastOrdersQuery();
        //dbreference for lunch orders
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
        DoLunchOrdersQuery();
        //dbreference for users table
        databaseReferenceusers = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        //Method to get all users in the Users table

      


        return  rootView;


    }

    private void DoLunchOrdersQuery() {
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders lunchitems = dataSnapshot.getValue(Orders.class);

                    if(lunchitems.getUsername().equals(username)){
                        myOrderslist.add(lunchitems);
                        myordertitles.add(lunchitems.getOrdertitle());
                        Log.e("ordertitlesman",lunchitems.getOrdertitle().toString() );
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void DoBreakfastOrdersQuery() {
        //This function will assign the orders of the current user to a list
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders breakfastitems = dataSnapshot.getValue(Orders.class);

                    if(breakfastitems.getUsername().equals(username)){
                        myOrderslist.add(breakfastitems);
                        myordertitles.add(breakfastitems.getOrdertitle());
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    public void DoUsernamequery(){
        //This function will assign the username of the current user to a variable
        Query emailquery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

        emailquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);
                    //Log.e("onDataChange: ", allmyorders.getTitle().toString());

                    //Set the username and balance of the current user
                    username = userCredentials.getUsername();
                    Log.e("The name",username );
                    //balance = userCredentials.getBalance();


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }


    public DatabaseReference getDb() {
        return myDBRef;
    }

}
