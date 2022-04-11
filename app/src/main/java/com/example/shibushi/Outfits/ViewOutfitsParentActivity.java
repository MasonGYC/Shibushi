package com.example.shibushi.Outfits;

import static com.example.shibushi.Utils.FirestoreMethods.addOutfit;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ViewOutfitsParentActivity extends AppCompatActivity {
    private static final String TAG = "ViewOutfits";
    public static final String KEY_OUTFIT_CREATE = "KEY_OUTFIT_CREATE";
    public static final String  KEY_OUTFIT_CAT = "KEY_OUTFIT_CAT";
    public static final String KEY_OUTFIT_NAME = "KEY_OUTFIT_NAME";
    private Context mContext = ViewOutfitsParentActivity.this;
    public String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();;

    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 3;

    RecyclerView outfitRecyclerView;
    OutfitParentAdapter outfitParentAdapter;
    OutfitParentModel.ParentDataSource parentDataSource;
    ImageView createNewCat;
    List<OutfitChildModel.ChildDataSource> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewoutfits);

        // Set up bottom navigation bar
        setupBottomNavigationView();

        // query old and get new outfit data
        Map<String[], ArrayList<cClothing>> outfitmap = null;
        try {
            outfitmap = getNewOutfit();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (outfitmap == null || outfitmap.isEmpty()){
            parentDataSource = dataInit(); //dummy
        }
        else {
            parentDataSource = dataInit(outfitmap);
        }
        //set recycler view
        outfitRecyclerView = findViewById(R.id.outfitParentRecyclerView);
        outfitRecyclerView.setLayoutManager(new LinearLayoutManager(ViewOutfitsParentActivity.this));
        outfitParentAdapter = new OutfitParentAdapter(ViewOutfitsParentActivity.this, parentDataSource);
        outfitRecyclerView.setAdapter(outfitParentAdapter);
    }

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

    // dummy
    public OutfitParentModel.ParentDataSource dataInit(){

        // init new child datas
        List<OutfitChildModel.ChildDataSource> datas = new ArrayList<>();

        ArrayList<cClothing> cClothingList = new ArrayList<>();
        cClothing cClothing1 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing2 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing3 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing4 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");
        cClothing cClothing5 = new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f");

        cClothingList.add(cClothing1);
        cClothingList.add(cClothing2);
        cClothingList.add(cClothing3);
        cClothingList.add(cClothing4);
        cClothingList.add(cClothing5);

        ArrayList<cOutfits> cOutfitsList = new ArrayList<>();
        cOutfits cOutfits1 = new cOutfits(userID,"outfitname1","spring",cClothingList);

        cOutfits cOutfits2 =  new cOutfits(userID,"outfitname2","summer",cClothingList);
        cOutfitsList.add(cOutfits1);
        cOutfitsList.add(cOutfits2);

        OutfitChildModel.ChildDataSource childDataSource = new OutfitChildModel.ChildDataSource(cOutfitsList);
        OutfitChildModel.ChildDataSource childDataSource1 = new OutfitChildModel.ChildDataSource(cOutfitsList);
        ArrayList<OutfitChildModel.ChildDataSource> cs_list = new ArrayList<OutfitChildModel.ChildDataSource>();
        cs_list.add(childDataSource);
        cs_list.add(childDataSource1);

        OutfitParentModel.ParentDataSource parentDataSource = new OutfitParentModel.ParentDataSource(cs_list,"category");
        return parentDataSource;
    }

    // real fetch data
    public OutfitParentModel.ParentDataSource dataInit(Map<String[],ArrayList<cClothing>> clothes_cat_uris){

        // init new child datas
        List<OutfitChildModel.ChildDataSource> datas = new ArrayList<>();
        //ArrayList<cOutfits>

        // get uris for a single outfit
        ArrayList<cOutfits> data = new ArrayList<>();
        String category = "default_cat";
        String name = "default_name";
        for (Map.Entry<String[],ArrayList<cClothing>> entry: clothes_cat_uris.entrySet()){
            ArrayList<cClothing> clothings = entry.getValue();
            category = entry.getKey()[0];
            name = entry.getKey()[1];
            cOutfits outfits = new cOutfits(userID,name,category,clothings);
            data.add(outfits);
            //upload outfit
            addOutfit(outfits,outfits.getName());

        }
        datas.add(new OutfitChildModel.ChildDataSource(data));
        OutfitParentModel.ParentDataSource parentDataSource = new OutfitParentModel.ParentDataSource(datas,category);
        return parentDataSource;
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

    public Map<String[],ArrayList<cClothing>> getNewOutfit() throws MalformedURLException, URISyntaxException {
        //to get intent data to retrieve new created single outfit

        Map<String[],ArrayList<cClothing>> map = new HashMap<>(); //downloaded processed data from firebase
        //default values
        ArrayList<cClothing> array_clothings = new ArrayList<>();
        array_clothings.add(new cClothing("userID", "Shirt", "red", "Formal", "XS", "7bd53aaf-7ecd-4f7a-b5cb-a91d3115d717", "com.google.android.gms.tasks.zzw@3971c6f"));
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
    public Map<String[], ArrayList<String>> getOldOutfit(Map<String[], ArrayList<String>> new_map){

        return new_map;
    }

}
