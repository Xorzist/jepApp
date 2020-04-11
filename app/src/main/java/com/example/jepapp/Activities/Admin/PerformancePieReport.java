package com.example.jepapp.Activities.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class PerformancePieReport extends AppCompatActivity {
    private String month;
    private Integer likes, dislikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_popup_window);
        Bundle intent = getIntent().getExtras();
        month = "March";
       // month = intent.getString("month");
        likes = intent.getInt("likes");
        dislikes = intent.getInt("dislikes");

        AnyChartView anyChartView = findViewById(R.id.pieChart);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_pie));

        Pie pie = AnyChart.pie();

//        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
//            @Override
//            public void onClick(Event event) {
//                Toast.makeText(PerformancePieReport.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
//            }
//        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("likes", likes));
        data.add(new ValueDataEntry("dislikes", dislikes));
//        data.add(new ValueDataEntry("Bananas", 7216301));
//        data.add(new ValueDataEntry("Grapes", 1486621));
//        data.add(new ValueDataEntry("Oranges", 1200000));

        pie.data(data);

        pie.title("Reviews for the month of "+month);
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Key")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
    }
}