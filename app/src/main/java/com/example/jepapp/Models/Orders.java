package com.example.jepapp.Models;

import java.util.ArrayList;

public class Orders {


    private Long cost;
    private String date;
    private String orderID;
    private ArrayList<String> ordertitle;
    private String paidby;
    private String payment_type;
    private String quantity;
    private String request;
    private String status;
    private String time;
    private String type;
    private String username;

    public Orders(Long cost, String date, String orderID, ArrayList<String> ordertitle, String paidby,
                  String payment_type, String quantity, String request, String status, String time, String type, String username) {
        this.cost = cost;
        this.date = date;
        this.orderID = orderID;
        this.ordertitle = ordertitle;
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

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public ArrayList<String> getOrdertitle() {
        return ordertitle;
    }

    public void setOrdertitle(ArrayList<String> ordertitle) {
        this.ordertitle = ordertitle;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
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

