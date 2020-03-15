package com.example.jepapp.Models;

public class UserCredentials {
    private  String key;
    private String UserID;
    private String Username;
    private String email;
    private String balance;



    public UserCredentials(String userID, String username, String email, String key, String balance) {
        this.UserID = userID;
        this.Username = username;
        this.email = email;
        this.key = key;
        this.balance = balance;
    }
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

    public String getKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public UserCredentials() {
    }
}
