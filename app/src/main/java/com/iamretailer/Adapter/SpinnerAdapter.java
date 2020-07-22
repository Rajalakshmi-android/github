package com.iamretailer.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iamretailer.POJO.SingleOptionPO;
import com.iamretailer.R;


import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {

    private final int from;
    private Context context;
    private ArrayList<SingleOptionPO> items;

    public SpinnerAdapter(Context context, ArrayList<SingleOptionPO> stateData, int from) {
        this.context = context;
        this.items = stateData;
        this.from = from;
    }

    @Override
    public int getCount() {

        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt, 0);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt, 1);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent, int loc) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.drop_downitem, parent, false);
        TextView main_text = (TextView) mySpinner.findViewById(R.id.name);
        main_text.setText(items.get(position).getName());

        return mySpinner;
    }

    static class ViewHolder {
        public TextView name;
    }


}