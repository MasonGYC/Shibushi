package com.example.shibushi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private Context mContext = MainActivity.this;
    private static final int b_menu_ACTIVTY_NUM = 0;
    Button bLogout, bChangePassword, bImportClothing;
    TextView tvWelcome;
    String welcome;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button Views
        bLogout = findViewById(R.id.bLogout);
        bChangePassword = findViewById(R.id.bChangePassword);
        bImportClothing = findViewById(R.id.bImportClothing);

        // Button OnClickListener
        bLogout.setOnClickListener(this);
        bChangePassword.setOnClickListener(this);
        bImportClothing.setOnClickListener(this);

        // Set up bottom navigation bar
        setupBottomNavigationView();

        //Firebase authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Welcome message
        //Obviously we don't have to include this but its an option to display the username somewhere
        //somehow
        String username = currentUser.getDisplayName();
        if (username != null){
            welcome = "Welcome, " + currentUser.getDisplayName();
        }
        else {
            welcome = ""; //not sure if it's possible to have a blank string
        }
        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText(welcome);
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

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bLogout:
                logOut();
                break;
            // Not high priority, may remove
            case R.id.bChangePassword:
                break;
            case R.id.bImportClothing:
                startActivity(new Intent(this, ImportClothing.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    // TODO: 3/15/2022 refactor into a new java file 
    private void logOut() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Toast.makeText(this, user.getEmail()+ "is logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
        }else{
            Toast.makeText(this, "You aren't logged in yet!", Toast.LENGTH_SHORT).show();
        }
    }

}


/*
* This is the Main screen that the user sees. From here, users can navigate to other activities.
* */
