package com.example.shibushi.Models;

import java.io.Serializable;
import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.Date;

public class cOutfits implements Serializable, Comparable<cOutfits> {

    String outfitID, userID, name, category;
    ArrayList<cClothing> items;
    Timestamp timeStamp;
    ArrayList<String> img_names;

    // empty constructor for recycler view
    public cOutfits(){}

    // constructor for new outfits (YC)
    public cOutfits(String userID, String name, String category, ArrayList<cClothing> items) {
        this.outfitID = UUID.randomUUID().toString();
        this.timeStamp = getTimeStamp();
        this.userID = userID;
        this.name = name;
        this.items = items;
        this.category = category;

    }

    // constructor for pull from firestore
    public cOutfits(String outfitID, Timestamp timeStamp, String userID, String name, String category , ArrayList<String> img_names) {
        this.outfitID = outfitID;
        this.timeStamp = timeStamp;
        this.userID = userID;
        this.name = name;
        this.img_names = img_names;
    }

    // constructor for new outfits (Samuel)
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<cClothing> getItems() {
        return items;
    }

    public void setItems(ArrayList<cClothing> items) {
        this.items = items;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<String> getImg_names() {
        return img_names;
    }

    public void setImg_names(ArrayList<String> img_names) {
        this.img_names = img_names;
    }

    @Override
    public int compareTo(cOutfits cOutfits) {
        return this.timeStamp.compareTo(cOutfits.getTimeStamp());
    }
}
