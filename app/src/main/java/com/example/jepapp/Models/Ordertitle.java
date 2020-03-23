package com.example.jepapp.Models;

import java.util.ArrayList;

public class Ordertitle {
    private ArrayList<String> itemname;



    public Ordertitle() {
    }

    public ArrayList<String> getItemname() {
        return itemname;
    }

    public void setItemname(ArrayList<String> itemname) {
        this.itemname = itemname;
    }

    public Ordertitle(ArrayList<String> itemname) {
        this.itemname = itemname;
    }
}
