package com.example.shibushi.Outfits;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;
import com.example.shibushi.Wardrobe.UtilsFetchBitmap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SingleOutfitAdapter extends RecyclerView.Adapter<SingleOutfitAdapter.SingleOutfitViewHolder>{

    Context context;
    LayoutInflater inflater;
    ArrayList<cClothing> datasource;


    public SingleOutfitAdapter(Context context, ArrayList<cClothing> datasource) {
        this.context = context;
        this.datasource = datasource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SingleOutfitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = inflater.inflate(R.layout.singleoutfit_relativeview, parent, false);
        return new SingleOutfitViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleOutfitViewHolder holder, int position) {
        String name = this.datasource.get(position).getImg_name();
        holder.clothingName.setText(name);
        URL url = null;
        // todo: if no url then what?
        try {
            url = new URL(this.datasource.get(position).getUrl());
            Bitmap bitmap = UtilsFetchBitmap.getBitmap(url);
            holder.outfitImageView.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return this.datasource.size();
    }

    public static class SingleOutfitViewHolder extends RecyclerView.ViewHolder{
        ImageView outfitImageView;
        TextView clothingName;
        SingleOutfitViewHolder(View view){
            super(view);
            outfitImageView = view.findViewById(R.id.singleoutfitImageview);
            clothingName= view.findViewById(R.id.clothingName);

        }
    }
}

