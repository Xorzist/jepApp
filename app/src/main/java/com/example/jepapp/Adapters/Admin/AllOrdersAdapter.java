package com.example.jepapp.Adapters.Admin;

import android.content.Context;
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
        View view = inflater.inflate(R.layout.allorderslayout, null);
        return new AllOrdersAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Orders item = allOrdersList.get(position);

        //binding the data with the viewholder views
        holder.allOrdersTitle.setText(item.getOrdertitle());
        holder.allOrdersCustomer.setText(String.valueOf(item.getUsername()));
        holder.allOrdersCost.setText(String.valueOf(item.getCost()));
        holder.allOrdersQuantity.setText(String.valueOf(item.getQuantity()));
        holder.allOrdersPayBy.setText(String.valueOf(item.getPaidby()));
        holder.allOrdersPaymentType.setText(String.valueOf(item.getPayment_type()));
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

        TextView allOrdersTitle, allOrdersCustomer, allOrdersQuantity, allOrdersPaymentType, allOrdersPayBy, allOrdersCost;
        LinearLayout parentLayout;
        ImageView allOrderscancel;

        public ProductViewHolder(View itemView) {
            super(itemView);

            allOrdersTitle = itemView.findViewById(R.id.allorderstitle);
            allOrdersCustomer = itemView.findViewById(R.id.allorderscustomer);
            allOrdersQuantity = itemView.findViewById(R.id.allordersquantity);
            allOrdersPaymentType = itemView.findViewById(R.id.allorderspaymenttype);
            allOrderscancel = itemView.findViewById(R.id.cancelled_image);
            parentLayout = itemView.findViewById(R.id.parent_layoutorder);
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
