package com.example.jepapp.Activities.Admin;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.lineargauge.pointers.Bar;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAmtReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_amt_report);

        Pie pie = AnyChart.pie();
        Cartesian bar = AnyChart.bar();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("John", 10000));
        data.add(new ValueDataEntry("Jake", 12000));
        data.add(new ValueDataEntry("Peter", 18000));

        pie.data(data);
        bar.data(data);

        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.itemsamount_pie);
        anyChartView.setChart(pie);
        AnyChartView anyChartView2 = (AnyChartView) findViewById(R.id.itemsamount_pie2);
        anyChartView2.setChart(bar);

    }
}
