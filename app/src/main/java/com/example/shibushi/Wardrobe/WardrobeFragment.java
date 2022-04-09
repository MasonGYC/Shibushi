package com.example.shibushi.Wardrobe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private Model.DataSource dataSource;
    private RecyclerView recyclerView;
    private imageAdapter imageAdapter;

    public WardrobeFragment() {
        // Required empty public constructor
    }

    public static WardrobeFragment newInstance(ArrayList<String> urls) {
        WardrobeFragment fragment = new WardrobeFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, urls);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgsArray = getArguments().getStringArrayList(ARG_PARAM1);

            // add imgs to recyclerView
            ArrayList<Model.Img> imgs = new ArrayList<>();
            imgs.add(new Model.Img("https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg"));
            imgs.add(new Model.Img("https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg"));
            dataSource = new Model.DataSource(imgs);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView==null){
            rootView = inflater.inflate(R.layout.fragment_wardrobe_page, container, false);

            recyclerView = rootView.findViewById(R.id.wd_fragment_recycler);
            imageAdapter = new imageAdapter(this.getContext(), dataSource);
            recyclerView.setAdapter(imageAdapter);
//            recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        }
        initView();
        return rootView;

    }

    private void initView() {
        TextView textView = rootView.findViewById(R.id.for_test);
        // todo: set the fragment

    }
}