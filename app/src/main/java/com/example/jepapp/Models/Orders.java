package com.example.jepapp.Models;

public class Orders {
    private String OrderID;
    private String ordertitle;

    public String getOrderID() {
        return OrderID;
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

    public Orders(String orderID, String ordertitle, String quantity) {
        OrderID = orderID;
        this.ordertitle = ordertitle;
        this.quantity = quantity;
    }
}
