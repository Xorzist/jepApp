package com.example.jepapp.Activities.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.example.jepapp.Activities.Login;
import com.example.jepapp.Fragments.Admin.Reports;
import com.example.jepapp.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class ReportsPageforViewPager extends AppCompatActivity {

    private RequestQueue mRequestq;
    private static final Object TAG = "Create Item Class";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BottomAppBar bottombar;
    private int[] tabIcons = {
            R.drawable.reportstabicon,


    };
    private FirebaseAuth mAuth;
    private NavigationView bottomNavigationView;
    private BottomSheetDialog bottomSheetDialog;
    private FloatingActionButton appbarfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.reports_viewpager);
        mAuth=FirebaseAuth.getInstance();
        bottombar = (BottomAppBar) findViewById(R.id.bottombar);
        bottombar.replaceMenu(R.menu.bottmappbar_menu);
        //setSupportActionBar(bottombar);



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setupTabIcons();
        bottombar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ReportsPageforViewPager.this);
                        builder1.setMessage("Are you sure you wish to logout?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent i = new Intent(ReportsPageforViewPager.this, Login.class);
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

                }
                return false;
            }

        });
        bottombar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNavigationMenu();
            }
        });



    }



    private void setupTabIcons() {
        //tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        //tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
       // tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFrag(new Orders(), "Orders");
        //adapter.addFrag(new Make_Menu(), "Menu");
        //adapter.addFrag(new Allitems(), "Items");
        //adapter.addFrag(new CreateItem(), "Item");
        //adapter.addFrag(new Reviews(), "Comments");
        adapter.addFrag(new Reports(),"Reports");
        //adapter.addFrag(new Reports(),"Reports");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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
        inflater.inflate(R.menu.bottmappbar_menu, menu);
        return true;
    }



    private void openNavigationMenu() {
        final View bootomNavigation = getLayoutInflater().inflate(R.layout.appbar_bottomsheet,null);
        bottomSheetDialog = new BottomSheetDialog(ReportsPageforViewPager.this);
        bottomSheetDialog.setContentView(bootomNavigation);
        bottomSheetDialog.show();

        //this will find NavigationView from id
        NavigationView navigationView = bootomNavigation.findViewById(R.id.bottom_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.orderingpage:
                        bottomSheetDialog.dismiss();
                        Intent a = new Intent(getApplicationContext(), AdminPageforViewPager.class);
                        startActivity(a);
                        finish();
                        break;
                    case R.id.menuitempage:
                        bottomSheetDialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), ItemsPageforViewPager.class);
                        startActivity(i);
                        finish();
                        break;

                    case R.id.reportspage:
                        bottomSheetDialog.dismiss();
                        Intent r = new Intent(getApplicationContext(), ReportsPageforViewPager.class);
                        startActivity(r);
                        finish();
                        break;


                }
                return false;
            }
        });
    }
}
