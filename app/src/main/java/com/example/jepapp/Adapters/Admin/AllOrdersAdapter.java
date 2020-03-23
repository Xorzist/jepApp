package com.example.jepapp.Adapters.Admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;
    final int VIEW_TYPE_ORDER = 0;
    final int VIEW_TYPE_ORDERINFO = 1;
    //we are storing all the products in a list
    private List<Orders> allOrdersList;


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
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Orders item = allOrdersList.get(position);

        ArrayList<String> orderstuff = item.getOrdertitle();
        String listString = "";
        String newlistString = "";
        for (String s : orderstuff)
        {
            listString += s + "\t";
        }
        newlistString = listString.replace(",","\n");
        Log.e("orderstuff", item.getDate());
        //binding the data with the viewholder views
        //TODO address this line by uncommenting
        // holder.allOrdersTitle.setText(item.getOrdertitle());
        holder.allOrdersCustomer.setText("Name:" + item.getUsername());
        holder.allOrdersTitle.setText("Items:\n" + newlistString);
        holder.allOrdersCost.setText("Total:"+item.getCost());
        holder.allOrdersDate.setText("Date:"+ item.getDate());
        holder.allOrdersStatus.setText("Status:"+item.getStatus());
        holder.allOrdersTime.setText("Time:"+item.getTime());
        holder.allOrdersRequests.setText("Special request:\n"+item.getRequest());
        holder.allOrdersPayBy.setText("Paid by:"+ String.valueOf(item.getPaidby()));
        holder.allOrdersPaymentType.setText("Pay with:" + item.getPayment_type());
        if (holder.allOrdersPaymentType.getText().equals("cancelled")) {
            holder.allOrderscancel.setVisibility(View.VISIBLE);
        } else {
            holder.allOrderscancel.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return allOrdersList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView allOrdersTitle, allOrdersCustomer, allOrdersDate, allOrdersStatus, allOrdersRequests, allOrdersTime, allOrdersPaymentType, allOrdersPayBy, allOrdersCost;
        LinearLayout parentLayout;
        ImageView allOrderscancel;

        public ProductViewHolder(View itemView) {
            super(itemView);

            allOrdersTitle = itemView.findViewById(R.id.allorderstitle);
            allOrdersCustomer = itemView.findViewById(R.id.allorderscustomername);
            allOrdersDate = itemView.findViewById(R.id.allordersdate);
            allOrdersPaymentType = itemView.findViewById(R.id.allorderspaymenttype);
            allOrdersTime = itemView.findViewById(R.id.allOrdersTime);
            allOrderscancel = itemView.findViewById(R.id.cancelled_image);
            allOrdersStatus = itemView.findViewById(R.id.allordersstatus);
            allOrdersRequests = itemView.findViewById(R.id.allordersrequest);
            parentLayout = itemView.findViewById(R.id.PARENT);
            allOrdersPayBy = itemView.findViewById(R.id.allorderspayby);
            allOrdersCost = itemView.findViewById(R.id.allorderscost);

        }
    }
    public void updateList(List<Orders> newList){
        allOrdersList = new ArrayList<>();
        allOrdersList = newList;
        notifyDataSetChanged();
    }
}
