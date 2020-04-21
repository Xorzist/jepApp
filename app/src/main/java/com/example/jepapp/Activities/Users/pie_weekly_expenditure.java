package com.example.jepapp.Activities.Users;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
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
import java.util.List;

public class pie_weekly_expenditure extends AppCompatActivity {

    private String start,end;

    private ArrayList<String> cash, card;
    private Query databaseReferencebreakfast;
    private Query databaseReferencelunch;
    List<DataEntry> entries;
    AnyChartView anyChartView;
    Cartesian cartesian;
    private String username;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private int cashamount,card_amount;
    TextView cardvalue, cashvalue,nodata;

    private RequestPermissionHandler mRequestPermissionHandler;
    private ScrollView mscrollView;
    ArrayList<String> daterange;
    private Date startdate,enddate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestPermissionHandler = new RequestPermissionHandler();
        setContentView(R.layout.pie_activity_weekly_expenditure);
        start = getIntent().getExtras().getString("startdate");
        end = getIntent().getExtras().getString("enddate");
        cardvalue = findViewById(R.id.customer_reportcardvalue);
        cashvalue = findViewById(R.id.customer_reportcashvalue);
        nodata = findViewById(R.id.nodatacustomerpie);
        nodata.setVisibility(View.GONE);
        daterange =new ArrayList<>();
        //Attempt to assign string dates to Date data types
        try {
            startdate =new SimpleDateFormat("dd-MM-yyyy").parse(start);
            enddate =new SimpleDateFormat("dd-MM-yyyy").parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mscrollView = findViewById(R.id.piechartscrollview);
        cashamount=0;
        card_amount =0;
        cash = new ArrayList<>();
        card = new ArrayList<>();
        cash = new ArrayList<>();
        entries = new ArrayList<>();
        cartesian = AnyChart.column();
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();
        anyChartView = findViewById(R.id.piechartview);

        //Call function to get days between start and end time
        getDaysBetweenDates(startdate,enddate);

        //Call function to retrieve username
        DoUsernamequery();
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders").orderByChild("date");
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders").orderByChild("date");

        //Call function to initiate retrieving records from the database
        Dbcall();

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
                    if (breakfastitems.getUsername().equals(username)) {
                        if (daterange.contains(breakfastitems.getDate()) && breakfastitems.getStatus().toLowerCase().equals("completed")) {
                            if (breakfastitems.getPayment_type().toLowerCase().equals("cash")) {
                                cashamount += Integer.parseInt(String.valueOf(breakfastitems.getCost()));
                            } else {
                                card_amount += Integer.parseInt(String.valueOf(breakfastitems.getCost()));
                            }
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
                    if (lunchitems.getUsername().equals(username)) {
                        if (daterange.contains(lunchitems.getDate())&& lunchitems.getStatus().toLowerCase().equals("completed")) {
                            if (lunchitems.getPayment_type().toLowerCase().equals("cash")) {
                                cashamount += Integer.parseInt(String.valueOf(lunchitems.getCost()));
                            } else {
                                card_amount += Integer.parseInt(String.valueOf(lunchitems.getCost()));
                            }
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
    //This function assigns relevant data and produces a pie chart
    private void AssignData(){

        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Cash", cashamount));
        data.add(new ValueDataEntry("Lunch Card", card_amount));


        pie.data(data);

        pie.title("Cash VS Card Report for : "+start+" - "+end);

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Payment Methods")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
        //Determine if the pie chart has any values on its axis
        if (data.size()==0){
            nodata.setVisibility(View.VISIBLE);
        }
        cashvalue.setText("$"+cashamount);
        cardvalue.setText("$"+card_amount);

    }

    //Function to retrieve the username of the current user
    public void DoUsernamequery(){
        final ProgressDialog UsernameDialog = new ProgressDialog(pie_weekly_expenditure.this);
        UsernameDialog.setMessage("Obtaining the username");
        UsernameDialog.show();
        Query usernamequery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

        usernamequery.addValueEventListener(new ValueEventListener() {
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
        final ProgressDialog CreatingimageDialog = new ProgressDialog(this);
        CreatingimageDialog.setMessage("Creating Image file...");
        CreatingimageDialog.show();

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

            CreatingimageDialog.dismiss();

            Toast.makeText(this, "Check the folder JEP_Reports for the Image", Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            e.printStackTrace();
            CreatingimageDialog.dismiss();
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
    //Function to add the image file to the users gallery
    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
                mRequestPermissionHandler.requestPermission(pie_weekly_expenditure.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(pie_weekly_expenditure.this, "request permission success", Toast.LENGTH_SHORT).show();
                        createImage();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(pie_weekly_expenditure.this, "request permission failed", Toast.LENGTH_SHORT).show();
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
        finish();
    }
}
