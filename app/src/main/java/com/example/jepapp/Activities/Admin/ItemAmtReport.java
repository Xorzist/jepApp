package com.example.jepapp.Activities.Admin;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Bar3d;
import com.anychart.core.lineargauge.pointers.Bar;
import com.example.jepapp.Adapters.Admin.AllOrdersAdapter;
import com.example.jepapp.R;
import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ItemAmtReport extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    List<com.example.jepapp.Models.Orders> allorderslist;
    ArrayList<String>allordertiitles;
    private ProgressDialog progressDialog;
    private Description g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_amt_report);
        allorderslist = new ArrayList<>();
        allordertiitles = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Orders");
        progressDialog = new ProgressDialog(getApplicationContext());
        final PieChart pieChart =  findViewById(R.id.newpie);
        pieChart.setUsePercentValues(true);
        Legend l = pieChart.getLegend();
        new Description().setText("wdw");
        pieChart.setDescription(g);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        mAuth = FirebaseAuth.getInstance();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allorderslist.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    com.example.jepapp.Models.Orders allfoodorders = dataSnapshot.getValue(com.example.jepapp.Models.Orders.class);

                    allorderslist.add(allfoodorders);

                }
                for (int i = 0; i<allorderslist.size(); i++){
                    allordertiitles.add(allorderslist.get(i).getType());
                }
                Set<String> uniquelabels = new HashSet<String>(allordertiitles);
                int u = 0;
                Iterator<String> numberitr = uniquelabels.iterator();
                Iterator<String> nameitr = uniquelabels.iterator();
                List<PieEntry> entries = new ArrayList<>();
                while (numberitr.hasNext()) {

                    entries.add(new PieEntry(Collections.frequency(allordertiitles,numberitr.next()),nameitr.next()));
                    u++;
                     }




//                List<PieEntry> entries = new ArrayList<>();
//                for (int i = 0; i<allordertiitles.size(); i++){
//                    //TODO : Create a list of unique order titles to use as the labels
//                    entries.add(new PieEntry(Collections.frequency(allordertiitles,allordertiitles.get(i)),allordertiitles.get(i)));
//                    Log.e(allordertiitles.get(i),allordertiitles.get(i));
//                }

                PieDataSet dataSet = new PieDataSet(entries,"All Items");
                ArrayList<Integer> colors = new ArrayList<>();

//                for (int c : ColorTemplate.VORDIPLOM_COLORS)
//                    colors.add(c);
//
//                for (int c : ColorTemplate.JOYFUL_COLORS)
//                    colors.add(c);
//
//                for (int c : ColorTemplate.COLORFUL_COLORS)
//                    colors.add(c);
//
//                for (int c : ColorTemplate.LIBERTY_COLORS)
//                    colors.add(c);

                for (int c : ColorTemplate.PASTEL_COLORS)
                    colors.add(c);

                colors.add(ColorTemplate.getHoloBlue());

                dataSet.setColors(colors);
                PieData data = new PieData(dataSet);  data.setValueFormatter(new PercentFormatter(pieChart));
                data.setValueTextSize(15f);
                data.setValueTextColor(Color.BLACK);

                pieChart.setData(data);
                pieChart.invalidate();



                progressDialog.dismiss();
            }@Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

    }
}
