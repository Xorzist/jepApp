package com.example.jepapp.Models;

public class CanteenStaff extends UserCredentials {

        String usertype;

        public CanteenStaff(String key, String userID, String username, String email, String userPass, String contact_number, String department, String balance, String usertype) {
            //Parent Class  object creation
            super(key, userID,username, email, userPass, contact_number, department, balance);
            this.usertype = usertype;
        }
    public CanteenStaff(String key, String userID, String username, String email, String userPass, String contact_number, String department, String balance) {
        //Parent Class  object creation
        super(key, userID, username, email, userPass, contact_number, department, balance);
        this.usertype = usertype;
    }

        public CanteenStaff() {

        }


    public String getUsertype() {
            return usertype;
        }
    }

