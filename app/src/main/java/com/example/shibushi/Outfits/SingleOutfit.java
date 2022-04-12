package com.example.shibushi.Outfits;

import static com.example.shibushi.Outfits.OutfitChildAdapter.KEY_SINGLE_OUTFIT_VIEW_ITEMS;
import static com.example.shibushi.Outfits.OutfitChildAdapter.KEY_SINGLE_OUTFIT_VIEW_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cClothing;

import com.example.shibushi.R;

import java.util.ArrayList;

public class SingleOutfit extends AppCompatActivity {

    TextView outfitNameText;
    RecyclerView singleoutfitRecyclerView;
    SingleOutfitAdapter singleOutfitAdapter;
    ImageView backArrow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_outfit);

        Intent intent = getIntent();
        ArrayList<cClothing> clothings = (ArrayList<cClothing>) intent.getSerializableExtra(KEY_SINGLE_OUTFIT_VIEW_ITEMS);
        String outfitname = intent.getStringExtra(KEY_SINGLE_OUTFIT_VIEW_NAME);

        // bind widgets
        outfitNameText = findViewById(R.id.tvOutfitName);
        outfitNameText.setText(outfitname);

        backArrow = findViewById(R.id.snippet_view_outfit_toolbar_back);
        backArrow.setOnClickListener(view -> {
         SingleOutfit.this.finish();
        });

        singleoutfitRecyclerView = findViewById(R.id.layout_single_outfit_recyclerView);
        //set recycler view
        singleoutfitRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if(singleoutfitRecyclerView.getWidth()!=0){
                    Container.w = singleoutfitRecyclerView.getWidth();
                    Log.i("SingleOutfit width", String.valueOf(Container.w));
                }
                singleOutfitAdapter = new SingleOutfitAdapter(singleoutfitRecyclerView.getContext(), clothings, Container.w);
                singleoutfitRecyclerView.setAdapter(singleOutfitAdapter);
                singleoutfitRecyclerView.setLayoutManager(new LinearLayoutManager(SingleOutfit.this));
            }
        });
//        singleoutfitRecyclerView = findViewById(R.id.layout_single_outfit_recyclerView);
//        singleoutfitRecyclerView.setLayoutManager(new LinearLayoutManager(SingleOutfit.this));
//        singleOutfitAdapter = new SingleOutfitAdapter(SingleOutfit.this, clothings);
//        singleoutfitRecyclerView.setAdapter(singleOutfitAdapter);


    }

    static class Container{
        static int w;
    }
}
