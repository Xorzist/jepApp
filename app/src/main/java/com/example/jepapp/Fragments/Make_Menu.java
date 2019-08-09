package com.example.jepapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jepapp.Activities.MainActivity;
import com.example.jepapp.Adapters.AdminMadeMenuAdapter;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.Models.Data;
import com.example.jepapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Make_Menu extends Fragment {

    private RecyclerView recyclerView;
    List<Admin_Made_Menu> admin_made_menu;
    private Button selectButton;
    private FloatingActionButton fab;
    private TextView emptyView;
    ArrayList<String> arrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.admin_make_menu, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        arrayList = new ArrayList<>();
        Bundle bundle = this.getArguments();
       // arrayList = getArguments().getStringArrayList("mylist");
//        FragmentTransaction fr = getFragmentManager().beginTransaction();
//        fr.replace(R.id.fragment_container, new Make_Menu());
//        fr.commit();
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.admin_make_menu_recyclerView);

        //loadData();
        //buildRecyclerView();
        //saveData();


//        //initializing the productlist
//        admin_made_menu = new ArrayList<>();
//        admin_made_menu.add(
//                new Admin_Made_Menu("Soup"));


//        Button buttonInFragment1 = rootView.findViewById(R.id.button_1);
//        buttonInFragment1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "button in fragment 1", Toast.LENGTH_SHORT).show();
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
//        if(arrayList.size() > 0){
        if (bundle!=null){

               // arrayList = (ArrayList<String>) getActivity().getIntent().getSerializableExtra("mylist");
                arrayList = getArguments().getStringArrayList("mylist");
                Log.d("not empty",arrayList.toString());
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                buildRecyclerView();
            }


        else {
            Log.d("restarted and empty","empty");
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
//            arrayList.add("l");
//            Log.d("first take at it",arrayList.toString());
          //  Toast.makeText(getContext(), "There are no Transactions in the Transactions Tracker.", Toast.LENGTH_LONG).show();
        }

//        ((MainActivity)getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//               buildRecyclerView();
//            }
//        });


        return rootView;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json2 = gson.toJson(arrayList);
        editor.putString("task list", json2);
        editor.apply();
    }

    private void buildRecyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (arrayList.size()>0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            AdminMadeMenuAdapter adapter = new AdminMadeMenuAdapter(getContext(), arrayList);

            //setting adapter to recyclerview
            recyclerView.setAdapter(adapter);
        }
        else {

            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private  void loadData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Data>>() {}.getType();
        arrayList = gson.fromJson(json, type);

        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }


    }

}
