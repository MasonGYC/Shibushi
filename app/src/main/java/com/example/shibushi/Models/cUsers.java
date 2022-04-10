package com.example.shibushi.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class cUsers implements Serializable {

    String username, bio, profile_photo;
    ArrayList<String> followers;
    ArrayList<String> following;

    public cUsers() {}

    public cUsers(String username, ArrayList<String> followers, ArrayList<String> following) {
        this.username = username;
        this.followers = followers;
        this.following = following;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

}
