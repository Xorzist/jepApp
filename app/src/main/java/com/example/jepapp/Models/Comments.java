package com.example.jepapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Comments implements Parcelable {
    private String parentcomment;
    private String title;
    private String comment;
    private String Date;
    private String UserID;
    private String key;


    public Comments(String key, String title, String comment,String Date, String UserID,String parentcomment) {
        this.key = key;
        this.title = title;
        this.comment = comment;
        this.Date = Date;
        this.UserID = UserID;
        this.parentcomment = parentcomment;
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


    public String getParentcomment() {
        return parentcomment;
    }

    public void setParentcomment(String parentcomment) {
        this.parentcomment = parentcomment;
    }
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
    public String getUserID() {
        return UserID;
    }

    public Comments(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}