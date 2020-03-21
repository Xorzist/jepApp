package com.example.jepapp.Models;

public class UserCredentials {
    private String userID;
    private String username;
    private String email;
    private String empID;
    private  String contactnumber;
    private String department;
    private String balance;
    private  String name;

    public UserCredentials(String userID, String username, String email, String empID, String contactnumber, String department, String balance, String name) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.empID = empID;
        this.contactnumber = contactnumber;
        this.department = department;
        this.balance = balance;
        this.name = name;
    }

    public UserCredentials() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
