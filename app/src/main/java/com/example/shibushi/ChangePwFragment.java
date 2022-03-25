package com.example.shibushi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shibushi.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChangePwFragment extends Fragment {
    private static final String TAG = "ChangePwFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starting");

        View view = inflater.inflate(R.layout.fragment_change_pw, container, false);

        //setup back arrow to profile
        ImageView back = view.findViewById(R.id.snippet_change_pw_toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to Profile");
                getActivity().finish();
            }
        });

        return view;
    }

}
