package com.example.shibushi.Outfits;

import java.util.ArrayList;

import com.example.shibushi.Models.cOutfits;

public class OutfitChildModel {
    // ArrayList<cOutfits>
    public static class ChildDataSource {
        ArrayList<cOutfits> data = new ArrayList<cOutfits>();
        String category = null;
        public ChildDataSource() {
        }
        public ChildDataSource(ArrayList<cOutfits> data, String category) {
            this.data = data;
            this.category = category;
        }
        public int count() { return this.data.size(); }
        public cOutfits get(int i) { return this.data.get(i); }
    }
}
