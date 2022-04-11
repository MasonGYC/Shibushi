package com.example.shibushi.Outfits;

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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewOutfitsParentActivity extends AppCompatActivity {
    private static final String TAG = "ViewOutfits";
    public static final String KEY_OUTFIT_CAT = "KEY_OUTFIT_CAT";
    public static final String KEY_OUTFIT_URIS = "KEY_OUTFIT_URIS";
    public static final String KEY_OUTFIT_NAME = "KEY_OUTFIT_NAME";
    private Context mContext = ViewOutfitsParentActivity.this;
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
        Map<String[], ArrayList<Uri>> outfitmap = null;
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

        Uri uri_one = getUriToDrawable(mContext,R.drawable.ic_launcher_background);
        Uri uri_two = getUriToDrawable(mContext,R.drawable.ic_launcher_foreground);
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(uri_one);
        uris.add(uri_two);
        OutfitChildModel.SingleOutfit singleOutfit = new OutfitChildModel.SingleOutfit(uri_two,"name1",uris);
        List<OutfitChildModel.SingleOutfit> data = new ArrayList<>();
        data.add(singleOutfit);
        data.add(singleOutfit);
        data.add(singleOutfit);

        OutfitChildModel.SingleOutfit singleOutfit1 = new OutfitChildModel.SingleOutfit(uri_one,"name2",uris);
        List<OutfitChildModel.SingleOutfit> data1 = new ArrayList<>();
        data1.add(singleOutfit1);
        data1.add(singleOutfit1);
        data1.add(singleOutfit1);

        OutfitChildModel.ChildDataSource childDataSource = new OutfitChildModel.ChildDataSource(data);
        OutfitChildModel.ChildDataSource childDataSource1 = new OutfitChildModel.ChildDataSource(data1);

        datas.add(childDataSource);
        datas.add(childDataSource1);
        OutfitParentModel.ParentDataSource parentDataSource = new OutfitParentModel.ParentDataSource(datas,"category");
        return parentDataSource;
    }

    // real fetch data
    public OutfitParentModel.ParentDataSource dataInit(Map<String[],ArrayList<Uri>> clothes_cat_uris){

        // init new child datas
        List<OutfitChildModel.ChildDataSource> datas = new ArrayList<>();

        // get uris for a single outfit
        List<OutfitChildModel.SingleOutfit> data = new ArrayList<>();
        String cat = "default";
        String name;
        for (Map.Entry<String[],ArrayList<Uri>> entry: clothes_cat_uris.entrySet()){
            ArrayList<Uri> uris = entry.getValue();
            cat = entry.getKey()[0];
            name = entry.getKey()[1];
            data.add(new OutfitChildModel.SingleOutfit(uris.get(0),name,uris));//cover image only

        }
        datas.add(new OutfitChildModel.ChildDataSource(data));
        OutfitParentModel.ParentDataSource parentDataSource = new OutfitParentModel.ParentDataSource(datas,cat);
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

    public Map<String[], ArrayList<Uri>> getNewOutfit() throws MalformedURLException, URISyntaxException {
        //to get intent data to retrieve new created single outfit

        Map<String[],ArrayList<Uri>> map = new HashMap<>(); //downloaded processed data from firebase

        Intent intent = getIntent();
        // get image uris and category name
        if (intent.getExtras() != null){
            Bundle new_map = intent.getExtras();
            String category = (String) new_map.get(KEY_OUTFIT_CAT);
            String name = (String) new_map.get(KEY_OUTFIT_NAME);
            ArrayList<String > image_uri_strings = (ArrayList<String>) new_map.get(KEY_OUTFIT_URIS);
            ArrayList<Uri> image_uris = new ArrayList<>();
            // chaneg string to url
            for (String s:image_uri_strings){
                Uri uri = Uri.parse(new URL(s).toString());
                image_uris.add(uri);
                Log.i("getNewOutfit",uri.toString());
            }
            String[] cat_name = {category,name};
            map.put(cat_name,image_uris);
            Log.i(TAG,map.toString());
        }
        else {
            Log.i(TAG,"No outfit created");
        }

        return map;
    }
    public Map<String[], ArrayList<Uri>> getOldOutfit(Map<String[], ArrayList<Uri>> new_map){

        return new_map;
    }

}
