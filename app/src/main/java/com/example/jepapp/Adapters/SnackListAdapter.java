package com.example.jepapp.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jepapp.Models.SnackItem;
import com.example.jepapp.R;

import java.util.List;


public class SnackListAdapter extends RecyclerView.Adapter<SnackListAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<SnackItem> snackItemList;

    //getting the context and product list with constructor
    public SnackListAdapter(Context mCtx, List<SnackItem> snackItemList) {
        this.mCtx = mCtx;
        this.snackItemList = snackItemList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.lunch_products, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder1, int position) {
        //getting the item of the specified position
        SnackItem item = snackItemList.get(position);

        //binding the data with the viewholder views
        holder1.textViewTitle.setText(item.getTitle());
        holder1.textViewShortDesc.setText(item.getShortdesc());
        holder1.textViewRating.setText(String.valueOf(item.getRating()));
        holder1.textViewPrice.setText(String.valueOf(item.getPrice()));

        holder1.imageView.setImageDrawable(mCtx.getResources().getDrawable(item.getImage()));

    }


    @Override
    public int getItemCount() {
        return snackItemList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
