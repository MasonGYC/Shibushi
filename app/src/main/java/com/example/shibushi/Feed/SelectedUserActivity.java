package com.example.shibushi.Feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Feed.Profile.AccountSettingsActivity;
import com.example.shibushi.Feed.Profile.EditProfileActivity;
import com.example.shibushi.Feed.Profile.Profile;
import com.example.shibushi.Models.cClothing;
import com.example.shibushi.Models.cOutfits;
import com.example.shibushi.Models.cUsers;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Utils.ProfileParentAdapter;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectedUserActivity extends AppCompatActivity {

    private static final String TAG = "SelectedUserActivity";
    private Context mContext = SelectedUserActivity.this;
    private static final int b_menu_ACTIVTY_NUM = 1; // Bottom navbar activity number

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        mDatabase = FirebaseFirestore.getInstance();

        intent = getIntent();

        if (intent != null ) {
            user = (cUsers) intent.getSerializableExtra("data");
        }

        setupActivityWidgets();
        initImageLoader();
        setupBottomNavigationView();

        setupToolBar(user);
        setupUserDetails(user);

        parentRecyclerView = findViewById(R.id.profile_outfit_RV);
        parentRecyclerView.setHasFixedSize(true);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileParentAdapter = new ProfileParentAdapter();
        parentRecyclerView.setAdapter(profileParentAdapter);

        // TODO: Utilise firestore methods
        // DUMMY DATA
        ArrayList<cClothing> cClothingList = new ArrayList<>();
        cClothing cClothing1 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing2 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing3 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing4 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing5 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");

        cClothingList.add(cClothing1);
        cClothingList.add(cClothing2);
        cClothingList.add(cClothing3);
        cClothingList.add(cClothing4);
        cClothingList.add(cClothing5);

        ArrayList<cOutfits> cOutfitsList = new ArrayList<>();
        cOutfits cOutfits1 = new cOutfits(
                "outfitID1", "timestamp1", "userID1", "outfitname1", cClothingList);
        cOutfits cOutfits2 = new cOutfits(
                "outfitID2", "timestamp2", "userID2", "outfitname2", cClothingList);
        cOutfitsList.add(cOutfits1);
        cOutfitsList.add(cOutfits2);

        profileParentAdapter.setcOutfitsList(cOutfitsList);
        profileParentAdapter.notifyDataSetChanged();

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

        TextView mFollowStatus = findViewById(R.id.textEditProfile);
        String selected_userID = user.getUserID();
        String current_userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRef = mDatabase.collection("cUsers").document(current_userID);
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
                            mFollowStatus.setText("Following");
                        } else {
                            // Update follow status to not following
                            mFollowStatus.setText("Follow");
                        }

                        setProfileImage(user);
                        getOutfitCount(user);

                        mFollowStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "onClick: Follow/ Unfollow");
                                // TODO: FOLLOW / UNFOLLOW
                            }
                        });

                        mFollowers.setText(String.valueOf(user.getFollowers().size()));
                        mFollowing.setText(String.valueOf(user.getFollowing().size()));
                        mBio.setText(user.getBio());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
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
