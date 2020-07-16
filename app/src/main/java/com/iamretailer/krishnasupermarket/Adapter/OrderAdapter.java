package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.POJO.OrdersPO;
import com.iamretailer.krishnasupermarket.R;


import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<OrdersPO> {

    Context context;
    ArrayList<OrdersPO> items;
    int res;
    LayoutInflater mInflater;
    DBController dbController;
    String cur_left = "";
    String cur_right = "";

    public OrderAdapter(Context context, int resource, ArrayList<OrdersPO> items) {
        super(context, resource, items);
        this.context = context;
        this.res = resource;
        this.items = items;
        mInflater = LayoutInflater.from(context);
        dbController=new DBController(context);
        cur_left = dbController.get_cur_Left();
        cur_right=dbController.get_cur_Right();
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
        holder.order_id = (TextView) convertView.findViewById(R.id.orderid);
        holder.order = (TextView) convertView.findViewById(R.id.order);
        holder.order_placed = (TextView) convertView.findViewById(R.id.daytime);
        holder.order_amount = (TextView) convertView.findViewById(R.id.totalamount);
        holder.product = (TextView) convertView.findViewById(R.id.quan);
        holder.status = (TextView) convertView.findViewById(R.id.status);
        holder.symbol = (TextView) convertView.findViewById(R.id.symbol);
        holder.symbol1 = (TextView) convertView.findViewById(R.id.symbol1);
        holder.image = (ImageView) convertView.findViewById(R.id.image);
        holder.symbol.setText(cur_left);
        holder.symbol1.setText(cur_right);


        holder.order_id.setText(items.get(position).getPayment() + "");
        holder.order.setText(items.get(position).getOrder_id() + "");
        holder.product.setText(items.get(position).getOrder_products() + "");
        holder.status.setText(items.get(position).getOrder_status() + "");
        holder.order_placed.setText(items.get(position).getOrder_date());
        Log.i("tag", "place_date " + items.get(position).getOrder_date());
        holder.order_amount.setText(String.format("%.2f", Double.parseDouble(items.get(position).getOrder_total_raw())));
        Log.i("tag", "place_date " + items.get(position).getOrder_status());
        if(items.get(position).getOrder_status().equalsIgnoreCase("Pending")){
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.orange_status));
            holder.image.setImageResource(R.mipmap.pending_my);
            holder.image.setVisibility(View.VISIBLE);

        }else if(items.get(position).getOrder_status().equalsIgnoreCase("Failed")){
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.image.setVisibility(View.GONE);

        }else if(items.get(position).getOrder_status().equalsIgnoreCase("Success")){
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.green_status));
            holder.image.setImageResource(R.mipmap.success);
            holder.image.setVisibility(View.VISIBLE);
        }
        else{
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.image.setVisibility(View.GONE);
        }

        return alertView;
    }

    private static class ViewHolder {
        public TextView order_id, subtotal1, order_placed, charges, order_amount, order, product, status,symbol,symbol1;
        public ImageView image;


    }


}
