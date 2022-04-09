package com.example.shibushi.cart;

import android.content.Context;
import android.graphics.ColorSpace;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.R;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

    Context context;
    LayoutInflater inflater;
    Model.DataSource datasource;

    public ContactAdapter(Context context, Model.DataSource datasource) {
        this.context = context;
        this.datasource = datasource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = inflater.inflate(R.layout.outfir_cart_image, parent, false);
        return new ContactViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ;
    }

    @Override
    public int getItemCount() {
        return this.datasource.count();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewName;

        ContactViewHolder(View view){
            super(view);
            imageViewName = view.findViewById(R.id.outfit_cart_image);

        }
    }
}

