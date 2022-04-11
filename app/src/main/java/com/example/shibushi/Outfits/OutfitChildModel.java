package com.example.shibushi.Outfits;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import com.example.shibushi.Models.cOutfits;
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

//    public static class cOutfits {
//        public Uri coverImage;
//        public String name;
//        public ArrayList<Uri> clothes_uri;
//        public cOutfits(Uri coverImage, String name, ArrayList<Uri> clothes_uri) {
//            this.coverImage = coverImage;
//            this.name = name;
//            this.clothes_uri = clothes_uri;
//        }
//    }
}
