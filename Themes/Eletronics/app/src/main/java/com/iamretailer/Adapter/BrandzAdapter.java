package com.iamretailer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.Allen;
import com.iamretailer.Category_level;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.BrandsPO;
import com.iamretailer.R;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

    public BrandzAdapter(Context ctx, ArrayList<BrandsPO> imageModelArrayList, int from,int widths,int height) {

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
        View view = inflater.inflate(R.layout.cat_list2, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final BrandzAdapter.MyViewHolder holder, final int position) {

        if (Build.VERSION.SDK_INT >= 24) {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name()));
        }
        if (frm == 1)

        {
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            float px1 = (float) ((widthPixels - (20 * context.getResources().getDisplayMetrics().density)) /4.5);
            holder.items_bg.getLayoutParams().width = (int) px1;
            holder.name.setSingleLine(true);
          /*  float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            float px1 = (float) ((widthPixels - (20 * context.getResources().getDisplayMetrics().density)) / 3.5);
            holder.items_bg.getLayoutParams().width = (int) px1;
            holder.items_bg.setPadding(0, 0, 0, (int) context.getResources().getDimension(R.dimen.dp15));
*/
          /*  if (position == 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.dp95), LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins((int) context.getResources().getDimension(R.dimen.dp15), 0, (int) context.getResources().getDimension(R.dimen.dp15), 0);
                holder.items_bg.setLayoutParams(lp);
            } else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.dp95), LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, (int) context.getResources().getDimension(R.dimen.dp15), 0);
                holder.items_bg.setLayoutParams(lp);

            }*/

        } else {
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            float px1 = (float) ((widthPixels - (20 * context.getResources().getDisplayMetrics().density)) / 3.5);
            holder.items_bg.getLayoutParams().width = (int) px1;
            holder.items_bg.setPadding(0, 0, 0, (int) context.getResources().getDimension(R.dimen.dp15));


        }
        if (list.get(position).getBg_img_url()!=null) {
            if (list.get(position).getBg_img_url().isEmpty()) {
                Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.bg_img);
            } else {
                Log.i("imagesss",list.get(position).getBg_img_url()+"");
                Picasso.with(context).load(list.get(position).getBg_img_url()).placeholder(R.mipmap.place_holder).into(holder.bg_img);

            }
        }
        else
        {
            Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.bg_img);
        }

       /* holder.items_bg.setOnClickListener(new View.OnClickListener() {
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
        });*/
        if (frm==1)
        {
            if (position == 3) {
                holder.more.setVisibility(View.VISIBLE);
                holder.item.setVisibility(View.GONE);
            }
        }


        }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frame;
        TextView name;
        LinearLayout items_bg,more,item;
        ImageView bg_img;

        public MyViewHolder(View itemView) {
            super(itemView);

            bg_img = (ImageView) itemView.findViewById(R.id.back_img);
            frame = (FrameLayout) itemView.findViewById(R.id.brand);
            name = (TextView) itemView.findViewById(R.id.store_name);
            items_bg = (LinearLayout) itemView.findViewById(R.id.item_bg);
            more = (LinearLayout) itemView.findViewById(R.id.more);
            item = (LinearLayout) itemView.findViewById(R.id.item);
        }
    }
}
