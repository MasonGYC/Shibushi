package com.example.shibushi.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class cOutfits implements Serializable {

    String outfitID, timeStamp, userID, name, category;
    ArrayList<cClothing> items;
    // empty constructor for recycler view
    public cOutfits(){}

    // constructor for new outfits
    public cOutfits(String userID, String name, String category, ArrayList<cClothing> items) {
        Date timestamp = new Date();
        this.outfitID = UUID.randomUUID().toString();
        this.timeStamp = timestamp.toString();
        this.userID = userID;
        this.name = name;
        this.items = items;
        this.category = category;

    }

    // constructor for outfits from firestore
    public cOutfits(String outfitID, String timeStamp, String userID, String name, ArrayList<cClothing> items) {
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

    public ArrayList<cClothing> getItems() {
        return items;
    }

    public void setItems(ArrayList<cClothing> items) {
        this.items = items;
    }
}
