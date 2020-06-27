package com.iamretailer.krishnasupermarket.Common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditext extends EditText {
    public CustomEditext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
         Typeface type_norma=Typeface.createFromAsset(getContext().getAssets(),"font/Heebo-Regular.ttf");
         Typeface type_bold=Typeface.createFromAsset(getContext().getAssets(),"font/Heebo-Bold.ttf");


        if (style == Typeface.BOLD) {
            super.setTypeface(type_bold);
        } else {
            super.setTypeface(type_norma);
        }
    }
}
