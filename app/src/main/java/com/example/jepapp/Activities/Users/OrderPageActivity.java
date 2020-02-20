package com.example.jepapp.Activities.Users;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class OrderPageActivity extends AppCompatActivity {
    private static final String TAG = "OrderPageActivity";
    private Spinner quantity_spinner, payment_type_spinner, paidby;
    private Button order;
    TextView title;
    TextView cost;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ArrayList<UserCredentials> Userslist;
    // Bundle b;
    //FCM variables
    private  String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String Server_key = "key=AAAAywbXNJo:APA91bETZC8P3pLjfmUN4h3spZu_u9DgTPsjuyqSewis6yGPv-pxzgND_2X-CE5U_x7GgMf5SBtqtQ7gbHTosf6acuG4By2qGtjR66aOTCx5ukw7CEU0_zi2fpV6EvV3wxJheCu_Hf8a";
    private String contentType = "application/json";
    private RequestQueue requestQueue;
    private String username;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderpage_activity);
        Bundle b = getIntent().getExtras();
        title = (TextView)findViewById(R.id.order_page_title);
        cost = (TextView)findViewById(R.id.order_page_cost);
        quantity_spinner = findViewById(R.id.spinner);
// THIS DOES NOT ACTUALLY EXIST
        paidby = findViewById(R.id.payment_type);
 // PLEASE REPLACE ABOVE
        order = (Button) findViewById(R.id.order_btn);
        payment_type_spinner = findViewById(R.id.payment_type);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();
        Userslist = new ArrayList<>();
        getSupportActionBar().setTitle("OrderPage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String ordertitle = b.getString("title");
        String amount = b.getString("price");
       // Toast.makeText(this, title + amount, Toast.LENGTH_SHORT).show();
        title.setText(ordertitle);
        cost.setText(amount);
        //setInfo(ordertitle, amount);
        //addItemsOnSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
        requestQueue= Volley.newRequestQueue(getApplicationContext());


        databaseReference = myDBRef.child("Users");
        mAuth = FirebaseAuth.getInstance();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);

                    Userslist.add(allusers);

                    // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
                }
                for (int i = 0; i < Userslist.size(); i++) {
                    if (mAuth.getUid().equals(Userslist.get(i).getUserID()))
                        username = Userslist.get(i).getUsername();
                }

//                adapter = new SelectMenuItemsAdaptertest(SelectMenuItems.this, list);
//
//                recyclerView.setAdapter(adapter);



            }@Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });



    }



    private void setInfo(String name, String price) {
        title.setText(name);
        cost.setText(price);
    }

    public void addListenerOnSpinnerItemSelection() {


        quantity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(adapterView.getContext(),
//                        "OnItemSelectedListener : " + adapterView.getItemAtPosition(i).toString(),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        payment_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void addListenerOnButton() {

        order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                runnotification();
                String dishquantity = quantity_spinner.getSelectedItem().toString().trim();
                String dishpaymentytpe = payment_type_spinner.getSelectedItem().toString().trim();
                String dishtitle = title.getText().toString().trim();
                String dishprice = cost.getText().toString().trim();
                String dishpaidby = paidby.getSelectedItem().toString().trim();

                String key =getDb().child("AllOrders").push().getKey();
               // String A = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                Orders allorders = new Orders(mAuth.getUid(),dishtitle,dishquantity,dishprice,username,key,dishpaymentytpe, dishpaidby);
                getDb().child("AllOrders")
                        .child(key)
                        .setValue(allorders);
                //String key2 =getDb().child("Orders").push().getKey();
                Orders order = new Orders(mAuth.getUid(),dishtitle,dishquantity,dishprice,username,key,dishpaymentytpe, dishpaidby);
                getDb().child("Orders")
                        .child(key)
                        .setValue(order);
                Log.d("Start Adding","Your order has been made");
                Toast.makeText(getApplicationContext(),"Your order has been placed",Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

//                Toast.makeText(OrderPageActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(OrderPageActivity.this, MyOrders.class);
//                intent.putExtra("name", item.getTitle());
//                intent.putExtra("price", String.valueOf(item.getPrice()));
//                mCtx.startActivity(intent);

//                    }
//                });


        });
    }

    private void runnotification() {
        String topic = "/topics/Orders";
        JSONObject notification = new JSONObject();
        JSONObject notificationbody = new JSONObject();

        try{
            notificationbody.put("title","Orders Notification");
            notificationbody.put("message",username+" has made a new order");
            notification  .put("to",topic);
            notification.put("data",notificationbody);
            Log.e("runnotification: ","Succeeded");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("runnotification: ","Failed");
        }
        sendNotification(notification);


    }

    private final void sendNotification(JSONObject notification) {
        Log.e("TAG", "sendNotification");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(this.FCM_API, notification,(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response1", response.toString());

            }
        })
        ,(new Response.ErrorListener() {
            public final void onErrorResponse(VolleyError it) {
                Toast.makeText(getApplicationContext(),"Did not work",Toast.LENGTH_LONG).show();
                Log.i("ErrorResponse", "onErrorResponse: Didn't work");
            }
        })) {
            @NotNull
            public Map<String,String> getHeaders() {
                HashMap params = new HashMap<String,String>();
                params.put("Authorization", OrderPageActivity.this.Server_key);
                params.put("Content-Type", OrderPageActivity.this.contentType);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public DatabaseReference getDb() {
        return myDBRef;
    }
}
