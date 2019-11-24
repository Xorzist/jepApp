package com.example.jepapp.Models;

public class Comments {
    private String title;
    private String comment;
    private String CommentID;
    private String key;


    public Comments(String key, String title, String comment, String commentID) {
        this.title = title;
        this.comment = comment;
        CommentID = commentID;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentID() {
        return CommentID;
    }

    public void setCommentID(String commentID) {
        CommentID = commentID;
    }

    public Comments(){}

}