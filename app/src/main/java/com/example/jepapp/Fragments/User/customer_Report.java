package com.example.jepapp.Fragments.User;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.jepapp.Activities.Users.pie_weekly_expenditure;
import com.example.jepapp.Activities.Users.weekly_expenditure;
import com.example.jepapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class customer_Report  extends Fragment {

    private LinearLayout enterstart,enterend,pieenterstart,pieenterend;
    private EditText calendartstart,calendarend,piecalendarstart,piecalendarend;
    DatePickerDialog picker;
    private TextView description,piedescription;
    private Button generate,piegenerate,day7generate,day7generatepie;
    private Date date;
    private String newdate;
    private ArrayList<String> dates;


    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.customer_reports, container, false);
        enterstart = rootView.findViewById(R.id.enterstartdate);
        pieenterstart = rootView.findViewById(R.id.pieenterstartdate);
        enterend = rootView.findViewById(R.id.enterenddate);
        pieenterend = rootView.findViewById(R.id.pieenterenddate);
        calendartstart = rootView.findViewById(R.id.calendarstarttext);
        piecalendarstart = rootView.findViewById(R.id.piecalendarstarttext);
        calendarend = rootView.findViewById(R.id.calendarendtext);
        piecalendarend = rootView.findViewById(R.id.piecalendarendtext);
        description = rootView.findViewById(R.id.customer_report_desc);
        piedescription = rootView.findViewById(R.id.piecustomer_report_desc);
        generate = rootView.findViewById(R.id.openweeklyexpense);
        piegenerate = rootView.findViewById(R.id.pieopenweeklyexpense);
        day7generate = rootView.findViewById(R.id.day7report);
        day7generatepie = rootView.findViewById(R.id.day7reportpie);



        description.setText("A report can be created to display your overall expenditure for a selected range of dates");
        piedescription.setText("A report can be created to display your overall expenses per card and cash purchases");
        enterstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showCalendar(calendartstart);
            }
        });
        pieenterstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar(piecalendarstart);
            }
        });
        enterend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar(calendarend);
            }
        });
        pieenterend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar(piecalendarend);
            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calendartstart.getText().toString().equals("") || calendarend.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter an appropriate start and end date", Toast.LENGTH_SHORT).show();
                }
                else if(datediff(calendartstart.getText().toString(), calendarend.getText().toString()))
                {
                    Toast.makeText(getActivity(), "Date range needs to be within 2 to 30 days", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(getContext(), weekly_expenditure.class);
                    i.putExtra("startdate",calendartstart.getText().toString() );
                    i.putExtra("enddate",calendarend.getText().toString());
                    startActivity(i);
                }
            }
        });
        piegenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(piecalendarstart.getText().toString().equals("") || piecalendarend.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter an appropriate start and end date", Toast.LENGTH_SHORT).show();
                }else if(datediff(piecalendarstart.getText().toString(), piecalendarend.getText().toString()))
                {
                    Toast.makeText(getActivity(), "Date range needs to be within 2 to 30 days", Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent i = new Intent(getContext(), pie_weekly_expenditure.class);
                    i.putExtra("startdate",piecalendarstart.getText().toString() );
                    i.putExtra("enddate",piecalendarend.getText().toString());
                    startActivity(i);
                }

            }
        });
        day7generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                newdate = new SimpleDateFormat("dd-MM-yyyy").format(date);
                //test subtract days method
                dates = new ArrayList<>();
                dates.add(newdate);
                dates.addAll(subtractDays(date));
                Intent i = new Intent(getContext(), weekly_expenditure.class);
                i.putExtra("startdate",dates.get(6));
                i.putExtra("enddate",dates.get(0));
                startActivity(i);
            }
        });
        day7generatepie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                newdate = new SimpleDateFormat("dd-MM-yyyy").format(date);
                //test subtract days method
                dates = new ArrayList<>();
                dates.add(newdate);
                dates.addAll(subtractDays(date));

                Intent i = new Intent(getContext(), pie_weekly_expenditure.class);
                i.putExtra("startdate", dates.get(6));
                i.putExtra("enddate",(dates.get(0)));
                startActivity(i);

            }
        });

        return rootView;
    }

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


       if(diff>30||diff==0){
           return true;
       }else{
           return  false;
       }

    }

    private void showCalendar(final EditText textfield) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(getActivity(),R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                        String dateString = dateformat.format(calendar.getTime());
                        //newdate = new SimpleDateFormat("dd-MM-yyyy").format(dayOfMonth);
                        textfield.setText(dateString);
                    }
                }, year, month, day);
        picker.show();

    }
    public static ArrayList<String> subtractDays(Date date) {
        int i=1;
        GregorianCalendar cal = new GregorianCalendar();
        ArrayList<String> subtracteddates = new ArrayList<>();
        while(i<=6){
            cal.setTime(date);
            cal.add(Calendar.DATE, -i);
            String newdate = new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
            subtracteddates.add(newdate);
            i+=1;
        }

        return subtracteddates;
    }



}
