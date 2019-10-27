package com.example.jepapp.Models;


public class Admin_Made_Menu {


    public int quantity;
    private boolean isChecked;
    private int id;
    private String title;
    private Float price;

    public Admin_Made_Menu() {
    }

    //
//    public Admin_Made_Menu(int id, String title) {
//        this.id = id;
//        this.title = title;
//    }
    public Admin_Made_Menu(int id, String title, Float price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public int getId() {
        return id;
    }
    public Float getPrice() {
        return price;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}