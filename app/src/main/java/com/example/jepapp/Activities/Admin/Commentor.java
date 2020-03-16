package com.example.jepapp.Activities.Admin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jepapp.Models.Comments;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

public class Commentor extends AppCompatActivity {

    private SubmitButton sBtnProgress;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private EditText comments_Description;
    private MyTask task;
    private SimpleDateFormat SimpleDateFormater;
    private Date datenow;
    private Spinner spinner;
    private DatabaseReference databaseReference;
    private ArrayList<MItems> list = new ArrayList<>();
    private ArrayList<String> nameslist = new ArrayList<>();
    private String reviewtitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_commentor);
        mAuth = FirebaseAuth.getInstance();

       SimpleDateFormater = new SimpleDateFormat("dd/MM/yyyy");
       datenow = new Date();



        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        comments_Description = findViewById(R.id.comment_description);
        sBtnProgress = (SubmitButton) findViewById(R.id.sbtn_progress);
        sBtnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( comments_Description.getText().toString().isEmpty()){
                    Toast.makeText(Commentor.this, "Description missing", Toast.LENGTH_SHORT).show();
                    sBtnProgress.reset();
                }
                else{
                    task = new MyTask();
                    task.execute();
                }

            }
        });
        sBtnProgress.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {
                Toast.makeText(Commentor.this, "Comment has been Posted", Toast.LENGTH_SHORT).show();

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MItems studentDetails = dataSnapshot.getValue(MItems.class);


                    list.add(studentDetails);

                }
                for (int i = 0; i<list.size();i++){
                    nameslist.add(list.get(i).getTitle());
                }
                spinner = findViewById(R.id.allitemsspinner);

                ArrayAdapter<String> adp1 = new ArrayAdapter<>(Commentor.this,
                        android.R.layout.simple_spinner_dropdown_item, nameslist);
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adp1);
                //adp1.notifyDataSetChanged();
                spinner.setPrompt("Select a Menu Item");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        parent.setSelection(position);
                        reviewtitle = parent.getItemAtPosition(position).toString();
                        spinner.setSelection(position);
                        Log.e("Stupidity",reviewtitle );
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });


    }



    private class MyTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

                if (isCancelled()) {
                    return null;
                }
                try {

                    ItemCreator(comments_Description.getText().toString(),reviewtitle);
                    //Thread.sleep(30);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == null) {
                sBtnProgress.reset();
            }
            sBtnProgress.doResult(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            sBtnProgress.setProgress(values[0]);
        }
    }
    private void ItemCreator(String comment, String title) {
        Comments comments;
        String key =getDb().child("Comments").push().getKey();
        comments = new Comments(key,title,comment,SimpleDateFormater.format(datenow),mAuth.getUid(),"None");
        getDb().child("Comments")
                .child(key)
                .setValue(comments);
        Log.d("Start Adding","START!");
    }
    public DatabaseReference getDb() {
        return myDBRef;
    }
}
