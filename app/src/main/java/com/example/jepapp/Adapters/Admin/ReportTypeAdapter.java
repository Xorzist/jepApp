package com.example.jepapp.Adapters.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Admin.DolItemspageforreport;
import com.example.jepapp.Activities.Admin.ItemAmtReport;
import com.example.jepapp.Activities.Admin.ItemSalesReport;
import com.example.jepapp.Activities.Admin.PerformanceReviewReport;
import com.example.jepapp.Models.ReportType;
import com.example.jepapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class ReportTypeAdapter extends RecyclerView.Adapter<ReportTypeAdapter.AllReportsViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    public List<ReportType> reportTypeList;
    private static DatabaseReference databaseReference;
    //private DatabaseReference myDBRef;




    //getting the context and product list with constructor
    public ReportTypeAdapter(Context mCtx, List<ReportType> ReportTypeList) {
        this.mCtx = mCtx;
        this.reportTypeList = ReportTypeList;



    }


    @Override
    public AllReportsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        //myDBRef = FirebaseDatabase.getInstance().getReference();
        View view = inflater.inflate(R.layout.reporttypelayout, null);
        ReportTypeAdapter.AllReportsViewHolder holder = new ReportTypeAdapter.AllReportsViewHolder(view);
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AllReportsViewHolder holder, final int position) {
        final ReportType reportType = reportTypeList.get(position);
        holder.Title.setText(reportType.getTitle());
        holder.description.setText(reportType.getDescription());
        holder.descriptionLayout.setVisibility(View.GONE);
        holder.Openallitems.setVisibility(View.GONE);
        if(position ==0){
            holder.Openallitems.setVisibility(View.VISIBLE);
        }
        holder.Openallitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Intent i = new Intent(mCtx, ItemAmtReport.class);
                i.putExtra("thismonth", String.valueOf(cal.get(Calendar.MONTH)+1));
                mCtx.startActivity(i);
            }
        });

        holder.openReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0){
                    Intent i = new Intent(mCtx, DolItemspageforreport.class);
                    mCtx.startActivity(i);

                }
                else if (position==1){
                    Intent i = new Intent(mCtx, ItemSalesReport.class);
                    mCtx.startActivity(i);

                }
                else if (position==2){
                    Intent i = new Intent(mCtx, PerformanceReviewReport.class);
                    mCtx.startActivity(i);
                }
            }
        });
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (holder.descriptionLayout.getVisibility()==View.VISIBLE){
                    holder.arrow.setRotation(0);
                    holder.descriptionLayout.setVisibility(View.GONE);

                }
                else if(holder.descriptionLayout.getVisibility()!= View.VISIBLE){
                    holder.arrow.setRotation(180);
                    holder.descriptionLayout.setVisibility(View.VISIBLE);
                }

            }
        });
    }




    @Override
    public int getItemCount() {
        return reportTypeList.size();
    }


     class AllReportsViewHolder extends RecyclerView.ViewHolder {
        TextView Title,description;
        Button openReport,Openallitems;
        ImageView arrow;
        LinearLayout descriptionLayout;
         ConstraintLayout parentLayout;


         public AllReportsViewHolder(View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.reportType);
            description=itemView.findViewById(R.id.reportdescription);
            arrow=itemView.findViewById(R.id.dropdowndesc);
            openReport = itemView.findViewById(R.id.openreport);
            Openallitems = itemView.findViewById(R.id.openreport2);
            descriptionLayout = itemView.findViewById(R.id.descriptionlayout);
            parentLayout = itemView.findViewById(R.id.reportLayout);

        }

    }


}
