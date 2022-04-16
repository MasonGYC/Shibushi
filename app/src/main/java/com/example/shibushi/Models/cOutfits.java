package com.example.shibushi.Models;

import java.io.Serializable;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.UUID;

public class cOutfits implements Serializable, Comparable<cOutfits> {

    public String outfitID, userID, name, category;
    public ArrayList<cClothing> items;
    public Timestamp timeStamp;
    public ArrayList<String> img_names;

    // empty constructor for recycler view
    public cOutfits(){}

    // constructor for new outfits
    public cOutfits(String userID, String name, String category, ArrayList<cClothing> items) {
        this.outfitID = UUID.randomUUID().toString();
        this.setTimeStamp(new Timestamp(Timestamp.now().toDate()));
        this.userID = userID;
        this.name = name;
        this.items = items;
        this.category = category;
        this.img_names = new ArrayList<>();
        for (cClothing i: items){
            this.img_names.add(i.getImg_name());
        }
    }

    // constructor for pull from firestore
    public cOutfits(String outfitID, Timestamp timeStamp, String userID, String name, String category , ArrayList<String> img_names) {
        this.outfitID = outfitID;
        this.timeStamp = timeStamp;
        this.userID = userID;
        this.name = name;
        this.img_names = img_names;
        this.category = category;
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

    /**
     * Sorts outfits according to timestamp in descending order
     * @param cOutfits
     * @return
     */
    @Override
    public int compareTo(cOutfits cOutfits) {
        return cOutfits.getTimeStamp().compareTo(this.getTimeStamp());
    }
}
