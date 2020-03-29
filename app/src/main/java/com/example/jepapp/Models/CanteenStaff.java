package com.example.jepapp.Models;

public class CanteenStaff extends UserCredentials {

        String usertype;

        public CanteenStaff(String key, String userID, String username, String email, String empID, String contact_number, String department, String balance, String usertype) {
            //Parent Class  object creation
            super(key, userID,username, email, empID, contact_number, department, balance);
            this.usertype = usertype;
        }
    public CanteenStaff(String key, String userID, String username, String email, String empID, String contact_number, String department, String balance) {
        //Parent Class  object creation
        super(key, userID, username, email, empID, contact_number, department, balance);
        this.usertype = usertype;
    }

        public CanteenStaff() {

        }


    public String getUsertype() {
            return usertype;
        }
    }

