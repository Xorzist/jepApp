package com.example.jepapp.Activities.Users;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

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


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jepapp.Adapters.Users.cartAdapter;
import com.example.jepapp.GMailSender;
import com.example.jepapp.Models.Cut_Off_Time;
import com.example.jepapp.Models.FoodItem;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.security.AccessController.getContext;


public class Cart extends AppCompatActivity {
    cartAdapter breakfastadapter, lunchadapter;
    private DatabaseReference myDBRef,databaseReferencelunch,databaseReferenceusers,
    referencecutofftime;
    private FirebaseAuth mAuth;
    Button breakfastcheckout,lunchcheckout;
    private ArrayList<com.example.jepapp.Models.Cart> breakfastcart;
    private ArrayList<com.example.jepapp.Models.Cart> lunchcart;
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
    private double total_breakfast;
    private double total_lunch;
    private String breakfastapptime,lunchapptime;
    SimpleDateFormat parseFormat;
    private ArrayList<Cut_Off_Time> cuttoftimes = new ArrayList<>();
    DateFormat inputFormat;
    private String notavailablelunch;
    private String notavailablebreakfast;
    private DatabaseReference databasebreakfastreference;
    private DatabaseReference databaselunchreference;
    private ArrayList<FoodItem> lunchitemsList,breakfastitemsList;
    private List<FoodItem> validlunchList,validbreakfastlist;
    private String notavailablebreakfastquantity;
    private String notavailablelunchquantity;
    private TextView lunchtotal,breakfasttotal;
    private  String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String Server_key = "key=AAAAywbXNJo:APA91bETZC8P3pLjfmUN4h3spZu_u9DgTPsjuyqSewis6yGPv-pxzgND_2X-CE5U_x7GgMf5SBtqtQ7gbHTosf6acuG4By2qGtjR66aOTCx5ukw7CEU0_zi2fpV6EvV3wxJheCu_Hf8a";
    private String contentType = "application/json";
    private RequestQueue requestQueue;
    private String available_Balance;
    private ArrayList<String> allusersavailablebalance;
    private String payeravailable_balance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_cart);
        setTitle("Cart");
        breakfastcart = new ArrayList<>();
        lunchcart = new ArrayList<>();
        allusersempid = new ArrayList<>();
        cuttoftimes = new ArrayList<>();
        validbreakfastlist = new ArrayList<>();
        validlunchList = new ArrayList<>();
        lunchitemsList =  new ArrayList<>();
        breakfastitemsList =  new ArrayList<>();
        allusersavailablebalance = new ArrayList<>();
        total_breakfast =0;
        total_lunch=0;

        breakfasttotal = findViewById(R.id.totalcostbreakfast);
        breakfasttotal.setText("Total Cost :$"+total_breakfast);
        lunchtotal = findViewById(R.id.totalcostluunch);
        lunchtotal.setText("Total Cost : $"+total_lunch);
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
        requestQueue= Volley.newRequestQueue(getApplicationContext());
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
        ProgressDialog SetupCartDialog = new ProgressDialog(Cart.this);
        SetupCartDialog.setMessage("Setting up Cart");
        SetupCartDialog.show();

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

        databasebreakfastreference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
        getBreakfastMenu();

        databaselunchreference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
        getLunchMenu();


        //Function to handle the user checking out their breakfast order
        breakfastcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Date timenow = simpleTimeFormat.parse(simpleTimeFormat.format(datenow));
                    Date bapptime = simpleTimeFormat.parse(breakfastapptime);
                    Date startime = null;
                    try {
                        startime = simpleTimeFormat.parse("06:00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Determine if the user tries to access the menu after cut off time
                    if(timenow.after(bapptime)||timenow.before(startime) ){
                        new AlertDialog.Builder(Cart.this,R.style.datepicker)
                                .setTitle("Orders Cut of Time")
                                .setMessage("Sorry,the time for ordering breakfast has passed")
                                .setPositiveButton("Okay",null)
                                .setIcon(R.drawable.adminprofile)
                                .show();

                    }
                    else if (breakfastcart.size()<=0){
                        Toast.makeText(getApplicationContext(),"You have no items in the breakfast cart",Toast.LENGTH_SHORT).show();
                    }
                    else if (!checkbreakfastitemquantities()){
                        Toast.makeText(getApplicationContext(),"The item "+ notavailablebreakfast +" only has "+notavailablebreakfastquantity+" available"
                                ,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        breakfastcheckingout();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        //Function to handle the user checking out his lunch order
        lunchcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Date timenow = simpleTimeFormat.parse(simpleTimeFormat.format(datenow));
                    Date lunchtime = simpleTimeFormat.parse(lunchapptime);
                    Date startime = null;
                    try {
                        startime = simpleTimeFormat.parse("06:00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // Determine if the user tries to access the menu after cut off time
                    if(timenow.after(lunchtime)||timenow.before(startime) ){
                        new AlertDialog.Builder(Cart.this,R.style.datepicker)
                                .setTitle("Orders Cut of Time")
                                .setMessage("Sorry,the time for ordering Lunch has passed")
                                .setPositiveButton("Okay",null)
                                .setIcon(R.drawable.adminprofile)
                                .show();

                    }
                    else if (lunchcart.size()<=0) {
                        Toast.makeText(getApplicationContext(),"You have no items in the lunch cart",Toast.LENGTH_SHORT).show();
                    }
                    else if (!checklunchitemquantity()){
                        Toast.makeText(getApplicationContext(),"The item "+ notavailablelunch +" only has "+notavailablelunchquantity+" available"
                                ,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        lunchcheckingout();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        SetupCartDialog.dismiss();

    }

    //Function to get cut off times from the database
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



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Function to checkout the user's items from the lunch cart
    private void lunchcheckingout() {
        final ArrayList<String> ordertitles = new ArrayList<>();
        Long totalvalue = 0L;
        final ArrayList<String> orderquantities = new ArrayList<>();
        final ArrayList<String> itemtitlesonly = new ArrayList<>();


            //add the order titles with their quantity to a list
            for (int i = 0; i <lunchcart.size(); i++){
                ordertitles.add(new Ordertitle().setItemname(lunchcart.get(i).getOrdertitle() +"(x"+lunchcart.get(i).getQuantity()+"),"+" "));

                //add the titles to a separate list
                itemtitlesonly.add(lunchcart.get(i).getOrdertitle());
                //add the quantities to a separate list in the same order as those in the cart
                orderquantities.add(lunchcart.get(i).getQuantity());

                //Calculate the total cost of cost times the quantity of items
                Long costvalue = Double.valueOf(lunchcart.get(i).getCost()).longValue();
                totalvalue= totalvalue+(((costvalue)*Long.valueOf(lunchcart.get(i).getQuantity())));
            }
            //Open the Dialog to show order details
            checkoutdialog(totalvalue,ordertitles,"Lunch", orderquantities, itemtitlesonly);


        }


    //Function to checkout the user's items from the breakfast cart
    private void breakfastcheckingout() {
        final ArrayList<String> ordertitles = new ArrayList<>();
        Long totalvalue = 0L;
        final ArrayList<String> orderquantities = new ArrayList<>();
        final ArrayList<String> itemtitlesonly = new ArrayList<>();

        //Check If the breakfast cart is empty
            for (int i = 0; i <breakfastcart.size(); i++){
                //add the order titles with their quantity to a list
                ordertitles.add(new Ordertitle().setItemname(breakfastcart.get(i).getOrdertitle() +"(x"+breakfastcart.get(i).getQuantity()+"),"+" "));
                //add the titles to a separate list
                itemtitlesonly.add(breakfastcart.get(i).getOrdertitle());
                //add the quantities to a separate list in the same order as those in the cart
                orderquantities.add(breakfastcart.get(i).getQuantity());
                //Calculate the total cost of cost times the quantity of items
                Long costvalue = Double.valueOf(breakfastcart.get(i).getCost()).longValue();
                totalvalue= totalvalue+((costvalue.longValue())*Long.valueOf(breakfastcart.get(i).getQuantity()));
            }
            //Open the Dialog to show order details
            checkoutdialog(totalvalue,ordertitles,"Breakfast",orderquantities,itemtitlesonly);



    }
     //For each item in the breakfast menu check the items in the cart to see if the
    //requested amount for each item in the breakfast cart is available
    private boolean checkbreakfastitemquantities() {
        boolean doable = true;

        for (int i = 0; i<validbreakfastlist.size(); i++) {
            for (int r = 0; r<breakfastcart.size(); r++){
                if (validbreakfastlist.get(i).getTitle().equals(breakfastcart.get(r).getOrdertitle())){
                    int desiredquantity = Integer.valueOf(breakfastcart.get(r).getQuantity());
                    int actualquantity = Integer.valueOf(validbreakfastlist.get(i).getQuantity());
                    int difference = actualquantity - desiredquantity;
                    if (difference<0){
                        doable= false;
                        notavailablebreakfast = validbreakfastlist.get(i).getTitle();
                        notavailablebreakfastquantity  =validbreakfastlist.get(i).getQuantity();
                        break;
                    }


                }
            }
        }

        return doable;
    }

    //For each item in the breakfast menu check the items in the cart to see if the
    //requested amount for each item in the breakfast cart is available
    private boolean checklunchitemquantity() {
        boolean doable = true;

        for (int i = 0; i<validlunchList.size(); i++) {
            for (int r = 0; r<lunchcart.size(); r++){
                if (validlunchList.get(i).getTitle().equals(lunchcart.get(r).getOrdertitle())){
                    int desiredquantity = Integer.valueOf(lunchcart.get(r).getQuantity());
                    int actualquantity = Integer.valueOf(validlunchList.get(i).getQuantity());
                    int difference = actualquantity - desiredquantity;
                    if (difference<0){
                        doable= false;
                        notavailablelunch = validlunchList.get(i).getTitle();
                        notavailablelunchquantity = validlunchList.get(i).getQuantity();
                        break;

                    }

                }
            }
        }

        return doable;

    }

    //Retrieves the contents of the lunch menu from the database
    private void getLunchMenu() {
        databaselunchreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem lunchdetails = dataSnapshot.getValue(FoodItem.class);

                    lunchitemsList.add(lunchdetails);

                }

                for(FoodItem lunchitem : lunchitemsList){
                    for(com.example.jepapp.Models.Cart cartitem: lunchcart){
                        if (lunchitem.getTitle().equals(cartitem.getOrdertitle())){
                            validlunchList.add(lunchitem);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Retrieves the contents of the breakfast menu from the database
    private void getBreakfastMenu() {
        databasebreakfastreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem breakfastDetails = dataSnapshot.getValue(FoodItem.class);

                    breakfastitemsList.add(breakfastDetails);

                }

                for(FoodItem breakfastitem : breakfastitemsList){
                    for(com.example.jepapp.Models.Cart cartitem: breakfastcart){
                        if (breakfastitem.getTitle().equals(cartitem.getOrdertitle())){
                            validbreakfastlist.add(breakfastitem);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Function to create a dialog for the user to submit his order and checkout his items from the cart
    private void checkoutdialog(final Long totalvalue, final ArrayList<String> ordertitles, String ordertype, final ArrayList<String> orderquantities, final ArrayList<String> itemtitlesonly) {
        final ArrayList<String> Orderquantities = orderquantities;
        final ArrayList<String> Itemtitlesonly = itemtitlesonly;
        final ArrayList<String> Ordertitles = ordertitles;
        final String Ordertype = ordertype;

        final ProgressDialog CheckoutDialog = new ProgressDialog(this);
        CheckoutDialog.setTitle("Checking Out Items!");

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
        //setup adapter containing employee id's to use for prediction of text for user payby
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, allusersempid);
        final AutoCompleteTextView autoCompleteTextView = customLayout.findViewById(R.id.autoCompleter);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setAdapter(adapter);
        final Spinner paymentspinner = customLayout.findViewById(R.id.paymentypespinner);
        ArrayList<String> list_for_spinner = new ArrayList<>();
        if(balance.toLowerCase().equals("new")||balance.toLowerCase().equals("visitor")){
            list_for_spinner.add("Cash");
        }
        else{
            list_for_spinner.add("Lunch Card");
            list_for_spinner.add("Cash");
        }
        ArrayAdapter<String> lunchoptionsadapter = new ArrayAdapter<String>(this,R.layout.myspinneritem,list_for_spinner);
        lunchoptionsadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentspinner.setAdapter(lunchoptionsadapter);

        final EditText specialrequest = customLayout.findViewById(R.id.requestfield);
        final TextView totalcost = customLayout.findViewById(R.id.totalcost);
        totalcost.setText(String.valueOf(totalvalue));
        paybygroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //statment to check if user is paying for meal himself or otherwise
                if (checkedId == R.id.other){
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                    paymentspinner.setVisibility(View.INVISIBLE);
                }
                else{
                    autoCompleteTextView.setVisibility(View.INVISIBLE);
                    paymentspinner.setVisibility(View.VISIBLE);
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
                        String payer;
                        //Check if user can afford the order
                            if (((!selected.equals("Cash") ) && (Long.valueOf(balance) - totalvalue< 0) || (Long.valueOf(available_Balance) - totalvalue < 0 ))) {
                                Toast.makeText(customLayout.getContext(), "Your Lunch Card balance is insufficient for this order", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                if (paybygroup.getCheckedRadioButtonId() == R.id.myself) {

                                    payer = employeeid;
                                    // If statements to clear the corresponding cart
                                    if (Ordertype.equals("Lunch")) {
                                        ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
                                                paymentspinner.getSelectedItem().toString(), String.valueOf(lunchcart.size()), specialrequest.getText().toString(),
                                                "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
                                        if (!selected.equals("Cash")) {
                                            updateavailableBalace(String.valueOf(Integer.valueOf(available_Balance) - totalvalue));
                                        }
                                        runnotification();
                                        //Function to update the corresponding menu to deduct the quantities
                                        for (int i = 0; i < ordertitles.size(); i++) {
                                            UpdateMenu("Lunch", orderquantities.get(i), itemtitlesonly.get(i));
                                        }
                                        total_lunch=0;
                                        lunchtotal.setText("$0");
                                        //Clear the Lunch Cart
                                        databaseReferencelunch.removeValue();
                                        CheckoutDialog.dismiss();
                                        dialog.cancel();
                                       lunchcart.clear();


                                    } else if (Ordertype.equals("Breakfast")) {
                                        ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
                                                paymentspinner.getSelectedItem().toString(), String.valueOf(breakfastcart.size()), specialrequest.getText().toString(),
                                                "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
                                        if (!selected.equals("Cash")) {
                                            updateavailableBalace(String.valueOf(Integer.valueOf(available_Balance) - totalvalue));
                                        }
                                        runnotification();
                                        //Function to update the corresponding menu to deduct the quantities
                                        for (int i = 0; i < ordertitles.size(); i++) {
                                            UpdateMenu("BreakfastMenu", orderquantities.get(i), itemtitlesonly.get(i));
                                        }
                                        total_breakfast =0;
                                        breakfasttotal.setText("$0");
                                        //Clear the Breakfast Cart
                                        databaseReferencebreakfast.removeValue();
                                        CheckoutDialog.dismiss();
                                        dialog.cancel();
                                        breakfastadapter.notifyDataSetChanged();
                                       breakfastcart.clear();


                                    }
                                }
                            }

                            //Check if user has selected other as who will pay
                            if (paybygroup.getCheckedRadioButtonId() == R.id.other) {
                              
                                if (autoCompleteTextView.getText().toString().isEmpty() || idcheck(autoCompleteTextView.getText().toString()) == false) {
                                    Toast.makeText(customLayout.getContext(), "Please enter a valid employee ID", Toast.LENGTH_SHORT).show();
                                }
                                else if (!checkbalance(Long.valueOf(payeravailable_balance),totalvalue)){
                                    Toast.makeText(customLayout.getContext(), "This employee can not facilitate your request at this time", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    payer = autoCompleteTextView.getText().toString();
                                    CheckoutDialog.show();
                                    // If statements to perform ordering on the corresponding cart
                                    if (Ordertype.equals("Lunch")) {
                                        ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
                                                "Lunch Card", String.valueOf(lunchcart.size()), specialrequest.getText().toString(),
                                                "pending", simpleTimeFormat.format(datenow), Ordertype, username);


                                        //Function to update the corresponding menu to deduct the quantities
                                        for (int i = 0; i < ordertitles.size(); i++) {
                                            UpdateMenu("Lunch", orderquantities.get(i), itemtitlesonly.get(i));
                                        }

                                        total_lunch=0;
                                        lunchtotal.setText("$0");
                                        //Clear the Lunch Cart
                                        databaseReferencelunch.removeValue();
                                        String message = username+" would like you to pay for their Lunch Order of amount $"+totalcost.getText().toString();
                                        String subject = username+"'s Lunch Order";
                                        //Send the user who is to pay for an order an email
                                        sendEmail(mAuth.getCurrentUser().getEmail(),message,subject);
                                        CheckoutDialog.dismiss();
                                        dialog.cancel();
                                        lunchadapter.notifyDataSetChanged();
                                        lunchcart.clear();



                                    } else if (Ordertype.equals("Breakfast")) {
                                        ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
                                                "Lunch Card", String.valueOf(breakfastcart.size()), specialrequest.getText().toString(),
                                                "pending", simpleTimeFormat.format(datenow), Ordertype, username);
                                        runnotification();
                                        //Function to update the corresponding menu to deduct the quantities
                                        for (int i = 0; i < ordertitles.size(); i++) {
                                            UpdateMenu("BreakfastMenu", orderquantities.get(i), itemtitlesonly.get(i));
                                        }
                                        total_breakfast=0;
                                        breakfasttotal.setText("$0");
                                        //Clear the Breakfast Cart
                                        databaseReferencebreakfast.removeValue();
                                        String message = username+" would like you to pay for their Lunch Order of amount $"+totalcost.getText().toString();
                                        String subject = username+"'s Lunch Order";
                                        //Send the user who is to pay for an order an email
                                        sendEmail(mAuth.getCurrentUser().getEmail(),message,subject);
                                        CheckoutDialog.dismiss();
                                        dialog.cancel();
                                        breakfastadapter.notifyDataSetChanged();
                                        breakfastcart.clear();



                                    }
                                }
                            }

                    }
        });
    }
    //This function will use only the title of an item within a specific menutype and update the quantity
    // of the corresponding Menu item
    private void UpdateMenu(String mMenuType, final String morderquantities, final String mitemtitlesonly) {
            final DatabaseReference ref = myDBRef.child(mMenuType);
            ref.addListenerForSingleValueEvent(new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        String title=data.child("title").getValue().toString();
                        if(title.equals(mitemtitlesonly)){
                            String keyid=data.getKey();
                            String oldvalue = data.child("quantity").getValue().toString();
                            int newvalue= (Integer.valueOf(oldvalue)) - (Integer.valueOf(morderquantities));
                            ref.child(keyid).child("quantity").setValue(String.valueOf(newvalue));

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }

//Function to retrieve the contents of the current user's lunch cart from the database
    private void getLunchcart() {
        total_lunch =0;
        lunchcart.clear();
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Cart lunchitems = dataSnapshot.getValue(com.example.jepapp.Models.Cart.class);


                    lunchcart.add(lunchitems);
                    total_lunch = (total_lunch+(Double.valueOf(lunchitems.getCost())*Integer.valueOf(lunchitems.getQuantity())));
                }
                lunchtotal.setText("Total Cost : $"+ total_lunch);

                lunchadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Function to retrieve the contents of the current user's breakfast cart from the database
    private void getbreakfastcart() {
        total_breakfast =0;
        breakfastcart.clear();
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                breakfastcart.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Cart breakfastitems = dataSnapshot.getValue(com.example.jepapp.Models.Cart.class);

                    breakfastcart.add(breakfastitems);
                    total_breakfast = (total_breakfast+Double.valueOf(breakfastitems.getCost())*Integer.valueOf(breakfastitems.getQuantity()));
                }
                breakfasttotal.setText("Total Cost : $"+ total_breakfast);
                breakfastadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Function to retrieve all  user's employee id's from the database
    private void getAllUsers() {
        databaseReferenceusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allusersempid.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials useremails = dataSnapshot.getValue(UserCredentials.class);
                    if (!mAuth.getCurrentUser().getEmail().equals(useremails.getEmail())) {
                        allusersempid.add(useremails.getEmpID());
                        allusersavailablebalance.add(useremails.getAvailable_balance());

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //This function will assign the username of  the current user to a variable
        public void DoUsernamequery(){
            Query emailquery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

            emailquery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);
                        username = userCredentials.getUsername();
                        balance = userCredentials.getBalance();
                        employeeid = userCredentials.getEmpID();
                        available_Balance = userCredentials.getAvailable_balance();


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

        }

        //This function checks to see if a user's employee id exists in a employee id list
    private boolean idcheck(String otheruser) {
        boolean returner = false;
        for (int i = 0; i < allusersempid.size(); i++) {
            if (allusersempid.get(i).equals(otheruser)){
                payeravailable_balance = allusersavailablebalance.get(i);
                returner = true;


            break;
        }
        }

        return returner;
    }

private boolean checkbalance(Long Balance,Long cost){
    boolean returner = false;
    if (Balance>cost){
        returner= true;
    }
    return  returner;
}


    //Function to create an order
    private void ItemCreator(Long mcost, String mdate, ArrayList<String> mordertitles, String mpaidby,
                             String mpayment_type, String mquantity, String mrequest, String mstatus, String mtime, String mtype, String musername) {
        Orders orders;
        String key =myDBRef.child(mtype+"Orders").push().getKey();


        orders = new Orders(mcost,mdate,key, mordertitles,mpaidby,mpayment_type,mquantity,mrequest,mstatus,mtime,mtype,musername);
        myDBRef.child(mtype+"Orders")
                .child(key)
                .setValue(orders);


    }
    //Function to reload the interface
    public void Reloadit(){
        finish();
        startActivity(getIntent());
    }
    //Function to update the available balance of the user
    private void updateavailableBalace(String cost){
        DatabaseReference availablebalanceref = myDBRef.child("Users").child(mAuth.getCurrentUser().getEmail().replace(".",""));
        availablebalanceref.child("available_balance").setValue((cost));



    }
    //Function to initiate sending notification to a user
    public void runnotification() {
        String topic = "/topics/Orders";
        JSONObject notification = new JSONObject();
        JSONObject notificationbody = new JSONObject();

        try{
            notificationbody.put("title","Orders Notification");
            notificationbody.put("message",username+" has made a new order");
            notification  .put("to",topic);
            notification.put("data",notificationbody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendNotification(notification);
    }


  //Function to send notifications to appropriate users'
    private final void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(this.FCM_API, notification,(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        })
                ,(new Response.ErrorListener() {
            public final void onErrorResponse(VolleyError it) {
                Toast.makeText(getApplicationContext(),"Did not work",Toast.LENGTH_LONG).show();
            }
        })) {
            @NotNull
            public Map<String,String> getHeaders() {
                HashMap params = new HashMap<String,String>();
                params.put("Authorization", Cart.this.Server_key);
                params.put("Content-Type", Cart.this.contentType);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }
    private void sendEmail(String email, String message, String subject) {

        //Creating SendMail object
        GMailSender sm = new GMailSender(Cart.this, email, message, subject);

        //Executing sendmail to send email
        sm.execute();

    }

}
