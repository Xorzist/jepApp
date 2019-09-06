package com.example.jepapp.Models;


public class Admin_Made_Menu {
   // private int id;
    private String title;

//
//    public Admin_Made_Menu(int id, String title) {
//        this.id = id;
//        this.title = title;
//    }
    public Admin_Made_Menu(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

}