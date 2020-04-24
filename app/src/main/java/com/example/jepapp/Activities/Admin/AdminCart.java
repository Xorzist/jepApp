package com.example.jepapp.Activities.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.jepapp.Models.Ordertitle;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;


public class AdminCart extends AppCompatActivity {
    cartAdapter admincartadapter;
    private DatabaseReference myDBRef;
    Button admincartcheckout, addtocart;
    private ArrayList<com.example.jepapp.Models.Cart> admincart;
    RecyclerView admincartrecycler;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private DatabaseReference databaseReference;
    private String ordertype;
    private String orderid;

    public AdminCart() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_admin);
        Bundle intent = getIntent().getExtras();
        //get intent data
        ordertype = intent.getString("ordertype");
        orderid = intent.getString("id");
        admincart = new ArrayList<>();
        admincartcheckout = findViewById(R.id.admincheckout);
        addtocart = findViewById(R.id.add_to_cart);
        admincartrecycler = findViewById(R.id.admincartlist);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        //setting title for action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("All items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                Intent intent = new Intent(AdminCart.this, MenuItemsListForAdmin.class);
                startActivity(intent);
            }
        });


    }

    private void admincheckingout() {
        final ArrayList<String> ordertitles = new ArrayList<>();
        Long totalvalue = 0L;

        //Check If the cart is empty
        if (admincart.size() > 0) {
            for (int i = 0; i < admincart.size(); i++) {
                //add the order titles with their quantity to a list
                ordertitles.add(new Ordertitle().setItemname(admincart.get(i).getOrdertitle() + "(x" + admincart.get(i).getQuantity() + ")," + " "));
                //Calculate the total cost of cost times the quantity of items
                Double costvalue = Double.valueOf(admincart.get(i).getCost());
                totalvalue = totalvalue + ((costvalue.longValue()) * Long.valueOf(admincart.get(i).getQuantity()));
            }
            //Open the Dialog to show order details
            checkoutdialog(totalvalue, ordertitles, ordertype);
        } else {
            Toast.makeText(getApplicationContext(), "You have no items in the cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkoutdialog(final Long totalvalue, ArrayList<String> ordertitles, String ordertype) {
        final ArrayList<String> Ordertitles = ordertitles;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Order");
        //updates the order title information as well as the cost
        update_ordertitles(Ordertitles, orderid, ordertype,String.valueOf(admincart.size()),totalvalue);
        //deletes the cart
        databaseReference.removeValue();
        progressDialog.dismiss();
        finish();


    }

    //get cart details from firebase
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



    private void update_ordertitles(final ArrayList<String> item, final String orderid, String ordertype, final String s, final Long totalvalue) {
        //updates the order titles, quantities and cost
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child(ordertype + "Orders");
        Query update_state = databaseReference.orderByChild("orderID").equalTo(orderid);
        update_state.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot updateQuantity : dataSnapshot.getChildren()) {
                    updateQuantity.getRef().child("ordertitle").setValue(item);
                    updateQuantity.getRef().child("quantity").setValue(s);
                    updateQuantity.getRef().child("cost").setValue(totalvalue);
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
    //overriding the system back button
    private void backbutton() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AdminCart.this);
        alert.setTitle("Cancel changes?");
        alert.setMessage("Are you sure you want to exit? \nAll changes made will be lost");
        alert.setIcon(R.drawable.minusicon);
        alert.setPositiveButton(R.string.dialogYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                databaseReference.removeValue();
            }
        });
        alert.setNegativeButton(R.string.dialogNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}
