package com.example.jepapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jepapp.BreakfastList;
import com.example.jepapp.LunchList;
import com.example.jepapp.Models.Genre;
import com.example.jepapp.R;

import java.util.List;


public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {

    private Context mContext;
    private List<Genre> musicGenres;
    private Genre bg;
    private String uname;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public GenreAdapter(Context mContext, List<Genre> musicGenres, String Username) {
        this.mContext = mContext;
        this.musicGenres = musicGenres;
        this.uname=Username;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menutype_row, parent, false);
        final TextView genreView = itemView.findViewById(R.id.title);
        final ImageView musicCover = itemView.findViewById(R.id.thumbnail);
        musicCover.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View itemView){

                String genre=genreView.getText().toString();

                if (genre == "Breakfast"){
                    Intent intent=new Intent(itemView.getContext(), BreakfastList.class);
                    itemView.getContext().startActivity(intent);
                }
                if (genre == "Lunch"){
                    Intent intent=new Intent(itemView.getContext(), LunchList.class);
                    itemView.getContext().startActivity(intent);
                }

            }
        });

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Genre genre = musicGenres.get(position);
        final String tit=genre.getName();
        holder.title.setText(genre.getName());

        Glide.with(mContext).load(genre.getThumbnail()).into(holder.thumbnail);

    }



    @Override
    public int getItemCount() {
        return musicGenres.size();
    }


}
