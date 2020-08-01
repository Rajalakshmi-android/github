package com.iamretailer.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.Allen;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.OptionsPO;
import com.logentries.android.AndroidLogger;
import com.iamretailer.R;

import java.util.ArrayList;

public class AboutAdapter extends ArrayAdapter<OptionsPO> {
    int resource;
    Context context;
    ArrayList<OptionsPO> items;
    LayoutInflater mInflater;
    Allen allen;
    DBController db;
    private String bwar, bname;
    ArrayList<OptionsPO> optionsPOArrayList;
    AndroidLogger logger;

    public AboutAdapter(Activity context, int resource, ArrayList<OptionsPO> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.context = context;
        this.items = items;
        allen = new Allen();
        db = new DBController(getContext());
        optionsPOArrayList = new ArrayList<>();
        logger = AndroidLogger.getLogger(context, Appconstatants.LOG_ID, false);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LinearLayout alertView = null;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(resource, alertView, true);
            convertView.setTag(holder);
            alertView = (LinearLayout) convertView;
        } else {
            alertView = (LinearLayout) convertView;
        }
        holder.product_name = (TextView) convertView.findViewById(R.id.title);

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      holder.product_name.setText(Html.fromHtml(items.get(position).getTitle(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
      holder.product_name.setText(Html.fromHtml(items.get(position).getTitle()));
                        }

       // holder.product_name.setText(items.get(position).getTitle());





        return alertView;
    }


    private static class ViewHolder {
        public TextView product_name;

    }







}
