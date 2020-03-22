package com.example.jepapp.Fragments.HR;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.HR.HRAdapterRequests;
import com.example.jepapp.Models.HR.Requests;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Page2 extends Fragment {
    private RecyclerView hrrecyclerView;
    HRAdapterRequests adapter;
    private List<com.example.jepapp.Models.HR.Requests> requestlist;
    private List<UserCredentials> userlist;
    private Button update_all;
    LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    ProgressDialog progressDialog;
    private TextView emptyView;
    DatabaseReference databaseReference;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hr_page2layout, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        //emptyView = rootView.findViewById(R.id.empty_view);
        requestlist = new ArrayList<>();
        userlist = new ArrayList<>();
        update_all = rootView.findViewById(R.id.update_all);
        hrrecyclerView = (RecyclerView) rootView.findViewById(R.id.hr_requests_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(hrrecyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new HRAdapterRequests(getContext(),requestlist,userlist);
        hrrecyclerView.setLayoutManager(linearLayoutManager);
        hrrecyclerView.addItemDecoration(dividerItemDecoration);
        hrrecyclerView.setAdapter(adapter);
        getUserData();
        getRequestData();
        setHasOptionsMenu(true);
        update_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return  rootView;
    }


    private void getRequestData() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Users");

        progressDialog.show();
        // checking for pending requests and adding them to a list to be attached to the adapter
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("Requests")
                .orderByChild("status").equalTo("pending");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                requestlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Requests pendingrequests = dataSnapshot.getValue(Requests.class);
                    requestlist.add(pendingrequests);

                }

                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });



//        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Requests");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                requestlist.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    com.example.jepapp.Models.HR.Requests allrequests = dataSnapshot.getValue(com.example.jepapp.Models.HR.Requests.class);
//
//                    requestlist.add(allrequests);
//
//
//                }
//                adapter.notifyDataSetChanged();
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                progressDialog.dismiss();
//            }
//        });
//
   }
    private void getUserData() {

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);

                    userlist.add(allusers);


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.user, menu);
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
//
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
                    List<Requests> newList = new ArrayList<>();

                    // for (com.example.jepapp.Models.Orders orders : allorderslist) {

                    //if (!searchView.isIconified()) {
                    getActivity().onSearchRequested();
                    //  com.example.jepapp.Models.Orders orders;
                    for (int i = 0; i< requestlist.size(); i++){

                        if (requestlist.get(i).getUsername().toLowerCase().contains(userInput)|| requestlist.get(i).getdate().toLowerCase().contains(userInput)) {

                            newList.add(requestlist.get(i));

                        }

                        // }

                    }
                    adapter.updateList(newList);
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

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_search:
//
//                return false;
//            default:
//                break;
//
//        }
//        searchView.setOnQueryTextListener(queryTextListener);
//        return super.onOptionsItemSelected(item);
//    }



}
