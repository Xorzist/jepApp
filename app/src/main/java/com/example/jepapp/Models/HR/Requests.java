package com.example.jepapp.Models.HR;


public class Requests {
    private  String key;
    private String UserID;
    private String Username;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    private String balance;





    public Requests(String userID, String username, String key, String request_amount) {
        this.UserID = userID;
        this.Username = username;
        this.key = key;
        this.balance = request_amount;
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
//    public String getRequest_amount() {
//        return request_amount;
//    }
    public void setUsername(String username) {
        Username = username;
    }

    public String getKey() {
        return key;
    }


//    public void setRequest_amount(String request_amount) {
//        this.request_amount = request_amount;
//    }

    public Requests() {
    }
}
