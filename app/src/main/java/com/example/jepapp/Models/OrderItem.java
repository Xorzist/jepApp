package com.example.jepapp.Models;


public class OrderItem {
    private int id;
    private String title;
    private String receiver;
    private int quantity;
    private int image;




    public OrderItem(int id, String title, String receiver, int quantity, int image) {
        this.id = id;
        this.title = title;
        this.receiver = receiver;
        this.image = image;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


}