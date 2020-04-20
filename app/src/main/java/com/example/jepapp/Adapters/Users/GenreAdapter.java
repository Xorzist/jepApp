package com.example.jepapp.Adapters.Users;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jepapp.Activities.Users.BreakfastList;
import com.example.jepapp.Activities.Users.LunchList;
import com.example.jepapp.Models.Cut_Off_Time;
import com.example.jepapp.Models.Genre;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {

    private Context mContext;
    private List<Genre> musicGenres;
    private Genre bg;
    private String uname;
    private DatabaseReference referencecutofftime;
    SimpleDateFormat parseFormat;
    private ArrayList<Cut_Off_Time> cuttoftimes = new ArrayList<>();
    DateFormat inputFormat;
    private SimpleDateFormat SimpleDateFormat,simpleTimeFormat;
    private Date datenow;
    private String breakfastapptime,lunchapptime;



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
        referencecutofftime = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");
        SimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleTimeFormat = new SimpleDateFormat("HH:mm");
        parseFormat = new SimpleDateFormat("hh:mm a");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm a.SSSX");
        datenow = new Date();

        //Method to get the  breakfast cut off time set by the admin
        //Method to get the lunch cut off time set by the admin

        Cutofftimesgetter();

        musicCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View itemView) {
                Cutofftimesgetter();

                String genre = genreView.getText().toString();

                if (genre == "Breakfast") {
                    try {
                        //If the user tries to access the menu after cut off time
                        Date timenow = simpleTimeFormat.parse(simpleTimeFormat.format(datenow));
                        Date bapptime = simpleTimeFormat.parse(breakfastapptime);
                        Date startime = simpleTimeFormat.parse("06:00");
                        if (timenow.after(bapptime) || timenow.before(startime)) {
                            new AlertDialog.Builder(mContext,R.style.datepicker)
                                    .setTitle("Orders Cut of Time")
                                    .setMessage("Sorry,the time for ordering breakfast has passed")
                                    .setPositiveButton("Okay", null)
                                    .setIcon(R.drawable.adminprofile)
                                    .show();

                        } else {
                            Intent intent = new Intent(itemView.getContext(), BreakfastList.class);
                            itemView.getContext().startActivity(intent);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                if (genre == "Lunch") {

                    try {
                        //If the user tries to access the menu after cut off time
                        Date timenow = simpleTimeFormat.parse(simpleTimeFormat.format(datenow));
                        Date lunchtime = simpleTimeFormat.parse(lunchapptime);
                        Date startime = simpleTimeFormat.parse("06:00");
                        if (timenow.after(lunchtime) || timenow.before(startime)) {
                            new AlertDialog.Builder(mContext,R.style.datepicker)
                                    .setTitle("Orders Cut of Time")
                                    .setMessage("Sorry,the time for ordering Lunch has passed")
                                    .setPositiveButton("Okay", null)
                                    .setIcon(R.drawable.adminprofile)
                                    .show();

                        } else {
                            Intent intent = new Intent(itemView.getContext(), LunchList.class);
                            itemView.getContext().startActivity(intent);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        return new MyViewHolder(itemView);
    }

    private void Cutofftimesgetter() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Getting Cut Off Times");
        progressDialog.show();
        referencecutofftime.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cut_Off_Time cuttofftime = dataSnapshot.getValue(Cut_Off_Time.class);
                    cuttoftimes.add(cuttofftime);

                }
                //Assign the breakfast and lunch times respectively straight from the database
                String dbbreakfasttime = cuttoftimes.get(0).getTime();
                String dblunchtime = cuttoftimes.get(1).getTime();

                try {
                    breakfastapptime = simpleTimeFormat.format(parseFormat.parse(dbbreakfasttime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    lunchapptime = simpleTimeFormat.format(parseFormat.parse(dblunchtime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("formatted breakfast!!", (breakfastapptime));
                Log.e("formatted breakfast!!", (lunchapptime));
                progressDialog.cancel();
                progressDialog.dismiss();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
