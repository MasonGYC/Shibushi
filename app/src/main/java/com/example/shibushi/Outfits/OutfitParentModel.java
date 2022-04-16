package com.example.shibushi.Outfits;

import java.util.ArrayList;
import java.util.List;

public class OutfitParentModel {
    // list of arraylist of outfits
    public static class ParentDataSource {
        List<OutfitChildModel.ChildDataSource> data = new ArrayList<>();
        String category;
        public ParentDataSource() {
        }
        public ParentDataSource(List<OutfitChildModel.ChildDataSource> data) {
            this.data = data;
            this.category = category;
        }
        public int count() { return this.data.size(); }
        public OutfitChildModel.ChildDataSource get(int i) { return this.data.get(i); }
    }

}

