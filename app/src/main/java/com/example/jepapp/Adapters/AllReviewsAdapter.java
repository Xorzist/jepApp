package com.example.jepapp.Adapters;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Comments;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class AllReviewsAdapter extends RecyclerView.Adapter<AllReviewsAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Comments> allCommentList;

    public AllReviewsAdapter(Context mCtx, List<Comments> allCommentList){
        this.mCtx = mCtx;
        this.allCommentList = allCommentList;
    }
    @Override
    public AllReviewsAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_discussion_parentview,null);
        return new AllReviewsAdapter.ProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Comments item = allCommentList.get(position);

        //binding the data with the viewholder views
        holder.allReviewsTitle.setText(String.valueOf(item.getTitle()));
        holder.allReviewsBody.setText(String.valueOf(item.getComment()));
        holder.date.setText(String.valueOf(item.getDate()));

    }

    @Override
    public int getItemCount() {
        return allCommentList.size();
    }

    public void updateList(List<Comments> newcommentList) {
        allCommentList = new ArrayList<>();
        allCommentList = newcommentList;
        notifyDataSetChanged();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView allReviewsTitle, allReviewsBody,date;
        LinearLayout parentLayout;
        public ProductViewHolder(View itemView) {
            super(itemView);

            allReviewsTitle = itemView.findViewById(R.id.parenttitle);
            allReviewsBody = itemView.findViewById(R.id.parentdescription);
            allReviewsBody.setMovementMethod(new ScrollingMovementMethod());
            date = itemView.findViewById(R.id.parentdate);
            parentLayout = itemView.findViewById(R.id.parent_layoutorder);

        }
    }

}
