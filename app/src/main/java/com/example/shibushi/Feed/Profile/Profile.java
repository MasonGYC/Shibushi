package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Feed.FeedActivity;
import com.example.shibushi.Models.cClothing;
import com.example.shibushi.Models.cOutfits;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
//import com.example.shibushi.Utils.GridImageAdapter;
import com.example.shibushi.Utils.FeedParentAdapter;
import com.example.shibushi.Utils.ProfileParentAdapter;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private static final String TAG = "Profile";
    private Context mContext = Profile.this;
    private static final int b_menu_ACTIVTY_NUM = 1; // Bottom navbar activity number
    private static final int NUM_GRID_PER_ROW = 2;
    private TextView mOutfits, mFollowers, mFollowing, mUsername, mBio;
    private TextView mEditProfile;
    private ProgressBar mProgressBar;
    private Toolbar toolbar;
    private GridView mGridView;
    private ImageView profilePhoto;
    // RCViews
    private RecyclerView parentRecyclerView;
    private ProfileParentAdapter profileParentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_profile);
        Log.d(TAG, "onCreate: started");

        setupToolBar();
        setupBottomNavigationView();
        setupActivityWidgets();
        setProfileImage();
//        tempGridSetup();

        parentRecyclerView = findViewById(R.id.profile_outfit_RV);
        parentRecyclerView.setHasFixedSize(true);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileParentAdapter = new ProfileParentAdapter();
        parentRecyclerView.setAdapter(profileParentAdapter);

        //TODO: Utilise firestore methods
        // DUMMY DATA
        ArrayList<cClothing> cClothingList = new ArrayList<>();
        cClothing cClothing1 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing2 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing3 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing4 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing5 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");

        cClothingList.add(cClothing1);
        cClothingList.add(cClothing2);
        cClothingList.add(cClothing3);
        cClothingList.add(cClothing4);
        cClothingList.add(cClothing5);

        ArrayList<cOutfits> cOutfitsList = new ArrayList<>();
        cOutfits cOutfits1 = new cOutfits(
                "outfitID1", "timestamp1", "userID1", "outfitname1","cat1", cClothingList);
        cOutfits cOutfits2 = new cOutfits(
                "outfitID2", "timestamp2", "userID2", "outfitname2","cat1", cClothingList);
        cOutfitsList.add(cOutfits1);
        cOutfitsList.add(cOutfits2);

        profileParentAdapter.setcOutfitsList(cOutfitsList);
        profileParentAdapter.notifyDataSetChanged();


    }

//    /**
//     * To load the profile fragment
//     */
//    private void init() {
//        Log.d(TAG, "init: inflating profile fragment");
//
//        ProfileFragment profileFragment = new ProfileFragment();
//        FragmentTransaction fragmentTransaction = Profile.this.getSupportFragmentManager().beginTransaction();
//        // replace the profile container in layout/commmunity_profile with the view for ProfileFragment class
//        fragmentTransaction.replace(R.id.profile_container, profileFragment);
//
//        // keeping track of fragment stack (order of page when going back)
//        // fragments usually do not keep track of previous fragments visited
//        fragmentTransaction.addToBackStack(getString(R.string.profile_fragment));
//        fragmentTransaction.commit();
//    }

    /**
     * Method to make it cleaner in onCreate
     */
    private void setupActivityWidgets() {
        // Progress bar
        mProgressBar = findViewById(R.id.profile_progress_bar);
        mProgressBar.setVisibility(View.GONE);

        // Profile photo
        profilePhoto = findViewById(R.id.layout_centre_profile_photo);

        // TextViews
        mOutfits = findViewById(R.id.tvOutfits);
        mFollowers = findViewById(R.id.tvFollowers);
        mFollowing = findViewById(R.id.tvFollowing);
        mUsername = findViewById(R.id.snippet_profile_top_toolbar_username);
        mBio = findViewById(R.id.layout_centre_profile_bio);

        // GridView
        // mGridView = findViewById(R.id.layout_centre_profile_gridView);

        // Toolbar
        toolbar = findViewById(R.id.snippet_profile_toolbar);
    }

    /**
     * Initialise ImageLoader
     * Quick Setup Src- https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Quick-Setup
     */
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /**
     * Profile Image setup
     * TODO: Obtain image from firebase, currently dummy image
     */
    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting profile image");
        String imgURL = "https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg";
        UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "");
    }

    /**
     * Top toolbar setup
     */
    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.snippet_profile_toolbar);
        setSupportActionBar(toolbar);

        ImageView mysettings = findViewById(R.id.snippet_profile_top_toolbar_settings);
        ImageView back = findViewById(R.id.snippet_profile_top_toolbar_back);
        TextView tvUsername = findViewById(R.id.snippet_profile_top_toolbar_username);

        TextView mEditProfile = findViewById(R.id.textEditProfile);

        // Firebase authentication
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Set username on toolbar
        String username = currentUser.getDisplayName();
        if (username != null) {
            tvUsername.setText(username);}

        mysettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to account settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to feed");
                Intent intent = new Intent(mContext, FeedActivity.class);
                startActivity(intent);
            }
        });

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to edit profile");
                Intent intent = new Intent(mContext, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Bottom navigation bar setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        BottomNavigationView bottom_navbar_view = findViewById(R.id.bottom_navbar_view);
        BottomNavigationViewHelper.setupBottomNavigationView(bottom_navbar_view);
        BottomNavigationViewHelper.enableNavigation(mContext, bottom_navbar_view);

        // To highlight the correct icon when on correct page
        Menu menu = bottom_navbar_view.getMenu();
        MenuItem menuItem = menu.getItem(b_menu_ACTIVTY_NUM);
        menuItem.setChecked(true);
    }
}
