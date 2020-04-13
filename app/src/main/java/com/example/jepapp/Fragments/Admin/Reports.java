package com.example.jepapp.Fragments.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Admin.ReportTypeAdapter;
import com.example.jepapp.Models.ReportType;
import com.example.jepapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Reports extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private ArrayList<ReportType> reportTypeArrayList;


    public ReportTypeAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.admin_report_activity, container, false);
        recyclerView = rootView.findViewById(R.id.allreportitems);
        reportTypeArrayList = new ArrayList<>();
        reportTypeArrayList.add(new ReportType("Item Demand","This will present an analytical representation on the amount " +
                "and type of menu items purchased using the application"));
        reportTypeArrayList.add(new ReportType("Item Sales","This will present an analytical representation on the cash value " +
                " of the type of menu items purchased using the application"));
        reportTypeArrayList.add(new ReportType("Performance Review","This will present an analytical representation on the performance of the canteen " +
                "based on the reviews it has been given by customers"));


        adapter = new ReportTypeAdapter(getContext(),reportTypeArrayList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        setHasOptionsMenu(true);

        return  rootView;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
    }

}



