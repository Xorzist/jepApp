package com.example.jepapp.Activities.Users;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jepapp.R;

public class OrderPageActivity extends AppCompatActivity {
    private static final String TAG = "OrderPageActivity";
    private Spinner quantity_spinner;
    private Button order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderpage_activity);
        getSupportActionBar().setTitle("OrderPage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIncomingIntent();
        //addItemsOnSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");
        if (getIntent().hasExtra("name") && getIntent().hasExtra("price")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");


            String title = getIntent().getStringExtra("name");
            String amount = getIntent().getStringExtra("price");

            Toast.makeText(this, title + amount, Toast.LENGTH_SHORT).show();

            setInfo(title, amount);
        }
    }

    private void setInfo(String name, String price) {
        TextView title = findViewById(R.id.image_description);
        title.setText(name);
        TextView cost = findViewById(R.id.cost);
        cost.setText(price);
    }

    public void addListenerOnSpinnerItemSelection() {
        quantity_spinner = findViewById(R.id.spinner);
        quantity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(adapterView.getContext(),
                        "OnItemSelectedListener : " + adapterView.getItemAtPosition(i).toString(),
                        Toast.LENGTH_SHORT).show();
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


                Toast.makeText(OrderPageActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : " + String.valueOf(quantity_spinner.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();


//                Toast.makeText(OrderPageActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(OrderPageActivity.this, MakeanOrder.class);
//                intent.putExtra("name", item.getTitle());
//                intent.putExtra("price", String.valueOf(item.getPrice()));
//                mCtx.startActivity(intent);

//                    }
//                });
            }

        });
    }
}
