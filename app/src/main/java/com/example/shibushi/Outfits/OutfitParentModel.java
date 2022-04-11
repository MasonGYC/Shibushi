package com.example.shibushi.Outfits;

import java.util.ArrayList;
import java.util.List;

public class OutfitParentModel {
    public static class ParentDataSource {
        List<OutfitChildModel.ChildDataSource> data = new ArrayList<OutfitChildModel.ChildDataSource>();
        public ParentDataSource() {
        }
        public ParentDataSource(List<OutfitChildModel.ChildDataSource> data) {
            this.data = data;
        }
        public int count() { return this.data.size(); }
        public OutfitChildModel.ChildDataSource get(int i) { return this.data.get(i); }
    }

}

