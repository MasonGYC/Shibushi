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
        public Uri image;
        public String name;
        public SingleOutfit(Uri image,String name) {
            this.image = image;
            this.name = name;
        }
    }
}
