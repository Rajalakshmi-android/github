package com.iamretailer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CircleTransforms;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.BannerBo;
import com.iamretailer.Product_list;
import com.iamretailer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SubBannerAdapter extends RecyclerView.Adapter<SubBannerAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<BannerBo> items;
    private final Context context;


    public SubBannerAdapter(Context ctx, ArrayList<BannerBo> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
        DBController dbController = new DBController(context);
        Appconstatants.CUR = dbController.getCurCode();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.activity_sub_banner, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        holder.banners.getLayoutParams().width = (int) (widthPixels /2.1);
        holder.banners.getLayoutParams().height = (int) (widthPixels /2.1);

        if (items.get(position).getImage() != null) {
            if (items.get(position).getImage().isEmpty()) {
                Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.bg_img);
            } else {

                float density = context.getResources().getDisplayMetrics().density;
                int px = 10 * Math.round(density);
                Picasso.with(context).load(items.get(position).getImage()).fit().placeholder(R.mipmap.place_holder).into(holder.bg_img);
            }
        } else {
            Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.bg_img);
        }

        holder.banners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Appconstatants.need_brand_product == 1) {
                    Intent intent = new Intent(context, Product_list.class);
                    Bundle best = new Bundle();
                    best.putString("view_all", "banners");
                    best.putString("head", "");
                    best.putString("banner_id", items.get(position).getBanner_id());
                    best.putString("title", items.get(position).getLink());
                    intent.putExtras(best);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        final ImageView bg_img;
        final LinearLayout banners;

        MyViewHolder(View itemView) {
            super(itemView);

            bg_img = itemView.findViewById(R.id.browsebackground);
            banners = itemView.findViewById(R.id.banners);


        }
    }
}
