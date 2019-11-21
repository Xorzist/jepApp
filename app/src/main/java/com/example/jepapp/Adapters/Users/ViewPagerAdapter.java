package com.example.jepapp.Adapters.Users;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.jepapp.Fragments.User.LunchMenu;
import com.example.jepapp.Fragments.User.MyOrders;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[]{
                new LunchMenu(), //0
                new MyOrders() //1
//                new SnackListing() //2
        };
    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length; //3 items
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = getItem(position).getClass().getName();
        return title.subSequence(title.lastIndexOf(".") + 1, title.length());
    }
}