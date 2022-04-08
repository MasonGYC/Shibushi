package com.example.shibushi.Utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cOutfits;
import com.example.shibushi.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileParentAdapter extends RecyclerView.Adapter<ProfileParentAdapter.ProfileParentViewHolder> {

    ArrayList<cOutfits> cOutfitsList;

    public void setcOutfitsList(ArrayList<cOutfits> cOutfitsList) {
        this.cOutfitsList = cOutfitsList;
    }

    @NonNull
    @Override
    public ProfileParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.snippet_profile_each_parent_outfit, null, false);
        return new ProfileParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileParentViewHolder holder, int position) {
        cOutfits cOutfits = cOutfitsList.get(position);
        // TODO: fetch data from firestore and set the views
        String userID = cOutfits.getUserID();

        holder.outfitNameTV.setText(cOutfits.getName());
        // Glide.with(holder.itemView.getContext()).load(profile_photo).into(holder.profilePhotoCIV);

        holder.clothesRecyclerView.setHasFixedSize(true);
        holder.clothesRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        ProfileChildAdapter profileChildAdapter = new ProfileChildAdapter();
        // TODO: TAKE NOTE MIGHT HAVE ISSUES
        // Array in cOutfit.items in firestore is a reference to a clothes document
        // Might still need to retrieve the image from the clothes
        profileChildAdapter.setChildItemList(cOutfits.getItems());
        holder.clothesRecyclerView.setAdapter(profileChildAdapter);
        profileChildAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cOutfitsList.size();
    }

    public class ProfileParentViewHolder extends RecyclerView.ViewHolder {

        private TextView outfitNameTV;
        private RecyclerView clothesRecyclerView;

        public ProfileParentViewHolder(@NonNull View itemView) {
            super(itemView);
            outfitNameTV = itemView.findViewById(R.id.profile_each_outfit_TV);
            clothesRecyclerView = itemView.findViewById(R.id.profile_clothing_RV);
        }
    }
}
