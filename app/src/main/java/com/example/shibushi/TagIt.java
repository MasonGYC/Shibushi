package com.example.shibushi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class TagIt extends AppCompatActivity {

    ImageView imageViewBitmap;
    Button buttonTagIt;
    Spinner spinnerColor;
    Spinner spinnerCategory;
    Spinner spinnerOccasion;
    final float scale = 0.7f; //scale factor for bitmap display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagit);

        //get widgets
        imageViewBitmap = findViewById(R.id.tagPhoto);
        buttonTagIt = findViewById(R.id.buttonTagIt);
        spinnerColor = findViewById(R.id.spinnerColor);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerOccasion = findViewById(R.id.spinnerOccasion);

        //get intent to set image
        Intent bitmapIntent = getIntent();
        String photoURIString = bitmapIntent.getStringExtra(MainActivity.KEY_PHOTO);
        Uri photoURI = Uri.parse(photoURIString);

        try {
            Bitmap bitmapfull = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
            Bitmap bitmap = Bitmap.createScaledBitmap(bitmapfull,
                    (int)(bitmapfull.getWidth()*scale),
                    (int)(bitmapfull.getHeight()*scale),
                    true); //bilinear filtering
            imageViewBitmap.setImageBitmap(bitmap);
            Log.i("imageViewBitmap", String.valueOf(bitmapfull.getWidth()*scale));
            Log.i("imageViewBitmap", String.valueOf(bitmapfull.getHeight()*scale));
        } catch (IOException e) {
            Log.i("tagit","NO imgUri");
        }

        buttonTagIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TagIt.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }


}
