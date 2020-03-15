package com.example.jepapp.Fragments.HR;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Page2 extends Fragment {
    private RecyclerView hrrecyclerView;
    HRAdapterRequests adapter;
    private List<Requests> requestlist;
    private List<UserCredentials> userlist;
    private Button update_all;
    LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    ProgressDialog progressDialog;
    private TextView emptyView;
    DatabaseReference databaseReference;



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

      //  getUserData();
       // getRequestData();

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
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Requests");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                requestlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Requests allrequests = dataSnapshot.getValue(Requests.class);

                    requestlist.add(allrequests);


                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }
    private void getUserData() {

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials allrequests = dataSnapshot.getValue(UserCredentials.class);

                    userlist.add(allrequests);


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
