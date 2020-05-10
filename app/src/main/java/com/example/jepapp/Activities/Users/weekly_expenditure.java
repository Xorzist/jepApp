package com.example.jepapp.Activities.Users;

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
import android.widget.ScrollView;
import android.widget.TextView;
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
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.example.jepapp.RequestPermissionHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
import java.util.Set;
import java.util.logging.LogRecord;

import static com.example.jepapp.Activities.Users.pie_weekly_expenditure.addImageToGallery;

public class weekly_expenditure extends AppCompatActivity {

    ArrayList<String> daterange;
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
    private RequestPermissionHandler mRequestPermissionHandler;
    private ScrollView mscrollView;
    Date startdate,enddate;
    private ProgressDialog SetupChartdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_expenditure);
        mRequestPermissionHandler = new RequestPermissionHandler();
        mscrollView = findViewById(R.id.weekly_expenditure_scrollview);
        start = getIntent().getExtras().getString("startdate");
        end = getIntent().getExtras().getString("enddate");
        daterange =new ArrayList<>();
        SetupChartdialog = new ProgressDialog(weekly_expenditure.this,R.style.Theme_AppCompat_Light_Dialog);
        SetupChartdialog.setMessage("Plotting requested Data...");
        SetupChartdialog.show();

        //Attempt to assign string dates to Date data types
        try {
            startdate =new SimpleDateFormat("dd-MM-yyyy").parse(start);
            enddate =new SimpleDateFormat("dd-MM-yyyy").parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        breakfastvalue = findViewById(R.id.customer_reportbreakfastvalue);
        lunchvalue = findViewById(R.id.customer_reportlunchvalue);
        breakfastotal = 0;
        lunchtotal = 0;

       nodata = findViewById(R.id.nodatacustomer);
        dateandcash = new ArrayList<>();
        onlydates = new ArrayList<>();
        cash = new ArrayList<>();
        entries = new ArrayList<>();
        cartesian = AnyChart.column();
        anyChartView = findViewById(R.id.customer_graph);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();

        //Call function to get days between start and end time
        getDaysBetweenDates(startdate,enddate);

        //Call function to retrieve username
        DoUsernamequery();
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders").orderByChild("date");
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders").orderByChild("date");

        //Call function to initiate retrieving records from the database
        Dbcall();

        SetupChartdialog.dismiss();
        SetupChartdialog.cancel();

    }

    //Function to retrieve orders from the breakfast table in the database
    private void Dbcall() {
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    //Determine if an order belongs to the current user and has already been completed as well as
                    //corresponding to the date range
                    if (breakfastitems.getUsername().equals(username) && breakfastitems.getStatus().toLowerCase().equals("completed"))
                    {
                        if (daterange.contains(breakfastitems.getDate()))
                        {
                            dateandcash.add(breakfastitems.getDate());
                            dateandcash.add(String.valueOf(breakfastitems.getCost()));
                            onlydates.add(breakfastitems.getDate());
                            breakfastotal += Integer.valueOf(String.valueOf(breakfastitems.getCost()));
                        }
                    }
                    }

                Lunchcall();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Function to retrieve orders from the lunch table in the database
    private void Lunchcall(){
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders lunchitems = snapshot.getValue(Orders.class);
                    //Determine if an order belongs to the current user and has already been completed as well as
                    //corresponding to the date range
                    if (lunchitems.getUsername().equals(username)&& lunchitems.getStatus().toLowerCase().equals("completed")) {
                        if (daterange.contains(lunchitems.getDate())) {
                            dateandcash.add(lunchitems.getDate());
                            dateandcash.add(String.valueOf(lunchitems.getCost()));
                            onlydates.add(lunchitems.getDate());
                            lunchtotal += Integer.valueOf(String.valueOf(lunchitems.getCost()));
                        }
                    }
                }
                AssignData();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //This function assigns relevant data and produces a column chart
    private void AssignData() {

        Set<String> uniquedates = new HashSet<>(onlydates);

        for (String key : uniquedates) {

            cash.add(calculatevalues(key));
        }

        List<String> datesList = new ArrayList<>(uniquedates);

        for (int i = 0; i < cash.size(); i++) {
            entries.add(new ValueDataEntry(datesList.get(i), cash.get(i)));
        }

        Column column = cartesian.column(entries);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("Spent:${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Report for the period of :  "+start+" to "+end);

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Dates");
        cartesian.yAxis(0).title("Cash Value");

        anyChartView.setChart(cartesian);
        //Determine if the pie chart has any values on its axis
        if (entries.size()==0){
            nodata.setVisibility(View.VISIBLE);
        }
        breakfastvalue.setText("$"+ breakfastotal);
        lunchvalue.setText("$"+ lunchtotal);

    }

    //Function to calculate the total amount spent for a specific date
    private int calculatevalues(String date) {
        int total=0;
        for (int i = 0; i <dateandcash.size() ; i++) {
            if( date.equals(dateandcash.get(i))) {
                total+=Integer.parseInt(dateandcash.get(i+1));
            }

        }
        return total;
    }

    //Function to retrieve the username of the current user
    public void DoUsernamequery(){
        final ProgressDialog UsernameDialog = new ProgressDialog(weekly_expenditure.this);
        UsernameDialog.setMessage("Obtaining the username");
        UsernameDialog.show();
        Query emailquery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

        emailquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);

                    //Set the username and balance of the current user
                    username = userCredentials.getUsername();




                }
                UsernameDialog.cancel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }

    //Function to create and save an image file from the graph that is produced
    private void createImage(){
        Date date = new Date();
        final ProgressDialog CreatingImageDialog = new ProgressDialog(this);
        CreatingImageDialog.setMessage("Creating Image File...");
        CreatingImageDialog.show();

        Bitmap bitmap = getBitmapFromView(mscrollView,mscrollView.getChildAt(0).getHeight(),mscrollView.getChildAt(0).getWidth());

        //Attempt to store the image in a specific folder
        try {
            File defaultFile = new File(getApplicationContext().getExternalFilesDir(null)+"/JEP_Reports");
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = "Report for "+start+" To "+end+ date.getTime()+".jpg";
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

            CreatingImageDialog.dismiss();

            Toast.makeText(this, "Check the folder JEP_Reports for the file", Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            e.printStackTrace();
            CreatingImageDialog.dismiss();
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
                mRequestPermissionHandler.requestPermission(weekly_expenditure.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(pie_weekly_expenditure.this, "request permission success", Toast.LENGTH_SHORT).show();
                        createImage();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(weekly_expenditure.this, "request permission failed", Toast.LENGTH_SHORT).show();
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Function to retrieve the days between a specific date range
    public boolean getDaysBetweenDates(Date startdate, Date enddate)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate))
        {
            Date result = calendar.getTime();
            daterange.add(new SimpleDateFormat("dd-MM-yyyy").format(result));
            calendar.add(Calendar.DATE, 1);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
