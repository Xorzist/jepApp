package com.example.jepapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jepapp.Adapters.RecyclerViewAdapter;
import com.example.jepapp.Fragments.Make_Menu;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private RecyclerViewAdapter adapter;
    String selectitemsformenu = "http://legacydevs.com/StoredItems.php";
    private List<Admin_Made_Menu> arrayList;
    private ArrayList<String> arrayList2;
    private LinearLayoutManager linearLayoutManager;
    private Button selectButton;

//    public FragmentRefreshListener getFragmentRefreshListener() {
//        return fragmentRefreshListener;
//    }
//
//    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
//        this.fragmentRefreshListener = fragmentRefreshListener;
//    }
//
//    private FragmentRefreshListener fragmentRefreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectButton = (Button) findViewById(R.id.select_button);
        populateRecyclerView();
       // loadData();
        onClickEvent();
    }
    private void populateRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
//        arrayList.add("RecyclerView Items ");
//        arrayList.add("Banana");//Adding items to recycler view
//        arrayList.add("Apple");

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(this, arrayList);
       // adapter2 = new RecyclerViewAdapter(this, arrayList2);
        recyclerView.setAdapter(adapter);
        getData();
    }
    private void onClickEvent() {
        findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                //Check if item is selected or not via size
                if (selectedRows.size() > 0) {
                    arrayList2 = new ArrayList<>();
                   // StringBuilder stringBuilder = new StringBuilder();
                    //Loop to all the selected rows array
                    //put code to populate breakfast database
                    for (int i = 0; i < selectedRows.size(); i++) {

                        //Check if selected rows have value i.e. checked item
                        if (selectedRows.valueAt(i)) {

                            //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                            String selectedRowLabel = String.valueOf(arrayList.get(selectedRows.keyAt(i)));
                            arrayList2.add(selectedRowLabel);
                            Log.d("array list ", String.valueOf(arrayList2));
                            //append the row label text
                            //stringBuilder.append(selectedRowLabel + "\n");
                        }
                    }
//                    RecyclerView recyclerView2 = (RecyclerView)findViewById(R.id.recycler_view2);
//                    recyclerView2.setHasFixedSize(true);
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
//                    recyclerView2.setLayoutManager(linearLayoutManager);
//                    adapter2 = new RecyclerViewAdapter(getBaseContext(), arrayList2);
//                    recyclerView2.setAdapter(adapter2);

                    if (selectedRows != null && !arrayList2.isEmpty()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mylist", arrayList2);
                        Make_Menu make_menu = new Make_Menu();
                        //Make_Menu make_menu = (Make_Menu) getFragmentManager().findFragmentById(R.id.fragment_container);
                        make_menu.setArguments(bundle);
                        FragmentTransaction fragment_transaction = getSupportFragmentManager().beginTransaction();
                        fragment_transaction.replace(R.id.fragment_containeryes, make_menu);
                        fragment_transaction.commit();

                        //return make_menu;
                        Log.d("ting","List being set");
                        Log.d("list", bundle.toString());

//                        Make_Menu myFragment = (Make_Menu) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                        if(myFragment != null && myFragment.isAdded(){
//                            myFragment.myRecyclerView.notifyItemRemoved();
//                        }

//                        if(getFragmentRefreshListener()!=null){
//                            getFragmentRefreshListener().onRefresh();
//                        }


//
//                        FragmentTransaction fragment_transaction = getSupportFragmentManager().beginTransaction();
//                        fragment_transaction.add(R.id.fragment_container, make_menu);
//                        fragment_transaction.commit();

//                        Intent intent = new Intent(getApplicationContext(), Make_Menu.class);
//                        intent.putExtra("mylist", arrayList2);
//                        startActivity(intent);
//                        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        Gson gson = new Gson();
//                        String json = gson.toJson(arrayList2);
//                        editor.putString("task list", json);
//                        editor.apply();
                    } else {
                        Log.e("123", "Avoiding null pointer, the routes are null!!!");

                    }


                   // Toast.makeText(.this, "Selected Rows\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
//        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
//                //Check if item is selected or not via size
//                if (selectedRows.size() > 0) {
//                    //Loop to all the selected rows array
//                    for (int i = (selectedRows.size() - 1); i >= 0; i--) {
//
//                        //Check if selected rows have value i.e. checked item
//                        if (selectedRows.valueAt(i)) {
//
//                            //remove the checked item
//                            arrayList.remove(selectedRows.keyAt(i));
//                        }
//                    }
//
//                    //notify the adapter and remove all checked selection
//                    adapter.removeSelection();
//                }
//            }
//        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //put code to populate lunch database

                //Check the current text of Select Button
                if (selectButton.getText().toString().equals(getResources().getString(R.string.select_all))) {

                    //If Text is Select All then loop to all array List items and check all of them
                    for (int i = 0; i < arrayList.size(); i++)
                        adapter.checkCheckBox(i, true);

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.deselect_all));
                } else {
                    //If button text is Deselect All remove check from all items
                    adapter.removeSelection();

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.select_all));
                }
            }
        });
    }
    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(selectitemsformenu, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("Starting Request", "Started!");
                        JSONObject jsonObject = response.getJSONObject(i);

                        Admin_Made_Menu items = new Admin_Made_Menu();
                        items.setId(jsonObject.getInt("item_id"));
                        items.setTitle(jsonObject.getString("title"));
                        //items.setIngredients(jsonObject.getString("ingredients"));
                        // items.setImage(jsonObject.getString("image_ref"));
                        items.setPrice(Float.valueOf(jsonObject.getString("item_cost")));

                        arrayList.add(items);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

//    public interface FragmentRefreshListener{
//        void onRefresh();
//    }

}

//    private  void loadData(){
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("task list", null);
//        Type type = new TypeToken<ArrayList<Data>>() {}.getType();
//        arrayList2 = gson.fromJson(json, type);
//
//        if (arrayList2 == null) {
//            arrayList2 = new ArrayList<>();
//        }
//    }


