package com.iamretailer.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.POJO.SingleOptionPO;
import com.iamretailer.R;

import java.util.ArrayList;


public class ColorAdapter extends ArrayAdapter<SingleOptionPO> {

    private LayoutInflater mInflater;
    private int resource;
    private Context context;
    private ViewHolder holder;
    private ArrayList<SingleOptionPO> items;


    public ColorAdapter(Context context, int resource, ArrayList<SingleOptionPO> item) {
        super(context, resource, item);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.context = context;
        this.items = item;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LinearLayout alertView = null;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(resource, alertView, true);
            convertView.setTag(holder);
        }
        holder.colortic = (LinearLayout) convertView.findViewById(R.id.colortic);
        holder.color = (TextView) convertView.findViewById(R.id.colortxt);
        try {
            GradientDrawable drawable = (GradientDrawable) holder.color.getBackground();
            drawable.setColor(Color.parseColor(items.get(position).getName()));
        } catch (Exception e) {
            Log.d("value_", e.toString());
        }


        if (items.get(position).isImgSel()) {
            holder.colortic.setVisibility(View.VISIBLE);
        } else {

            holder.colortic.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {
        LinearLayout colortic;
        TextView color;
    }
}
