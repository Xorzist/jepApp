package com.example.jepapp.Adapters.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<Orders> allOrdersList;
    private static int currentPosition = -1;


    public AllOrdersAdapter(Context mCtx, List<Orders> allOrdersList) {
        this.mCtx = mCtx;
        this.allOrdersList = allOrdersList;
    }

    @Override
    public AllOrdersAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.allorderslayout, parent, false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allorderslayout, parent, false );
        return new AllOrdersAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        final Orders item = allOrdersList.get(position);

        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

            //toggling visibility
            holder.ordersbuttonlayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.ordersbuttonlayout.startAnimation(slideDown);
        } else if (currentPosition == -1) {
            Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
            holder.ordersbuttonlayout.setVisibility(View.GONE);

            //adding sliding effect
            holder.ordersbuttonlayout.startAnimation(slideUp);

        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                if (currentPosition == position) {
                    currentPosition = -1;

                } else if (currentPosition != position) {
                    currentPosition = position;
                }
                //reloding the list
                notifyDataSetChanged();
            }

        });


        ArrayList<String> orderstuff = item.getOrdertitle();
        String listString = "";
        String newlistString = "";
        for (String s : orderstuff) {
            listString += s + "\t";
        }
        newlistString = listString.replace(", ", "\n");
        Log.e("orderstuff", listString);
        //binding the data with the viewholder views
        //TODO address this line by uncommenting
        // holder.allOrdersTitle.setText(item.getOrdertitle());
        holder.allOrdersCustomer.setText("Name:" + item.getUsername());
        holder.allOrdersID.setText(item.getOrderID());
        holder.allOrdersTitle.setText("Items:\n" + newlistString);
        holder.allOrdersCost.setText("Total:" + item.getCost());
        holder.allOrdersDate.setText("Date:" + item.getDate());
        holder.allOrdersStatus.setText("Status: " + item.getStatus());
        holder.allOrdersTime.setText("Time:" + item.getTime());
        holder.allOrdersRequests.setText("Special request:\n" + item.getRequest());
        holder.allOrdersPayBy.setText("Paid by:" + String.valueOf(item.getPaidby()));
        holder.allOrdersPaymentType.setText(item.getPayment_type());
        if (holder.allOrdersStatus.getText().equals("Status: cancelled")) {
            holder.allOrderscancel.setVisibility(View.VISIBLE);
        } else {
            holder.allOrderscancel.setVisibility(View.GONE);
        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
                builder1.create();
                builder1.setTitle("Cancel Order");
                builder1.setMessage("Are you sure you would like to cancel this order? \nNB This action cannot be undone");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update_status(item, "cancelled");
                       // notifyDataSetChanged();
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
            }
        });
        holder.prepared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
                builder1.create();
                builder1.setMessage("Set order as prepared");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        update_status(item, "prepared");
                        Log.e("prep", item.getType());

                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(mCtx);

                View promptsView = li.inflate(R.layout.admin_update_user_order_details, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setView(promptsView);
                builder.setTitle("Edit User Order Details");
                builder.setCancelable(true);
                final TextView id, date, time, username, orderstatus, data, request, cost, payby, paymenttype;
                final EditText orderinfo;
                final FloatingActionButton editorder, editpaymenttype;
                final Button submit =promptsView.findViewById(R.id.dialogbuttonsubmit);
                id = promptsView.findViewById(R.id.dialogallordersid);
                date = promptsView.findViewById(R.id.dialogallordersdate);
                time = promptsView.findViewById(R.id.dialogallorderstime);
                username = promptsView.findViewById(R.id.dialogallorderscustomername);
                orderstatus = promptsView.findViewById(R.id.dialogallordersstatus);
                data = promptsView.findViewById(R.id.dialogallorderstitle);
                request = promptsView.findViewById(R.id.dialogallordersrequest);
                cost = promptsView.findViewById(R.id.dialogallorderscost);
                payby = promptsView.findViewById(R.id.dialogallorderspayby);
                orderinfo = promptsView.findViewById(R.id.editText);
                paymenttype = promptsView.findViewById(R.id.dialogallorderspaymenttype);
                editorder = promptsView.findViewById(R.id.dialogedit_ordertitles);
                editpaymenttype = promptsView.findViewById(R.id.dialogedit_order_paymenttype);
                id.setText(item.getOrderID());
                date.setText("Date: " + item.getDate());
                time.setText("Time: "+item.getTime());
                username.setText("Name: " + item.getUsername());
                orderstatus.setText("Status: " +item.getStatus());
                request.setText("Request: "+ item.getRequest());
                String coststring = String.valueOf(item.getCost());
                cost.setText("Cost: " + coststring);
                payby.setText("Buyer: "+ item.getPaidby());
                paymenttype.setText(item.getPayment_type());
                ArrayList<String> dialogorderstuff = item.getOrdertitle();
                String dialoglistString = "";
                String newdialoglistString = "";
                for (String s : dialogorderstuff) {
                    dialoglistString += s + "\t";
                }
                newdialoglistString = dialoglistString.replace(", ", "\n");
                data.setText(newdialoglistString);
                orderinfo.setText(newdialoglistString);

                editorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // showPopup(item)
                        data.setVisibility(View.GONE);
                        orderinfo.setVisibility(View.VISIBLE);

                        Toast toast = Toast.makeText(mCtx,
                                "Order details can now be edited",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                editpaymenttype.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
                        builder1.create();
                        builder1.setMessage("Select payment type below");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Lunch card", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                paymenttype.setText("Lunch card");
                            }
                        });
                        builder1.setNegativeButton("Cash", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                paymenttype.setText("Cash");
                            }
                        });

                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();
                    }

                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                AlertDialog alertDialog1 = builder.create();
                alertDialog1.show();
            }

        });

    }




    @Override
    public int getItemCount() {
        return allOrdersList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView allOrdersTitle, allOrdersID, allOrdersCustomer, allOrdersDate, allOrdersStatus, allOrdersRequests, allOrdersTime, allOrdersPaymentType, allOrdersPayBy, allOrdersCost;
        LinearLayout parentLayout, ordersbuttonlayout;
        Button prepared, cancel, edit;
        ImageView allOrderscancel;

        public ProductViewHolder(View itemView) {
            super(itemView);
            allOrdersID = itemView.findViewById(R.id.allordersid);
            allOrdersTitle = itemView.findViewById(R.id.allorderstitle);
            allOrdersCustomer = itemView.findViewById(R.id.allorderscustomername);
            allOrdersDate = itemView.findViewById(R.id.allordersdate);
            allOrdersPaymentType = itemView.findViewById(R.id.allorderspaymenttype);
            allOrdersTime = itemView.findViewById(R.id.allorderstime);
            allOrderscancel = itemView.findViewById(R.id.cancelled_image);
            allOrdersStatus = itemView.findViewById(R.id.allordersstatus);
            ordersbuttonlayout = itemView.findViewById(R.id.ordersbuttonslayout);
            allOrdersRequests = itemView.findViewById(R.id.allordersrequest);
            parentLayout = itemView.findViewById(R.id.PARENT);
            prepared = itemView.findViewById(R.id.button_prepared);
            cancel = itemView.findViewById(R.id.button_cancel);
            edit = itemView.findViewById(R.id.button_edit);
            allOrdersPayBy = itemView.findViewById(R.id.allorderspayby);
            allOrdersCost = itemView.findViewById(R.id.allorderscost);

        }
    }
    public void updateList(List<Orders> newList){
        allOrdersList = new ArrayList<>();
        allOrdersList = newList;
        notifyDataSetChanged();
    }

    public void showPopup(Orders item){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View convertView = inflater.inflate(R.layout.recycler_popup_window, null);
        alertDialog.setView(convertView);
        alertDialog.setCancelable(true);
        RecyclerView list = convertView.findViewById(R.id.rv_recycler_view);
        list.setLayoutManager(new LinearLayoutManager(mCtx));

        PopupRecyclerViewAdapter adapter = new PopupRecyclerViewAdapter(mCtx, item.getOrdertitle());
        list.setAdapter(adapter);



        alertDialog.show();
//        final View popupView = LayoutInflater.from(mCtx).inflate(R.layout.recycler_popup_window, null);
//        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//
//        Button btn = (Button) popupView.findViewById(R.id.button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
//
//        RecyclerView recyclerView = (RecyclerView) popupView.findViewById(R.id.rv_recycler_view);
//        ArrayList<String> data = new ArrayList<>();
//        data = item.getOrdertitle();
//        PopupRecyclerViewAdapter adapter = new PopupRecyclerViewAdapter(mCtx,data);
//        recyclerView.setAdapter(adapter);
//
//        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
//
    }


    private void update_status(Orders item, final String status) {

        if (item.getType().equals("Breakfast")){
            DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
            Query update_state = databaseReference.orderByChild("orderID").equalTo(item.getOrderID());
            Log.e("checking", "Breakfast");
            update_state.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot updateQuantity: dataSnapshot.getChildren()){
                        updateQuantity.getRef().child("status").setValue(status);
                        Log.e("uodated",status);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        if (item.getType().equals("Lunch")){
            DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
            Query update_state = databaseReference.orderByChild("orderID").equalTo(item.getOrderID());
            update_state.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot updateQuantity: dataSnapshot.getChildren()){
                        updateQuantity.getRef().child("status").setValue(status);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        }


}
