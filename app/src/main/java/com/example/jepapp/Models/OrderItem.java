package com.example.jepapp.Models;


import java.util.ArrayList;

public class OrderItem {
    private String cost;
    private String date;
    private int orderID;
    private ArrayList<String> ordertitles;
    private String paidby;
    private String payment_type;
    private int quantity;
    private String request;
    private String status;
    private String time;
    private String type;
    private String username;

    public OrderItem(String cost, String date, int orderID, ArrayList<String> ordertitles, String paidby,
                     String payment_type, int quantity, String request, String status, String time,
                     String type, String username) {
        this.cost = cost;
        this.date = date;
        this.orderID = orderID;
        this.ordertitles = ordertitles;
        this.paidby = paidby;
        this.payment_type = payment_type;
        this.quantity = quantity;
        this.request = request;
        this.status = status;
        this.time = time;
        this.type = type;
        this.username = username;
    }
}