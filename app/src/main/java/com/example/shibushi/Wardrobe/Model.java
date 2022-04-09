package com.example.shibushi.Wardrobe;

import android.util.Log;

import com.example.shibushi.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class Model {
    public static class DataSource{
        List<Img> data = new ArrayList<>();
        public DataSource(){}
        public DataSource(List<Img> data){
            this.data = data;
        }
        public int count(){return this.data.size();}
        public Img get(int i){return this.data.get(i);}
    }

    // img class
    public static class Img{
        public String url;
        public Img(String url){
           this.url = url;
        }

    }
}
