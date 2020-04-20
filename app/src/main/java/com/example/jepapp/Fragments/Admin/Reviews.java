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

import com.example.jepapp.Adapters.AllReviewsAdapter;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reviews extends Fragment {
    List<com.example.jepapp.Models.Reviews> reviewssList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressDialog progressDialogReviews;
    DatabaseReference databaseReference;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    //private FloatingActionButton search_fab;
    SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    public AllReviewsAdapter adapter;

    private Menu menu;
    private MenuInflater inflater;
    private SwipeRefreshLayout rswipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.reviews_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.reviewsrecycler);
        reviewssList = new ArrayList<>();
        setupSwipeRefresh(rootView);
        adapter = new AllReviewsAdapter(getContext(), reviewssList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
       // progressDialogReviews = rootView.findViewById(R.id.reviewsprogressor);
        progressDialogReviews = new ProgressDialog(getContext());
        //initializing the reviews list
        progressDialogReviews.setMessage("Loading Comments now");
        progressDialogReviews.show();
        setHasOptionsMenu(true);
       // search_fab = rootView.findViewById(R.id.search_fab);
        //Hides Search fab temporarily
//        search_fab.hide();
//
//        search_fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.setIconified(false);
//            }
//        });
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Reviews");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                reviewssList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    com.example.jepapp.Models.Reviews allreviews = dataSnapshot.getValue(com.example.jepapp.Models.Reviews.class);

                    if (!allreviews.getTitle().equals("none")){
                        reviewssList.add(allreviews);
                    }
                }

                Collections.reverse(reviewssList);
                adapter.notifyDataSetChanged();

                progressDialogReviews.dismiss();
            }@Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialogReviews.dismiss();

            }
        });

        return  rootView;
    }
//
    private void setupSwipeRefresh(View View) {
        rswipeRefreshLayout = View.findViewById(R.id.swiperefresh);
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
//
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        this.menu = menu;
        this.inflater = inflater;
        menu.clear();
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        //getActivity().invalidateOptionsMenu();Removed because of scrolling toolbar animation
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
                    List<com.example.jepapp.Models.Reviews> newreviewList = new ArrayList<>();

                    // for (com.example.jepapp.Models.Orders orders : allorderslist) {

                   // if (!searchView.isIconified()) {
                        getActivity().onSearchRequested();
                        //  com.example.jepapp.Models.Orders orders;
                        for (int i = 0; i< reviewssList.size(); i++){

                            if (reviewssList.get(i).getReviewtopic().toLowerCase().contains(userInput)|| reviewssList.get(i).getDescription().toLowerCase().contains(userInput) || reviewssList.get(i).getTitle().toLowerCase().contains(userInput)) {

                                newreviewList.add(reviewssList.get(i));
                            }

                    }
                    adapter.updateList(newreviewList);
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

                return true;
            default:
                break;

        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
    }
