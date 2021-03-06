package com.iamretailer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iamretailer.POJO.CountryPO;
import com.iamretailer.R;

import java.util.ArrayList;

public class StateAdapter extends ArrayAdapter<CountryPO> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final ArrayList<CountryPO> items;
    private final int mResource;

    public StateAdapter(Context context, int resource, ArrayList<CountryPO> objects) {
        super(context, resource, 0, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        TextView textView = view.findViewById(R.id.txt);

        if (position == 0) {
            textView.setTextColor(mContext.getResources().getColor(R.color.plceholder));

        }
        textView.setText(items.get(position).getCount_name());


        return view;
    }
}
