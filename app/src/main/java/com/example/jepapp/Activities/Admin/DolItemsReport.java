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
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DolItemsReport extends AppCompatActivity {
    private AnyChartView barChart;
    private String name;
    private DatabaseReference databaseReferencebreakfast,databaseReferencelunch;
    Integer[] monthlynumber = {0};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_review);
        barChart = (AnyChartView)findViewById(R.id.barChart);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        name = getIntent().getExtras().getString("name");
        //getReviews();
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
        getInfo();


        // Query reviewlikes = databaseReference.orderByChild("liked").equalTo("yes");
        //Query reviewdislikes = databaseReference.orderByChild("disliked").equalTo("yes");



//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                monthlynumber.clear();
//                int countjan = 0, countfeb=0,countmar=0,countapril=0,countmay=0,countjune=0,countjul=0
//                        ,countaug=0,countsep=0,countoct=0,countnov=0,countdec = 0;
//                int dcountjan = 0, dcountfeb=0,dcountmar=0,dcountapril=0,dcountmay=0,dcountjune=0,dcountjul=0
//                        ,dcountaug=0,dcountsep=0,dcountoct=0,dcountnov=0,dcountdec = 0;
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    com.example.jepapp.Models.Reviews allreviews = dataSnapshot.getValue(com.example.jepapp.Models.Reviews.class);
//
//                    String date = allreviews.getDate();
//                    String liked = allreviews.getLiked();
//                    String [] dateParts = date.split("-");
//                    String month = dateParts[1];
//                    if(liked.equals("yes")){
//                        if (month.equals("01")){
//                            countjan=countjan + 1;
//                        }else if (month.equals("02")){
//                            countfeb = countfeb + 1;
//                        }else if (month.equals("03")){
//                            countmar = countmar + 1;
//                        }else if (month.equals("04")){
//                            countapril = countapril + 1;
//                        }else if (month.equals("05")){
//                            countmay = countmay + 1;
//                        }else if (month.equals("06")){
//                            countjune = countjune + 1;
//                        }else if (month.equals("07")){
//                            countjul = countjul + 1;
//                        }else if (month.equals("08")){
//                            countaug = countaug + 1;
//                        }else if (month.equals("09")){
//                            countsep = countsep + 1;
//                        }else if (month.equals("10")){
//                            countoct = countoct + 1;
//                        }else if (month.equals("11")){
//                            countnov = countnov + 1;
//                        }else if (month.equals("12")) {
//                            countdec = countdec + 1;
//                        }
//                    }else{
//                        if (month.equals("01")){
//                            dcountjan=dcountjan + 1;
//                        }else if (month.equals("02")){
//                            dcountfeb = dcountfeb + 1;
//                        }else if (month.equals("03")){
//                            dcountmar = dcountmar + 1;
//                        }else if (month.equals("04")){
//                            dcountapril = dcountapril + 1;
//                        }else if (month.equals("05")){
//                            dcountmay = dcountmay + 1;
//                        }else if (month.equals("06")){
//                            dcountjune = dcountjune + 1;
//                        }else if (month.equals("07")){
//                            dcountjul = dcountjul + 1;
//                        }else if (month.equals("08")){
//                            dcountaug = dcountaug + 1;
//                        }else if (month.equals("09")){
//                            dcountsep = dcountsep + 1;
//                        }else if (month.equals("10")){
//                            dcountoct = dcountoct + 1;
//                        }else if (month.equals("11")){
//                            dcountnov = dcountnov + 1;
//                        }else if (month.equals("12")) {
//                            dcountdec = dcountdec + 1;
//                        }
//                    }
//                }
//                Integer[] li = {countjan,countfeb,countmar,countapril,countmay,countjune,countjul,
//                        countaug,countsep,countoct,countnov,countdec};
//                Integer[] disli = {dcountjan,dcountfeb,dcountmar,dcountapril,dcountmay,dcountjune,dcountjul,
//                        dcountaug,dcountsep,dcountoct,dcountnov,dcountdec};
//                Log.e("Whats getting passed", Arrays.toString(li));
//               AssignData();
//
//            }@Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
    }

    private void getInfo() {
        final Integer[] monthcount = {0,0,0,0,0,0,0,0,0,0,0,0};
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    for (String s : breakfastitems.getOrdertitle()){
                        //retrieve the string only
                        String noparantheses = s.split("[\\](},]")[0];
                        if (noparantheses.equals(name)) {
                            //retrieve date value
                            String date = breakfastitems.getDate();
                            String[] dateParts = date.split("-");
                            String month = dateParts[1];
                            //Retrieve number value only between the parentheses
                            String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                            Integer num = Integer.parseInt(number);
                            Integer x = monthcount[findmonth(month)];
                            monthcount[findmonth(month)] = num+x;

                        }
                    }
                    // get the lunch data and add it to the list
                    }getLunch(monthcount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Integer[] getLunch(final Integer[] monthcount) {
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int countjan = 0, countfeb=0,countmar=0,countapril=0,countmay=0,countjune=0,countjul=0
//                        ,countaug=0,countsep=0,countoct=0,countnov=0,countdec = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    for (String s : breakfastitems.getOrdertitle()){
                        //retrieve the string only
                        String noparantheses = s.split("[\\](},]")[0];
                        if (noparantheses.equals(name)) {
                            //retrieve date value
                            String date = breakfastitems.getDate();
                            String[] dateParts = date.split("-");
                            String month = dateParts[1];
                            //Retrieve number value only between the parentheses
                            String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                            Integer num = Integer.parseInt(number);
                            Integer x = monthcount[findmonth(month)];
                            monthcount[findmonth(month)] = num+x;


                        }
                    }
    //          Assigning the title and values in the arraylist to graph
                }AssignData(monthcount);
                Log.e("in lunch now",Arrays.toString(monthlynumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return monthcount;

    }

    private int findmonth(String month) {
        //returns a index position to be used to assess and change the value of the integer array list based on the month identified
        Integer val = 0;
        if (month.equals("01")) {
           val = 0;
        } else if (month.equals("02")) {
           val = 1;
        } else if (month.equals("03")) {
            val = 2;
        } else if (month.equals("04")) {
            val = 3;
        } else if (month.equals("05")) {
            val = 4;
        } else if (month.equals("06")) {
            val = 5;
        } else if (month.equals("07")) {
            val = 6;
        } else if (month.equals("08")) {
            val = 7;
        } else if (month.equals("09")) {
            val = 8;
        } else if (month.equals("10")) {
            val = 9;
        } else if (month.equals("11")) {
           val = 10;
        } else if (month.equals("12")) {
           val = 11;
        }
        return val;
    }

    private void AssignData(Integer[] monthcount) {
        final Cartesian cartesian = AnyChart.bar();

        cartesian.animation(true);


        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.yScale().stackMode(ScaleStackMode.NONE);
        cartesian.yAxis(0).labels(true);


        cartesian.legend().align(Align.CENTER).enabled(true);

        cartesian.yAxis(0d).title("Months");

        cartesian.xAxis(0d).overlapMode(LabelsOverlapMode.NO_OVERLAP);

        Linear xAxis1 = cartesian.xAxis(1d);
        xAxis1.enabled(true);
        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.NO_OVERLAP);

        cartesian.title(name +" sales by month");
        Set set = Set.instantiate();
        set.data(getData(monthcount));
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Bar series1 = cartesian.bar(series1Data);
        series1.name("Amount Sold")
                .color("#33cc5a");
        series1.labels(true)
                .hover().labels(true);
        series1.labels().position("center");
        series1.labels().fontColor("#ffffff");
        series1.labels().anchor("center");
        series1.tooltip()
                .position("left")
                .anchor(Anchor.RIGHT_CENTER);

        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.legend().selectable(true);

        cartesian.legend().enabled(true);
        cartesian.legend().inverted(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 20d, 0d);

        barChart.setChart(cartesian);
    }


    private ArrayList getData(Integer[] itemcount){
        ArrayList<DataEntry> entries = new ArrayList<>();
        //ArrayList<Number> likes = new ArrayList<>(),dislikes = new ArrayList<>();
        String[] months = {"January", "February", "March", "April", "May","June","July","August","September",
                "October","November","December"};

        for (int i = 0; i <12 ; i++) {
            entries.add(new ValueDataEntry(months[i],itemcount[i]));
        }

        return entries;
    }

    }

