package com.example.jepapp.Models;

public class Reviews {
    private String OrderID;
    private String Liked;
    private String  Disliked;
    private String Title;
    private String Description;
    private String Date;
    private String order_type;
    private String reviewtopic;

    public Reviews(String orderID, String liked, String disliked, String title, String description, String date, String order_type, String reviewtopic) {
        OrderID = orderID;
        Liked = liked;
        Disliked = disliked;
        Title = title;
        Description = description;
        Date = date;
        this.order_type = order_type;
        this.reviewtopic = reviewtopic;
    }

    public Reviews() {
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getLiked() {
        return Liked;
    }

    public void setLiked(String liked) {
        Liked = liked;
    }

    public String getDisliked() {
        return Disliked;
    }

    public void setDisliked(String disliked) {
        Disliked = disliked;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getReviewtopic() {
        return reviewtopic;
    }

    public void setReviewtopic(String reviewtopic) {
        this.reviewtopic = reviewtopic;
    }
}
