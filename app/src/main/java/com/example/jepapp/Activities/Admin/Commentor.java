package com.example.jepapp.Activities.Admin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jepapp.Models.Comments;
import com.example.jepapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Commentor extends AppCompatActivity {

    private SubmitButton sBtnProgress;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private TextInputEditText commentsTitle;
    private EditText comments_Description;
    private MyTask task;
    private SimpleDateFormat SimpleDateFormater;
    private Date datenow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_commentor);
        mAuth = FirebaseAuth.getInstance();
       SimpleDateFormater = new SimpleDateFormat("dd/MM/yyyy");
       datenow = new Date();

        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        commentsTitle= findViewById(R.id.comments_title);
        comments_Description = findViewById(R.id.comment_description);
        sBtnProgress = (SubmitButton) findViewById(R.id.sbtn_progress);
        sBtnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentsTitle.getText().toString().isEmpty() ||  comments_Description.getText().toString().isEmpty()){
                    Toast.makeText(Commentor.this, "Title or Description missing", Toast.LENGTH_SHORT).show();
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


    }
    private class MyTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

                if (isCancelled()) {
                    return null;
                }
                try {

                    ItemCreator(comments_Description.getText().toString(),commentsTitle.getText().toString());
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
        //sBtnProgress.doResult(true);
    }
    public DatabaseReference getDb() {
        return myDBRef;
    }
}
