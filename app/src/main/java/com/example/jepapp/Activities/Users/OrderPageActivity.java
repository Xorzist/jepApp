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
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderPageActivity extends AppCompatActivity {
    private static final String TAG = "OrderPageActivity";
    private Spinner quantity_spinner;
    private Button order;
    TextView title;
    TextView cost;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
   // Bundle b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderpage_activity);
        Bundle b = getIntent().getExtras();
        title = (TextView)findViewById(R.id.image_description);
        cost = (TextView)findViewById(R.id.cost);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();

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
    }



    private void setInfo(String name, String price) {
        title.setText(name);
        cost.setText(price);
    }

    public void addListenerOnSpinnerItemSelection() {
        quantity_spinner = findViewById(R.id.spinner);
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
    }

    public void addListenerOnButton() {

        quantity_spinner = (Spinner) findViewById(R.id.spinner);
        order = (Button) findViewById(R.id.order_btn);

        order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String dishquantity = quantity_spinner.getSelectedItem().toString().trim();
                String dishtitle = title.getText().toString().trim();

                Orders mItems = new Orders(mAuth.getUid(),dishtitle,dishquantity);
                String key =getDb().child("Orders").push().getKey();
                getDb().child("Orders")
                        .child(key)
                        .setValue(mItems);
                Log.d("Start Adding","Your order has been made");
                Toast.makeText(getApplicationContext(),"Your order has been placed",Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

//                Toast.makeText(OrderPageActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(OrderPageActivity.this, MakeanOrder.class);
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
