package com.example.jepapp.Models;

public class Admin extends CanteenStaff {
    String usertype;

    public Admin(String userID, String username, String email, String key, String balance, String usertype) {
        //Parent Class  object creation
        super(userID, username, email, key, balance);
        this.usertype=usertype;

    }

    public Admin() {

    }

    @Override
    public String getBalance() {
        return super.getBalance();
    }

    @Override
    public void setBalance(String balance) {
        super.setBalance(balance);
    }

    @Override
    public String getUserID() {
        return super.getUserID();
    }

    @Override
    public void setUserID(String userID) {
        super.setUserID(userID);
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public String getKey() {
        return super.getKey();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    public String getUsertype() {
        return usertype;
    }

}
