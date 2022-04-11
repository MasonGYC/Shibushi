package com.example.shibushi.Wardrobe;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ViewWardrobeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ViewWardrobe";
    final private Context mContext = ViewWardrobeActivity.this;
    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 2;

    // Left sidebar
    ImageView side_bar_clothes;
    ImageView side_bar_bottoms;
    ImageView side_bar_others;
    ImageView current_view;

    // Fragment & ViewPage
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wardrobe);

        // Set up bottom navigation bar
        setupBottomNavigationView();

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

        // todo: set to fetch from firebase
        ArrayList<String> clothes = new ArrayList<>();
        ArrayList<String> bottoms = new ArrayList<>();
        ArrayList<String> accessories = new ArrayList<>();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(WardrobeFragment.newInstance(clothes));    // clothes
        fragments.add(WardrobeFragment.newInstance(bottoms));    // bottoms
        fragments.add(WardrobeFragment.newInstance(accessories));    // accessories
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

    private void changeTab(int position) {
        current_view.setBackgroundColor(Color.parseColor("#eeeeee"));
//        current_view.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        current_view.setTextColor(Color.parseColor("#6d6d6d"));
        current_view.setSelected(false);
        switch (position){
            case R.id.id_wd_clothes:
                viewPager.setCurrentItem(0);
                current_view = side_bar_clothes;
                side_bar_clothes.setBackgroundColor(Color.parseColor("#FFFFFF"));
                side_bar_clothes.setSelected(true);
//                side_bar_clothes.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                side_bar_clothes.setTextColor(Color.parseColor("#292929"));

            case 0: // R.id.id_wd_clothes
                break;
            case R.id.id_wd_bottoms:
                viewPager.setCurrentItem(1);
                current_view = side_bar_bottoms;
                side_bar_bottoms.setBackgroundColor(Color.parseColor("#FFFFFF"));
                side_bar_bottoms.setSelected(true);
//                side_bar_bottoms.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                side_bar_bottoms.setTextColor(Color.parseColor("#292929"));

            case 1: // R.id.id_wd_clothes
                break;
            case R.id.id_wd_others:
                viewPager.setCurrentItem(2);
                current_view = side_bar_others;
                side_bar_others.setBackgroundColor(Color.parseColor("#FFFFFF"));
                side_bar_others.setSelected(true);
//                side_bar_others.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                side_bar_others.setTextColor(Color.parseColor("#292929"));
            case 2: // R.id.id_wd_clothes
                break;
        }
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

    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }
}
