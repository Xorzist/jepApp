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

import com.example.jepapp.Adapters.AllReviewsAdapter;
import com.example.jepapp.Models.Comments;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Reviews extends Fragment {
    List<Comments> commentsList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private FloatingActionButton search_fab;
    SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    public AllReviewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_makean_order, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.myOrdersRecyclerView);
        //commentsList = new ArrayList<>();
        commentsList = new ArrayList<>();

        adapter = new AllReviewsAdapter(getContext(), commentsList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(getContext());
        //initializing the reviews list
        progressDialog.setMessage("Loading Comments now");
        progressDialog.show();
        setHasOptionsMenu(true);
        search_fab = rootView.findViewById(R.id.search_fab);

        search_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Comments");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                commentsList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    Comments allreviews = dataSnapshot.getValue(Comments.class);

                    commentsList.add(allreviews);

                }

              //  Collections.reverse(commentsList);
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }@Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

        return  rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        getActivity().invalidateOptionsMenu();
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
                    List<com.example.jepapp.Models.Comments> newcommentList = new ArrayList<>();

                    // for (com.example.jepapp.Models.Orders orders : allorderslist) {

                   // if (!searchView.isIconified()) {
                        getActivity().onSearchRequested();
                        //  com.example.jepapp.Models.Orders orders;
                        for (int i = 0; i< commentsList.size(); i++){

                            if (commentsList.get(i).getTitle().toLowerCase().contains(userInput)|| commentsList.get(i).getComment().toLowerCase().contains(userInput)) {

                                newcommentList.add(commentsList.get(i));
                            }

                        //}

                    }
                    adapter.updateList(newcommentList);
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

