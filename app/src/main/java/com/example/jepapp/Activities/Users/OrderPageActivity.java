package com.example.jepapp.Activities.Users;

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

import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderPageActivity extends AppCompatActivity {
    private static final String TAG = "OrderPageActivity";
    private Spinner quantity_spinner, payment_type_spinner;
    private Button order;
    TextView title;
    TextView cost;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ArrayList<UserCredentials> Userslist;
    // Bundle b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderpage_activity);
        Bundle b = getIntent().getExtras();
        title = (TextView)findViewById(R.id.order_page_title);
        cost = (TextView)findViewById(R.id.order_page_cost);
        quantity_spinner = findViewById(R.id.spinner);
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
                String dishquantity = quantity_spinner.getSelectedItem().toString().trim();
                String dishpaymentytpe = payment_type_spinner.getSelectedItem().toString().trim();
                String dishtitle = title.getText().toString().trim();
                String dishprice = cost.getText().toString().trim();
                String username = null;
                for (int i = 0; i < Userslist.size(); i++) {
                    if (mAuth.getUid().equals(Userslist.get(i).getUserID())){
                        username = Userslist.get(i).getUsername();

                    }
                }

                String key =getDb().child("AllOrders").push().getKey();
               // String A = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                Orders allorders = new Orders(mAuth.getUid(),dishtitle,dishquantity,dishprice,username,key,dishpaymentytpe);
                getDb().child("AllOrders")
                        .child(key)
                        .setValue(allorders);
                String key2 =getDb().child("Orders").push().getKey();
                Orders order = new Orders(mAuth.getUid(),dishtitle,dishquantity,dishprice,username,key,dishpaymentytpe);
                getDb().child("Orders")
                        .child(key2)
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
    public DatabaseReference getDb() {
        return myDBRef;
    }
}
