package com.example.shibushi;

import static com.example.shibushi.Feed.FeedActivity.KEY_FEED_PHOTO;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Crop extends AppCompatActivity {

    private static final int CROP_PHOTO = 4;
    String photoURIString;
    Uri photoURI;

    ImageView imageViewBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageViewBitmap = findViewById(R.id.tagPhoto);

        //get intent to set image
        Intent bitmapIntent = getIntent();
        if (bitmapIntent.getStringExtra(KEY_FEED_PHOTO) != null){
            photoURIString= bitmapIntent.getStringExtra(KEY_FEED_PHOTO);
        }
        else if (bitmapIntent.getStringExtra(MainActivity.KEY_PHOTO) != null){
            photoURIString = bitmapIntent.getStringExtra(MainActivity.KEY_PHOTO);
        }

        photoURI = Uri.parse(photoURIString);
        Toast.makeText(Crop.this,photoURI.toString(),Toast.LENGTH_SHORT).show();

        try {
            cropPhoto(photoURI);
            Toast.makeText(Crop.this,"debug",Toast.LENGTH_SHORT).show();
            Intent tagItIntent = new Intent(Crop.this,TagIt.class);
            startActivity(tagItIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cropPhoto(Uri uri) throws IOException {
        Log.i("CROP", "launch cropPhoto");
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);

        // crop output size
        intent.putExtra("crop","true");
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX", R.dimen.tagItImage);
        intent.putExtra("outputY", R.dimen.tagItImage);
        intent.putExtra("return-data", true);
        Log.i("CROP", "cropPhoto start activity for result");
        if( intent.resolveActivity(getPackageManager()) != null){
            Log.i("CROP", "Able to initialize");
            try {
                startActivityForResult(intent, CROP_PHOTO);
            }catch (Exception e){
                Log.i("EXCEPTION","catch exception");
            }
            Log.i("CROP", "Able to initialize ends");
        }
        else{
            Log.i("CROP", "Fail to initialize");
        }


        //startActivityForResult(intent, CROP_PHOTO);
        Log.i("CROP", "cropPhoto start activity for result ends");

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == CROP_PHOTO && resultCode == RESULT_OK) {
//                Bitmap bitmap = data.getExtras().getParcelable("data");
//                imageViewBitmap.setImageBitmap(bitmap);
//                Toast.makeText(Crop.this, "setbitmap", Toast.LENGTH_SHORT).show();
//            }
//        }
//        catch (Exception e){
//            Log.i("oncropresult", String.valueOf(e));
//        }
//    }
//}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("CROP", "launch onActivityResult");
        Log.i("HAHAHA", String.valueOf(requestCode+resultCode));
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CROP_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmap = data.getExtras().getParcelable("data");
            Log.i("CROP", bitmap.toString());
            imageViewBitmap.setImageBitmap(bitmap);
            Toast.makeText(Crop.this,"setbitmap",Toast.LENGTH_SHORT).show();
//            Intent tagItIntent = new Intent(Crop.this, TagIt.class);
//            startActivity(tagItIntent);
        }
    }
}
