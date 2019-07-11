package com.example.jepapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jepapp.Adapters.RecyclerViewAdapter;
import com.example.jepapp.Models.Book;

import java.util.ArrayList;
import java.util.List;

public class LunchMenu extends Fragment {
    List<Book> lstBook ;

    public LunchMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstBook = new ArrayList<>();
        lstBook.add(new Book("The Vegitarian","Categorie Book","Description book",R.drawable.ic_launcher_background));
        lstBook.add(new Book("The Wild Robot","Categorie Book","Description book",R.drawable.ic_launcher_background));
        lstBook.add(new Book("Maria Semples","Categorie Book","Description book",R.drawable.ic_launcher_background));
        lstBook.add(new Book("The Martian","Categorie Book","Description book",R.drawable.ic_launcher_background));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_lunch_menu, container, false);

        RecyclerView myrv = (RecyclerView) view.findViewById(R.id.add_header);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getContext(),lstBook);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        myrv.setLayoutManager(gridLayoutManager);
        myrv.setAdapter(myAdapter);
        return view;
    }

}
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View rootView = inflater.inflate(R.layout.activity_lunch_menu, container, false);
////        Button buttonInFragment1 = rootView.findViewById(R.id.button_1);
////        buttonInFragment1.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Toast.makeText(getContext(), "button in fragment 1", Toast.LENGTH_SHORT).show();
////            }
////        });
//
//        return rootView;
//    }


