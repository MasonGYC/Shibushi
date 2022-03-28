package com.example.shibushi;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class SquareImage extends AppCompatImageView {

    public SquareImage(Context context){
        super(context);
    }

    public SquareImage(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public SquareImage(Context context, AttributeSet attrs, int defStylrAttr){
        super(context, attrs, defStylrAttr);
    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);
    }
}
