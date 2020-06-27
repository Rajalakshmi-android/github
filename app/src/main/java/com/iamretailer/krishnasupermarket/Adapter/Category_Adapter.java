package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.krishnasupermarket.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import com.iamretailer.krishnasupermarket.Common.CircleTransforms;
import com.iamretailer.krishnasupermarket.POJO.BrandsPO;


public class Category_Adapter extends ArrayAdapter<BrandsPO> {

    Context context;
    private ArrayList<BrandsPO> list;
    int res;
    LayoutInflater mInflater;
    int frm;

    public Category_Adapter(Context context, int res, ArrayList<BrandsPO> list, int from) {
        super(context, res, list);
        this.context = context;
        this.list = list;
        this.res = res;
        mInflater = LayoutInflater.from(context);
        this.frm = from;
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
        final ViewHolder holder;
        LinearLayout alertView = null;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(res, alertView, true);
            convertView.setTag(holder);
            alertView = (LinearLayout) convertView;
        } else {
            alertView = (LinearLayout) convertView;
        }
        //holder.bg_img = (ImageView) convertView.findViewById(R.id.back_img);
        holder.frame = (FrameLayout) convertView.findViewById(R.id.brand);
        holder.name = (TextView) convertView.findViewById(R.id.store_name);
        holder.name.setText(list.get(position).getStore_name());


        int j;
        if (position >= 4) {
            j = position % 4;
        } else {
            j = position;
        }
        int pos = j + 1;
        Log.i("tag", "colorrrrrr " + position);

        if (pos == 1) {
            holder.frame.setBackgroundResource(R.mipmap.cate_pinkbg);
        } else if (pos == 2) {
            holder.frame.setBackgroundResource(R.mipmap.cate_violetbg);

        } else if (pos == 3) {
            holder.frame.setBackgroundResource(R.mipmap.cate_greenbg);
        } else if (pos == 4) {
            holder.frame.setBackgroundResource(R.mipmap.cate_orangebg);
        }



        return alertView;
    }

    public class ViewHolder {
        FrameLayout frame;
        ImageView bg_img;
        TextView name;
        ImageView more;

    }


}
