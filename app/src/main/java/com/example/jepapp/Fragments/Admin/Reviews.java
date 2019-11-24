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

import com.example.jepapp.Adapters.AllReviewsAdapter;
import com.example.jepapp.Models.Comments;
import com.example.jepapp.R;
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

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Reviews");


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
}
