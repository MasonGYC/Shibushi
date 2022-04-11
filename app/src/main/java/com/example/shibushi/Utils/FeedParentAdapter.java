package com.example.shibushi.Utils;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shibushi.Models.cOutfits;
import com.example.shibushi.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedParentAdapter extends RecyclerView.Adapter<FeedParentAdapter.FeedParentViewHolder> {

    ArrayList<cOutfits> cOutfitsList;

    public void setcOutfitsList(ArrayList<cOutfits> cOutfitsList) {
        this.cOutfitsList = cOutfitsList;
    }

    @NonNull
    @Override
    public FeedParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.snippet_feed_each_parent_outfit, null, false);
        return new FeedParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedParentViewHolder holder, int position) {
        cOutfits cOutfits = cOutfitsList.get(position);
        // TODO: fetch data from firestore and set the views
        String userID = cOutfits.getUserID();
        // TODO: fetch username from cUsers using userID
        String username = "UsErNaMe";
        // TODO: fetch profile photo from cUsers using userID
        String profile_photo = "https://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8cGhvdG98ZW58MHx8MHx8&w=1000&q=80";

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("images/7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717");

        holder.outfitNameTV.setText(cOutfits.getName());
        holder.usernameTV.setText(username);

        UniversalImageLoader.setImage(profile_photo, holder.profilePhotoCIV, null, "");

        // Glide.with(holder.itemView.getContext()).load(profile_photo).into(holder.profilePhotoCIV);



        holder.clothesRecyclerView.setHasFixedSize(true);
        holder.clothesRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        FeedChildAdapter feedChildAdapter = new FeedChildAdapter();
        // TODO: TAKE NOTE MIGHT HAVE ISSUES
        // Array in cOutfit.items in firestore is a reference to a clothes document
        // Might still need to retrieve the image from the clothes
        feedChildAdapter.setChildItemList(cOutfits.getImg_names());
        holder.clothesRecyclerView.setAdapter(feedChildAdapter);
        feedChildAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cOutfitsList.size();
    }

    public class FeedParentViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTV;
        private TextView outfitNameTV;
        private CircleImageView profilePhotoCIV;
        private RecyclerView clothesRecyclerView;

        public FeedParentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTV = itemView.findViewById(R.id.feed_each_outfit_username_TV);
            outfitNameTV = itemView.findViewById(R.id.profile_each_outfit_TV);
            profilePhotoCIV = itemView.findViewById(R.id.feed_each_outfit_profile_photo_CIV);
            clothesRecyclerView = itemView.findViewById(R.id.profile_clothing_RV);
        }
    }
}
