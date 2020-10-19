package com.iamretailer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.OrdersPO;
import com.iamretailer.R;

import java.util.ArrayList;
import java.util.Locale;

public class ReturnlistAdapter extends ArrayAdapter<OrdersPO> {

    private final Context context;
    private final ArrayList<OrdersPO> items;
    private final int res;
    private final LayoutInflater mInflater;
    private final String cur_left;
    private final String cur_right;

    public ReturnlistAdapter(Context context, int resource, ArrayList<OrdersPO> items) {
        super(context, resource, items);
        this.context = context;
        this.res = resource;
        this.items = items;
        mInflater = LayoutInflater.from(context);
        DBController dbController = new DBController(context);
        cur_left = dbController.get_cur_Left();
        cur_right = dbController.get_cur_Right();
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
        LinearLayout alertView;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(res, null, true);
            convertView.setTag(holder);
            alertView = (LinearLayout) convertView;
        } else {
            alertView = (LinearLayout) convertView;
        }

        holder.order = convertView.findViewById(R.id.order);
        holder.order_placed = convertView.findViewById(R.id.daytime);
        holder.status = convertView.findViewById(R.id.status);
        holder.return_id = convertView.findViewById(R.id.return_id);
        holder.order.setText(items.get(position).getOrder_id());
        holder.status.setText(items.get(position).getOrder_status());
        holder.order_placed.setText(items.get(position).getOrder_date());
        holder.return_id.setText(items.get(position).getReturn_id());



        return alertView;
    }

    private static class ViewHolder {

        TextView order_placed;
        TextView order;
        TextView status,return_id;



    }

}
