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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.LabelsOverlapMode;
import com.anychart.enums.Orientation;
import com.anychart.enums.ScaleStackMode;
import com.example.jepapp.Activities.Users.pie_weekly_expenditure;
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
import java.util.List;

public class PerformanceReviewReport extends AppCompatActivity {
    private AnyChartView barChart;
    List<Integer> reviewslike = new ArrayList<>();
    List<Integer> reviewsdislike = new ArrayList<>();
    LinearLayout cashandcard, breakfastandlunch;
    private RequestPermissionHandler mRequestPermissionHandler;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_review);
        barChart = (AnyChartView)findViewById(R.id.barChart);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        reviewslike = new ArrayList<>();
        reviewsdislike = new ArrayList<>();
        mRequestPermissionHandler = new RequestPermissionHandler();
        mLinearLayout = findViewById(R.id.perfromancereviewview);
        cashandcard = findViewById(R.id.cashcardlayout);
        breakfastandlunch = findViewById(R.id.breakfastlunchlayout);
        cashandcard.setVisibility(View.GONE);
        breakfastandlunch.setVisibility(View.GONE);
        //getReviews();
        Log.e(reviewslike.toString(),reviewsdislike.toString());

        final Cartesian cartesian = AnyChart.bar();

        cartesian.animation(true);


        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.yScale().stackMode(ScaleStackMode.NONE);
        cartesian.yAxis(0).labels(true);
//        cartesian.tooltip()
//                .title(false)
//                .separator(false)
//                .displayMode(TooltipDisplayMode.UNION)
//                .positionMode(TooltipPositionMode.POINT)
//                .useHtml(true)
//                .fontSize(12d)
//                .offsetX(5d)
//                .offsetY(0d)
//                .format(
//                        "function() {\n" +
//                                "      return '<span style=\"color: #D9D9D9\">$</span>' +  Math.abs(this.value).toLocaleString();\n" +
//                                "    }");
//
//        cartesian.yAxis(0).labels().format(
//                "function() {\n" +
//                        "    return Math.abs(this.value).toLocaleString();\n" +
//                        "  }");
        cartesian.legend().align(Align.CENTER).enabled(true);

        cartesian.yAxis(0d).title("Months");

        cartesian.xAxis(0d).overlapMode(LabelsOverlapMode.NO_OVERLAP);

        Linear xAxis1 = cartesian.xAxis(1d);
        xAxis1.enabled(true);
        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.NO_OVERLAP);

        cartesian.title("Reviews by month");
        cartesian.legend().title().enabled(true);
        cartesian.legend().title().text("Click on the likes bar to generate a pie chart")
        .padding(0d, 0d, 10d, 0d);




        DatabaseReference databaseReference;

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Reviews");
        // Query reviewlikes = databaseReference.orderByChild("liked").equalTo("yes");
        //Query reviewdislikes = databaseReference.orderByChild("disliked").equalTo("yes");



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                reviewslike.clear();
                int countjan = 0, countfeb=0,countmar=0,countapril=0,countmay=0,countjune=0,countjul=0
                        ,countaug=0,countsep=0,countoct=0,countnov=0,countdec = 0;
                int dcountjan = 0, dcountfeb=0,dcountmar=0,dcountapril=0,dcountmay=0,dcountjune=0,dcountjul=0
                        ,dcountaug=0,dcountsep=0,dcountoct=0,dcountnov=0,dcountdec = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.Reviews allreviews = dataSnapshot.getValue(com.example.jepapp.Models.Reviews.class);

                    String date = allreviews.getDate();
                    String liked = allreviews.getLiked();
                    String [] dateParts = date.split("-");
                    String month = dateParts[1];
                    if(liked.equals("yes")){
                        if (month.equals("01")){
                            countjan=countjan + 1;
                        }else if (month.equals("02")){
                            countfeb = countfeb + 1;
                        }else if (month.equals("03")){
                            countmar = countmar + 1;
                        }else if (month.equals("04")){
                            countapril = countapril + 1;
                        }else if (month.equals("05")){
                            countmay = countmay + 1;
                        }else if (month.equals("06")){
                            countjune = countjune + 1;
                        }else if (month.equals("07")){
                            countjul = countjul + 1;
                        }else if (month.equals("08")){
                            countaug = countaug + 1;
                        }else if (month.equals("09")){
                            countsep = countsep + 1;
                        }else if (month.equals("10")){
                            countoct = countoct + 1;
                        }else if (month.equals("11")){
                            countnov = countnov + 1;
                        }else if (month.equals("12")) {
                            countdec = countdec + 1;
                        }
                    }else{
                        if (month.equals("01")){
                            dcountjan=dcountjan + 1;
                        }else if (month.equals("02")){
                            dcountfeb = dcountfeb + 1;
                        }else if (month.equals("03")){
                            dcountmar = dcountmar + 1;
                        }else if (month.equals("04")){
                            dcountapril = dcountapril + 1;
                        }else if (month.equals("05")){
                            dcountmay = dcountmay + 1;
                        }else if (month.equals("06")){
                            dcountjune = dcountjune + 1;
                        }else if (month.equals("07")){
                            dcountjul = dcountjul + 1;
                        }else if (month.equals("08")){
                            dcountaug = dcountaug + 1;
                        }else if (month.equals("09")){
                            dcountsep = dcountsep + 1;
                        }else if (month.equals("10")){
                            dcountoct = dcountoct + 1;
                        }else if (month.equals("11")){
                            dcountnov = dcountnov + 1;
                        }else if (month.equals("12")) {
                            dcountdec = dcountdec + 1;
                        }
                    }
                }
                Integer[] li = {countjan,countfeb,countmar,countapril,countmay,countjune,countjul,
                        countaug,countsep,countoct,countnov,countdec};
                Integer[] disli = {dcountjan,dcountfeb,dcountmar,dcountapril,dcountmay,dcountjune,dcountjul,
                        dcountaug,dcountsep,dcountoct,dcountnov,dcountdec};
                Log.e("Whats getting passed", Arrays.toString(li));
                Set set = Set.instantiate();
                set.data(getData(li,disli));
                Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
                Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

                Bar series1 = cartesian.bar(series1Data);
                series1.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value", "value2"}) {
                    @Override
                    public void onClick(Event event) {
                    String month = String.valueOf(event.getData().get("x"));
                    Integer likes = Integer.parseInt(event.getData().get("value"));
                    Integer dislikes = Integer.parseInt(event.getData().get("value2"));

                    Bundle bundle = new Bundle();
                    bundle.putString("month", month);
                    bundle.putInt("likes", likes);
                    bundle.putInt("dislikes", dislikes);
                    Intent intent = new Intent(PerformanceReviewReport.this, PerformancePieReport.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("series 1",month);
                        Log.e("series 1",likes.toString());
                        Log.e("series 1",dislikes.toString());

                    }
                });

                series1.name("Likes")
                        .color("#33cc5a");
                series1.labels(true);
                series1.labels().position("center");
                series1.labels().fontColor("#ffffff");
                series1.labels().anchor("center");
//                series1.tooltip()
//                        .position("right")
//                        .anchor(Anchor.LEFT_CENTER);

                Bar series2 = cartesian.bar(series2Data);
                series2.name("Disikes")
                        .color("#e6191e");
                series2.labels(true);

                series2.labels().position("center");
                series2.labels().fontColor("#ffffff");
               // series2.labels().anchor("center");
//                series2.tooltip()
//                        .position("right")
//                        .anchor(Anchor.LEFT_CENTER);



            }@Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 20d, 0d);

        barChart.setChart(cartesian);
    }



    private ArrayList getData(Integer[] reviewslike, Integer[] reviewsdislike){
        ArrayList<DataEntry> entries = new ArrayList<>();
        //ArrayList<Number> likes = new ArrayList<>(),dislikes = new ArrayList<>();
        String[] months = {"January", "February", "March", "April", "May","June","July","August","September",
        "October","November","December"};

        for (int i = 0; i <12 ; i++) {
            entries.add(new CustomDataEntry(months[i],reviewslike[i],reviewsdislike[i]));
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

            String filename = "Admin Report for Weekly Sales "+ date.getTime()+".jpg";
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
                mRequestPermissionHandler.requestPermission(PerformanceReviewReport.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(pie_weekly_expenditure.this, "request permission success", Toast.LENGTH_SHORT).show();
                        createImage();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(PerformanceReviewReport.this, "request permission failed", Toast.LENGTH_SHORT).show();
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
