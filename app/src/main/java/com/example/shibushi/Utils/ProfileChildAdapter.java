package com.example.shibushi.Utils;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class ProfileChildAdapter extends RecyclerView.Adapter<ProfileChildAdapter.ProfileChildViewHolder> {

    private final String TAG = "ProfileChildAdapter";
    private ArrayList<String> cClothesList;

    public void setChildItemList(ArrayList<String> cClothesList){
        this.cClothesList = cClothesList;

    }

    public ProfileChildAdapter(ArrayList<String> cClothesArrayList) {
        this.cClothesList = cClothesArrayList;
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
        Log.d(TAG, "onBindViewHolder: setting up viewholder on position " + position);
        String imageName = cClothesList.get(position);

        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

        mStorageReference.child("images").child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                String imageURL = uri.toString();
                Glide.with(holder.itemView.getContext()).load(imageURL).into(holder.clothingIV);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
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
