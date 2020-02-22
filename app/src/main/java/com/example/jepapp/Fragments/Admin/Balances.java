package com.example.jepapp.Fragments.Admin;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.jepapp.Adapters.Admin.AllOrdersAdapter;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.example.jepapp.SwipeController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Balances extends Fragment {


    DatabaseReference databaseReference, databaseReferenceforuser;

    ProgressDialog progressDialog;

    List<Orders> balanceList = new ArrayList<>();
    List<UserCredentials> userList = new ArrayList<>();

    RecyclerView recyclerView;

    FloatingActionButton fab_search;

    private RequestQueue mRequestq;
    private Bitmap bitmap;
    private static CreateItem createiteminstance;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    SwipeController swipeControl = null;
    DatabaseReference myDBRef;
    public AllOrdersAdapter adapter ;
    SearchView searchView = null;
    private Integer funds, balance, userbalance, price_of_order;
    private String userkey;
    private SearchView.OnQueryTextListener queryTextListener;
    private Paint p = new Paint();


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_makean_order, container, false);
        recyclerView = rootView.findViewById(R.id.myOrdersRecyclerView);
        balanceList = new ArrayList<>();
        myDBRef = FirebaseDatabase.getInstance().getReference("JEP").child("Balances");
        adapter = new AllOrdersAdapter(getContext(), balanceList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);
        fab_search = rootView.findViewById(R.id.search_fab);

        fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        //Hides Search fab temporarily
        fab_search.hide();

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Balances from Firebase Database");

        progressDialog.show();

        databaseReferenceforuser = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

        databaseReferenceforuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials userinfo = dataSnapshot.getValue(UserCredentials.class);


                    userList.add(userinfo);
                }}
            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }



        });


        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Balances");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                balanceList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Orders orderbalances = dataSnapshot.getValue(Orders.class);



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


        initSwipe();
        return  rootView;

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        getActivity().invalidateOptionsMenu();
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
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
                    List<com.example.jepapp.Models.Orders> newList = new ArrayList<>();
                    getActivity().onSearchRequested();

                    for (int i = 0; i< balanceList.size(); i++){

                        if (balanceList.get(i).getOrdertitle().toLowerCase().contains(userInput)|| balanceList.get(i).getUsername().toLowerCase().contains(userInput)) {

                            newList.add(balanceList.get(i));
                            Log.e("Eror", newList.get(0).getOrdertitle());
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


    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT){
                    //paid

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Are you sure this order is paid for?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (balanceList.get(position).getPayment_type().equals("Cash")) {
                                        deleteItem(balanceList.get(position));
                                        adapter.notifyItemRemoved(position);
                                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                        //  adapter.removeItem(position);
                                        Toast toast = Toast.makeText(getContext(),
                                                "Item has been deleted",
                                                Toast.LENGTH_SHORT);
                                        toast.show();
                                        dialog.cancel();
                                        // adapter.notifyItemChanged(position);
                                    }
                                    if (balanceList.get(position).getPaidby().toString().equals("me")) {

                                        for (int i = 0; i < userList.size(); i++) {
                                            if (userList.get(i).getUsername().equals(balanceList.get(position).getUsername())) {
                                                funds = Integer.parseInt(userList.get(i).getBalance());
                                                userkey = userList.get(i).getKey();
                                                price_of_order = Integer.parseInt(balanceList.get(position).getCost().trim().toString());

                                                Log.e("Funds", funds.toString());
                                                // Log.e("Price", price_of_order.toString());
                                            }
                                        }
                                        if (make_payment(price_of_order, funds)){
                                            deleteItem(balanceList.get(position));
                                            adapter.notifyItemRemoved(position);
                                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                            //  adapter.removeItem(position);
                                            Toast toast = Toast.makeText(getContext(),
                                                    "Item has been deleted",
                                                    Toast.LENGTH_SHORT);
                                            toast.show();
                                            dialog.cancel();
                                        } else{
                                            Toast toast = Toast.makeText(getContext(),"User's balance is insufficient", Toast.LENGTH_LONG);
                                            toast.show();
                                            dialog.cancel();
                                        }
                                    }
                                    else {
                                        for (int i=0; i<userList.size(); i++){
                                            if (userList.get(i).getUsername().equals(balanceList.get(position).getPaidby())){
                                                funds = Integer.parseInt(userList.get(i).getBalance());
                                                userkey = userList.get(i).getKey();
                                                price_of_order = Integer.parseInt(balanceList.get(position).getCost().trim().toString());

                                                Log.e("Funds", funds.toString());
                                                // Log.e("Price", price_of_order.toString());
                                            }
                                        }
                                        if (make_payment(price_of_order,funds)){
                                            deleteItem(balanceList.get(position));
                                            adapter.notifyItemRemoved(position);
                                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                            //  adapter.removeItem(position);
                                            Toast toast = Toast.makeText(getContext(),
                                                    "Item has been deleted",
                                                    Toast.LENGTH_SHORT);
                                            toast.show();
                                            dialog.cancel();
                                        }
                                        else{
                                            Toast toast = Toast.makeText(getContext(),"User's balance is insufficient", Toast.LENGTH_LONG);
                                            toast.show();
                                            dialog.cancel();

                                        }
                                    }
                                }
                            });
                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    adapter.notifyItemChanged(position);
                                    // removeView();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3c"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.paid);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
//                    else {
//                        p.setColor(Color.parseColor("#388E3c"));
//                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background,p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.paid);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
//                        c.drawBitmap(icon,null,icon_dest,p);
//                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private boolean make_payment(Integer price_of_order, Integer funds) {
        if (price_of_order <= funds){
//                                               userbalance = Integer.parseInt(balanceList.get(position).getCost().trim());
            balance = funds - price_of_order;
            DatabaseReference subtractor = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
            Query subtract_funds = subtractor.orderByChild("key").equalTo(userkey);
            subtract_funds.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot updatebalance: dataSnapshot.getChildren()){
                        updatebalance.getRef().child("balance").setValue(balance.toString());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } return true;

    }

    public void deleteItem(com.example.jepapp.Models.Orders remove){
        databaseReference.child(remove.getKey()).removeValue();
        Log.e( "deleteItem: ",remove.getKey() );

    }


}
