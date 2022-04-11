package com.example.shibushi.Utils;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        String img_name = uploadImage(filePath);
        map.put("img_name", img_name);
        map.put("userid", userID);
        metadataUpload(map, img_name);
    }

    private static String uploadImage(Uri filePath) {

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
            return img_name;
        }
        return null;
    }

    /*
     * Takes in a hashmap of associated metadata for an image and uploads it to cloud firestore
     * */
    private static void metadataUpload(HashMap<String, Object> map, String name){
        if (map == null) return ;

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
    /*
    * Deleting methods
    * */
    public static void deleteClothes(){
        String img_name="";
        String document_name="";
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
        StorageReference imgRef = mStorageReference.child("images/" + img_name);
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

    /*
    * Accessing image and metadata methods
    * */
    public static String getDownloadUrlString(String img_name){
        StorageReference imgRef = mStorageReference.child("images/" + img_name);
        final String[] url = {null};
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("success", uri.toString());
                url[0] = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        return url[0];
    }

    // Query clothes
//    public static ArrayList<String> getmyClothes(String userID){
//        ArrayList<String> clothes_Array = new ArrayList<>();
//        CollectionReference clothesRef = mFirestoreDB.collection("cClothes");
//        Query myClothes = clothesRef.whereEqualTo("userid", userID);
//        myClothes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Object image_name = document.get("img_name");
//                        clothes_Array.add(image_name.toString());
//                        Log.d(TAG, document.getId() + " => " + clothes_Array);
//                        //Bitmap image = Image.getBitmap(image_url.toString());
//                    }
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//        return clothes_Array;
//    }



    // For User followers and followings
    public static void getAllFollow(String userID){

        getmyFollowers(userID);
    }
    private static void getmyFollowers(String userID){
        DocumentReference docRef = mFirestoreDB.collection("cUsers").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        // DocumentSnapshot data: {following=[user1, user2], followers=[user0]} , is returned
                        Map<String, Object> map = document.getData();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            int followers_count = 0;
                            if (entry.getKey().equals("followers")) {
                                followers_count +=1;
                                Log.d("TAG", entry.getValue().toString());
                            }
                            // later return count
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

// register
    public static void initialise_cUser(FirebaseUser currentUser) {
        String userID = currentUser.getUid();
        String username = currentUser.getDisplayName();

        // Initialise data in firestore
        Map<String, Object> username_hashMap = new HashMap<>();
        username_hashMap.put("username", username);
        mFirestoreDB.collection("cUsers").document(userID).set(username_hashMap);

        Map<String, Object> userID_hashMap = new HashMap<>();
        userID_hashMap.put("userID", userID);
        mFirestoreDB.collection("cUsers").document(userID).update(userID_hashMap);

        Map<String, Object> bio = new HashMap<>();
        bio.put("bio", "No description");
        mFirestoreDB.collection("cUsers").document(userID).update(bio);

        Map<String, Object> following = new HashMap<>();
        following.put("following", new ArrayList<>());
        mFirestoreDB.collection("cUsers").document(userID).update(following);

        Map<String, Object> followers = new HashMap<>();
        followers.put("followers", new ArrayList<>());
        mFirestoreDB.collection("cUsers").document(userID).update(followers);

        // TODO: Update when url is working
        Map<String, Object> profile_photo = new HashMap<>();
        profile_photo.put("profile_photo", "https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png");
        mFirestoreDB.collection("cUsers").document(userID).update(profile_photo);

    }


    // Populate/update wardrobe with all clothes from firebase
    public static ArrayList<Map> getAllClothes(){
        ArrayList<Map> clothes_Array = new ArrayList<>();
        HashMap<String,String> clothes_map = new HashMap<>();
        CollectionReference clothesRef = mFirestoreDB.collection("cClothes");
        // All of User's clothes saved as a Query Object.
        Query myClothes = clothesRef.whereEqualTo("userid", userID);
        myClothes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // for each clothing as a document, put as a map
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();
                        clothes_Array.add(map);

//                        Object image_name = document.get("img_name");
//                        clothes_Array.add(image_name.toString());
//                        Log.d(TAG, document.getId() + " => " + clothes_Array);
                        //Bitmap image = Image.getBitmap(image_url.toString());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return clothes_Array;
    }

    // Populate/update wardrobe with all Outfits from firebase
    public static ArrayList<Map> getAllOutfits(){
        ArrayList<Map> outfits_Array = new ArrayList<>();
        HashMap<String,String> outfits_map = new HashMap<>();
        CollectionReference outfitRef = mFirestoreDB.collection("cOutfits");
        // All of User's clothes saved as a Query Object.
        Query myOutfits = outfitRef.whereEqualTo("userID", userID);
        myOutfits.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // for each clothing as a document, put as a map
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();
                        outfits_Array.add(map);

//                        Object image_name = document.get("img_name");
//                        clothes_Array.add(image_name.toString());
//                        Log.d(TAG, document.getId() + " => " + clothes_Array);
                        //Bitmap image = Image.getBitmap(image_url.toString());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return outfits_Array;
    }
}