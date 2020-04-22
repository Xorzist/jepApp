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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.jepapp.Models.Reviews;
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
import java.util.List;
import java.util.Objects;

import static com.example.jepapp.Activities.Users.pie_weekly_expenditure.addImageToGallery;

public class PerformancePieReport extends AppCompatActivity {
    private String month,year;
    private Integer likes=0, dislikes=0;
    List<DataEntry> entries;
    private String name = "Performance report";
    Cartesian cartesian;
    AnyChartView anyChartView;
    TextView nodata;
    private DatabaseReference reviewsreference;
    private RequestPermissionHandler mRequestPermissionHandler;
    private ScrollView mscrollView;
    private String[] monthlist;
    private String firstchar;
    private boolean userIsInteracting;
    private Spinner monthSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestPermissionHandler = new RequestPermissionHandler();
        setContentView(R.layout.activity_item_amt_report);
        Date date = new Date();
        String newdate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        //extracts the month and year from the system's date
        String[] dateParts = newdate.split("-");
        year = dateParts[2];
        //setting the title of the action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle(name+" "+ year);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRequestPermissionHandler = new RequestPermissionHandler();
        monthSpinner = findViewById(R.id.monthselect);
        monthlist = new String[]{"January","February","March","April","May",
                "June","July","August","September","October","November","December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monthlist);
        monthSpinner.setAdapter(adapter);

        nodata = findViewById(R.id.nodatacustomerpie);
        nodata.setVisibility(View.GONE);
        mscrollView = findViewById(R.id.itemamtscrollview);
        entries = new ArrayList<>();
        anyChartView =  findViewById(R.id.newpie);
        cartesian = AnyChart.column();
        //get intent data
        month = getIntent().getExtras().getString("thismonth");
        //Set spinner as current month
        monthSpinner.setSelection(Integer.parseInt(month)-1);
        reviewsreference = FirebaseDatabase.getInstance().getReference("JEP").child("Reviews");

        //Function to calculate the calendar month based on the item selected in the spinner
        monthcalculator();

        //get data from database
        getInfo(month);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSpinner.setSelection(position);
                if(userIsInteracting) {
                    //check if the user has interacted with the spinner
                    Intent i = new Intent(PerformancePieReport.this, PerformancePieReport.class);
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
        firstchar= String.valueOf(month.charAt(0));
        if((firstchar.equals('1')==false)){
            //set the month value as the selected spinner month
            month = addChar(month, '0',0);

        }
    }
    public String addChar(String str, char ch, int position) {
        //This function will add a character to the desired position in a given string
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }
    //gets reviews data from database
    private void getInfo(final String month) {
        reviewsreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsnap : dataSnapshot.getChildren()) {
                    com.example.jepapp.Models.Reviews allreviews = dsnap.getValue(Reviews.class);
                    String date = allreviews.getDate();
                    String liked = allreviews.getLiked();
                    String disliked = allreviews.getDisliked();
                    String[] dateParts = date.split("-");
                    String nummonth = dateParts[1];

                    if (nummonth.equals(month)) {
                        if (liked.toLowerCase().equals("yes")) {
                            likes += 1;
                        } else if (disliked.toLowerCase().equals("yes")) {
                            dislikes += 1;
                        }
                    }
                }
                if (likes==0&&dislikes==0){
                    nodata.setVisibility(View.VISIBLE);
                    anyChartView.setVisibility(View.GONE);
                }
            AssignData(likes,dislikes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //assign data to chart
    private void AssignData(Integer likes, Integer dislikes){
        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("likes", likes));
        data.add(new ValueDataEntry("dislikes", dislikes));
        pie.data(data);
        pie.title("Reviews for the month of "+monthlist[Integer.parseInt(month)-1]);
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Key")
                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);


    }
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    //Function to create and save an image file from the graph that is produced
    private void createImage(){
        Date date = new Date();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating PDF...");
        dialog.show();

        Bitmap bitmap = getBitmapFromView(mscrollView,mscrollView.getHeight(),mscrollView.getWidth());
        //Attempt to store the image in a specific folder
        try {
            File defaultFile = new File(getApplicationContext().getExternalFilesDir(null)+"/JEP_Reports");
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = "Admin Monthly Performance Report Pie "+ "  "+ date.getTime()+".jpg";
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
            addImageToGallery(String.valueOf(file),this);

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
                mRequestPermissionHandler.requestPermission(PerformancePieReport.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        createImage();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(PerformancePieReport.this, "request permission failed", Toast.LENGTH_SHORT).show();
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}