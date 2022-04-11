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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewOutfitsParentActivity extends AppCompatActivity {
    private static final String TAG = "ViewOutfits";
    private Context mContext = ViewOutfitsParentActivity.this;
    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 3;
    RecyclerView outfitRecyclerView;
    OutfitParentAdapter outfitParentAdapter;
    OutfitParentModel.ParentDataSource parentDataSource;
    ImageView createNewCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewoutfits);

        // Set up bottom navigation bar
        setupBottomNavigationView();

        // todo: query old and get new outfit data
        Intent intent = getIntent();


        // create new category
//        createNewCat = findViewById(R.id.snippet_viewoutfit_toolbar_create);
//        createNewCat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent createNew = new Intent(ViewOutfitsParentActivity.this,CreateNewCateOutfit.class);
//                startActivity(createNew);
//            }
//        });

        // receive created new cat
        //String[] created = getIntent().getStringArrayExtra(CreateNewCateOutfit.CREATED_CAT);


        //set recycler view
        List<OutfitChildModel.ChildDataSource> datas = dataInit();
        outfitRecyclerView = findViewById(R.id.outfitParentRecyclerView);
        outfitRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        outfitParentAdapter = new OutfitParentAdapter(this, datas);
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
    public List<OutfitChildModel.ChildDataSource> dataInit(){

        // init new child datas
        List<OutfitChildModel.ChildDataSource> datas = new ArrayList<>();

        // get uris for a single outfit
//        for (Uri clothe: clothes_cat_uris){
//            OutfitChildModel.SingleOutfit singleOutfit = new OutfitChildModel.SingleOutfit(clothe,"Cat1");
//        }
        Uri uri_one = getUriToDrawable(mContext,R.drawable.ic_launcher_background);
        Uri uri_two = getUriToDrawable(mContext,R.drawable.ic_launcher_foreground);

        OutfitChildModel.SingleOutfit singleOutfit = new OutfitChildModel.SingleOutfit(uri_two,"Cat1");
        List<OutfitChildModel.SingleOutfit> data = new ArrayList<>();
        data.add(singleOutfit);
        data.add(singleOutfit);
        data.add(singleOutfit);

        OutfitChildModel.SingleOutfit singleOutfit1 = new OutfitChildModel.SingleOutfit(uri_one,"Cat2");
        List<OutfitChildModel.SingleOutfit> data1 = new ArrayList<>();
        data1.add(singleOutfit1);
        data1.add(singleOutfit1);
        data1.add(singleOutfit1);

        OutfitChildModel.ChildDataSource childDataSource = new OutfitChildModel.ChildDataSource(data);
        OutfitChildModel.ChildDataSource childDataSource1 = new OutfitChildModel.ChildDataSource(data1);

        datas.add(childDataSource);
        datas.add(childDataSource1);
        parentDataSource = new OutfitParentModel.ParentDataSource(datas);
        return datas;
    }

    // real fetch data
    public List<OutfitChildModel.ChildDataSource> dataInit(Map<String,ArrayList<Uri>> clothes_cat_uris){

        // init new child datas
        List<OutfitChildModel.ChildDataSource> datas = new ArrayList<>();

        // get uris for a single outfit

        for (Map.Entry<String,ArrayList<Uri>> entry: clothes_cat_uris.entrySet()){
            List<OutfitChildModel.SingleOutfit> data = new ArrayList<>();
            ArrayList<Uri> uris = entry.getValue();
            String cat = entry.getKey();
            for (Uri uri: uris){
                data.add(new OutfitChildModel.SingleOutfit(uri,cat));
            }
            datas.add(new OutfitChildModel.ChildDataSource(data));
        }
        //parentDataSource = new OutfitParentModel.ParentDataSource(datas);
        return datas;
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

}
