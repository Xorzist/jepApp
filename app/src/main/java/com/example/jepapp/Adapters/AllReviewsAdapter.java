package com.example.jepapp.Adapters;

import android.content.Context;
import android.media.Image;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Comments;
import com.example.jepapp.Models.Reviews;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class AllReviewsAdapter extends RecyclerView.Adapter<AllReviewsAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Reviews> allreviewsList;

    public AllReviewsAdapter(Context mCtx, List<Reviews> allreviewsList){
        this.mCtx = mCtx;
        this.allreviewsList = allreviewsList;
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
        final Reviews item = allreviewsList.get(position);

        //binding the data with the viewholder views
        holder.allReviewsTitle.setText(String.valueOf(item.getTitle()));
        holder.allReviewsBody.setText(String.valueOf(item.getDescription()));
       // holder.allReviewsReplier.setVisibility(View.INVISIBLE);
        holder.date.setText("Date: " + String.valueOf(item.getDate()).toString());
        String topics = item.getReviewtopic();
        topics = topics.replaceAll(", $", "");
        holder.reviewtopics.setText(topics);
        if(item.getLiked().toLowerCase().equals("yes")){
            holder.likeordislike.setImageResource(R.drawable.likeshaded);
        }
        else if (item.getDisliked().toLowerCase().equals("yes")){
            holder.likeordislike.setImageResource(R.drawable.dislikeshaded);

        }else{
            holder.likeordislike.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return allreviewsList.size();
    }

    public void updateList(List<Reviews> newcommentList) {
        allreviewsList = new ArrayList<>();
        allreviewsList = newcommentList;
        notifyDataSetChanged();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView allReviewsTitle, allReviewsBody,date, allReviewsReplier,reviewtopics;
        ConstraintLayout parentreviewLayout;
        ImageView likeordislike;
        public ProductViewHolder(View itemView) {
            super(itemView);

            allReviewsTitle = itemView.findViewById(R.id.parenttitle);
            allReviewsBody = itemView.findViewById(R.id.parentdescription);
           // allReviewsReplier = itemView.findViewById(R.id.replier);
           // allReviewsBody.setMovementMethod(new ScrollingMovementMethod());
            date = itemView.findViewById(R.id.parentdate);
            likeordislike = itemView.findViewById(R.id.likeordislike);
            parentreviewLayout = itemView.findViewById(R.id.relativereviewLayout);
            reviewtopics = itemView.findViewById(R.id.review_topic);

        }
    }

}
