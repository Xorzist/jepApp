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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.jepapp.Adapters.Admin.AllOrdersAdapter;
import com.example.jepapp.GMailSender;
import com.example.jepapp.R;
import com.example.jepapp.SwipeController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Orders extends Fragment   {
    List<com.example.jepapp.Models.Orders> allorderslist;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference, myDBref;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private FirebaseAuth mAuth;
    SwipeController swipeControl = null;
    private View view;
    public AllOrdersAdapter adapter;
    private Paint p = new Paint();
    private FloatingActionButton search_fab;
    SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    SwipeRefreshLayout rswipeRefreshLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.activity_makean_order, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.myOrdersRecyclerView);
        allorderslist = new ArrayList<>();
        adapter = new AllOrdersAdapter(getContext(),allorderslist);
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
        myDBref = FirebaseDatabase.getInstance().getReference("JEP");
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);
//        searchView = rootView.findViewById(R.id.search_view);
        rswipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        rswipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        //Swipe refresh animation
        rswipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                rswipeRefreshLayout.setRefreshing(true);
                //Notifies system that adapter has changed which prompts server
                adapter.notifyDataSetChanged();
                rswipeRefreshLayout.setRefreshing(false);

            }
        });
        rswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Notifies system that adapter has changed which prompts server
                adapter.notifyDataSetChanged();
                rswipeRefreshLayout.setRefreshing(false);
            }
        });
//        final MenuItem searchItem = menu.findItem(R.id.action_search);
        search_fab = rootView.findViewById(R.id.search_fab);
        //Hides Search fab temporarily
     //   search_fab.hide();
        search_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    GMailSender sender = new GMailSender("_mainaccount@legacydevs.com", "cecile21");
//                    sender.sendMail("This is Subject",
//                            "This is Body",
//                            "_mainaccount@legacydevs.com",
//                            "cdolieth@yahoo.com");
//                    Toast.makeText(getContext(),"mail sent", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Log.e("SendMail", e.getMessage(), e);
//                }
//
//            }
              //  sendEmail();
            }
    });
        progressDialog = new ProgressDialog(getContext());
        //initializing the productlist

//        menuItem = menu.getItem(0);

        progressDialog.setMessage("Loading Comments now");
        progressDialog.show();

//


        mAuth = FirebaseAuth.getInstance();

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot taskNo : dataSnapshot.getChildren()) {
//                    Object firebaseObj = taskNo.getValue(Orders.class);
//                    Object replayObj = taskNo.child("Replay").getValue(MenuItem.class); //class with params set/get methods
//
//                    // ALTERNATIVE
//            /*
//            for (DataSnapshot child : taskNo.getChildren()) {
//                if(child.getKey().equals("Firebase")) {
//                    String address = child.child("Address").getValue(String.class);
//                    String customer = child.child("Customer").getValue(String.class);
//                    // ...
//                } else if (child.getKey().equals("Replay")) {
//                    // replay
//                    // ...
//                }
//            }
//            */
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) { }
//        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allorderslist.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = dataSnapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allorderslist.add(allfoodorders);

                }

                Collections.reverse(allorderslist);
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }@Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

        initSwipe();
        return  rootView;
    }
    private void sendEmail() {
        //Getting content for email

        //Creating SendMail object
       // GMailSender sm = new GMailSender(getContext(), email, message);

        //Executing sendmail to send email
       // sm.execute();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        // searchItem.setVisible(false);
        //getActivity().invalidateOptionsMenu(); Removed because of scrolling toolbar animation
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
                    List<com.example.jepapp.Models.Orders> newList = new ArrayList<>();

                    // for (com.example.jepapp.Models.Orders orders : allorderslist) {

                    //if (!searchView.isIconified()) {
                        getActivity().onSearchRequested();
                        //  com.example.jepapp.Models.Orders orders;
                        for (int i = 0; i< allorderslist.size(); i++){
                            //Log.e("idk",allorderslist.get(i).getOrdertitle().toLowerCase());
                            //Todo address this by uncommenting
                            // if (allorderslist.get(i).getOrdertitle().toLowerCase().contains(userInput)|| allorderslist.get(i).getUsername().toLowerCase().contains(userInput))
                            {

                                newList.add(allorderslist.get(i));
                                //Log.e("Eror", newList.get(0).getOrdertitle());
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
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    //paid

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Are you sure this order is paid for?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteItem(allorderslist.get(position));
                                    adapter.notifyItemRemoved(position);
                                    adapter.notifyItemRangeChanged(position,adapter.getItemCount());
                                    //  adapter.removeItem(position);
                                    Toast toast = Toast.makeText(getContext(),
                                            "Item has been deleted",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                    dialog.cancel();
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

                if (direction == ItemTouchHelper.RIGHT) {
                    //unpaid
                    DatabaseReference dbref = myDBref.child("Balances");
                    //String title = allorderslist.get(position).getOrdertitle();
                    int quantity = allorderslist.get(position).getQuantity();
                    String cost = allorderslist.get(position).getCost();
                    String orderid = String.valueOf(allorderslist.get(position).getOrderID());
                    //String itemkey = allorderslist.get(position).getKey();
                    String paidby = allorderslist.get(position).getPaidby();
                    String username = allorderslist.get(position).getUsername();
                    String payment_type = allorderslist.get(position).getPayment_type();
                    String key = myDBref.child("Balances").push().getKey();
                    //Todo address this by uncommenting
                    // com.example.jepapp.Models.Orders balancedueorders = new com.example.jepapp.Models.Orders(orderid, title, quantity, cost,username,key,payment_type,paidby);
                    // myDBref.child("Balances")
//                            .child(key)
//                            .setValue(balancedueorders);
                    deleteItem(allorderslist.get(position));
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position,adapter.getItemCount());
                    Log.e("deleting","delete started");
                    Toast toast = Toast.makeText(getContext(),
                            "Item has been moved",
                            Toast.LENGTH_SHORT);
                    toast.show();
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
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.unpaid);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#388E3c"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.paid);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

    }
    public void deleteItem(com.example.jepapp.Models.Orders remove){
        //Todo address this by uncommenting
        // databaseReference.child(remove.getKey()).removeValue();
        // Log.e("Keytime", remove.getKey());

    }



}