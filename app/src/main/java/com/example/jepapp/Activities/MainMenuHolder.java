package com.example.jepapp.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jepapp.Fragments.Admin.Make_Menu;

public class MainMenuHolder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
         // If not already added to the Fragment manager add it. If you don't do this a new Fragment will be added every time this method is called (Such as on orientation change)
        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, new Make_Menu()).commit();
    }
}
