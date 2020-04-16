package com.example.jepapp.Activities.Admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ItemSalesWeeklyReportNew extends AppCompatActivity {
    private AnyChartView barChart;
    private String start,end;
    private Query databaseReferencebreakfast, databaseReferencelunch;
    //Integer[] monthlynumber = {0};
    Date date;
    String newdate;
    ArrayList<String> dates;
    TextView card,cash,breakfast,lunch;
    LinearLayout casdandcard, breakfastandlunch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_review);
        start = getIntent().getExtras().getString("startdate");
        end = getIntent().getExtras().getString("enddate");
        barChart = (AnyChartView)findViewById(R.id.barChart);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        // getting date
//        date = new Date();
//        newdate = new SimpleDateFormat("dd-MM-yyyy").format(date);
//        //test subtract days method
//        dates = new ArrayList<>();
//        dates.add(newdate);
        //dates.addAll(subtractDays(date));
        //assign variables
        card= findViewById(R.id.lunchcard_value_report);
        cash = findViewById(R.id.cash_value_report);
        breakfast = findViewById(R.id.breakfast_value_report);
        lunch = findViewById(R.id.lunch_report);
        casdandcard = findViewById(R.id.cashcardlayout);
        breakfastandlunch = findViewById(R.id.breakfastlunchlayout);
//

//        Log.e("newdate",dates.toString());

        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders").orderByChild("date").startAt(start).endAt(end);
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders").orderByChild("date").startAt(start).endAt(end);
        getInfo();

    }



    private void getInfo() {
        final ArrayList cashcount = new ArrayList();
        final ArrayList cardcount = new ArrayList();
        final ArrayList justdates = new ArrayList();
        final Integer[] costforbreakfast = {0};
        final Integer[] costforcard = { 0 };
        final Integer[] costforcash = { 0 };
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    costforbreakfast[0] = costforbreakfast[0] + breakfastitems.getCost().intValue();

                    if (breakfastitems.getPayment_type().toLowerCase().equals("cash")){
                        cashcount.add(breakfastitems.getDate());
                        cashcount.add(String.valueOf(breakfastitems.getCost()));
                        justdates.add(breakfastitems.getDate());
                        Integer cost = breakfastitems.getCost().intValue();
                        costforcash[0] = costforcash[0] + cost;
                    }
                    else{
                        cardcount.add(breakfastitems.getDate());
                        cardcount.add(String.valueOf(breakfastitems.getCost()));
                        justdates.add(breakfastitems.getDate());
                        Integer cost = breakfastitems.getCost().intValue();
                        costforcard[0] = costforcard[0] + cost;
                    }






                } getLunch(cashcount,cardcount, costforcash, costforcard, costforbreakfast,justdates);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLunch(final ArrayList cashcount, final ArrayList cardcount, final Integer[] costforcash, final Integer[] costforcard, final Integer[] costforbreakfast, final ArrayList justdates) {
        final Integer[] costforlunch = {0};
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders lunchitems = snapshot.getValue(Orders.class);
                    costforlunch[0] = costforlunch[0] + lunchitems.getCost().intValue();
                    if (lunchitems.getPayment_type().toLowerCase().equals("cash")){
                        cashcount.add(lunchitems.getDate());
                        cashcount.add(String.valueOf(lunchitems.getCost()));
                        justdates.add(lunchitems.getDate());
                        Integer cost = lunchitems.getCost().intValue();
                        costforcash[0] = costforcash[0] + cost;
                    }
                    else{
                        cardcount.add(lunchitems.getDate());
                        cardcount.add(String.valueOf(lunchitems.getCost()));
                        justdates.add(lunchitems.getDate());
                        Integer cost = lunchitems.getCost().intValue();
                        costforcard[0] = costforcard[0] + cost;
                    }

                    } Log.e("Cash list",cashcount.toString());
                Log.e("Card list",cardcount.toString());
                Log.e("Dates list",justdates.toString());
                Log.e("Cash size",String.valueOf(cashcount.size()));
                Log.e("Card size",String.valueOf(cardcount.size()));
                Log.e("Dates size",String.valueOf(justdates.size()));

                    //          Assigning the title and values in the arraylist to graph
                CalculateCashandCard(justdates,cashcount,cardcount);
                 //AssignData(datesL, cashcount, cardcount);
                AssignVariables(costforbreakfast[0], costforlunch[0],costforcash[0],costforcard[0]);
                // Log.e("in lunch now",Arrays.toString(monthlynumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void CalculateCashandCard(ArrayList justdates, ArrayList cashcount, ArrayList cardcount) {
        ArrayList cashList = new ArrayList();
        ArrayList cardList = new ArrayList();

        java.util.Set<String> uniquedates = new HashSet<>(justdates);

        for (String key : uniquedates) {

            cashList.add(calculatevalues(key,cashcount));
            cardList.add(calculatevalues(key,cardcount));
        }

        List<String> datesList = new ArrayList<>(uniquedates);
        Log.e("Date list", datesList.toString());
        Log.e("Cash list", cashList.toString());
        Log.e("Card list", cardList.toString());
        AssignData(datesList,cashList,cardList);
    }

    private int calculatevalues(String date, ArrayList<String> list) {
        int total=0;
        for (int i = 0; i <list.size() ; i++) {
            if( date.equals(list.get(i))) {
                total+=Integer.parseInt(list.get(i+1));
            }

        }
        Log.e("Total for"+date,String.valueOf(total) );
        return total;
    }

    private void AssignVariables(Integer costbreakfast, Integer costlunch, Integer costcash, Integer costcard) {
        breakfast.setText("$"+costbreakfast.toString());
        lunch.setText("$"+costlunch.toString());
        cash.setText("$"+costcash.toString());
        card.setText("$"+costcard.toString());

    }


    private void AssignData(List<String> uniquedates, ArrayList cashcount, ArrayList cardcount) {
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
        set.data(getData(uniquedates,cashcount,cardcount));
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");
        cartesian.legend().inverted(true);

        Bar series1 = cartesian.bar(series1Data);

        series1.name("Cash")
                .color("#33cc5a");
        series1.labels(true);
        series1.labels().position("center");
        series1.labels().fontColor("#ffffff");
        series1.labels().anchor("center");
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER);

        Bar series2 = cartesian.bar(series2Data);
        series2.name("Lunch Card")
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


    private ArrayList getData(List<String> uniquedates, ArrayList<Integer> cashcount, ArrayList<Integer> cardcount){
        ArrayList<DataEntry> entries = new ArrayList<>();
        //ArrayList<Number> likes = new ArrayList<>(),dislikes = new ArrayList<>();
        //String[] months = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday","Sunday"};

        for (int i = 0; i<uniquedates.size() ; i++) {
            entries.add(new CustomDataEntry(uniquedates.get(i),cashcount.get(i),cardcount.get(i)));
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

