package com.iamretailer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
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

    private final LayoutInflater mInflater;
    private final int resource;
    private final Context context;

    public PointerAdapter(Context context, int resource, List<ImgBo> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        holder = new ViewHolder();
        ImgBo detail = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(resource, null, true);
            convertView.setTag(holder);
        }
        holder.name = convertView.findViewById(R.id.tagimg);
        holder.bg = convertView.findViewById(R.id.background);
        if (detail != null) {
            if (detail.isImgSel()) {
            } else {
                holder.bg.setAlpha(0.4f);
            }
        }

        if (detail != null) {
            if (detail.getUrl().length() > 0)
                Picasso.with(context).load(detail.getUrl()).resize(200, 200).placeholder(R.mipmap.place_holder).into(holder.name);
            else
                Picasso.with(context).load(R.mipmap.place_holder).resize(200, 200).placeholder(R.mipmap.place_holder).into(holder.name);
        }
        return convertView;
    }

    static class ViewHolder {
        LinearLayout bg;
        ImageView name;
    }

}
