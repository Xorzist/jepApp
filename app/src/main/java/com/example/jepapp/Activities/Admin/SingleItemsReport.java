package com.example.jepapp.Activities.Admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LabelsOverlapMode;
import com.anychart.enums.Orientation;
import com.anychart.enums.ScaleStackMode;
import com.example.jepapp.Activities.Users.pie_weekly_expenditure;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.example.jepapp.RequestPermissionHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class SingleItemsReport extends AppCompatActivity {
    private AnyChartView barChart;
    private String name;
    private DatabaseReference databaseReferencebreakfast,databaseReferencelunch;
    LinearLayout cashlayout, lunchlayout;
    private RequestPermissionHandler mRequestPermissionHandler;
    private LinearLayout mLinearLayout;
    private String[] months = {"January", "February", "March", "April", "May","June","July","August","September",
            "October","November","December"};
    private String month,year,newdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_layout);
        barChart = findViewById(R.id.barChart);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        name = Objects.requireNonNull(getIntent().getExtras()).getString("name");
        //setting the title of the action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle(name+" Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRequestPermissionHandler = new RequestPermissionHandler();
        mLinearLayout = findViewById(R.id.reportsview);
        Date date = new Date();
        newdate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        cashlayout = findViewById(R.id.cashcardlayout);
        lunchlayout = findViewById(R.id.breakfastlunchlayout);
        cashlayout.setVisibility(View.GONE);
        lunchlayout.setVisibility(View.GONE);
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
        //get information from firebase
        getInfo();
        //extracts the month and year from the system's date
        String[] dateParts = newdate.split("-");
        month = dateParts[1];
        year = dateParts[2];
    }

    //function to split the array
    private Integer[] getSizeforGraph(String month, Integer[] monthcount) {
        //change string into an integer value
        Integer end = Integer.parseInt(month);

        // Get the slice of the array
        Integer[] slice = new Integer[end];

        // Copy elements of arr to slice
        for (int i = 0; i < slice.length; i++) {
            slice[i] = monthcount[i];
        }
       //returns the shorted array
        return slice;
    }
    //function to slice the months array
    private String[] getMonthsforGraph(String month, String[] months) {
        //change string into an integer value
        Integer end = Integer.parseInt(month);
        // Get the slice of the array
        String[] slice = new String[end];

        // Copy elements of arr to slice
        for (int i = 0; i < slice.length; i++) {
            slice[i] = months[i];
        }
        //returns the shorted array
        return slice;
    }


    private void getInfo() {
        final Integer[] monthcount = {0,0,0,0,0,0,0,0,0,0,0,0};
        //retrieves breakfast data from firebase
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders orderitems = snapshot.getValue(Orders.class);
                    //retrieves only completed orders
                    assert orderitems != null;
                    if(orderitems.getStatus().toLowerCase().equals("completed")){
                        for (String s : orderitems.getOrdertitle()){
                            //retrieve the string only
                            String noparantheses = s.split("[\\](},]")[0];
                            //search if the name of the item for report is included in the order and adds all occurrences
                            if (noparantheses.equals(name)) {
                                //retrieve date value
                                String date = orderitems.getDate();
                                String[] dateParts = date.split("-");
                                String month = dateParts[1];
                                //Retrieve number value only between the parentheses
                                String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                                Integer num = Integer.parseInt(number);
                                Integer x = monthcount[findmonth(month)];
                                monthcount[findmonth(month)] = num+x;
                            }
                        }
                    }
                    // get the lunch data and add it to the list
                }getLunch(monthcount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLunch(final Integer[] monthcount) {
        //retrieves lunch data from firebase
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders orderitems = snapshot.getValue(Orders.class);
                    //retrieves only completed orders
                    assert orderitems != null;
                    if(orderitems.getStatus().toLowerCase().equals("completed")){
                        for (String s : orderitems.getOrdertitle()){
                            //retrieve the string only
                            String noparantheses = s.split("[\\](},]")[0];
                            //search if the name of the item for report is included in the order and adds all occurrences
                            if (noparantheses.equals(name)) {
                                //retrieve date value
                                String date = orderitems.getDate();
                                String[] dateParts = date.split("-");
                                String month = dateParts[1];
                                //Retrieve number value only between the parentheses
                                String number = s.substring(s.indexOf("(") + 2, s.indexOf(")"));
                                Integer num = Integer.parseInt(number);
                                Integer x = monthcount[findmonth(month)];
                                monthcount[findmonth(month)] = num + x;
                            }
                        }
                    }

                }
                // Assigning the title and values of the shortened array to graph
                AssignData(getSizeforGraph(month,monthcount),getMonthsforGraph(month,months));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //returns a index position to be used to assess and change the value of the integer array list based on the month identified
    private int findmonth(String month) {
        Integer val = 0;
        if (month.equals("01")) {
           val = 0;
        } else if (month.equals("02")) {
           val = 1;
        } else if (month.equals("03")) {
            val = 2;
        } else if (month.equals("04")) {
            val = 3;
        } else if (month.equals("05")) {
            val = 4;
        } else if (month.equals("06")) {
            val = 5;
        } else if (month.equals("07")) {
            val = 6;
        } else if (month.equals("08")) {
            val = 7;
        } else if (month.equals("09")) {
            val = 8;
        } else if (month.equals("10")) {
            val = 9;
        } else if (month.equals("11")) {
           val = 10;
        } else if (month.equals("12")) {
           val = 11;
        }
        return val;
    }

    //assigns headings and data to graph
    private void AssignData(Integer[]newcount,String[]months) {
        final Cartesian cartesian = AnyChart.bar();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.yScale().stackMode(ScaleStackMode.NONE);
        cartesian.yAxis(0).labels(true);
        cartesian.legend().align(Align.CENTER).enabled(true);
        cartesian.yAxis(0d).title("Number of Items Sold");
        cartesian.xAxis(0d).overlapMode(LabelsOverlapMode.NO_OVERLAP);
        Linear xAxis1 = cartesian.xAxis(1d);
        xAxis1.enabled(true);
        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.NO_OVERLAP);
        cartesian.title(name +" sales "+year+" as at "+newdate);
        Set set = Set.instantiate();
        set.data(getData(newcount,months));
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Bar series1 = cartesian.bar(series1Data);
        series1.name("Amount of "+name+" sold by month")
                .color("#9d7eb7");
        series1.labels(true)
                .hover().labels(true);
        series1.labels().position("center");
        series1.labels().fontColor("#ffffff");
        series1.labels().anchor("center");
        series1.tooltip()
                .position("left")
                .anchor(Anchor.RIGHT_CENTER);

        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.legend().selectable(true);

        cartesian.legend().enabled(true);
        cartesian.legend().inverted(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 20d, 0d);

        barChart.setChart(cartesian);
    }

    //adds the values to respective axis on the graph
    private ArrayList getData(Integer[]itemcount, String[] months){
        ArrayList<DataEntry> entries = new ArrayList<>();

        for (int i = 0; i <months.length ; i++) {
            entries.add(new ValueDataEntry(months[i],itemcount[i]));
        }

        return entries;
    }
    //Function to create and save an image file from the graph that is produced
    private void createImage(){
        Date date = new Date();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating PDF...");
        dialog.show();

        Bitmap bitmap = getBitmapFromView(mLinearLayout,mLinearLayout.getHeight(),mLinearLayout.getWidth());
        //Attempt to store the image in a specific folder
        try {
            File defaultFile = new File(getApplicationContext().getExternalFilesDir(null)+"/JEP_Reports");
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = "Admin Report for  "+name+ "  "+ date.getTime()+".jpg";
            File file = new File(defaultFile,filename);
            if (file.exists()) {
                file.delete();
                file = new File(defaultFile,filename);
            }

            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            //Calls the function to add image to user gallery from pie_weekly_expenditure class
            pie_weekly_expenditure.addImageToGallery(String.valueOf(file),this);

            dialog.dismiss();

            Toast.makeText(this, "Check the folder JEP_Reports for the Image", Toast.LENGTH_LONG).show();


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

    //shows menu option
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.genreport, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.makereport:
                mRequestPermissionHandler.requestPermission(SingleItemsReport.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                       createImage();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(SingleItemsReport.this, "request permission failed", Toast.LENGTH_SHORT).show();
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }

 }

