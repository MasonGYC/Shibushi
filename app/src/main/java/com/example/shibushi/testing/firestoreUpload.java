package com.example.shibushi.testing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shibushi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class firestoreUpload extends AppCompatActivity {


    FirebaseStorage storage;
    StorageReference storageReference;
    DocumentReference mDocRef;
    public static final String TAG = "Logcat";
    public static final String NAME = "name";
    Button buttonTagIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore_upload);

        // get the Firebase Firestore reference
        mDocRef = FirebaseFirestore.getInstance().document("cUsers/dUsers");
        buttonTagIt = findViewById(R.id.buttonTagIt);

        buttonTagIt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveLine(view);
            }
        });
    }
    public void saveLine(View view){
        //get widgets

        EditText editFirestore = findViewById(R.id.editFirestore);
        String line = editFirestore.getText().toString();

        if (line.isEmpty()) return;
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(NAME,line);
        mDocRef.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Document was saved!");
                }
                else{
                    Log.w(TAG, "Document was not saved!");
                }
            }
        });

    }




}
