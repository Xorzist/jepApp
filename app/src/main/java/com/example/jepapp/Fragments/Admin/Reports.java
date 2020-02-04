package com.example.jepapp.Fragments.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Bar3d;
import com.example.jepapp.Activities.Admin.AdminPageforViewPager;
import com.example.jepapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Reports extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_report_activity);
        Cartesian3d pie = AnyChart.bar3d();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("John", 10000));
        data.add(new ValueDataEntry("Jake", 12000));
        data.add(new ValueDataEntry("Peter", 18000));
        data.add(new ValueDataEntry("bob", 1000));
        data.add(new ValueDataEntry("tod", 67000));
        data.add(new ValueDataEntry("crow", 4000));

        pie.data(data);

        AnyChartView anyChartView = (AnyChartView) findViewById (R.id.firstchart);
        anyChartView.setChart(pie);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.orderspage:
                        Intent intent = new Intent(getApplicationContext(), AdminPageforViewPager.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.inventorypage:

                        break;

                    case R.id.analysispage:
                        Intent intent3 = new Intent(getApplicationContext(), Reports.class);
                        startActivity(intent3);
                        finish();
                        break;
                }


                return true;
            }
        });

    }


    }



