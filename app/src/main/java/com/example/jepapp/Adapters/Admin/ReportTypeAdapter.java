package com.example.jepapp.Adapters.Admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Activities.Admin.MonthlyIncomeReport;
import com.example.jepapp.Activities.Admin.DailyIncomeReport;
import com.example.jepapp.Activities.Admin.PerformancePieReport;
import com.example.jepapp.Activities.Admin.SingleItemsReportActivity;
import com.example.jepapp.Activities.Admin.ItemAmtReport;
import com.example.jepapp.Activities.Admin.WeeksIncomeReport;
import com.example.jepapp.Models.ReportType;
import com.example.jepapp.R;
import org.jetbrains.annotations.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReportTypeAdapter extends RecyclerView.Adapter<ReportTypeAdapter.AllReportsViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    public List<ReportType> reportTypeList;



    //getting the context and product list with constructor
    public ReportTypeAdapter(Context mCtx, List<ReportType> ReportTypeList) {
        this.mCtx = mCtx;
        this.reportTypeList = ReportTypeList;



    }


    @NotNull
    @Override
    public AllReportsViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.reporttypelayout, null);
        ReportTypeAdapter.AllReportsViewHolder holder = new ReportTypeAdapter.AllReportsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AllReportsViewHolder holder, final int position) {
        final ReportType reportType = reportTypeList.get(position);
        holder.Title.setText(reportType.getTitle());
        holder.description.setText(reportType.getDescription());
        //hiding linear layouts with buttons
        holder.descriptionLayout.setVisibility(View.GONE);
        holder.Openallitems.setVisibility(View.GONE);
        holder.openweeklyReport.setVisibility(View.GONE);
        holder.openReviewsReport.setVisibility(View.GONE);
        holder.openMonthlyIncomeReport.setVisibility(View.GONE);
        holder.openReport.setVisibility(View.GONE);
        holder.openSevenDays.setVisibility(View.GONE);
        holder.openCustomDays.setVisibility(View.GONE);

        //setting button visibility based on which item is clicked
        if(position ==0){
            holder.Openallitems.setVisibility(View.VISIBLE);
            holder.openReport.setVisibility(View.VISIBLE);
        }else if(position == 1){
            holder.openCustomDays.setVisibility(View.VISIBLE);
            holder.openSevenDays.setVisibility(View.VISIBLE);
            holder.openMonthlyIncomeReport.setVisibility(View.VISIBLE);
        }else{
            holder.openReviewsReport.setVisibility(View.VISIBLE);
        }
        //launches Item Report Interface
        holder.Openallitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Intent i = new Intent(mCtx, ItemAmtReport.class);
                i.putExtra("thismonth", String.valueOf(cal.get(Calendar.MONTH)+1));
                mCtx.startActivity(i);
            }
        });
        //launches Single Item Report Interface
        holder.openReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(mCtx, SingleItemsReportActivity.class);
                    mCtx.startActivity(i);
            }
        });
        //launches Monthly Income Report Interface
        holder.openMonthlyIncomeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, MonthlyIncomeReport.class);
                mCtx.startActivity(i);
            }
        });
        //launches Performance Report Interface
        holder.openReviewsReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Intent i = new Intent(mCtx, PerformancePieReport.class);
                i.putExtra("thismonth", String.valueOf(cal.get(Calendar.MONTH)+1));
                mCtx.startActivity(i);
            }
        });
        //displays report description on click
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
        //launches Weekly Income Report Interface
        holder.openSevenDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, WeeksIncomeReport.class);
                mCtx.startActivity(intent);
            }
        });
        //launches Daily Income Report Interface
        holder.openCustomDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // displays custom dialog that allows users to choose calendar dates
                final Dialog dialog = new Dialog(mCtx);
                dialog.setContentView(R.layout.choose_dates_alertdialog);
                final EditText calendartstart,calendarend;
                final LinearLayout enterstart,enterend;
                calendartstart = dialog.findViewById(R.id.calendarstarttext);
                calendarend = dialog.findViewById(R.id.calendarendtext);
                enterstart = dialog.findViewById(R.id.enterstartdate);
                enterend = dialog.findViewById(R.id.enterenddate);

                enterstart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog picker;
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(mCtx, R.style.datepicker,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, monthOfYear, dayOfMonth);
                                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                                        String dateString = dateformat.format(calendar.getTime());
                                        calendartstart.setText(dateString);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });

                enterend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog picker;
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(mCtx,R.style.datepicker,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, monthOfYear, dayOfMonth);
                                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                                        String dateString = dateformat.format(calendar.getTime());
                                        calendarend.setText(dateString);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });

                Button dialogButton =  dialog.findViewById(R.id.opencustomreport);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //checks that the date range selected is appropriate
                        if(calendartstart.getText().toString().equals("") || calendarend.getText().toString().equals("")){
                            Toast.makeText(mCtx, "Please enter an appropriate start and end date", Toast.LENGTH_SHORT).show();
                        }
                        else if(datediff(calendartstart.getText().toString(), calendarend.getText().toString()))
                        {
                            Toast.makeText(mCtx, "Date range needs to be within 31 days", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //sends data to Daily Income Report class and launches interface
                            Intent i = new Intent(mCtx, DailyIncomeReport.class);
                            i.putExtra("startdate",calendartstart.getText().toString() );
                            i.putExtra("enddate",calendarend.getText().toString());
                            mCtx.startActivity(i);
                        }
                        dialog.dismiss();

                    }
                });
                dialog.show();

            }
        });
    }




    @Override
    public int getItemCount() {
        return reportTypeList.size();
    }


     class AllReportsViewHolder extends RecyclerView.ViewHolder {
        TextView Title,description;
        Button openReport,Openallitems, openweeklyReport,openMonthlyIncomeReport, openReviewsReport, openSevenDays, openCustomDays;
        ImageView arrow;
        LinearLayout descriptionLayout;
         ConstraintLayout parentLayout;


         public AllReportsViewHolder(View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.reportType);
            description=itemView.findViewById(R.id.reportdescription);
            arrow=itemView.findViewById(R.id.dropdowndesc);
            openReport = itemView.findViewById(R.id.openreport);
            Openallitems = itemView.findViewById(R.id.openallitemsreport);
            openweeklyReport = itemView.findViewById(R.id.weeklyreport);
            descriptionLayout = itemView.findViewById(R.id.descriptionlayout);
            openMonthlyIncomeReport = itemView.findViewById(R.id.monthlyIncome);
            openSevenDays = itemView.findViewById(R.id.sevendaysreport);
            openCustomDays = itemView.findViewById(R.id.choosedaysreport);
            openReviewsReport = itemView.findViewById(R.id.performancereport);
            parentLayout = itemView.findViewById(R.id.reportLayout);

        }

    }
    //get all the dates that occur between the selected start and end dates
    private boolean datediff(String datestart, String datend) {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        Date firstDate = null;
        try {
            firstDate = dateformat.parse(datestart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date secondDate = null;
        try {
            secondDate = dateformat.parse(datend);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        //only allow a maximum of 31 days to be selected and a minimum of 1
        if(diff>31||diff==0){
            return true;
        }else{
            return  false;
        }

    }




}
