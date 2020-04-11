package com.example.jepapp.Activities.Admin;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LabelsOverlapMode;
import com.anychart.enums.Orientation;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ItemSalesWeeklyReport extends AppCompatActivity {
    private AnyChartView barChart;
    private String name;
    private Query databaseReferencebreakfast, databaseReferencelunch;
    Integer[] monthlynumber = {0};
    Date date;
    String newdate;
    ArrayList<String> dates;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_review);
        barChart = (AnyChartView)findViewById(R.id.barChart);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        // getting date
        date = new Date();
        newdate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        //test subtract days method
        dates = new ArrayList<>();
        dates.add(newdate);
        dates.addAll(subtractDays(date));

        Log.e("newdate",dates.toString());

         databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders").orderByChild("date").startAt(dates.get(6)).endAt(dates.get(0));
         databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders").orderByChild("date").startAt(dates.get(6)).endAt(dates.get(0));
        getInfo();

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


    private void getInfo() {
        final Integer[] breakfastcount = {0,0,0,0,0,0,0};
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    for (int i = 0; i <dates.size() ; i++) {
                        if (breakfastitems.getDate().equals(dates.get(i))){
                            Integer x = breakfastcount[i];
                            Integer cost = breakfastitems.getCost().intValue();
                            breakfastcount[i] = x + cost;
                        }

                    }

                } getLunch(breakfastcount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Integer[] getLunch(final Integer[] breakfastcount) {
        final Integer[] lunchcount = {0,0,0,0,0,0,0};
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders lunchitems = snapshot.getValue(Orders.class);
                        for (int i = 0; i <dates.size() ; i++) {
                            if (lunchitems.getDate().equals(dates.get(i))){
                                Integer x = lunchcount[i];
                                Integer cost = lunchitems.getCost().intValue();
                                lunchcount[i] = x + cost;
                            }

                        }

                        //          Assigning the title and values in the arraylist to graph
                }AssignData(dates,breakfastcount,lunchcount);
                Log.e("in lunch now",Arrays.toString(monthlynumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return lunchcount;

    }


    private void AssignData(ArrayList<String> dates, Integer[] breakfastcount, Integer[] lunchcount) {
        final Cartesian cartesian = AnyChart.bar();

        cartesian.animation(true);


        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.yScale().stackMode(ScaleStackMode.NONE);
        cartesian.yAxis(0).labels(true);
        cartesian.yAxis(0).labels().format("function() {\n" +
                "    return Math.abs(this.value).toLocaleString();\n" +
                "  }");


        cartesian.legend().align(Align.CENTER).enabled(true);

        cartesian.yAxis(0d).title("Weekly Income");

        cartesian.xAxis(0d).overlapMode(LabelsOverlapMode.NO_OVERLAP);

        Linear xAxis1 = cartesian.xAxis(1d);
        xAxis1.enabled(true);

        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.NO_OVERLAP);

        cartesian.title("Weekly sales");
        Set set = Set.instantiate();
        set.data(getData(dates,breakfastcount,lunchcount));
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

        Bar series1 = cartesian.bar(series1Data);

        series1.name("Breakfast")
                .color("#33cc5a");
        series1.labels(true);
        series1.labels().position("center");
        series1.labels().fontColor("#ffffff");
        series1.labels().anchor("center");
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER);

        Bar series2 = cartesian.bar(series2Data);
        series2.name("Lunch")
                .color("#e6191e");
        series2.labels(true);

        series2.labels().position("center");
        series2.labels().fontColor("#ffffff");
        series2.labels().anchor("center");
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER);

        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.legend().selectable(true);

        cartesian.legend().enabled(true);
        cartesian.legend().inverted(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 20d, 0d);
        cartesian.tooltip()
                .title(false)
                .separator(false)
                .displayMode(TooltipDisplayMode.SEPARATED)
                .positionMode(TooltipPositionMode.POINT)
                .useHtml(true)
                .fontSize(12d)
                .offsetX(5d)
                .offsetY(0d)
                .format(
                        "function() {\n" +
                                "      return '<span style=\"color: #D9D9D9\">$</span>' + Math.abs(this.value).toLocaleString();\n" +
                                "    }");
        barChart.setChart(cartesian);
    }


    private ArrayList getData(ArrayList<String> dates, Integer[] breakfastcount, Integer[] lunchcount){
        ArrayList<DataEntry> entries = new ArrayList<>();
        //ArrayList<Number> likes = new ArrayList<>(),dislikes = new ArrayList<>();
        //String[] months = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday","Sunday"};

        for (int i = 0; i <7 ; i++) {
            entries.add(new CustomDataEntry(dates.get(i),breakfastcount[i],lunchcount[i]));
        }

        return entries;
    }
    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }

}

