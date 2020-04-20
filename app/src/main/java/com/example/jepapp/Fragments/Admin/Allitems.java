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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.jepapp.Adapters.Admin.AllitemsAdapter;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;
import java.util.List;

public class Allitems extends Fragment {


    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<MItems> list = new ArrayList<>();

    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    SwipeRefreshLayout rswipeRefreshLayout;
    SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    public AllitemsAdapter adapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating view holder
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.all_imenu_items, container, false);
        recyclerView = rootView.findViewById(R.id.allmenuitems);
        //instantiating variables
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        adapter = new AllitemsAdapter(getContext(), list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        setHasOptionsMenu(true);
        //calling adapterMenuBreakfast
        recyclerView.setAdapter(adapter);
        //calling refresh method to reload the page on swipe down
        setupSwipeRefresh(rootView);


        //creating progress dialog
        progressDialog.setMessage("Loading Inventory Items from Firebase Database");

        progressDialog.show();
        // retrieving menu items from firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MItems inventoryitems = dataSnapshot.getValue(MItems.class);

                    list.add(inventoryitems);
                }
                //update recycler view
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });
// returning viewholder
        return rootView;

    }

    // method to refresh recycler view on down swipe
    private void setupSwipeRefresh(View View) {
        rswipeRefreshLayout = View.findViewById(R.id.swiperefreshallitems);
        rswipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        //Swipe refresh animation
        rswipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                rswipeRefreshLayout.setRefreshing(true);
                //Notifies system that adapterMenuBreakfast has changed which prompts server
                adapter.notifyDataSetChanged();
                rswipeRefreshLayout.setRefreshing(false);

            }
        });
        rswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Notifies system that adapterMenuBreakfast has changed which prompts server
                adapter.notifyDataSetChanged();
                rswipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
            menu.clear();
            //super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.main_menu, menu);
            android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
            //getActivity().invalidateOptionsMenu();Removed because of scrolling toolbar animation
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        searchView.setIconified(false);
            if (searchItem != null) {
                searchView = (SearchView) searchItem.getActionView();
            }
            if (searchView != null) {
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
                        List<com.example.jepapp.Models.MItems> newreviewList = new ArrayList<>();

                        // for (com.example.jepapp.Models.Orders orders : allorderslist) {

                        // if (!searchView.isIconified()) {
                        getActivity().onSearchRequested();
                        //  com.example.jepapp.Models.Orders orders;
                        for (int i = 0; i < list.size(); i++) {

                            if (list.get(i).getTitle().toLowerCase().contains(userInput)){

                                newreviewList.add(list.get(i));
                            }

                        }
                        adapter.updateList(newreviewList);
                        return true;
                    }
                };
                searchView.setOnQueryTextListener(queryTextListener);
            }
            super.onCreateOptionsMenu(menu, inflater);

        }

        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){
            switch (item.getItemId()) {
                case R.id.action_search:

                    return true;
                default:
                    break;

            }
            searchView.setOnQueryTextListener(queryTextListener);
            return super.onOptionsItemSelected(item);
        }
    }

