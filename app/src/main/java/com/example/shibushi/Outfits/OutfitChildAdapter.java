package com.example.shibushi.Outfits;

import static com.example.shibushi.Utils.FirestoreMethods.getDownloadUrlString;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;

public class OutfitChildAdapter extends RecyclerView.Adapter<OutfitChildAdapter.OutfitChildViewHolder>{

    Context context;
    LayoutInflater inflater;
    OutfitChildModel.ChildDataSource datasource;
    public static final String KEY_SINGLE_OUTFIT_VIEW = "KEY_SINGLE_OUTFIT_VIE";

    public OutfitChildAdapter(Context context, OutfitChildModel.ChildDataSource datasource) {
        this.context = context;
        this.datasource = datasource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OutfitChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = inflater.inflate(R.layout.viewoutfits_child_cardview, parent, false);
        return new OutfitChildViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitChildViewHolder holder, int position) {
        //todo: scale image to 150*150
        String name = this.datasource.get(position).getName();
        ArrayList<cClothing> items =  this.datasource.get(position).getItems();
        //get imageuri
        String cover = this.datasource.get(position).getItems().get(0).getImg_name();
        Uri imageuri = Uri.parse(getDownloadUrlString(cover));
        holder.outfitImageButton.setImageURI(imageuri);
        holder.outfitName.setText(this.datasource.get(position).getName());
        holder.outfitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"outfitImageButton",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), SingleOutfit.class);
                // TODO: to start Single outfit page
                intent.putExtra(KEY_SINGLE_OUTFIT_VIEW,items);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.datasource.count();
    }

    public static class OutfitChildViewHolder extends RecyclerView.ViewHolder{
        ImageButton outfitImageButton;
        TextView outfitName;
        OutfitChildViewHolder(View view){
            super(view);
            outfitImageButton = view.findViewById(R.id.outfitImageButton);
            outfitName= view.findViewById(R.id.outfitName);

        }
    }
}
