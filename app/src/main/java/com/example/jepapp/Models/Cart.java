package com.example.jepapp.Models;

public class Cart {
    private String cost;
    private String image;
    private String ordertitle;
    private String quantity;
    private String type;
    private String username;
    private String ingredients;
    private String ID;

    public Cart(String cost, String image, String ordertitle, String quantity, String type, String username) {
        this.cost = cost;
        this.image = image;
        this.ordertitle = ordertitle;
        this.quantity = quantity;
        this.type = type;
        this.username = username;
    }

    public Cart() {
    }

    public Cart(String cost, String image, String ordertitle, String quantity, String type, String username, String ingredients, String ID) {
        this.cost = cost;
        this.image = image;
        this.ordertitle = ordertitle;
        this.quantity = quantity;
        this.type = type;
        this.username = username;
        this.ingredients = ingredients;
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
