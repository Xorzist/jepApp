package com.example.jepapp.Fragments.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Users.MyOrdersAdapter;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Orders extends Fragment {
    List<com.example.jepapp.Models.Orders> allorderslist= new ArrayList<>();
    //the recyclerview
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_makean_order, container, false);

        //  return rootView;
        //getting the recyclerview from xml
        recyclerView = (RecyclerView) rootView.findViewById(R.id.myOrdersRecyclerView);
        recyclerView.setHasFixedSize(true);
        allorderslist = new ArrayList<>();
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new MyOrdersAdapter(getContext(),allorderslist);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(getContext());
        //initializing the productlist
        progressDialog.setMessage("Loading Data now");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Orders");
        mAuth = FirebaseAuth.getInstance();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Orders allfoodorders = dataSnapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allorderslist.add(allfoodorders);
                    // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
                }

//                adapter = new SelectMenuItemsAdaptertest(SelectMenuItems.this, list);
//
//                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }@Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

        //setting adapter to recyclerview
       // recyclerView.setAdapter(adapter);
        return  rootView;
    }
}

