package com.example.jepapp.Models;

public class UserCredentials {
    private  String key;
    private String UserID;
    private String Username;
    private String email;
    private Boolean isAdmin;

    public UserCredentials(String userID, String username, String email,Boolean isAdmin,String key) {
        this.UserID = userID;
        this.Username = username;
        this.email = email;
        this.key = key;
        this.isAdmin = isAdmin;
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
        return email;
    }

    public void setUserPass(String userPass) {
        email = userPass;
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
    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public UserCredentials() {
    }
}
