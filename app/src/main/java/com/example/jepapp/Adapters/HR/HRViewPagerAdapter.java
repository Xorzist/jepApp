package com.example.jepapp.Adapters.HR;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.jepapp.Fragments.HR.Page2;
import com.example.jepapp.Fragments.HR.UserLIst;
import com.example.jepapp.Fragments.User.LunchMenu;
import com.example.jepapp.Fragments.User.MyOrders;


public class HRViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;

    public HRViewPagerAdapter(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[]{
                new UserLIst(), //0
                new Page2() //1
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
