package com.example.shibushi.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;

import java.util.ArrayList;
import java.util.Collections;

public class FeedChildAdapter extends RecyclerView.Adapter<FeedChildAdapter.FeedChildViewHolder> {

    private ArrayList<cClothing> cClothesList;
    public void setChildItemList(ArrayList<cClothing> cClothesList){
        this.cClothesList = cClothesList;

        this.cClothesList.removeAll(Collections.singleton(null));
    }

    @NonNull
    @Override
    public FeedChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.snippet_feed_each_child_clothing, null, false);
        return new FeedChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedChildViewHolder holder, int position) {
        cClothing cClothes = cClothesList.get(position);

        String image = "https://i.pinimg.com/originals/03/d1/7b/03d17b74083eab433ea19b6be067d1c5.jpg";

        // TODO: fetch image from firestore
        // Glide.with(holder.itemView.getContext()).load(image).into(holder.clothingIV);
        holder.clothingIV.setImageResource(R.drawable.sampleclothing);
    }

    @Override
    public int getItemCount() {
        return cClothesList.size();
    }

    public class FeedChildViewHolder extends RecyclerView.ViewHolder {

        private ImageView clothingIV;

        public FeedChildViewHolder(@NonNull View itemView) {
            super(itemView);

            clothingIV = itemView.findViewById(R.id.feed_each_child_clothing_IV);
        }
    }
}
