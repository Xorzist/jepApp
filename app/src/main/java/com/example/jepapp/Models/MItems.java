package com.example.jepapp.Models;

public class MItems {
        private String id;
        private String title;



    private String ingredients;
        private Float price;
        private String image;


        public MItems(String id, String title, String ingredients, Float price, String image) {
            this.id = id;
            this.title = title;
            this.ingredients = ingredients;
            this.price = price;
            this.image = image;
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
    }

