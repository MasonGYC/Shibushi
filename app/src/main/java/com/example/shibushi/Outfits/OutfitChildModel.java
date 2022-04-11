package com.example.shibushi.Outfits;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class OutfitChildModel {
    public static class ChildDataSource {
        List<SingleOutfit> data = new ArrayList<SingleOutfit>();
        public ChildDataSource() {
        }
        public ChildDataSource(List<SingleOutfit> data) {
            this.data = data;
        }
        public int count() { return this.data.size(); }
        public SingleOutfit get(int i) { return this.data.get(i); }
    }

    public static class SingleOutfit {
        public Uri coverImage;
        public String name;
        public ArrayList<Uri> clothes_uri;
        public SingleOutfit(Uri coverImage, String name, ArrayList<Uri> clothes_uri) {
            this.coverImage = coverImage;
            this.name = name;
            this.clothes_uri = clothes_uri;
        }
    }
}
