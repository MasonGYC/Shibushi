package com.example.shibushi.Wardrobe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.R;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.imageViewHolder> {
    Context context;
    LayoutInflater inflater;
    Model.DataSource dataSource;

    public imageAdapter(Context context, Model.DataSource dataSource){
        this.context = context;
        this.dataSource = dataSource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View imgView = inflater.inflate(R.layout.card_wd_image_layout, parent, false);
        return new imageViewHolder(imgView);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {
//        holder.itemView.setBackground(this.dataSource.get(position).url);
        holder.imageView.setImageDrawable(Drawable.createFromPath("drawable/for_test_only.xml"));
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
        }
    }
}
