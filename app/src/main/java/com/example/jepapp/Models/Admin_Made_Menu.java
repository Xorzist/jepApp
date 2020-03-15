package com.example.jepapp.Models;


public class Admin_Made_Menu extends MItems {


    public String quantity;
    private boolean isChecked;
    private String key;
    private String type;
    public Admin_Made_Menu(String quantity, String ingredients, String id, String title, Float price, String image, String key, String type) {
        //Composite Class  object creation
        super(id, title, ingredients, price, image);
        this.quantity = quantity;
        this.key = key;
        this.type = type;
    }

    public Admin_Made_Menu() {
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public void setIngredients(String ingredients) {
        super.setIngredients(ingredients);
    }

    @Override
    public void setPrice(Float price) {
        super.setPrice(price);
    }

    @Override
    public void setImage(String image) {
        super.setImage(image);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public Float getPrice() {
        return super.getPrice();
    }

    @Override
    public String getImage() {
        return super.getImage();
    }

    @Override
    public String getIngredients() {
        return super.getIngredients();
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