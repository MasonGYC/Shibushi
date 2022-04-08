package com.example.shibushi.Utils;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shibushi.Models.cOutfits;
import com.example.shibushi.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedParentAdapter extends RecyclerView.Adapter<FeedParentAdapter.FeedParentViewHolder> {

    Context context;
    ArrayList<cOutfits> cOutfitsArrayList;

    public FeedParentAdapter() { }

    public FeedParentAdapter(Context context, ArrayList<cOutfits> cOutfitsArrayList) {
        this.context = context;
        this.cOutfitsArrayList = cOutfitsArrayList;
    }

    @NonNull
    @Override
    public FeedParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.snippet_feed_each_parent_outfit, parent, false);
        return new FeedParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedParentViewHolder holder, int position) {
        cOutfits cOutfits = cOutfitsArrayList.get(position);
        // TODO: fetch data from firestore and set the views
        String userID = cOutfits.getUserID();
        // TODO: fetch username from cUsers using userID
        String username = "UsErNaMe";
        // TODO: fetch profile photo from cUsers using userID
        String profile_photo = "https://preview.redd.it/v0caqchbtn741.jpg?auto=webp&s=c5d05662a039c031f50032e22a7c77dfcf1bfddc";

        holder.outfitnameTV.setText(cOutfits.getName());
        holder.usernameTV.setText(username);
        Glide.with(holder.itemView.getContext()).load(profile_photo).into(holder.profilePhotoCIV);

        holder.clothesRecyclerView.setHasFixedSize(true);
        holder.clothesRecyclerView.setLayoutManager(
                new GridLayoutManager(holder.itemView.getContext(), 4));
        FeedChildAdapter feedChildAdapter = new FeedChildAdapter();
        // TODO: TAKE NOTE MIGHT HAVE ISSUES
        // Array in cOutfit.items in firestore is a reference to a clothes document
        // Might still need to retrieve the image from the clothes
        feedChildAdapter.setChildItemList(cOutfits.getItems());
        holder.clothesRecyclerView.setAdapter(feedChildAdapter);
        feedChildAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cOutfitsArrayList.size();
    }

    public class FeedParentViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTV;
        private TextView outfitnameTV;
        private CircleImageView profilePhotoCIV;
        private RecyclerView clothesRecyclerView;

        public FeedParentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTV = itemView.findViewById(R.id.feed_each_outfit_username_TV);
            outfitnameTV = itemView.findViewById(R.id.tvOutfitName);
            profilePhotoCIV = itemView.findViewById(R.id.feed_each_outfit_profile_photo_CIV);
            clothesRecyclerView = itemView.findViewById(R.id.clothing_RV);
        }
    }
}
