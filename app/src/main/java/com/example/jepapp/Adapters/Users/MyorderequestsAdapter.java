package com.example.jepapp.Adapters.Users;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Users.CustomerViewPager;
import com.example.jepapp.Models.Cut_Off_Time;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyorderequestsAdapter extends RecyclerView.Adapter<MyorderequestsAdapter.MyorderequestAdapterViewHolder> {
    private Context mcontext;

    private List<Orders> RequestsList;
    private DatabaseReference mydbreference,referencecutofftime;
    private FirebaseAuth mAuth;
    private UserCredentials TheUser;
    private ArrayList<Cut_Off_Time> cuttoftimes = new ArrayList<>();
    SimpleDateFormat simpleTimeFormat;
    SimpleDateFormat parseFormat;
    private String breakfastapptime;
    private String lunchapptime;

    public MyorderequestsAdapter(Context mCtx, List<Orders> requestsList) {
        this.mcontext = mCtx;
        this.RequestsList = requestsList;
    }

    @Override
    public MyorderequestAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout for the items that will populate or list
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.myorderequestslayout,null);
        mydbreference = FirebaseDatabase.getInstance().getReference("JEP");
        referencecutofftime = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");
        simpleTimeFormat = new SimpleDateFormat("HH:mm");
        parseFormat = new SimpleDateFormat("hh:mm a");

        mAuth = FirebaseAuth.getInstance();
        GetUser();
        Cutofftimesgetter();
        return new MyorderequestAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyorderequestAdapterViewHolder holder, final int position) {
        //Gets the specific object based on location of an item on the recycler view
        final Orders item  = RequestsList.get(position);
        holder.username.setText(item.getUsername());
        holder.orderamount.setText(String.valueOf(item.getCost()));
        //Approve the order to be paid for by the current user on behalf of someone else
        holder.approve.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Long payeeBalance = (Float.valueOf(TheUser.getAvailable_balance())).longValue();

                if (!(payeeBalance > item.getCost())) {
                    Toast.makeText(mcontext.getApplicationContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();
                }else if(CheckTime(lunchapptime)){
                    Toast.makeText(mcontext.getApplicationContext(), "Lunch Time has Passed. Please Deny Order", Toast.LENGTH_SHORT).show();
                }
                else if(CheckTime(breakfastapptime)){
                    Toast.makeText(mcontext.getApplicationContext(), "Breakfast Time has Passed. Please Deny Order", Toast.LENGTH_SHORT).show();
                }
                else{
                    mydbreference.child(item.getType() + "Orders")
                            .child(item.getOrderID())
                            .child("status")
                            .setValue("Incomplete");
                    String newbalance = String.valueOf((payeeBalance - item.getCost()));
                    String emailfield = mAuth.getCurrentUser().getEmail().toString().replace(".", "");
                    mydbreference.child("Users").child(emailfield).child("available_balance").setValue(newbalance);
                    RequestsList.remove(position);
                    Intent inside = new Intent(mcontext, CustomerViewPager.class);
                mcontext.startActivity(inside);
                ((Activity)mcontext).finish();
                }
            }
        });
        //Deny the order to be paid for by the current user on behalf of someone else
        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydbreference.child(item.getType()+"Orders")
                        .child(item.getOrderID())
                        .child("status")
                        .setValue("denied");
                for (String s : item.getOrdertitle()) {
                    String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                    String noparantheses = s.split("[\\](},]")[0];
                    if (item.getType().toLowerCase().equals("Lunch")) {
                        UpdateMenuAdd("Lunch", number, noparantheses);
                    } else {
                        UpdateMenuAdd("BreakfastMenu", number, noparantheses);
                    }
                    RequestsList.remove(position);
                }
                Intent inside = new Intent(mcontext, CustomerViewPager.class);
                mcontext.startActivity(inside);
                ((Activity)mcontext).finish();

            }
        });


    }



    @Override
    public int getItemCount() {
        return RequestsList.size();
    }

    class MyorderequestAdapterViewHolder  extends RecyclerView.ViewHolder{
        TextView username,orderamount;
        Button approve,deny;
        public MyorderequestAdapterViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.payeeid);
            orderamount = itemView.findViewById(R.id.payeeamount);
            approve = itemView.findViewById(R.id.approverequest);
            deny = itemView.findViewById(R.id.denyrequest);

        }
    }
    //Function to get the details of the user who will pay for the order
    public void GetUser(){

        Query emailquery = mydbreference.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());


        emailquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);
                    TheUser = (userCredentials);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    //This function will use the title of an item within a specific menuType and update the quantity
    // of the  corresponding Menu item as well as update the available balance for a user
    private void UpdateMenuAdd(String mMenuType, final String morderquantities, final String mitemtitlesonly) {
        final DatabaseReference ref = mydbreference.child(mMenuType);
        ref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String title=data.child("title").getValue().toString();
                    if(title.equals(mitemtitlesonly)){
                        String keyid=data.getKey();
                        String oldvalue = data.child("quantity").getValue().toString();
                        int newvalue= (Integer.valueOf(oldvalue)) + (Integer.valueOf(morderquantities));
                        ref.child(keyid).child("quantity").setValue(String.valueOf(newvalue));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private boolean CheckTime(String apptime) {
        Date datenow = new Date();
        Date timenow = null;

        try {
            timenow = simpleTimeFormat.parse(simpleTimeFormat.format(datenow));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date cutofftime = null;
        try {
            cutofftime = simpleTimeFormat.parse(apptime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date startime = null;
        try {
            startime = simpleTimeFormat.parse("06:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Determine if the user tries to access the lunch menuitems after cut off time
        // and when an order has not yet been pprocessed
        if (timenow.after(cutofftime) || timenow.before(startime)){
            return true;
        }
        else{
            return false;
        }
    }
    public void Cutofftimesgetter() {
        final ProgressDialog progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("Getting Cut Off Times");
        progressDialog.show();
        referencecutofftime.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cut_Off_Time cuttofftime = dataSnapshot.getValue(Cut_Off_Time.class);
                    cuttoftimes.add(cuttofftime);

                }
                //Assign the breakfast and lunch cut off times respectively straight from the database
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
                progressDialog.cancel();
                progressDialog.dismiss();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
