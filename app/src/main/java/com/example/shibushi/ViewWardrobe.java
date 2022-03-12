package com.example.shibushi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ViewWardrobe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wardrobe);
    }
}

//todo remove clothing
// TODO: 3/12/2022 view by certain metadata
//todo research if can sort by metadata
/**
 * According to this link:
 * https://stackoverflow.com/questions/58590666/firebase-use-metadata-to-sort-which-files-get-displayed-when-use-list
 * It's good to use Firebase storage for image storage
 * in conjunction with RTDB or Cloud Firestore for file metadata.
 * This allows us to easily store, query and update the metadata
 * We may not want to separate the uploads into different folders for each user as
 * we want to be able to view others by tags
 *
 * According to this link:
 * https://firebase.google.com/docs/firestore/query-data/queries#java_5
 * We can do complex queries in Cloud Firestore
 * E.g. we can structure each document in Cloud Firestore as
 *
 * Documetn structure
 * { username: michael
 *   imageurl: url(truncated)
 *   color : color
 *   etc...
 * }
 *
 * Common operations:
 *
 *  Query/search:
 *      All yellow shirts (color, type) -> Query chainedQuery1 = images.whereEqualTo(color , "yellow").whereEqualTo(username, "Joshua)
 *      All formal pants (occasion, type)
 *      All white cotton (color, material)
 *
 *      -> Query chainedQuery1 = images.whereEqualTo(color , "yellow")
 *      .whereEqualTo(username, "Michael")
 *      .whereEqualTo(type, "Shirt")
 *      Query chainedQuery2 = images.whereEqualTo(color, "yellow").whereEqualTo(type, "Shirt").whereEqualTo(privacy, "public")
 *
 *      We may not search by privacy attribute
 *
 *  Add:
 *
 *  Delete Image, if user match/authorized:
 *
 *  Tree structure - flat hierarchy:
 *
 *  Images
 *      -Public
 *          -Image1
 *          -Image2
 *          -Image3
 *      -Private
 *          -User
 *              -Image1
 *
 *         -OR-
 *   Images
 *      -Image1
 *      -Image2
 *      -Image3
 *
 *
 *  Don't bother splitting between private and public, just use a != filter for privacy
 *
 *
 *
 *
 * */
