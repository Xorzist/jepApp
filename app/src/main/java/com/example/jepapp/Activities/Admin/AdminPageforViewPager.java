package com.example.jepapp.Activities.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.jepapp.Fragments.Admin.Balances;
import com.example.jepapp.Fragments.Admin.Orders;
import com.example.jepapp.Fragments.Admin.Reviews;
import com.example.jepapp.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class AdminPageforViewPager extends AppCompatActivity {

    private RequestQueue mRequestq;
    private static final Object TAG = "Create Item Class";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BottomAppBar bottombar;
    private int[] tabIcons = {
            R.drawable.icons_menu2,
            R.drawable.menuitems2,
            R.drawable.reportsnew,

    };
    private FirebaseAuth mAuth;
    private NavigationView bottomNavigationView;
    private BottomSheetDialog bottomSheetDialog;
    private FloatingActionButton appbarfab;
    private Toolbar mytoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("JEPOS");
        setContentView(R.layout.admin_viewpager);
        //Custom Toolbar setup
       setupToolbar();

        mAuth=FirebaseAuth.getInstance();
         bottombar = (BottomAppBar) findViewById(R.id.bottombar);
         bottombar.replaceMenu(R.menu.bottmappbar_menu);

        appbarfab=findViewById(R.id.appbarfab);
        appbarfab.setImageResource(R.drawable.menuitems2);
        appbarfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Space Holder for default action
            }
        });
        appbarfab.hide();


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==1){
                    appbarfab.show();
                    appbarfab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent c = new Intent(getApplicationContext(), Commentor.class);
                            startActivity(c);

                        }
                    });

                }
                else{
                    appbarfab.hide();
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
    bottombar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.logout:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminPageforViewPager.this);
                    builder1.setMessage("Are you sure you wish to logout?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mAuth.signOut();
                                    Intent i = new Intent(AdminPageforViewPager.this, Login.class);
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
    bottombar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            openNavigationMenu();
        }
    });



    }

    private void setupToolbar() {
            mytoolbar = findViewById(R.id.admintoolbar);
            setSupportActionBar(mytoolbar);
            getSupportActionBar().setTitle("J.E.P.O.S");
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Orders(), "Orders");
        //adapter.addFrag(new Make_Menu(), "Menu");
        //adapter.addFrag(new Allitems(), "Items");
        //adapter.addFrag(new CreateItem(), "Item");
        adapter.addFrag(new Reviews(), "Comments");
        adapter.addFrag(new Balances(),"Balances");

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


private void openNavigationMenu() {
    final View bootomNavigation = getLayoutInflater().inflate(R.layout.appbar_bottomsheet,null);
    bottomSheetDialog = new BottomSheetDialog(AdminPageforViewPager.this);
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
                    Log.e("Pressed?", "Pressed: Yes!" );
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
