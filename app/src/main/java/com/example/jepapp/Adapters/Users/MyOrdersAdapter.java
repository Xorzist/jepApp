package com.example.jepapp.Adapters.Users;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Users.CustomerViewPager;
import com.example.jepapp.Models.Cut_Off_Time;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.Reviews;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ProductViewHolder> {


    private final List<Reviews> myReviewsList;
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Orders> myOrdersList;
    private DatabaseReference referencecutofftime,Ordersreference;
    SimpleDateFormat parseFormat;
    private ArrayList<Cut_Off_Time> cuttoftimes = new ArrayList<>();
    DateFormat inputFormat;
    private SimpleDateFormat SimpleDateFormat,simpleTimeFormat;
    private Date datenow;
    private String breakfastapptime,lunchapptime;
    private DatabaseReference referencereviews;
    private ArrayList<Reviews> currentuserreviews;

    public MyOrdersAdapter(Context mCtx, List<Orders> myOrdersList,List<Reviews>myReviewsList) {
        this.mCtx = mCtx;
        this.myOrdersList = myOrdersList;
        this.myReviewsList = myReviewsList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.customer_orders_recyclerlayout,null);

        referencecutofftime = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");
        referencereviews = FirebaseDatabase.getInstance().getReference("JEP").child("Reviews");
        Ordersreference = FirebaseDatabase.getInstance().getReference("JEP");
        SimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleTimeFormat = new SimpleDateFormat("HH:mm");
        parseFormat = new SimpleDateFormat("hh:mm a");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm a.SSSX");
        datenow = new Date();
        Cutofftimesgetter();
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder1, int position) {
        //getting the item of the specified position
        final Orders item = myOrdersList.get(position);
        holder1.myordertype.setText(item.getType());

        holder1.myOrdersCost.setText(""+item.getCost());
        holder1.myOrdersPaymentType.setText(String.valueOf(item.getPayment_type()));
        holder1.myorderdate.setText(item.getDate());
        holder1.myorderstatus.setText(item.getStatus());
        ArrayList<String> orderdescription = item.getOrdertitle();
        String descriptionstring = "";
        for (String s : orderdescription){
            descriptionstring += s +"\n";

        }
        Log.e("is running?", myReviewsList.get(0).getOrderID());
        for (Reviews reviews : myReviewsList){
            Log.e("is running?", reviews.getOrderID());
            if (reviews.getOrderID().equals(item.getOrderID())){
                holder1.haslike.setText(reviews.getLiked());
                holder1.hasdislike.setText(reviews.getDisliked());
                holder1.hasreivew.setText(reviews.getTitle());
                Log.e("like", holder1.haslike.getText().toString());
                Log.e("dislike", holder1.hasdislike.getText().toString());
                Log.e("like", holder1.hasreivew.getText().toString());
            }

        }
        if (holder1.haslike.getText().toString().toLowerCase().equals("yes")){
            holder1.like.setImageResource(R.drawable.likeunshaded);


        } else if (holder1.hasdislike.getText().toString().toLowerCase().equals("yes")){
            holder1.dislike.setImageResource(R.drawable.dislikeshaded);

        }
        holder1.myordertext.setText(descriptionstring);
        holder1.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder1.optionslayout.getVisibility()==View.GONE){
                    holder1.optionslayout.setVisibility(View.VISIBLE);
                }else{
                    holder1.optionslayout.setVisibility(View.GONE);

                }

            }
        });
        holder1.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String type = item.getType();

                Log.e("CLicked cancel", type);

                if (type.equals("Breakfast")) {

                        //If the user tries to access the menu after cut off time
                    Date timenow = null;
                    try {
                        timenow = simpleTimeFormat.parse(simpleTimeFormat.format(datenow));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date bapptime = null;
                    try {
                        bapptime = simpleTimeFormat.parse(breakfastapptime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date startime = null;
                    try {
                        startime = simpleTimeFormat.parse("06:00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.e("CLicked cancel", "first if: ");
                        if (timenow.after(bapptime)||timenow.before(startime)) {
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Orders Cut of Time")
                                    .setMessage("Sorry,the time for ordering breakfast has passed")
                                    .setPositiveButton("Okay", null)
                                    .setIcon(R.drawable.adminprofile)
                                    .show();

                        } else if(holder1.myorderstatus.getText().toString().equals("Incomplete")) {
                            Log.e("CLicked cancel", "Secondif: ");
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Cancel My Order")
                                    .setMessage("Do you want to cancel your order?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ProgressDialog progressDialog1 = new ProgressDialog(mCtx);
                                            progressDialog1.show();
                                            Ordersreference.child("BreakfastOrders")
                                                    .child(item.getOrderID())
                                                    .child("status")
                                                    .setValue("cancelled");
                                            progressDialog1.cancel();
                                            Intent inside = new Intent(mCtx, CustomerViewPager.class);
                                            mCtx.startActivity(inside);
                                        }
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .setIcon(R.drawable.adminprofile)
                                    .show();

                        }
                        else{
                            Log.e("CLicked cancel", "else: ");
                            Toast.makeText(mCtx.getApplicationContext(), "The Order has already been processed", Toast.LENGTH_SHORT).show();
                        }
                    }


                if (type.equals("Lunch")) {


                        //If the user tries to access the menu after cut off time
                    Date timenow = null;
                    try {
                        timenow = simpleTimeFormat.parse(simpleTimeFormat.format(datenow));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date lunchtime = null;
                    try {
                        lunchtime = simpleTimeFormat.parse(lunchapptime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date startime = null;
                    try {
                        startime = simpleTimeFormat.parse("06:00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (timenow.after(lunchtime) || timenow.before(startime)) {
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Orders Cut of Time")
                                    .setMessage("Sorry,the time for ordering Lunch has passed")
                                    .setPositiveButton("Okay", null)
                                    .setIcon(R.drawable.adminprofile)
                                    .show();

                        } else if(holder1.myorderstatus.getText().toString().equals("Incomplete")) {
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Cancel My Order")
                                    .setMessage("Do you want to cancel your order?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ProgressDialog progressDialog1 = new ProgressDialog(mCtx);
                                            progressDialog1.show();
                                            Ordersreference.child("LunchOrders")
                                                    .child(item.getOrderID())
                                                    .child("status")
                                                    .setValue("cancelled");
                                            progressDialog1.cancel();
                                            Intent inside = new Intent(mCtx, CustomerViewPager.class);
                                            mCtx.startActivity(inside);


                                        }
                                    })
                                    .setNegativeButton("No",null)
                                    .setIcon(R.drawable.adminprofile)
                                    .show();

                        }
                        else{
                            Toast.makeText(mCtx.getApplicationContext(), "The Order has already been processed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }


        });

        holder1.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the order already has a like
                if(holder1.haslike.getText().toString().equals("none")){
                    submitReview(item.getOrderID(),"yes","no","none","none",item.getDate(),item.getType());
                    holder1.like.setImageResource(R.drawable.likeunshaded);
                }else if (holder1.haslike.getText().toString().toLowerCase().equals("no")){

                }
                else{
                    Toast.makeText(mCtx.getApplicationContext(), "Order has already been liked", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder1.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the order already has a dislike
                if(holder1.hasdislike.getText().toString().equals("none") || holder1.hasdislike.getText().toString().toLowerCase().equals("no")){
                    submitReview(item.getOrderID(),"no","yes","none","none",item.getDate(),item.getType());
                    holder1.dislike.setImageResource(R.drawable.dislikeshaded);
                }
                else{
                    Toast.makeText(mCtx.getApplicationContext(), "Order has already been disliked", Toast.LENGTH_SHORT).show();
                }


            }
        });

        holder1.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }



    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView myordertype, myOrdersCost, myOrdersPaymentType,myorderdate,myorderstatus,myordertext,hasreivew
                ,hasdislike,haslike;
        ImageView cancel,like,dislike,review;
        LinearLayout parentLayout,optionslayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            myordertype = itemView.findViewById(R.id.customerordertype);
            myOrdersCost = itemView.findViewById(R.id.customerordercost);
            myOrdersPaymentType = itemView.findViewById(R.id.customerpaymentype);
            myorderstatus = itemView.findViewById(R.id.customerorderstatus);
            myorderdate = itemView.findViewById(R.id.customerorderdate);
            myordertext = itemView.findViewById(R.id.customerorderitems);
            parentLayout = itemView.findViewById(R.id.orderlayout);
            optionslayout = itemView.findViewById(R.id.optionslayout);
            cancel = itemView.findViewById(R.id.cancelmyorder);
            like = itemView.findViewById(R.id.likeordder);
            dislike = itemView.findViewById(R.id.dislikeorder);
            review = itemView.findViewById(R.id.revieworder);
            haslike = itemView.findViewById(R.id.haslike);
            hasdislike = itemView.findViewById(R.id.hasdislike);
            hasreivew = itemView.findViewById(R.id.hasdescription);

        }
    }

    private  void submitReview(String orderID, String liked, String disliked, String title, String description, String date, String order_type)
        {
        final ProgressDialog progressDialog1 = new ProgressDialog(mCtx);
        progressDialog1.setMessage("Submitting your order");
        progressDialog1.show();
        Reviews reviews;
        String key =referencereviews.push().getKey();
        reviews = new Reviews(orderID,key,liked,disliked,title,description,date,order_type);
        referencereviews
                .child(key)
                .setValue(reviews);
        Log.d("Start Adding","START!");
        progressDialog1.cancel();
    }

    private void Cutofftimesgetter() {
        final ProgressDialog progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Getting Cut Off Times");
        progressDialog.show();
        referencecutofftime.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cut_Off_Time cuttofftime = dataSnapshot.getValue(Cut_Off_Time.class);
                    cuttoftimes.add(cuttofftime);

                }
                //Assign the breakfast and lunch times respectively straight from the database
                String dbbreakfasttime = cuttoftimes.get(0).getTime();
                String dblunchtime = cuttoftimes.get(1).getTime();

                try {
                    breakfastapptime = simpleTimeFormat.format(parseFormat.parse(dbbreakfasttime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    lunchapptime = simpleTimeFormat.format(parseFormat.parse(dblunchtime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("formatted breakfast!!", (breakfastapptime));
                Log.e("formatted breakfast!!", (lunchapptime));
                progressDialog.cancel();
                progressDialog.dismiss();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void Reviewsgetter() {

        final ProgressDialog progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Getting All Reviews");
        progressDialog.show();
        referencereviews.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get all the reviews that belong to the specific user
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Reviews reviews = dataSnapshot.getValue(Reviews.class);
//                    if(reviews.get)
//                    currentuserreviews.add(reviews);

                }

                progressDialog.cancel();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
