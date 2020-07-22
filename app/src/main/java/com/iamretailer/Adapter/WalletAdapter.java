package com.iamretailer.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Address;
import com.iamretailer.AddressList;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.AddressPO;
import com.iamretailer.POJO.OptionsPO;
import com.iamretailer.R;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class WalletAdapter extends ArrayAdapter<OptionsPO> {
    private final DBController db;
    private final ArrayList<Object> optionsPOArrayList;


    Context context;
    ArrayList<OptionsPO> items;
    int res;
    LayoutInflater mInflater;
    AndroidLogger logger;
    int from;
    String cur_left = "";
    String cur_right = "";

    public WalletAdapter(Context context, int resource, ArrayList<OptionsPO> items, int from) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.res = resource;
        this.context = context;
        this.items = items;
        this.from=from;
        db = new DBController(getContext());
        optionsPOArrayList = new ArrayList<>();
        cur_left = db.get_cur_Left();
        cur_right=db.get_cur_Right();
        logger=AndroidLogger.getLogger(context, Appconstatants.LOG_ID,false);
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
        FrameLayout alertView = null;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(res, alertView, true);
            convertView.setTag(holder);
            alertView = (FrameLayout) convertView;
        } else {
            alertView = (FrameLayout) convertView;
        }

        holder.select=(ImageView) convertView.findViewById(R.id.select);
        holder.add=(LinearLayout)convertView.findViewById(R.id.add);
        holder.product_name = (TextView) convertView.findViewById(R.id.p_name);
        holder.prod_offer_rate = (TextView) convertView.findViewById(R.id.p_rate1);
        holder.prdoct_img = (ImageView) convertView.findViewById(R.id.p_image);
        holder.full = (LinearLayout) convertView.findViewById(R.id.fullview);
        holder.orginal_rate = (TextView) convertView.findViewById(R.id.orginal);


        holder.product_name.setText(items.get(position).getName());
        if(items.get(position).getOffer_rate() > 0) {
            holder.prod_offer_rate.setText(cur_left + String.format("%.2f", items.get(position).getOffer_rate())+cur_right);
            holder.orginal_rate.setText(cur_left + String.format("%.2f", items.get(position).getRate())+cur_right);
        }else{
            holder.prod_offer_rate.setText(cur_left + String.format("%.2f", items.get(position).getRate())+cur_right);
            holder.orginal_rate.setText("");
        }


        Picasso.with(getContext()).load(items.get(position).getImage()).into(holder.prdoct_img);


        if (from==1) {
            if (items.get(position).isSelect()) {
               holder.select.setVisibility(View.VISIBLE);

            } else {
                holder.select.setVisibility(View.GONE);

            }
        }
        else
        {
            holder.select.setVisibility(View.GONE);
        }


        return alertView;
    }



    private  class ViewHolder {

        public TextView city,state,country,delete;
        ImageView select;
        LinearLayout add;
        public TextView product_name;
        public TextView prod_offer_rate, orginal_rate;
        public ImageView prdoct_img;
        public LinearLayout full;

    }





}



