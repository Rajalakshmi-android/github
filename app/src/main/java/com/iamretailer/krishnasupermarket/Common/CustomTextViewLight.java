package com.iamretailer.krishnasupermarket.Common;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewLight extends TextView {
    public CustomTextViewLight(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"font/Heebo-Light.ttf");
        super.setTypeface(typeface);

    }
}
