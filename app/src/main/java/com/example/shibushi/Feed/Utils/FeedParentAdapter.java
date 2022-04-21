package com.example.shibushi.Feed.Utils;

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
import com.example.shibushi.Models.cUsers;
import com.example.shibushi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedParentAdapter extends RecyclerView.Adapter<FeedParentAdapter.FeedParentViewHolder> {

    private final String TAG = "FeedParentAdapter";
    private static final FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private static final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    ArrayList<cOutfits> cOutfitsList;


    public FeedParentAdapter(ArrayList<cOutfits> cOutfitsArrayList) {
        this.cOutfitsList = cOutfitsArrayList;
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
        Log.d(TAG, "onBindViewHolder: setting up viewholder on position " + position);
        cOutfits cOutfit = cOutfitsList.get(position);

        String userID = cOutfit.getUserID();

        // Get the document of the owner of the current outfit
        mDatabase.collection("cUsers")
                .whereEqualTo("userID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QueryDocumentSnapshot document = task.getResult().iterator().next();
                            cUsers user = document.toObject(cUsers.class);

                            String username = user.getUsername();
                            String profile_photo_name = user.getProfile_photo();

                            holder.outfitNameTV.setText(cOutfit.getName());
                            holder.usernameTV.setText(username);
                            Log.d(TAG, cOutfit.toString());

                            mStorageReference.child("images").child(profile_photo_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    // Set profile photo
                                    String imageURL = uri.toString();
                                    Glide.with(holder.itemView.getContext()).load(imageURL).into(holder.profilePhotoCIV);

                                    holder.clothesRecyclerView.setHasFixedSize(true);
                                    holder.clothesRecyclerView.setLayoutManager(
                                            new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));

                                    Log.d("FeedParentAdapter", cOutfit.toString());
                                    FeedChildAdapter feedChildAdapter = new FeedChildAdapter(cOutfit.getImg_names());
                                    holder.clothesRecyclerView.setAdapter(feedChildAdapter);
                                    feedChildAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

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
            outfitNameTV = itemView.findViewById(R.id.feed_each_outfit_TV);
            profilePhotoCIV = itemView.findViewById(R.id.feed_each_outfit_profile_photo_CIV);
            clothesRecyclerView = itemView.findViewById(R.id.feed_clothing_RV);
        }
    }
}
