package com.example.shibushi.Wardrobe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shibushi.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WardrobeFragmentPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WardrobeFragmentPage extends Fragment {

    private static final String ARG_PARAM1 = "param1";


    private String mParam1;
    private String mParam2;

    public WardrobeFragmentPage() {
        // Required empty public constructor
    }

    public static WardrobeFragmentPage newInstance(String param1, String param2) {
        WardrobeFragmentPage fragment = new WardrobeFragmentPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wardrobe_page, container, false);
    }
}