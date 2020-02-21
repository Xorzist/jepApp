package com.example.jepapp.Models;


public class Admin_Made_Menu {


    public String quantity;
    private boolean isChecked;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public Admin_Made_Menu(String quantity, String ingredients, String id, String title, Float price, String image, String key, String type) {
        this.quantity = quantity;
        this.ingredients = ingredients;
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.key = key;
        this.type = type;
    }

    private String ingredients;
    private String id;
    private String title;
    private Float price;
    private String image;


    public Admin_Made_Menu() {
    }



    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //
//    public Admin_Made_Menu(int id, String title) {
//        this.id = id;
//        this.title = title;
//    }
    public Admin_Made_Menu(String id, String title, Float price, String ingredients, String image) {
        this.image = image;
        this.ingredients = ingredients;
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
    public String getId() {
        return id;
    }
    public Float getPrice() {
        return price;
    }
    public void setId(String id) {
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

    public String getQuantity() {
        return quantity;
    }


    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}