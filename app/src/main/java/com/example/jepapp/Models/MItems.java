package com.example.jepapp.Models;

import androidx.annotation.NonNull;

public class MItems {
        private String id;
        private String title;
        private String ingredients;
        private Float price;
        private String image;
        private String key;


    public MItems(String key,String id, String title, String ingredients, Float price, String image) {
            this.id = id;
            this.title = title;
            this.ingredients = ingredients;
            this.price = price;
            this.image = image;
            this.key = key;
        }

    public MItems() {

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}

