package com.example.jepapp.Activities.Admin;

import android.Manifest;
import android.app.ProgressDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ItemSalesWeeklyReport2 extends AppCompatActivity {
    private AnyChartView barChart;
    private String name;
    private Query databaseReferencebreakfast, databaseReferencelunch;
    //Integer[] monthlynumber = {0};
    Date date;
    String newdate;
    ArrayList<String> dates;
    TextView card,cash,breakfast,lunch;
    LinearLayout casdandcard, breakfastandlunch;
    private LinearLayout mLinearLayout;
    private RequestPermissionHandler mRequestPermissionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_review);
        mLinearLayout = findViewById(R.id.perfromancereviewview);
        mRequestPermissionHandler = new RequestPermissionHandler();
        barChart = (AnyChartView)findViewById(R.id.barChart);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        // getting date
        date = new Date();
        newdate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        //test subtract days method
        dates = new ArrayList<>();
        dates.add(newdate);
        dates.addAll(subtractDays(date));
        //assign variables
        card= findViewById(R.id.lunchcard_value_report);
        cash = findViewById(R.id.cash_value_report);
        breakfast = findViewById(R.id.breakfast_value_report);
        lunch = findViewById(R.id.lunch_report);
        casdandcard = findViewById(R.id.cashcardlayout);
        breakfastandlunch = findViewById(R.id.breakfastlunchlayout);
//

        Log.e("newdate",dates.toString());

        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders").orderByChild("date").startAt(dates.get(6)).endAt(dates.get(0));
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders").orderByChild("date").startAt(dates.get(6)).endAt(dates.get(0));
        getInfo();

    }

    public static ArrayList<String> subtractDays(Date date) {
        int i=1;
        GregorianCalendar cal = new GregorianCalendar();
        ArrayList<String> subtracteddates = new ArrayList<>();
        while(i<=6){
            cal.setTime(date);
            cal.add(Calendar.DATE, -i);
            String newdate = new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
            subtracteddates.add(newdate);
            i+=1;
        }

        return subtracteddates;
    }


    private void getInfo() {
        final Integer[] cashcount = {0,0,0,0,0,0,0};
        final Integer[] cardcount ={0,0,0,0,0,0,0};
        final Integer[] costforbreakfast = {0};
        final Integer[] costforcard = { 0 };
        final Integer[] costforcash = { 0 };
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    costforbreakfast[0] = costforbreakfast[0] + breakfastitems.getCost().intValue();

                    for (int i = 0; i <dates.size() ; i++) {
                        if (breakfastitems.getDate().equals(dates.get(i))){
                            if (breakfastitems.getPayment_type().toLowerCase().equals("cash")){
                                Integer x = cashcount[i];
                                Integer cost = breakfastitems.getCost().intValue();
                                costforcash[0] = costforcash[0] + cost;
                                cashcount[i] = x + cost;
                            }
                            else{
                                Integer x = cardcount[i];
                                Integer cost = breakfastitems.getCost().intValue();
                                costforcard[0] = costforcard[0] + cost;
                                cardcount[i] = x + cost;
                            }


                        }

                    }

                } getLunch(cashcount,cardcount, costforcash, costforcard, costforbreakfast);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLunch(final Integer[] cashcount, final Integer[] cardcount, final Integer[] costforcash,  final Integer[] costforcard, final Integer[] costforbreakfast) {
        final Integer[] costforlunch = {0};
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders lunchitems = snapshot.getValue(Orders.class);
                    costforlunch[0] = costforlunch[0] + lunchitems.getCost().intValue();
                    for (int i = 0; i <dates.size() ; i++) {
                        if (lunchitems.getPayment_type().toLowerCase().equals("cash")){
                            Integer x = cashcount[i];
                            Integer cost = lunchitems.getCost().intValue();
                            costforcash[0]+= cost;
                            cashcount[i] = x + cost;
                        }
                        else{
                            Integer x = cardcount[i];
                            Integer cost = lunchitems.getCost().intValue();
                            costforcard[0] += cost;
                            cardcount[i] = x + cost;
                        }

                    }

                    //          Assigning the title and values in the arraylist to graph
                }AssignData(dates,cashcount,cardcount);
                AssignVariables(costforbreakfast[0], costforlunch[0],costforcash[0],costforcard[0]);
               // Log.e("in lunch now",Arrays.toString(monthlynumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void AssignVariables(Integer costbreakfast, Integer costlunch, Integer costcash, Integer costcard) {
        breakfast.setText("$"+costbreakfast.toString());
        lunch.setText("$"+costlunch.toString());
        cash.setText("$"+costcash.toString());
        card.setText("$"+costcard.toString());

    }


    private void AssignData(ArrayList<String> dates, Integer[] cashcount, Integer[] cardcount) {
        final Cartesian cartesian = AnyChart.bar();

        cartesian.animation(true);


        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.yScale().stackMode(ScaleStackMode.NONE);
        cartesian.yAxis(0).labels(true);
        cartesian.yAxis(0).labels().format("function() {\n" +
                "    return Math.abs(this.value).toLocaleString();\n" +
                "  }");


        cartesian.legend().align(Align.CENTER).enabled(true);

        cartesian.yAxis(0d).title("Weekly Income");

        cartesian.xAxis(0d).overlapMode(LabelsOverlapMode.NO_OVERLAP);

        Linear xAxis1 = cartesian.xAxis(1d);
        xAxis1.enabled(true);

        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.NO_OVERLAP);

        cartesian.title("Weekly sales");
        Set set = Set.instantiate();
        set.data(getData(dates,cashcount,cardcount));
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");
        cartesian.legend().inverted(true);

        Bar series1 = cartesian.bar(series1Data);

        series1.name("Cash")
                .color("#33cc5a");
        series1.labels(true);
        series1.labels().position("center");
        series1.labels().fontColor("#ffffff");
        series1.labels().anchor("center");
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER);

        Bar series2 = cartesian.bar(series2Data);
        series2.name("Lunch Card")
                .color("#e6191e");
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


    private ArrayList getData(ArrayList<String> dates, Integer[] cashcount, Integer[] cardcount){
        ArrayList<DataEntry> entries = new ArrayList<>();
        //ArrayList<Number> likes = new ArrayList<>(),dislikes = new ArrayList<>();
        //String[] months = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday","Sunday"};

        for (int i = 0; i <7 ; i++) {
            entries.add(new CustomDataEntry(dates.get(i),cashcount[i],cardcount[i]));
        }

        return entries;
    }
    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }

    private void createImage(){
        Date date = new Date();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating PDF...");
        dialog.show();

        Bitmap bitmap = getBitmapFromView(mLinearLayout,mLinearLayout.getHeight(),mLinearLayout.getWidth());

        try {
            File defaultFile = new File(getApplicationContext().getExternalFilesDir(null)+"/JEP_Reports");
            Log.e("filepath",defaultFile.toString() );
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = "Admin Report "+ date.getTime()+".jpg";
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

            Toast.makeText(this, "Check the folder JEP_Reports for the file", Toast.LENGTH_LONG).show();


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
                mRequestPermissionHandler.requestPermission(ItemSalesWeeklyReport2.this, new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(pie_weekly_expenditure.this, "request permission success", Toast.LENGTH_SHORT).show();
                        createImage();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(ItemSalesWeeklyReport2.this, "request permission failed", Toast.LENGTH_SHORT).show();
                    }
                });

                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}

