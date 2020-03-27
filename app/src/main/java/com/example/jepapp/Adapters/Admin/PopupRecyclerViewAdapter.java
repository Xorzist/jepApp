package com.example.jepapp.Adapters.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.R;

import java.util.ArrayList;

public class PopupRecyclerViewAdapter extends RecyclerView.Adapter<PopupRecyclerViewAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<String> data;

    public PopupRecyclerViewAdapter(Context mContext, ArrayList<String> data) {
        this.mContext = mContext;
        this.data = data;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_popup_card_item, parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    //View Holder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView, mTextView2;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTextView2 = (TextView) itemView.findViewById(R.id.tv_text2);
            mTextView = itemView.findViewById(R.id.tv_text);
        }
    }
}
