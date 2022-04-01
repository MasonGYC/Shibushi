package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shibushi.R;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private CircleImageView mProfilePhoto;
    private ImageView mCheck;
    private TextView mEmail, mUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starting");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Widgets setup
        mProfilePhoto = view.findViewById(R.id.snippet_edit_profile_center_pp);
        mEmail = view.findViewById(R.id.snippet_edit_profile_center_tvEmail);
        mUsername = view.findViewById(R.id.snippet_edit_profile_center_tvUsername);
        mCheck = view.findViewById(R.id.snippet_edit_profile_toolbar_check);

        // Set textViews
        mEmail.setText(currentUser.getEmail());
        mUsername.setText(currentUser.getDisplayName());

        // setup profile image
        setProfileImage();

        //setup back arrow to profile
        ImageView back = view.findViewById(R.id.snippet_edit_profile_toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to Profile");
                getActivity().finish();
            }
        });

        // TODO: setup Check (Save changed)
        setupCheck();

        return view;
    }

    // TODO: For setting up check button
    private void setupCheck() {
        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implement changes into firebase
                getActivity().finish();
            }
        });
    }


    // For setting profile image
    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting profile image");

        // TODO: Set profile image from firebase, currently a dummy image
        String imgURL = "https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "");
    }

}
