package com.iamretailer.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.POJO.TypePO;
import com.iamretailer.R;

import java.util.ArrayList;


public class PaymentAdapter extends ArrayAdapter<TypePO>

{

    private LayoutInflater mInflater;
    private int resource;
    private Context context;
    private ViewHolder holder;
    ArrayList<TypePO> items;
    int pos = -1;
    int form;

    public PaymentAdapter(Context context, int resource, ArrayList<TypePO> item, int from) {
        super(context, resource, item);
        // TODO Auto-generated method stub
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.context = context;
        this.items = item;
        this.form = from;
    }
    public void setChild1(int newClickedChildPosition) {
        this.pos = newClickedChildPosition;
        Log.i("pos", pos + "");
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LinearLayout alertView = null;
        holder = new ViewHolder();
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.payment_item, null);
            convertView.setTag(holder);

        }
        holder.paylinear = convertView.findViewById(R.id.paylayout);
        holder.text = convertView.findViewById(R.id.payment_text);
        holder.paymentimage = convertView.findViewById(R.id.paymentimg);

        if (form == 0) {
            holder.text.setText(items.get(position).getTitle());

        } else {
            holder.text.setText(items.get(position).getTitle() + " - " + items.get(position).getAmount());

        }

        if (position == pos) {
            holder.paymentimage.setImageResource(R.mipmap.addres_selects);
            notifyDataSetChanged();
            Log.i("pos11", pos + "");

        } else {
            holder.paymentimage.setImageResource(R.mipmap.gray_circle1);

        }

        return convertView;

    }

    class ViewHolder {
        LinearLayout paylinear;
        TextView text;
        ImageView paymentimage;
    }

}
