package com.iamretailer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iamretailer.POJO.CurPO;
import com.iamretailer.R;

import java.util.ArrayList;

public class CurAdapter extends ArrayAdapter<CurPO> {

    private final LayoutInflater mInflater;
    private final int resource;
    private final ArrayList<CurPO> items;


    public CurAdapter(Context context, int resource, ArrayList<CurPO> item) {
        super(context, resource, item);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.items = item;
    }


    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(resource, null, true);
            convertView.setTag(holder);
        }
        holder.lang_name = convertView.findViewById(R.id.lang_name);
        holder.img= convertView.findViewById(R.id.img);
        String lang=items.get(position).getCur_title()+" ("+items.get(position).getCur_left()+items.get(position).getCur_right()+")";
        holder.lang_name.setText(lang);


        if (items.get(position).isIsselected()) {
            holder.img.setImageResource(R.mipmap.checked);
        } else {

            holder.img.setImageResource(R.mipmap.unchecked);
        }


        return convertView;
    }

    class ViewHolder {

        TextView lang_name;
        ImageView img;
    }
}
