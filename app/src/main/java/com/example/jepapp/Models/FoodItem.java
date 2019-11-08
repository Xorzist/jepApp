package com.example.jepapp.Models;


public class FoodItem {
    private String id;
    private String title;
    private String ingredients;
    private Float price;
    private String image;
    private boolean isChecked;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    private String quantity;


    public FoodItem(String id, String title, String ingredients, Float price, String image, String quantity) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }

    public FoodItem() {

    }
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }
    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTitle() {
        return title;
    }

    public Float getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
    public String getIngredients() {
        return ingredients;
    }
}



//    private int id;
//    private String title;
//    private String shortdesc;
//    private double rating;
//    private double price;
//    private int image;
//
//    public FoodItem(int id, String title, String shortdesc, double rating, double price, int image) {
//        this.id = id;
//        this.title = title;
//        this.shortdesc = shortdesc;
//        this.rating = rating;
//        this.price = price;
//        this.image = image;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getShortdesc() {
//        return shortdesc;
//    }
//
//    public double getRating() {
//        return rating;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public int getImage() {
//        return image;
//    }
//}
