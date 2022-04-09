package com.example.shibushi.Wardrobe;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shibushi.R;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        String url_s = this.dataSource.get(position).url;

        ExecutorService executor;
        executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.myLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Container<Bitmap> cBitmap = new Container<Bitmap>();
                try {
                    URL url = new URL(url_s);
                    Bitmap bitmap = UtilsFetchBitmap.getBitmap(url);
                    cBitmap.set(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cBitmap.get() != null) {
                            holder.imageView.setImageBitmap(cBitmap.get());
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
        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.wardrobe_image);
        }
    }

    class Container<T>{
        T value;
        Container(){this.value=null;}
        void set(T x){this.value=x;}
        T get(){return this.value;}
    }
}
