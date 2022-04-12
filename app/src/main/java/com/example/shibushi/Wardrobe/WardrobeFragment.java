package com.example.shibushi.Wardrobe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shibushi.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WardrobeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WardrobeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
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

//            ArrayList<String> myClothes = FirestoreMethods.getmyClothes(FirestoreMethods.userID);
//            ArrayList<String> myClothesUrl = new ArrayList<>();
//            for (String i:myClothes){
//                myClothesUrl.add(FirestoreMethods.getDownloadUrlString(i));
//            }
            ArrayList<Model.Img> imgs = new ArrayList<>();
//
//            for (String i:myClothesUrl){
//                imgs.add(new Model.Img(i));
//            }
//
//            imgs.add(new Model.Img("https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg"));
//            imgs.add(new Model.Img("https://testfeuer.at/img/sr-originals/10/spongebob-squarepants-theory.jpg"));
//            imgs.add(new Model.Img("https://img2.jiemian.com/101/original/20181128/154339834262152300_a700x398.jpeg"));
//            imgs.add(new Model.Img("https://i2.jueshifan.com/7b077d83/790e7f8b/30073cc809a1a756c57d.jpg"));
//            imgs.add(new Model.Img("https://testfeuer.at/img/sr-originals/10/spongebob-squarepants-theory.jpg"));
//            imgs.add(new Model.Img("https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg"));
//            imgs.add(new Model.Img("https://img2.jiemian.com/101/original/20181128/154339834262152300_a700x398.jpeg"));
//            imgs.add(new Model.Img("https://i2.jueshifan.com/7b077d83/790e7f8b/30073cc809a1a756c57d.jpg"));
            imgs.add(new Model.Img("https://i.pinimg.com/236x/a4/67/aa/a467aae2b7aeb08a8bd01a0a528b51e2.jpg"));
            imgs.add(new Model.Img("https://i.pinimg.com/236x/2e/41/47/2e4147c28c008ad1f1c581c1c503f7fb.jpg"));
            imgs.add(new Model.Img("https://i.pinimg.com/236x/d2/ec/af/d2ecaf3155d4baddb8e5f5554f064f8b.jpg"));
            imgs.add(new Model.Img("https://i.pinimg.com/236x/e2/5b/e9/e25be9e39741d352c3140d4552a139d1.jpg"));
            imgs.add(new Model.Img("https://i.pinimg.com/236x/fb/0a/72/fb0a72338f43142f4377f259016d14d3.jpg"));
            imgs.add(new Model.Img("https://i.pinimg.com/236x/1c/d9/2a/1cd92aad4f82ed2f1916f86695f63433.jpg"));
            imgs.add(new Model.Img("https://i.pinimg.com/236x/39/fa/af/39faafa68c09e7ff8748b48a59319140.jpg"));
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
            final Container<Integer> width_container = new Container<>();
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    if (recyclerView.getWidth() != 0) {
                        Container.w = recyclerView.getWidth();
                        Log.i("Wardrobe fragment width", String.valueOf(width_container.get()));
                    }
                    imageAdapter = new imageAdapter(recyclerView.getContext(), dataSource, Container.w);
                    recyclerView.setAdapter(imageAdapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
                    recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
//                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                }
            });

//            int recyclerViewWidth = rootView.getMeasuredWidth();
//            Log.i("WIDTH",String.valueOf(recyclerViewWidth));
//            imageAdapter = new imageAdapter(this.getContext(), dataSource, recyclerViewWidth);
//
//            recyclerView.setAdapter(imageAdapter);
//            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
        return rootView;
    }

    static class Container<T>{
        static int w;
        T value;
        Container(){this.value=null;}
        void set(T x){this.value=x;}
        T get(){return this.value;}
    }
}