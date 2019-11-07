package com.example.jepapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Users.OrderPageActivity;
import com.example.jepapp.Models.FoodItem;
import com.example.jepapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<FoodItem> foodItemList;


    //getting the context and product list with constructor
    public FoodListAdapter(Context mCtx, List<FoodItem> foodItemList) {
        this.mCtx = mCtx;
        this.foodItemList = foodItemList;

    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.breakfast_products, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the item of the specified position
        final FoodItem item = foodItemList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewIngredients.setText(item.getIngredients());
        holder.textViewPrice.setText(String.valueOf(item.getPrice()));
        holder.textViewQuantity.setText(String.valueOf(item.getQuantity()));

        Picasso.with(mCtx)
                .load(item.getImage())
                .into(holder.imageView);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCtx, "clicked", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("title",item.getTitle());
                bundle.putString("price",String.valueOf(item.getPrice()));
                Intent intent = new Intent(mCtx, OrderPageActivity.class);
                //String l = holder.textViewTitle.
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return foodItemList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewIngredients, textViewPrice, textViewQuantity;
        ImageView imageView;
        LinearLayout parentLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.title);
            textViewIngredients = itemView.findViewById(R.id.ingredients);
            textViewPrice = itemView.findViewById(R.id.price);
            textViewQuantity = itemView.findViewById(R.id.quantity);
            imageView = itemView.findViewById(R.id.imageView);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }
}
