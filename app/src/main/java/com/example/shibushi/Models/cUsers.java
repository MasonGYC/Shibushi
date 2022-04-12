package com.example.shibushi.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class cUsers implements Serializable {

    String username, bio, profile_photo, userID;
    ArrayList<String> followers;
    ArrayList<String> following;

    public cUsers() {}

    public cUsers(String username, ArrayList<String> followers, String profile_photo, ArrayList<String> following, String userID) {
        this.username = username;
        this.followers = followers;
        this.profile_photo = profile_photo;
        this.following = following;
        this.userID = userID;
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

    public String getUserID() {
        return userID;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    @NonNull
    @Override
    public String toString() {
        String output = "userID: " + this.getUserID()
                + "username: " + this.getUsername()
                + "bio: " + this.getBio()
                + "profile_photo: " + this.getProfile_photo()
                + "getFollowersCount: " + getFollowers().size()
                + "getFollowingCount: " + getFollowing().size();
        return output;
    }
}
