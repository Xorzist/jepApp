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
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.jepapp.Activities.Login;
import com.example.jepapp.Fragments.Admin.Reports;
import com.example.jepapp.Fragments.Admin.Reviews;
import com.example.jepapp.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;


public class ReportsViewPager extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BottomAppBar bottombar;
    private int[] tabIcons = {
            R.drawable.reportstabicon,
            R.drawable.reviewstabicon,
    };
    private FirebaseAuth mAuth;

    private BottomSheetDialog bottomSheetDialog;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.reports_viewpager);
        mAuth=FirebaseAuth.getInstance();
        bottombar = findViewById(R.id.bottombar);
        //replace any existing menu with the bottombar menu
        bottombar.replaceMenu(R.menu.bottmappbar_menu);
        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    search = findViewById(R.id.action_search);
                    search.setIconified(true);
                    search.setIconified(true);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //assigns icons to the tab items on the viewpager
        setupTabIcons();
        //log out functionality
        bottombar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ReportsViewPager.this,R.style.datepicker);
                        builder1.setMessage("Are you sure you wish to logout?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                R.string.dialogYes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent i = new Intent(ReportsViewPager.this, Login.class);
                                        startActivity(i);
                                        finish();
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                R.string.dialogNo,
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
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void addTabs(ViewPager viewPager) {
        //assigns fragments to the viewpager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Reports(),getString(R.string.Reports));
        adapter.addFrag(new Reviews(),getString(R.string.Reviews));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottmappbar_menu, menu);
        return true;
    }


    //launches interface based on item selected from bottom navigation menu
    private void openNavigationMenu() {
        final View bottomNavigation = getLayoutInflater().inflate(R.layout.appbar_bottomsheet,null);
        bottomSheetDialog = new BottomSheetDialog(ReportsViewPager.this);
        bottomSheetDialog.setContentView(bottomNavigation);
        bottomSheetDialog.show();

        //this will find NavigationView from id
        NavigationView navigationView = bottomNavigation.findViewById(R.id.bottom_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.orderingpage:
                        bottomSheetDialog.dismiss();
                        Intent a = new Intent(getApplicationContext(), OrdersViewPager.class);
                        startActivity(a);
                        finish();
                        break;
                    case R.id.menuitempage:
                        bottomSheetDialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), MenuCreationViewPager.class);
                        startActivity(i);
                        finish();
                        break;

                    case R.id.reportspage:
                        bottomSheetDialog.dismiss();
                        Intent r = new Intent(getApplicationContext(), ReportsViewPager.class);
                        startActivity(r);
                        finish();
                        break;


                }
                return false;
            }
        });
    }
}
