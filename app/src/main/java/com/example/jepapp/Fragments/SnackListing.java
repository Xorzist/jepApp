package com.example.jepapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jepapp.Adapters.FoodListAdapter;
import com.example.jepapp.Models.FoodItem;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class SnackListing extends Fragment {
    //a list to store all the products
    List<FoodItem> snackItemList;


    //the recyclerview
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_snack_listing, container, false);

      //  return rootView;
        //getting the recyclerview from xml
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //initializing the productlist
        snackItemList = new ArrayList<>();


        //adding some items to our list
        snackItemList.add(
                new FoodItem(
                        1,
                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000,
                        R.drawable.user_profile_image_background));

        snackItemList.add(
                new FoodItem(
                        1,
                        "Dell Inspiron 7000 Core i5 7th Gen - (8 GB/1 TB HDD/Windows 10 Home)",
                        "14 inch, Gray, 1.659 kg",
                        4.3,
                        60000,
                        R.drawable.user_profile_image_background));

        snackItemList.add(
                new FoodItem(
                        1,
                        "Microsoft Surface Pro 4 Core m3 6th Gen - (4 GB/128 GB SSD/Windows 10)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000,
                        R.drawable.user_profile_image_background));

        //creating recyclerview adapter
        FoodListAdapter adapter = new FoodListAdapter(getContext(), snackItemList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
        return  rootView;
    }
}