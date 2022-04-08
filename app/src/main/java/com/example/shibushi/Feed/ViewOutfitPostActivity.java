package com.example.shibushi.Feed;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * This class is for:
 * When we click on a outfit from user profile
 * It will navigate back to a larger view of a single outfit
 * Exactly the same on one as the one from feed
 * But only ONE outfit - No parent RecyclerView
 */
public class ViewOutfitPostActivity extends AppCompatActivity {

    private static final String TAG = "ViewOutfitPostActivity";
    private Context mContext = ViewOutfitPostActivity.this;
    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_outfit);
        Log.d(TAG, "onCreate: started");

        setupBottomNavigationView(); //Setup bottom navigation bar
    }

    // BottomNavigationView setup
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
