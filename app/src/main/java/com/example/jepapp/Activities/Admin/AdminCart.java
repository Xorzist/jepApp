package com.example.jepapp.Activities.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jepapp.Adapters.Users.cartAdapter;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.Ordertitle;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.time.LocalTime.parse;

public class AdminCart extends AppCompatActivity {
    cartAdapter admincartadapter;
    private DatabaseReference myDBRef;
    Button admincartcheckout, addtocart;
    private ArrayList<com.example.jepapp.Models.Cart> admincart;
    //Will be used to store all user emails
    private ArrayList<String> allusersempid;
    RecyclerView admincartrecycler;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private DatabaseReference databaseReference;

    private String intentusername, date, time, ordertype, paymenttype, paidby, specialrequest, status, orderid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_admin);
        Bundle intent = getIntent().getExtras();
        intentusername = intent.getString("username");
        date = intent.getString("date");
        time = intent.getString("time");
        ordertype = intent.getString("ordertype");
        orderid = intent.getString("id");
        paymenttype = intent.getString("paymenttype");
        paidby = intent.getString("paidby");
        specialrequest = intent.getString("specialrequest");
        status = intent.getString("status");
        admincart = new ArrayList<>();
        allusersempid = new ArrayList<>();
        admincartcheckout = findViewById(R.id.admincheckout);
        addtocart = findViewById(R.id.add_to_cart);
        admincartrecycler = findViewById(R.id.admincartlist);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        dividerItemDecoration = new DividerItemDecoration(admincartrecycler.getContext(), linearLayoutManager.getOrientation());
        admincartadapter = new cartAdapter(AdminCart.this, admincart);
        admincartrecycler.setLayoutManager(linearLayoutManager);
        admincartrecycler.addItemDecoration(dividerItemDecoration);
        admincartrecycler.setAdapter(admincartadapter);

        //dbreference for breakfast cart table
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastCart").child("Admin");
        //Method to get all items in the breakfast cart
        getadmincart();


        admincartcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function to handle the admin checkout button
                admincheckingout();


            }
        });
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCart.this, AllMenuItemsList.class);
                startActivity(intent);
            }
        });


    }

    private void admincheckingout() {
        final ArrayList<String> ordertitles = new ArrayList<>();
        Long totalvalue = 0L;

        //Check If the lunch cart is empty
        if (admincart.size() > 0) {
            for (int i = 0; i < admincart.size(); i++) {
                //add the order titles with their quantity to a list
                ordertitles.add(new Ordertitle().setItemname(admincart.get(i).getOrdertitle() + "(x" + admincart.get(i).getQuantity() + ")," + " "));
                //Calculate the total cost of cost times the quantity of items
                Double costvalue = Double.valueOf(admincart.get(i).getCost());
                totalvalue = totalvalue + ((costvalue.longValue()) * Long.valueOf(admincart.get(i).getQuantity()));
            }
            Log.e("working", ordertype);
            //Open the Dialog to show order details
            checkoutdialog(totalvalue, ordertitles, ordertype);
        } else {
            Toast.makeText(getApplicationContext(), "You have no items in the cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkoutdialog(final Long totalvalue, ArrayList<String> ordertitles, String ordertype) {
        final ArrayList<String> Ordertitles = ordertitles;
        //final String Ordertype = ordertype;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Order");
      //  ItemCreator(Long.valueOf(totalvalue), date, Ordertitles, paidby, paymenttype,
              //  String.valueOf(admincart.size()), specialrequest, status, time, ordertype, intentusername);
        update_ordertitles(Ordertitles, orderid, ordertype,String.valueOf(admincart.size()));
        databaseReference.removeValue();
        progressDialog.dismiss();
        finish();
        //Create Alert Builder

    }


    private void getadmincart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                admincart.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Cart breakfastitems = dataSnapshot.getValue(com.example.jepapp.Models.Cart.class);

                    admincart.add(breakfastitems);
                }


                admincartadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void ItemCreator(Long mcost, String mdate, ArrayList<String> mordertitles, String mpaidby,
                             String mpayment_type, String mquantity, String mrequest, String mstatus, String mtime, String mtype, String musername) {
        Orders orders;
        String key = myDBRef.child(mtype + "Orders").push().getKey();
        orders = new Orders(mcost, mdate, key, mordertitles, mpaidby, mpayment_type, mquantity, mrequest, mstatus, mtime, mtype, musername);
        myDBRef.child(mtype + "Orders")
                .child(key)
                .setValue(orders);
        Log.d("Start Adding", "START!");
    }

    public void Reloadit() {
        finish();
        startActivity(getIntent());
    }

    private void update_ordertitles(final ArrayList<String> item, final String orderid, String ordertype, final String s) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child(ordertype + "Orders");
        Query update_state = databaseReference.orderByChild("orderID").equalTo(orderid);
        Log.e("checking", "Breakfast");
        update_state.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot updateQuantity : dataSnapshot.getChildren()) {
                    updateQuantity.getRef().child("ordertitle").setValue(item);
                    updateQuantity.getRef().child("quantity").setValue(s);

                    Log.e("uodated", status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        backbutton();
        return;
    }

    private void backbutton() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AdminCart.this);
        alert.setTitle("Cancel changes?");
        alert.setMessage("Are you sure you want to exit? \nAll changes made will be lost");
        alert.setIcon(R.drawable.minusicon);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                databaseReference.removeValue();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}
