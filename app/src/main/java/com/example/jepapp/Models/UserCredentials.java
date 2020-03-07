package com.example.jepapp.Models;

public class UserCredentials {
    private  String key;
    private String UserID;
    private String Username;
    private String email;
    private Boolean isAdmin;
    private String balance;

//    public int getRequest_amount() {
//        return request_amount;
//    }
//
//    public void setRequest_amount(int request_amount) {
//        this.request_amount = request_amount;
//    }
//
//    private int request_amount;


    public UserCredentials(String userID, String username, String email,Boolean isAdmin,String key, String balance) {
        this.UserID = userID;
        this.Username = username;
        this.email = email;
        this.key = key;
        this.isAdmin = isAdmin;
        this.balance = balance;
    }
//    public UserCredentials(String userID, String username, String email,Boolean isAdmin,String key, String balance, int request_amount) {
//        this.UserID = userID;
//        this.Username = username;
//        this.email = email;
//        this.key = key;
//        this.isAdmin = isAdmin;
//        this.balance = balance;
//        this.request_amount = request_amount;
//    }
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserPass() {
        return email;
    }

    public void setUserPass(String userPass) {
        email = userPass;
    }
    public String getKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public UserCredentials() {
    }
}
