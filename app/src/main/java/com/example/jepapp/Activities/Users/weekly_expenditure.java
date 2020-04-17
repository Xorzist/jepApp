package com.example.jepapp.Activities.Users;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
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

public class weekly_expenditure extends AppCompatActivity {

    private String start,end;

    private ArrayList<String> dateandcash,onlydates;
    private ArrayList<Integer>cash;
    private Query databaseReferencebreakfast;
    private Query databaseReferencelunch;
    List<DataEntry> entries;
    AnyChartView anyChartView;
    Cartesian cartesian;
    private String username;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    TextView breakfastvalue,lunchvalue,nodata;
    Integer breakfastotal,lunchtotal;
    Button createpdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_expenditure);
        start = getIntent().getExtras().getString("startdate");
        end = getIntent().getExtras().getString("enddate");
        breakfastvalue = findViewById(R.id.customer_reportbreakfastvalue);
        lunchvalue = findViewById(R.id.customer_reportlunchvalue);
        createpdf = findViewById(R.id.create_PDF);
        breakfastotal = 0;
        lunchtotal = 0;

       nodata = findViewById(R.id.nodatacustomer);
        dateandcash = new ArrayList<>();
        onlydates = new ArrayList<>();
        cash = new ArrayList<>();
        entries = new ArrayList<>();
        cartesian = AnyChart.column();
        anyChartView = findViewById(R.id.customer_graph);
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
                        dateandcash.add(breakfastitems.getDate());
                        dateandcash.add(String.valueOf(breakfastitems.getCost()));
                        onlydates.add(breakfastitems.getDate());
                        breakfastotal+=Integer.valueOf(String.valueOf(breakfastitems.getCost()));
                    }
                    }

                //Log.e(dateandcash.get(0), dateandcash.get(1));
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
                        dateandcash.add(lunchitems.getDate());
                    dateandcash.add(String.valueOf(lunchitems.getCost()));
                    onlydates.add(lunchitems.getDate());
                        lunchtotal+=Integer.valueOf(String.valueOf(lunchitems.getCost()));
                }


                }

                //Log.e(dateandcash.get(0), dateandcash.get(1));
                AssignData();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void AssignData(){
        //This function assigns values to variables to produce a graph
        Log.e("AssignData1: ","Called" );

        Set<String> uniquedates = new HashSet<>(onlydates);

        for (String key : uniquedates) {

            cash.add(calculatevalues(key));
        }

        List<String> datesList = new ArrayList<>(uniquedates);

        for (int i = 0; i <cash.size() ; i++) {
            entries.add(new ValueDataEntry(datesList.get(i),cash.get(i)));
        }






            //entries.add(new ValueDataEntry(key,Collections.frequency(allordertiitles,key)));


        Column column = cartesian.column(entries);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("Spent:${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Report for the period of :  "+start+" --> "+end);

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Dates");
        cartesian.yAxis(0).title("Cash Value");

        anyChartView.setChart(cartesian);
        if (entries.size()==0){
            nodata.setVisibility(View.VISIBLE);
        }
        breakfastvalue.setText("$"+ breakfastotal);
        lunchvalue.setText("$"+ lunchtotal);



    }

    private int calculatevalues(String date) {
        int total=0;
        for (int i = 0; i <dateandcash.size() ; i++) {
            if( date.equals(dateandcash.get(i))) {
                total+=Integer.parseInt(dateandcash.get(i+1));
            }

        }
        Log.e("Total for"+date,String.valueOf(total) );
        return total;
    }
    public void DoUsernamequery(){
        final ProgressDialog progressDialog = new ProgressDialog(weekly_expenditure.this);
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
