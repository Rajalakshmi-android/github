package com.iamretailer.krishnasupermarket.Common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"font/Heebo-Bold.ttf");
        super.setTypeface(typeface, style);
    }
}
