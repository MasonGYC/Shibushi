package com.example.shibushi.Wardrobe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.imageViewHolder> {
    Context context;
    LayoutInflater inflater;
    Model.DataSource dataSource;
    int width;
    public static ArrayList<cClothing> selectedItems = new ArrayList<>();

    public imageAdapter(Context context, Model.DataSource dataSource, int width){
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
        return new imageViewHolder(imgView, width);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String url_s = this.dataSource.get(position).url;

//        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (NotificationManagerCompat.from(view.getContext()).areNotificationsEnabled() ){
//                    Toast.makeText(view.getContext(),"Selected",Toast.LENGTH_LONG).show();
//                }
//                else {
//                    Log.i("noti","notification unabled");
//                }
//
//                Log.i("onBindVH",view.getContext().toString());
//                ViewWardrobeActivity.isChoosing = true;
//                if (view.getTag(R.id.imageView_tag_uri) != null){
//                    cClothing clothes =  (cClothing) view.getTag(R.id.imageView_tag_uri);
//                    Log.i("selcted", String.valueOf(clothes));
//                    selectedItems.add(clothes);
//
//                }
//                return true;
//            }
//        });

        ExecutorService executor;
        executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.myLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
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

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cBitmap.get() != null) {
                            Picasso.get().load(cUri.get()).resize(width, width).centerCrop().into(holder.imageView);
//                            Picasso.get().load(cUri.get()).into(holder.imageView);
//                            holder.imageView.setImageBitmap(cBitmap.get());
                            holder.imageView.setMaxWidth(width);
                            holder.imageView.setTag(R.id.imageView_tag_uri, new cClothing(cUri.get())); //todo: image_name string
//                            holder.imageView.setMaxHeight(width);
//                            holder.imageViewLayout.setMinimumHeight(width);
//                            holder.imageViewLayout.setMinimumWidth(width);
                            executor.shutdown();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dataSource.count();
    }


    public static class imageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public imageViewHolder(@NonNull View itemView, int width) {
            super(itemView);
            imageView = itemView.findViewById(R.id.wardrobe_image);
            imageView.setLongClickable(true);
//            imageViewLayout = itemView.findViewById(R.id.wardrobe_image);
//            imageView.setMinimumWidth(width);
//            imageView.setMaxWidth(width);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (selectedItems.contains((cClothing) view.getTag(R.id.imageView_tag_uri))){
                        return false;
                    }
                    else {
                        ViewWardrobeActivity.isChoosing = true;
                        cClothing clothes = (cClothing) view.getTag(R.id.imageView_tag_uri);
                        Log.i("selcted", String.valueOf(clothes));
                        selectedItems.add(clothes);
                        Toast.makeText(imageView.getContext(), "Selected", Toast.LENGTH_SHORT).show();

                        Drawable selected_clothes = imageView.getDrawable();
                        selected_clothes.setColorFilter(Color.argb(180, 240, 240, 240), PorterDuff.Mode.MULTIPLY);
                        return true;
                    }
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
