package com.example.jepapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jepapp.Adapters.AdminMadeMenuAdapter;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;

import java.util.ArrayList;
import java.util.List;

public class Make_Menu extends Fragment {

    private RecyclerView recyclerView;
    List<Admin_Made_Menu> admin_made_menu;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.admin_make_menu, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.admin_make_menu_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //initializing the productlist
        admin_made_menu = new ArrayList<>();
        admin_made_menu.add(
                new Admin_Made_Menu(1,"Soup"));

        AdminMadeMenuAdapter adapter = new AdminMadeMenuAdapter(getContext(), admin_made_menu);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
//        Button buttonInFragment1 = rootView.findViewById(R.id.button_1);
//        buttonInFragment1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "button in fragment 1", Toast.LENGTH_SHORT).show();
//            }
//        });

        return rootView;
    }

}
