package com.iamretailer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iamretailer.POJO.LangPO;
import com.iamretailer.R;

import java.util.ArrayList;

public class LangAdapter extends ArrayAdapter<LangPO> {

    private final LayoutInflater mInflater;
    private final int resource;
    private final ArrayList<LangPO> items;

    public LangAdapter(Context context, int resource, ArrayList<LangPO> item) {
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
        holder.img = convertView.findViewById(R.id.img);
        holder.lang_name.setText(items.get(position).getLang_name());

        if (items.get(position).isSelect_lang()) {
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
