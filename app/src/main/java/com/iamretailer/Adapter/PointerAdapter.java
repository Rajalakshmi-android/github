package com.iamretailer.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import com.iamretailer.POJO.ImgBo;


import com.iamretailer.R;
import com.squareup.picasso.Picasso;

public class PointerAdapter extends ArrayAdapter<ImgBo> {

    private LayoutInflater mInflater;
    private int resource;
    private Context context;
    private List<ImgBo> items;
    private int from;

    public PointerAdapter(Context context, int resource, List<ImgBo> items, int from) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.context = context;
        this.items = items;
        this.from = from;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LinearLayout alertView = null;
        holder = new ViewHolder();
        ImgBo detail = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(resource, alertView, true);
            convertView.setTag(holder);
        }
        holder.name = (ImageView) convertView.findViewById(R.id.tagimg);
        holder.bg = (LinearLayout) convertView.findViewById(R.id.background);
        if (detail.isImgSel()) {
            // holder.name.setBackgroundColor(Color.parseColor("#80f05c5b"));
        } else {
            //holder.name.setBackgroundColor(Color.parseColor("#80ffffff"));
            holder.bg.setAlpha(0.4f);
        }

        Picasso.with(context).load(detail.getUrl()).resize(200, 200).placeholder(R.mipmap.place_holder).into(holder.name);
        return convertView;
    }

    static class ViewHolder {
        public LinearLayout bg;
        ImageView name;
    }

}
