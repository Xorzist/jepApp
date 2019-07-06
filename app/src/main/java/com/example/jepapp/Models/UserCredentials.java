package com.example.jepapp.Models;

public class UserCredentials {
    private String UserID;
    private String Username;
    private String UserPass;

    public UserCredentials(String userID, String username, String userPass) {
        UserID = userID;
        Username = username;
        UserPass = userPass;
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

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }



}
