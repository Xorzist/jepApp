package com.example.jepapp.Fragments.Admin;

import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Admin.AllOrdersAdapter;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewBalances extends Fragment   {
    List<com.example.jepapp.Models.Orders> allprepared, allcancelled, allprepared2, allcancelled2;
    RecyclerView recyclerView_prepared, recyclerView_cancelled;
    ProgressDialog progressDialog;
    DatabaseReference myDBref;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    public AllOrdersAdapter adapterprepared, adaptercancelled;
   // private Button acceptall_breakfast, acceptall_lunch;
    private FloatingActionButton lunch_refresh, breakfast_refresh;
    SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    List<UserCredentials> userList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.admin_fragment_order_, container, false);
        recyclerView_prepared = (RecyclerView) rootView.findViewById(R.id.ordersbreakfastlist);
        recyclerView_cancelled = rootView.findViewById(R.id.orderslunchlist);
        allprepared = new ArrayList<>();
        allcancelled = new ArrayList<>();
        allprepared2 = new ArrayList<>();
        DatabaseReference databaseReferenceforuser;
        allcancelled2 = new ArrayList<>();
        lunch_refresh = rootView.findViewById(R.id.lunch_resize);
        breakfast_refresh = rootView.findViewById(R.id.breakfast_resize);
        adapterprepared = new AllOrdersAdapter(getContext(), allprepared, userList);
//        acceptall_breakfast = rootView.findViewById(R.id.update_allbreakfast);
//        acceptall_lunch = rootView.findViewById(R.id.update_allLunch);
//        acceptall_breakfast.setVisibility(View.VISIBLE);
//        acceptall_lunch.setVisibility(View.VISIBLE);
        adaptercancelled = new AllOrdersAdapter(getContext(), allcancelled, userList);
        myDBref = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_prepared.getContext(), linearLayoutManager.getOrientation());
        linearLayoutManager2 = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(recyclerView_cancelled.getContext(), linearLayoutManager2.getOrientation());
        recyclerView_prepared.setLayoutManager(linearLayoutManager);
        recyclerView_cancelled.setLayoutManager(linearLayoutManager2);
        recyclerView_prepared.setAdapter(adapterprepared);
        recyclerView_cancelled.setAdapter(adaptercancelled);
        setHasOptionsMenu(true);

        getpreparedBreakfastOrders();
        getpreparedLunchOrders();
       // getcancelledBreakfastOrders();
       // getcancelledLunchOrders();

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Balances from Firebase Database");

        progressDialog.show();

        databaseReferenceforuser = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

        databaseReferenceforuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials userinfo = dataSnapshot.getValue(UserCredentials.class);


                    userList.add(userinfo);
                }}
            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }



        });

        // initSwipe();
        return  rootView;
    }



    private void getpreparedBreakfastOrders() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Prepared Breakfast Orders");

        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders")
                .orderByChild("status").equalTo("Prepared");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allprepared.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allprepared.add(allfoodorders);

                }
                Collections.reverse(allprepared);

                adapterprepared.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();

            }

        });

    }
    private void getpreparedLunchOrders() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Prepared Lunch Orders");

        progressDialog.show();

        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders")
                .orderByChild("status").equalTo("Prepared");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allcancelled.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allcancelled.add(allfoodorders);

                }
               Collections.reverse(allcancelled);
                adaptercancelled.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();

            }

        });

    }
    private void getcancelledBreakfastOrders() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Prepared Breakfast Orders");

        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders")
                .orderByChild("status").equalTo("Cancelled");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allprepared.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allcancelled.add(allfoodorders);

                }
                Collections.reverse(allcancelled);
                adaptercancelled.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();

            }

        });

    }
    private void getcancelledLunchOrders() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Prepared Lunch Orders");

        progressDialog.show();

        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders")
                .orderByChild("status").equalTo("Cancelled");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 allcancelled2.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allcancelled2.add(allfoodorders);

                }
                 Collections.reverse(allcancelled2);
                adaptercancelled.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();

            }

        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        final List<com.example.jepapp.Models.Orders> combinedlist = new ArrayList<>();
        combinedlist.addAll(allprepared);
        combinedlist.addAll(allcancelled);
        inflater.inflate(R.menu.main_menu, menu);
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        // searchItem.setVisible(false);
        //getActivity().invalidateOptionsMenu(); Removed because of scrolling toolbar animation
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
//        searchView.setIconified(false);
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

                    Log.d("Query", newText);
                    String userInput = newText.toLowerCase();
                    List<com.example.jepapp.Models.Orders> newList = new ArrayList<>();
                    List<com.example.jepapp.Models.Orders> newListlunch = new ArrayList<>();
                    // for (com.example.jepapp.Models.Orders orders : allorderslist) {

                    //if (!searchView.isIconified()) {
                    getActivity().onSearchRequested();
                    //  com.example.jepapp.Models.Orders orders;
                    for (int i = 0; i< allprepared.size(); i++){
                        //Log.e("idk",allorderslist.get(i).getOrdertitle().toLowerCase());
                        ArrayList<String> orderstuff = allprepared.get(i).getOrdertitle();
                        String listString = "";
                        for (String s : orderstuff)
                        {
                            listString += s + "\t";
                        }
                        //Todo address this by uncommenting
                        if (allprepared.get(i).getUsername().toLowerCase().contains(userInput)|| listString.toLowerCase().contains(userInput))
                        {

                            newList.add(allprepared.get(i));
                            //Log.e("Eror", newList.get(0).getOrdertitle());
                        }


                        // }

                    }    for (int i = 0; i< allcancelled.size(); i++){
                        //Log.e("idk",allorderslist.get(i).getOrdertitle().toLowerCase());
                        ArrayList<String> orderstufflunch = allcancelled.get(i).getOrdertitle();
                        String listStringLunch = "";
                        for (String s : orderstufflunch)
                        {
                            listStringLunch += s + "\t";
                        }
                        //Todo address this by uncommenting
                        if (allcancelled.get(i).getUsername().toLowerCase().contains(userInput)|| listStringLunch.toLowerCase().contains(userInput))
                        {

                            newListlunch.add(allcancelled.get(i));
                            //Log.e("Eror", newList.get(0).getOrdertitle());
                        }


                        // }

                    }

                    adapterprepared.updateList(newList);
                    adaptercancelled.updateList(newListlunch);

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
            case R.id.action_search:

                return false;
            default:
                break;

        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }



}