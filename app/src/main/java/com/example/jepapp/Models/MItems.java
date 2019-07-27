package com.example.jepapp.Models;

public class MItems {
        private int id;
        private String title;
        private String ingredients;
        private double price;
        private String image;

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    private String itemtype;

        public MItems(int id, String title, String ingredients, Float price, String image,String itemtype) {
            this.id = id;
            this.title = title;
            this.ingredients = ingredients;
            this.price = price;
            this.image = image;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public double getPrice() {
            return price;
        }

        public String getImage() {
            return image;
        }
    }

