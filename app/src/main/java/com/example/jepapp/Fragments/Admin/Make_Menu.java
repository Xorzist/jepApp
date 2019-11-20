package com.example.jepapp.Fragments.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Admin.SelectMenuItems;
import com.example.jepapp.Adapters.Admin.AdminMadeMenuAdapter;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Make_Menu extends Fragment {

    private RecyclerView recyclerView, recyclerView2;
    AdminMadeMenuAdapter adapter, adapter2;
    private List<Admin_Made_Menu> admin_made_menu, admin_made_menulunch;
    String menuitemsurl = "http://legacydevs.com/BreakfastMenuGet.php";
    private Button selectButton;
    private FloatingActionButton fab;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    private DividerItemDecoration dividerItemDecoration;
    private TextView emptyView;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
   // ArrayList<String> arrayList;
    //private List<MItems> MenuItemsList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.admin_make_menu, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        admin_made_menu = new ArrayList<>();
        admin_made_menulunch = new ArrayList<>();

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.admin_make_menu_recyclerView);
        recyclerView2 = rootView.findViewById(R.id.admin_make_menu_recyclerView2);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager2 = new  LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new AdminMadeMenuAdapter(getContext(),admin_made_menu);
        adapter2 = new AdminMadeMenuAdapter(getContext(),admin_made_menulunch);

        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView2.setAdapter(adapter2);
        recyclerView.setAdapter(adapter);

        getBreakfastData();
        getLunchData();


        //loadData();
        //buildRecyclerView();
        //saveData();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelectMenuItems.class);
                startActivity(intent);

            }
        });



        return rootView;

    }

    private void getLunchData() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                admin_made_menulunch.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu lunchDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    admin_made_menulunch.add(lunchDetails);
                   //  Log.d("SIZERZ", String.valueOf(admin_made_menulunch.get(0).getTitle()));
                }

//                adapter = new SelectMenuItemsAdaptertest(SelectMenuItems.this, balanceList);
//
//                recyclerView.setAdapter(adapter);
                adapter2.notifyDataSetChanged();

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });
    }


    private void getBreakfastData() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Data from Firebase Database");

        //progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                admin_made_menu.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu breakfastDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    admin_made_menu.add(breakfastDetails);
                    // Log.d("SIZERZ", String.valueOf(balanceList.get(0).getTitle()));
                }

//                adapter = new SelectMenuItemsAdaptertest(SelectMenuItems.this, balanceList);
//
//                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                //progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

    }

//    private void saveData() {
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json2 = gson.toJson(arrayList);
//        editor.putString("task balanceList", json2);
//        editor.apply();
//    }

    private void buildRecyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (admin_made_menu.size()>0) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            AdminMadeMenuAdapter adapter = new AdminMadeMenuAdapter(getContext(), admin_made_menu);

            //setting adapter to recyclerview
            recyclerView.setAdapter(adapter);
        }
        else {

            recyclerView.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

//    private  void loadData(){
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("task balanceList", null);
//        Type type = new TypeToken<ArrayList<Data>>() {}.getType();
//        arrayList = gson.fromJson(json, type);
//
//        if (arrayList == null) {
//            arrayList = new ArrayList<>();
//        }
//
//
//    }

}
