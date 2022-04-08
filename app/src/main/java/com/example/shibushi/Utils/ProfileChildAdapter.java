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

public class ProfileChildAdapter extends RecyclerView.Adapter<ProfileChildAdapter.ProfileChildViewHolder> {

    private ArrayList<cClothing> cClothesList;
    public void setChildItemList(ArrayList<cClothing> cClothesList){
        this.cClothesList = cClothesList;

        this.cClothesList.removeAll(Collections.singleton(null));
    }

    @NonNull
    @Override
    public ProfileChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.snippet_profile_each_child_clothing, null, false);
        return new ProfileChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileChildViewHolder holder, int position) {
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

    public class ProfileChildViewHolder extends RecyclerView.ViewHolder {

        private ImageView clothingIV;

        public ProfileChildViewHolder(@NonNull View itemView) {
            super(itemView);

            clothingIV = itemView.findViewById(R.id.profile_each_child_clothing_IV);
        }
    }
}
