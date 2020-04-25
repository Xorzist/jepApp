package com.example.jepapp.Activities.Users;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.jepapp.Activities.Login;
import com.example.jepapp.Fragments.Admin.Reviews;
import com.example.jepapp.Fragments.User.AllMenus;
import com.example.jepapp.Fragments.User.MyOrders;
import com.example.jepapp.Fragments.User.customer_Report;
import com.example.jepapp.Fragments.User.profilepage;
import com.example.jepapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CustomerViewPager extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.icons_menu,
            R.drawable.ic_action_restaurant_menu,
            R.drawable.profileround,
            R.drawable.reportstabicon,
            R.drawable.leavereview,
    };
    private FirebaseAuth mAuth;
    private SearchView search;
    private int lastpage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_viewpager);
        mAuth=FirebaseAuth.getInstance();
        lastpage = 0;


        tabLayout =  findViewById(R.id.customertabs);
        viewPager = findViewById(R.id.viewpager);

        addTabs(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            //Determine the current page the user has selected and clear the search view
            // if the page does not contain a search view
            public void onPageSelected(int position) {
                if (position ==4) {
                    lastpage = 4;
                    Runtabcheck(true,1);

                }else if (position ==1){
                    lastpage =1;
                    Runtabcheck(true,4);
                }
                else if( lastpage==4) {
                    lastpage=0;
                    search = findViewById(R.id.action_search);
                    search.setIconified(true);
                    search.setIconified(true);
                    Runtabcheck(false,1);
                }
                else if(lastpage ==1){
                    lastpage=0;
                    search = findViewById(R.id.myorders_action_search);
                    search.setIconified(true);
                    search.setIconified(true);
                    Runtabcheck(false,4);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);



        setupTabIcons();

    }
    //Function to enable or disable a tab ,
    // false to enable adn true to disable
    private void Runtabcheck(final boolean value, int index) {
        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        tabStrip.getChildAt(index).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return value;
            }
        });
    }

    //Function to assign icons to the various tabs
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);

    }
    //Function to assign classes to tabs
    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AllMenus(),"Menu");
        adapter.addFrag(new MyOrders(), "Orders");
        adapter.addFrag(new profilepage(), "Profile");
        adapter.addFrag(new customer_Report(),"Report");
        adapter.addFrag(new  Reviews(),"Reviews");
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
        inflater.inflate(R.menu.user, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
           case R.id.logout:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerViewPager.this,R.style.datepicker);
                builder1.setMessage("Are you sure you wish to logout?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Signout();
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
            case R.id.cart:
                // Open cart page
                Intent intent = new Intent(this, Cart.class);
                startActivity(intent);
                break;
        }
        return false;
    }
    //Function to allow the user to sign-out of the system
    public void Signout() {
        mAuth.signOut();
        Intent i = new Intent(CustomerViewPager.this, Login.class);
        startActivity(i);
        finish();
    }


}
