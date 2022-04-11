package com.example.shibushi.Models;

import android.util.Log;

import com.example.shibushi.Utils.FirestoreMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cWardrobe {
    private HashMap<String, cClothing> wardrobeClothing = new HashMap();
    private HashMap<String, cOutfits> wardrobeOutfit = new HashMap();
    private int clothing_count = 0;
    private int outfit_count = 0;

    cWardrobe(){

    }

    public void updateWardrobe(){

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
            String timeStamp = outfit.get("timeStamp").toString();
            String userID = outfit.get("userID").toString();
            String outfit_name = outfit.get("name").toString();
            ArrayList<String> items =  (ArrayList<String>) outfit.get("items");
            ArrayList<String> clothings =  (ArrayList<String>) outfit.get("img_names");
            // If it clothing object does not exist
            if (!wardrobeOutfit.containsKey(outfitID)){
                cOutfits o1 = new cOutfits(outfitID, timeStamp, timeStamp, userID, outfit_name, items, clothings);
                wardrobeOutfit.put(outfitID,o1);
            }
        }
        outfit_count = wardrobeOutfit.size();
    }

}
