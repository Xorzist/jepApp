package com.example.jepapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.jepapp.Activities.Login;

public class LunchMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_menu);
//        session=new SessionPref(getApplicationContext());
//        String globaluid= session.GetKeyUserId();
//        Log.d("User ID : ", globaluid);
    }

}

