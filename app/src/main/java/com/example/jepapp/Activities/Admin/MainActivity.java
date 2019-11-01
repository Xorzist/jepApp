package com.example.jepapp.Activities.Admin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jepapp.Adapters.RecyclerViewAdapter;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;
import com.example.jepapp.RequestHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private static final Object TAG ="Creating Menu";
    private RecyclerViewAdapter adapter;
    String selectitemsformenu = "http://legacydevs.com/StoredItems.php";
    List<Admin_Made_Menu> arrayList;
    private ArrayList<String> arrayListTitles, arrayListQuantities;
    private LinearLayoutManager linearLayoutManager;
    String creatorurl = "http://legacydevs.com/BreakfastMenuStore.php";
    String updaterurl = "http://legacydevs.com/UPDATEBreakfast.php";
    String updatelunch = "http://legacydevs.com/UPDATELunch.php";
    private String deletedb="http://legacydevs.com/ClearBreakfastTable.php";
    //needs to be changed
    private String deletedblunch="http://legacydevs.com";
    private Button selectButton;
    private ProgressBar progressBar;
    private CheckBox checker;
    private EditText quantity;
    ProgressDialog progressDialog;
    private TextView title;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    //RecyclerView.Adapter adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectButton = (Button) findViewById(R.id.save_lunch);
        title = (TextView) findViewById(R.id.label);
        checker = (CheckBox) findViewById(R.id.checkbox1);
        quantity = (EditText) findViewById(R.id.quantity);
        progressBar = (ProgressBar) findViewById(R.id.menuprogressor);


        populateRecyclerView();
       // loadData();
       // onClickEvent();
    }
    private void populateRecyclerView() {
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        progressDialog = new ProgressDialog(MainActivity.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();
       // progressBar.setVisibility();
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
//        arrayList.add("RecyclerView Items ");
//        arrayList.add("Banana");//Adding items to recycler view
//        arrayList.add("Apple");
        adapter = new RecyclerViewAdapter(this,arrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference("MenuItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu menuitemswithcheckbox = dataSnapshot.getValue(Admin_Made_Menu.class);

                    arrayList.add(menuitemswithcheckbox);
                }
                adapter.notifyDataSetChanged();
               // adapter = new RecyclerViewAdapter(MainActivity.this,arrayList );

                recyclerView.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }

//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapter = new RecyclerViewAdapter(this, arrayList);
//       // adapter2 = new RecyclerViewAdapter(this, arrayList2);
//        recyclerView.setAdapter(adapter);
//
//        getData();

            });
        return;
    }
    private void onClickEvent() {
        findViewById(R.id.save_breakfast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearData();
                arrayListQuantities = adapter.getQuantity();
                arrayListTitles = adapter.getTitle();

                for (int i=0; i < arrayListTitles.size(); i++){
                    String title = arrayListTitles.get(i);
                    String quantity = arrayListQuantities.get(i);
                    ItemCreator( quantity.trim(),title.trim());
                    ItemUpdater(quantity.trim(),title.trim());
                }
                onBackPressed();

               }

            //}
        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClearDataLunch();
                arrayListQuantities = adapter.getQuantity();
                arrayListTitles = adapter.getTitle();

                for (int i=0; i < arrayListTitles.size(); i++){
                    String title = arrayListTitles.get(i);
                    String quantity = arrayListQuantities.get(i);
                    ItemCreator2( quantity.trim(),title.trim());
                    ItemUpdater2(quantity.trim(),title.trim());
                }
                onBackPressed();

            }
        });
    }

    private void ItemCreator(String quantity, String title) {
        final String menutitle = title.trim();
        final String menuquantity = quantity.trim();

        class ItemCreator extends AsyncTask<Void,Void,String> {
            //private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //Creates a request handler object
                RequestHandler requestHandler = new RequestHandler();

                //Creating input parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("title", menutitle);
                params.put("quantity", menuquantity);


                // Returns rhe server response
                return  requestHandler.sendPostRequest(creatorurl,params);


            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //display the progress bar while request is executed
                progressBar.setVisibility(View.VISIBLE);
                Log.e("onPreExecute: ","Started adding" );
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                progressBar.setVisibility(View.GONE);
                Log.d("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d(String.valueOf(TAG), "Creation Response: " + response);

                        Toast.makeText(getApplicationContext(), "Menu has been successfully created", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in creation. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(String.valueOf(TAG), "Creation Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };


        ItemCreator IC = new ItemCreator();
        IC.execute();
    }
    private void ItemUpdater(String quantity, String title) {
        final String menutitle = title.trim();
        final String menuquantity = quantity.trim();

        class ItemUpdater extends AsyncTask<Void,Void,String> {
            //private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //Creates a request handler object
                RequestHandler requestHandler = new RequestHandler();

                //Creating input parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("title", menutitle);
                params.put("quantity", menuquantity);


                // Returns rhe server response
                return  requestHandler.sendPostRequest(updaterurl,params);


            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //display the progress bar while request is executed
                progressBar.setVisibility(View.VISIBLE);
                Log.e("onPreExecute: ","Started updating" );
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                progressBar.setVisibility(View.GONE);
                Log.d("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d(String.valueOf(TAG), "Creation Response: " + response);

                        Toast.makeText(getApplicationContext(), "Menu has been successfully created", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in creation. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(String.valueOf(TAG), "Creation Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };


        ItemUpdater IU = new ItemUpdater();
        IU.execute();
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
                        items.setId(jsonObject.getString("item_id"));
                        items.setTitle(jsonObject.getString("title"));
                        //items.setIngredients(jsonObject.getString("ingredients"));
                        // items.setImage(jsonObject.getString("image_ref"));
                        items.setPrice(Float.valueOf(jsonObject.getString("item_cost")));

                        String a = jsonObject.getString("title");
                        //arrayList2.add(a);
                        Log.e("hhh",a);

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

    private void ClearData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Clearing...");
        progressDialog.show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(deletedb, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Starting Request", "Started!");
                Log.d("mhm","Yahsuh it start");
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

    private void ClearDataLunch() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Clearing...");
        progressDialog.show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(deletedblunch, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Starting Request", "Started!");
                Log.d("mhm","Yahsuh it start");
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



    private void ItemCreator2(String quantity, String title) {
        final String menutitle = title.trim();
        final String menuquantity = quantity.trim();

        class ItemCreator extends AsyncTask<Void,Void,String> {
            //private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //Creates a request handler object
                RequestHandler requestHandler = new RequestHandler();

                //Creating input parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("title", menutitle);
                params.put("quantity", menuquantity);


                // Returns rhe server response
                return  requestHandler.sendPostRequest(creatorurl,params);


            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //display the progress bar while request is executed
                progressBar.setVisibility(View.VISIBLE);
                Log.e("onPreExecute: ","Started adding" );
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                progressBar.setVisibility(View.GONE);
                Log.d("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d(String.valueOf(TAG), "Creation Response: " + response);

                        Toast.makeText(getApplicationContext(), "Menu has been successfully created", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in creation. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(String.valueOf(TAG), "Creation Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };


        ItemCreator IC = new ItemCreator();
        IC.execute();
    }
    private void ItemUpdater2(String quantity, String title) {
        final String menutitle = title.trim();
        final String menuquantity = quantity.trim();

        class ItemUpdater extends AsyncTask<Void,Void,String> {
            //private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //Creates a request handler object
                RequestHandler requestHandler = new RequestHandler();

                //Creating input parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("title", menutitle);
                params.put("quantity", menuquantity);


                // Returns rhe server response

                //shculd be changed to updatelunch
                //return requestHandler.sendPostRequest(updatelunch,params);
                return  requestHandler.sendPostRequest(updaterurl,params);


            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //display the progress bar while request is executed
                progressBar.setVisibility(View.VISIBLE);
                Log.e("onPreExecute: ","Started updating" );
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                progressBar.setVisibility(View.GONE);
                Log.d("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d(String.valueOf(TAG), "Creation Response: " + response);

                        Toast.makeText(getApplicationContext(), "Menu has been successfully created", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in creation. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(String.valueOf(TAG), "Creation Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };


        ItemUpdater IU = new ItemUpdater();
        IU.execute();
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


