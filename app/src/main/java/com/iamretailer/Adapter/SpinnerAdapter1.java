package com.iamretailer.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iamretailer.POJO.SingleOptionPO;
import com.iamretailer.R;


import java.util.ArrayList;

public class SpinnerAdapter1 extends BaseAdapter {

    
    private Context context;
    private ArrayList<String> items;
    Typeface typeface;

    public SpinnerAdapter1(Context context, ArrayList<String> stateData) {
        this.context = context;
        this.items = stateData;
        typeface=Typeface.createFromAsset(context.getAssets(),"font/Heebo-Regular.ttf");
        
    }

    @Override
    public int getCount() {

        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt, 0);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt, 1);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent, int loc) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.drop_downitem1, parent, false);
        TextView main_text = (TextView) mySpinner.findViewById(R.id.name);
        main_text.setText(items.get(position));

        if (position==3) {
            main_text.setTextColor(context.getResources().getColor(R.color.blue));
            main_text.setTypeface(typeface);
            main_text.setTextSize(12.0f);
        }

        return mySpinner;
    }

    static class ViewHolder {
        public TextView name;
    }


}