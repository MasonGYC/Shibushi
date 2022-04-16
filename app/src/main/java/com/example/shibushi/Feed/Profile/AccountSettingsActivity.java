package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettings";
    private final Context mContext = AccountSettingsActivity.this;
    private static final int b_menu_ACTIVTY_NUM = 0; // Bottom navbar activity number

    ListView settingsListView;
    ArrayList<String> settingsArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        this.setTitle("Account Settings");

        settingsListView = findViewById(R.id.account_settings_ListView);
        settingsArrayList = new ArrayList<>();
        settingsArrayList.add("Edit Profile");
        settingsArrayList.add("Change Password");
        settingsArrayList.add("Sign Out");

        setupBottomNavigationView();

        ArrayAdapter arrayAdapter = new ArrayAdapter(
                mContext, android.R.layout.simple_list_item_1, settingsArrayList);
        settingsListView.setAdapter(arrayAdapter);

        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    // Edit profile
                    Intent intent = new Intent(AccountSettingsActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                } else if (i == 1) {
                    // Change Password
                    Intent intent = new Intent(AccountSettingsActivity.this, ChangePassword.class);
                    startActivity(intent);
                } else {
                    // Sign Out
                    Intent intent = new Intent(AccountSettingsActivity.this, SignOutActivity.class);
                    startActivity(intent);
                }
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
