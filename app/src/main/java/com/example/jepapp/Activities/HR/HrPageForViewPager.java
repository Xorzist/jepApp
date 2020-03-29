package com.example.jepapp.Activities.HR;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.jepapp.Activities.Login;
import com.example.jepapp.Fragments.HR.Page2;
import com.example.jepapp.Fragments.HR.UserLIst;
import com.example.jepapp.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HrPageForViewPager extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_notifications_black_24dp,

    };
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("JEPOS");
        setContentView(R.layout.hr_viewpager);
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("test", Context.MODE_PRIVATE);
        int test1 = userDetails.getInt("number",0);
        SharedPreferences requestDetails = getApplicationContext().getSharedPreferences("requests", Context.MODE_PRIVATE);
        int requestnum = requestDetails.getInt("request number",0);
        //String test2 = userDetails.getString("test2", "");


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    //appbarfab.show();

                } else {
                    //appbarfab.hide();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       addTabs(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setupTabIcons();
        BadgeDrawable badge = tabLayout.getTabAt(0).getOrCreateBadge();
        badge.setVisible(true);
// Optionally show a number.
        badge.setNumber(test1);

        BadgeDrawable newbadge = tabLayout.getTabAt(1).getOrCreateBadge();
        newbadge.setVisible(true);
        newbadge.setNumber(requestnum);
    }
    private void setupTabIcons(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(0).setCustomView(R.layout.notification_badge);
//
//        TextView textView = (TextView) tabLayout.getTabAt(0).getCustomView().findViewById(R.id.text);
//        textView.setText("5");
       // tabLayout.getTabAt(0).showBadge().setNumber(1)

    }

    private void addTabs(ViewPager viewPager) {
        HRViewPagerAdapter adapter = new HRViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new UserLIst(), "Users");
        adapter.addFrag(new Page2(), "Requests");


        viewPager.setAdapter(adapter);
    }

    class HRViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public HRViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(HrPageForViewPager.this);
                builder1.setMessage("Are you sure you wish to logout?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAuth.signOut();
                                Intent i = new Intent(HrPageForViewPager.this, Login.class);
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
