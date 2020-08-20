package com.iamretailer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.OptionsPO;
import com.iamretailer.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Locale;

public class WalletAdapter extends ArrayAdapter<OptionsPO> {
    private final Context context;
    private final ArrayList<OptionsPO> items;
    private final int res;
    private final LayoutInflater mInflater;
    private final int from;
    private final String cur_left ;
    private final String cur_right ;

    public WalletAdapter(Context context, int resource, ArrayList<OptionsPO> items, int from) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.res = resource;
        this.context = context;
        this.items = items;
        this.from=from;
        DBController db = new DBController(getContext());
        cur_left = db.get_cur_Left();
        cur_right= db.get_cur_Right();
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
        FrameLayout alertView ;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(res, null, true);
            convertView.setTag(holder);
            alertView = (FrameLayout) convertView;
        } else {
            alertView = (FrameLayout) convertView;
        }

        holder.select= convertView.findViewById(R.id.select);
        holder.product_name = convertView.findViewById(R.id.p_name);
        holder.prod_offer_rate = convertView.findViewById(R.id.p_rate1);
        holder.prdoct_img = convertView.findViewById(R.id.p_image);
        holder.orginal_rate = convertView.findViewById(R.id.orginal);


        holder.product_name.setText(items.get(position).getName());
        if(items.get(position).getOffer_rate() > 0) {
            String val=cur_left + String.format(Locale.ENGLISH,"%.2f", items.get(position).getOffer_rate())+cur_right;
            String val1=cur_left + String.format(Locale.ENGLISH,"%.2f", items.get(position).getRate())+cur_right;
            holder.prod_offer_rate.setText(val);
            holder.orginal_rate.setText(val1);
        }else{
            String val=cur_left + String.format(Locale.ENGLISH,"%.2f", items.get(position).getRate())+cur_right;
            holder.prod_offer_rate.setText(val);
            holder.orginal_rate.setText("");
        }
        if (items.get(position).getImage()!=null && items.get(position).getImage().length()>0)
         Picasso.with(getContext()).load(items.get(position).getImage()).into(holder.prdoct_img);
        else
            Picasso.with(context).load(R.mipmap.place_holder).into(holder.prdoct_img);


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
        ImageView select;
        TextView product_name;
        TextView prod_offer_rate;
        TextView orginal_rate;
        ImageView prdoct_img;
    }
}



