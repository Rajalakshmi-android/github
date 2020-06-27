package com.iamretailer.krishnasupermarket.Common;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class CustomTextview1 extends TextView {
    public CustomTextview1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"font/Heebo-Medium.ttf");
        super.setTypeface(typeface);

    }
}
