package com.iamretailer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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

    private final LayoutInflater inflater;
    private final ArrayList<FilterPO> items;
    private final Context context;


    public FilterSubAdapter(Context ctx, ArrayList<FilterPO> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.filter_sub_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.filter_name.setText(items.get(position).getFilter_name());

        if (items.get(position).isSelected())
        {
            holder.select.setImageResource(R.mipmap.filter_checked);
        }
        else
        {
            holder.select.setImageResource(R.mipmap.filter_unchec);
        }



        if (items.get(position).getFilter_name().contains("price"))
        {
         holder.item_select.setVisibility(View.GONE);
         holder.price_view.setVisibility(View.VISIBLE);


            holder.price_values.setRange(Math.round(items.get(position).getPrice_values()),Math.round(items.get(position).getPrice_values0()));
            holder.price_values.setProgress(Math.round(items.get(position).getSeek_min()),Math.round(items.get(position).getSeek_max()));
            String b=Math.round(items.get(position).getSeek_min())+"";
            String b1=Math.round(items.get(position).getSeek_max())+"";
            holder.textMin1.setText(b);
            holder.textMax1.setText(b1);


            if (items.get(position).isSelected()) {
                holder.price_values.setRange(Math.round(items.get(position).getPrice_values()),Math.round(items.get(position).getPrice_values0()));
                holder.price_values.setProgress(Math.round(items.get(position).getSeek_min()),Math.round(items.get(position).getSeek_max()));

                String h=Math.round(items.get(position).getSeek_min())+"";
                String k=Math.round(items.get(position).getSeek_max())+"";
                holder.textMin1.setText(h);
                holder.textMax1.setText(k);

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

                items.get(position).setSeek_min(Math.round(leftValue));
                items.get(position).setSeek_max(Math.round(rightValue));
                items.get(position).setSelected(true);
                String n=Math.round(items.get(position).getSeek_min())+"";
                String kk=Math.round(items.get(position).getSeek_max())+"";
                holder.textMin1.setText(n);
                holder.textMax1.setText(kk);
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
        final ImageView select;
        final TextView filter_name;
        final TextView textMin1;
        final TextView textMax1;
        final RangeSeekBar price_values;
        final LinearLayout price_view;
        final LinearLayout item_select;



        MyViewHolder(View itemView) {
            super(itemView);

            select = itemView.findViewById(R.id.select);
            filter_name = itemView.findViewById(R.id.sub_name);
            price_values= itemView.findViewById(R.id.price_values);
            price_view= itemView.findViewById(R.id.price_view);
            item_select= itemView.findViewById(R.id.item_select);
            textMax1= itemView.findViewById(R.id.textMax1);
            textMin1= itemView.findViewById(R.id.textMin1);



        }
    }
}

