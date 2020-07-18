package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iamretailer.krishnasupermarket.POJO.FilterPO;
import com.iamretailer.krishnasupermarket.R;

import java.util.ArrayList;

public class FilterSubAdapter extends RecyclerView.Adapter<FilterSubAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<FilterPO> items;
    Context context;


    public FilterSubAdapter(Context ctx, ArrayList<FilterPO> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.filter_sub_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.filter_name.setText(items.get(position).getFilter_name());

        if (items.get(position).isSelected())
        {
            holder.select.setImageResource(R.mipmap.filter_checked);


        }
        else
        {
            holder.select.setImageResource(R.mipmap.filter_unchec);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView select;
        TextView filter_name;



        public MyViewHolder(View itemView) {
            super(itemView);

            select = (ImageView) itemView.findViewById(R.id.select);
            filter_name = (TextView) itemView.findViewById(R.id.sub_name);


        }
    }
}

