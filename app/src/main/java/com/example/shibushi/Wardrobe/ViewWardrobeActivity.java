package com.example.shibushi.Wardrobe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shibushi.Feed.FeedActivity;
import com.example.shibushi.Models.cClothing;
import com.example.shibushi.Outfits.ViewOutfitsParentActivity;
import com.example.shibushi.PhotoProcess.CropActivity;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Utils.FirestoreMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ViewWardrobeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "ViewWardrobe";
    final private Context mContext = ViewWardrobeActivity.this;
    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 1;

    // Left sidebar
    ImageView side_bar_clothes;
    ImageView side_bar_bottoms;
    ImageView side_bar_others;
    ImageView current_view;

    // Create outfit
    Button create_outfit_button;
    Button create_cancel_button;
    TextView create_outfit_name;
    TextView create_outfit_category;
    public static boolean isChoosing = false;

    // Top Toolbar
    ImageView basket;
    ImageView delete_cloth_button;
    ImageView add_new_clothing;

    // Alert Dialog for deleting clothes
    AlertDialog dialog;

    // Fragment & ViewPage
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wardrobe);

        isChoosing = false;
        // Set up bottom navigation bar
        setupBottomNavigationView();
        setupAddImportNewClothingButton();
        initDeleteClothFunction();
        initCreateOutfitFunction();
        initPage();
        initTabView();
    }

    private void initTabView(){
        side_bar_clothes = findViewById(R.id.id_wd_clothes);
        side_bar_clothes.setOnClickListener(this);
        side_bar_bottoms = findViewById(R.id.id_wd_bottoms);
        side_bar_bottoms.setOnClickListener(this);
        side_bar_others = findViewById(R.id.id_wd_others);
        side_bar_others.setOnClickListener(this);
        current_view = side_bar_clothes;
        changeTab(R.id.id_wd_clothes);
    }

    private void initPage() {
        viewPager = findViewById(R.id.vp_wardrobe);
        viewPager.setUserInputEnabled(false);

        ArrayList<String> clothes = new ArrayList<>();
        ArrayList<String> bottoms = new ArrayList<>();
        ArrayList<String> accessories = new ArrayList<>();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(WardrobeFragment.newInstance(clothes, "Top"));    // clothes
        fragments.add(WardrobeFragment.newInstance(bottoms, "Bottom"));    // bottoms
        fragments.add(WardrobeFragment.newInstance(accessories, "Accessories"));    // accessories
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(pageAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    /*
    * Change current fragment
    * Highlight the corresponding fragment icon
    * */
    private void changeTab(int position) {
        current_view.setBackgroundColor(Color.parseColor("#eeeeee"));
        current_view.setSelected(false);
        switch (position){
            case R.id.id_wd_clothes:
                viewPager.setCurrentItem(0);
                current_view = side_bar_clothes;
                side_bar_clothes.setBackgroundColor(Color.parseColor("#FFFFFF"));
                side_bar_clothes.setSelected(true);
            case 0: // R.id.id_wd_clothes
                break;
            case R.id.id_wd_bottoms:
                viewPager.setCurrentItem(1);
                current_view = side_bar_bottoms;
                side_bar_bottoms.setBackgroundColor(Color.parseColor("#FFFFFF"));
                side_bar_bottoms.setSelected(true);
            case 1: // R.id.id_wd_clothes
                break;
            case R.id.id_wd_others:
                viewPager.setCurrentItem(2);
                current_view = side_bar_others;
                side_bar_others.setBackgroundColor(Color.parseColor("#FFFFFF"));
                side_bar_others.setSelected(true);
            case 2: // R.id.id_wd_clothes
                break;
        }
    }

    /**
     * Setup import clothing button
     */
    private void setupAddImportNewClothingButton() {
        add_new_clothing = findViewById(R.id.wardrobe_add);
        add_new_clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Please select an image from gallery", Toast.LENGTH_SHORT).show();
                importClothing();
            }
        });
    }

    /**
     * Import Clothing from user's gallery
     */
    public void importClothing() {
        // Defining Implicit Intent to mobile gallery
        Intent selectIntent = new Intent(mContext, CropActivity.class);
        selectIntent.putExtra("startingClass", ViewWardrobeActivity.TAG);
        startActivity(selectIntent);
    }

    /**
     * Bottom Navigation Bar setup
     */
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

    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }

    @Override
    public void onDestroy(){
        if (dialog!=null){
            // destroy dialog
            dialog.cancel();
        }
        super.onDestroy();
    }

    // Whether it's choosing
    public void setState(boolean b) {
        isChoosing = b;
        if (b) {
            create_outfit_name.setVisibility(View.VISIBLE);
            create_outfit_category.setVisibility(View.VISIBLE);
            create_outfit_button.setVisibility(View.VISIBLE);
            create_cancel_button.setVisibility(View.VISIBLE);
        } else {
            create_outfit_name.setVisibility(View.GONE);
            create_outfit_category.setVisibility(View.GONE);
            create_outfit_button.setVisibility(View.GONE);
            create_cancel_button.setVisibility(View.GONE);
        }
    }

    public void initDeleteClothFunction(){
        delete_cloth_button = findViewById(R.id.wardrobe_delete);
        delete_cloth_button.setOnClickListener(view -> {
            if (imageAdapter.selectedItems.size()!=0){
                if (dialog==null) {
                    LayoutInflater inflater = LayoutInflater.from(getApplication());
                    View view_pop_out = inflater.inflate(R.layout.pop_out_delete_clothes, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewWardrobeActivity.this);
                    builder.setView(view_pop_out);
                    dialog = builder.create();
                    dialog.show();
                    Button cancel_delete = dialog.findViewById(R.id.cancel_delete_cloth);
                    Button confirm_delete = dialog.findViewById(R.id.confirm_delete_cloth);

                    assert cancel_delete != null;
                    cancel_delete.setOnClickListener(view1 -> dialog.hide());
                    assert confirm_delete != null;
                    confirm_delete.setOnClickListener(view12 -> {
                        for (cClothing c:imageAdapter.selectedItems){
                            String img_name = c.getImg_name();
                            FirestoreMethods.deleteClothes(img_name);
                        }
                        dialog.hide();
                        Toast.makeText(this.mContext,"Clothes deleted from my wardrobe and all associated outfits.",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(mContext, ViewWardrobeActivity.class));
                    });
                }
                else {
                    // show dialog alert
                    dialog.show();
                }
            }
        });
    }

    public void initCreateOutfitFunction(){
        // outfit button
        create_outfit_button = findViewById(R.id.buttonCreateOutfit);
        create_outfit_name = findViewById(R.id.create_outfit_name);
        create_outfit_category = findViewById(R.id.create_outfit_category);
        create_outfit_button.setOnClickListener(view -> {
            // init intent
            if (create_outfit_name.getText().toString().equals("")){
                create_outfit_name.setError("outfit name cannot be empty!");
                create_outfit_name.requestFocus();
            }
            else if (create_outfit_category.getText().toString().equals("")){
                create_outfit_category.setError("outfit category cannot be empty!");
                create_outfit_name.requestFocus();
            }
            else {
                Intent intent = new Intent(mContext, ViewOutfitsParentActivity.class);
                String category = create_outfit_category.getText().toString();
                String name = create_outfit_name.getText().toString();
                isChoosing = false;
                // process data
                intent.putExtra(ViewOutfitsParentActivity.KEY_OUTFIT_CREATE, imageAdapter.selectedItems);
                intent.putExtra(ViewOutfitsParentActivity.KEY_OUTFIT_CAT, category);
                intent.putExtra(ViewOutfitsParentActivity.KEY_OUTFIT_NAME, name);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        // cancel button
        create_cancel_button = findViewById(R.id.buttonCancelCreate);
        create_cancel_button.setOnClickListener(view -> {
            isChoosing = false;
            setState(isChoosing);
        });

        // basket icon
        basket = findViewById(R.id.wardrobe_basket);
        basket.setOnClickListener(view -> {
            isChoosing = true;
            setState(isChoosing);
        });

        setState(isChoosing);
    }
}
