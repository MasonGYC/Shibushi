package com.example.shibushi.Outfits;

import static com.example.shibushi.Utils.FirestoreMethods.getAllOutfits;

import com.example.shibushi.Models.cWardrobe;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Models.cOutfits;
import com.example.shibushi.Utils.FirestoreMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewOutfitsParentActivity extends AppCompatActivity {
    private static final String TAG = "ViewOutfits";
    public static final String KEY_OUTFIT_CREATE = "KEY_OUTFIT_CREATE";
    public static final String  KEY_OUTFIT_CAT = "KEY_OUTFIT_CAT";
    public static final String KEY_OUTFIT_NAME = "KEY_OUTFIT_NAME";
    private Context mContext = ViewOutfitsParentActivity.this;
    public String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();;
    private static final FirebaseFirestore mFirestoreDB = FirebaseFirestore.getInstance();

    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 3;

    RecyclerView outfitRecyclerView;
    OutfitParentAdapter outfitParentAdapter;
    OutfitParentModel.ParentDataSource parentDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewoutfits);

        // Set up bottom navigation bar
        setupBottomNavigationView();

        // query old and get new outfit data
        final Map<String[], ArrayList<cClothing>> outfitmap = getNewOutfit();

        // init new ArrayList<cOutfits>
        List<OutfitChildModel.ChildDataSource> all_outfit_data = new ArrayList<>();
        // query old outfits
        ArrayList<Map<String, Object>> outfits_map = new ArrayList<>();

        CollectionReference outfitRef = mFirestoreDB.collection("cOutfits");
        Query myOutfits = outfitRef.whereEqualTo("userID", userID);
        myOutfits.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // for each clothing as a document, put as a map
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();
                        outfits_map.add(map);
                    }
                    Map<String, ArrayList<cOutfits>> cate_map = new HashMap<>();
                    //For each clothing document
                    for (Map<String,Object> outfit : outfits_map){
                        String outfitID = outfit.get("outfitID").toString();
                        Timestamp timeStamp = (Timestamp) outfit.get("timeStamp");
                        String userID = outfit.get("userID").toString();
                        String outfit_name = outfit.get("name").toString();
                        String category = outfit.get("category").toString();
                        ArrayList<String> names =  (ArrayList<String>) outfit.get("img_names");

                        cOutfits new_outfit =new cOutfits(outfitID, timeStamp, userID, outfit_name, category, names);

                        // put into category map
                        if (cate_map.containsKey(category)){
                            ArrayList<cOutfits> temp = cate_map.get(category);
                            temp.add(new_outfit);
                            cate_map.put(category,temp);
                        }
                        else {
                            ArrayList<cOutfits> temp = new ArrayList<>();
                            temp.add(new_outfit);
                            cate_map.put(category,temp);
                        }
                    }

                    // for new single outfit
                    String category = "default_cat";
                    String name = "default_name";
                    for (Map.Entry<String[],ArrayList<cClothing>> entry: outfitmap.entrySet()){
                        ArrayList<cClothing> clothings = entry.getValue();
                        category = entry.getKey()[0];
                        name = entry.getKey()[1];
                        if (name.equals("default_name")){
                            continue;
                        }
                        cOutfits outfit = new cOutfits(userID,name,category,clothings);
                        // put into category map
                        if (cate_map.containsKey(category)){
                            ArrayList<cOutfits> temp = cate_map.get(category);
                            temp.add(outfit);
                            cate_map.put(category,temp);
                        }
                        else {
                            ArrayList<cOutfits> temp = new ArrayList<>();
                            temp.add(outfit);
                            cate_map.put(category,temp);
                        }

                        //upload outfit to firebase
                        ArrayList<String> img_names = new ArrayList<>();
                        for (cClothing clothing:clothings){
                            img_names.add(clothing.getImg_name());
                        }
                        new cWardrobe().addOutfit(userID, name, category, clothings);
                    }
                    for (Map.Entry<String,ArrayList<cOutfits>> entry: cate_map.entrySet()){
                        all_outfit_data.add(new OutfitChildModel.ChildDataSource(entry.getValue(), entry.getKey()));
                    }

                    parentDataSource = new OutfitParentModel.ParentDataSource(all_outfit_data);

                    //set recycler view
                    outfitRecyclerView = findViewById(R.id.outfitParentRecyclerView);
                    outfitRecyclerView.setLayoutManager(new LinearLayoutManager(ViewOutfitsParentActivity.this));
                    outfitParentAdapter = new OutfitParentAdapter(ViewOutfitsParentActivity.this, parentDataSource);
                    outfitRecyclerView.setAdapter(outfitParentAdapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
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

    public Map<String[],ArrayList<cClothing>> getNewOutfit(){
        //to get intent data to retrieve new created single outfit

        Map<String[],ArrayList<cClothing>> map = new HashMap<>(); //downloaded processed data from firebase
        //default values
        ArrayList<cClothing> array_clothings = new ArrayList<>();
//        array_clothings.add(new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f"));
        String category = "default_cat";
        String name = "default_name";
        Intent intent = getIntent();
        // get image uris and category name
        if (intent.getSerializableExtra(KEY_OUTFIT_CREATE) != null){
            array_clothings.clear();//clear default
            array_clothings = (ArrayList<cClothing>) intent.getSerializableExtra(KEY_OUTFIT_CREATE);}
        else {
            Log.i(TAG,"No outfit created");
        }
        if (intent.getStringExtra(KEY_OUTFIT_CAT)!=null){
            category = (String) intent.getStringExtra(KEY_OUTFIT_CAT);}
        if (intent.getStringExtra(KEY_OUTFIT_NAME)!=null){
            name = (String)intent.getStringExtra(KEY_OUTFIT_NAME);}

            String[] cat_name = {category,name};
            map.put(cat_name,array_clothings);
            Log.i(TAG,map.toString());

        return map;
    }
}
