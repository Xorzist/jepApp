package com.example.jepapp.Adapters.Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;

import java.util.List;

public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Orders> allOrdersList;

    public AllOrdersAdapter(Context mCtx, List<Orders> allOrdersList){
        this.mCtx = mCtx;
        this.allOrdersList = allOrdersList;
    }
    @Override
    public AllOrdersAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.allorderslayout,null);
        return new AllOrdersAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Orders item = allOrdersList.get(position);

        //binding the data with the viewholder views
        holder.allOrdersTitle.setText(item.getOrdertitle());
        holder.allOrdersCustomer.setText(String.valueOf(item.getOrderID()));
        holder.allOrdersQuantity.setText(String.valueOf(item.getQuantity()));

    }


    @Override
    public int getItemCount() {
        return allOrdersList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView allOrdersTitle, allOrdersCustomer, allOrdersQuantity, textViewPrice;
        // LinearLayout parentLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            allOrdersTitle = itemView.findViewById(R.id.allorderstitle);
            allOrdersCustomer = itemView.findViewById(R.id.allorderscustomer);
            allOrdersQuantity = itemView.findViewById(R.id.allordersquantity);

            //parentLayout = itemView.findViewById(R.id.)

        }
    }
}
