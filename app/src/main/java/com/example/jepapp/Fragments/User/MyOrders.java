package com.example.jepapp.Fragments.User;

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

import com.example.jepapp.Adapters.Users.MyOrdersAdapter;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.example.jepapp.SwipeController;
import com.example.jepapp.SwipeControllerActions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends Fragment {
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    SwipeController swipeControl = null;
    List<Orders> myOrderslist =new ArrayList<>();

   // RecyclerView.Adapter adapter ;
    public MyOrdersAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_makean_order, container, false);
        recyclerView = rootView.findViewById(R.id.myOrdersRecyclerView);
        myOrderslist = new ArrayList<>();
        //linearLayoutManager = new LinearLayoutManager(getContext());
      //  dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new MyOrdersAdapter(getContext(),myOrderslist);
        swipeControl = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you want to cancel this order?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseReference cancelorder = FirebaseDatabase.getInstance().getReference("JEP").child("AllOrders");
                                DatabaseReference cancelorder2 = FirebaseDatabase.getInstance().getReference("JEP").child("Orders");
                                //Log.e("key",cancelkey.getKey().toString());
                                Query ordertocancel = cancelorder.orderByChild("key").equalTo(myOrderslist.get(position).getKey());
                                ordertocancel.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        for(DataSnapshot cancelorder: snapshot.getChildren()){
                                            cancelorder.getRef().child("payment_type").setValue("cancelled");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        throw databaseError.toException();
                                    }
                                });
                                Query ordertocancel_Orders = cancelorder2.orderByChild("key").equalTo(myOrderslist.get(position).getKey());
                                ordertocancel_Orders.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        for(DataSnapshot cancelorder2: snapshot.getChildren()){
                                            cancelorder2.getRef().child("payment_type").setValue("cancelled");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        throw databaseError.toException();
                                    }
                                });


                                //deleteItem(list.get(position));
                                //adapter.notifyItemRemoved(position);
                               // adapter.notifyItemRangeChanged(position,adapter.getItemCount());
//                                Toast toast = Toast.makeText(getContext(),
//                                        "Item has been deleted",
//                                        Toast.LENGTH_SHORT);
//                                toast.show();
                                Log.e("LOL","Hush" );

                                dialog.cancel();
                                adapter.notifyItemChanged(position);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //adapter.notifyItemChanged(position);
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            @Override
            public void onLeftClicked(int position) {
                //editItem(position);
                //adapter.notifyItemChanged(position);
                Log.e("OLC", "Clicked");

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
                swipeControl.onDrawMyOrderpage(c);
            }
        });
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("AllOrders");
        mAuth = FirebaseAuth.getInstance();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("AllOrders")
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
