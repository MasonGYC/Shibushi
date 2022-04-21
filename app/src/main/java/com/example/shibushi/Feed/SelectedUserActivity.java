package com.example.shibushi.Feed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cOutfits;
import com.example.shibushi.Models.cUsers;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Feed.Profile.Utils.ProfileParentAdapter;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectedUserActivity extends AppCompatActivity {

    private static final String TAG = "SelectedUserActivity";
    private Context mContext = SelectedUserActivity.this;
    private static final int b_menu_ACTIVTY_NUM = 0; // Bottom navbar activity number

    // Widgets
    private TextView mOutfits, mFollowers, mFollowing, mUsername, mBio;
    private TextView mToFollow;
    private ProgressBar mProgressBar;
    private Toolbar toolbar;
    private ImageView profilePhoto;

    // RCViews
    private RecyclerView parentRecyclerView;
    private ProfileParentAdapter profileParentAdapter;

    // Data from previous intent
    private cUsers user;
    Intent intent;

    private FirebaseFirestore mDatabase;
    private static CollectionReference userRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        mDatabase = FirebaseFirestore.getInstance();
        userRef = mDatabase.collection("cUsers");

        intent = getIntent();

        if (intent != null ) {
            user = (cUsers) intent.getSerializableExtra("data");
        }

        setupActivityWidgets();
        initImageLoader();
        setupBottomNavigationView();

        setupToolBar(user);
        setupUserDetails(user);
        setupRecyclerViews(user.getUserID());
    }

    private void setupRecyclerViews(String current_UserID) {
        mDatabase.collection("cOutfits")
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .whereEqualTo("userID", current_UserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // set outfit count textview
                            mOutfits.setText(String.valueOf(task.getResult().size()));
                            ArrayList<cOutfits> cOutfitsArrayList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // Change document into class
                                cOutfits outfit = document.toObject(cOutfits.class);
                                cOutfitsArrayList.add(outfit);
                                Log.e(TAG, String.valueOf(cOutfitsArrayList.size()));
                            }

                            // Recycler Views and Adapters
                            parentRecyclerView = findViewById(R.id.profile_outfit_RV);
                            parentRecyclerView.setHasFixedSize(true);
                            parentRecyclerView.setLayoutManager(new LinearLayoutManager(SelectedUserActivity.this));
                            profileParentAdapter = new ProfileParentAdapter(cOutfitsArrayList);
                            parentRecyclerView.setAdapter(profileParentAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Method to make it cleaner in onCreate
     */
    private void setupActivityWidgets() {
        // Progress bar
        mProgressBar = findViewById(R.id.profile_progress_bar);
        mProgressBar.setVisibility(View.GONE);

        // Profile photo
        profilePhoto = findViewById(R.id.snippet_centre_profile_photo);

        // TextViews
        mOutfits = findViewById(R.id.tvOutfits);
        mFollowers = findViewById(R.id.tvFollowers);
        mFollowing = findViewById(R.id.tvFollowing);
        mUsername = findViewById(R.id.snippet_profile_top_toolbar_username);
        mBio = findViewById(R.id.snippet_centre_profile_bio);

        // Toolbar
        toolbar = findViewById(R.id.snippet_profile_toolbar);
    }

    /**
     * Setup user details: followStatus, OutfitsCount, Following, Followers
     * and update the UI respectively
     */
    private void setupUserDetails(cUsers user) {
        Log.d(TAG, "setupUserDetails: setting user details");

        Button mFollowStatus = findViewById(R.id.buttonFollowStatus);
        String selected_userID = user.getUserID();
        String current_userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRef = mDatabase.collection("cUsers").document(current_userID);
        DocumentReference docRef_selected_user = mDatabase.collection("cUsers").document(selected_userID);
        DocumentReference selUserRef = userRef.document(selected_userID);


        //check if current user is already following the selected user
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        cUsers currentUser = document.toObject(cUsers.class);
                        if (currentUser.getFollowing().contains(selected_userID)) {
                            // Update follow status to following
                            mFollowStatus.setText("FOLLOWING");
                        } else {
                            // Update follow status to not following
                            mFollowStatus.setText("FOLLOW");
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // Updates selected user details
        getOutfitCount(user);
        mFollowers.setText(String.valueOf(user.getFollowers().size()));
        mFollowing.setText(String.valueOf(user.getFollowing().size()));
        mBio.setText(user.getBio());
        final int[] follower_count = {user.getFollowers().size()};

        // When current user clicks follow
        mFollowStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: Follow/ Unfollow");
                if (mFollowStatus.getText().toString().equals("FOLLOWING")) {
                    // Unfollow - Remove userID from current user's following
                    Map<String, Object> remove_hashMap_following = new HashMap<>();
                    remove_hashMap_following.put("following", FieldValue.arrayRemove(selected_userID));

                    Map<String, Object> remove_hashMap_followers = new HashMap<>();
                    remove_hashMap_followers.put("followers", FieldValue.arrayRemove(selected_userID));

                    // update on firestore
                    docRef.update(remove_hashMap_following);
                    docRef_selected_user.update(remove_hashMap_followers);

                    mFollowStatus.setText("FOLLOW");

                    // hardcoded method
                    follower_count[0]--;
                    mFollowers.setText(String.valueOf(follower_count[0]));
                    Toast.makeText(mContext, "Unfollowed!", Toast.LENGTH_SHORT).show();

                } else {
                    // Follow - Add userID to current user's following
                    Map<String, Object> add_hashMap_following = new HashMap<>();
                    add_hashMap_following.put("following", FieldValue.arrayUnion(selected_userID));

                    Map<String, Object> add_hashMap_followers = new HashMap<>();
                    add_hashMap_followers.put("followers", FieldValue.arrayUnion(selected_userID));
                    docRef.update(add_hashMap_following);
                    docRef_selected_user.update(add_hashMap_followers);

                    mFollowStatus.setText("FOLLOWING");
                    // hardcoded method
                    follower_count[0]++;
                    mFollowers.setText(String.valueOf(follower_count[0]));
                    mFollowers.setText(String.valueOf(follower_count[0]));
                    Toast.makeText(mContext, "Followed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

        mStorageReference.child("images").child(user.getProfile_photo()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                String imageURL = uri.toString();
                UniversalImageLoader.setImage(imageURL, profilePhoto, mProgressBar, "");
                            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }

    /**
     * Method to search through cOutfits collection and returning the count
     * @param user
     */
    private void getOutfitCount(cUsers user) {
        mDatabase.collection("cOutfits")
                .whereEqualTo("userID", user.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mOutfits.setText(String.valueOf(task.getResult().size()));
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Profile Image setup
     */
    private void setProfileImage(cUsers user) {
        Log.d(TAG, "setProfileImage: setting profile image");
        String imgURL = user.getProfile_photo();
        UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "");
    }

    /**
     * Top toolbar setup
     */
    private void setupToolBar(cUsers user) {
        Log.d(TAG, "setupToolBar: setting toolbar");
        toolbar = findViewById(R.id.snippet_profile_toolbar);
        setSupportActionBar(toolbar);

        ImageView back = findViewById(R.id.snippet_other_profile_top_toolbar_back);
        TextView tvUsername = findViewById(R.id.snippet_other_profile_top_toolbar_username);

        tvUsername.setText(user.getUsername());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to SearchActivity");
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

    /**
     * Bottom navigation bar setup
     */
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
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: Navigating back to SearchActivity");
        Intent intent = new Intent(mContext, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
