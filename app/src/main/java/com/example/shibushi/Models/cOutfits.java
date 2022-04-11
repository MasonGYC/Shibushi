package com.example.shibushi.Models;

import java.io.Serializable;
import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.Date;

public class cOutfits implements Serializable {

    String outfitID, userID, name, category;
    ArrayList<cClothing> items;
    Timestamp timeStamp;
    ArrayList<String> img_names;
    // empty constructor for recycler view
    public cOutfits(){}

    // constructor for new outfits
    public cOutfits(String userID, String name, String category, ArrayList<cClothing> items) {
        this.outfitID = UUID.randomUUID().toString();
        this.timeStamp = timeStamp;
        this.userID = userID;
        this.name = name;
        this.items = items;
        this.category = category;

    }

    // constructor for pull from firestore
    public cOutfits(String outfitID, Timestamp timeStamp, String userID, String name, ArrayList<String> img_names) {
        this.outfitID = outfitID;
        this.timeStamp = timeStamp;
        this.userID = userID;
        this.name = name;
        this.img_names = img_names;
    }

    // constructor for new outfits
    public cOutfits(String userID, String name, ArrayList<String> img_names) {
        this.outfitID = UUID.randomUUID().toString();
        this.timeStamp = Timestamp.now();
        this.userID = userID;
        this.name = name;
        this.img_names = img_names;

    }

    public String getOutfitID() {
        return outfitID;
    }

    public void setOutfitID(String outfitID) {
        this.outfitID = outfitID;
    }

    public Timestamp getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timeStamp = timestamp;
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
