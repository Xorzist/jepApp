package com.example.jepapp.Fragments.User;

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
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MakeanOrder extends Fragment {
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<Orders> myOrderslist =new ArrayList<>();

    RecyclerView.Adapter adapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_makean_order, container, false);
        recyclerView = rootView.findViewById(R.id.myOrdersRecyclerView);
        myOrderslist = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new MyOrdersAdapter(getContext(),myOrderslist);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Orders");
        mAuth = FirebaseAuth.getInstance();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("Orders")
                .orderByChild("orderID").equalTo(mAuth.getUid());


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                myOrderslist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders allmyorders = dataSnapshot.getValue(Orders.class);
                     //Log.e("onDataChange: ", allmyorders.getTitle().toString());

                    myOrderslist.add(allmyorders);

                }
                adapter.notifyDataSetChanged();

//                adapter = new AllitemsAdapter(getContext(), list);
//
//                recyclerView.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });



        return  rootView;


    }

}
