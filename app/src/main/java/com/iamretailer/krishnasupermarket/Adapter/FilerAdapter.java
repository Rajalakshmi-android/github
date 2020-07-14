package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.krishnasupermarket.Allen;
import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.POJO.BrandsPO;
import com.iamretailer.krishnasupermarket.R;
import com.logentries.android.AndroidLogger;

import java.util.ArrayList;


public class FilerAdapter extends RecyclerView.Adapter<FilerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BrandsPO> list;
    private ArrayList<BrandsPO> items;
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

        if (list.get(position).isSelect()){
            holder.cat_img.setVisibility(View.VISIBLE);
            holder.cat_img1.setVisibility(View.GONE);
        }

        else{
            holder.cat_img1.setVisibility(View.VISIBLE);
            holder.cat_img.setVisibility(View.GONE);
        }

        holder.cat_imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cat_img.getVisibility()==View.VISIBLE) {
                    holder.cat_img.setVisibility(View.GONE);
                    holder.cat_img1.setVisibility(View.VISIBLE);
                    list.get(position).setSelect(false);
                    Log.i("dsfsfdsf","notcheck"+position+"  "+list.get(position).getStore_name());
                } else {
                    holder.cat_img.setVisibility(View.VISIBLE);
                    holder.cat_img1.setVisibility(View.GONE);
                    list.get(position).setSelect(true);
                    Log.i("dsfsfdsf","ischecked"+position+"  "+list.get(position).getStore_name());
                }


                }

        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        FrameLayout items_bg,cat_imgs;
        ImageView cat_img,cat_img1;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.store_name);
            items_bg = (FrameLayout) itemView.findViewById(R.id.item_bg);
            cat_imgs = (FrameLayout) itemView.findViewById(R.id.cat_imgs);
            cat_img = (ImageView) itemView.findViewById(R.id.cat_img);
            cat_img1 = (ImageView) itemView.findViewById(R.id.cat_img1);
        }
    }
}
