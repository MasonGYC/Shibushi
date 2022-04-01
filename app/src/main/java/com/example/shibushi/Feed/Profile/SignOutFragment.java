package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shibushi.Login.Login;
import com.example.shibushi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignOutFragment extends Fragment {

    private static final String TAG = "SignOutFragment";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ProgressBar mProgressBar;
    private TextView tvSignOut;
    private TextView tvSigningOut;
    private Button buttonSignOut;
    private Button buttonCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: creating view");
        View view = inflater.inflate(R.layout.fragment_signout, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        tvSignOut = view.findViewById(R.id.fragment_signout_tvSignoutQn);
        tvSigningOut = view.findViewById(R.id.fragment_signout_tvSigningOut);
        buttonSignOut = view.findViewById(R.id.fragment_signout_ButtonSignout);
        buttonCancel = view.findViewById(R.id.fragment_signout_ButtonCancel);
        mProgressBar = view.findViewById(R.id.fragment_signout_progBar);

        mProgressBar.setVisibility(View.GONE);
        tvSigningOut.setVisibility(View.GONE);

        // button for confirm signing out
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Signing out...");
                mProgressBar.setVisibility(View.VISIBLE);
                tvSigningOut.setVisibility(View.VISIBLE);

                if (currentUser != null){
                    mAuth.signOut();
                    Toast.makeText(getActivity(), currentUser.getDisplayName()+ " is logged out!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Prevents going back to prev page when back button is pressed
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "You aren't logged in yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // button for cancelling sign out
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to Profile");
                getActivity().finish();
            }
        });

        return view;
    }

}
