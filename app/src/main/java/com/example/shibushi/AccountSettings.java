package com.example.shibushi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountSettings extends AppCompatActivity {

    private static final String TAG = "AccountSettings";
    private Context mContext = AccountSettings.this;
    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 1;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.community_account_settings);
        Log.d(TAG, "onCreate: started");

        // Setup top toolbar
        setupToolBar();

        // Setup bottom navigation bar
        setupBottomNavigationView();
    }

    // Top toolbar setup
    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.community_account_settings_toolbar);
        setSupportActionBar(toolbar);

        ImageView back = findViewById(R.id.snippet_account_settings_toolbar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to profile");
                Intent intent = new Intent(mContext, Profile.class);
                startActivity(intent);
            }
        });
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
