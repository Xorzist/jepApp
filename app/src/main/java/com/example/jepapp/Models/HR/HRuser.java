package com.example.jepapp.Models.HR;

import com.example.jepapp.Models.UserCredentials;

public class HRuser extends UserCredentials {
    String usertype;

//    public HRuser(String userID, String username, String email, String key, String balance, String usertype) {
//        //Parent Class  object creation
//        super(userID, username, email, key, balance);
//        this.usertype = usertype;
//    }

    public HRuser(String usertype) {
        this.usertype = usertype;
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
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    public HRuser() {
        super();
    }
}
