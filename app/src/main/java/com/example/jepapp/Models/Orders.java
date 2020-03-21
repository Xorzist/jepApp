package com.example.jepapp.Models;

import java.util.ArrayList;

public class Orders {


    private String cost;
    private String date;
    private int orderID;
    private ArrayList<Ordertitle> ordertitles;
    private String paidby;
    private String payment_type;
    private int quantity;
    private String request;
    private String status;
    private String time;
    private String type;
    private String username;

    public Orders(String cost, String date, int orderID, ArrayList<Ordertitle> ordertitles, String paidby,
                  String payment_type, int quantity, String request, String status, String time, String type, String username) {
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

    public Orders() {
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public ArrayList<Ordertitle> getOrdertitles() {
        return ordertitles;
    }

    public void setOrdertitles(ArrayList<Ordertitle> ordertitles) {
        this.ordertitles = ordertitles;
    }

    public String getPaidby() {
        return paidby;
    }

    public void setPaidby(String paidby) {
        this.paidby = paidby;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

