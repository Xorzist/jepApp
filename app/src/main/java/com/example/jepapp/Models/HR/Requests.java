package com.example.jepapp.Models.HR;


public class Requests {
    private String key;
    private String UserID;
    private String Username;
    private String amount;
    private String date;
    private String status;
    private String empID;


    public Requests(String key, String userID, String username, String amount, String date, String status, String empID) {
        this.key = key;
        UserID = userID;
        Username = username;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.empID = empID;
    }

    public Requests() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getamount() {
        return amount;
    }

    public void setamount(String amount) {
        this.amount = amount;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }
}
