package com.example.jepapp.Adapters.Users;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.Ordertitle;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Orders> myOrdersList;
    private MyOrdertitlesAdapter ordertitlesadapter;
    private  List<ArrayList<String>> ordertitles;
    private ArrayList getOrdertitleArrayList;


    //getting the context and product list with constructor
//    public MyOrdersAdapter(Context mCtx, List<Orders> myOrdersList) {
//        this.mCtx = mCtx;
//        this.myOrdersList = myOrdersList;
//        this.ordertitles =ordertitles;
//    }
    public MyOrdersAdapter(Context mCtx, List<Orders> myOrdersList) {
        this.mCtx = mCtx;
        this.myOrdersList = myOrdersList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.customer_orders_recyclerlayout,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder1, int position) {
        //getting the item of the specified position
        final Orders item = myOrdersList.get(position);
        holder1.myordertype.setText(item.getType());

        holder1.myOrdersCost.setText(String.valueOf(item.getCost()).toString());
        holder1.myOrdersPaymentType.setText(String.valueOf(item.getPayment_type()));
        holder1.myorderdate.setText(item.getDate());
        holder1.myorderstatus.setText(item.getStatus());
        ArrayList<String> orderdescription = item.getOrdertitle();
        String descriptionstring = "";
        for (String s : orderdescription){
            descriptionstring += s +"\n";
        }
        holder1.myordertext.setText(descriptionstring);


    }


    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }



    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView myordertype, myOrdersCost, myOrdersPaymentType,myorderdate,myorderstatus,myordertext;
        LinearLayout parentLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            myordertype = itemView.findViewById(R.id.customerordertype);
            myOrdersCost = itemView.findViewById(R.id.customerordercost);
            myOrdersPaymentType = itemView.findViewById(R.id.customerpaymentype);
            myorderstatus = itemView.findViewById(R.id.customerorderstatus);
            myorderdate = itemView.findViewById(R.id.customerorderdate);
            myordertext = itemView.findViewById(R.id.customerorderitems);

        }
    }
}
