package com.example.shibushi.Models;

import java.util.ArrayList;

public class cOutfits {

    String outfitID, timeStamp, userID, name;
    ArrayList<cClothes> items;

    public cOutfits(){}

    public cOutfits(String outfitID, String timeStamp, String userID, String name, ArrayList<cClothes> items) {
        this.outfitID = outfitID;
        this.timeStamp = timeStamp;
        this.userID = userID;
        this.name = name;
        this.items = items;
    }

    public String getOutfitID() {
        return outfitID;
    }

    public void setOutfitID(String outfitID) {
        this.outfitID = outfitID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<cClothes> getItems() {
        return items;
    }

    public void setItems(ArrayList<cClothes> items) {
        this.items = items;
    }
}
