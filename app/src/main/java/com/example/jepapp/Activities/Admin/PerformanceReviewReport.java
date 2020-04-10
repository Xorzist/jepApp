package com.example.jepapp.Activities.Admin;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.ui.Center;
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
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerformanceReviewReport extends AppCompatActivity {
    private AnyChartView barChart;
    List<Integer> reviewslike = new ArrayList<>();
    List<Integer> reviewsdislike = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_review);
        barChart = (AnyChartView)findViewById(R.id.barChart);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        reviewslike = new ArrayList<>();
        reviewsdislike = new ArrayList<>();
        //getReviews();
        Log.e(reviewslike.toString(),reviewsdislike.toString());

        final Cartesian cartesian = AnyChart.bar();

        cartesian.animation(true);


        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.yScale().stackMode(ScaleStackMode.NONE);
        cartesian.yAxis(0).labels(true);

//        cartesian.yAxis(0).labels().format(
//                "function() {\n" +
//                        "    return Math.abs(this.value).toLocaleString();\n" +
//                        "  }");
        cartesian.legend().align(Align.CENTER).enabled(true);

        cartesian.yAxis(0d).title("Months");

        cartesian.xAxis(0d).overlapMode(LabelsOverlapMode.NO_OVERLAP);

        Linear xAxis1 = cartesian.xAxis(1d);
        xAxis1.enabled(true);
        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.NO_OVERLAP);

        cartesian.title("Reviews by month");




        DatabaseReference databaseReference;

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Reviews");
        // Query reviewlikes = databaseReference.orderByChild("liked").equalTo("yes");
        //Query reviewdislikes = databaseReference.orderByChild("disliked").equalTo("yes");



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                reviewslike.clear();
                int countjan = 0, countfeb=0,countmar=0,countapril=0,countmay=0,countjune=0,countjul=0
                        ,countaug=0,countsep=0,countoct=0,countnov=0,countdec = 0;
                int dcountjan = 0, dcountfeb=0,dcountmar=0,dcountapril=0,dcountmay=0,dcountjune=0,dcountjul=0
                        ,dcountaug=0,dcountsep=0,dcountoct=0,dcountnov=0,dcountdec = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Reviews allreviews = dataSnapshot.getValue(com.example.jepapp.Models.Reviews.class);

                    String date = allreviews.getDate();
                    String liked = allreviews.getLiked();
                    String [] dateParts = date.split("-");
                    String month = dateParts[1];
                    if(liked.equals("yes")){
                        if (month.equals("01")){
                            countjan=countjan + 1;
                        }else if (month.equals("02")){
                            countfeb = countfeb + 1;
                        }else if (month.equals("03")){
                            countmar = countmar + 1;
                        }else if (month.equals("04")){
                            countapril = countapril + 1;
                        }else if (month.equals("05")){
                            countmay = countmay + 1;
                        }else if (month.equals("06")){
                            countjune = countjune + 1;
                        }else if (month.equals("07")){
                            countjul = countjul + 1;
                        }else if (month.equals("08")){
                            countaug = countaug + 1;
                        }else if (month.equals("09")){
                            countsep = countsep + 1;
                        }else if (month.equals("10")){
                            countoct = countoct + 1;
                        }else if (month.equals("11")){
                            countnov = countnov + 1;
                        }else if (month.equals("12")) {
                            countdec = countdec + 1;
                        }
                    }else{
                        if (month.equals("01")){
                            dcountjan=dcountjan + 1;
                        }else if (month.equals("02")){
                            dcountfeb = dcountfeb + 1;
                        }else if (month.equals("03")){
                            dcountmar = dcountmar + 1;
                        }else if (month.equals("04")){
                            dcountapril = dcountapril + 1;
                        }else if (month.equals("05")){
                            dcountmay = dcountmay + 1;
                        }else if (month.equals("06")){
                            dcountjune = dcountjune + 1;
                        }else if (month.equals("07")){
                            dcountjul = dcountjul + 1;
                        }else if (month.equals("08")){
                            dcountaug = dcountaug + 1;
                        }else if (month.equals("09")){
                            dcountsep = dcountsep + 1;
                        }else if (month.equals("10")){
                            dcountoct = dcountoct + 1;
                        }else if (month.equals("11")){
                            dcountnov = dcountnov + 1;
                        }else if (month.equals("12")) {
                            dcountdec = dcountdec + 1;
                        }
                    }
                }
                Integer[] li = {countjan,countfeb,countmar,countapril,countmay,countjune,countjul,
                        countaug,countsep,countoct,countnov,countdec};
                Integer[] disli = {dcountjan,dcountfeb,dcountmar,dcountapril,dcountmay,dcountjune,dcountjul,
                        dcountaug,dcountsep,dcountoct,dcountnov,dcountdec};
                Log.e("Whats getting passed", Arrays.toString(li));
                Set set = Set.instantiate();
                set.data(getData(li,disli));
                Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
                Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");
//                series1Data.setOnClickListener(new ListenersInterface.OnClickListener() {
//                    @Override
//                    public void onClick(Event event) {
//
//                    }
//                });
                Bar series1 = cartesian.bar(series1Data);
                series1.name("Likes")
                        .color("#33cc5a");
                series1.labels(true)
                        .hover().labels(true);
                series1.labels().position("center");
                series1.labels().fontColor("#ffffff");
                series1.labels().anchor("center");
                series1.tooltip()
                        .position("left")
                        .anchor(Anchor.RIGHT_CENTER);
                Bar series2 = cartesian.bar(series2Data);
                series2.name("Disikes")
                        .color("#e6191e");
                series2.labels(true);
                series2.labels().position("center");
                series2.labels().fontColor("#ffffff");
                series2.labels().anchor("center");
                series2.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER);
                //getData(li,disli);

//                reviewslike.add(countjan);
//                reviewslike.add(countfeb);
//                reviewslike.add(countmar);
//                reviewslike.add(countapril);
//                reviewslike.add(countmay);
//                reviewslike.add(countjune);
//                reviewslike.add(countjul);
//                reviewslike.add(countaug);
//                reviewslike.add(countsep);
//                reviewslike.add(countoct);
//                reviewslike.add(countnov);
//                reviewslike.add(countdec);

                Log.e("print likes for me",reviewslike.toString());

            }@Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        cartesian.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value", "value2"}) {
            @Override
            public void onClick(Event event) {
                String month = event.getData().get("x");
                Integer likes = Integer.parseInt(event.getData().get("value"));
                Integer dislikes = Integer.parseInt(event.getData().get("value2"));

                Bundle bundle = new Bundle();
                bundle.putString("month", month);
                bundle.putInt("likes", likes);
                bundle.putInt("dislikes", dislikes);
                Intent intent = new Intent(PerformanceReviewReport.this, PerformancePieReport.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Log.e("Likes:"+likes.toString(),"Dislikes"+dislikes.toString());

            }
        });
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

//        cartesian.tooltip()
//                .title(false)
//                .enabled(true)
//                .separator(true)
//                .displayMode(TooltipDisplayMode.SEPARATED)
//                .positionMode(TooltipPositionMode.POINT)
//                .useHtml(true)
//                .fontSize(12d)
//                .offsetX(5d)
//                .offsetY(0d)
//                .format(
//                        "function() {\n" +
//                                "      return '<span style=\"color: #ffffff\"></span>' + Math.abs(this.value).toLocaleString();\n" +
//                                "    }");

//        Set set = Set.instantiate();
//        set.data(getData(li,disli));
//        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
//        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");
//
//        Bar series1 = cartesian.bar(series1Data);
//        series1.name("Dislikes")
//                .color("HotPink");
//        series1.labels(true)
//                .hover().labels(true);
//        series1.labels().position("center");
//        series1.labels().fontColor("#663399");
//        series1.labels().anchor("center");
////        series1.tooltip()
////                .position("right")
////                .anchor(Anchor.LEFT_CENTER);
//        Bar series2 = cartesian.bar(series2Data);
//        series2.name("Likes");
//        series2.labels(true);
//        series2.labels().position("center");
//        series2.labels().fontColor("#663399");
//        series2.labels().anchor("center");
//        series2.tooltip()
//                .position("left")
//                .anchor(Anchor.RIGHT_CENTER);
        cartesian.legend().selectable(true);
        cartesian.legend().setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value", "value2"}) {
            @Override
            public void onClick(Event event) {
                String month = event.getData().get("x");
                Integer likes = Integer.parseInt(event.getData().get("value"));
                Integer dislikes = Integer.parseInt(event.getData().get("value2"));
                Log.e("Month:"+month,"Dislikes"+dislikes.toString());
                Log.e("Month:"+month,"Likes"+likes.toString());
            }
        });
        cartesian.legend().enabled(true);
        cartesian.legend().inverted(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 20d, 0d);

        barChart.setChart(cartesian);
    }

//    private void getReviews() {
//        DatabaseReference databaseReference;
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Reviews");
//       // Query reviewlikes = databaseReference.orderByChild("liked").equalTo("yes");
//        //Query reviewdislikes = databaseReference.orderByChild("disliked").equalTo("yes");
//
//
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                reviewslike.clear();
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
//                            countjan=countjan+1;
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
//                            dcountjan=dcountjan+1;
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
//
//                getData(li,disli);
//
////                reviewslike.add(countjan);
////                reviewslike.add(countfeb);
////                reviewslike.add(countmar);
////                reviewslike.add(countapril);
////                reviewslike.add(countmay);
////                reviewslike.add(countjune);
////                reviewslike.add(countjul);
////                reviewslike.add(countaug);
////                reviewslike.add(countsep);
////                reviewslike.add(countoct);
////                reviewslike.add(countnov);
////                reviewslike.add(countdec);
//
//                Log.e("print likes for me",reviewslike.toString());
//
//            }@Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        reviewdislikes.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                reviewsdislike.clear();
//                int countjan = 0, countfeb=0,countmar=0,countapril=0,countmay=0,countjune=0,countjul=0
//                        ,countaug=0,countsep=0,countoct=0,countnov=0,countdec = 0;
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                    com.example.jepapp.Models.Reviews allreviews = snapshot.getValue(com.example.jepapp.Models.Reviews.class);
//
//                    String date = allreviews.getDate();
//                    String [] dateParts = date.split("-");
//                    String month = dateParts[1];
//                    if (month.equals("01")){
//                        countjan=countjan+1;
//                    }else if (month.equals("02")){
//                        countfeb = countfeb + 1;
//                    }else if (month.equals("03")){
//                        countmar = countmar + 1;
//                    }else if (month.equals("04")){
//                        countapril = countapril + 1;
//                    }else if (month.equals("05")){
//                        countmay = countmay + 1;
//                    }else if (month.equals("06")){
//                        countjune = countjune + 1;
//                    }else if (month.equals("07")){
//                        countjul = countjul + 1;
//                    }else if (month.equals("08")){
//                        countaug = countaug + 1;
//                    }else if (month.equals("09")){
//                        countsep = countsep + 1;
//                    }else if (month.equals("10")){
//                        countoct = countoct + 1;
//                    }else if (month.equals("11")){
//                        countnov = countnov + 1;
//                    }else if (month.equals("12")) {
//                        countdec = countdec + 1;
//                    }
//
//                    // allreviews.getDate().)
//
//                }
//
//
//                reviewsdislike.add(countjan);
//                reviewsdislike.add(countfeb);
//                reviewsdislike.add(countmar);
//                reviewsdislike.add(countapril);
//                reviewsdislike.add(countmay);
//                reviewsdislike.add(countjune);
//                reviewsdislike.add(countjul);
//                reviewsdislike.add(countaug);
//                reviewsdislike.add(countsep);
//                reviewsdislike.add(countoct);
//                reviewsdislike.add(countnov);
//                reviewsdislike.add(countdec);
//
//                Log.e("print dislikes for me",reviewsdislike.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });

 //   }

    private ArrayList getData(Integer[] reviewslike, Integer[] reviewsdislike){
        ArrayList<DataEntry> entries = new ArrayList<>();
        //ArrayList<Number> likes = new ArrayList<>(),dislikes = new ArrayList<>();
        String[] months = {"January", "February", "March", "April", "May","June","July","August","September",
        "October","November","December"};

        for (int i = 0; i <12 ; i++) {
            entries.add(new CustomDataEntry(months[i],reviewslike[i],reviewsdislike[i]));
        }
//        for (int i = 40; i < 52; i++) {
//            likes.add((i));
//        }
//        for (int i = 40; i < 52; i++) {
//            dislikes.add((i));
//        }

//        entries.add(new CustomDataEntry("January", likes.get(0), dislikes.get(0)));
//        entries.add(new CustomDataEntry("February", likes.get(1), dislikes.get(1)));
//        entries.add(new CustomDataEntry("March", likes.get(2), dislikes.get(2)));
//        entries.add(new CustomDataEntry("April", likes.get(3), dislikes.get(3)));
//        entries.add(new CustomDataEntry("May", likes.get(4), dislikes.get(4)));
//        entries.add(new CustomDataEntry("June", likes.get(5), dislikes.get(5)));
//        entries.add(new CustomDataEntry("July", likes.get(6), dislikes.get(6)));
//        entries.add(new CustomDataEntry("August", likes.get(7), dislikes.get(7)));
//        entries.add(new CustomDataEntry("September", likes.get(8), dislikes.get(8)));
//        entries.add(new CustomDataEntry("October", likes.get(9), dislikes.get(9)));
//        entries.add(new CustomDataEntry("November",likes.get(10), dislikes.get(10)));
//        entries.add(new CustomDataEntry("December", likes.get(11), dislikes.get(11)));

        return entries;
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }
}
