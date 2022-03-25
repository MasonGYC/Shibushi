package com.example.shibushi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Utils.GridImageAdapter;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private static final String TAG = "Profile";
    private Context mContext = Profile.this;
    private static final int b_menu_ACTIVTY_NUM = 1; // Bottom navbar activity number
    private ProgressBar mProgressBar;
    private ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_profile);
        Log.d(TAG, "onCreate: started");

        setupToolBar();
        setupBottomNavigationView();
        setupActivityWidgets();
        setProfileImage();
        tempGridSetup();
    }

    /**
     * Method to make it cleaner in onCreate
     */
    private void setupActivityWidgets() {
        // Progress bar
        mProgressBar = findViewById(R.id.community_profile_progress_bar);
        mProgressBar.setVisibility(View.GONE);

        // Profile photo
        profilePhoto = findViewById(R.id.layout_centre_profile_photo);
    }

    /**
     * Temporary grid setup
     * TODO: Temporary for testing that this view works, removed later
     */
    private void tempGridSetup() {
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://i.pinimg.com/736x/07/87/ca/0787ca9df636bbbaaca33374cfb24d66.jpg");
        imgURLs.add("https://i.pinimg.com/originals/76/ab/da/76abda9cd997be17540107d58fa4056b.jpg");
        imgURLs.add("https://i.pinimg.com/originals/b0/86/06/b08606e424577d699c73d6cdeb8e0811.jpg");
        imgURLs.add("https://i.pinimg.com/originals/c6/5e/89/c65e89429b2223bd6cc0b1e07386fea4.jpg");
        imgURLs.add("https://i.pinimg.com/originals/29/8c/bc/298cbc3b1419bfa5d85f91651a9345b2.jpg");
        imgURLs.add("https://i.pinimg.com/originals/92/97/69/92976925c6dbde908b1c8abc7c6aa5cd.jpg");
        imgURLs.add("https://i.pinimg.com/originals/f2/db/4d/f2db4d98e7545380407bdd7cdac97407.jpg");
        imgURLs.add("https://s12.favim.com/orig/160309/adidas-aesthetic-alternative-bw-Favim.com-4064782.jpg");
        imgURLs.add("https://i.pinimg.com/736x/0c/33/80/0c3380fd4133bed5ce50bd196b7732ce.jpg");
        imgURLs.add("https://i.pinimg.com/originals/fe/95/07/fe950798e4e0e7ca61cd229d834a0007.png");
        imgURLs.add("https://i.pinimg.com/originals/fb/77/d7/fb77d7c7bff424ca0067ded646df9fde.jpg");
        imgURLs.add("https://data.whicdn.com/images/162374411/original.jpg");

        setupImageGrid(imgURLs);
    }

    /**
     * Posts grid view setup
     * TODO: Obtain image URLs from firebase, currently dummy images
     */
    private void setupImageGrid(ArrayList<String> imgURLs) {
        GridView gridView = findViewById(R.id.layout_centre_profile_gridView);
        GridImageAdapter gridImageAdapter = new GridImageAdapter(mContext, R.layout.layout_profile_grid_imageview, "", imgURLs);
        gridView.setAdapter(gridImageAdapter);
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

        mysettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to account settings");
                Intent intent = new Intent(mContext, AccountSettings.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to feed");
                Intent intent = new Intent(mContext, Feed.class);
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
