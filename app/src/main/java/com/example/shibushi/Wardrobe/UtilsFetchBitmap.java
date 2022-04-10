package com.example.shibushi.Wardrobe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.util.MalformedJsonException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Utils contain static methods to access the internet
 * You may just use them directly
 * The static methods that you can access are
 * (1) getBitmap
 * (2) isNetwork
 * (3) getImageURLFromXkcdApi
 */

public class UtilsFetchBitmap {

    static final String UTILS_TAG = "UtilsTag";

    /**
     * This method establishes a HTTP connection from a URL object
     * @param url a URL object
     * @return an InputStream object
     */

    private static InputStream getInputStream (URL url) throws IOException{

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
        }catch(IOException e) {
            e.printStackTrace();
            Log.i(UTILS_TAG,"getInputStream: No Stream");
            throw e;
        }

        return inputStream;
    }

    private static String convertStreamToString(InputStream inputStream) throws IOException{

        BufferedReader reader = null;
        String outString;

        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        //readline will throw a IO Exception
        while ((line = reader.readLine()) != null) {
   /* Since it's JSON, adding a newline isn't necessary (it won't affect
      parsing) but it does make debugging a *lot* easier if you print out the
      completed buffer for debugging. */
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            // Stream was empty.  No point in parsing.
            return null;
        }
        outString = buffer.toString();
        Log.i(UTILS_TAG,outString);
        return outString;

    }

    /**
     * This is a wrapper method to obtain a JSON string from a URL object
     * This is used to query an API for a JSON Response
     * @param url a URL object
     * @return a String containing JSON
     */

    private static String getJson(URL url) throws IOException {

        String json = null;
        json = convertStreamToString(getInputStream(url));
        return json;
    }


    /**
     * This method gets a Bitmap from a InputStream HTTP connection
     * @param  inputStream object
     * @return a Bitmap
     */
    private static Bitmap convertStreamToBitmap (InputStream inputStream){

        return BitmapFactory.decodeStream(inputStream);

    }

    /**
     * This method builds a URL object to call the xkcd API from the comic No
     * @param  comicNo a string containing a comic Number
     * @return a URL object
     */

    private static URL buildURL(String comicNo){

        String scheme = "https";
        final String authority = "xkcd.com";
        final String back = "info.0.json";
        Uri.Builder builder;
        URL url = null;


        builder = new Uri.Builder();
        builder.scheme(scheme)
                .authority(authority)
                .appendPath(comicNo)
                .appendPath(back);

        Uri uri = builder.build();

        try{
            url = new URL(uri.toString());
            Log.i(UTILS_TAG,"URL OK: " + url.toString());
        }catch(MalformedURLException e) {
            Log.i(UTILS_TAG, "malformed URL: " + url.toString());
        }

        return url;

    }

    /**
     * This method gets a Bitmap image from a URL object
     * @param  url a URL object
     * @return a Bitmap object
     */
    static Bitmap getBitmap(URL url) throws IOException{

        return convertStreamToBitmap(getInputStream(url));
    }

    /**
     * This method checks if an Activity has a network connection
     * @param  context a Context object (Context is the superclass of AppCompatActivity
     * @return a boolean object
     */

    static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.i(UTILS_TAG, "Active Network: " + haveNetwork);
        return haveNetwork;
    }

    /**
     * This method gets the xkcd JSON response from the comic No
     * @param  comicNo a String containing the comicNo
     * @return a String containing the JSON response
     */
    static String getImageURLFromXkcdApi(String comicNo)
            throws IOException, JSONException{

        final String xkcdImageKey = "img";

        URL url = buildURL(comicNo);
        String jsonString = getJson(url);
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getString(xkcdImageKey);
    }

}
