package com.example.jepapp.Adapters.Users;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.Ordertitle;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyOrdertitlesAdapter extends  RecyclerView.Adapter<MyOrdertitlesAdapter.OrderTitlesViewHolder> {
    private List<ArrayList<String>> myOrdertitles;
    private ArrayList<String> getOrdertitleArrayList = new ArrayList<>();
    private Context mCtx;
    private LayoutInflater inflater;


    public MyOrdertitlesAdapter(List<ArrayList<String>> myOrdertitles, Context mCtx) {
        this.myOrdertitles = myOrdertitles;
        inflater = LayoutInflater.from(mCtx);
        this.mCtx = mCtx;
        getOrdertitleArrayList = new ArrayList<>();
        for (int i = 0 ; i<myOrdertitles.size();i++) {
            for (int r = 0; r < myOrdertitles.get(i).size(); r++) {
                getOrdertitleArrayList.add(myOrdertitles.get(i).get(r));
                Log.e("getthethingsthem",myOrdertitles.get(i).get(r));
            }
        }

    }

    @Override
    public OrderTitlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
//        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.myordertitlelayout,parent,false);
        OrderTitlesViewHolder holder = new OrderTitlesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdertitlesAdapter.OrderTitlesViewHolder holder, int position) {

        final String item = getOrdertitleArrayList.get(position);
        holder.ordetitles.setText(item);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderTitlesViewHolder extends RecyclerView.ViewHolder {
       TextView ordetitles;
        public OrderTitlesViewHolder(@NonNull View itemView) {
            super(itemView);
            ordetitles = itemView.findViewById(R.id.mycustomerordertitles);
        }
    }
}
