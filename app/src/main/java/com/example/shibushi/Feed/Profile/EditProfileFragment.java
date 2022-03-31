package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shibushi.R;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starting");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mProfilePhoto = view.findViewById(R.id.snippet_edit_profile_center_pp);

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

        return view;
    }


    // For setting profile image
    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting profile image");

        // TODO: Set profile image from firebase, currently a dummy image
        String imgURL = "https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "");
    }

}
