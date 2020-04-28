package com.example.jepapp.Activities.HR;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.jepapp.Activities.Login;
import com.example.jepapp.Fragments.HR.HRRequests;
import com.example.jepapp.Fragments.HR.UserLIst;
import com.example.jepapp.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HrViewPager extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_person_grey_24dp,
            R.drawable.ic_notifications_black_24dp,

    };
    private BottomAppBar bottombar;
    private Toolbar mytoolbar;
    private FirebaseAuth mAuth;
    private SearchView search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_viewpager);
        //assigns title to toolbar
        setupToolbar(0);
        mAuth=FirebaseAuth.getInstance();
        //replace any existing menu with the bottombar menu
        bottombar =  findViewById(R.id.bottombar);
        bottombar.replaceMenu(R.menu.bottmappbar_menu);

        viewPager =  findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setupToolbar(0);
                    search =findViewById(R.id.action_search);
                    search.setIconified(true);
                    search.setIconified(true);

                } else {
                    setupToolbar(1);
                    search =findViewById(R.id.action_search);
                    search.setIconified(true);
                    search.setIconified(true);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       addTabs(viewPager);
        //log out functionality
        bottombar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(HrViewPager.this,R.style.datepicker);
                        builder1.setMessage("Are you sure you wish to logout?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent i = new Intent(HrViewPager.this, Login.class);
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

        });

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //assigns icons to the tab items on the viewpager
        setupTabIcons();
    }
    private void setupTabIcons(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }
    private void setupToolbar(int i) {
        if (i==0){
            mytoolbar = findViewById(R.id.admintoolbar);
            setSupportActionBar(mytoolbar);
            getSupportActionBar().setTitle("Users");
        }else if (i==1){
            mytoolbar = findViewById(R.id.admintoolbar);
            setSupportActionBar(mytoolbar);
            getSupportActionBar().setTitle("Requests");

        }

    }

    private void addTabs(ViewPager viewPager) {
        //assigns fragments to the viewpager
        HRViewPagerAdapter adapter = new HRViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new UserLIst(), getString(R.string.Users));
        adapter.addFrag(new HRRequests(), getString(R.string.Requests));


        viewPager.setAdapter(adapter);
    }

    class HRViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();



        public HRViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        //gets item position
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

}
