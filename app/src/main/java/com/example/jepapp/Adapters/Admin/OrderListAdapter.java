package com.example.jepapp.Adapters.Admin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.OrderItem;
import com.example.jepapp.R;

import java.util.List;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<OrderItem> orderItemList;


    //getting the context and product list with constructor
    public OrderListAdapter(Context mCtx, List<OrderItem> orderItemList) {
        this.mCtx = mCtx;
        this.orderItemList = orderItemList;

    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_order_layout_admin, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        //getting the item of the specified position
        final OrderItem item = orderItemList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewPerson.setText(item.getReceiver());
        holder.textViewQuantity.setText(String.valueOf(item.getQuantity()));

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(item.getImage()));
//        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mCtx, "clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mCtx, class);
////                intent.putExtra("name", item.getTitle());
////                intent.putExtra("price", String.valueOf(item.getPrice()));
//                mCtx.startActivity(intent);
//
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return orderItemList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewPerson, textViewQuantity;
        ImageView imageView;
        LinearLayout parentLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.title);
            textViewPerson = itemView.findViewById(R.id.ingredients);
            imageView = itemView.findViewById(R.id.imageView);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            parentLayout = itemView.findViewById(R.id.parent_layoutbreakfast);

        }

    }
}
