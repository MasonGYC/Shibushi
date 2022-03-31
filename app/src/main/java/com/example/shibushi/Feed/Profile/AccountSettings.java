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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Utils.SectionsStatePagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AccountSettings extends AppCompatActivity {

    private static final String TAG = "AccountSettings";
    private final Context mContext = AccountSettings.this;
    private static final int b_menu_ACTIVTY_NUM = 1; // Bottom navbar activity number
    public SectionsStatePagerAdapter pagerAdapter;
    private ViewPager2 mViewPager;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_account_settings);
        Log.d(TAG, "onCreate: started");

        mViewPager = findViewById(R.id.layout_centre_viewPager); // This is a general container; can be reused
        mRelativeLayout = findViewById(R.id.community_account_settings_relLayout1); // RelativeLayout that holds the entire account setting page

        // Setup top toolbar
        setupToolBar();

        // Setup bottom navigation bar
        setupBottomNavigationView();

        // setup Setting list
        setupSettingsList();

        // setup Fragments
        setupFragments();
    }

    // Fragment setup
    private void setupFragments() {
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager(), getLifecycle());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile)); //fragment #0
        pagerAdapter.addFragment(new ChangePwFragment(), getString(R.string.change_password)); //fragment #1
        pagerAdapter.addFragment(new SignOutFragment(), getString(R.string.logout)); //fragment #2
    }

    // ViewPager setup: Responsible for nav to fragments
    private void setViewPager(int fragmentNumber) {
        mRelativeLayout.setVisibility(View.GONE); //RelativeLayout1 (Account setting list page) will be gone when proceeding to a fragment
        Log.d(TAG, "setViewPager: Navigating to fragment #" + fragmentNumber);
        mViewPager.setAdapter(pagerAdapter); //Sets of fragments
        mViewPager.setCurrentItem(fragmentNumber); //Depends on what list item is clicked
    }

    // Setting List setup
    private void setupSettingsList() {
        Log.d(TAG, "setupSettingsList: initialising 'Account Setting List");
        ListView listView = findViewById(R.id.layout_account_settings_center_listView);

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile)); //fragment #0
        options.add(getString(R.string.change_password)); //fragment #1
        options.add(getString(R.string.logout)); //fragment #2

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        // When fragment #i is clicked: setViewPager will set the page to the respective fragment #i
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int fragmentNumber, long id) {
                Log.d(TAG, "onItemClick: Navigating to fragment #" + fragmentNumber  );
                setViewPager(fragmentNumber); //
            }
        });
    }

    // Top toolbar setup
    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.community_account_settings_toolbar);
        setSupportActionBar(toolbar);

        ImageView back = findViewById(R.id.snippet_account_settings_toolbar_back);
        TextView tvUsername = findViewById(R.id.snippet_account_settings_toolbar_username);

        // Firebase authentication
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Set username on toolbar
        String username = currentUser.getDisplayName();
        if (username != null) {
            tvUsername.setText(username);}

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to profile");
                Intent intent = new Intent(mContext, Profile.class);
                finish();
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
