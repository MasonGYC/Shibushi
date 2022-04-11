package com.example.shibushi.Models;

import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class cOutfits {

    String outfitID, userID, name;
    Timestamp timeStamp;
    ArrayList<String> img_names;

    public cOutfits(){}

    public cOutfits(String outfitID, Timestamp timeStamp, String userID, String name, ArrayList<String> img_names) {
        this.outfitID = outfitID;
        this.timeStamp = timeStamp;
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
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

    public ArrayList<String> getImg_names() {
        return img_names;
    }

    public void setImg_names(ArrayList<String> img_names) {
        this.img_names = img_names;
    }
}
