package com.example.shibushi.Models;

public class User {
    private String userID;
    private String username;
    private String email;

    public User(){}

    public User(String userID, String username, String email){
        this.userID = userID;
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=' " + userID +'\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
