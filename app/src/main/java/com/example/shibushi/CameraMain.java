package com.example.shibushi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.LifecycleOwner;

import com.example.shibushi.*; //?databinding.ActivityMainBinding
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import android.provider.MediaStore;
import com.google.common.util.concurrent.ListenableFuture;

import android.content.ContentValues;
import java.util.Collections;


// Camera preview the photo
public class CameraMain extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture = null;
    private String TAG = "Shibushi";
    private static final String  FILENANE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    //private lateinit var viewBinding: ActivityMainBinding : do we use viewBinding? instead of findbyid

    //companion object
    private static final int REQUEST_CODE_PERMISSIOINS = 10;

    public ArrayList<String> REQUIRED_PERMISSION;
    {
        REQUIRED_PERMISSION = new ArrayList<>();
        REQUIRED_PERMISSION.add(Manifest.permission.CAMERA);
        REQUIRED_PERMISSION.add(Manifest.permission.RECORD_AUDIO);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            REQUIRED_PERMISSION.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        
    }
    public String[] REQUIRED_PERMISSIONS = (String[]) REQUIRED_PERMISSION.toArray();

    ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main);

        // Request camera permission
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIOINS);
        }

        // Set up listeners for take photo
        Button imageCaptureButton = findViewById(R.id.image_capture_button);
        imageCaptureButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            takePhoto();
        }
        });
        //ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIOINS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void takePhoto(){
        // Create time stamped name and MediaStore entry
        String name = new SimpleDateFormat(FILENANE_FORMAT).format(System.currentTimeMillis());

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
            }

        // Create output options object which contains file + metadata(local storage)
        ContentResolver contentResolver = new ContentResolver(CameraMain.this) {
            @Nullable
            @Override
            public String[] getStreamTypes(@NonNull Uri url, @NonNull String mimeTypeFilter) {
                return super.getStreamTypes(url, mimeTypeFilter);
            }
        };
        ImageCapture.OutputFileOptions.Builder outputOptions =
                new ImageCapture.OutputFileOptions.Builder(
                        contentResolver,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues);

        // todo: set up listener
    }

    private void startCamera(){
        // Request a camera provider
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        // Check availability
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Create a preview
                Preview preview = new Preview.Builder().build();

                // Specify the desired camera LensFacing option, back as default
                CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

                // Bind the selected camera and any use cases to the lifecycle
                Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);

                // Connect Preview to PreviewView
                PreviewView previewView = findViewById(R.id.previewView);
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

            } catch (ExecutionException | InterruptedException ex) {
                // This should never be reached
                Log.e(TAG,"StartCamera failed",ex);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    
    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(CameraMain.this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }








    /**
     * preview
     */
    private void previewView() {

    }

}





