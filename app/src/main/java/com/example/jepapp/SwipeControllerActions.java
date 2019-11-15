package com.example.jepapp;

import android.util.Log;

import com.example.jepapp.Fragments.Admin.Allitems;
import com.example.jepapp.Models.MItems;

public class SwipeControllerActions {
    private Allitems allitems;
    public void onLeftClicked(int position) {
        Log.e("CLICK", "onLeftClicked: " );



    }

    public void onRightClicked(int position) {
       Log.e("CLICK", "onRightClicked: ");
        //allitems.adapter.deleteItem(allitems.adapter.MenuItemList.get(position));
    }

    public void SnapBack(int position) {

        //allitems.adapter.deleteItem(allitems.adapter.MenuItemList.get(position));
    }

}
