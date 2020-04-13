package com.example.jepapp.Activities.Users;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class pie_weekly_expenditure extends AppCompatActivity {

    private String start,end;

    private ArrayList<String> cash, card;
    private Query databaseReferencebreakfast;
    private Query databaseReferencelunch;
    List<DataEntry> entries;
    AnyChartView anyChartView;
    Cartesian cartesian;
    private String username;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private int cashamount,card_amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_activity_weekly_expenditure);
        start = getIntent().getExtras().getString("startdate");
        end = getIntent().getExtras().getString("enddate");
        cashamount=0;
        card_amount =0;
        cash = new ArrayList<>();
        card = new ArrayList<>();
        cash = new ArrayList<>();
        entries = new ArrayList<>();
        cartesian = AnyChart.column();
        anyChartView = findViewById(R.id.piechartview);
        Log.e("Oncreatestart",start );
        Log.e("Oncreateend",end );
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();
        DoUsernamequery();
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders").orderByChild("date").startAt(start).endAt(end);
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders").orderByChild("date").startAt(start).endAt(end);
        Dbcall();

    }

    private void Dbcall() {
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    if (breakfastitems.getUsername().equals(username))
                    {
                        if(breakfastitems.getPayment_type().toLowerCase().equals("cash")){
                            cashamount+=Integer.parseInt(String.valueOf(breakfastitems.getCost()));
                        }else{
                            card_amount+=Integer.parseInt(String.valueOf(breakfastitems.getCost()));
                        }
                    }
                }

                Log.e("Cash amount", String.valueOf(cashamount));
                Log.e("Card Amount", String.valueOf(card_amount));
                Lunchcall();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void Lunchcall(){
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders lunchitems = snapshot.getValue(Orders.class);
                    if (lunchitems.getUsername().equals(username)){
                        if(lunchitems.getPayment_type().toLowerCase().equals("cash")){
                            cashamount+=Integer.parseInt(String.valueOf(lunchitems.getCost()));
                        }else{
                            card_amount+=Integer.parseInt(String.valueOf(lunchitems.getCost()));
                        }
                    }
                }

                Log.e("Cash amount", String.valueOf(cashamount));
                Log.e("Card Amount", String.valueOf(card_amount));
                AssignData();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void AssignData(){
        //This function assigns values to variables to produce a piechart
        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Cash", cashamount));
        data.add(new ValueDataEntry("Lunch Card", card_amount));


        pie.data(data);

        pie.title("Cash VS Card Report for : "+start+" - "+end);

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Payment Methods")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);




    }

    private int calculatevalues(String date) {
        int total=0;
        for (int i = 0; i < cash.size() ; i++) {
            if( date.equals(cash.get(i))) {
                total+=Integer.parseInt(cash.get(i+1));
            }

        }
        Log.e("Total for"+date,String.valueOf(total) );
        return total;
    }
    public void DoUsernamequery(){
        final ProgressDialog progressDialog = new ProgressDialog(pie_weekly_expenditure.this);
        progressDialog.setMessage("Obtaining the username");
        progressDialog.show();
        Query emailquery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

        emailquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);


                    //Set the username and balance of the current user
                    username = userCredentials.getUsername();
                    Log.e("The name",username );
                    //balance = userCredentials.getBalance();


                }
                progressDialog.cancel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }
}
