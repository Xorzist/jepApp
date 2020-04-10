package com.example.jepapp.Activities.Admin;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

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
    List<DataEntry> entries;
     BarChart barChart;
    AnyChartView anyChartView;
    Cartesian cartesian;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_amt_report);
        allorderslist = new ArrayList<>();
        allordertiitles = new ArrayList<>();
        entries = new ArrayList<>();
        progressDialog = new ProgressDialog(getApplicationContext());
          anyChartView =  findViewById(R.id.newpie);
         cartesian = AnyChart.column();

        mAuth = FirebaseAuth.getInstance();
        //dbreference for breakdast orders
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
        DoBreakfastOrdersQuery();
        //dbreference for lunch orders
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
        DoLunchOrdersQuery();



    }

    private void AssignData() {


        Set<String> uniquelabels = new HashSet<>(allordertiitles);
        Log.e("The List",allordertiitles.get(0));
        for (String key : uniquelabels){
            Log.e( "AssignData: " ,key);
            entries.add(new ValueDataEntry(key,Collections.frequency(allordertiitles,key)));
        }
//        for (int i = 0; i <30 ; i++) {
//            entries.add(new ValueDataEntry(i,i+10));
//        }


        Column column = cartesian.column(entries);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("Amount:{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Item Report For the Month");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Menu Items");
        cartesian.yAxis(0).title("Amount Bought");

        anyChartView.setChart(cartesian);


    }

    private void DoBreakfastOrdersQuery() {
        //This function will assign the orders of the current user to a list
        final ProgressDialog progressDialog1 = new ProgressDialog(ItemAmtReport.this);
        progressDialog1.setMessage("Getting My Breakfast Orders");
        progressDialog1.show();
       // myOrderslist.clear();
        //myordertitles.clear();
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                allordertiitles.clear();
//                DoLunchOrdersQuery();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders breakfastitems = dataSnapshot.getValue(Orders.class);
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
                //AssignData();
                progressDialog1.dismiss();
                progressDialog1.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void DoLunchOrdersQuery() {
        final ProgressDialog progressDialog2 = new ProgressDialog(ItemAmtReport.this);
        progressDialog2.setMessage("Getting My Orders");
        progressDialog2.show();

        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    final Orders lunchitems = dataSnapshot.getValue(Orders.class);
                    for (String s : lunchitems.getOrdertitle()){
                        //Retrieve the number value only between the parentheses
                        String number = s.substring(s.indexOf("(")+2,s.indexOf(")"));
                        for (int i = 0; i <Integer.valueOf(number) ; i++) {
                            String noparantheses = s.split("[\\](},]")[0];
                            setAllordertiitles(noparantheses);
                            Log.e(number,noparantheses );
                        }
                       // setAllordertiitles(s);


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
        this.allordertiitles.add(allordertiitles);
    }
}
