package com.example.jepapp.Models;


public class FoodItem {
    private String id;
    private String title;
    private String ingredients;
    private Float price;
    private String image;
    private boolean checked;
    private String type;
    private String key;
    private String quantity;

    public FoodItem(String id, String title, String ingredients, Float price, String image, boolean checked, String type, String key, String quantity) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.price = price;
        this.image = image;
        this.checked = checked;
        this.type = type;
        this.key = key;
        this.quantity = quantity;
    }

    public FoodItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
