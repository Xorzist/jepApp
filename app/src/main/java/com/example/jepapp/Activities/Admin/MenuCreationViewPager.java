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

import com.android.volley.RequestQueue;
import com.example.jepapp.Activities.Login;
import com.example.jepapp.Fragments.Admin.Allitems;
import com.example.jepapp.Fragments.Admin.Make_Menu;
import com.example.jepapp.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class MenuCreationViewPager extends AppCompatActivity {

    private RequestQueue mRequestq;
    private static final Object TAG = "Create Item Class";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BottomAppBar bottombar;
    private int[] tabIcons = {
            R.drawable.icons_menu,
            R.drawable.fooditem


    };
    private FirebaseAuth mAuth;
    private NavigationView bottomNavigationView;
    private BottomSheetDialog bottomSheetDialog;
    FloatingActionButton appbarfab;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_viewpager);
        mAuth=FirebaseAuth.getInstance();
        bottombar = (BottomAppBar) findViewById(R.id.bottombar);
        bottombar.replaceMenu(R.menu.bottmappbar_menu);
        //setSupportActionBar(bottombar);
        appbarfab=findViewById(R.id.appbarfab);
        appbarfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectMenuItems2.class);
                startActivity(intent);
            }
        });



        viewPager = (ViewPager) findViewById(R.id.itemviewpager);
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
                            Intent intent = new Intent(getApplicationContext(), CreatingItem.class);
                            startActivity(intent);
                        }
                    });
                }
                else if (position==0){
                    search = findViewById(R.id.action_search);
                    search.setIconified(true);
                    search.setIconified(true);
                    appbarfab.show();
                    appbarfab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), SelectMenuItems2.class);
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addTabs(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.itemtabs);
        tabLayout.setupWithViewPager(viewPager);


        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setupTabIcons();
        bottombar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuCreationViewPager.this,R.style.datepicker);
                        builder1.setMessage("Are you sure you wish to logout?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent i = new Intent(MenuCreationViewPager.this,Login.class);
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



    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
//        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFrag(new Orders(), "Orders");
        adapter.addFrag(new Make_Menu(), "Menu");
        adapter.addFrag(new Allitems(), "Items");
        //adapter.addFrag(new CreateItem(), "Item");
        //adapter.addFrag(new Reviews(), "Comments");
        //adapter.addFrag(new Balances(),"Balances");

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
        bottomSheetDialog = new BottomSheetDialog(MenuCreationViewPager.this);
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
