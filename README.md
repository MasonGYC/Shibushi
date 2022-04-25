# 50.001-info-sys

Shibushi (试不试) is a virtual wardrobe app designed to help you organise your wardrobe, plan your fits and share them with a like-minded community! This app was submitted for the 1D project during the 50.001 Information Systems & Programming course in SUTD, Spring 2022.  

## Contributors
TEAM 3B
- Guo Yuchen 1004885
- Jolin Teo Hui Lyn 1005344
- Joshua Ng Tze Wee 1005285
- Samuel Chiang Wenbin 1005142
- Xiang Siqi 1004875
- Wang Yueheng 1004866
- Wang Zhao 1005460

## Features

#### Tag It
Upload, tag and organise your wardrobe.  

<img src="https://github.com/MasonGYC/50.001-info-sys/blob/main/images/tag_it.png" width="200">

#### Build Your Fit

Unleash your creativity. Make a moodboard for every vibe!  

<img src="https://github.com/MasonGYC/50.001-info-sys/blob/main/images/outfit.png" width="200">

#### Community 

Browse and share outfits with friends!  

<img src="https://github.com/MasonGYC/50.001-info-sys/blob/main/images/feed.png" width="200"><img src="https://github.com/MasonGYC/50.001-info-sys/blob/main/images/popup.png" width="200">  

## Design and Structure
<img src="https://github.com/MasonGYC/50.001-info-sys/blob/main/images/datapath_android.jpg">

### Cloud Data Structure
We used two databases from Firebase - **Cloud Storage** and **Cloud Firestore**. The reason was that Cloud Storage was better suited for image storage with higher storage capacity and could natively handle JPEG and PNG images. Clothing metadata was stored on Cloud Firestore due to more comprehensive queries, and to consolidate with user and outfits metadata.  

For Cloud Firestore, we have 3 collections: **cUsers**, **cClothes** and **cOutfits** which holds the metadata for a user, clothing and outfit respectively. All images are stored under the images folder in Firebase Storage.  

#### cClothes
Each clothing is stored as a document containing its metadata such as _colour_, _category_, _size_ and _occasion_. The field “userid'' helps us identify the owner of that clothing. _Creation time_ as a timestamp object for tracking purposes, and most importantly the _img_name_ which is used as a reference to the image of the clothing stored in cloud storage.  

#### cOutfits
Each outfit is stored as a document containing its _name_, _userid_ of the owner, _timestamp_ of its creation time, unique _outfitID_ which is used to reference the outfit document, and _category_. For fast referencing to the images in cloud storage, we also stored an _img_names_ array containing the image names of all clothing in a single outfit. Furthermore, under _item_ is an array of all the clothing documents that are part of the outfit which could be used for outfit classifications.  

#### cUsers
We keep track of attributes related to the community feed. The field _bio_ contains user’s bio, an array of _followers_ containing the userid of all the user’s followers, another array of _following_ containing all the userid that the user is following, _userid_ as a reference to the current document, and _username_ of the user. The user’s _profile_photo_ also stores the reference to the image of the user’s profile photo in cloud storage.  

### Models

**cWardrobe**, **cClothing**, **cUsers** and **cOutfits** are model classes that correspond to the collections in the firestore. These models were used to store data in local variables temporarily for easy referencing, so that we do not need to do multiple queries to fetch them from the Firestore.

## Concept Highlights

### Comparable Interface
To sort outfits in feed in descending timestamp, the Comparable interface was implemented under the model class cOutfits.  

<img src="https://github.com/MasonGYC/50.001-info-sys/blob/main/images/one.png">

### Nested RecyclerView
On top of the recyclerView taught, we modified it and implemented a nested recyclerview to suit the needs of our application. The feed and profile consists of a parent recyclerview, populated by the user's outfits. Inside each outfit is a child recyclerview populated by images belonging to that outfit. Thus, to do this we created FeedParentAdapter and Feed Child Adapter classes.  

In FeedActvitiy, a query is made to fetch the most recent outfits created by current user’s list of following users, and stored to cOutfitsArrayList. It is then passed to FeedParentAdapter, which will then pass a list of the clothing’s image names into FeedChildAdapter.  

### Nested Interface in an Adapter Class
cUsersAdapter is an adapter class used to populate the RecyclerView in SearchActivity. It makes use of a nested interface called UserClickListener, that requires us to define the method onSelectedUser under SearchActivity. An example is shown in the code below. When users select any of the Users displayed in the recyclerview in SearchActivity, it will start an activity of the selected user’s profile.  
<img src="https://github.com/MasonGYC/50.001-info-sys/blob/main/images/two.png">
<img src="https://github.com/MasonGYC/50.001-info-sys/blob/main/images/three.png">

### Executor and ExecutorService
Since processing and loading photos takes time, we added ExecutorService to load the bitmaps inside the recyclerView in outfits and wardrobe features. The code snippet below shows how Picasso library was used in the background single thread to load and resize images. This modification improves the user experience as it significantly reduces the time taken to load bitmaps onto the imageviews.  
<img src="https://github.com/MasonGYC/50.001-info-sys/blob/main/images/four.png">


## Technologies
- Android Studio
- Cloud Firestore
- Cloud Storage
- Firebase Authentication
- Java


## External Libraries
1. uCrop module https://github.com/Yalantis/uCrop 
2. uCrop sample activities  https://github.com/Yalantis/uCrop  
3. Interactive floating action button https://github.com/zendesk/android-floating-action-button  
4. Universal Image Loader https://github.com/nostra13/Android-Universal-Image-Loader  
5. Glide https://github.com/bumptech/glide  
6. Circle Image View https://github.com/hdodenhof/CircleImageView  
7. Jackson Object Mapper https://github.com/FasterXML/jackson  
8. Picasso Picasso (square.github.io)  

## License

    Copyright 2022, Mason Guo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
