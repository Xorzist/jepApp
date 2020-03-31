package com.example.jepapp.Fragments.Admin;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.jepapp.Adapters.Admin.AllOrdersAdapter;
import com.example.jepapp.R;
import com.example.jepapp.SwipeController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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

public class Orders extends Fragment   {
    List<com.example.jepapp.Models.Orders> allordersbreakfast, allorderslunch, allorderscancelled;
    RecyclerView recyclerView_breakfast, recyclerView_lunch, recyclerView_cancelled;
    ProgressBar progressBar;
    DatabaseReference databaseReferencebreakfast, databaseReferencelunch, myDBref;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2, linearLayoutManager3;
    private View view;
    private FirebaseAuth mAuth;
    public AllOrdersAdapter adapterbreakfast, adapterlunch, adaptercancelled;
    private Paint p = new Paint();
    private FloatingActionButton lunch_resize, breakfast_resize, cancelled_resize;
    SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    SwipeRefreshLayout rswipeRefreshLayoutbreakfast, rswipeRefreshLayoutlunch;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.admin_fragment_order_, container, false);
        recyclerView_breakfast = (RecyclerView) rootView.findViewById(R.id.ordersbreakfastlist);
        //recyclerView_cancelled = rootView.findViewById(R.id.orderscancelledlist);
        recyclerView_lunch = rootView.findViewById(R.id.orderslunchlist);
        allordersbreakfast = new ArrayList<>();
        allorderscancelled = new ArrayList<>();
        allorderslunch = new ArrayList<>();
        lunch_resize = rootView.findViewById(R.id.lunch_resize);
        progressBar = rootView.findViewById(R.id.myOrdersProgress);
        breakfast_resize = rootView.findViewById(R.id.breakfast_resize);
        //cancelled_resize = rootView.findViewById(R.id.cancelled_resize);
        adapterbreakfast = new AllOrdersAdapter(getContext(),allordersbreakfast);
        adapterlunch = new AllOrdersAdapter(getContext(), allorderslunch);
        adaptercancelled = new AllOrdersAdapter(getContext(), allorderscancelled);
        myDBref = FirebaseDatabase.getInstance().getReference("JEP");
        linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_breakfast.getContext(), linearLayoutManager.getOrientation());
        linearLayoutManager2 = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(recyclerView_lunch.getContext(), linearLayoutManager2.getOrientation());
        linearLayoutManager3 = new LinearLayoutManager(getContext());
//        DividerItemDecoration dividerItemDecoration3 = new DividerItemDecoration(recyclerView_cancelled.getContext(), linearLayoutManager3.getOrientation());
        recyclerView_breakfast.setLayoutManager(linearLayoutManager);
        recyclerView_lunch.setLayoutManager(linearLayoutManager2);
       // recyclerView_cancelled.setLayoutManager(linearLayoutManager3);
        recyclerView_breakfast.setAdapter(adapterbreakfast);
        recyclerView_lunch.setAdapter(adapterlunch);
        //recyclerView_cancelled.setAdapter(adaptercancelled);
        setHasOptionsMenu(true);
//        cancelled_resize.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (recyclerView_cancelled.getVisibility() == View.GONE) {
//                    recyclerView_cancelled.setVisibility(View.VISIBLE);
//                }
//                recyclerView_cancelled.setVisibility(View.GONE);
//            }
//        });
        lunch_resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView_lunch.getVisibility() == View.GONE) {
                    recyclerView_lunch.setVisibility(View.VISIBLE);
                } else {
                    recyclerView_lunch.setVisibility(View.GONE);
                }
            }
        });
        breakfast_resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView_breakfast.getVisibility() == View.GONE) {
                    recyclerView_breakfast.setVisibility(View.VISIBLE);
                } else {
                    recyclerView_breakfast.setVisibility(View.GONE);
                }
            }
        });
        //initializing the productlist

//        menuItem = menu.getItem(0)

//


        mAuth = FirebaseAuth.getInstance();
        getBreakfastOrders();
        getLunchOrders();
      //  getCancelledOrders();



       // initSwipe();
        return  rootView;
    }

    private void getBreakfastOrders() {
        final ProgressDialog progressDialog1 = new ProgressDialog(getContext());
        progressDialog1.setMessage("Getting Breakfast Orders");
        progressDialog1.show();

//        progressDialog = new ProgressDialog(getContext());
//
//        progressDialog.setMessage("Loading Breakfast Orders");
//
//        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders")
                    .orderByChild("status").equalTo("Incomplete");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allordersbreakfast.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allordersbreakfast.add(allfoodorders);

                }
                Collections.reverse(allordersbreakfast);
                adapterbreakfast.notifyDataSetChanged();
                progressDialog1.cancel();


                //progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //progressDialog.dismiss();
                progressDialog1.cancel();

                }

        });

    }
//    private void getCancelledOrders() {
//        final ProgressDialog progressDialog2 = new ProgressDialog(getContext());
//        progressDialog2.setMessage("Getting Cancelled Orders");
//        progressDialog2.show();
//        Query query, query1;
//        query = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders")
//                .orderByChild("date").equalTo(date);
//        query1 = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders")
//                .orderByChild("date").equalTo(date);
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                allorderscancelled.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//
//                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);
//                    if (allfoodorders.getStatus().equals("cancelled")) {
//                        allorderscancelled.add(allfoodorders);
//                    }
//                }
//               // Collections.reverse(allordersbreakfast);
//                adaptercancelled.notifyDataSetChanged();
//                progressDialog2.cancel();
//
//                //progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //progressDialog.dismiss();
//                progressDialog2.cancel();
//
//            }
//
//        });
//        query1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()){
//                    com.example.jepapp.Models.Orders allcancelledorders = data.getValue(com.example.jepapp.Models.Orders.class);
//                    if (allcancelledorders.getStatus().equals("cancelled")){
//                        allorderscancelled.add(allcancelledorders);
//                    }
//
//                }
//                adaptercancelled.notifyDataSetChanged();
//                //progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //progressDialog.dismiss();
//            }
//        });
//
////        for (int i = 0; i < allorderscancelled.size(); i++) {
////
////        }
//
//    }
    private void getLunchOrders() {
        final ProgressDialog progressDialog3 = new ProgressDialog(getContext());
        progressDialog3.setMessage("Getting Lunch Orders");
        progressDialog3.show();

        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders")
                .orderByChild("status").equalTo("Incomplete");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allorderslunch.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allorderslunch.add(allfoodorders);

                }
                Collections.reverse(allorderslunch);
                adapterlunch.notifyDataSetChanged();
                progressDialog3.cancel();

                //progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog3.cancel();
                //progressDialog.dismiss();

            }

        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        final List<com.example.jepapp.Models.Orders> combinedlist = new ArrayList<>();
        combinedlist.addAll(allordersbreakfast);
        combinedlist.addAll(allorderslunch);
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
                        for (int i = 0; i< allordersbreakfast.size(); i++){
                            //Log.e("idk",allorderslist.get(i).getOrdertitle().toLowerCase());
                            ArrayList<String> orderstuff = allordersbreakfast.get(i).getOrdertitle();
                            String listString = "";
                            for (String s : orderstuff)
                            {
                                listString += s + "\t";
                            }
                            //Todo address this by uncommenting
                            if (allordersbreakfast.get(i).getUsername().toLowerCase().contains(userInput)|| listString.toLowerCase().contains(userInput))
                            {

                                newList.add(allordersbreakfast.get(i));
                                //Log.e("Eror", newList.get(0).getOrdertitle());
                            }


                       // }

                    }    for (int i = 0; i< allorderslunch.size(); i++){
                        //Log.e("idk",allorderslist.get(i).getOrdertitle().toLowerCase());
                        ArrayList<String> orderstufflunch = allorderslunch.get(i).getOrdertitle();
                        String listStringLunch = "";
                        for (String s : orderstufflunch)
                        {
                            listStringLunch += s + "\t";
                        }
                        //Todo address this by uncommenting
                        if (allorderslunch.get(i).getUsername().toLowerCase().contains(userInput)|| listStringLunch.toLowerCase().contains(userInput))
                        {

                            newListlunch.add(allorderslunch.get(i));
                            //Log.e("Eror", newList.get(0).getOrdertitle());
                        }


                        // }

                    }

                        adapterbreakfast.updateList(newList);
                        adapterlunch.updateList(newListlunch);

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

    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

    }
    public void deleteItem(com.example.jepapp.Models.Orders remove){
        //Todo address this by uncommenting
        // databaseReference.child(remove.getKey()).removeValue();
        // Log.e("Keytime", remove.getKey());

    }



}