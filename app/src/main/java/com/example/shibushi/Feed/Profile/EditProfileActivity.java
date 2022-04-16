package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shibushi.Feed.FeedActivity;
import com.example.shibushi.MainActivity;
import com.example.shibushi.Models.cUsers;
import com.example.shibushi.PhotoProcess.CropActivity;
import com.example.shibushi.PhotoProcess.ResultActivity;
import com.example.shibushi.R;
import com.example.shibushi.Utils.FirestoreMethods;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    public static final String TAG = "EditProfileActivity";
    private final Context mContext = EditProfileActivity.this;

    private CircleImageView profile_photo_CIV;
    private TextView emailTV, usernameTV, edit_profile_TV;
    private EditText new_bio;
    private Button saveChangesBUTTON;

    private Uri photoURI;


    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: starting");
        setContentView(R.layout.activity_edit_profile);
        this.setTitle("Edit Profile");

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Widgets setup
        profile_photo_CIV = findViewById(R.id.snippet_edit_profile_center_pp);
        edit_profile_TV = findViewById(R.id.snippet_edit_profile_center_change_pp_txt);
        emailTV = findViewById(R.id.snippet_edit_profile_center_tvEmail);
        usernameTV = findViewById(R.id.snippet_edit_profile_center_tvUsername);
        saveChangesBUTTON = findViewById(R.id.activity_edit_profile_save_changes_Button);
        new_bio = findViewById(R.id.snippet_edit_profile_center_etBio);

        // Set textViews
        emailTV.setText(currentUser.getEmail());
        usernameTV.setText(currentUser.getDisplayName());

        // setup profile image
        initImageLoader();

        // change profile picture;
        setup_changePP();

        // Get intent to set image
        Intent bitmapIntent = getIntent();

        // If user comes from ProfileActivity
        if (getIntent().toString().equals("Intent { cmp=com.example.shibushi/.Feed.Profile.EditProfileActivity }")) {
            Log.d(TAG, "Intent from Profile");
            initProfileImage();
        }

        // If user comes from Change profile picture
        if (!getIntent().toString().equals("Intent { cmp=com.example.shibushi/.Feed.Profile.EditProfileActivity }")){
            Log.d(TAG, "Intent from edit profile Photo");

            photoURI = Uri.parse(bitmapIntent.getStringExtra(ResultActivity.KEY_PHOTO));

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                profile_photo_CIV.setImageBitmap(bitmap);
                // get user bio info
                String current_UserID = currentUser.getUid();
                DocumentReference docRefUser = FirebaseFirestore.getInstance().collection("cUsers").document(current_UserID);
                docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                cUsers user = document.toObject(cUsers.class);
                                String user_bio = user.getBio();
                                new_bio.setText(user_bio);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

            } catch (IOException e) {
                Log.i(TAG, "NO imgUri");
            }
        }

        setupSaveChangesBUTTON();

    }

    // For setting profile image
    private void initProfileImage() {
        Log.d(TAG, "setProfileImage: setting profile image");
        String current_UserID = currentUser.getUid();
        DocumentReference docRefUser = FirebaseFirestore.getInstance().collection("cUsers").document(current_UserID);
        docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        cUsers user = document.toObject(cUsers.class);
                        String profile_photo = user.getProfile_photo();
                        String user_bio = user.getBio();

                        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
                        mStorageReference.child("images").child(profile_photo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                UniversalImageLoader.setImage(uri.toString(), profile_photo_CIV, null, "");

                                // set user bio on bio edit text
                                new_bio.setText(user_bio);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    // Change profile picture on current view
    private void setup_changePP() {
        edit_profile_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropActivity.isTakingPhoto = false;
                Intent selectIntent = new Intent(mContext, CropActivity.class);
                selectIntent.putExtra("startingClass", EditProfileActivity.TAG);
                startActivity(selectIntent);
            }
        });
    }

    private void setupSaveChangesBUTTON() {

        new_bio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveChangesBUTTON.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(mContext, "Profile updated!", Toast.LENGTH_SHORT).show();
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseFirestore mFirestoreDB = FirebaseFirestore.getInstance();

                        // update profile picture in firestore
                        if (photoURI == null) {
                            // if change profile picture was not pressed

                        } else {
                            // if change profile picture was pressed
                            Log.d(TAG, photoURI.toString());
                            FirestoreMethods.change_profilepic(currentUser.getUid(), photoURI);
                        }

                        // Update bio in firestore
                        Map<String, Object> bio_map = new HashMap<>();
                        bio_map.put("bio", editable.toString());
                        mFirestoreDB.collection("cUsers").document(userID).update(bio_map);

                        // Navigate back to user profile
                        Intent intent = new Intent(mContext, Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // Prevents going back to prev page when back button is pressed
                        startActivity(intent);
                    }
                });

            }
        });

    }

    /**
     * Initialise ImageLoader
     * Quick Setup Src- https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Quick-Setup
     */
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
}
