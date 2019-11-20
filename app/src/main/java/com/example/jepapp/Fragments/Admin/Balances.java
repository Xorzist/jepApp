package com.example.jepapp.Fragments.Admin;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.jepapp.Adapters.Admin.AllOrdersAdapter;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.example.jepapp.SwipeController;
import com.example.jepapp.SwipeControllerActions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Balances extends Fragment {


    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<Orders> balanceList = new ArrayList<>();

    RecyclerView recyclerView;

    FloatingActionButton fabcreatebtn;

    private RequestQueue mRequestq;
    private Bitmap bitmap;
    private static CreateItem createiteminstance;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    SwipeController swipeControl = null;
    DatabaseReference myDBRef;
    public AllOrdersAdapter adapter ;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.all_imenu_items, container, false);
        recyclerView = rootView.findViewById(R.id.allmenuitems);
        balanceList = new ArrayList<>();
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        adapter = new AllOrdersAdapter(getContext(), balanceList);
        swipeControl = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure this order is completed?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                DatabaseReference dbref = myDBRef.child("BreakfastMenu");
//                                String title = balanceList.get(position).getOrdertitle();
//                                String quantity = balanceList.get(position).getQuantity();
//                                String cost = balanceList.get(position).getCost();
//                                String orderid = balanceList.get(position).getOrderID();
//
//                                Orders balancedueorders = new Orders(orderid, title, quantity, cost);
//                                String key = myDBRef.child("BreakfastMenu").push().getKey();
//                                myDBRef.child("BreakfastMenu")
//                                        .child(key)
//                                        .setValue(balancedueorders);
//                                Log.d("Start Adding", "START!");
//
//                                adapter.notifyItemRemoved(position);
//                                adapter.notifyItemRangeChanged(position,adapter.getItemCount());
//                                Toast toast = Toast.makeText(getContext(),
//                                        "Item has been moved",
//                                        Toast.LENGTH_SHORT);
//                                toast.show();
//                                Log.e("LOL","Hush" );
//
//                                dialog.cancel();
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
                swipeControl.onDraw(c);
            }
        });
        recyclerView.setAdapter(adapter);


        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Balances");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                balanceList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders orderbalances = dataSnapshot.getValue(Orders.class);



                    balanceList.add(orderbalances);
                }
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });



        return  rootView;

    }

//    public void deleteItem(Orders balance){
//        databaseReference.child(Orders.getKey()).removeValue();
//
//    }

}
