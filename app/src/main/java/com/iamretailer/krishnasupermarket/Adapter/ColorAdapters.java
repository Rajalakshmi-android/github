package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.krishnasupermarket.POJO.SingleOptionPO;
import com.iamretailer.krishnasupermarket.R;

import java.util.ArrayList;

public class ColorAdapters extends RecyclerView.Adapter<ColorAdapters.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<SingleOptionPO> items;
    Context context;

    public ColorAdapters(Context ctx, ArrayList<SingleOptionPO> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.color_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.d("value_","asdfadf");

        try {
            GradientDrawable drawable = (GradientDrawable) holder.color.getBackground();
            drawable.setColor(Color.parseColor(items.get(position).getName()));
        } catch (Exception e) {
            Log.d("value_", e.toString());
        }


        if (items.get(position).isImgSel()) {
            holder.colortic.setVisibility(View.VISIBLE);
            holder.text_bg.setBackgroundResource(R.drawable.black_rounds);
        } else {

            holder.colortic.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout colortic;
        TextView color;
        FrameLayout text_bg;

        public MyViewHolder(View itemView) {
            super(itemView);
            colortic = (LinearLayout) itemView.findViewById(R.id.colortic);
            text_bg = (FrameLayout) itemView.findViewById(R.id.txt_bg);
            color = (TextView) itemView.findViewById(R.id.colortxt);

        }
    }


}
