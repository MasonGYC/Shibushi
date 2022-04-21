package com.example.shibushi.Feed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Feed.Profile.ProfileActivity;
import com.example.shibushi.Models.cOutfits;
import com.example.shibushi.Models.cUsers;
import com.example.shibushi.Utils.PhotoProcess.CropActivity;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Utils.Feed.FeedParentAdapter;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.example.shibushi.Wardrobe.ViewWardrobeActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;

public class FeedActivity extends AppCompatActivity {

    public static final String TAG = "CommunityFeed";
    private Context mContext = FeedActivity.this;

    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 0;

    // Feed
    private RecyclerView parentRecyclerView;
    private FeedParentAdapter feedParentAdapter;

    // For TagIt
    Uri imageUri;
    public static final int PICK_IMAGE_REQUEST = 2;

    // Firestore
    public static final FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    public static final String currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Log.d(TAG, "onCreate: started");

        initImageLoader(); //Init Image Loader
        setupToolBar(); //Setup top toolbar
        setupBottomNavigationView(); //Setup bottom navigation bar
        setup_FAB(); //Setup floating action button

        // RecyclerView
        mDatabase.collection("cUsers").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                cUsers cUser_current = task.getResult().toObject(cUsers.class);
                // Get a list of userID that current user is following
                ArrayList following = cUser_current.getFollowing();

                setupRecyclerViews(following);
            }
        });

    }

    private void setupRecyclerViews(ArrayList<String> current_UserID_ArrayList) {
        ArrayList<cOutfits> cOutfitsArrayList = new ArrayList<>();

        // for every userID in current user's followings, find all their outfits by order
        for (String userID_i : current_UserID_ArrayList) {
            mDatabase.collection("cOutfits")
                    // .orderBy("timeStamp", Query.Direction.ASCENDING)
                    .whereEqualTo("userID", userID_i)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            // iterate through user i's outfit
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                cOutfits user_i_cOutfit = document.toObject(cOutfits.class);
                                // Store the outfit into an arraylist of max size 20
                                if (cOutfitsArrayList.size() <= 20) {
                                    cOutfitsArrayList.add(user_i_cOutfit);
                                } else {
                                    break;
                                }
                            }

                            // Sort the outfits by timestamp
                            Collections.sort(cOutfitsArrayList);

                            // Recycler Views and Adapters
                            parentRecyclerView = findViewById(R.id.feed_parent_RV);
                            parentRecyclerView.setHasFixedSize(true);
                            parentRecyclerView.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
                            feedParentAdapter = new FeedParentAdapter(cOutfitsArrayList);
                            parentRecyclerView.setAdapter(feedParentAdapter);
                        }
                    });
        }
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
     * Floating action button setup
     */
    private void setup_FAB() {
        Log.d(TAG, "setupFloatingActionButton: Setting up FAB menu");

        // new clothing
        FloatingActionButton fab_hanger = findViewById(R.id.community_feed_fab_hanger);
        // new outfit
        FloatingActionButton fab_outfit = findViewById(R.id.community_feed_fab_outfit);

        // New Clothing
        fab_hanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Please select an image from gallery", Toast.LENGTH_SHORT).show();
                importClothing();
            }
        });

        fab_outfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "To select, long press on clothing and click add to basket/delete", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, ViewWardrobeActivity.class);
                startActivity(intent);
            }
        });
    }

    // Top toolbar setup
    private void setupToolBar() {
        Log.d(TAG, "setupBottomNavigationView: Setting up top Toolbar");
        Toolbar toolbar = findViewById(R.id.community_feed_top_toolbar);
        setSupportActionBar(toolbar);

        ImageView myprofile = findViewById(R.id.community_feed_top_toolbar_profile);
        ImageView searchBar = findViewById(R.id.community_feed_top_toolbar_search);

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to my profile");
                Intent intent = new Intent(mContext, ProfileActivity.class);
                startActivity(intent);
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to search");
                Intent intent = new Intent(mContext, SearchActivity.class);
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

    // Select Image method
    public void importClothing() {
        // Defining Implicit Intent to mobile gallery
        Intent selectIntent = new Intent(FeedActivity.this, CropActivity.class);
        selectIntent.putExtra("startingClass", FeedActivity.TAG);
        startActivity(selectIntent);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FeedActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (intent.getData() != null) {
                imageUri = intent.getData();
            }
        }
    }

}
