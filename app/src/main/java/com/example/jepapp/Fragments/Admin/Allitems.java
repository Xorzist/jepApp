package com.example.jepapp.Fragments.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jepapp.Activities.Admin.CreatingItem;
import com.example.jepapp.Adapters.AllitemsAdapter;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.example.jepapp.SessionPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Allitems extends Fragment {
    //new thing added
    private boolean shouldRefreshOnResume = false;
    private static final Object TAG ="All Item Class";
    SessionPref session;
    ProgressBar progressBar;
    RecyclerView.Adapter adapter;
    RecyclerView Theitems;

    String allitemsurl = "http://legacydevs.com/StoredItems.php";
    FloatingActionButton fabcreatebtn;
    private List<MItems> MenuItemsList;

    private RequestQueue mRequestq;
    private Bitmap bitmap;
    private static CreateItem createiteminstance;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.all_imenu_items, container, false);
        Theitems = rootView.findViewById(R.id.allmenuitems);
        MenuItemsList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(Theitems.getContext(), linearLayoutManager.getOrientation());
        adapter = new AllitemsAdapter(getContext(),MenuItemsList);
        Theitems.setLayoutManager(linearLayoutManager);
        Theitems.addItemDecoration(dividerItemDecoration);
        Theitems.setAdapter(adapter);
        getData();

        fabcreatebtn=rootView.findViewById(R.id.createitembtn);
        fabcreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreatingItem.class);
                startActivity(intent);
            }
        });

        return  rootView;

    }

//    private void ItemCreator() {
//
//        class ItemCreator extends AsyncTask<Void,Void,String> {
//            //private ProgressBar progressBar;
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                //Creates a request handler object
//                RequestHandler requestHandler = new RequestHandler();
//
//                //Creating input parameters
//                HashMap<String, String> params = new HashMap<String, String>();
//
//                // Returns rhe server response
//                return  requestHandler.sendPostRequest(allitemsurl,params);
//
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                //display the progress bar while request is executed
//                //progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected void onPostExecute(String response) {
//                super.onPostExecute(response);
//                //progressBar.setVisibility(View.GONE);
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {
//                        String globaluid = jObj.getString("item_id");
//                        MItems items = new MItems();
//                        items.setId(jObj.getInt("item_id"));
//                        items.setTitle(jObj.getString("title"));
//                        items.setIngredients(jObj.getString("ingredients"));
//                        items.setImage(jObj.getString("image_ref"));
//                        items.setPrice(Float.valueOf(jObj.getString("item_cost")));
//
//                        MenuItemsList.add(items);
//                        Log.d(String.valueOf(TAG), "Loadup Response: " + globaluid);
//
//                        Toast.makeText(getContext(), "Items have been loaded", Toast.LENGTH_LONG).show();
//                    } else {
//
//                        // Error occurred in creation. Get the error
//                        // message
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        } new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(String.valueOf(TAG), "Loadup Error: " + error.getMessage());
//                Toast.makeText(getContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        };
//
//
//        ItemCreator IC=new ItemCreator();
//        IC.execute();
//    }
    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(allitemsurl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("Starting Request", "Started!");
                        JSONObject jsonObject = response.getJSONObject(i);

                        MItems items = new MItems();
                        items.setId(String.valueOf(jsonObject.getInt("item_id")));
                        items.setTitle(jsonObject.getString("title"));
                        items.setIngredients(jsonObject.getString("ingredients"));
                        items.setImage(jsonObject.getString("image_ref"));
                        items.setPrice(Float.valueOf(jsonObject.getString("item_cost")));

                        MenuItemsList.add(items);
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

    //new tings added
    @Override
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        if(shouldRefreshOnResume){
            Fragment newFragment = new Allitems();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.containeritems, newFragment).commit();
            // refresh fragment
        }
    }

    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }
}
