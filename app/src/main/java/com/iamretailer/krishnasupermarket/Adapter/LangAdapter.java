package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.krishnasupermarket.POJO.LangPO;
import com.iamretailer.krishnasupermarket.R;

import java.util.ArrayList;

public class LangAdapter  extends ArrayAdapter<LangPO> {

     LayoutInflater mInflater;
    int resource;
    Context context;
    ArrayList<LangPO> items;


    public LangAdapter(Context context, int resource, ArrayList<LangPO> item) {
        super(context, resource, item);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.context = context;
        this.items = item;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LinearLayout alertView = null;
        final ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(resource, alertView, true);
            convertView.setTag(holder);
        }
        holder.lang_name = (TextView) convertView.findViewById(R.id.lang_name);
        holder.img=(ImageView) convertView.findViewById(R.id.img);
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
