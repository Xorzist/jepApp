package com.example.jepapp.Activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.example.jepapp.Activities.Login;
import com.example.jepapp.Activities.Users.PageforViewPager;
import com.example.jepapp.Fragments.Admin.Allitems;
import com.example.jepapp.Fragments.Admin.Balances;
import com.example.jepapp.Fragments.Admin.Make_Menu;
import com.example.jepapp.Fragments.Admin.Orders;
import com.example.jepapp.Fragments.Admin.Reports;
import com.example.jepapp.Fragments.Admin.Reviews;
import com.example.jepapp.Models.Comments;
import com.example.jepapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class AdminPageforViewPager extends AppCompatActivity {

    private RequestQueue mRequestq;
    private static final Object TAG = "Create Item Class";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private int[] tabIcons = {
            R.drawable.menu,
            R.drawable.snack,
            R.drawable.grapes,
            R.drawable.snack,
            R.drawable.snack,

    };
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_viewpager);
        mAuth=FirebaseAuth.getInstance();

         bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setupTabIcons();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.orderspage:
                        Intent intent = new Intent(getApplicationContext(), AdminPageforViewPager.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.inventorypage:

                        break;

                    case R.id.analysispage:
                        Intent intent3 = new Intent(getApplicationContext(), Reports.class);
                        startActivity(intent3);
                        finish();
                        break;
                }


                return true;
            }
        });

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Orders(), "Orders");
        adapter.addFrag(new Make_Menu(), "Menu");
        adapter.addFrag(new Allitems(), "Items");
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
                // session.setLogin(false);
                // session.setUID("Reserved");
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
