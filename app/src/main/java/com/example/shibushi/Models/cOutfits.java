package com.example.shibushi.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class cOutfits {

    String outfitID, timeStamp, userID, name;
    ArrayList<String> items;
    ArrayList<String> img_names;

    public cOutfits(){}

    // constructor for pull from firestore
    public cOutfits(String outfitID, String timeStamp, String userID, String name, ArrayList<String> items,ArrayList<String> img_names ) {
        this.outfitID = outfitID;
        this.timeStamp = timeStamp;
        this.userID = userID;
        this.name = name;
        this.items = items;
        this.img_names = img_names;
    }
    // constructor for new outfits
    public cOutfits(String userID, String name, ArrayList<cClothing> items) {
        Date timestamp = new Date();
        this.outfitID = UUID.randomUUID().toString();
        this.timeStamp = timeStamp.toString();
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

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }
}
