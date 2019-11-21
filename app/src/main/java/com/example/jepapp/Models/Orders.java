package com.example.jepapp.Models;

public class Orders {


    private String username;
    private String OrderID;
    private String ordertitle;
    private String cost;
    private String key;
    private String quantity;
    private String payment_type;

    public Orders() {
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getOrderID() {
        return OrderID;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrdertitle() {
        return ordertitle;
    }

    public void setOrdertitle(String ordertitle) {
        this.ordertitle = ordertitle;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public Orders(String orderID, String ordertitle, String quantity, String cost, String username, String key, String payment_type) {
        OrderID = orderID;
        this.ordertitle = ordertitle;
        this.quantity = quantity;
        this.cost = cost;
        this.username = username;
        this.key = key;
        this.payment_type = payment_type;
    }
}
