package com.example.shibushi.Wardrobe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;
import com.example.shibushi.Utils.Wardrobe.WardrobeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WardrobeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    View rootView;
    private Model.DataSource dataSource;
    private RecyclerView recyclerView;
    private com.example.shibushi.Utils.Wardrobe.WardrobeAdapter WardrobeAdapter;
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final FirebaseFirestore mFirestoreDB = FirebaseFirestore.getInstance();
    private static final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    private final HashMap<String, cClothing> wardrobeClothing = new HashMap<>();
    private final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
    private final String type;


    public WardrobeFragment(String type) {
        this.type = type;
    }

    public static WardrobeFragment newInstance(ArrayList<String> urls, String type) {
        // type: Top/Bottom/Accessories
        WardrobeFragment fragment = new WardrobeFragment(type);
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, urls);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView==null){
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.fragment_wardrobe_page, container, false);
            recyclerView = rootView.findViewById(R.id.wd_fragment_recycler);
            recyclerView.post(() -> {
                // get the size of the recyclerview
                if (recyclerView.getWidth() != 0) {
                    Container.w = recyclerView.getWidth();
                    Log.i("Wardrobe fragment width", String.valueOf(Container.w));
                }

                CollectionReference clothesRef = mFirestoreDB.collection("cClothes");
                Query myClothes = clothesRef.whereEqualTo("userid", userID);
                myClothes.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        // query images and set recyclerview adapter
                        for (QueryDocumentSnapshot document: task.getResult()){
                            Map<String, Object> map = document.getData();
                            String img_name = Objects.requireNonNull(map.get("img_name")).toString();
                            if (!wardrobeClothing.containsKey(img_name)){
                                cClothing c1 = new cClothing(map, img_name);
                                wardrobeClothing.put(img_name,c1);
                            }
                        }
                        Set<String> clothes = wardrobeClothing.keySet();
                        ArrayList<Model.Img> images = new ArrayList<>();
                        ArrayList<String> image_name = new ArrayList<>();

                        for (String c: clothes){
                            cClothing cloth = wardrobeClothing.get(c);
                            assert cloth != null;
                            if (!cloth.getCategory().equals(type)){
                                continue;
                            }
                            mStorageReference.child("images").child(cloth.getImg_name()).getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        images.add(new Model.Img(uri.toString()));
                                        image_name.add(cloth.getImg_name());
                                        dataSource = new Model.DataSource(images, image_name);
                                        Log.i("Wardrobe", "get "+ dataSource.count() +" images");
                                        WardrobeAdapter = new WardrobeAdapter(recyclerView.getContext(), dataSource, Container.w);
                                        recyclerView.setAdapter(WardrobeAdapter);
                                        // set GridLayoutManager to show content in two columns
                                        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
                                    });
                        }
                    }
                });
            });
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