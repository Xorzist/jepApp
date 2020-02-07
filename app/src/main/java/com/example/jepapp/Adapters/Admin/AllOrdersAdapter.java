package com.example.jepapp.Adapters.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.ProductViewHolder> implements Filterable {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private static List<Orders> allOrdersList;
    private static List<Orders> allOrdersFiltered;
    private AllOrdersAdapterListener listener;



    public AllOrdersAdapter(Context mCtx, List<Orders> allOrdersList, AllOrdersAdapterListener listener) {
        this.mCtx = mCtx;
        this.allOrdersList = allOrdersList;
        this.listener= listener;
        this.allOrdersFiltered = allOrdersList;
    }



    @Override
    public AllOrdersAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.allorderslayout, null);
        return new AllOrdersAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Orders item = allOrdersFiltered.get(position);

        //binding the data with the viewholder views
        holder.allOrdersTitle.setText(item.getOrdertitle());
        holder.allOrdersCustomer.setText(String.valueOf(item.getUsername()));
        holder.allOrdersQuantity.setText(String.valueOf(item.getQuantity()));
        holder.allOrdersPaymentType.setText(String.valueOf(item.getPayment_type()));
        if (holder.allOrdersPaymentType.getText().equals("cancelled")) {
            holder.allOrderscancel.setVisibility(View.VISIBLE);
        } else {
            holder.allOrderscancel.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return allOrdersFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allOrdersFiltered= allOrdersList;
                } else {
                    List<Orders> filteredList = new ArrayList<>();
                    for (Orders row : allOrdersList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getOrdertitle().toLowerCase().contains(charString.toLowerCase()) || row.getUsername().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    allOrdersFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = allOrdersFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                allOrdersFiltered = (ArrayList<Orders>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView allOrdersTitle, allOrdersCustomer, allOrdersQuantity, allOrdersPaymentType;
        LinearLayout parentLayout;
        ImageView allOrderscancel;

        public ProductViewHolder(View itemView) {
            super(itemView);

            allOrdersTitle = itemView.findViewById(R.id.allorderstitle);
            allOrdersCustomer = itemView.findViewById(R.id.allorderscustomer);
            allOrdersQuantity = itemView.findViewById(R.id.allordersquantity);
            allOrdersPaymentType = itemView.findViewById(R.id.allorderspaymenttype);
            allOrderscancel = itemView.findViewById(R.id.cancelled_image);
            parentLayout = itemView.findViewById(R.id.parent_layoutorder);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    listener.onItemSelected(allOrdersFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface AllOrdersAdapterListener {
        void onItemSelected(Orders order);
    }
}
