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
            imgs.add(new Model.Img("https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg"));
            imgs.add(new Model.Img("https://i2.jueshifan.com/7b077d83/790e7f8b/30073cc809a1a756c57d.jpg"));
            imgs.add(new Model.Img("https://testfeuer.at/img/sr-originals/10/spongebob-squarepants-theory.jpg"));
            imgs.add(new Model.Img("https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg"));
            imgs.add(new Model.Img("https://i2.jueshifan.com/7b077d83/790e7f8b/30073cc809a1a756c57d.jpg"));
            imgs.add(new Model.Img("https://img2.jiemian.com/101/original/20181128/154339834262152300_a700x398.jpeg"));
            imgs.add(new Model.Img("https://testfeuer.at/img/sr-originals/10/spongebob-squarepants-theory.jpg"));
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