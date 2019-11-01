package com.example.jepapp.Fragments.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jepapp.Activities.Admin.SelectMenuItems;
import com.example.jepapp.Adapters.AdminMadeMenuAdapter;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Make_Menu extends Fragment {

    private RecyclerView recyclerView, recyclerView2;
    AdminMadeMenuAdapter adapter;
    private List<Admin_Made_Menu> admin_made_menu;
    String menuitemsurl = "http://legacydevs.com/BreakfastMenuGet.php";
    private Button selectButton;
    private FloatingActionButton fab;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    private DividerItemDecoration dividerItemDecoration;
    private TextView emptyView;
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
        //Bundle bundle = this.getArguments();
       // arrayList = getArguments().getStringArrayList("mylist");
//        FragmentTransaction fr = getFragmentManager().beginTransaction();
//        fr.replace(R.id.fragment_container, new Make_Menu());
//        fr.commit();
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.admin_make_menu_recyclerView);
        recyclerView2 = rootView.findViewById(R.id.admin_make_menu_recyclerView2);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager2 = new  LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new AdminMadeMenuAdapter(getContext(),admin_made_menu);

        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView2.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
        getBreakfastData();


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
                Intent intent = new Intent(getContext(), SelectMenuItems.class);
                startActivity(intent);

            }
        });
//        if(arrayList.size() > 0){
        //if (bundle!=null){

               // arrayList = (ArrayList<String>) getActivity().getIntent().getSerializableExtra("mylist");
          //      arrayList = getArguments().getStringArrayList("mylist");
            //    Log.d("not empty",arrayList.toString());
              //  recyclerView.setVisibility(View.VISIBLE);
                //emptyView.setVisibility(View.GONE);
        //buildRecyclerView();
        //    }


        //else {
          //  Log.d("restarted and empty","empty");
            //recyclerView.setVisibility(View.GONE);
            //emptyView.setVisibility(View.VISIBLE);
//            arrayList.add("l");
//            Log.d("first take at it",arrayList.toString());
          //  Toast.makeText(getContext(), "There are no Transactions in the Transactions Tracker.", Toast.LENGTH_LONG).show();
        //}

//        ((MainActivity)getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//               buildRecyclerView();
//            }
//        });


        return rootView;

    }

    private void getBreakfastData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(menuitemsurl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("Starting Request", "Started!");
                        JSONObject jsonObject = response.getJSONObject(i);

                        Admin_Made_Menu items = new Admin_Made_Menu();
                        items.setId(jsonObject.getString("breakfast_id"));
                        items.setTitle(jsonObject.getString("title"));
                        //items.setIngredients(jsonObject.getString("ingredients"));
                       // items.setImage(jsonObject.getString("image_ref"));
                        items.setPrice(Float.valueOf(jsonObject.getString("item_cost")));

                        admin_made_menu.add(items);

                        Log.d("mhm","Yahsuh it start");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

//    private void saveData() {
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json2 = gson.toJson(arrayList);
//        editor.putString("task list", json2);
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
//        String json = sharedPreferences.getString("task list", null);
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
