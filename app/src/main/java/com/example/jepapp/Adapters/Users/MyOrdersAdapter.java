package com.example.jepapp.Adapters.Users;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;

import java.util.List;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Orders> myOrdersList;

    //getting the context and product list with constructor
    public MyOrdersAdapter(Context mCtx, List<Orders> myOrdersList) {
        this.mCtx = mCtx;
        this.myOrdersList = myOrdersList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.myorderslayout,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder1, int position) {
        //getting the item of the specified position
        final Orders item = myOrdersList.get(position);

        //binding the data with the viewholder views
        holder1.myOrdersTitle.setText(item.getOrdertitle());
        holder1.myOrdersCost.setText(String.valueOf(item.getCost()));
        holder1.myOrdersQuantity.setText(String.valueOf(item.getQuantity()));
        holder1.myOrdersPaymentType.setText(String.valueOf(item.getPayment_type()));

//        holder1.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView myOrdersTitle, myOrdersCost, myOrdersQuantity, myOrdersPaymentType;
        LinearLayout parentLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            myOrdersTitle = itemView.findViewById(R.id.myordertitle);
            myOrdersCost = itemView.findViewById(R.id.myorderscost);
            myOrdersQuantity = itemView.findViewById(R.id.myordersquantity);
            myOrdersPaymentType = itemView.findViewById(R.id.myPaymentType);

            parentLayout = itemView.findViewById(R.id.parent_layoutorder);

        }
    }
}
