package com.example.shibushi.Utils;

import android.graphics.Bitmap;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;


public class FirestoreMethods {

    private static final String TAG = "FirebaseMethods";
    private static final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String userID = mAuth.getCurrentUser().getUid();
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static final FirebaseFirestore mFirestoreDB = FirebaseFirestore.getInstance();

    //Cloud Firestore references
    private static DocumentReference mDocRef;
    private static final CollectionReference mUsersRef= mFirestoreDB.collection("cUsers");
    private static final CollectionReference clothesRef = mFirestoreDB.collection("cClothes");


    /*
    * Takes in an image and uploads it to Cloud Storage
    * */
    public static void addClothes(HashMap<String, Object> map , Uri filePath){
        String[] result = uploadImage(filePath);
        String url = result[0];
        String img_name = result[1];
        map.put("url", url);
        map.put("img_name", img_name);
        map.put("userid", userID);
        metadataUpload(map, url);
    }

    private static String[] uploadImage(Uri filePath) {
        String[] result = new String[2];
        if (filePath != null) {
            // Defining the child of storageReference
            String img_name = UUID.randomUUID().toString();
            StorageReference ref = mStorageReference.child("images/" + img_name);

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
            result[0] = ref.getDownloadUrl().toString();
            result[1] = img_name;

            return result;
        }
    return null;
    }

    /*
    * Takes in a hashmap of associated metadata for an image and uploads it to cloud firestore
    * */
    private static void metadataUpload(HashMap<String, Object> map, String url){
        if (map == null) return ;
        String name = url.substring(33);
        mDocRef = clothesRef.document(name);
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
    public static void deleteClothes(){
        String img_name;
        String document_name;
        deleteImage(img_name);
        deleteMetadata(document_name);
    }
    private static void deleteMetadata(String name){
        mDocRef = clothesRef.document(name);
        mDocRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Document was deleted!");
                }
                else{
                    Log.w(TAG, "Document was not deleted!");
                }
            }
        });

    }
    private static void deleteImage(String img_name){
        StorageReference imgRef = mStorageReference.child("images/" + img_name +".jpeg");
        // Delete the file
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "Image deleted successfully.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d(TAG, "Image was not deleted!");
            }
        });
    }



    public static void addNewUser(String email, String username, String bio, String profile_photo) {
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

}
