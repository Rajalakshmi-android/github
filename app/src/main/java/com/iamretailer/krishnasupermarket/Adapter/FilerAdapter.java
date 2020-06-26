package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.POJO.BrandsPO;
import com.iamretailer.krishnasupermarket.R;
import com.logentries.android.AndroidLogger;

import java.util.ArrayList;


public class FilerAdapter extends RecyclerView.Adapter<FilerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BrandsPO> list;
    Context context;
    AndroidLogger logger;

    public FilerAdapter(Context ctx, ArrayList<BrandsPO> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.list = imageModelArrayList;
        this.context = ctx;
        logger = AndroidLogger.getLogger(context, Appconstatants.LOG_ID, false);
    }

    @Override
    public FilerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.filter_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final FilerAdapter.MyViewHolder holder, final int position) {


        holder.name.setText(list.get(position).getStore_name());

        if (list.get(position).isSelect())
            holder.cat_img.setImageResource(R.mipmap.checked);
        else
            holder.cat_img.setImageResource(R.mipmap.unchecked);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        FrameLayout items_bg;
        ImageView cat_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.store_name);
            items_bg = (FrameLayout) itemView.findViewById(R.id.item_bg);
            cat_img = (ImageView) itemView.findViewById(R.id.cat_img);
        }
    }
}
