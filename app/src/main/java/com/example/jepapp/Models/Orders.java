package com.example.jepapp.Models;

public class Orders {
    private String OrderID;
    private String ordertitle;
    private String cost;

    public Orders() {
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

    private String quantity;

    public Orders(String orderID, String ordertitle, String quantity, String cost) {
        OrderID = orderID;
        this.ordertitle = ordertitle;
        this.quantity = quantity;
        this.cost = cost;
    }
}
