package com.example.jepapp.Adapters.Users;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Users.CustomerViewPager;
import com.example.jepapp.Models.Cut_Off_Time;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.Reviews;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ProductViewHolder> {


    private  List<Reviews> myReviewsList;
    private Context mCtx;
    private List<Orders> myOrdersList;
    private DatabaseReference referencecutofftime, mydbreference;
    SimpleDateFormat parseFormat;
    private ArrayList<Cut_Off_Time> cuttoftimes = new ArrayList<>();
    DateFormat inputFormat;
    private SimpleDateFormat simpleTimeFormat;
    private Date datenow;
    private String breakfastapptime;
    private  String lunchapptime;
    private DatabaseReference referencereviews;
    private UserCredentials ThePayingUser;



    public MyOrdersAdapter(Context mCtx, List<Orders> myOrdersList, List<Reviews> myReviewsList) {
        this.mCtx = mCtx;
        this.myOrdersList = myOrdersList;
        this.myReviewsList = myReviewsList;
    }

    public MyOrdersAdapter() {

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.customer_orders_recyclerlayout,null);

        referencecutofftime = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");
        referencereviews = FirebaseDatabase.getInstance().getReference("JEP").child("Reviews");
        mydbreference = FirebaseDatabase.getInstance().getReference("JEP");
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
        final ArrayList<String> orderdescription = item.getOrdertitle();
        String descriptionstring = "";
        for (String s : orderdescription){
            descriptionstring += s +"\n";

        }
        //Check each review object in a list of reviews retrieved from the database
        for (int i = 0; i < myReviewsList.size(); i++) {
            //If any review in the list matches with a user's order,add that specific review's details
            //to the corresponding Order's holder information.
            if (myReviewsList.get(i).getOrderID().equals(item.getOrderID())){

                holder1.haslike.setText(myReviewsList.get(i).getLiked());
                holder1.hasdislike.setText(myReviewsList.get(i).getDisliked());
                holder1.hasreivew.setText(myReviewsList.get(i).getTitle());
                holder1.hasID.setText(myReviewsList.get(i).getOrderID());
                holder1.title.setText(myReviewsList.get(i).getTitle());
                holder1.description.setText(myReviewsList.get(i).getDescription());
                holder1.reviewtopic.setText(myReviewsList.get(i).getReviewtopic());
            }

        }

        if (holder1.haslike.getText().toString().toLowerCase().equals("yes")){
            //Check to see if the order has been liked,based on the review details.
            holder1.like.setImageResource(R.drawable.likeshaded);
            holder1.dislike.setImageResource(R.drawable.dislikeunshaded);


        } else if (holder1.hasdislike.getText().toString().toLowerCase().equals("yes")){
            //Check to see if the order has been disliked,based on the review details.
            holder1.dislike.setImageResource(R.drawable.dislikeshaded);
            holder1.like.setImageResource(R.drawable.likeusnhaded);

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
        //This function will allow a user to cancel their order
        holder1.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String type = item.getType();
                //Function to assign the user who pays for an order to a variable
                GetPayingUser(item.getPaidby());

                if (type.equals("Breakfast")) {


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
                    //Determine if the user tries to access the breakfast menuitems after cut off time
                    // and when an order has not yet been pprocessed
                        if (timenow.after(bapptime)||timenow.before(startime)) {

                            new AlertDialog.Builder(v.getContext(),R.style.datepicker)
                                    .setTitle("Orders Cut of Time")
                                    .setMessage("Sorry,the time for ordering breakfast has passed")
                                    .setPositiveButton("Okay", null)
                                    .setIcon(R.drawable.adminprofile)
                                    .show();

                        } else if(holder1.myorderstatus.getText().toString().equals("Incomplete")) {

                            new AlertDialog.Builder(v.getContext(),R.style.datepicker)
                                    .setTitle("Cancel My Order")
                                    .setMessage("Do you want to cancel your order?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ProgressDialog CancelbreakfastOrder = new ProgressDialog(mCtx);
                                            CancelbreakfastOrder.show();
                                            mydbreference.child("BreakfastOrders")
                                                    .child(item.getOrderID())
                                                    .child("status")
                                                    .setValue("cancelled");
                                            //Update the available balance of the user who paid for the order
                                            for (String s: orderdescription) {
                                                String number = s.substring(s.indexOf("(")+2,s.indexOf(")"));
                                                String noparantheses = s.split("[\\](},]")[0];
                                                UpdateMenuAdd("BreakfastMenu",number,noparantheses);
                                            }
                                            //Determine if the customer used their card to pay for the order
                                            if (item.getPayment_type().toLowerCase().toString().equals("lunch card")) {
                                                Long payeeBalance = (Float.valueOf(ThePayingUser.getAvailable_balance())).longValue();
                                                String newbalance = String.valueOf((payeeBalance + item.getCost()));
                                                //Update the available balance of the user who paid for the order
                                                mydbreference.child("Users")
                                                        .child(ThePayingUser.getEmail().replace(".", ""))
                                                        .child("available_balance").setValue(newbalance);
                                            }
                                            CancelbreakfastOrder.cancel();
                                            Intent inside = new Intent(mCtx, CustomerViewPager.class);
                                            mCtx.startActivity(inside);
                                        }
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .setIcon(R.drawable.adminprofile)
                                    .show();

                        }
                        else{

                            Toast.makeText(mCtx.getApplicationContext(), "The Order has already been processed", Toast.LENGTH_SHORT).show();
                        }
                    }


                if (type.equals("Lunch")) {
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

                    //Determine if the user tries to access the lunch menuitems after cut off time
                    // and when an order has not yet been pprocessed
                    if (timenow.after(lunchtime) || timenow.before(startime)) {

                            new AlertDialog.Builder(v.getContext(),R.style.datepicker)
                                    .setTitle("Orders Cut of Time")
                                    .setMessage("Sorry,the time for ordering Lunch has passed")
                                    .setPositiveButton("Okay", null)
                                    .setIcon(R.drawable.adminprofile)
                                    .show();

                        } else if(holder1.myorderstatus.getText().toString().equals("Incomplete")) {
                            new AlertDialog.Builder(v.getContext(),R.style.datepicker)
                                    .setTitle("Cancel My Order")
                                    .setMessage("Do you want to cancel your order?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ProgressDialog CancelLunchOrderDialog = new ProgressDialog(mCtx);
                                            CancelLunchOrderDialog.show();
                                            mydbreference.child("LunchOrders")
                                                    .child(item.getOrderID())
                                                    .child("status")
                                                    .setValue("cancelled");
                                            //Update Menu Item values
                                            for (String s: orderdescription) {
                                                String number = s.substring(s.indexOf("(")+2,s.indexOf(")"));
                                                String noparantheses = s.split("[\\](},]")[0];
                                                UpdateMenuAdd("Lunch",number,noparantheses);
                                            }
                                            //Determine if the customer used their card to pay for the order
                                            if ((item.getPayment_type().toLowerCase().toString().equals("lunch card"))) {
                                                Long payeeBalance = (Long.valueOf(ThePayingUser.getAvailable_balance()));
                                                String newbalance = String.valueOf((payeeBalance + item.getCost()));
                                                //Update the available balance of the user who paid for the order
                                                mydbreference.child("Users")
                                                        .child(ThePayingUser.getEmail().replace(".", ""))
                                                        .child("available_balance").setValue(newbalance);
                                            }
                                            CancelLunchOrderDialog.cancel();
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

        //This function will allow a user to like their order
        holder1.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getStatus().toLowerCase().equals("completed")){
                    //Check if the order already has a like
                    if(holder1.haslike.getText().toString().equals("none") && holder1.hasreivew.getText().toString().equals("none")){
                        //Submit a brand new review as the review contents for the order do not exist
                        submitReview(item.getOrderID(),"yes","no","none","none",item.getDate(),item.getType(),"none");
                        //Set the image for the order to liked
                        holder1.like.setImageResource(R.drawable.likeshaded);
                    }
                    else if (holder1.haslike.getText().toString().equals("none") && !holder1.hasreivew.getText().toString().equals("none")){
                        //This checks if the like or dislike values are set to none and that a descriptive review has been entered
                        referencereviews
                                .child(holder1.hasID.getText().toString())
                                .child("liked")
                                .setValue("yes");
                        referencereviews
                                .child(holder1.hasID.getText().toString())
                                .child("disliked")
                                .setValue("no");
                        //Set the image for the order to liked
                        holder1.like.setImageResource(R.drawable.likeshaded);
                        //Remove the disliked image
                        holder1.dislike.setImageResource(R.drawable.dislikeunshaded);

                    }

                    else if (holder1.haslike.getText().toString().toLowerCase().equals("no") && !holder1.hasreivew.getText().toString().equals("none")){
                        //This checks if the order has a like value and if it has a descriptive review
                        //Therefore we will set this orders liked value in the db to yes and change its disliked value to no
                        referencereviews
                                .child(holder1.hasID.getText().toString())
                                .child("liked")
                                .setValue("yes");
                        referencereviews
                                .child(holder1.hasID.getText().toString())
                                .child("disliked")
                                .setValue("no");
                        //Set the image for the order to liked
                        holder1.like.setImageResource(R.drawable.likeshaded);
                        //Remove the disliked image
                        holder1.dislike.setImageResource(R.drawable.dislikeunshaded);
                    }
                    else if (holder1.haslike.getText().toString().toLowerCase().equals("no") && holder1.hasreivew.getText().toString().equals("none")){
                        //This checks if the order has a like value and if it has a descriptive review
                        //Therefore we will set this orders liked value in the db to yes and change its disliked value to no
                        submitReview(item.getOrderID(),"yes","no","none","none",item.getDate(),item.getType(),"none");

                        //Set the image for the order to liked
                        holder1.like.setImageResource(R.drawable.likeshaded);
                        //Remove the disliked image
                        holder1.dislike.setImageResource(R.drawable.dislikeunshaded);
                    }

                    else if (holder1.haslike.getText().toString().equals("yes")) {
                        //Response if the user presses the disliked button after the order has already been disliked
                        Toast.makeText(mCtx.getApplicationContext(), "Order has already been liked", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    //Response if the order has not been completed
                    Toast.makeText(mCtx.getApplicationContext(), "Order has not yet been processed", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //This function will allow a user to dislike their order
        holder1.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getStatus().toLowerCase().equals("completed")){
                    //Check if the order already has a dislike
                    if(holder1.hasdislike.getText().toString().equals("none")  && holder1.hasreivew.getText().toString().equals("none")){
                      //This checks if the user has no like or dislike value and that it also has no descriptive review
                        submitReview(item.getOrderID(),"no","yes","none","none",item.getDate(),item.getType(),"none");
                        //Set the image for the order to disliked
                        holder1.dislike.setImageResource(R.drawable.dislikeshaded);
                    }
                    else if (holder1.hasdislike.getText().toString().equals("none") && !holder1.hasreivew.getText().toString().equals("none")){
                        //This checks if the like or dislike values are set to none and that a descriptive review has been entered

                        referencereviews
                                .child(holder1.hasID.getText().toString())
                                .child("liked")
                                .setValue("no");
                        referencereviews
                                .child(holder1.hasID.getText().toString())
                                .child("disliked")
                                .setValue("yes");
                        holder1.dislike.setImageResource(R.drawable.dislikeshaded);
                        //Remove the liked image
                        holder1.like.setImageResource(R.drawable.likeusnhaded);



                    }
                    else if (holder1.hasdislike.getText().toString().toLowerCase().equals("no") && !holder1.hasreivew.getText().toString().equals("none")){
                        //This checks if the order has a dislike value and if it has a descriptive review
                        //Therefore we will set this orders liked value in the db to yes and change its disliked value to no

                        referencereviews
                                .child(holder1.hasID.getText().toString())
                                .child("liked")
                                .setValue("no");
                        referencereviews
                                .child(holder1.hasID.getText().toString())
                                .child("disliked")
                                .setValue("yes");
                        //Set the image for the order to disliked
                        holder1.dislike.setImageResource(R.drawable.dislikeshaded);
                        //Remove the liked image
                        holder1.like.setImageResource(R.drawable.likeusnhaded);


                    }
                    else if (holder1.hasdislike.getText().toString().toLowerCase().equals("no") && holder1.hasreivew.getText().toString().equals("none")){
                        //This checks if the order has a dislike value and if it has a descriptive review
                        //Therefore we will set this orders liked value in the db to yes and change its disliked value to no
                        submitReview(item.getOrderID(),"no","yes","none","none",item.getDate(),item.getType(),"none");
                        //Set the image for the order to disliked
                        holder1.dislike.setImageResource(R.drawable.dislikeshaded);
                        //Remove the liked image
                        holder1.like.setImageResource(R.drawable.likeusnhaded);


                    }

                    else if (holder1.hasdislike.getText().toString().equals("yes")) {
                        //Response if the user presses the disliked button after the order has already been disliked
                        Toast.makeText(mCtx.getApplicationContext(), "Order has already been disliked", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    //Response if the order has not been completed
                    Toast.makeText(mCtx.getApplicationContext(), "Order has not yet been processed", Toast.LENGTH_SHORT).show();
                }



            }
        });

        holder1.review.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> thetitlesonly = new ArrayList<>();
            @Override
            public void onClick(View v) {
                if(item.getStatus().toLowerCase().equals("completed")){
                    //Check if the order already has a review

                    if (holder1.title.getText().toString().toLowerCase().equals("none")){
                        //Open a dialog to allow the user to enter their descriptive reivew

                        for (String s : orderdescription) {
                            //Retrieve the number value only between the parentheses
                            String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                            for (int i = 0; i < Integer.valueOf(number); i++) {
                                String noparantheses = s.split("[\\](},]")[0];
                                thetitlesonly.add(noparantheses);
                            }
                        }
                        reviewDialog(true, "none","none",holder1.hasID.getText().toString(),
                                thetitlesonly,item.getOrderID(),item.getDate(),item.getType(),
                                holder1.haslike.getText().toString(),holder1.hasdislike.getText().toString());
                    }else{
                        //Open a dialog to present the  descriptive review that was left on the order
                        reviewDialog(false,holder1.title.getText().toString(),holder1.description.getText().toString(), holder1.hasID.getText().toString(),
                                thetitlesonly,item.getOrderID(),item.getDate(),item.getType(),
                                holder1.haslike.getText().toString(),holder1.hasdislike.getText().toString());
                    }

                }
                else {
                    Toast.makeText(mCtx.getApplicationContext(), "Order has not yet been processed", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }

    public void updateList(List<Orders> searchorderslist) {
            myOrdersList = new ArrayList<>();
            myOrdersList = searchorderslist;
            notifyDataSetChanged();

    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView myordertype, myOrdersCost, myOrdersPaymentType,myorderdate,myorderstatus,myordertext,hasreivew
                ,hasdislike,haslike,hasID,title,description,reviewtopic;

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
            hasID = itemView.findViewById(R.id.reviewID);
            title = itemView.findViewById(R.id.reviewtitlefield);
            description = itemView.findViewById(R.id.reviewdescriptionfield);
            reviewtopic = itemView.findViewById(R.id.reviewtopics);

        }
    }

    //Function to submit a review into the database
    private  void submitReview(String orderID, String liked, String disliked, String title, String description, String date, String order_type,String reviewtopic)
        {
        final ProgressDialog progressDialog1 = new ProgressDialog(mCtx);
        progressDialog1.setMessage("Submitting your Review");
        progressDialog1.show();
        Reviews reviews;
        reviews = new Reviews(orderID, liked,disliked,title,description,date,order_type, reviewtopic);
        referencereviews
                .child(orderID)
                .setValue(reviews);
        progressDialog1.cancel();
    }

    //Function to get the cut off times from the database
    public void Cutofftimesgetter() {
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
                //Assign the breakfast and lunch cut off times respectively straight from the database
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
                progressDialog.cancel();
                progressDialog.dismiss();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Function to get the details of the user who will pay for the order
    public void GetPayingUser(String paidby){

        Query emailquery = mydbreference.child("Users").orderByChild("empID").equalTo(paidby);


        emailquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);
                    ThePayingUser = (userCredentials);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    //Dialog for the user to enter a descriptive review.If b is false then the dialog shall bring up the review information
    //if b is true then the user wil be allowed ot enter a review
    private void reviewDialog(final boolean b, final String title, final String desc, final String key, final ArrayList<String> titles, final String orderID
    , final String date, final String type, final String likedvalue, final String dislikedvalue) {

        final ProgressDialog ReviewDialog = new ProgressDialog(mCtx);
        ReviewDialog.setTitle("Reviewing Order!");

        //Create Alert Builder
        Activity activity = (Activity) mCtx;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.datepicker);
        builder.setTitle("Order Review");
        //Add Custom Layout
        final View customLayout = LayoutInflater.from(mCtx.getApplicationContext()).inflate(R.layout.customer_reviewlayout, null);
        builder.setView(customLayout);
        final EditText customerdesc,customertitle;
        final Spinner titlespinner;
        final Button titelsadder;
        final TextView aselectedtitles,reviewitemtitle;
        customertitle = customLayout.findViewById(R.id.orderreviewtitle);
        customerdesc = customLayout.findViewById(R.id.orderreviewdesc);
        titlespinner  = customLayout.findViewById(R.id.reviewtitlespinner);
        titelsadder = customLayout.findViewById(R.id.titelbtn);
        aselectedtitles = customLayout.findViewById(R.id.reviewtitlebox);
        reviewitemtitle = customLayout.findViewById(R.id.reviewitemtitle);
        final ArrayList dialogtitles = titles;

        dialogtitles.add("Other");
        final ArrayAdapter<String> stringsadapter =new ArrayAdapter<String>(mCtx,R.layout.myspinneritem, dialogtitles);
        titlespinner.setAdapter(stringsadapter);
        titelsadder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddandRemovefromList(aselectedtitles,stringsadapter,titlespinner);

            }
        });
        if (b){
            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //clear the list for the spinner
                    dialogtitles.clear();
                }
            });

        }
        else{
            customertitle.setText(title);
            customerdesc.setText(desc);
            aselectedtitles.setVisibility(View.GONE);
            titelsadder.setVisibility(View.GONE);
            titlespinner.setVisibility(View.GONE);
            reviewitemtitle.setVisibility(View.GONE);
            customertitle.setEnabled(false);
            customerdesc.setEnabled(false);
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        }
        final AlertDialog ReviewAlert = builder.create();
        ReviewAlert.show();
        ReviewAlert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Determine if an order has already been reviewed and allow a user to enter a new descriptive review if
                //one does not exist
                if (b) {
                    if (customertitle.getText().toString().isEmpty()) {
                        Toast.makeText(mCtx.getApplicationContext(), " A title or description was not entered", Toast.LENGTH_SHORT).show();
                    } else if (customerdesc.getText().toString().isEmpty()) {
                        Toast.makeText(mCtx.getApplicationContext(), " A description was not entered", Toast.LENGTH_SHORT).show();
                    } else if (aselectedtitles.getText().toString().isEmpty()) {
                        Toast.makeText(mCtx.getApplicationContext(), " A review topic was not selected from the list", Toast.LENGTH_SHORT).show();
                    } else {

                        if (key.toLowerCase().equals("none")) {
                            ReviewDialog.show();
                            submitReview(orderID, "none", "none", customertitle.getText().toString(), customerdesc.getText().toString()
                                    , date, type,
                                    aselectedtitles.getText().toString());
                            ReviewDialog.cancel();
                            ReviewDialog.dismiss();
                            ReviewAlert.cancel();
                        }  else if (!key.toLowerCase().equals("none") ){
                            ReviewDialog.show();
                            submitReview(orderID, likedvalue, dislikedvalue, customertitle.getText().toString(), customerdesc.getText().toString()
                                    , date, type,
                                    aselectedtitles.getText().toString());
                            ReviewDialog.cancel();
                            ReviewDialog.dismiss();
                            ReviewAlert.cancel();
                        }


                    }
                }
                else{
                    ReviewAlert.cancel();
                }
            }
        });

    }
    //This function will aid in add and removing order titles from a user's review
    private void AddandRemovefromList(TextView titles, ArrayAdapter<String> stringsadapter, Spinner titlespinner) {

        if (stringsadapter.getCount() == 0) {

        } else {
            String selectedtext = titlespinner.getSelectedItem().toString();
            if (selectedtext.toLowerCase().equals("other") && titles.getText().length()>0){
                Toast.makeText(mCtx, "You can NOT choose 'OTHER' along with a menu item", Toast.LENGTH_SHORT).show();
            } else if (!titles.getText().toString().toLowerCase().contains("other")) {
                stringsadapter.remove(selectedtext);
                String oldtext = titles.getText().toString();
                titles.setText(oldtext + "" + selectedtext + ", ");
                stringsadapter.notifyDataSetChanged();
            }

            else{
                    Toast.makeText(mCtx, "You can NOT enter additional items once other is selected", Toast.LENGTH_SHORT).show();
                }

            }
        }

    //This function will use the title of an item within a specific menuType and update the quantity
    // of the  corresponding Menu item as well as update the available balance for a user
    private void UpdateMenuAdd(String mMenuType, final String morderquantities, final String mitemtitlesonly) {
        final DatabaseReference ref = mydbreference.child(mMenuType);
        ref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String title=data.child("title").getValue().toString();
                    if(title.equals(mitemtitlesonly)){
                        String keyid=data.getKey();
                        String oldvalue = data.child("quantity").getValue().toString();
                        int newvalue= (Integer.valueOf(oldvalue)) + (Integer.valueOf(morderquantities));
                        ref.child(keyid).child("quantity").setValue(String.valueOf(newvalue));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
