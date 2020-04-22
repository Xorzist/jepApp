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
import android.widget.TextView;
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
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.example.jepapp.Activities.Users.pie_weekly_expenditure;
import com.example.jepapp.Models.Orders;
import com.example.jepapp.R;
import com.example.jepapp.RequestPermissionHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class DailyIncomeReport extends AppCompatActivity {
    private AnyChartView barChart;
    private String name = "Daily Income report";
    private String start,end;
    private Query databaseReferencebreakfast, databaseReferencelunch;
    TextView card,cash,breakfast,lunch;
    LinearLayout casdandcard, breakfastandlunch;
    private RequestPermissionHandler mRequestPermissionHandler;
    private LinearLayout mLinearLayout;
    ArrayList<String> daterange;
    private Date startdate,enddate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_layout);
        //setting the title of the action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get intent data and assign to variables
        start = getIntent().getExtras().getString("startdate");
        end = getIntent().getExtras().getString("enddate");
        barChart = findViewById(R.id.barChart);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        mRequestPermissionHandler = new RequestPermissionHandler();
        mLinearLayout = findViewById(R.id.reportsview);
        daterange =new ArrayList<>();
        // convert date values received into a date object
        try {
            startdate =new SimpleDateFormat("dd-MM-yyyy").parse(start);
            enddate =new SimpleDateFormat("dd-MM-yyyy").parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //assign variables
        card= findViewById(R.id.lunchcard_value_report);
        cash = findViewById(R.id.cash_value_report);
        breakfast = findViewById(R.id.breakfast_value_report);
        lunch = findViewById(R.id.lunch_report);
        casdandcard = findViewById(R.id.cashcardlayout);
        breakfastandlunch = findViewById(R.id.breakfastlunchlayout);
        //gets all the dates that exist between the start and end dates given
        getDaysBetweenDates(startdate,enddate);
        //sorts Breakfast and lunch orders by date
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders").orderByChild("date");
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders").orderByChild("date");
        getInfo();

    }



    private void getInfo() {
        final ArrayList cashcount = new ArrayList();
        final ArrayList cardcount = new ArrayList();
        final ArrayList justdates = new ArrayList();
        final Integer[] costforbreakfast = {0};
        final Integer[] costforcard = { 0 };
        final Integer[] costforcash = { 0 };
        //retrieve breakfast data from firebase
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //retrieves data from database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    //checks for only completed orders
                    if (breakfastitems.getStatus().toLowerCase().equals("completed")) {
                        //gets the cost of all completed breakfast orders
                        costforbreakfast[0] = costforbreakfast[0] + breakfastitems.getCost().intValue();
                        //sorts completed order by payment type (cash or card)
                        if (breakfastitems.getPayment_type().toLowerCase().equals("cash")) {
                            if (daterange.contains(breakfastitems.getDate())) {
                                cashcount.add(breakfastitems.getDate());
                                cashcount.add(String.valueOf(breakfastitems.getCost()));
                                justdates.add(breakfastitems.getDate());
                                Integer cost = breakfastitems.getCost().intValue();
                                costforcash[0] = costforcash[0] + cost;
                            }
                        } else {
                            if (daterange.contains(breakfastitems.getDate())) {
                                cardcount.add(breakfastitems.getDate());
                                cardcount.add(String.valueOf(breakfastitems.getCost()));
                                justdates.add(breakfastitems.getDate());
                                Integer cost = breakfastitems.getCost().intValue();
                                costforcard[0] = costforcard[0] + cost;
                            }

                        }

                    }

                }
                //gets the lunch order data from database
                getLunch(cashcount,cardcount, costforcash, costforcard, costforbreakfast,justdates);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLunch(final ArrayList cashcount, final ArrayList cardcount, final Integer[] costforcash, final Integer[] costforcard, final Integer[] costforbreakfast, final ArrayList justdates) {
        final Integer[] costforlunch = {0};
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            //retrieves lunch from firebase
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders lunchitems = snapshot.getValue(Orders.class);
                    //gets only completed orders
                    if (lunchitems.getStatus().toLowerCase().equals("completed")){
                        //gets the cost of all completed lunch orders
                        costforlunch[0] = costforlunch[0] + lunchitems.getCost().intValue();
                        //sorts completed order by payment type (cash or card)
                        if (lunchitems.getPayment_type().toLowerCase().equals("cash")) {
                            if (daterange.contains(lunchitems.getDate())) {
                                cashcount.add(lunchitems.getDate());
                                cashcount.add(String.valueOf(lunchitems.getCost()));
                                justdates.add(lunchitems.getDate());
                                Integer cost = lunchitems.getCost().intValue();
                                costforcash[0] = costforcash[0] + cost;
                            }
                        }
                        else {
                            if (daterange.contains(lunchitems.getDate())) {
                                cardcount.add(lunchitems.getDate());
                                cardcount.add(String.valueOf(lunchitems.getCost()));
                                justdates.add(lunchitems.getDate());
                                Integer cost = lunchitems.getCost().intValue();
                                costforcard[0] = costforcard[0] + cost;
                            }
                        }
                    }


                }
                // removes recurring dates in the list of dates and adds the corresponding values in the cash and card lists
                CalculateCashandCard(justdates,cashcount,cardcount);
                // assigns the cost calculated to their respective positions on the graph
                AssignVariables(costforbreakfast[0], costforlunch[0],costforcash[0],costforcard[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void CalculateCashandCard(ArrayList justdates, ArrayList cashcount, ArrayList cardcount) {
        ArrayList cashList = new ArrayList();
        ArrayList cardList = new ArrayList();
        //removes recurring dates
        java.util.Set<String> uniquedates = new HashSet<>(justdates);
        //adds the values of all recurring dates
        for (String key : uniquedates) {

            cashList.add(calculatevalues(key,cashcount));
            cardList.add(calculatevalues(key,cardcount));
        }
        List<String> datesList = new ArrayList<>(uniquedates);

        //Assigning the title and values in the arraylist to the graph
        AssignData(datesList,cashList,cardList);
    }

    private int calculatevalues(String date, ArrayList<String> list) {
        int total=0;
        for (int i = 0; i <list.size() ; i++) {
            if( date.equals(list.get(i))) {
                total+=Integer.parseInt(list.get(i+1));
            }

        }
        return total;
    }


    private void AssignVariables(Integer costbreakfast, Integer costlunch, Integer costcash, Integer costcard) {
        breakfast.setText("$"+costbreakfast.toString());
        lunch.setText("$"+costlunch.toString());
        cash.setText("$"+costcash.toString());
        card.setText("$"+costcard.toString());

    }

    //assigns headings and data to graph
    private void AssignData(List<String> uniquedates, ArrayList cashcount, ArrayList cardcount) {
        final Cartesian cartesian = AnyChart.bar();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.yScale().stackMode(ScaleStackMode.NONE);
        cartesian.yAxis(0).labels(true);
        cartesian.yAxis(0).labels().format("function() {\n" +
                "    return Math.abs(this.value).toLocaleString();\n" +
                "  }");

        cartesian.legend().align(Align.CENTER).enabled(true);

        cartesian.yAxis(0d).title("Income from sales");

        cartesian.xAxis(0d).overlapMode(LabelsOverlapMode.NO_OVERLAP);
        cartesian.xAxis(0d).title("Dates");
        Linear xAxis1 = cartesian.xAxis(1d);
        xAxis1.enabled(true);
        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.NO_OVERLAP);
        cartesian.title("Daily Income for period "+start+ " --->" + end);
        Set set = Set.instantiate();
        set.data(getData(uniquedates,cashcount,cardcount));
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");
        cartesian.legend().inverted(true);

        Bar series1 = cartesian.bar(series1Data);

        series1.name("Cash")
                .color("#7fa675");
        series1.labels(true);
        series1.labels().position("center");
        series1.labels().fontColor("#ffffff");
        series1.labels().anchor("center");
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER);

        Bar series2 = cartesian.bar(series2Data);
        series2.name("Lunch Card")
                .color("#51b0ca");
        series2.labels(true);

        series2.labels().position("center");
        series2.labels().fontColor("#ffffff");
        series2.labels().anchor("center");
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER);

        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.legend().selectable(true);

        cartesian.legend().enabled(true);
        cartesian.legend().inverted(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 20d, 0d);
        cartesian.tooltip()
                .title(false)
                .separator(false)
                .displayMode(TooltipDisplayMode.SEPARATED)
                .positionMode(TooltipPositionMode.POINT)
                .useHtml(true)
                .fontSize(12d)
                .offsetX(5d)
                .offsetY(0d)
                .format(
                        "function() {\n" +
                                "      return '<span style=\"color: #D9D9D9\">$</span>' + Math.abs(this.value).toLocaleString();\n" +
                                "    }");
        barChart.setChart(cartesian);
    }

    //adds the values to respective axis on the graph
    private ArrayList getData(List<String> uniquedates, ArrayList<Integer> cashcount, ArrayList<Integer> cardcount){
        ArrayList<DataEntry> entries = new ArrayList<>();
        for (int i = 0; i<uniquedates.size() ; i++) {
            entries.add(new CustomDataEntry(uniquedates.get(i),cashcount.get(i),cardcount.get(i)));
        }

        return entries;
    }
    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
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

            String filename = "Admin Report for Weekly Sales "+ date.getTime()+".jpg";
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
    private Bitmap getBitmapFromView(View view, int height, int width) {
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
                mRequestPermissionHandler.requestPermission(DailyIncomeReport.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {

                        createImage();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(DailyIncomeReport.this, "request permission failed", Toast.LENGTH_SHORT).show();
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //returns a list with the days that occur between the selected days
    public boolean getDaysBetweenDates(Date startdate, Date enddate)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);
        Calendar c = new GregorianCalendar();
         c.setTime(enddate);
        //Incrementing the date by 1 day
        c.add(Calendar.DAY_OF_MONTH, 1);

        while (calendar.getTime().before(c.getTime()))
        {
            Date result = calendar.getTime();
            daterange.add(new SimpleDateFormat("dd-MM-yyyy").format(result));
            calendar.add(Calendar.DATE, 1);

        }

        return true;
    }
}

