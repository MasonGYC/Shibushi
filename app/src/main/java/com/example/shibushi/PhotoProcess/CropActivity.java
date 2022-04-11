package com.example.shibushi.PhotoProcess;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;
import com.example.shibushi.R;

import java.io.File;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 */
public class CropActivity extends BaseActivity implements UCropFragmentCallback {

    private static final String TAG = "CropActivity";

    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final int REQUEST_SELECT_PICTURE_FOR_FRAGMENT = 0x02;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    private Toolbar toolbar;
    private ScrollView settingsView;
    private int requestMode = 1;

    private UCropFragment fragment;
    private boolean mShowLoader;

    private String mToolbarTitle;
    @DrawableRes
    private int mToolbarCancelDrawable;
    @DrawableRes
    private int mToolbarCropDrawable;
    // Enables dynamic coloring
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mToolbarWidgetColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sample);
        //setupUI();
        pickFromGallery();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestMode) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCrop(selectedUri);
                } else {
                    Toast.makeText(CropActivity.this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    /*
    @SuppressWarnings("ConstantConditions")
    private void setupUI() {
        findViewById(R.id.button_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        //MODIFIED:NO NEED RANDOM CROP
        //settingsView = findViewById(R.id.settings);

    }

     */

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }

        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), requestMode);
    }

    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        //MODIFIED: CHANGE TO JPG
        destinationFileName += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

        uCrop.start(CropActivity.this);

        /*
        if (requestMode == REQUEST_SELECT_PICTURE_FOR_FRAGMENT) {       //if build variant = fragment
            setupFragment(uCrop);
        } else {                                                        // else start uCrop Activity
            uCrop.start(CropActivity.this);
        }

         */

    }

    /**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop basisConfig(@NonNull UCrop uCrop) {

        //MODIFIED: CHANGE TO SQUARE
        uCrop = uCrop.withAspectRatio(1, 1);

        //MODIFIED: CHANGE TO 300DP*300DP
        uCrop = uCrop.withMaxResultSize(300, 300);
        return uCrop;
    }

    /**
     * Sometimes you want to adjust more options, it's done via {@link UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        //MODIFIED: CHANGE TO JPEG,100 QUALITY
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setMaxBitmapSize(640);





        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            ResultActivity.startWithUri(CropActivity.this, resultUri);
        } else {
            Toast.makeText(CropActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(CropActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CropActivity.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadingProgress(boolean showLoader) {
        mShowLoader = showLoader;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {
        switch (result.mResultCode) {
            case RESULT_OK:
                handleCropResult(result.mResultData);
                break;
            case UCrop.RESULT_ERROR:
                handleCropError(result.mResultData);
                break;
        }
        removeFragmentFromScreen();
    }

    public void removeFragmentFromScreen() {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
        toolbar.setVisibility(View.GONE);
        settingsView.setVisibility(View.VISIBLE);
    }

    /*
    public void setupFragment(UCrop uCrop) {
        fragment = uCrop.getFragment(uCrop.getIntent(this).getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, UCropFragment.TAG)
                .commitAllowingStateLoss();

        setupViews(uCrop.getIntent(this).getExtras());
    }



    public void setupViews(Bundle args) {
        //settingsView.setVisibility(View.GONE);
        mStatusBarColor = args.getInt(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(this, com.yalantis.ucrop.R.color.ucrop_color_statusbar));
        mToolbarColor = args.getInt(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(this, com.yalantis.ucrop.R.color.ucrop_color_toolbar));
        mToolbarCancelDrawable = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, com.yalantis.ucrop.R.drawable.ucrop_ic_cross);
        mToolbarCropDrawable = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_CROP_DRAWABLE, com.yalantis.ucrop.R.drawable.ucrop_ic_done);
        mToolbarWidgetColor = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(this, com.yalantis.ucrop.R.color.ucrop_color_toolbar_widget));
        mToolbarTitle = args.getString(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        mToolbarTitle = mToolbarTitle != null ? mToolbarTitle : getResources().getString(com.yalantis.ucrop.R.string.ucrop_label_edit_photo);

        setupAppBar();
    }

     */


    /**
     * Configures and styles both status bar and toolbar.
     */
    /*
    private void setupAppBar() {
        setStatusBarColor(mStatusBarColor);

        toolbar = findViewById(R.id.toolbar);

        // Set all of the Toolbar coloring
        toolbar.setBackgroundColor(mToolbarColor);
        toolbar.setTitleTextColor(mToolbarWidgetColor);
        toolbar.setVisibility(View.VISIBLE);
        final TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(mToolbarWidgetColor);
        toolbarTitle.setText(mToolbarTitle);

        // Color buttons inside the Toolbar
        Drawable stateButtonDrawable = ContextCompat.getDrawable(getBaseContext(), mToolbarCancelDrawable);
        if (stateButtonDrawable != null) {
            stateButtonDrawable.mutate();
            stateButtonDrawable.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(stateButtonDrawable);
        }

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

     */

    /**
     * Sets status-bar color for L devices.
     *
     * @param color - status-bar color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(com.yalantis.ucrop.R.menu.ucrop_menu_activity, menu);

        // Change crop & loader menu icons color to match the rest of the UI colors

        MenuItem menuItemLoader = menu.findItem(com.yalantis.ucrop.R.id.menu_loader);
        Drawable menuItemLoaderIcon = menuItemLoader.getIcon();
        if (menuItemLoaderIcon != null) {
            try {
                menuItemLoaderIcon.mutate();
                menuItemLoaderIcon.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
                menuItemLoader.setIcon(menuItemLoaderIcon);
            } catch (IllegalStateException e) {
                Log.i(this.getClass().getName(), String.format("%s - %s", e.getMessage(), getString(com.yalantis.ucrop.R.string.ucrop_mutate_exception_hint)));
            }
            ((Animatable) menuItemLoader.getIcon()).start();
        }

        MenuItem menuItemCrop = menu.findItem(com.yalantis.ucrop.R.id.menu_crop);
        Drawable menuItemCropIcon = ContextCompat.getDrawable(this, mToolbarCropDrawable == 0 ? com.yalantis.ucrop.R.drawable.ucrop_ic_done : mToolbarCropDrawable);
        if (menuItemCropIcon != null) {
            menuItemCropIcon.mutate();
            menuItemCropIcon.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            menuItemCrop.setIcon(menuItemCropIcon);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(com.yalantis.ucrop.R.id.menu_crop).setVisible(!mShowLoader);
        menu.findItem(com.yalantis.ucrop.R.id.menu_loader).setVisible(mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.yalantis.ucrop.R.id.menu_crop) {
            if (fragment != null && fragment.isAdded())
                fragment.cropAndSaveImage();
        } else if (item.getItemId() == android.R.id.home) {
            removeFragmentFromScreen();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("cropAct","onDestroy");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i("cropAct","onStop");
    }
}
