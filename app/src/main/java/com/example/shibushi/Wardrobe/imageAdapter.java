package com.example.shibushi.Wardrobe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        String url = this.dataSource.get(position).url;
        Bitmap img = getURLimage(url);

        holder.imageView.setImageBitmap(img);
    }

    private Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try{
            URL targetUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
            conn.setConnectTimeout(6000);   // set timeout
            conn.setDoInput(true);
            conn.setUseCaches(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            is.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return bmp;
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
