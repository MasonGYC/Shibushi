package com.example.shibushi.Wardrobe.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;
import com.example.shibushi.Wardrobe.ViewWardrobeActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WardrobeAdapter extends RecyclerView.Adapter<WardrobeAdapter.imageViewHolder> {
    Context context;
    LayoutInflater inflater;
    Model.DataSource dataSource;
    int width;
    public static ArrayList<cClothing> selectedItems = new ArrayList<>();

    public WardrobeAdapter(Context context, Model.DataSource dataSource, int width){
        this.width = width;
        this.context = context;
        this.dataSource = dataSource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        selectedItems.clear();
        View imgView = inflater.inflate(R.layout.card_wd_image_layout, parent, false);
        return new imageViewHolder(imgView);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String url_s = this.dataSource.get(position).url;
        String image_name = this.dataSource.data_name.get(position);

        ExecutorService executor;
        executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.myLooper());
        executor.execute(() -> {
            final Container<Bitmap> cBitmap = new Container<>();
            final Container<String> cUri = new Container<>();
            try {
                URL url = new URL(url_s);
                Bitmap bitmap = UtilsFetchBitmap.getBitmap(url);
                cBitmap.set(bitmap);
                cUri.set(url_s);
            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                if (cBitmap.get() != null) {
                    // Load images and resize them into squares
                    Picasso.get().load(cUri.get()).resize(width, width).centerCrop().into(holder.imageView);
                    holder.imageView.setMaxWidth(width);
                    cClothing new_cloth = new cClothing(image_name);
                    new_cloth.setUrl(cUri.get());
                    holder.imageView.setTag(R.id.imageView_tag_uri, new_cloth);
                    executor.shutdown();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return this.dataSource.count();
    }


    public static class imageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.wardrobe_image);
            imageView.setLongClickable(true);
            // Set LongClickListener for images
            imageView.setOnLongClickListener(view -> {
                if (selectedItems.contains((cClothing) view.getTag(R.id.imageView_tag_uri))){
                    return false;
                }
                else {
                    ViewWardrobeActivity.isChoosing = true;
                    cClothing clothes = (cClothing) view.getTag(R.id.imageView_tag_uri);
                    Log.i("selected", String.valueOf(clothes));
                    selectedItems.add(clothes);
                    Toast.makeText(imageView.getContext(), "Selected", Toast.LENGTH_SHORT).show();

                    Drawable selected_clothes = imageView.getDrawable();
                    selected_clothes.setColorFilter(Color.argb(180, 240, 240, 240), PorterDuff.Mode.MULTIPLY);
                    return true;
                }
            });

            // Set onClickListener for images
            imageView.setOnClickListener(view -> {
                if (!selectedItems.contains((cClothing) view.getTag(R.id.imageView_tag_uri))){
                    return;
                }
                else{
                    cClothing clothes = (cClothing) view.getTag(R.id.imageView_tag_uri);
                    selectedItems.remove(clothes);
                    Drawable selected_clothes = imageView.getDrawable();
                    selected_clothes.setColorFilter(null);
                    Toast.makeText(imageView.getContext(), "Unselected", Toast.LENGTH_SHORT).show();
                    Log.i("unselected", String.valueOf(clothes));
                }
                if (selectedItems.size()==0){
                    ViewWardrobeActivity.isChoosing = false;
                }
            });
        }
    }

    public static class Container<T>{
        T value;
        public Container(){this.value=null;}
        public void set(T x){this.value=x;}
        public T get(){return this.value;}
    }
}
