package com.example.shibushi.Utils;

import android.content.Context;

import com.example.shibushi.Models.User;
import com.example.shibushi.Models.UserAccountSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

/*
* Helper methods for use with Cloud Storage and Cloud Firestore
* */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference dRef;
    private StorageReference mStorageReference;
    private String userID;

    private Context mContext;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://shibushi-default-rtdb.asia-southeast1.firebasedatabase.app/");
        dRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void addNewUser(String email, String username, String profile_photo) {
        User user = new User(userID, email, username);
        dRef = dRef.child("users").child(userID);

        HashMap<String, String> new_user = new HashMap<>();
        new_user.put("username", user.getUsername());
        new_user.put("email", user.getEmail());
        dRef.setValue(new_user);

        String bio = "I am too lazy to introduce to myself.";
        long defFollower = 0;
        long defFollowing = 0;
        long defOutfits = 0;

        UserAccountSettings settings = new UserAccountSettings(
                username, profile_photo, bio, defFollower, defFollowing, defOutfits);

        dRef.child("user_account_settings").child(userID).setValue(settings);
    }


}
