package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.iamretailer.krishnasupermarket.Category_level;
import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.POJO.BrandsPO;
import com.iamretailer.krishnasupermarket.R;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.iamretailer.krishnasupermarket.AppClass.getContext;


public class BrandzAdapter extends RecyclerView.Adapter<BrandzAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BrandsPO> list;
    Context context;
    DBController dbController;
    String cur_left = "";
    String cur_right = "";
    AndroidLogger logger;
    int frm;
    int widths;
    int height;

    public BrandzAdapter(Context ctx, ArrayList<BrandsPO> imageModelArrayList, int from, int widths, int height) {

        inflater = LayoutInflater.from(ctx);
        this.list = imageModelArrayList;
        this.context = ctx;
        this.frm = from;
        this.widths = widths;
        this.height = height;
        dbController = new DBController(context);
        cur_left = dbController.get_cur_Left();
        cur_right = dbController.get_cur_Right();
        Appconstatants.CUR = dbController.getCurCode();
        logger = AndroidLogger.getLogger(context, Appconstatants.LOG_ID, false);
    }

    @Override
    public BrandzAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cat_list2copy, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final BrandzAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getStore_name());


        if (frm == 1)

        {
            if (position == 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.dp95), LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins((int) context.getResources().getDimension(R.dimen.dp15), 0, (int) context.getResources().getDimension(R.dimen.dp10), 0);
                holder.items_bg.setLayoutParams(lp);
            } else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.dp95), LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, (int) context.getResources().getDimension(R.dimen.dp10), 0);
                holder.items_bg.setLayoutParams(lp);

            }

        } else {
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            float px1 = (float) ((widthPixels - (20 * context.getResources().getDisplayMetrics().density)) / 3.5);
            holder.items_bg.getLayoutParams().width = (int) px1;
            holder.items_bg.setPadding(0, 0, 0, (int) context.getResources().getDimension(R.dimen.dp5));

        }


        holder.items_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getArrayList().size() == 0) {
                    Intent u = new Intent(context, Allen.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", list.get(position).getS_id());
                    bundle.putString("cat_name", list.get(position).getStore_name());
                    u.putExtras(bundle);
                    context.startActivity(u);
                } else {
                    Intent intent = new Intent(context, Category_level.class);
                    Bundle pos = new Bundle();
                    pos.putInt("pos", position);
                    intent.putExtras(pos);
                    context.startActivity(intent);
                }

            }
        });

     /*   if (frm == 1) {

            if (position == 1) {

                holder.frame.setBackgroundResource(R.mipmap.cate_violetbg);

            } else if (position == 2) {
                holder.frame.setBackgroundResource(R.mipmap.cate_greenbg);
            } else if (position == 3) {
                holder.frame.setBackgroundResource(R.mipmap.cate_orangebg);
            } else {
                holder.frame.setBackgroundResource(R.mipmap.cate_pinkbg);
            }
        } else {
            int j;
            if (position >= 4) {
                j = position % 4;
            } else {
                j = position;
            }
            int pos = j + 1;
            Log.i("tag", "colorrrrrr " + position);

            if (pos == 1) {
                holder.frame.setBackgroundResource(R.mipmap.cate_pinkbg);
            } else if (pos == 2) {
                holder.frame.setBackgroundResource(R.mipmap.cate_violetbg);

            } else if (pos == 3) {
                holder.frame.setBackgroundResource(R.mipmap.cate_greenbg);
            } else if (pos == 4) {
                holder.frame.setBackgroundResource(R.mipmap.cate_orangebg);
            }
        }*/


        if (list.get(position).getBg_img_url().isEmpty())
            holder.cat_img.setImageResource(R.mipmap.place_holder);
        else
            Picasso.with(getContext()).load(list.get(position).getBg_img_url()).into(holder.cat_img);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frame;
        TextView name;
        LinearLayout items_bg;
        ImageView cat_img;

        public MyViewHolder(View itemView) {
            super(itemView);

            frame = (FrameLayout) itemView.findViewById(R.id.brand);
            name = (TextView) itemView.findViewById(R.id.store_name);
            items_bg = (LinearLayout) itemView.findViewById(R.id.item_bg);
            cat_img=(ImageView)itemView.findViewById(R.id.cat_img);
        }
    }
}
