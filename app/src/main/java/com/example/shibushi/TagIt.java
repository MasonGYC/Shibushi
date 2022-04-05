package com.example.shibushi;

import static com.example.shibushi.Feed.FeedActivity.KEY_FEED_PHOTO;

import android.content.Intent;
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

import com.example.shibushi.Utils.FirestoreMethods;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;

public class TagIt extends AppCompatActivity {

    ImageView imageViewBitmap;
    Button buttonTagIt;
    Spinner spinnerColor;
    Spinner spinnerCategory;
    Spinner spinnerOccasion;
    final float scale = 0.7f; //scale factor for bitmap display
    FirebaseStorage storage;
    StorageReference storageReference;
    public Uri filePath;
    String photoURIString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagit);

        //dummy hashmap
        HashMap<String, Object> map = new HashMap<>();
        map.put("color", "red");
        map.put("size", "M");
        map.put("Category", "Shirt");

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //get widgets
        imageViewBitmap = findViewById(R.id.tagPhoto);
        buttonTagIt = findViewById(R.id.buttonTagIt);
        spinnerColor = findViewById(R.id.spinnerColor);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerOccasion = findViewById(R.id.spinnerOccasion);

        //get intent to set image
        Intent bitmapIntent = getIntent();
        if (bitmapIntent.getStringExtra(KEY_FEED_PHOTO) != null){
            photoURIString= bitmapIntent.getStringExtra(KEY_FEED_PHOTO);
        }
        else if (bitmapIntent.getStringExtra(MainActivity.KEY_PHOTO) != null){
            photoURIString = bitmapIntent.getStringExtra(MainActivity.KEY_PHOTO);
            }

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
                FirestoreMethods.addClothes(map,photoURI);
                Intent intent = new Intent(TagIt.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    /*public void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Toast.makeText(TagIt.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(TagIt.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                            });
        }
    }*/



}
