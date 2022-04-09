package com.example.shibushi.cart;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class Model {
    public static class DataSource {
        List<Contact> data = new ArrayList<Contact>();
        public DataSource() {
        }
        public DataSource(List<Contact> data) {
            this.data = data;
        }
        public int count() { return this.data.size(); }
        public Contact get(int i) { return this.data.get(i); }
    }

    public static class Contact {
        public Image image;

        public Contact(Image image) {
            this.image=image;

        }
    }
}
