package com.example.jepapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jepapp.Models.MItems;
import com.example.jepapp.Models.OrderItem;
import com.example.jepapp.R;

import java.util.List;

public class AllitemsAdapter extends RecyclerView.Adapter<AllitemsAdapter.AllitemsViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<MItems> MenuItemList;


    //getting the context and product list with constructor
    public AllitemsAdapter(Context mCtx, List<MItems> MenuItemList) {
        this.mCtx = mCtx;
        this.MenuItemList = MenuItemList;

    }


    @Override
    public AllitemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.all_menu_items_recylayout, null);
        AllitemsViewHolder holder = new AllitemsViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(AllitemsAdapter.AllitemsViewHolder holder, final int position) {
        //getting the item of the specified position
        final MItems item = MenuItemList.get(position);
        //binding the data with the viewholder views
        holder.Title.setText(item.getTitle());
        //holder.itempics.setImageDrawable();
        holder.deletbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return MenuItemList.size();
    }


    class AllitemsViewHolder extends RecyclerView.ViewHolder {
        TextView Title ;
        ImageView deletbtn,itempics;
        LinearLayout parentLayout;

        public AllitemsViewHolder(View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.itemtitle);
            deletbtn=itemView.findViewById(R.id.deleteitem);
            itempics=itemView.findViewById(R.id.itempic);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }
}
