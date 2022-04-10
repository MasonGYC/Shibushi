package com.example.shibushi.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cUsers;
import com.example.shibushi.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This class is an adapter to populate the recycler view of the search user activity in feed
 */
public class cUsersAdapter extends FirestoreRecyclerAdapter<cUsers, cUsersAdapter.cUsersViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final OnItemClickListener listener;

    cUsersAdapter(FirestoreRecyclerOptions<cUsers> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    public cUsersAdapter(FirestoreRecyclerOptions<cUsers> options) {
        super(options);
        this.listener = null;
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
    public void onBindViewHolder(final cUsersViewHolder holder, @NonNull int position, @NonNull final cUsers users) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.username.setText(users.getUsername());
        UniversalImageLoader.setImage(users.getProfile_photo(), holder.profile_photo, null, "");

        if (listener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.getAbsoluteAdapterPosition());
                }
            });
        }
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
