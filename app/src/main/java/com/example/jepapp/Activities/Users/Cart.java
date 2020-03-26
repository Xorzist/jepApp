package com.example.jepapp.Activities.Users;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jepapp.Adapters.Users.cartAdapter;
import com.example.jepapp.Models.Cut_Off_Time;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.Ordertitle;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Cart extends AppCompatActivity {
    cartAdapter breakfastadapter, lunchadapter;
    private DatabaseReference myDBRef,databaseReferencelunch,databaseReferenceusers,
    referencecutofftime;
    private FirebaseAuth mAuth;
    Button breakfastcheckout,lunchcheckout;
    private ArrayList<com.example.jepapp.Models.Cart> breakfastcart;
    private ArrayList<com.example.jepapp.Models.Cart> lunchcart;
    //Will be used to store all user emails
    private ArrayList<String> allusersempid;
    RecyclerView breakfastrecycler,lunchrecycler;
    private LinearLayoutManager linearLayoutManager,linearLayoutManager2;
    private DividerItemDecoration dividerItemDecoration;
    private DividerItemDecoration dividerItemDecoration2;
    private DatabaseReference databaseReferencebreakfast;
    private String email,username,balance;
    private SimpleDateFormat SimpleDateFormat,simpleTimeFormat;
    private Date datenow;
    private String employeeid;
    private String breakfastapptime,lunchapptime;
    SimpleDateFormat parseFormat;
    private ArrayList<Cut_Off_Time> cuttoftimes = new ArrayList<>();
    DateFormat inputFormat;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_cart);
        breakfastcart = new ArrayList<>();
        lunchcart = new ArrayList<com.example.jepapp.Models.Cart>();
        allusersempid = new ArrayList<>();
        cuttoftimes = new ArrayList<>();
        breakfastcheckout = findViewById(R.id.breakfastcheckout);
        lunchcheckout = findViewById(R.id.lunchcheckout);
        breakfastrecycler = findViewById(R.id.cartbreakfastlist);
        lunchrecycler = findViewById(R.id.cartlunchlist);
         mAuth = FirebaseAuth.getInstance();
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        SimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleTimeFormat = new SimpleDateFormat("HH:mm");
        parseFormat = new SimpleDateFormat("hh:mm a");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm a.SSSX");

        datenow = new Date();


        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        dividerItemDecoration = new DividerItemDecoration(breakfastrecycler.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration2 = new DividerItemDecoration(lunchrecycler.getContext(), linearLayoutManager.getOrientation());
        breakfastadapter = new cartAdapter(Cart.this,breakfastcart);
        lunchadapter = new cartAdapter(Cart.this,lunchcart);

        breakfastrecycler.setLayoutManager(linearLayoutManager2);
        lunchrecycler.setLayoutManager(linearLayoutManager);
        breakfastrecycler.addItemDecoration(dividerItemDecoration);
        lunchrecycler.addItemDecoration(dividerItemDecoration2);
        breakfastrecycler.setAdapter(breakfastadapter);
        lunchrecycler.setAdapter(lunchadapter);

        email = mAuth.getCurrentUser().getEmail().replace(".","");
        //Method to get the username
        DoUsernamequery();
        //dbreference for breakfast table
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastCart").child(email);
        //Method to get all items in the breakfast cart
       getbreakfastcart();
        //dbreference for lunch table
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchCart").child(email);
        //Method to get all items in the lunch cart
        getLunchcart();
        //dbreference for users table
        databaseReferenceusers = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        //Method to get all users in the Users table
        getAllUsers();
        referencecutofftime = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");

        //Method to get the  breakfast cut off time set by the admin
        //Method to get the lunch cut off time set by the admin
        Cutofftimesgetter();



        breakfastcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function to handle the breakfast checkout button

                try {
                    //If the user tries to access the menu after cut off time
                    Date timenow = simpleTimeFormat.parse(simpleTimeFormat.format(datenow));
                    Date bapptime = simpleTimeFormat.parse(breakfastapptime);
                    if(timenow.after(bapptime)){
                        new AlertDialog.Builder(Cart.this)
                                .setTitle("Orders Cut of Time")
                                .setMessage("Sorry,the time for ordering breakfast has passed")
                                .setPositiveButton("Okay",null)
                                .setIcon(R.drawable.adminprofile)
                                .show();

                    }
                    else{
                        breakfastcheckingout();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        lunchcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function to handle the lunch checkout button
                try {
                    //If the user tries to access the menu after cut off time
                    Date timenow = simpleTimeFormat.parse(simpleTimeFormat.format(datenow));
                    Date lunchtime = simpleTimeFormat.parse(lunchapptime);
                    if(timenow.after(lunchtime)){
                        new AlertDialog.Builder(Cart.this)
                                .setTitle("Orders Cut of Time")
                                .setMessage("Sorry,the time for ordering Lunch has passed")
                                .setPositiveButton("Okay",null)
                                .setIcon(R.drawable.adminprofile)
                                .show();

                    }
                    else{
                        lunchcheckingout();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void Cutofftimesgetter() {
        referencecutofftime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cut_Off_Time cuttofftime = dataSnapshot.getValue(Cut_Off_Time.class);
                    cuttoftimes.add(cuttofftime);

                }
                //Assign the breakfast and lunch times respectively straight from the database
                String dbbreakfasttime = cuttoftimes.get(0).getTime();
                String dblunchtime = cuttoftimes.get(1).getTime();

                try {
                    breakfastapptime = simpleTimeFormat.format(parseFormat.parse(dbbreakfasttime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    lunchapptime = simpleTimeFormat.format(parseFormat.parse(dblunchtime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("formatted breakfast!!", (breakfastapptime));
                Log.e("formatted breakfast!!", (lunchapptime));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void lunchcheckingout() {
        final ArrayList<String> ordertitles = new ArrayList<>();
        Long totalvalue = 0L;


        //Check If the lunch cart is empty
        if(lunchcart.size()>0){
            //add the order titles with their quantity to a list
            for (int i = 0; i <lunchcart.size(); i++){
               ordertitles.add(new Ordertitle().setItemname(" " + lunchcart.get(i).getOrdertitle() +"(x"+lunchcart.get(i).getQuantity()+"),"));
                //Calculate the total cost of cost times the quantity of items
                Double costvalue = Double.valueOf(lunchcart.get(i).getCost());
                totalvalue= totalvalue+(((costvalue.longValue())*Long.valueOf(lunchcart.get(i).getQuantity())));
            }
            //Open the Dialog to show order details
            checkoutdialog(totalvalue,ordertitles,"Lunch");
        }

        else {
            Toast.makeText(getApplicationContext(),"You have no items in the lunch cart",Toast.LENGTH_SHORT).show();
        }
    }
    private void breakfastcheckingout() {
        final ArrayList<String> ordertitles = new ArrayList<>();
        Long totalvalue = 0L;

        //Check If the lunch cart is empty
        if(breakfastcart.size()>0){
            for (int i = 0; i <breakfastcart.size(); i++){
                //add the order titles with their quantity to a list
               ordertitles.add(new Ordertitle().setItemname(" " + breakfastcart.get(i).getOrdertitle() +"(x"+breakfastcart.get(i).getQuantity()+")"));
                //Calculate the total cost of cost times the quantity of items
                Double costvalue = Double.valueOf(breakfastcart.get(i).getCost());
                totalvalue= totalvalue+((costvalue.longValue())*Long.valueOf(breakfastcart.get(i).getQuantity()));
            }
            Log.e("working", "Dialog now " );
            //Open the Dialog to show order details
            checkoutdialog(totalvalue,ordertitles,"Breakfast");
        }

        else {
            Toast.makeText(getApplicationContext(),"You have no items in the lunch cart",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkoutdialog(final Long totalvalue, ArrayList<String> ordertitles, String ordertype) {
        final ArrayList<String> Ordertitles = ordertitles;
        final String Ordertype = ordertype;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");

        //Create Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Ordertype+" Checkout");
        //Add Custom Layout
        final View customLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.checkoutpage, null);
        builder.setView(customLayout);
        RadioButton myself = customLayout.findViewById(R.id.myself);
        final RadioButton other = customLayout.findViewById(R.id.other);
        final RadioGroup paybygroup = customLayout.findViewById(R.id.paybygroup);
        paybygroup.check(R.id.myself);
        //setup adapter containing emails to use for prediction of text for user payby
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, allusersempid);
        final AutoCompleteTextView autoCompleteTextView = customLayout.findViewById(R.id.autoCompleter);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setAdapter(adapter);
        final Spinner paymentspinner = customLayout.findViewById(R.id.paymentypespinner);
        final EditText specialrequest = customLayout.findViewById(R.id.requestfield);
        final TextView totalcost = customLayout.findViewById(R.id.totalcost);
        totalcost.setText(String.valueOf(totalvalue));
        paybygroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //statment to check if user is paying for meal himself or otherwise
                if (checkedId == R.id.other){
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                }
                else{
                    autoCompleteTextView.setVisibility(View.INVISIBLE);
                }
            }
        });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //Setup button to terminate the dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selected = paymentspinner.getSelectedItem().toString();
                String payer = new String("empty");
                //Check if user can afford the order
                 if  ((Float.valueOf(balance)-totalvalue)<0 && !paymentspinner.getSelectedItem().equals("Cash")) {
                     Toast.makeText(customLayout.getContext(), "Your balance is insufficient for this order", Toast.LENGTH_SHORT).show();
                 }
                 else{
                     if (paybygroup.getCheckedRadioButtonId() == R.id.myself) {
                         payer = employeeid;

                         // If statements to clear the corresponding cart
                         if (Ordertype.equals("Lunch")) {
                             ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
                                     paymentspinner.getSelectedItem().toString(), String.valueOf(lunchcart.size()), specialrequest.getText().toString(),
                                     "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
                             databaseReferencelunch.removeValue();
                             progressDialog.dismiss();
                             dialog.cancel();
                             onBackPressed();
                             Log.e("grgrg", balance );
                         } else if (Ordertype.equals("Breakfast")) {
                             ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
                                     paymentspinner.getSelectedItem().toString(), String.valueOf(breakfastcart.size()), specialrequest.getText().toString(),
                                     "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
                             databaseReferencebreakfast.removeValue();
                             progressDialog.dismiss();
                             dialog.cancel();
                             onBackPressed();

                         }
                     }

                     //Check if user has selected other as who will pay
                     else if (paybygroup.getCheckedRadioButtonId() == R.id.other) {
                         if (autoCompleteTextView.getText().toString().isEmpty() ) {
                             Toast.makeText(customLayout.getContext(), "Please enter a valid username", Toast.LENGTH_SHORT).show();
                         } else {
                             payer = autoCompleteTextView.getText().toString();
                             progressDialog.show();
                             // If statements to perform ordering on the corresponding cart
                             if (Ordertype.equals("Lunch")) {
                                 ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
                                         paymentspinner.getSelectedItem().toString(), String.valueOf(lunchcart.size()), specialrequest.getText().toString(),
                                         "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
                                 databaseReferencelunch.removeValue();
                                 progressDialog.dismiss();
                                 dialog.cancel();
                                 onBackPressed();

                             } else if (Ordertype.equals("Breakfast")) {
                                 ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
                                         paymentspinner.getSelectedItem().toString(), String.valueOf(breakfastcart.size()), specialrequest.getText().toString(),
                                         "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
                                 databaseReferencebreakfast.removeValue();
                                 progressDialog.dismiss();
                                 dialog.cancel();
                                 onBackPressed();

                             }
                         }
                     }

                 }
            }
        });
    }

    private boolean userscheck(String otheruser) {
        boolean returner = false;
        for (String usernames : allusersempid){

            returner = otheruser.equals(usernames);

        }
        return returner;
    }


    private void getLunchcart() {
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Cart lunchitems = dataSnapshot.getValue(com.example.jepapp.Models.Cart.class);


                    lunchcart.add(lunchitems);
                }


                breakfastadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getbreakfastcart() {
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                breakfastcart.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Cart breakfastitems = dataSnapshot.getValue(com.example.jepapp.Models.Cart.class);

                    breakfastcart.add(breakfastitems);
                }


                lunchadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getAllUsers() {
        databaseReferenceusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allusersempid.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials useremails = dataSnapshot.getValue(UserCredentials.class);

                    allusersempid.add(useremails.getEmpID());
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
        public void DoUsernamequery(){
        //This function will assign the username of the current user to a variable
            Query emailquery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

            emailquery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);
                        //Log.e("onDataChange: ", allmyorders.getTitle().toString());

                        //Set the username and balance of the current user
                        username = userCredentials.getUsername();
                        balance = userCredentials.getBalance();
                        employeeid = userCredentials.getEmpID();


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

        }


    private void ItemCreator(Long mcost, String mdate, ArrayList<String> mordertitles, String mpaidby,
                             String mpayment_type, String mquantity, String mrequest, String mstatus, String mtime, String mtype, String musername) {
        Orders orders;
        String key =myDBRef.child(mtype+"Orders").push().getKey();
        orders = new Orders(mcost,mdate,key, mordertitles,mpaidby,mpayment_type,mquantity,mrequest,mstatus,mtime,mtype,musername);
        myDBRef.child(mtype+"Orders")
                .child(key)
                .setValue(orders);
        Log.d("Start Adding","START!");
    }
    public void Reloadit(){
        finish();
        startActivity(getIntent());
    }
}
