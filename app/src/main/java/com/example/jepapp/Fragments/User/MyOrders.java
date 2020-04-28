package com.example.jepapp.Fragments.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Users.MyOrdersAdapter;
import com.example.jepapp.Adapters.Users.MyorderequestsAdapter;
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
    RecyclerView recyclerView1;
    DatabaseReference myDBRef;
    SearchView searchView = null;
    List<Orders> myOrderslist =new ArrayList<>();
    List<Reviews> myReviewsList =new ArrayList<>();
    ArrayList<ArrayList<String>> myordertitles =new ArrayList<ArrayList<String>>();
    private SearchView.OnQueryTextListener queryTextListener;
    private Menu menu;
    private MenuInflater inflater;
    public MyOrdersAdapter adapter;
    public MyorderequestsAdapter myorderrequestsadapter;
    private SimpleDateFormat SimpleDateFormater;
    private Date datenow;
    private String email;
    private DatabaseReference databaseReferencebreakfast;
    private DatabaseReference databaseReferencelunch;
    private DatabaseReference databaseReferenceusers;
    private ArrayList<String> alluseremail;
    private String username,employeeid;
    private TextView nodata;
    private DatabaseReference databaseReferenceReviews;
    private List<Orders> myorderequestslist = new ArrayList<>();



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
        myorderequestslist = new ArrayList<>();
        nodata= rootView.findViewById(R.id.orderempty);
        setHasOptionsMenu(true);
        adapter = new MyOrdersAdapter(getContext(),myOrderslist,myReviewsList);
        myorderrequestsadapter = new MyorderequestsAdapter(getContext(),myorderequestslist);
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
    public void DoLunchOrdersQuery() {

        final ProgressDialog LunchDialog = new ProgressDialog(getContext());
        LunchDialog.setMessage("Getting My Orders");
        LunchDialog.show();
        myOrderslist.clear();
        myordertitles.clear();
        myorderequestslist.clear();
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < myOrderslist.size(); i++) {
                    if (myOrderslist.get(i).getType().toLowerCase().equals("lunch")){
                        myOrderslist.remove(i);
                        myordertitles.remove(i);
                        adapter.notifyDataSetChanged();

                    }
                }
                for (int i = 0; i < myorderequestslist.size(); i++) {
                    if (myorderequestslist.get(i).getType().toLowerCase().equals("lunch")){
                        myorderequestslist.remove(i);
                        myorderrequestsadapter.notifyDataSetChanged();
                    }
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    final Orders lunchitems = dataSnapshot.getValue(Orders.class);
                    //Determine if order matches username
                    if(lunchitems.getUsername().equals(username)){
                        myOrderslist.add(lunchitems);
                        myordertitles.add(lunchitems.getOrdertitle());

                    }else if(!(lunchitems.getUsername().equals(username)) && (lunchitems.getPaidby().equals(employeeid)
                            &&lunchitems.getStatus().equals("pending"))){
                        myorderequestslist.add(lunchitems);

                    }

                }
                adapter.notifyDataSetChanged();
                myorderrequestsadapter.notifyDataSetChanged();
                LunchDialog.cancel();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }
    //Function to retrieve current user's breakfast orders
    public void DoBreakfastOrdersQuery() {
        final ProgressDialog BreakfastDialog = new ProgressDialog(getContext());
        BreakfastDialog.setMessage("Getting My Orders");
        BreakfastDialog.show();
        myOrderslist.clear();
        myordertitles.clear();
        myorderequestslist.clear();
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < myOrderslist.size(); i++) {
                    if (myOrderslist.get(i).getType().toLowerCase().equals("breakfast")){
                        myOrderslist.remove(i);
                        myordertitles.remove(i);
                       adapter.notifyDataSetChanged();
                    }
                }
                for (int i = 0; i < myorderequestslist.size(); i++) {
                    if (myorderequestslist.get(i).getType().toLowerCase().equals("breakfast")){
                        myorderequestslist.remove(i);
                        myorderrequestsadapter.notifyDataSetChanged();
                    }
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders breakfastitems = dataSnapshot.getValue(Orders.class);
                    //Determine if the order belongs to the current user
                    if(breakfastitems.getUsername().equals(username)){
                        myOrderslist.add(breakfastitems);
                        myordertitles.add(breakfastitems.getOrdertitle());
                    }
                else if(!(breakfastitems.getUsername().equals(username)) &&
                            (breakfastitems.getPaidby().equals(employeeid)
                                    &&breakfastitems.getStatus().equals("pending"))){
                    myorderequestslist.add(breakfastitems);

                }
                }
                adapter.notifyDataSetChanged();
                myorderrequestsadapter.notifyDataSetChanged();
                BreakfastDialog.cancel();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    //Function to retrieve the user's usernamme and employeeid
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
                    employeeid = userCredentials.getEmpID();


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
        myReviewsList.clear();
            databaseReferenceReviews.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    adapter.notifyDataSetChanged();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Reviews reviews = dataSnapshot.getValue(Reviews.class);

                            myReviewsList.add(reviews);


                    }
                    adapter.notifyDataSetChanged();
                    ReviewsDialog.cancel();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
    //Function to show a dialog with all the order requests sent to the user
    private void OpenOrderRequestsDialog() {
        AlertDialog.Builder OLrderRequestsDialogBuilder = new AlertDialog.Builder(getContext(),R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        OLrderRequestsDialogBuilder.setTitle("Order Requests");
        //Add Custom Layout
        final View customLayout = getLayoutInflater().inflate(R.layout.myorderrequests, null);
        OLrderRequestsDialogBuilder.setView(customLayout);
        RecyclerView recyclerView = customLayout.findViewById(R.id.viewallrorderequests);
        LinearLayoutManager linearLayoutManagerrequests = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManagerrequests);
        recyclerView.setAdapter(myorderrequestsadapter);
        OLrderRequestsDialogBuilder.setPositiveButton("Go Back",null);
        final AlertDialog OrderRequestAlert = OLrderRequestsDialogBuilder.create();
        OrderRequestAlert.show();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        this.menu = menu;
        this.inflater = inflater;
        menu.clear();
        inflater.inflate(R.menu.myorders_menu, menu);
        MenuItem ordersitem = menu.findItem(R.id.myorder_requests);
        MenuItemCompat.setActionView(ordersitem, R.layout.myactionbar_badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(ordersitem);
        final TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv.setText(String.valueOf(myorderrequestsadapter.getItemCount()));
        notifCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (myorderrequestsadapter.getItemCount() ==0){
                    tv.setText(String.valueOf(myorderrequestsadapter.getItemCount()));
                    Toast.makeText(getContext(), "You have no Order Requests", Toast.LENGTH_SHORT).show();
                }else{
                    OpenOrderRequestsDialog();
                }
            }
        });


        android.view.MenuItem searchItem = menu.findItem(R.id.myorders_action_search);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null){
            searchView = (SearchView)searchItem.getActionView();
        }
        if(searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    String userInput = newText.toLowerCase();
                    List<Orders> searchorderslist = new ArrayList<>();
                    getActivity().onSearchRequested();

                    for (int i = 0; i< myOrderslist.size(); i++){

                        if (myOrderslist.get(i).getStatus().toLowerCase().contains(userInput)|| myOrderslist.get(i).getType().toLowerCase().contains(userInput)) {

                            searchorderslist.add(myOrderslist.get(i));
                        }

                    }
                    adapter.updateList(searchorderslist);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu,inflater);

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.myorders_action_search:
                return true;
            case R.id.myorder_requests:

            default:
                break;

        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }




}
