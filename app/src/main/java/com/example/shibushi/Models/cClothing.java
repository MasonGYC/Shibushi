package com.example.shibushi.Models;

import java.util.HashMap;
import java.util.Map;
import android.net.Uri;

import java.io.Serializable;
import java.net.URL;



public class cClothing implements Serializable {
    String userid, img_name, category, color, occasion, size, url;
    HashMap<String, String> tags = new HashMap<>();

    public cClothing(){}

    public cClothing(Map clothing, String img_name){
        this.img_name = img_name;
        this.category = clothing.get("category").toString();
        clothing.remove("img_name");
        userid = clothing.get("userid").toString();
        clothing.remove("userid");
        tags = (HashMap<String, String>) clothing;
    }



        public cClothing(String userid, String category, String color, String occasion, String size, String img_name, String url) {
        this.userid = userid;
        this.category = category;
        this.color = color;
        this.occasion = occasion;
        this.size = size;
        this.img_name = img_name;
        this.url = url;
    }

    //for retrival
    public cClothing(String img_name){
        this.img_name = img_name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOccation() {
        return occasion;
    }

    public void setOccation(String occation) {
        this.occasion = occasion;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
