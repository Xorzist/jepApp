package com.example.jepapp.Models;

public class UserCredentials {
    private  String key;
    private String UserID;
    private String Username;
    private String email;
    private String userPass;
    private  String contact_number;
    private String department;
    private String balance;
    private String department;
    private String contact;



    public UserCredentials(String key, String userID, String username, String email, String userPass, String contact_number, String department, String balance) {
        this.key = key;
        UserID = userID;
        Username = username;
        this.email = email;
        this.userPass = userPass;
        this.contact_number = contact_number;
        this.department = department;
        this.balance = balance;
    }

    public UserCredentials(String key, String userID, String username, String email, String balance, String department, String contact) {
        this.key = key;
        UserID = userID;
        Username = username;
        this.email = email;
        this.balance = balance;
        this.department = department;
        this.contact = contact;
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

    public void setKey(String key) {
        this.key = key;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public UserCredentials() {
    }
}
