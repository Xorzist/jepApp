package com.example.jepapp.Fragments.Admin;

import androidx.fragment.app.Fragment;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Adapters.Admin.AllPreparedAdapter;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class PreparedOrders extends Fragment   {
    private List<com.example.jepapp.Models.Orders> allpreparedBF, allpreparedlunch;
    private RecyclerView recyclerView_preparedBF, recyclerView_preparedlunch;
    private AllPreparedAdapter adapterprepared, adapterlunch;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.admin_fragment_order_, container, false);
        recyclerView_preparedBF =  rootView.findViewById(R.id.ordersbreakfastlist);
        recyclerView_preparedlunch = rootView.findViewById(R.id.orderslunchlist);
        allpreparedBF = new ArrayList<>();
        allpreparedlunch = new ArrayList<>();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(calendar.getTime());
        FloatingActionButton lunch_resize = rootView.findViewById(R.id.lunch_resize);
        FloatingActionButton breakfast_resize = rootView.findViewById(R.id.breakfast_resize);
        adapterprepared = new AllPreparedAdapter(getContext(), allpreparedBF);
        adapterlunch = new AllPreparedAdapter(getContext(), allpreparedlunch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView_preparedBF.setLayoutManager(linearLayoutManager);
        recyclerView_preparedlunch.setLayoutManager(linearLayoutManager2);
        recyclerView_preparedBF.setAdapter(adapterprepared);
        recyclerView_preparedlunch.setAdapter(adapterlunch);
        setHasOptionsMenu(true);

        //hides the lunch recycler view
        lunch_resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView_preparedlunch.getVisibility() == View.GONE) {
                    recyclerView_preparedlunch.setVisibility(View.VISIBLE);
                } else {
                    recyclerView_preparedlunch.setVisibility(View.GONE);
                }
            }
        });
        //hides the breakfast recycler view
        breakfast_resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView_preparedBF.getVisibility() == View.GONE) {
                    recyclerView_preparedBF.setVisibility(View.VISIBLE);
                } else {
                    recyclerView_preparedBF.setVisibility(View.GONE);
                }
            }
        });
        getpreparedBreakfastOrders();
        getpreparedLunchOrders();
        return  rootView;
    }


    //gets all the breakfast orders with the status prepared
    private void getpreparedBreakfastOrders() {
        final ProgressDialog bfDialog = new ProgressDialog(getContext());
        bfDialog.setMessage("Getting Prepared Breakfast Orders");
        bfDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders")
                .orderByChild("date").equalTo(date);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allpreparedBF.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);
                    if (allfoodorders.getStatus().toLowerCase().equals("prepared")){
                        allpreparedBF.add(allfoodorders);
                    }


                }
                //hows the most recent orders first
                Collections.reverse(allpreparedBF);

                adapterprepared.notifyDataSetChanged();

                bfDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                bfDialog.cancel();

            }

        });

    }
    //gets all lunch orders with the status prepared
    private void getpreparedLunchOrders() {
        final ProgressDialog lunchProgress = new ProgressDialog(getContext());
        lunchProgress.setMessage("Getting Prepared Lunch Orders");
        lunchProgress.show();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders")
                .orderByChild("date").equalTo(date);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allpreparedlunch.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);
                    if (allfoodorders.getStatus().toLowerCase().equals("prepared")) {
                        allpreparedlunch.add(allfoodorders);
                    }
                }
                //hows the most recent orders first
               Collections.reverse(allpreparedlunch);
                adapterlunch.notifyDataSetChanged();

                lunchProgress.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                lunchProgress.cancel();

            }

        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       //deletes any existing menu
        menu.clear();
        //inflates new menu
        inflater.inflate(R.menu.main_menu, menu);
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
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
                    List<com.example.jepapp.Models.Orders> newList = new ArrayList<>();
                    List<com.example.jepapp.Models.Orders> newListlunch = new ArrayList<>();
                    getActivity().onSearchRequested();
                    for (int i = 0; i< allpreparedBF.size(); i++){
                        ArrayList<String> orderstuff = allpreparedBF.get(i).getOrdertitle();
                        String listString = "";
                        for (String s : orderstuff)
                        {
                            listString += s + "\t";
                        }
                        if (allpreparedBF.get(i).getUsername().toLowerCase().contains(userInput)|| listString.toLowerCase().contains(userInput))
                        {

                            newList.add(allpreparedBF.get(i));
                        }

                    }    for (int i = 0; i< allpreparedlunch.size(); i++){
                        ArrayList<String> orderstufflunch = allpreparedlunch.get(i).getOrdertitle();
                        String listStringLunch = "";
                        for (String s : orderstufflunch)
                        {
                            listStringLunch += s + "\t";
                        }
                        if (allpreparedlunch.get(i).getUsername().toLowerCase().contains(userInput)|| listStringLunch.toLowerCase().contains(userInput))
                        {

                            newListlunch.add(allpreparedlunch.get(i));
                        }

                    }

                    adapterprepared.updateList(newList);
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



}