package com.example.jepapp.Models;

public class CanteenStaff extends UserCredentials {

        String usertype;

        public CanteenStaff(String userID, String username, String email, String key, String balance, String usertype) {
            //Parent Class  object creation
            super(userID, username, email, key, balance);
            this.usertype = usertype;
        }
    public CanteenStaff(String userID, String username, String email, String key, String balance) {
        //Parent Class  object creation
        super(userID, username, email, key, balance);
        this.usertype = usertype;
    }

        public CanteenStaff() {

        }


    public String getUsertype() {
            return usertype;
        }
    }

