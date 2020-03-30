package com.example.jepapp.Activities.HR;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.jepapp.Activities.Login;
import com.example.jepapp.Fragments.HR.Page2;
import com.example.jepapp.Fragments.HR.UserLIst;
import com.example.jepapp.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class Hrjob extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_main);
        mAuth = FirebaseAuth.getInstance();
        //loading the default fragment
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserLIst()).commit();
        }
        bottomNavigationView = findViewById(R.id.navhr);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //getting bottom navigation view and attaching the listener
        //BottomNavigationView navigation = findViewById(R.id.navigation);
        ///navigation.setOnNavigationItemSelectedListener(this);

//        BottomNav bottomNav = findViewById(R.id.navigation);
//        BadgeIndicator badgeIndicator = new BadgeIndicator(this, android.R.color.holo_red_dark, android.R.color.white);
//        //bottomNav.addItemNav(new ItemNav(this, R.drawable.ic_person_black_24dp).addColorAtive(R.color.colorAccent).addColorInative(R.color.yellow).addBadgeIndicator(badgeIndicator));
//        //bottomNav.addItemNav(new ItemNav(this, R.drawable.ic_notifications_black_24dp).addColorAtive(R.color.colorAccent));
//        bottomNav.build();
////        SharedPreferences userDetails = this.getSharedPreferences("test", Context.MODE_PRIVATE);
////        int test1 = userDetails.getInt("number",0);
////        BadgeDrawable badge = navigation.getOrCreateBadge(test1);
////        badge.setVisible(true);
//        bottomNav.setTabSelectedListener(new BottomNav.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(int i) {
//                Fragment fragment = null;
//                if (i==0) {
//                    fragment = new UserLIst();
//
//                }else{
//                    fragment = new Page2();
//
//                }
//                loadFragment(fragment);
//
//
//        }
//
//            @Override
//            public void onTabLongSelected(int i) {
//
//            }
//        });

//        int[] tabColors = getApplicationContext().getResources().getIntArray(R.array.rainbow);
//        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
//        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.navigation);
//        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
//        // Set background color
//        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
//
//// Disable the translation inside the CoordinatorLayout
//        bottomNavigation.setBehaviorTranslationEnabled(false);
//
//        bottomNavigation.setCurrentItem(0);
//
//// Customize notification (title, background, typeface)
//        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
//
//// Add or remove notification for each item
//        bottomNavigation.setNotification("1", 1);
//// OR
//        AHNotification notification = new AHNotification.Builder()
//                .setText("1")
//                .setBackgroundColor(ContextCompat.getColor(Hrjob.this, R.color.yellow))
//                .setTextColor(ContextCompat.getColor(Hrjob.this, R.color.red))
//                .build();
//        bottomNavigation.setNotification(notification, 1);
//        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//
//                loadFragment(new Page2());
//                return true;
//            }
//        });
//        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
//            @Override public void onPositionChange(int y) {
//                // Manage the new y position
//
//            }
//        });

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Fragment fragment = null;
//
//        switch (item.getItemId()) {
//            case R.id.navigation_home:
//                fragment = new UserLIst();
//                break;
//
//            case R.id.navigation_dashboard:
//                fragment = new Page2();
//                break;
//
//
//        }
//
//        return loadFragment(fragment);
//    }

//addBadgeView();
    }

//    private void addBadgeView() {
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
//        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
//
//        notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, menuView, false);
//
//        itemView.addView(notificationBadge);
//    }

//    private boolean loadFragment(Fragment fragment) {
//
//        //switching fragment
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new UserLIst();
                    break;

                case R.id.navigation_dashboard:
                    fragment = new Page2();
                    break;


            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;

        }
        };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_and_logout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Hrjob.this);
                builder1.setMessage("Are you sure you wish to logout?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAuth.signOut();
                                Intent i = new Intent(Hrjob.this, Login.class);
                                startActivity(i);
                                finish();

                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;


        }

        return false;

    }

}