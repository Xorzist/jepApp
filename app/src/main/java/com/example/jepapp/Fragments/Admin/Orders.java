package com.example.jepapp.Fragments.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Admin.OrderListAdapter;
import com.example.jepapp.Models.OrderItem;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class Orders extends Fragment {
    List<OrderItem> orderItemList;
    //the recyclerview
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.admin_order_listing, container, false);

        //  return rootView;
        //getting the recyclerview from xml
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //initializing the productlist
        orderItemList = new ArrayList<>();


        //adding some items to our list
        orderItemList.add(
                new OrderItem(1, "Ackee", "Mel", 2, R.drawable.user_profile_image_background));

        orderItemList.add(
                new OrderItem(
                        1,
                        "Dell Inspiron 7000 Core i5 7th Gen - (8 GB/1 TB HDD/Windows 10 Home)",
                        "You", 1,
                        R.drawable.user_profile_image_background));

        orderItemList.add(
                new OrderItem(
                        1,
                        "Microsoft Surface Pro 4 Core m3 6th Gen - (4 GB/128 GB SSD/Windows 10)",
                        "Marshmellow", 1,
                        R.drawable.user_profile_image_background));

        //creating recyclerview adapter
        OrderListAdapter adapter = new OrderListAdapter(getContext(), orderItemList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
        return  rootView;
    }
}

