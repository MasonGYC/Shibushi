package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shibushi.Login.Login;
import com.example.shibushi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignOutActivity extends AppCompatActivity {

    private static final String TAG = "SignOutActivity";
    private final Context mContext = SignOutActivity.this;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ProgressBar mProgressBar;
    private TextView tvSignOut;
    private TextView tvSigningOut;
    private Button buttonSignOut;
    private Button buttonCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signout);
        Log.d(TAG, "onCreate: starting...");

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Widgets setup
        tvSignOut = findViewById(R.id.activity_signout_tvSignoutQn);
        tvSigningOut = findViewById(R.id.activity_signout_tvSigningOut);
        buttonSignOut = findViewById(R.id.activity_signout_ButtonSignout);
        buttonCancel = findViewById(R.id.activity_signout_ButtonCancel);
        mProgressBar = findViewById(R.id.activity_signout_progBar);

        mProgressBar.setVisibility(View.GONE);
        tvSigningOut.setVisibility(View.GONE);

        setupSignOutBUTTON();
        setupCancelBUTTON();
    }

    /**
     * Button for confirming sign out
     */
    private void setupSignOutBUTTON() {
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Signing out...");
                mProgressBar.setVisibility(View.VISIBLE);
                tvSigningOut.setVisibility(View.VISIBLE);

                if (currentUser != null){
                    mAuth.signOut();
                    Toast.makeText(mContext, currentUser.getDisplayName()+ " is logged out!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Prevents going back to prev page when back button is pressed
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext, "You aren't logged in yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Button for cancelling sign out
     */
    private void setupCancelBUTTON() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to Account Settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Prevents going back to prev page when back button is pressed
                startActivity(intent);
            }
        });
    }
}
