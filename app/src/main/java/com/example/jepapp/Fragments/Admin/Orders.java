package com.example.jepapp.Fragments.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Admin.AllOrdersAdapter;
import com.example.jepapp.R;
import com.example.jepapp.SwipeController;
import com.example.jepapp.SwipeControllerActions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Orders extends Fragment {
    List<com.example.jepapp.Models.Orders> allorderslist= new ArrayList<>();
    //the recyclerview
    RecyclerView recyclerView;
   // RecyclerView.Adapter adapter;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private FirebaseAuth mAuth;
    SwipeController swipeControl = null;
    public AllOrdersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_makean_order, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.myOrdersRecyclerView);
        allorderslist = new ArrayList<>();
        adapter = new AllOrdersAdapter(getContext(),allorderslist);

        swipeControl = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you this order is complete and paid?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                deleteItem(list.get(position));
//                                adapter.notifyItemRemoved(position);
//                                adapter.notifyItemRangeChanged(position,adapter.getItemCount());
//                                Toast toast = Toast.makeText(getContext(),
//                                        "Item has been deleted",
//                                        Toast.LENGTH_SHORT);
//                                toast.show();
                                Log.e("LOL","Hush" );

                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeControl);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
               // swipeControl.onDraw(c);
                swipeControl.onDrawOrderpage(c);
            }
        });
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
                allorderslist.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Orders allfoodorders = dataSnapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allorderslist.add(allfoodorders);

                    // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
                }

//                adapter = new SelectMenuItemsAdaptertest(SelectMenuItems.this, list);
//
//                recyclerView.setAdapter(adapter);
                Collections.reverse(allorderslist);
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

