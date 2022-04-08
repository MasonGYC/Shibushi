package com.example.shibushi;

import static com.example.shibushi.Feed.FeedActivity.KEY_FEED_PHOTO;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shibushi.PhotoProcess.ResultActivity;
import com.example.shibushi.Utils.FirebaseMethods;
import com.example.shibushi.Utils.FirestoreMethods;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TagIt extends AppCompatActivity {

    ImageView imageViewBitmap;
    Button buttonTagIt;
    Spinner spinnerColor;
    Spinner spinnerCategory;
    Spinner spinnerOccasion;
    Spinner spinnerPrivacy;
    final float scale = 0.7f; //scale factor for bitmap display
    FirebaseStorage storage;
    StorageReference storageReference;
    public Uri filePath;
    String photoURIString;
    HashMap<String, Object> map = new HashMap<>();

    //TAGS
    public final static String COLOR = "color";
    public final static String OCCASION = "occasion";
    public final static String SIZE = "size";
    public final static String CATEGORY = "category";
    public final static String PRIVACY = "privacy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagit);


        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //get widgets
        imageViewBitmap = findViewById(R.id.tagPhoto);
        buttonTagIt = findViewById(R.id.buttonTagIt);
        spinnerColor = findViewById(R.id.spinnerColor);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerOccasion = findViewById(R.id.spinnerOccasion);
        spinnerPrivacy = findViewById(R.id.spinnerPrivacy);
        //get map data
        map.put(COLOR,spinnerColor.getSelectedItem().toString());
        map.put(CATEGORY,spinnerCategory.getSelectedItem().toString());
        map.put(OCCASION,spinnerOccasion.getSelectedItem().toString());
        map.put(PRIVACY, spinnerPrivacy.getSelectedItem().toString());

        //get intent to set image
        Intent bitmapIntent = getIntent();

        Uri photoURI = Uri.parse(bitmapIntent.getStringExtra(ResultActivity.KEY_PHOTO));

        try {
            //Toast.makeText(TagIt.this,photoURI.toString(),Toast.LENGTH_LONG).show();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
            imageViewBitmap.setImageBitmap(bitmap);

        } catch (IOException e) {
            Log.i("tagit", "NO imgUri");
        }

        buttonTagIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store tags info
                map.put(COLOR, spinnerColor.getSelectedItem().toString());
                map.put(SIZE, spinnerSize.getSelectedItem().toString());
                map.put(CATEGORY, spinnerCategory.getSelectedItem().toString());
                map.put(OCCASION, spinnerOccasion.getSelectedItem().toString());
                // UPLOAD IMAGE AND TAGS
                FirestoreMethods.addClothes(map, photoURI);
                // go back to MainActivity
                Toast.makeText(TagIt.this, "TAG IT", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(TagIt.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


}
