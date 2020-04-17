package com.example.jepapp.Activities.Users;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
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
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    TextView cardvalue, cashvalue;
    private Button createpdf;
    private RequestPermissionHandler mRequestPermissionHandler;
    private ScrollView mscrollView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestPermissionHandler = new RequestPermissionHandler();
        setContentView(R.layout.pie_activity_weekly_expenditure);
        start = getIntent().getExtras().getString("startdate");
        end = getIntent().getExtras().getString("enddate");
        cardvalue = findViewById(R.id.customer_reportcardvalue);
        cashvalue = findViewById(R.id.customer_reportcashvalue);
        createpdf = findViewById(R.id.create_PDF_pie);
        mscrollView = findViewById(R.id.piechartscrollview);
        createpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createpdf.setVisibility(View.GONE);

                mRequestPermissionHandler.requestPermission(pie_weekly_expenditure.this, new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(pie_weekly_expenditure.this, "request permission success", Toast.LENGTH_SHORT).show();
                        createpdf2(v);
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(pie_weekly_expenditure.this, "request permission failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        cashamount=0;
        card_amount =0;
        cash = new ArrayList<>();
        card = new ArrayList<>();
        cash = new ArrayList<>();
        entries = new ArrayList<>();
        cartesian = AnyChart.column();
        anyChartView = findViewById(R.id.piechartview);
        Log.e("Oncreatestart",start );
        Log.e("Oncreateend",end );
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();
        DoUsernamequery();
        databaseReferencebreakfast = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastOrders").orderByChild("date").startAt(start).endAt(end);
        databaseReferencelunch = FirebaseDatabase.getInstance().getReference("JEP").child("LunchOrders").orderByChild("date").startAt(start).endAt(end);
        Dbcall();

    }



    private void Dbcall() {
        databaseReferencebreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Orders breakfastitems = snapshot.getValue(Orders.class);
                    if (breakfastitems.getUsername().equals(username))
                    {
                        if(breakfastitems.getPayment_type().toLowerCase().equals("cash")){
                            cashamount+=Integer.parseInt(String.valueOf(breakfastitems.getCost()));
                        }else{
                            card_amount+=Integer.parseInt(String.valueOf(breakfastitems.getCost()));
                        }
                    }
                }

                Log.e("Cash amount", String.valueOf(cashamount));
                Log.e("Card Amount", String.valueOf(card_amount));
                Lunchcall();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void Lunchcall(){
        databaseReferencelunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders lunchitems = snapshot.getValue(Orders.class);
                    if (lunchitems.getUsername().equals(username)){
                        if(lunchitems.getPayment_type().toLowerCase().equals("cash")){
                            cashamount+=Integer.parseInt(String.valueOf(lunchitems.getCost()));
                        }else{
                            card_amount+=Integer.parseInt(String.valueOf(lunchitems.getCost()));
                        }
                    }
                }

                Log.e("Cash amount", String.valueOf(cashamount));
                Log.e("Card Amount", String.valueOf(card_amount));
                AssignData();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void AssignData(){
        //This function assigns values to variables to produce a piechart
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
        cashvalue.setText("$"+cashamount);
        cardvalue.setText("$"+card_amount);



    }

    private int calculatevalues(String date) {
        int total=0;
        for (int i = 0; i < cash.size() ; i++) {
            if( date.equals(cash.get(i))) {
                total+=Integer.parseInt(cash.get(i+1));
            }

        }
        Log.e("Total for"+date,String.valueOf(total) );
        return total;
    }
    public void DoUsernamequery(){
        final ProgressDialog progressDialog = new ProgressDialog(pie_weekly_expenditure.this);
        progressDialog.setMessage("Obtaining the username");
        progressDialog.show();
        Query emailquery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

        emailquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);


                    //Set the username and balance of the current user
                    username = userCredentials.getUsername();
                    Log.e("The name",username );
                    //balance = userCredentials.getBalance();


                }
                progressDialog.cancel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }

    private void createPDF() {
        AbstractViewRenderer page = new AbstractViewRenderer(this, R.layout.pie_activity_weekly_expenditure) {
            private String _text;

            public void setText(String text) {
                _text = text;
            }

            @Override
            protected void initView(View view) {
                TextView tv_hello = (TextView)view.findViewById(R.id.nodatacustomerpie);
                tv_hello.setText(_text);
            }
        };

        // you can reuse the bitmap if you want
        page.setReuseBitmap(true);

        PdfDocument doc = new PdfDocument(this);

// add as many pages as you have
        doc.addPage(page);

        doc.setRenderWidth(getScreenWidth());
        doc.setRenderHeight(getScreenHeight());
        doc.setOrientation(PdfDocument.A4_MODE.LANDSCAPE);
        doc.setProgressTitle(R.string.gen_please_wait);
        doc.setProgressMessage(R.string.gen_pdf_file);
        doc.setFileName("Report for"+start+" to "+end);
        doc.setSaveDirectory(this.getExternalFilesDir(null));
        doc.setInflateOnMainThread(false);
        doc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");
            }

            @Override
            public void onError(Exception e) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
            }
        });

        doc.createPdf(this);

    }
    private void createpdf2(View v){
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

            String filename = "Report for "+start+" To "+end+ date.getTime()+".jpg";
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
            addImageToGallery(String.valueOf(file),this);

            dialog.dismiss();

            Toast.makeText(this, "Check the folder JEP_Reports for the file", Toast.LENGTH_LONG).show();
           createpdf.setVisibility(View.VISIBLE);

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
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
