package com.iamretailer.Common;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class CustomTextview extends TextView {
    public CustomTextview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"font/Heebo-Regular.ttf");
        Typeface type_bold=Typeface.createFromAsset(getContext().getAssets(),"font/Heebo-Bold.ttf");
        if (style == Typeface.BOLD) {
            super.setTypeface(type_bold);
        } else {
            super.setTypeface(typeface);
        }
    }
}
