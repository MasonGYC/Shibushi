package com.example.shibushi.Models;

public class UserAccountSettings {

    private String username;
    private String profile_photo;
    private String bio;
    private long followers;
    private long following;
    private long outfits;

    public UserAccountSettings() {
    }

    public UserAccountSettings(String username, String profile_photo, String bio, long followers, long following, long outfits) {
        this.bio = bio;
        this.followers = followers;
        this.following = following;
        this.outfits = outfits;
        this.profile_photo = profile_photo;
        this.username = username;
    }

    // Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public long getOutfits() {
        return outfits;
    }

    public void setOutfits(long outfits) {
        this.outfits = outfits;
    }

    @Override
    public String toString() {
        return "UserAccountSettings{" +
                "username='" + username + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                ", bio='" + bio + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", outfits=" + outfits +
                '}';
    }
}
