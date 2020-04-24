package com.example.jepapp.Activities.Admin;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.example.jepapp.RequestPermissionHandler;

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

import static com.example.jepapp.Activities.Users.pie_weekly_expenditure.addImageToGallery;

public class ItemAmtReport extends AppCompatActivity {

    private FirebaseAuth mAuth;
    List<com.example.jepapp.Models.Orders> allorderslist;
    ArrayList<String>allordertiitles;
    private ProgressDialog SetupChartdialog;
    private DatabaseReference databaseReferencebreakfast;
    private DatabaseReference databaseReferencelunch;
    List<DataEntry> entries;
    AnyChartView anyChartView;
    Cartesian cartesian;
    Spinner monthSpinner;
    private String month;
    private String firstchar;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monthlist);
        monthSpinner.setAdapter(adapter);
        mRequestPermissionHandler = new RequestPermissionHandler();
        mscrollView = findViewById(R.id.itemamtscrollview);
        allorderslist = new ArrayList<>();
        allordertiitles = new ArrayList<>();
        entries = new ArrayList<>();
        SetupChartdialog = new ProgressDialog(ItemAmtReport.this,R.style.Theme_AppCompat_Light_Dialog);
        SetupChartdialog.setMessage("Plotting requested Data...");
        SetupChartdialog.show();
        anyChartView =  findViewById(R.id.newpie);
        cartesian = AnyChart.column();
        month = getIntent().getExtras().getString("thismonth");

        //Set spinner as current month
        monthSpinner.setSelection(Integer.parseInt(month)-1);

        //Function to calculate the calendar month based on the item selected in the spinner
        monthcalculator();
        //dbreference for breakdast orders
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
        DoBreakfastOrdersQuery(month);
        //dbreference for lunch orders
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
        DoLunchOrdersQuery(month);

        //Function to handle the user selecting different months
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSpinner.setSelection(position);
                //check if the user has interacted with the spinner
                if(userIsInteracting) {
                    //Launch the appropriate interface
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

    SetupChartdialog.dismiss();
    SetupChartdialog.cancel();
    }


    //This function will calculate the month as a digit based on the user selected month
    //from the spinner widget
    private void monthcalculator() {

        firstchar= String.valueOf(month.charAt(0));
        if((firstchar.equals('1')==false)){
            //set the month value as the selected spinner month
            month = addChar(month, '0',0);

        }
    }

    //This function assigns values to variables to produce a graph
    private void AssignData() {

        Set<String> uniquelabels = new HashSet<>(allordertiitles);

        for (String key : uniquelabels){
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

    //This function will retrieve specific breakfast orders from the database
    private void DoBreakfastOrdersQuery(final String thismonth) {
        getAllordertiitles().clear();

        final ProgressDialog BreakfastQueryDialog = new ProgressDialog(ItemAmtReport.this);
        BreakfastQueryDialog.setMessage("Getting All Breakfast Orders");
        BreakfastQueryDialog.show();

        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Orders breakfastitems = dataSnapshot.getValue(Orders.class);
                    if (breakfastitems.getStatus().toLowerCase().equals("completed")){
                        String mydate = breakfastitems.getDate();
                        String [] dateParts = mydate.split("-");
                        String numbermonth = dateParts[1];
                        if (numbermonth.equals(thismonth)){
                            for (String s : breakfastitems.getOrdertitle()) {
                                //Retrieve number value only between the parentheses
                                String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                                for (int i = 0; i < Integer.valueOf(number); i++) {
                                    String noparantheses = s.split("[\\](},]")[0];
                                    setAllordertiitles(noparantheses);
                                }
                            }
                        }
                    }
                }

                BreakfastQueryDialog.dismiss();
                BreakfastQueryDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    //This function will retrieve specific lunch orders from the database
    private void DoLunchOrdersQuery(final String thismonth) {
        getAllordertiitles().clear();
        final ProgressDialog LunchQueryDialog = new ProgressDialog(ItemAmtReport.this);
        LunchQueryDialog.setMessage("Getting All Lunch Orders");
        LunchQueryDialog.show();

        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    final Orders lunchitems = dataSnapshot.getValue(Orders.class);
                    if(lunchitems.getStatus().toLowerCase().equals("completed")){
                        String mydate = lunchitems.getDate();
                        String [] dateParts = mydate.split("-");
                        String numbermonth = dateParts[1];
                        if (numbermonth.equals(thismonth)) {
                            for (String s : lunchitems.getOrdertitle()) {
                                //Retrieve the number value only between the parentheses
                                String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                                for (int i = 0; i < Integer.valueOf(number); i++) {
                                    String noparantheses = s.split("[\\](},]")[0];
                                    setAllordertiitles(noparantheses);
                                }
                            }
                        }

                    }
                }
                AssignData();
                LunchQueryDialog.cancel();
                LunchQueryDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }
    public ArrayList<String> getAllordertiitles() {
        return allordertiitles;
    }

    //This function will add a given variable to an array list
    public void setAllordertiitles(String allordertiitles) {
        this.allordertiitles.add(allordertiitles);
    }

    //This function will add a character to the desired position in a given string
    public String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

    @Override
    //Function to determine if a user has interacted with an item on screen
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    //Function to create and save an image file from the graph that is produced
    private void createImage(){
        Date date = new Date();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Image File...");
        dialog.show();

        Bitmap bitmap = getBitmapFromView(mscrollView,mscrollView.getChildAt(0).getHeight(),mscrollView.getChildAt(0).getWidth());

        //Attempt to store the image in a specific folder
        try {
            File defaultFile = new File(getApplicationContext().getExternalFilesDir(null)+"/JEP_Reports");
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = "Admin Report "+monthlist[Integer.parseInt(month)-1] +date.getTime()+".jpg";
            File file = new File(defaultFile,filename);
            if (file.exists()) {
                file.delete();
                file = new File(defaultFile,filename);
            }

            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            //Adds image to user gallery for ease of access
            addImageToGallery(String.valueOf(file),this);

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
    //Retrieves the results of requesting from the user to allow access to storage
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
