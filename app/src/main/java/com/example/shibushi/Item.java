package com.example.shibushi;

import java.util.Date;

public abstract class Item {

   private int id;
   public String category; //(outerwear, tops, bottoms)
   public String colour;
   public String size;
   public String material;
   public String brand;
   public String occasion;
   public Date added_date;
   public int last_worn_days; //(x days ago)
   public int frequency;   //how many times have you worn it since added




}
