package com.example.jepapp.Fragments.User;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jepapp.Activities.Admin.ItemAmtReport;
import com.example.jepapp.Activities.Users.weekly_expenditure;
import com.example.jepapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class customer_Report  extends Fragment {

    private LinearLayout enterstart,enterend;
    private EditText calendartstart,calendarend;
    DatePickerDialog picker;
    private TextView description;
    private Button generate;


    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.customer_reports, container, false);
        enterstart = rootView.findViewById(R.id.enterstartdate);
        enterend = rootView.findViewById(R.id.enterenddate);
        calendartstart = rootView.findViewById(R.id.calendarstarttext);
        calendarend = rootView.findViewById(R.id.calendarendtext);
        description = rootView.findViewById(R.id.customer_report_desc);
        generate = rootView.findViewById(R.id.openweeklyexpense);
        description.setText("A report can be created to display your overall expenditure for a selected range of dates");
        enterstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showCalendar(calendartstart);
            }
        });
        enterend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar(calendarend);
            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calendartstart.getText().toString().equals("") || calendarend.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter an appropriate start and end date", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.e("calendarstartdate",calendartstart.getText().toString() );
                    Intent i = new Intent(getContext(), weekly_expenditure.class);
                    i.putExtra("startdate",calendartstart.getText().toString() );
                    i.putExtra("enddate",calendarend.getText().toString());
                    startActivity(i);
                }
            }
        });

        return rootView;
    }

    private void showCalendar(final EditText textfield) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(getActivity(),
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



}
