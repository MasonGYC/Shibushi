package com.example.shibushi.Models;

import android.util.Log;

import com.example.shibushi.Utils.FirestoreMethods;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cWardrobe {
    private HashMap<String, cClothing> wardrobeClothing = new HashMap();
    private HashMap<String, cOutfits> wardrobeOutfit = new HashMap();
    private int clothing_count = 0;
    private int outfit_count = 0;

    public cWardrobe(){

    }

    public void updateWardrobe(){

    }
    public void addClothing(Map clothing_map, String img_name){
        cClothing c = new cClothing(clothing_map, img_name);
        wardrobeClothing.put(img_name, c);
    }
    // outfit-name get from userinput, img_names list of clothong to add in.
    public void addOutfit(String userid, String outfit_name, String category, ArrayList<cClothing> img_names){
        // add string "/cOutfit/" + img_names, for each
        cOutfits o = new cOutfits(userid, outfit_name, category, img_names);
        wardrobeOutfit.put(outfit_name, o);
        FirestoreMethods.addOutfit(o,outfit_name);
    }



    // get clothing documents from firestore, create clothing object for each, and add clothing object into wardrobe array
    public void updateClothing(){
        ArrayList<Map> clothing_map;
        clothing_map = FirestoreMethods.getAllClothes();
        //For each clothing document
        for (Map<String,Object> clothing : clothing_map){
            String img_name = clothing.get("img_name").toString();
            // If it clothing object does not exist
            if (!wardrobeClothing.containsKey(img_name)){
                cClothing c1 = new cClothing(clothing, img_name);
                wardrobeClothing.put(img_name,c1);
            }
        }
        clothing_count = wardrobeClothing.size();
    }
    // get Outfits documents from firestore, create clothing object for each, and add outfit object into wardrobe array
    public void updateOutfit(){
        ArrayList<Map> outfit_map;
        outfit_map = FirestoreMethods.getAllOutfits();
        //For each clothing document
        for (Map<String,Object> outfit : outfit_map){
            String outfitID = outfit.get("outfitID").toString();
            Timestamp timeStamp = (Timestamp) outfit.get("timeStamp");
            String userID = outfit.get("userID").toString();
            String outfit_name = outfit.get("name").toString();
            ArrayList<String> items =  (ArrayList<String>) outfit.get("items");
            String category = outfit.get("category").toString();
            // If it clothing object does not exist
            if (!wardrobeOutfit.containsKey(outfitID)){
                cOutfits o1 = new cOutfits(outfitID, timeStamp, userID, outfit_name, category, items);
                wardrobeOutfit.put(outfitID,o1);
            }
        }
        outfit_count = wardrobeOutfit.size();
    }
}
