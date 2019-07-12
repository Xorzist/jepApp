package com.example.jepapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class LunchMenu extends AppCompatActivity {
    SessionPref session;
    TextView insidertext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_menu);
        session=new SessionPref(getApplicationContext());
        String globaluid= session.GetKeyUserId();
        insidertext=findViewById(R.id.inside);
        insidertext.setText("Good day "+globaluid);

        Log.d("User ID : ", globaluid);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                session.setLogin(false);
                session.setUID("Reserved");
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

