package com.iamretailer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iamretailer.POJO.TypePO;
import com.iamretailer.R;

import java.util.ArrayList;


public class PaymentAdapter extends ArrayAdapter<TypePO>

{

    private final LayoutInflater mInflater;
    private final ArrayList<TypePO> items;
    private int pos = -1;
    private final int form;

    public PaymentAdapter(Context context, int resource, ArrayList<TypePO> item, int from) {
        super(context, resource, item);
        // TODO Auto-generated method stub
        mInflater = LayoutInflater.from(context);
        this.items = item;
        this.form = from;
    }

    public void setChild1(int newClickedChildPosition) {
        this.pos = newClickedChildPosition;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.payment_item, null);
            convertView.setTag(holder);
        }
        holder.text = convertView.findViewById(R.id.payment_text);
        holder.paymentimage = convertView.findViewById(R.id.paymentimg);

        if (form == 0) {
            holder.text.setText(items.get(position).getTitle());
        } else {
            String textss = items.get(position).getTitle() + " - " + items.get(position).getAmount();
            holder.text.setText(textss);
        }

        if (position == pos) {
            holder.paymentimage.setImageResource(R.mipmap.addres_selects);
            notifyDataSetChanged();

        } else {
            holder.paymentimage.setImageResource(R.mipmap.gray_circle1);
        }

        return convertView;

    }

    class ViewHolder {
        TextView text;
        ImageView paymentimage;
    }

}
