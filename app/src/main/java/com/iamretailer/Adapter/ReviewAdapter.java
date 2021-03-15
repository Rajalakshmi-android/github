package com.iamretailer.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.iamretailer.POJO.SingleOptionPO;
import com.iamretailer.R;


public class ReviewAdapter extends ArrayAdapter<SingleOptionPO> {
    private final ArrayList<SingleOptionPO> items;
    private final int res;
    private final LayoutInflater mInflater;

    public ReviewAdapter(Context context, int resource, ArrayList<SingleOptionPO> items) {
        super(context, resource, items);
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

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        FrameLayout alertView;
        holder = new ViewHolder();

        if (convertView == null) {
            convertView = mInflater.inflate(res, null, true);
            convertView.setTag(holder);
            alertView = (FrameLayout) convertView;
        } else {
            alertView = (FrameLayout) convertView;
        }
        holder.comts = convertView.findViewById(R.id.comments);
        holder.rev_date = convertView.findViewById(R.id.date);
        holder.user_name = convertView.findViewById(R.id.user_name);
        holder.user_first = convertView.findViewById(R.id.user_first);
        holder.review_bg = convertView.findViewById(R.id.review_bg);
        holder.r1 = convertView.findViewById(R.id.r1);
        holder.r2 = convertView.findViewById(R.id.r2);
        holder.r3 = convertView.findViewById(R.id.r3);
        holder.r4 = convertView.findViewById(R.id.r4);
        holder.r5 = convertView.findViewById(R.id.r5);
        if (Build.VERSION.SDK_INT >= 24) {
            holder.comts.setText(Html.fromHtml(items.get(position).getRev_text(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.comts.setText(Html.fromHtml(items.get(position).getRev_text()));
        }
        holder.rev_date.setText(items.get(position).getRev_date());
        holder.user_name.setText(items.get(position).getRev_author());
        holder.user_first.setText(String.valueOf(items.get(position).getRev_author().charAt(0)).toUpperCase());


        switch (items.get(position).getRating()) {
            case 0:
                holder.r1.setImageResource(R.mipmap.un_fill);
                holder.r2.setImageResource(R.mipmap.un_fill);
                holder.r3.setImageResource(R.mipmap.un_fill);
                holder.r4.setImageResource(R.mipmap.un_fill);
                holder.r5.setImageResource(R.mipmap.un_fill);
                break;
            case 1:
                holder.r1.setImageResource(R.mipmap.fill);
                holder.r2.setImageResource(R.mipmap.un_fill);
                holder.r3.setImageResource(R.mipmap.un_fill);
                holder.r4.setImageResource(R.mipmap.un_fill);
                holder.r5.setImageResource(R.mipmap.un_fill);
                break;
            case 2:
                holder.r1.setImageResource(R.mipmap.fill);
                holder.r2.setImageResource(R.mipmap.fill);
                holder.r3.setImageResource(R.mipmap.un_fill);
                holder.r4.setImageResource(R.mipmap.un_fill);
                holder.r5.setImageResource(R.mipmap.un_fill);
                break;
            case 3:
                holder.r1.setImageResource(R.mipmap.fill);
                holder.r2.setImageResource(R.mipmap.fill);
                holder.r3.setImageResource(R.mipmap.fill);
                holder.r4.setImageResource(R.mipmap.un_fill);
                holder.r5.setImageResource(R.mipmap.un_fill);
                break;
            case 4:
                holder.r1.setImageResource(R.mipmap.fill);
                holder.r2.setImageResource(R.mipmap.fill);
                holder.r3.setImageResource(R.mipmap.fill);
                holder.r4.setImageResource(R.mipmap.fill);
                holder.r5.setImageResource(R.mipmap.un_fill);
                break;
            case 5:
                holder.r1.setImageResource(R.mipmap.fill);
                holder.r2.setImageResource(R.mipmap.fill);
                holder.r3.setImageResource(R.mipmap.fill);
                holder.r4.setImageResource(R.mipmap.fill);
                holder.r5.setImageResource(R.mipmap.fill);
                break;
            default:
                holder.r1.setImageResource(R.mipmap.un_fill);
                holder.r2.setImageResource(R.mipmap.un_fill);
                holder.r3.setImageResource(R.mipmap.un_fill);
                holder.r4.setImageResource(R.mipmap.un_fill);
                holder.r5.setImageResource(R.mipmap.un_fill);
                break;
        }

        if (position % 2 == 1)
            holder.review_bg.setBackground(getContext().getResources().getDrawable(R.drawable.rev_bg));
        else
            holder.review_bg.setBackground(getContext().getResources().getDrawable(R.drawable.rev_bg));

        return alertView;
    }

    private static class ViewHolder {
        TextView comts;
        TextView user_name;
        TextView rev_date;
        ImageView r1, r2, r3, r4, r5;
        TextView user_first;
        LinearLayout review_bg;

    }
}