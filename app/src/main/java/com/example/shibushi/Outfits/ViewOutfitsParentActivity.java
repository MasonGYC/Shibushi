package com.example.shibushi.Outfits;

import com.example.shibushi.Models.cWardrobe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.Outfits.Adapters.OutfitChildModel;
import com.example.shibushi.Outfits.Adapters.OutfitParentAdapter;
import com.example.shibushi.Outfits.Adapters.OutfitParentModel;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Models.cOutfits;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ViewOutfitsParentActivity extends AppCompatActivity {
    private static final String TAG = "ViewOutfits";
    public static final String KEY_OUTFIT_CREATE = "KEY_OUTFIT_CREATE";
    public static final String  KEY_OUTFIT_CAT = "KEY_OUTFIT_CAT";
    public static final String KEY_OUTFIT_NAME = "KEY_OUTFIT_NAME";
    private final Context mContext = ViewOutfitsParentActivity.this;
    public String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private static final FirebaseFirestore mFirestoreDB = FirebaseFirestore.getInstance();

    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 2;

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
        myOutfits.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // for each clothing as a document, put as a map
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> map = document.getData();
                    outfits_map.add(map);
                }
                Map<String, ArrayList<cOutfits>> cate_map = new HashMap<>();
                //For each clothing document
                for (Map<String,Object> outfit : outfits_map){
                    String outfitID = Objects.requireNonNull(outfit.get("outfitID")).toString();
                    Timestamp timeStamp = (Timestamp) outfit.get("timeStamp");
                    String userID = Objects.requireNonNull(outfit.get("userID")).toString();
                    String outfit_name = Objects.requireNonNull(outfit.get("name")).toString();
                    String category = Objects.requireNonNull(outfit.get("category")).toString();
                    ArrayList<String> names =  (ArrayList<String>) outfit.get("img_names");

                    cOutfits new_outfit =new cOutfits(outfitID, timeStamp, userID, outfit_name, category, names);

                    // put into category map
                    ArrayList<cOutfits> temp;
                    if (cate_map.containsKey(category)){
                        temp = cate_map.get(category);
                        assert temp != null;
                    }
                    else {
                        temp = new ArrayList<>();
                    }
                    temp.add(new_outfit);
                    cate_map.put(category,temp);
                }

                // for new single outfit
                for (Map.Entry<String[],ArrayList<cClothing>> entry: outfitmap.entrySet()){
                    ArrayList<cClothing> clothings = entry.getValue();
                    String category = entry.getKey()[0];
                    String name = entry.getKey()[1];
                    if (name.equals("default_name")){
                        break;
                    }
                    cOutfits outfit = new cOutfits(userID,name,category,clothings);
                    // put into category map
                    ArrayList<cOutfits> temp;
                    if (cate_map.containsKey(category)){
                        temp = cate_map.get(category);
                        assert temp != null;
                    }
                    else {
                        temp = new ArrayList<>();
                    }
                    temp.add(outfit);
                    cate_map.put(category,temp);

                    //upload outfit to firebase
                    new cWardrobe().addOutfit(userID, name, category, clothings);
                }
                for (Map.Entry<String,ArrayList<cOutfits>> entry: cate_map.entrySet()){
                    all_outfit_data.add(new OutfitChildModel.ChildDataSource(entry.getValue(), entry.getKey()));
                }

                parentDataSource = new OutfitParentModel.ParentDataSource(all_outfit_data);

                //set recycler view
                outfitParentAdapter = new OutfitParentAdapter(ViewOutfitsParentActivity.this, parentDataSource);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
            outfitRecyclerView = findViewById(R.id.outfitParentRecyclerView);
            outfitRecyclerView.setLayoutManager(new LinearLayoutManager(ViewOutfitsParentActivity.this));
            outfitRecyclerView.setAdapter(outfitParentAdapter);
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
    // Create new outfit
    public Map<String[],ArrayList<cClothing>> getNewOutfit(){
        //to get intent data to retrieve new created single outfit
        Map<String[],ArrayList<cClothing>> map = new HashMap<>(); //downloaded processed data from firebase
        ArrayList<cClothing> array_clothings = new ArrayList<>();
        String category = "default_cat";
        String name = "default_name";
        Intent intent = getIntent();
        // get image uris and category name
        if (intent.getSerializableExtra(KEY_OUTFIT_CREATE) != null){
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