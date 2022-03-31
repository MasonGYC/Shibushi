package com.example.shibushi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
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
    private static final int b_menu_ACTIVTY_NUM = 0; // Bottom navbar activity number

    Button bLogout, bChangePassword, bImportClothing;
    Button bLogout, bChangePassword, bImportClothing, bTakePhoto;
    TextView tvWelcome;
    String welcome;
    private FirebaseAuth mAuth;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    static final String KEY_PHOTO = "PHOTO";
    Uri photoURI;

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

        //take photo
        bTakePhoto = findViewById(R.id.bTakePhoto);
        bTakePhoto.setOnClickListener(this);

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
                ImportPhoto importPhoto1 = new ImportPhoto();
                importPhoto1.SelectImage(PICK_IMAGE_REQUEST);
                break;
            case R.id.bTakePhoto:
                ImportPhoto importPhoto = new ImportPhoto();
                importPhoto.dispatchTakePictureIntent(photoURI,REQUEST_IMAGE_CAPTURE,MainActivity.this);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("onActivityResult","tagItIntent");
            Intent tagItIntent = new Intent(MainActivity.this,TagIt.class);
            tagItIntent.putExtra(KEY_PHOTO, photoURI.toString());
            startActivity(tagItIntent);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK  && intent != null && intent.getData() != null) {
            Uri filePath = intent.getData();
            Intent tagItIntent = new Intent(MainActivity.this, TagIt.class);
            tagItIntent.putExtra(KEY_PHOTO, filePath.toString());
            startActivity(tagItIntent);
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
