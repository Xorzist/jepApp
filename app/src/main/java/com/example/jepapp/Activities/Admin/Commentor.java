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

public class Commentor extends AppCompatActivity {

    private SubmitButton sBtnProgress;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private TextInputEditText commentsTitle;
    private EditText comments_Description;
    private MyTask task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_commentor);
        mAuth = FirebaseAuth.getInstance();
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        commentsTitle= findViewById(R.id.comments_title);
        comments_Description = findViewById(R.id.comment_description);
        sBtnProgress = (SubmitButton) findViewById(R.id.sbtn_progress);
        sBtnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new MyTask();
                task.execute();



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
    private void ItemCreator(String comment, String name) {
        Comments comments;
        String key =getDb().child("Comments").push().getKey();
        comments = new Comments(key,comment,name,mAuth.getUid());
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
