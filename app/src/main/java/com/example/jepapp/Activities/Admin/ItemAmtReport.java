package com.example.jepapp.Activities.Admin;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.example.jepapp.Activities.Users.pie_weekly_expenditure;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.example.jepapp.RequestPermissionHandler;

import com.github.mikephil.charting.components.Description;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemAmtReport extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    List<com.example.jepapp.Models.Orders> allorderslist;



    ArrayList<String>allordertiitles;
    private ProgressDialog progressDialog;
    private Description g;
    private DatabaseReference databaseReferencebreakfast;
    private DatabaseReference databaseReferencelunch;
    List<DataEntry> entries,entries2;
    AnyChartView anyChartView;
    Cartesian cartesian;
    Spinner monthSpinner;
    private String month;
    private String firstchar;
    private String intentmonth;
    private String[] monthlist;
    private boolean userIsInteracting;
    private RequestPermissionHandler mRequestPermissionHandler;
    private ScrollView mscrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_amt_report);
        monthSpinner = findViewById(R.id.monthselect);
        monthlist = new String[]{"January","February","March","April","May",
        "June","July","August","September","October","November","December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthlist);
        monthSpinner.setAdapter(adapter);
        mRequestPermissionHandler = new RequestPermissionHandler();
        mscrollView = findViewById(R.id.itemamtscrollview);
        allorderslist = new ArrayList<>();
        allordertiitles = new ArrayList<>();
        entries = new ArrayList<>();
        progressDialog = new ProgressDialog(getApplicationContext());
          anyChartView =  findViewById(R.id.newpie);
         cartesian = AnyChart.column();
        month = getIntent().getExtras().getString("thismonth");
        //Set spinner as current month
        monthSpinner.setSelection(Integer.parseInt(month)-1);

        mAuth = FirebaseAuth.getInstance();
        //Function to calculate the calendar month based on the item selected in the spinner
        monthcalculator();
        //dbreference for breakdast orders
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
        DoBreakfastOrdersQuery(month);
        //dbreference for lunch orders
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
        DoLunchOrdersQuery(month);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSpinner.setSelection(position);
                if(userIsInteracting) {
                    //check if the user has interacted with the spinner
                    Log.e(" Itemselectcalled ", month);
                    Intent i = new Intent(ItemAmtReport.this, ItemAmtReport.class);
                    i.putExtra("thismonth", String.valueOf(monthSpinner.getSelectedItemPosition() + 1));
                    finish();
                    startActivity(i);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ///Do nothing
            }
        });


    }



    private void monthcalculator() {
        //This function will calculate the month as a digit based on the user selected month
        //from the spinner widget
        //month = String.valueOf(monthSpinner.getSelectedItemPosition()+1);
        Log.e(" month show ", month);
        firstchar= String.valueOf(month.charAt(0));
        Log.e(" first ", firstchar);
        if((firstchar.equals('1')==false)){
            //set the month value as the selected spinner month
            month = addChar(month, '0',0);
            Log.e(" real month show ", month);

        }
    }

    private void AssignData() {
        //This function assigns values to variables to produce a graph
        Log.e("AssignData1: ","Called" );

        Set<String> uniquelabels = new HashSet<>(allordertiitles);

        for (String key : uniquelabels){
            Log.e("forloopcall: ","Called" );
            Log.e( "AssignData: " ,key);
            entries.add(new ValueDataEntry(key,Collections.frequency(allordertiitles,key)));
        }

        Column column = cartesian.column(entries);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("Amount:{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Item Report for the month of "+monthlist[Integer.parseInt(month)-1]);

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Menu Items");
        cartesian.yAxis(0).title("Amount Bought");

        anyChartView.setChart(cartesian);


    }


    private void DoBreakfastOrdersQuery(final String thismonth) {
        getAllordertiitles().clear();
        //This function will assign the orders of the current user to a list
        final ProgressDialog progressDialog1 = new ProgressDialog(ItemAmtReport.this);
        progressDialog1.setMessage("Getting My Breakfast Orders");
        progressDialog1.show();

        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders breakfastitems = dataSnapshot.getValue(Orders.class);
                    String mydate = breakfastitems.getDate();
                    String [] dateParts = mydate.split("-");
                    String numbermonth = dateParts[1];
                    Log.e("breakfastnumbermonth",numbermonth );
                    if (numbermonth.equals(thismonth)){
                        for (String s : breakfastitems.getOrdertitle()){
                            //Retrieve number value only between the parentheses
                            String number = s.substring(s.indexOf("(")+2,s.indexOf(")"));
                            for (int i = 0; i <Integer.valueOf(number) ; i++) {
                                String noparantheses = s.split("[\\](},]")[0];
                                setAllordertiitles(noparantheses);
                                Log.e(number,noparantheses );
                            }

                        }
                    }

                }

                progressDialog1.dismiss();
                progressDialog1.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void DoLunchOrdersQuery(final String thismonth) {
        getAllordertiitles().clear();
        final ProgressDialog progressDialog2 = new ProgressDialog(ItemAmtReport.this);
        progressDialog2.setMessage("Getting My Orders");
        progressDialog2.show();

        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    final Orders lunchitems = dataSnapshot.getValue(Orders.class);
                    String mydate = lunchitems.getDate();
                    String [] dateParts = mydate.split("-");
                    String numbermonth = dateParts[1];
                    Log.e("lunchnumbermonth",numbermonth );
                    if (numbermonth.equals(thismonth)){
                        for (String s : lunchitems.getOrdertitle()) {
                            //Retrieve the number value only between the parentheses
                            String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                            for (int i = 0; i < Integer.valueOf(number); i++) {
                                String noparantheses = s.split("[\\](},]")[0];
                                setAllordertiitles(noparantheses);
                                Log.e(number, noparantheses);
                            }
                        }


                    }


                }
                AssignData();
                progressDialog2.cancel();
                progressDialog2.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }
    public ArrayList<String> getAllordertiitles() {
        return allordertiitles;
    }

    public void setAllordertiitles(String allordertiitles) {
        Log.e("setAllordertiitles: ","called" );
        this.allordertiitles.add(allordertiitles);
    }
    public String addChar(String str, char ch, int position) {
        //This function will add a character to the desired position in a given string
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }
    private void createImage(){
        Date date = new Date();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating PDF...");
        dialog.show();

        Bitmap bitmap = getBitmapFromView(mscrollView,mscrollView.getChildAt(0).getHeight(),mscrollView.getChildAt(0).getWidth());

        try {
            File defaultFile = new File(getApplicationContext().getExternalFilesDir(null)+"/JEP_Reports");
            Log.e("filepath",defaultFile.toString() );
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = "Admin Report "+monthlist[Integer.parseInt(month)-1] +date.getTime()+".jpg";
            File file = new File(defaultFile,filename);
            if (file.exists()) {
                file.delete();
                file = new File(defaultFile,filename);
            }

            FileOutputStream output = new FileOutputStream(file);
            Log.e("filepath2",file.toString());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            pie_weekly_expenditure.addImageToGallery(String.valueOf(file),this);

            dialog.dismiss();

            Toast.makeText(this, "Image saved to folder JEP_Reports ", Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //create bitmap from the view
    private Bitmap getBitmapFromView(View view,int height,int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.genreport, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.makereport:
                mRequestPermissionHandler.requestPermission(ItemAmtReport.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(pie_weekly_expenditure.this, "request permission success", Toast.LENGTH_SHORT).show();
                        createImage();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(ItemAmtReport.this, "request permission failed", Toast.LENGTH_SHORT).show();
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
