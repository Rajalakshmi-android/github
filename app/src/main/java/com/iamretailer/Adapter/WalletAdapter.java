package com.iamretailer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.OptionsPO;
import com.iamretailer.R;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Locale;

public class WalletAdapter extends ArrayAdapter<OptionsPO> {
    private final DBController db;



    Context context;
    ArrayList<OptionsPO> items;
    int res;
    LayoutInflater mInflater;
    AndroidLogger logger;
    int from;
    String cur_left ;
    String cur_right ;

    public WalletAdapter(Context context, int resource, ArrayList<OptionsPO> items, int from) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.res = resource;
        this.context = context;
        this.items = items;
        this.from=from;
        db = new DBController(getContext());
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
            String val=cur_left + String.format("%.2f", items.get(position).getOffer_rate())+cur_right;
            String val1=cur_left + String.format("%.2f", items.get(position).getRate())+cur_right;
            holder.prod_offer_rate.setText(val);
            holder.orginal_rate.setText(val1);
        }else{
            String val=cur_left + String.format("%.2f", items.get(position).getRate())+cur_right;
            holder.prod_offer_rate.setText(val);
            holder.orginal_rate.setText("");
        }
        if (items.get(position).getImage().length()>0)
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
        public TextView product_name;
        public TextView prod_offer_rate, orginal_rate;
        public ImageView prdoct_img;
    }
}



