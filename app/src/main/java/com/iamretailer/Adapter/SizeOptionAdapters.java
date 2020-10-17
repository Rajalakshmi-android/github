package com.iamretailer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.iamretailer.POJO.SingleOptionPO;
import com.iamretailer.R;

import java.util.ArrayList;

public class SizeOptionAdapters extends RecyclerView.Adapter<SizeOptionAdapters.MyViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<SingleOptionPO> items;
    private final Context context;

    public SizeOptionAdapters(Context ctx, ArrayList<SingleOptionPO> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.option, parent, false);
        return new MyViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        if (items.get(position).getImga_url().isEmpty()) {
            if (items.get(position).isImgSel()) {
                holder.name.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.txt_bg.setBackgroundResource(R.drawable.sizegreen);
            } else {
                holder.name.setTextColor(ContextCompat.getColor(context, R.color.app_text_color));
                holder.txt_bg.setBackgroundResource(R.drawable.size_grey_bg);
            }

        } else {
            if (items.get(position).isImgSel()) {
                holder.name.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.txt_bg.setBackgroundResource(R.drawable.sizegreen);
            } else {
                holder.name.setTextColor(ContextCompat.getColor(context, R.color.app_text_color));
                holder.txt_bg.setBackgroundResource(R.drawable.size_grey_bg);
            }
        }
        holder.name.setText(items.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final FrameLayout txt_bg;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.child);
            txt_bg = itemView.findViewById(R.id.text_bg);

        }
    }


}
