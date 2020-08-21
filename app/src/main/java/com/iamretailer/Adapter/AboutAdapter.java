package com.iamretailer.Adapter;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.POJO.OptionsPO;
import com.iamretailer.R;

import java.util.ArrayList;

public class AboutAdapter extends ArrayAdapter<OptionsPO> {
    private final int resource;
    private final ArrayList<OptionsPO> items;
    private final LayoutInflater mInflater;

    public AboutAdapter(Activity context, int resource, ArrayList<OptionsPO> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.items = items;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        LinearLayout alertView;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(resource, null, true);
            convertView.setTag(holder);
            alertView = (LinearLayout) convertView;
        } else {
            alertView = (LinearLayout) convertView;
        }
        holder.product_name = convertView.findViewById(R.id.title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.product_name.setText(Html.fromHtml(items.get(position).getTitle(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.product_name.setText(Html.fromHtml(items.get(position).getTitle()));
        }

        return alertView;
    }


    private class ViewHolder {
        TextView product_name;

    }


}
