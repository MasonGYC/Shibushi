package com.example.shibushi.Utils;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;


public class FirestoreMethods {

    private static final String TAG = "FirebaseMethods";
    private static final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static final FirebaseFirestore mFirestoreDB = FirebaseFirestore.getInstance();
    private static final DocumentReference mDocRef= mFirestoreDB.document("cUsers/dUsers");
    private static final CollectionReference clothesRef = mFirestoreDB.collection("clothes");
    private static String userID;



    public static void addNewUser(String email, String username, String bio, String profile_photo) {
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /*
    * Takes in image ID, returns bitmap.
    * */
    /*public static Bitmap fetchClothes(String clothesID){
        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.CACHE;
        Query query = clothesRef.whereEqualTo("ID", clothesID);
        query.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "Cached document data: " + document.getData());
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });

    }*/








    /*
    * Takes in an image and uploads it to Cloud Storage
    * */
    public static void addClothes(HashMap<String, Object> map , Uri filePath){
        String url = uploadImage(filePath);
        map.put("url", url);
        metadataUpload(map);
    }

    private static String uploadImage(Uri filePath) {

        if (filePath != null) {
            // Defining the child of storageReference
            StorageReference ref = mStorageReference.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    Log.d(TAG, "Image Uploaded!!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            Log.d(TAG, "Failed " + e.getMessage());
                        }
                    });
            return ref.getDownloadUrl().toString();
        }
    return null;
    }

    /*
    * Takes in a hashmap of associated metadata for an image and uploads it to cloud firestore
    * */
    private static void metadataUpload(HashMap<String, Object> map ){
        if (map == null) return ;
        mDocRef.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
