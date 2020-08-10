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
    private LayoutInflater inflater;
    private ArrayList<SingleOptionPO> items;
    Context context;
    int from;

    public SizeOptionAdapters(Context ctx, ArrayList<SingleOptionPO> imageModelArrayList,int i) {

        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
        this.from=i;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.option, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.d("value_","asdfadf");


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

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public FrameLayout txt_bg;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.child);
            txt_bg = (FrameLayout) itemView.findViewById(R.id.text_bg);

        }
    }


}
