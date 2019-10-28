package com.example.jepapp.Adapters;


import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jepapp.Activities.Users.OrderPageActivity;
import com.example.jepapp.Models.FoodItem;
import com.example.jepapp.R;

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
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        //getting the item of the specified position
        final FoodItem item = foodItemList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewShortDesc.setText(item.getShortdesc());
        holder.textViewRating.setText(String.valueOf(item.getRating()));
        holder.textViewPrice.setText(String.valueOf(item.getPrice()));

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(item.getImage()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCtx, "clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mCtx, OrderPageActivity.class);
                intent.putExtra("name", item.getTitle());
                intent.putExtra("price", String.valueOf(item.getPrice()));
                mCtx.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return foodItemList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;
        LinearLayout parentLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageView);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }
}
