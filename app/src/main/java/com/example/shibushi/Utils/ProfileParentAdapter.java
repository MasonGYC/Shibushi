package com.example.shibushi.Utils;

import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileParentAdapter extends RecyclerView.Adapter<ProfileParentAdapter.ProfileParentViewHolder> {

    private final String TAG = "ProfileParentAdapter";
    ArrayList<cOutfits> cOutfitsList;


    public ProfileParentAdapter(ArrayList<cOutfits> cOutfitsArrayList) {
        this.cOutfitsList = cOutfitsArrayList;
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
        Log.d(TAG, "onBindViewHolder: setting up viewholder on position " + position);
        cOutfits cOutfit = cOutfitsList.get(position);

        holder.outfitNameTV.setText(cOutfit.getName());

        holder.clothesRecyclerView.setHasFixedSize(true);
        holder.clothesRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        ProfileChildAdapter profileChildAdapter = new ProfileChildAdapter(cOutfit.getImg_names());
        holder.clothesRecyclerView.setAdapter(profileChildAdapter);
        profileChildAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.cOutfitsList.size();
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
