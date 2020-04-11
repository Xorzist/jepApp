package com.example.jepapp.Activities.Admin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.components.Description;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemAmtReport extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    List<com.example.jepapp.Models.Orders> allorderslist;



    ArrayList<String>allordertiitles;
    private ProgressDialog progressDialog;
    private Description g;
    private DatabaseReference databaseReferencebreakfast;
    private DatabaseReference databaseReferencelunch;
    List<DataEntry> entries,entries2;
    AnyChartView anyChartView;
    Cartesian cartesian;
    Spinner monthSpinner;
    private String month;
    private String firstchar;
    private String intentmonth;
    private String[] monthlist;
    private boolean userIsInteracting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_amt_report);
        monthSpinner = findViewById(R.id.monthselect);
        monthlist = new String[]{"January","February","March","April","May",
        "June","July","August","September","October","November","December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthlist);
        monthSpinner.setAdapter(adapter);

        allorderslist = new ArrayList<>();
        allordertiitles = new ArrayList<>();
        entries = new ArrayList<>();
        progressDialog = new ProgressDialog(getApplicationContext());
          anyChartView =  findViewById(R.id.newpie);
         cartesian = AnyChart.column();
        month = getIntent().getExtras().getString("thismonth");
        //Set spinner as current month
        monthSpinner.setSelection(Integer.parseInt(month)-1);

        mAuth = FirebaseAuth.getInstance();
        //Function to calculate the calendar month based on the item selected in the spinner
       monthcalculator();
        //dbreference for breakdast orders
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
        DoBreakfastOrdersQuery(month);
        //dbreference for lunch orders
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
        DoLunchOrdersQuery(month);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSpinner.setSelection(position);
                if(userIsInteracting) {
                    //check if the user has interacted with the spinner
                    Log.e(" Itemselectcalled ", month);
                    Intent i = new Intent(ItemAmtReport.this, ItemAmtReport.class);
                    i.putExtra("thismonth", String.valueOf(monthSpinner.getSelectedItemPosition() + 1));
                    finish();
                    startActivity(i);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ///Do nothing
            }
        });


    }

    private void monthcalculator() {
        //This function will calculate the month as a digit based on the user selected month
        //from the spinner widget
        //month = String.valueOf(monthSpinner.getSelectedItemPosition()+1);
        Log.e(" month show ", month);
        firstchar= String.valueOf(month.charAt(0));
        Log.e(" first ", firstchar);
        if((firstchar.equals('1')==false)){
            //set the month value as the selected spinner month
            month = addChar(month, '0',0);
            Log.e(" real month show ", month);

        }
    }

    private void AssignData() {
        //This function assigns values to variables to produce a graph
        Log.e("AssignData1: ","Called" );

        Set<String> uniquelabels = new HashSet<>(allordertiitles);

        for (String key : uniquelabels){
            Log.e("forloopcall: ","Called" );
            Log.e( "AssignData: " ,key);
            entries.add(new ValueDataEntry(key,Collections.frequency(allordertiitles,key)));
        }
        
        Column column = cartesian.column(entries);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("Amount:{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Item Report for the month of "+monthlist[Integer.parseInt(month)-1]);

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Menu Items");
        cartesian.yAxis(0).title("Amount Bought");

        anyChartView.setChart(cartesian);


    }


    private void DoBreakfastOrdersQuery(final String thismonth) {
        getAllordertiitles().clear();
        //This function will assign the orders of the current user to a list
        final ProgressDialog progressDialog1 = new ProgressDialog(ItemAmtReport.this);
        progressDialog1.setMessage("Getting My Breakfast Orders");
        progressDialog1.show();

        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders breakfastitems = dataSnapshot.getValue(Orders.class);
                    String mydate = breakfastitems.getDate();
                    String [] dateParts = mydate.split("-");
                    String numbermonth = dateParts[1];
                    Log.e("breakfastnumbermonth",numbermonth );
                    if (numbermonth.equals(thismonth)){
                        for (String s : breakfastitems.getOrdertitle()){
                            //Retrieve number value only between the parentheses
                            String number = s.substring(s.indexOf("(")+2,s.indexOf(")"));
                            for (int i = 0; i <Integer.valueOf(number) ; i++) {
                                String noparantheses = s.split("[\\](},]")[0];
                                setAllordertiitles(noparantheses);
                                Log.e(number,noparantheses );
                            }

                        }
                    }

                }

                progressDialog1.dismiss();
                progressDialog1.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void DoLunchOrdersQuery(final String thismonth) {
        getAllordertiitles().clear();
        final ProgressDialog progressDialog2 = new ProgressDialog(ItemAmtReport.this);
        progressDialog2.setMessage("Getting My Orders");
        progressDialog2.show();

        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    final Orders lunchitems = dataSnapshot.getValue(Orders.class);
                    String mydate = lunchitems.getDate();
                    String [] dateParts = mydate.split("-");
                    String numbermonth = dateParts[1];
                    Log.e("lunchnumbermonth",numbermonth );
                    if (numbermonth.equals(thismonth)){
                        for (String s : lunchitems.getOrdertitle()) {
                            //Retrieve the number value only between the parentheses
                            String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                            for (int i = 0; i < Integer.valueOf(number); i++) {
                                String noparantheses = s.split("[\\](},]")[0];
                                setAllordertiitles(noparantheses);
                                Log.e(number, noparantheses);
                            }
                        }


                    }


                }
                AssignData();
                progressDialog2.cancel();
                progressDialog2.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }
    public ArrayList<String> getAllordertiitles() {
        return allordertiitles;
    }

    public void setAllordertiitles(String allordertiitles) {
        Log.e("setAllordertiitles: ","called" );
        this.allordertiitles.add(allordertiitles);
    }
    public String addChar(String str, char ch, int position) {
        //This function will add a character to the desired position in a given string
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }
}
