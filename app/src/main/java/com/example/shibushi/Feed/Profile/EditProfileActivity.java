package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shibushi.R;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";
    private final Context mContext = EditProfileActivity.this;

    private CircleImageView profile_photo_CIV;
    private TextView emailTV, usernameTV;
    private Button saveChangesBUTTON;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: starting");
        setContentView(R.layout.activity_edit_profile);
        this.setTitle("Edit Profile");

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Widgets setup
        profile_photo_CIV = findViewById(R.id.snippet_edit_profile_center_pp);
        emailTV = findViewById(R.id.snippet_edit_profile_center_tvEmail);
        usernameTV = findViewById(R.id.snippet_edit_profile_center_tvUsername);
        saveChangesBUTTON = findViewById(R.id.activity_edit_profile_save_changes_Button);

        // Set textViews
        emailTV.setText(currentUser.getEmail());
        usernameTV.setText(currentUser.getDisplayName());

        // setup profile image
        initImageLoader();
        setProfileImage();

        // TODO: setup Save Changes to firestore
        setupSaveChangesBUTTON();
    }

    // For setting profile image
    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting profile image");

        // TODO: Set profile image from firebase, currently a dummy image
        String imgURL = "https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg";
        UniversalImageLoader.setImage(imgURL, profile_photo_CIV, null, "");
    }

    // TODO: To update firestore with changes
    private void setupSaveChangesBUTTON() {
        saveChangesBUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Update firestore
                Intent intent = new Intent(mContext, Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // Prevents going back to prev page when back button is pressed
                startActivity(intent);
            }
        });
    }

    /**
     * Initialise ImageLoader
     * Quick Setup Src- https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Quick-Setup
     */
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
}
