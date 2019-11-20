package com.example.jepapp.Models;

public class Orders {
    private String OrderID;
    private String ordertitle;
    private String cost;
    private String key;

//    public Orders(String orderID, String ordertitle, String quantity, String cost) {
//        OrderID = orderID;
//        this.ordertitle = ordertitle;
//        this.quantity = quantity;
//        this.cost = cost;
  //  }
    public Orders(String key,String orderID, String ordertitle, String quantity, String cost) {
        OrderID = orderID;
        this.ordertitle = ordertitle;
        this.quantity = quantity;
        this.cost = cost;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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


}
