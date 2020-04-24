package com.example.jepapp.Adapters.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AllPreparedAdapter extends RecyclerView.Adapter<AllPreparedAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;
    private List<Orders> allpreparedList;
    private static int currentPosition = -1;
    private Integer funds, balance, price_of_order;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;


    public AllPreparedAdapter(Context mCtx, List<Orders> allpreparedList) {
        this.mCtx = mCtx;
        this.allpreparedList = allpreparedList;

    }


    @NotNull
    @Override
    public AllPreparedAdapter.ProductViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.allorderslayout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        return new AllPreparedAdapter.ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        final Orders item = allpreparedList.get(position);

        //place order details into a list and splitting the list to display order details in different lines
        ArrayList<String> orderstuff = item.getOrdertitle();
        String listString = "";
        for (String s : orderstuff) {
            listString += s + "\t";
        }
        String newlistString = listString.replace(", ", "\n");

        //binding the data with the viewholder views
        holder.allOrdersCustomer.setText("Name:" + item.getUsername());
        holder.allOrdersID.setText(item.getOrderID());
        holder.allOrdersTitle.setText("Items:\n" + newlistString);
        holder.allOrdersCost.setText("Total:" + item.getCost());
        holder.allOrdersDate.setText("Date:" + item.getDate());
        holder.allOrdersStatus.setText("Status: " + item.getStatus());
        holder.allOrdersTime.setText("Time:" + item.getTime());
        holder.allOrdersRequests.setText("Special request:\n" + item.getRequest());
        holder.allOrdersPayBy.setText("Paid by:" + item.getPaidby());
        holder.allOrdersPaymentType.setText(item.getPayment_type());
        //check the status of the order and display the appropriate linear layouts with buttons
            if (currentPosition == position) {
                //creating an animation
                Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

                //toggling visibility
                holder.preparedbuttonlayout.setVisibility(View.VISIBLE);

                //adding sliding effect
                holder.preparedbuttonlayout.startAnimation(slideDown);
            } else if (currentPosition == -1) {
                Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
                holder.preparedbuttonlayout.setVisibility(View.GONE);

                //adding sliding effect
                holder.preparedbuttonlayout.startAnimation(slideUp);

            }

            if(currentUser.getEmail().equalsIgnoreCase("canteenstaff@cf.com")){
                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mCtx, "Sorry, no further action can be performed", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {

                //showing buttons on item click
                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //getting the position of the item to expand it
                        if (currentPosition == position) {
                            currentPosition = -1;

                        } else {
                            currentPosition = position;
                        }
                        //reloading the list
                        notifyDataSetChanged();
                    }

                });
            }
        //collects payment for item
        holder.collect_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getPayment_type().equals("Cash")) {
                    update_status(item, "Completed");
                    // notifyDataSetChanged();
                    Toast toast = Toast.makeText(mCtx,
                            "Item has been paid for",
                            Toast.LENGTH_SHORT);
                    toast.show();

                }
                else{
                    //sets the order status in firebase to completed
                    make_payment(item);

                }
            }
        });

    }

    //deducts the cost of an item from the user's balance who is responsible for payment
    private void make_payment(final Orders item) {
        price_of_order = item.getCost().intValue();
        DatabaseReference subtractor = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        Query subtract_funds = subtractor.orderByChild("empID").equalTo(item.getPaidby());
        subtract_funds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot updatebalance: dataSnapshot.getChildren()){
                    UserCredentials user = updatebalance.getValue(UserCredentials.class);
                    funds = Integer.parseInt(user.getBalance());
                    balance = funds - price_of_order;
                    updatebalance.getRef().child("balance").setValue(balance.toString());
                }
                update_status(item, "Completed");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return allpreparedList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView allOrdersTitle, allOrdersID, allOrdersCustomer, allOrdersDate, allOrdersStatus, allOrdersRequests, allOrdersTime, allOrdersPaymentType, allOrdersPayBy, allOrdersCost;
        LinearLayout parentLayout, preparedbuttonlayout;
        Button collect_payment;

        ProductViewHolder(View itemView) {
            super(itemView);
            allOrdersID = itemView.findViewById(R.id.allordersid);
            allOrdersTitle = itemView.findViewById(R.id.allorderstitle);
            allOrdersCustomer = itemView.findViewById(R.id.allorderscustomername);
            allOrdersDate = itemView.findViewById(R.id.allordersdate);
            allOrdersPaymentType = itemView.findViewById(R.id.allorderspaymenttype);
            allOrdersTime = itemView.findViewById(R.id.allorderstime);
            allOrdersStatus = itemView.findViewById(R.id.allordersstatus);
            preparedbuttonlayout = itemView.findViewById(R.id.preparedbuttonlayout);
            allOrdersRequests = itemView.findViewById(R.id.allordersrequest);
            parentLayout = itemView.findViewById(R.id.PARENT);
            collect_payment = itemView.findViewById(R.id.collectpayment);
            allOrdersPayBy = itemView.findViewById(R.id.allorderspayby);
            allOrdersCost = itemView.findViewById(R.id.allorderscost);

        }
    }
    public void updateList(List<Orders> newList){
        allpreparedList = new ArrayList<>();
        allpreparedList = newList;
        notifyDataSetChanged();
    }
    //update the status of an order
    private void update_status(Orders item, final String status) {
        if (item.getType().equals("Breakfast")){
            DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
            databaseReference.child(item.getOrderID()).child("status").setValue(status);
            AllPreparedAdapter.this.notifyDataSetChanged();

        }
        else if (item.getType().equals("Lunch")){
            DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
            databaseReference.child(item.getOrderID()).child("status").setValue(status);
            AllPreparedAdapter.this.notifyDataSetChanged();

        }
    }


}
