package com.example.shibushi.Outfits;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;
import com.example.shibushi.Wardrobe.UtilsFetchBitmap;
import com.example.shibushi.Wardrobe.imageAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OutfitChildAdapter extends RecyclerView.Adapter<OutfitChildAdapter.OutfitChildViewHolder>{

    private static final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    Context context;
    LayoutInflater inflater;
    OutfitChildModel.ChildDataSource datasource;
    public static final String KEY_SINGLE_OUTFIT_VIEW_ITEMS = "KEY_SINGLE_OUTFIT_VIEW_ITEMS";
    public static final String KEY_SINGLE_OUTFIT_VIEW_NAME = "KEY_SINGLE_OUTFIT_VIEW_NAME";

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
        ArrayList<String> items =  this.datasource.get(position).getImg_names();

        String cover = this.datasource.get(position).getImg_names().get(0);
        mStorageReference.child("images").child(cover).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(
        ) {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("onBVH",cover);
                Picasso.get().load(uri).resize(600,600).centerCrop().into(holder.outfitImageButton);
            }
        });

        holder.outfitName.setText(this.datasource.get(position).getName());
        holder.outfitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SingleOutfit.class);
                // TODO: to start Single outfit page
                intent.putExtra(KEY_SINGLE_OUTFIT_VIEW_ITEMS,items);
                intent.putExtra(KEY_SINGLE_OUTFIT_VIEW_NAME,name);
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