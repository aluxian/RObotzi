package com.creativemonkeyz.robotzi;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PreviewImage extends ImageView {

    public PreviewImage(Context context) {
        super(context);
    }

    public PreviewImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviewImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}