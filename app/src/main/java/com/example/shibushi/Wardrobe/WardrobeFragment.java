package com.example.shibushi.Wardrobe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shibushi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WardrobeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WardrobeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";


    private ArrayList<String> imgsArray;
    View rootView;

    public WardrobeFragment() {
        // Required empty public constructor
    }

    public static WardrobeFragment newInstance(ArrayList<String> url) {
        WardrobeFragment fragment = new WardrobeFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgsArray = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView==null){
            rootView = inflater.inflate(R.layout.fragment_wardrobe_page, container, false);
        }
        initView();
        return rootView;

    }

    private void initView() {
        TextView textView = rootView.findViewById(R.id.for_test);
        // todo: set the fragment

    }
}