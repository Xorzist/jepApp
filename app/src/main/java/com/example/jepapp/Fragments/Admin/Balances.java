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
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Balances extends Fragment   {
    List<com.example.jepapp.Models.Orders> allcancelled, allcancelled2;
    RecyclerView recyclerView_cancelled2, recyclerView_cancelled;
    DatabaseReference myDBref;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    public AllOrdersAdapter adaptercancelled2, adaptercancelled;
    private FloatingActionButton lunch_reize, breakfast_resize;
    SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    List<UserCredentials> userList = new ArrayList<>();
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.admin_fragment_order_, container, false);
        recyclerView_cancelled2 = (RecyclerView) rootView.findViewById(R.id.ordersbreakfastlist);
        recyclerView_cancelled = rootView.findViewById(R.id.orderslunchlist);
        allcancelled2 = new ArrayList<>();
        allcancelled = new ArrayList<>();
        DatabaseReference databaseReferenceforuser;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(calendar.getTime());

        allcancelled2 = new ArrayList<>();
        lunch_reize = rootView.findViewById(R.id.lunch_resize);
        breakfast_resize = rootView.findViewById(R.id.breakfast_resize);
        adaptercancelled2 = new AllOrdersAdapter(getContext(), allcancelled2, userList);

        adaptercancelled = new AllOrdersAdapter(getContext(), allcancelled, userList);
        myDBref = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_cancelled2.getContext(), linearLayoutManager.getOrientation());
        linearLayoutManager2 = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(recyclerView_cancelled.getContext(), linearLayoutManager2.getOrientation());
        recyclerView_cancelled2.setLayoutManager(linearLayoutManager);
        recyclerView_cancelled.setLayoutManager(linearLayoutManager2);
        recyclerView_cancelled2.setAdapter(adaptercancelled2);
        recyclerView_cancelled.setAdapter(adaptercancelled);
        setHasOptionsMenu(true);


         getcancelledBreakfastOrders();
         getcancelledLunchOrders();

        lunch_reize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView_cancelled.getVisibility() == View.GONE) {
                    recyclerView_cancelled.setVisibility(View.VISIBLE);
                } else {
                    recyclerView_cancelled.setVisibility(View.GONE);
                }
            }
        });
        breakfast_resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView_cancelled2.getVisibility() == View.GONE) {
                    recyclerView_cancelled2.setVisibility(View.VISIBLE);
                } else {
                    recyclerView_cancelled2.setVisibility(View.GONE);
                }
            }
        });

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

                //ItemLoaderDialog.dismiss();

            }



        });

        // initSwipe();
        return  rootView;
    }


    private void getcancelledBreakfastOrders() {

        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders")
                .orderByChild("date").equalTo(date);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allcancelled.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);
                    if (allfoodorders.getStatus().equals("cancelled")){
                        allcancelled.add(allfoodorders);
                    }

                }
                Collections.reverse(allcancelled);
                adaptercancelled.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }

        });

    }
    private void getcancelledLunchOrders() {
//        ItemLoaderDialog = new ProgressDialog(getContext());
//
//        ItemLoaderDialog.setMessage("Loading Prepared Lunch Orders");
//
//        ItemLoaderDialog.show();

        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders")
                .orderByChild("date").equalTo(date);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allcancelled2.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);
                    if (allfoodorders.getStatus().equals("cancelled")){
                        allcancelled2.add(allfoodorders);
                    }

                }
                Collections.reverse(allcancelled2);
                adaptercancelled.notifyDataSetChanged();

                //ItemLoaderDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //ItemLoaderDialog.dismiss();

            }

        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
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
                    for (int i = 0; i< allcancelled2.size(); i++){
                        //Log.e("idk",allorderslist.get(i).getOrdertitle().toLowerCase());
                        ArrayList<String> orderstuff = allcancelled2.get(i).getOrdertitle();
                        String listString = "";
                        for (String s : orderstuff)
                        {
                            listString += s + "\t";
                        }
                        //Todo address this by uncommenting
                        if (allcancelled2.get(i).getUsername().toLowerCase().contains(userInput)|| listString.toLowerCase().contains(userInput))
                        {

                            newList.add(allcancelled2.get(i));
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

                    adaptercancelled2.updateList(newList);
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