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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ItemSalesReportNew extends AppCompatActivity {
    private AnyChartView barChart;
    private String name;
    private DatabaseReference databaseReferencebreakfast, databaseReferencelunch;
    private Integer[] monthlynumber = {0};
//    Date date;
//    String newdate;
//    ArrayList<String> dates;
    private TextView card,cash,breakfast,lunch,titlebreakfast, titlelunch;
    private LinearLayout breakfastandlunch;
    private RequestPermissionHandler mRequestPermissionHandler;
    private LinearLayout mLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_review);
        barChart = (AnyChartView)findViewById(R.id.barChart);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        card= findViewById(R.id.lunchcard_value_report);
        cash = findViewById(R.id.cash_value_report);
        mRequestPermissionHandler = new RequestPermissionHandler();
        mLinearLayout = findViewById(R.id.perfromancereviewview);
        breakfastandlunch = findViewById(R.id.breakfastlunchlayout);
        breakfastandlunch.setVisibility(View.GONE);
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders");
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders");
        getInfo();

    }




    private void getInfo() {
        final Integer[] cashcount = {0,0,0,0,0,0,0,0,0,0,0,0};
        final Integer[] cashval ={0};
        final Integer[] cardcount = {0,0,0,0,0,0,0,0,0,0,0,0};
        final Integer[] cardval ={0};
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    //retrieve date value
                    String date = breakfastitems.getDate();
                    String[] dateParts = date.split("-");
                    String month = dateParts[1];
                    //Retrieve cost of order
                    Integer number = breakfastitems.getCost().intValue();
                    if (breakfastitems.getPayment_type().toLowerCase().equals("cash")){
                        Integer x = cashcount[findmonth(month)];
                        cashcount[findmonth(month)] = number + x;
                        cashval[0]+=number;
                    }else{
                        Integer x = cardcount[findmonth(month)];
                        cardcount[findmonth(month)] = number + x;
                        cardval[0]+=number;
                    }





                }getLunch(cashcount,cardcount,cashval,cardval);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLunch(final Integer[] cashcount, final Integer[] cardcount, final Integer[] cashval, final Integer[] cardval) {

        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders lunchitems = snapshot.getValue(Orders.class);
                    String date = lunchitems.getDate();
                    String[] dateParts = date.split("-");
                    String month = dateParts[1];
                    //Retrieve cost of order
                    Integer number = lunchitems.getCost().intValue();
                    if (lunchitems.getPayment_type().toLowerCase().equals("cash")){
                        Integer x = cashcount[findmonth(month)];
                        cashcount[findmonth(month)] = number + x;
                        cashval[0]+=number;
                    }else{
                        Integer x = cardcount[findmonth(month)];
                        cardcount[findmonth(month)] = number + x;
                        cardval[0]+=number;
                    }


                    //          Assigning the title and values in the arraylist to graph
                }AssignData(cashcount,cardcount);
                AssignVariables(cashval[0],cardval[0]);
                Log.e("in lunch now",Arrays.toString(monthlynumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void AssignVariables(Integer integer, Integer integer1) {
        cash.setText("$"+integer.toString());
        card.setText("$"+integer1.toString());
    }

    private int findmonth(String month) {
        //returns a index position to be used to assess and change the value of the integer array list based on the month identified
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


    private void AssignData(Integer[] cashcount, Integer[] cardcount) {
        final Cartesian cartesian = AnyChart.bar();

        cartesian.animation(true);


        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.yScale().stackMode(ScaleStackMode.NONE);
        cartesian.yAxis(0).labels(true);
        cartesian.yAxis(0).labels().format("function() {\n" +
                "    return Math.abs(this.value).toLocaleString();\n" +
                "  }");


        cartesian.legend().align(Align.CENTER).enabled(true);

        cartesian.yAxis(0d).title("Monthly Income");

        cartesian.xAxis(0d).overlapMode(LabelsOverlapMode.NO_OVERLAP);

        Linear xAxis1 = cartesian.xAxis(1d);
        xAxis1.enabled(true);

        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.NO_OVERLAP);

        cartesian.title("Monthly sales");
        Set set = Set.instantiate();
        set.data(getData(cashcount,cardcount));
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

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
        series1.labels().format("${%value}");
        Bar series2 = cartesian.bar(series2Data);
        series2.name("Card")
                .color("#e6191e");
        series2.labels(true);

        series2.labels().position("center");
        series2.labels().fontColor("#ffffff");
        series2.labels().anchor("center");
        series2.labels().format("${%value}");
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER);


        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.legend().selectable(true);
        cartesian.legend().inverted(true);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 20d, 0d);
//        cartesian.tooltip()
//                .title(false)
//                .separator(false)
//                .displayMode(TooltipDisplayMode.SEPARATED)
//                .positionMode(TooltipPositionMode.POINT)
//                .useHtml(true)
//                .fontSize(12d)
//                .offsetX(5d)
//                .offsetY(0d)
//                .format(
//                        "function() {\n" +
//                                "      return '<span style=\"color: #D9D9D9\">$</span>' + Math.abs(this.value).toLocaleString();\n" +
//                                "    }");
        barChart.setChart(cartesian);
    }


    private ArrayList getData(Integer[] cashcount, Integer[] cardcount){
        ArrayList<DataEntry> entries = new ArrayList<>();
        //ArrayList<Number> likes = new ArrayList<>(),dislikes = new ArrayList<>();
        //String[] months = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday","Sunday"};
        String[] months = {"January", "February", "March", "April", "May","June","July","August","September",
                "October","November","December"};
        for (int i = 0; i <12 ; i++) {
            entries.add(new CustomDataEntry(months[i],cashcount[i],cardcount[i]));
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

            String filename = "Admin Report for  "+name+ "  "+ date.getTime()+".jpg";
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
                mRequestPermissionHandler.requestPermission(ItemSalesReportNew.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(pie_weekly_expenditure.this, "request permission success", Toast.LENGTH_SHORT).show();
                        createImage();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(ItemSalesReportNew.this, "request permission failed", Toast.LENGTH_SHORT).show();
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

