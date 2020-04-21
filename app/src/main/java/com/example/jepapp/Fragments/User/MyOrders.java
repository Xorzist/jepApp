package com.example.jepapp.Fragments.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Users.MyOrdersAdapter;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.Reviews;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MyOrders extends Fragment {
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView1,recyclerView2;
    DatabaseReference databaseReference, myDBRef;


    List<Orders> myOrderslist =new ArrayList<>();
    List<Reviews> myReviewsList =new ArrayList<>();
    ArrayList<ArrayList<String>> myordertitles =new ArrayList<ArrayList<String>>();


    public MyOrdersAdapter adapter;
    private SimpleDateFormat SimpleDateFormater;
    private Date datenow;
    private String email;
    private DatabaseReference databaseReferencebreakfast;
    private DatabaseReference databaseReferencelunch;
    private DatabaseReference databaseReferenceusers;
    private ArrayList<String> alluseremail;
    private String username;
    private TextView nodata;
    private DatabaseReference databaseReferenceReviews;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.customer_orders, container, false);
        recyclerView1 = rootView.findViewById(R.id.customerordersrecycler);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();
        SimpleDateFormater = new SimpleDateFormat("dd/MM/yyyy");
        datenow = new Date();
        myOrderslist = new ArrayList<>();
        alluseremail = new ArrayList<>();
        myordertitles = new ArrayList<>();
        nodata= rootView.findViewById(R.id.orderempty);


        adapter = new MyOrdersAdapter(getContext(),myOrderslist,myReviewsList);


        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView1.getContext(), linearLayoutManager.getOrientation());
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView1.setAdapter(adapter);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
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
        Collections.reverse(myOrderslist);

        databaseReferenceReviews = FirebaseDatabase.getInstance().getReference("JEP").child("Reviews");
        //Method to assign reviews to orders
        DoReviewsSort();

        return  rootView;


    }

    //Function to retrieve all lunch orders for the current user
    private void DoLunchOrdersQuery() {
        final ProgressDialog BreakfastordersDialog = new ProgressDialog(getContext());
        BreakfastordersDialog.setMessage("Getting My Orders");
        BreakfastordersDialog.show();
        myOrderslist.clear();
        myordertitles.clear();
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    final Orders lunchitems = dataSnapshot.getValue(Orders.class);
                    //Determine if order matches username
                    if(lunchitems.getUsername().equals(username)){
                        myOrderslist.add(lunchitems);
                        myordertitles.add(lunchitems.getOrdertitle());
                    }

                }
                adapter.notifyDataSetChanged();
                BreakfastordersDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }
    //Function to retrieve current user's breakfast orders
    private void DoBreakfastOrdersQuery() {
        final ProgressDialog LunchOrdersDialog = new ProgressDialog(getContext());
        LunchOrdersDialog.setMessage("Getting My Orders");
        LunchOrdersDialog.show();
        myOrderslist.clear();
        myordertitles.clear();
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders breakfastitems = dataSnapshot.getValue(Orders.class);
                    //Determine if the order belongs to the current user
                    if(breakfastitems.getUsername().equals(username)){
                        myOrderslist.add(breakfastitems);
                        myordertitles.add(breakfastitems.getOrdertitle());
                    }
                }
                adapter.notifyDataSetChanged();
                LunchOrdersDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    //Function to retrieve the user's usernamme
    public void DoUsernamequery(){
        final ProgressDialog UsernameDialog = new ProgressDialog(getContext());
        UsernameDialog.setMessage("Obtaining the username");
        UsernameDialog.show();
        Query emailquery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

        emailquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);


                    //Set the username and balance of the current user
                    username = userCredentials.getUsername();


                }
                UsernameDialog.cancel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }
    //Function to retrieve reviews from the database
    public void DoReviewsSort(){
        final ProgressDialog ReviewsDialog = new ProgressDialog(getContext());
        ReviewsDialog.setMessage("Obtaining the Reviews");
        ReviewsDialog.show();
            databaseReferenceReviews.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    myReviewsList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Reviews reviews = dataSnapshot.getValue(Reviews.class);

                            myReviewsList.add(reviews);
                            adapter.notifyDataSetChanged();

                    }
                    ReviewsDialog.cancel();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }


}
