package com.example.shibushi.Utils.Feed;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shibushi.Models.cUsers;
import com.example.shibushi.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This class is an adapter to populate the recycler view of the search user activity in feed
 */
public class cUsersAdapter extends FirestoreRecyclerAdapter<cUsers, cUsersAdapter.cUsersViewHolder> {

    public UserClickListener userClickListener;

    public interface UserClickListener {
        void onSelectedUser(cUsers cUser);
    }

    public cUsersAdapter(FirestoreRecyclerOptions<cUsers> options, UserClickListener userClickListener) {
        super(options);
        this.userClickListener = userClickListener;
    }

    class cUsersViewHolder extends RecyclerView.ViewHolder {
        final CardView view;
        final TextView username;
        final CircleImageView profile_photo;

        cUsersViewHolder(CardView v) {
            super(v);
            view = v;
            username = v.findViewById(R.id.username);
            profile_photo = v.findViewById(R.id.profile_photo);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final cUsersViewHolder holder, @NonNull int position, @NonNull final cUsers user) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.username.setText(user.getUsername());

        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

        mStorageReference.child("images").child(user.getProfile_photo()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                String imageURL = uri.toString();
                Glide.with(holder.itemView.getContext()).load(imageURL).into(holder.profile_photo);

                // When user selects a user
                if (userClickListener != null) {
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userClickListener.onSelectedUser(user);

                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public cUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user_list_item, parent, false);

        return new cUsersViewHolder(v);
    }

}
