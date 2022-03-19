package com.example.shibushi.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.shibushi.Community.Feed;
import com.example.shibushi.Home.MainActivity;
import com.example.shibushi.R;
import com.example.shibushi.Outfits.ViewOutfits;
import com.example.shibushi.Wardrobe.ViewWardrobe;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// A Helper class to organise implementation of Bottom Navigation Bar which applies to all screens
public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavViewHelper";

    public static void setupBottomNavigationView(BottomNavigationView bottomNavigationView) {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
    }

    // setup navigation between activities
    public static void enableNavigation(final Context context, BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.ic_home: // b_menu_ACTIVTY_NUM = 0
                        Intent intent_activity_home = new Intent(context, MainActivity.class);
                        context.startActivity(intent_activity_home);
                        break;

                    case R.id.ic_community_feed: // b_menu_ACTIVTY_NUM = 1
                        Intent intent_community_feed = new Intent(context, Feed.class);
                        context.startActivity(intent_community_feed);
                        break;

                    case R.id.ic_add: // b_menu_ACTIVTY_NUM = 2
                        break;

                    case R.id.ic_wardrobe: // b_menu_ACTIVTY_NUM = 3
                        Intent intent_wardrobe = new Intent(context, ViewWardrobe.class);
                        context.startActivity(intent_wardrobe);
                        break;

                    case R.id.ic_outfits: // b_menu_ACTIVTY_NUM = 4
                        Intent intent_outfits = new Intent(context, ViewOutfits.class);
                        context.startActivity(intent_outfits);
                        break;
                }

                return false;
            }
        });
    }
}
