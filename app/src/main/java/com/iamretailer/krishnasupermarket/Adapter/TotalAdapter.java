package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.iamretailer.krishnasupermarket.POJO.PlacePO;
import com.iamretailer.krishnasupermarket.R;


import java.util.ArrayList;

public class TotalAdapter extends ArrayAdapter<PlacePO> {

    Context context;
    ArrayList<PlacePO> items;
    int res;
    LayoutInflater mInflater;

    public TotalAdapter(Context context, int resource, ArrayList<PlacePO> items) {
        super(context, resource, items);
        this.context = context;
        this.res = resource;
        this.items = items;
        mInflater = LayoutInflater.from(context);
    }

    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LinearLayout alertView = null;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(res, alertView, true);
            convertView.setTag(holder);
            alertView = (LinearLayout) convertView;
        } else {
            alertView = (LinearLayout) convertView;
        }
        holder.product_name = (TextView) convertView.findViewById(R.id.head);
        holder.total = (TextView) convertView.findViewById(R.id.amount);

        holder.product_name.setText(items.get(position).getTot_title());
        holder.total.setText(items.get(position).getTot_amt_txt());

        return alertView;
    }

    private static class ViewHolder {
        public TextView product_name, p_qty, p_total, total;
        public ImageView p_img;


    }


}
