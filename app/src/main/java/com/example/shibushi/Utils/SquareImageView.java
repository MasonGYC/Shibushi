package com.example.shibushi.Utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * This class is for making images look square in gridview
 */
public class SquareImageView extends AppCompatImageView {

    public SquareImageView(Context context){
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStylrAttr){
        super(context, attrs, defStylrAttr);
    }

    @Override
    protected void onMeasure(int width, int height){
        // makes the images square
        super.onMeasure(width, width);
    }
}
