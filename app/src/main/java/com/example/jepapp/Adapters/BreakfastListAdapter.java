package com.example.jepapp.Adapters;


import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;


import com.example.jepapp.Models.BreakfastItem;
import com.example.jepapp.R;

import java.util.List;



public class BreakfastListAdapter extends RecyclerView.Adapter<BreakfastListAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<BreakfastItem> breakfastItemList;

    //getting the context and product list with constructor
    public BreakfastListAdapter(Context mCtx, List<BreakfastItem> breakfastItemList) {
        this.mCtx = mCtx;
        this.breakfastItemList = breakfastItemList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.breakfast_products, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the item of the specified position
        BreakfastItem item = breakfastItemList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewShortDesc.setText(item.getShortdesc());
        holder.textViewRating.setText(String.valueOf(item.getRating()));
        holder.textViewPrice.setText(String.valueOf(item.getPrice()));

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(item.getImage()));

    }


    @Override
    public int getItemCount() {
        return breakfastItemList.size();
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
