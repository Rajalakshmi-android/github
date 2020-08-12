package com.iamretailer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.Common.widget.OnRangeChangedListener;
import com.iamretailer.Common.widget.RangeSeekBar;
import com.iamretailer.Filter;
import com.iamretailer.POJO.FilterPO;
import com.iamretailer.R;

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



        if (items.get(position).getFilter_name().equalsIgnoreCase("price_range"))
        {
         holder.item_select.setVisibility(View.GONE);
         holder.price_view.setVisibility(View.VISIBLE);


            holder.price_values.setRange(Math.round(items.get(position).getPrice_values()),Math.round(items.get(position).getPrice_values0()));
            holder.price_values.setProgress(Math.round(items.get(position).getSeek_min()),Math.round(items.get(position).getSeek_max()));

            holder.textMin1.setText(Math.round(items.get(position).getSeek_min())+"");
            holder.textMax1.setText(Math.round(items.get(position).getSeek_max())+"");


            if (items.get(position).isSelected()) {
                holder.price_values.setRange(Math.round(items.get(position).getPrice_values()),Math.round(items.get(position).getPrice_values0()));
                holder.price_values.setProgress(Math.round(items.get(position).getSeek_min()),Math.round(items.get(position).getSeek_max()));

                holder.textMin1.setText(Math.round(items.get(position).getSeek_min())+"");
                holder.textMax1.setText(Math.round(items.get(position).getSeek_max())+"");

            }
            else {
                holder.price_values.setRange(Math.round(items.get(position).getPrice_values()),Math.round(items.get(position).getPrice_values0()));
            }
        }
        else
        {
            holder.item_select.setVisibility(View.VISIBLE);
            holder.price_view.setVisibility(View.GONE);

        }

        holder.price_values.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

                items.get(position).setSeek_min(Math.round((float) leftValue));
                items.get(position).setSeek_max(Math.round((float) rightValue));
                items.get(position).setSelected(true);
                holder.textMin1.setText(Math.round(items.get(position).getSeek_min())+"");
                holder.textMax1.setText(Math.round(items.get(position).getSeek_max())+"");
                ((Filter)context).change_bg();


            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });





    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView select;
        TextView filter_name,textMin1,textMax1;
        RangeSeekBar price_values;
        LinearLayout price_view,item_select;



        public MyViewHolder(View itemView) {
            super(itemView);

            select = (ImageView) itemView.findViewById(R.id.select);
            filter_name = (TextView) itemView.findViewById(R.id.sub_name);
            price_values=(RangeSeekBar) itemView.findViewById(R.id.price_values);
            price_view=(LinearLayout)itemView.findViewById(R.id.price_view);
            item_select=(LinearLayout)itemView.findViewById(R.id.item_select);
            textMax1=(TextView)itemView.findViewById(R.id.textMax1);
            textMin1=(TextView)itemView.findViewById(R.id.textMin1);



        }
    }
}

