package com.example.jepapp.Fragments.Admin;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Adapters.Admin.AllOrdersAdapter;
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

public class Orders extends Fragment {
    private List<com.example.jepapp.Models.Orders> allordersbreakfast, allorderslunch;
    private RecyclerView recyclerView_breakfast, recyclerView_lunch;
    public AllOrdersAdapter adapterbreakfast, adapterlunch;
    private FloatingActionButton lunch_resize, breakfast_resize;
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
        recyclerView_breakfast =  rootView.findViewById(R.id.ordersbreakfastlist);
        recyclerView_lunch = rootView.findViewById(R.id.orderslunchlist);
        allordersbreakfast = new ArrayList<>();
        allorderslunch = new ArrayList<>();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(calendar.getTime());
        lunch_resize = rootView.findViewById(R.id.lunch_resize);
        breakfast_resize = rootView.findViewById(R.id.breakfast_resize);
        adapterbreakfast = new AllOrdersAdapter(getContext(),allordersbreakfast);
        adapterlunch = new AllOrdersAdapter(getContext(), allorderslunch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView_breakfast.setLayoutManager(linearLayoutManager);
        recyclerView_lunch.setLayoutManager(linearLayoutManager2);
        recyclerView_breakfast.setAdapter(adapterbreakfast);
        recyclerView_lunch.setAdapter(adapterlunch);

        setHasOptionsMenu(true);
       //hides the lunch recycler view
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
        //hides the lunch recycler view
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
        //gets data from firebase
        getBreakfastOrders();
        getLunchOrders();

        return  rootView;
    }
    //gets breakfast data with the status of incomplete from firebase
    private void getBreakfastOrders() {

        final ProgressDialog breakfastProgress = new ProgressDialog(getContext());
        breakfastProgress.setMessage("Getting Breakfast Orders");
        breakfastProgress.show();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders")
                .orderByChild("date").equalTo(date);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allordersbreakfast.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);
                    if (allfoodorders.getStatus().toLowerCase().equals("incomplete")){
                        allordersbreakfast.add(allfoodorders);
                    }
                }
                //shows the most recent order first
                Collections.sort(allordersbreakfast);
                //Collections.reverse(allordersbreakfast);
                adapterbreakfast.notifyDataSetChanged();
                breakfastProgress.cancel();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                breakfastProgress.cancel();

                }

        });

    }
    //gets lunch data with the status of incomplete from firebase
    private void getLunchOrders() {
        final ProgressDialog lunchProgress = new ProgressDialog(getContext());
        lunchProgress.setMessage("Getting Lunch Orders");
        lunchProgress.show();

        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders")
                .orderByChild("date").equalTo(date);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allorderslunch.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = snapshot.getValue(com.example.jepapp.Models.Orders.class);
                    if (allfoodorders.getStatus().toLowerCase().equals("incomplete")) {
                        allorderslunch.add(allfoodorders);
                    }
                }
                //shows the most recent order first
                Collections.sort(allorderslunch);
               // Collections.reverse(allorderslunch);
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
        //clears any existing menu
        menu.clear();
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

                    Log.d("Query", newText);
                    String userInput = newText.toLowerCase();
                    List<com.example.jepapp.Models.Orders> newList = new ArrayList<>();
                    List<com.example.jepapp.Models.Orders> newListlunch = new ArrayList<>();
                        getActivity().onSearchRequested();
                        for (int i = 0; i< allordersbreakfast.size(); i++){
                            ArrayList<String> orderstuff = allordersbreakfast.get(i).getOrdertitle();
                            String listString = "";
                            for (String s : orderstuff)
                            {
                                listString += s + "\t";
                            }
                            if (allordersbreakfast.get(i).getUsername().toLowerCase().contains(userInput)|| listString.toLowerCase().contains(userInput))
                            {

                                newList.add(allordersbreakfast.get(i));
                            }


                    }    for (int i = 0; i< allorderslunch.size(); i++){
                        ArrayList<String> orderstufflunch = allorderslunch.get(i).getOrdertitle();
                        String listStringLunch = "";
                        for (String s : orderstufflunch)
                        {
                            listStringLunch += s + "\t";
                        }
                        if (allorderslunch.get(i).getUsername().toLowerCase().contains(userInput)|| listStringLunch.toLowerCase().contains(userInput))
                        {

                            newListlunch.add(allorderslunch.get(i));
                        }


                    }
                        //updates adapters
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
        if (item.getItemId() == R.id.action_search) {
            return false;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }


}