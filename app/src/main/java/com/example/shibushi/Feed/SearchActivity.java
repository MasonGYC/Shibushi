package com.example.shibushi.Feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cUsers;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Utils.Feed.cUsersAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchActivity extends AppCompatActivity implements cUsersAdapter.UserClickListener {
    private static final String TAG = "SearchActivity";
    private static final int b_menu_ACTIVTY_NUM = 0;
    private Context mContext = SearchActivity.this;

    // Widgets
    private EditText etSearch;

    // Variables
    private cUsersAdapter mAdapter;

    // Firestore
    private final FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "onCreate: started");
        this.setTitle("Search Users");

        etSearch = findViewById(R.id.snippet_seachbar_etSearch);
        RecyclerView recyclerView = findViewById(R.id.activity_search_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNavigationView(); //Setup bottom navigation bar

        // Search stuffs
        Query query = mDatabase.collection("cUsers");
        FirestoreRecyclerOptions<cUsers> options = new FirestoreRecyclerOptions.Builder<cUsers>()
                .setQuery(query, cUsers.class)
                .build();
        mAdapter = new cUsersAdapter(options, this::onSelectedUser);
        recyclerView.setAdapter(mAdapter);

        // Listens for any change in edit text field
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable keyword) {
                Log.d(TAG, "onCreate: Searchbox has been changed to " + keyword.toString());
                Query query;
                if (keyword.toString().isEmpty()) {
                    query = mDatabase.collection("cUsers");
                } else {
                    query = mDatabase.collection("cUsers")
                            .whereEqualTo("username", keyword.toString());
                }
                FirestoreRecyclerOptions<cUsers> options = new FirestoreRecyclerOptions.Builder<cUsers>()
                        .setQuery(query, cUsers.class)
                        .build();
                mAdapter.updateOptions(options);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * When we select a user, navigate to their profile page
     * @param user
     */
    @Override
    public void onSelectedUser(cUsers user) {
        Intent intent = new Intent(mContext, SelectedUserActivity.class).putExtra("data", user);
        startActivity(intent);
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
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

}
