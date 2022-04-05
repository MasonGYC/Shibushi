//package com.example.shibushi.Feed.Profile;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.widget.Toolbar;
//import androidx.fragment.app.Fragment;
//
//import com.example.shibushi.Feed.FeedActivity;
//import com.example.shibushi.R;
//import com.example.shibushi.Utils.BottomNavigationViewHelper;
//import com.example.shibushi.Utils.GridImageAdapter;
//import com.example.shibushi.Utils.UniversalImageLoader;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class ProfileFragment extends Fragment {
//
//    private static final String TAG = "ProfileFragment";
//
//    private FirebaseAuth mAuth;
//    private FirebaseUser user;
//    private FirebaseDatabase mFirebaseDatabase;
//    private DatabaseReference mRef;
//
//
//    private TextView mOutfits, mFollowers, mFollowing, mUsername, mBio;
//    private TextView mEditProfile;
//    private Context mContext;
//    private GridView mGridView;
//    private static final int b_menu_ACTIVTY_NUM = 1; // Bottom navbar activity number
//    private static final int NUM_GRID_PER_ROW = 2;
//    private Toolbar toolbar;
//    private BottomNavigationView bottomNavigationView;
//    private ProgressBar mProgressBar;
//    private CircleImageView mprofilePhoto;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        Log.d(TAG, "onCreateView: started.");
//        System.out.println(TAG);
//
//        setupFirebase();
//        setupActivityWidgets(view);
//        setupBottomNavigationView();
//        setupToolBar(view);
//        tempGridSetup(view);
//        setProfileImage();
//        setupEditProfile(view);
//
//        return view;
//    }
//
//        /**
//     * Temporary grid setup
//     * TODO: Temporary for testing that this view works, removed later
//     */
//    private void tempGridSetup(View view) {
//        ArrayList<String> imgURLs = new ArrayList<>();
//        imgURLs.add("https://i.pinimg.com/736x/07/87/ca/0787ca9df636bbbaaca33374cfb24d66.jpg");
//        imgURLs.add("https://i.pinimg.com/originals/76/ab/da/76abda9cd997be17540107d58fa4056b.jpg");
//        imgURLs.add("https://i.pinimg.com/originals/b0/86/06/b08606e424577d699c73d6cdeb8e0811.jpg");
//        imgURLs.add("https://i.pinimg.com/originals/c6/5e/89/c65e89429b2223bd6cc0b1e07386fea4.jpg");
//        imgURLs.add("https://i.pinimg.com/originals/29/8c/bc/298cbc3b1419bfa5d85f91651a9345b2.jpg");
//        imgURLs.add("https://i.pinimg.com/originals/92/97/69/92976925c6dbde908b1c8abc7c6aa5cd.jpg");
//        imgURLs.add("https://i.pinimg.com/originals/f2/db/4d/f2db4d98e7545380407bdd7cdac97407.jpg");
//        imgURLs.add("https://i.pinimg.com/originals/46/7b/e7/467be7dcdfaa8a27222be53990a8e02e.jpg");
//        imgURLs.add("https://i.pinimg.com/736x/0c/33/80/0c3380fd4133bed5ce50bd196b7732ce.jpg");
//        imgURLs.add("https://i.pinimg.com/originals/fe/95/07/fe950798e4e0e7ca61cd229d834a0007.png");
//        imgURLs.add("https://i.pinimg.com/originals/fb/77/d7/fb77d7c7bff424ca0067ded646df9fde.jpg");
//        imgURLs.add("https://data.whicdn.com/images/162374411/original.jpg");
//
//        setupImageGrid(view, imgURLs);
//    }
//
//    /**
//     * Posts grid view setup
//     * TODO: Obtain image URLs from firebase, currently dummy images
//     */
//    private void setupImageGrid(View view, ArrayList<String> imgURLs) {
//        GridView gridView = view.findViewById(R.id.layout_centre_profile_gridView);
//
//        // set the width of each image grid
//        int gridWidth = getResources().getDisplayMetrics().widthPixels;
//        int imageWidth = gridWidth/NUM_GRID_PER_ROW;
//        gridView.setColumnWidth(imageWidth);
//
//        GridImageAdapter gridImageAdapter = new GridImageAdapter(mContext, R.layout.layout_profile_grid_imageview, "", imgURLs);
//        gridView.setAdapter(gridImageAdapter);
//    }
//
//    /**
//     * Profile Image setup
//     * TODO: Obtain image from firebase, currently dummy image
//     */
//    private void setProfileImage() {
//        Log.d(TAG, "setProfileImage: setting profile image");
//        String imgURL = "https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg";
//        UniversalImageLoader.setImage(imgURL, mprofilePhoto, mProgressBar, "");
//    }
//
//    /**
//     * Method to make it it cleaner is onCreateView
//     * @param view current view
//     */
//    private void setupActivityWidgets(View view) {
//        // Context
//        mContext = getActivity();
//
//        // Progress bar
//        mProgressBar = view.findViewById(R.id.fragment_profile_progress_bar);
//        mProgressBar.setVisibility(View.GONE);
//
//        // TextViews
//        mOutfits = view.findViewById(R.id.tvOutfits);
//        mFollowers = view.findViewById(R.id.tvFollowers);
//        mFollowing = view.findViewById(R.id.tvFollowing);
//        mUsername = view.findViewById(R.id.snippet_profile_top_toolbar_username);
//        mBio = view.findViewById(R.id.layout_centre_profile_bio);
//        mEditProfile = view.findViewById(R.id.textEditProfile);
//
//        // Profile photo
//        mprofilePhoto = view.findViewById(R.id.layout_centre_profile_photo);
//
//        // GridView
//        mGridView = view.findViewById(R.id.layout_centre_profile_gridView);
//
//        // Toolbar
//        toolbar = view.findViewById(R.id.snippet_profile_toolbar);
//
//        // Bottom Navigation
//        bottomNavigationView = view.findViewById(R.id.bottom_navbar_view);
//    }
//
//    private void setupEditProfile(View view) {
//        mEditProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: navigating to edit profile fragment");
//                Intent intent = new Intent(mContext, AccountSettings.class);
//                // Create a key-value pair
//                // key: calling activity
//                // value: profile activity
//                // when we arrive at account setting, this will automatically direct us to edit profile
//                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile));
//                startActivity(intent);
//            }
//        });
//    }
//
//    /**
//     * Top toolbar setup
//     * @param view current view
//     */
//    private void setupToolBar(View view) {
//        ((Profile)getActivity()).setSupportActionBar(toolbar);
//
//        ImageView mSettings = view.findViewById(R.id.snippet_profile_top_toolbar_settings);
//        ImageView back = view.findViewById(R.id.snippet_profile_top_toolbar_back);
//        TextView tvUsername = view.findViewById(R.id.snippet_profile_top_toolbar_username);
//
//        // Set username on toolbar
//        String username = user.getDisplayName();
//        if (username != null) {
//            tvUsername.setText(username);}
//
//        mSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Navigating to account settings");
//                Intent intent = new Intent(mContext, AccountSettings.class);
//                startActivity(intent);
//            }
//        });
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Navigating back to feed");
//                Intent intent = new Intent(mContext, FeedActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    /**
//     * Bottom navigation bar setup
//     */
//    private void setupBottomNavigationView() {
//        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
//        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);
//
//        // To highlight the correct icon when on correct page
//        Menu menu = bottomNavigationView.getMenu();
//        MenuItem menuItem = menu.getItem(b_menu_ACTIVTY_NUM);
//        menuItem.setChecked(true);
//    }
//
//    /**
//     * Firebase setup
//     * TODO: Easier & Neater if there was a FirebaseMethods class in Utils
//     */
//    private void setupFirebase() {
//        mAuth= FirebaseAuth.getInstance();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mRef = FirebaseDatabase.getInstance().getReference();
//        user = mAuth.getCurrentUser();
//
//        // Add a listener for changes in the data at this location
//        // Each time the data changes, the listener will be called with an immutable snapshot of the data.
//        // Allows us to read/write data to database
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // retrieve user info from database
//
//                // retrieve outfit images for user
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//}
