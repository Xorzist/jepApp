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

public class Reports extends Fragment {
    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.admin_report_activity, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        Cartesian3d pie = AnyChart.bar3d();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("John", 10000));
        data.add(new ValueDataEntry("Jake", 12000));
        data.add(new ValueDataEntry("Peter", 18000));
        data.add(new ValueDataEntry("bob", 1000));
        data.add(new ValueDataEntry("tod", 67000));
        data.add(new ValueDataEntry("crow", 4000));

        pie.data(data);

        AnyChartView anyChartView = (AnyChartView) rootView.findViewById (R.id.firstchart);
        anyChartView.setChart(pie);



        return  rootView;
    }


    }



