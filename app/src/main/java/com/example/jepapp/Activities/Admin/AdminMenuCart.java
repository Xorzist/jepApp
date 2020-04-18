package com.example.jepapp.Activities.Admin;

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
import android.widget.ProgressBar;
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
import com.example.jepapp.Adapters.Admin.adminCartAdapter;
import com.example.jepapp.Adapters.Users.cartAdapter;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.Models.Cart;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminMenuCart extends AppCompatActivity {
    adminCartAdapter breakfastadapter, lunchadapter;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menucart);
        getSupportActionBar().setTitle("Select Items");
        breakfastcart = new ArrayList<>();
        lunchcart = new ArrayList<com.example.jepapp.Models.Cart>();
        allusersempid = new ArrayList<>();
        cuttoftimes = new ArrayList<>();
        validbreakfastlist = new ArrayList<>();
        validlunchList = new ArrayList<>();
        lunchitemsList =  new ArrayList<>();
        breakfastitemsList =  new ArrayList<>();
        total_breakfast =0;
        total_lunch=0;

        breakfasttotal = findViewById(R.id.totalcostbreakfast);
        //breakfasttotal.setVisibility(View.GONE);
        lunchtotal = findViewById(R.id.totalcostluunch);
       // lunchtotal.setVisibility(View.GONE);
        breakfastcheckout = findViewById(R.id.breakfastcheckout);
        breakfastcheckout.setText("Done");
        lunchcheckout = findViewById(R.id.lunchcheckout);
        lunchcheckout.setText("Done");
        breakfastrecycler = findViewById(R.id.cartbreakfastlist);
        lunchrecycler = findViewById(R.id.cartlunchlist);
        mAuth = FirebaseAuth.getInstance();
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        dividerItemDecoration = new DividerItemDecoration(breakfastrecycler.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration2 = new DividerItemDecoration(lunchrecycler.getContext(), linearLayoutManager.getOrientation());
        breakfastadapter = new adminCartAdapter(AdminMenuCart.this,breakfastcart);
        lunchadapter = new adminCartAdapter(AdminMenuCart.this,lunchcart);

        breakfastrecycler.setLayoutManager(linearLayoutManager2);
        lunchrecycler.setLayoutManager(linearLayoutManager);
        breakfastrecycler.addItemDecoration(dividerItemDecoration);
        lunchrecycler.addItemDecoration(dividerItemDecoration2);
        breakfastrecycler.setAdapter(breakfastadapter);
        lunchrecycler.setAdapter(lunchadapter);
        ProgressDialog progressDialog = new ProgressDialog(AdminMenuCart.this);
        progressDialog.setMessage("Setting up Cart");
        progressDialog.show();

        //dbreference for breakfast table
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastCart").child("Admin Menu");
        //Method to get all items in the breakfast cart
        getbreakfastcart();
        //dbreference for lunch table
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchCart").child("Admin Menu");
        //Method to get all items in the lunch cart
        getLunchcart();
        //dbreference for users table
        databaseReferenceusers = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        //Method to get all users in the Users table




        breakfastcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function to handle the breakfast checkout button

                    if (breakfastcart.size()<=0){
                        Toast.makeText(getApplicationContext(),"You have no items in the breakfast cart",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        breakfastcheckingout();
                    }
            }
        });

        lunchcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function to handle the lunch checkout button
               if (lunchcart.size()<=0) {
                        Toast.makeText(getApplicationContext(),"You have no items in the lunch cart",Toast.LENGTH_SHORT).show();
                    }
               else{
                   lunchcheckingout();
            }
            }
        });

        progressDialog.dismiss();

    }

//    private void Cutofftimesgetter() {
//        referencecutofftime.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    Cut_Off_Time cuttofftime = dataSnapshot.getValue(Cut_Off_Time.class);
//                    cuttoftimes.add(cuttofftime);
//
//                }
//                //Assign the breakfast and lunch times respectively straight from the database
//                String dbbreakfasttime = cuttoftimes.get(0).getTime();
//                String dblunchtime = cuttoftimes.get(1).getTime();
//
//                try {
//                    breakfastapptime = simpleTimeFormat.format(parseFormat.parse(dbbreakfasttime));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    lunchapptime = simpleTimeFormat.format(parseFormat.parse(dblunchtime));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Log.e("formatted breakfast!!", (breakfastapptime));
//                Log.e("formatted breakfast!!", (lunchapptime));
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }




    private void lunchcheckingout() {
       DatabaseReference dbref = myDBRef.child("Lunch");
            dbref.removeValue();
            for (int i = 0; i < lunchcart.size(); i++) {
                String title = lunchcart.get(i).getOrdertitle();
                String quantity = lunchcart.get(i).getQuantity();
                String ingredients = lunchcart.get(i).getIngredients();
                String id = lunchcart.get(i).getID();
                Float price = Float.valueOf(lunchcart.get(i).getCost());
                String image = lunchcart.get(i).getImage();
                String type = "Lunch";
                String key = myDBRef.child("Lunch").push().getKey();
                Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image, key, type);
                myDBRef.child("Lunch")
                        .child(key)
                        .setValue(mItems);
                Log.d("Start Adding", "START!");

            }
        DatabaseReference ref = myDBRef.child("LunchCart").child("Admin Menu");
        ref.removeValue();
        finish();
    }







    private void breakfastcheckingout() {
            DatabaseReference dbref = myDBRef.child("BreakfastMenu");
            dbref.removeValue();
            for (int i = 0; i < breakfastcart.size(); i++) {
                String title = breakfastcart.get(i).getOrdertitle();
                String quantity = breakfastcart.get(i).getQuantity();
                String ingredients = breakfastcart.get(i).getIngredients();
                String id = breakfastcart.get(i).getID();
                Float price = Float.valueOf(breakfastcart.get(i).getCost());
                String image = breakfastcart.get(i).getImage();
                String type = "Breakfast";
                String key = myDBRef.child("BreakfastMenu").push().getKey();
                Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image, key, type);
                myDBRef.child("BreakfastMenu")
                        .child(key)
                        .setValue(mItems);
                Log.d("Start Adding", "START!");

            }

        DatabaseReference ref = myDBRef.child("BreakfastCart").child("Admin Menu");
        ref.removeValue();


        finish();
        }





//    private boolean checkbreakfastitemquantities() {
//        boolean doable = true;
//        //For each item in the breakfast menu check the items in the cart to see if the
//        //requested amount for each item in the breakfast cart is available
//        for (int i = 0; i<validbreakfastlist.size(); i++) {
//            for (int r = 0; r<breakfastcart.size(); r++){
//                if (validbreakfastlist.get(i).getTitle().equals(breakfastcart.get(r).getOrdertitle())){
//                    int desiredquantity = Integer.valueOf(breakfastcart.get(r).getQuantity());
//                    int actualquantity = Integer.valueOf(validbreakfastlist.get(i).getQuantity());
//                    int difference = actualquantity - desiredquantity;
//                    if (difference<0){
//                        Log.e( "doablefalse:", String.valueOf(difference));
//                        doable= false;
//                        notavailablebreakfast = validbreakfastlist.get(i).getTitle();
//                        notavailablebreakfastquantity  =validbreakfastlist.get(i).getQuantity();
//                        break;
//                    }
//
//
//                }
//            }
//        }
//
//        return doable;
//    }
//    private boolean checklunchitemquantity() {
//        boolean doable = true;
//        //For each item in the breakfast menu check the items in the cart to see if the
//        //requested amount for each item in the breakfast cart is available
//        for (int i = 0; i<validlunchList.size(); i++) {
//            for (int r = 0; r<lunchcart.size(); r++){
//                if (validlunchList.get(i).getTitle().equals(lunchcart.get(r).getOrdertitle())){
//                    int desiredquantity = Integer.valueOf(lunchcart.get(r).getQuantity());
//                    int actualquantity = Integer.valueOf(validlunchList.get(i).getQuantity());
//                    int difference = actualquantity - desiredquantity;
//                    if (difference<0){
//                        doable= false;
//                        Log.e( "doablefalse:", String.valueOf(difference));
//                        Log.e( "checklunchitemquantity:","this was done" );
//                        notavailablelunch = validlunchList.get(i).getTitle();
//                        notavailablelunchquantity = validlunchList.get(i).getQuantity();
//                        break;
//
//                    }
//
//                }
//            }
//        }
//
//        return doable;
//
//    }
//
//    private void getLunchMenu() {
//        databaselunchreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    FoodItem lunchdetails = dataSnapshot.getValue(FoodItem.class);
//
//                    lunchitemsList.add(lunchdetails);
//
//                }
//
//                for(FoodItem lunchitem : lunchitemsList){
//                    for(Cart cartitem: lunchcart){
//                        if (lunchitem.getTitle().equals(cartitem.getOrdertitle())){
//                            validlunchList.add(lunchitem);
//                        }
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//
//
//            }
//        });
//
//    }
//
//    private void getBreakfastMenu() {
//        databasebreakfastreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    FoodItem breakfastDetails = dataSnapshot.getValue(FoodItem.class);
//
//                    breakfastitemsList.add(breakfastDetails);
//
//                }
//
//                for(FoodItem breakfastitem : breakfastitemsList){
//                    for(Cart cartitem: breakfastcart){
//                        if (breakfastitem.getTitle().equals(cartitem.getOrdertitle())){
//                            validbreakfastlist.add(breakfastitem);
//                        }
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//
//
//            }
//        });
//    }

//    private void checkoutdialog(final Long totalvalue, final ArrayList<String> ordertitles, String ordertype, final ArrayList<String> orderquantities, final ArrayList<String> itemtitlesonly) {
//        final ArrayList<String> Orderquantities = orderquantities;
//        final ArrayList<String> Itemtitlesonly = itemtitlesonly;
//        final ArrayList<String> Ordertitles = ordertitles;
//        final String Ordertype = ordertype;
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Checking Out Items!");
//
//        //Create Alert Builder
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(Ordertype+" Checkout");
//        //Add Custom Layout
//        final View customLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.checkoutpage, null);
//        builder.setView(customLayout);
//        RadioButton myself = customLayout.findViewById(R.id.myself);
//        final RadioButton other = customLayout.findViewById(R.id.other);
//        final RadioGroup paybygroup = customLayout.findViewById(R.id.paybygroup);
//        paybygroup.check(R.id.myself);
//        //setup adapter containing employee id's to use for prediction of text for user payby
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this,android.R.layout.select_dialog_item, allusersempid);
//        final AutoCompleteTextView autoCompleteTextView = customLayout.findViewById(R.id.autoCompleter);
//        autoCompleteTextView.setThreshold(0);
//        autoCompleteTextView.setAdapter(adapter);
//        final Spinner paymentspinner = customLayout.findViewById(R.id.paymentypespinner);
//        final EditText specialrequest = customLayout.findViewById(R.id.requestfield);
//        final TextView totalcost = customLayout.findViewById(R.id.totalcost);
//        totalcost.setText(String.valueOf(totalvalue));
//        paybygroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                //statment to check if user is paying for meal himself or otherwise
//                if (checkedId == R.id.other){
//                    autoCompleteTextView.setVisibility(View.VISIBLE);
//                }
//                else{
//                    autoCompleteTextView.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        //Setup button to terminate the dialog
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String selected = paymentspinner.getSelectedItem().toString();
//                String payer = new String("empty");
//                //Check if user can afford the order
//                if  ((Float.valueOf(balance)-totalvalue)<0 && !selected.equals("Cash")||Float.valueOf(available_Balance)-totalvalue<0 && !selected.equals("Cash")){
//                    Toast.makeText(customLayout.getContext(), "Your balance is insufficient for this order", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    if (paybygroup.getCheckedRadioButtonId() == R.id.myself) {
//                        payer = employeeid;
//
//                        // If statements to clear the corresponding cart
//                        if (Ordertype.equals("Lunch")) {
//                            ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
//                                    paymentspinner.getSelectedItem().toString(), String.valueOf(lunchcart.size()), specialrequest.getText().toString(),
//                                    "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
//                            if(!selected.equals("Cash")){
//                                updateavailableBalace(String.valueOf(Float.valueOf(available_Balance)-totalvalue));
//                            }
//                            runnotification();
//                            //Function to update the corresponding menu to deduct the quantities
//                            for (int i = 0; i<ordertitles.size();i++){
//                                UpdateMenu("Lunch", orderquantities.get(i), itemtitlesonly.get(i));
//                            }
//
//                            databaseReferencelunch.removeValue();
//                            progressDialog.dismiss();
//                            dialog.cancel();
//                            Reloadit();
//                            //onBackPressed();
//                            Log.e("grgrg", balance );
//                        } else if (Ordertype.equals("Breakfast")) {
//                            ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
//                                    paymentspinner.getSelectedItem().toString(), String.valueOf(breakfastcart.size()), specialrequest.getText().toString(),
//                                    "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
//                            if(!selected.equals("Cash")){
//                                updateavailableBalace(String.valueOf(Float.valueOf(available_Balance)-totalvalue));
//                            }
//                            runnotification();
//                            //Function to update the corresponding menu to deduct the quantities
//                            for (int i = 0; i<ordertitles.size();i++){
//                                UpdateMenu("BreakfastMenu", orderquantities.get(i), itemtitlesonly.get(i));
//                            }
//                            databaseReferencebreakfast.removeValue();
//                            progressDialog.dismiss();
//                            dialog.cancel();
//                            breakfastadapter.notifyDataSetChanged();
//                            Reloadit();
//
//
//                        }
//                    }
//
//                    //Check if user has selected other as who will pay
//                    else if (paybygroup.getCheckedRadioButtonId() == R.id.other) {
//                        if (autoCompleteTextView.getText().toString().isEmpty() || idcheck(autoCompleteTextView.getText().toString())==false) {
//                            Toast.makeText(customLayout.getContext(), "Please enter a valid employee ID", Toast.LENGTH_SHORT).show();
//                        } else {
//                            payer = autoCompleteTextView.getText().toString();
//                            progressDialog.show();
//                            // If statements to perform ordering on the corresponding cart
//                            if (Ordertype.equals("Lunch")) {
//                                ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
//                                        paymentspinner.getSelectedItem().toString(), String.valueOf(lunchcart.size()), specialrequest.getText().toString(),
//                                        "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
//                                if(!selected.equals("Cash")){
//                                    updateavailableBalace(String.valueOf(Float.valueOf(available_Balance)-totalvalue));
//                                }
//                                runnotification();
//                                //Function to update the corresponding menu to deduct the quantities
//                                for (int i = 0; i<ordertitles.size();i++){
//                                    UpdateMenu("Lunch", orderquantities.get(i), itemtitlesonly.get(i));
//                                }
//                                databaseReferencelunch.removeValue();
//                                progressDialog.dismiss();
//                                dialog.cancel();
//                                Reloadit();
//
//                            } else if (Ordertype.equals("Breakfast")) {
//                                ItemCreator(Long.valueOf(totalcost.getText().toString()), SimpleDateFormat.format(datenow), Ordertitles, payer,
//                                        paymentspinner.getSelectedItem().toString(), String.valueOf(breakfastcart.size()), specialrequest.getText().toString(),
//                                        "Incomplete", simpleTimeFormat.format(datenow), Ordertype, username);
//                                if(!selected.equals("Cash")){
//                                    updateavailableBalace(String.valueOf(Float.valueOf(available_Balance)-totalvalue));
//                                }
//
//                                runnotification();
//                                //Function to update the corresponding menu to deduct the quantities
//                                for (int i = 0; i<ordertitles.size();i++){
//                                    UpdateMenu("BreakfastMenu", orderquantities.get(i), itemtitlesonly.get(i));
//                                }
//
//
//                                databaseReferencebreakfast.removeValue();
//                                progressDialog.dismiss();
//                                dialog.cancel();
//                                breakfastadapter.notifyDataSetChanged();
//                                Reloadit();
//
//                            }
//                        }
//                    }
//
//                }
//            }
//        });
//    }
//
//    private void UpdateMenu(String mMenuType, final String morderquantities, final String mitemtitlesonly) {
//        //This function will use only the title of an item within a specific menutype and update the quantity
//        // of the  corresponding Menu item
//        final DatabaseReference ref = myDBRef.child(mMenuType);
//        ref.addListenerForSingleValueEvent(new ValueEventListener(){
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot){
//                for(DataSnapshot data: dataSnapshot.getChildren()){
//                    String title=data.child("title").getValue().toString();
//                    if(title.equals(mitemtitlesonly)){
//                        String keyid=data.getKey();
//                        String oldvalue = data.child("quantity").getValue().toString();
//                        int newvalue= (Integer.valueOf(oldvalue)) - (Integer.valueOf(morderquantities));
//                        ref.child(keyid).child("quantity").setValue(String.valueOf(newvalue));
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
//


    private void getLunchcart() {
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cart lunchitems = dataSnapshot.getValue(Cart.class);


                    lunchcart.add(lunchitems);
                }


                lunchadapter.notifyDataSetChanged();
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

                    Cart breakfastitems = dataSnapshot.getValue(Cart.class);

                    breakfastcart.add(breakfastitems);

                }

                breakfastadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//    private void getAllUsers() {
//        databaseReferenceusers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                allusersempid.clear();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    UserCredentials useremails = dataSnapshot.getValue(UserCredentials.class);
//
//                    allusersempid.add(useremails.getEmpID());
//                }
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//    public void DoUsernamequery(){
//        //This function will assign the username of  the current user to a variable
//        Query emailquery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
//
//        emailquery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);
//                    //Log.e("onDataChange: ", allmyorders.getTitle().toString());
//
//                    //Set the username and balance of the current user
//                    username = userCredentials.getUsername();
//                    balance = userCredentials.getBalance();
//                    employeeid = userCredentials.getEmpID();
//                    available_Balance = userCredentials.getAvailable_balance();
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//
//            }
//        });
//
//    }
//    private boolean idcheck(String otheruser) {
//        boolean returner = false;
//        for (int i = 0; i < allusersempid.size(); i++) {
//            if (allusersempid.get(i).equals(otheruser)){
//                returner = true;
//                break;
//            }
//        }
//
//        return returner;
//    }



//    private void ItemCreator(Long mcost, String mdate, ArrayList<String> mordertitles, String mpaidby,
//                             String mpayment_type, String mquantity, String mrequest, String mstatus, String mtime, String mtype, String musername) {
//        Orders orders;
//        String key =myDBRef.child(mtype+"Orders").push().getKey();
//
//
//        orders = new Orders(mcost,mdate,key, mordertitles,mpaidby,mpayment_type,mquantity,mrequest,mstatus,mtime,mtype,musername);
//        myDBRef.child(mtype+"Orders")
//                .child(key)
//                .setValue(orders);
//
//        Log.d("Start Adding","START!");
//    }
    public void Reloadit(){
        finish();
        startActivity(getIntent());
    }
//    private void updateavailableBalace(String cost){
//        //Function to update the available balance of the user
//        DatabaseReference availablebalanceref = myDBRef.child("Users").child(mAuth.getCurrentUser().getEmail().replace(".",""));
//        availablebalanceref.child("available_balance").setValue((cost));
//
//
//
//    }

//    private void runnotification() {
//        String topic = "/topics/Orders";
//        JSONObject notification = new JSONObject();
//        JSONObject notificationbody = new JSONObject();
//
//        try{
//            notificationbody.put("title","Orders Notification");
//            notificationbody.put("message",username+" has made a new order");
//            notification  .put("to",topic);
//            notification.put("data",notificationbody);
//            Log.e("runnotification: ","Succeeded");
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("runnotification: ","Failed");
//        }
//        sendNotification(notification);
//    }



//    private final void sendNotification(JSONObject notification) {
//        Log.e("TAG", "sendNotification");
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(this.FCM_API, notification,(new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("Response1", response.toString());
//
//            }
//        })
//                ,(new Response.ErrorListener() {
//            public final void onErrorResponse(VolleyError it) {
//                Toast.makeText(getApplicationContext(),"Did not work",Toast.LENGTH_LONG).show();
//                Log.i("ErrorResponse", "onErrorResponse: Didn't work");
//            }
//        })) {
//            @NotNull
//            public Map<String,String> getHeaders() {
//                HashMap params = new HashMap<String,String>();
//                params.put("Authorization", Cart.this.Server_key);
//                params.put("Content-Type", Cart.this.contentType);
//                return params;
//            }
//        };
//        requestQueue.add(jsonObjectRequest);
//    }

}
