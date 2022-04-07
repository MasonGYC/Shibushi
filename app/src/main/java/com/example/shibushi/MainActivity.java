package com.example.shibushi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.shibushi.Login.ChangePassword;
import com.example.shibushi.Login.Login;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.shibushi.PhotoProcess.CropActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private Context mContext = MainActivity.this;
    private static final int b_menu_ACTIVTY_NUM = 0; // Bottom navbar activity number

    Button bLogout, bChangePassword, bImportClothing, bTakePhoto;
    TextView tvWelcome;
    String welcome;
    private FirebaseAuth mAuth;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE_REQUEST = 2;
    static final String KEY_PHOTO = "PHOTO";
    Uri photoURI;
    String currentPhotoPath;

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
                changePassword();
                break;
            case R.id.bImportClothing:
                Intent cropIntent = new Intent(MainActivity.this, CropActivity.class);
                startActivity(cropIntent);
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

    //Methods below better not put in a separate file, due to pass-by-reference i guess
    //take picture
    public void dispatchTakePictureIntent(int REQUEST_IMAGE_CAPTURE) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(MainActivity.this,"Cannot create files for photos",Toast.LENGTH_SHORT).show();
                //Log.i("TakePicture: ","Cannot create files for photos");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);

            }
        }
    }

    public void changePassword() {
        startActivity(new Intent(MainActivity.this, ChangePassword.class));
    }



    //store in public Pictures directory
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Shibushi_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Select Image method
    public void SelectImage(int PICK_IMAGE_REQUEST) {
        // Defining Implicit Intent to mobile gallery
        Intent selectIntent = new Intent();
        selectIntent.setType("image/*");
        selectIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectIntent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

}


/*
* This is the Main screen that the user sees. From here, users can navigate to other activities.
* */
