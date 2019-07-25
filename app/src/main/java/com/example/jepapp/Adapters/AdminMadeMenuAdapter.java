package com.example.jepapp.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;

import java.util.List;


public class AdminMadeMenuAdapter extends RecyclerView.Adapter<AdminMadeMenuAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Admin_Made_Menu> madeMenuList;


    //getting the context and product list with constructor
    public AdminMadeMenuAdapter(Context mCtx, List<Admin_Made_Menu> madeMenuList) {
        this.mCtx = mCtx;
        this.madeMenuList = madeMenuList;

    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_make_menu_layout_admin, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        //getting the item of the specified position
        final Admin_Made_Menu item = madeMenuList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(item.getTitle());
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
        return madeMenuList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        LinearLayout parentLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.admin_menu_title);
            parentLayout = itemView.findViewById(R.id.parent_layout2);

        }

    }
}
