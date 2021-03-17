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

import static com.iamretailer.AppClass.getContext;


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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cat_list2copy, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (Build.VERSION.SDK_INT >= 24) {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name()));
        }



        if (frm == 1)

        {
            if (position == 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.dp85), LinearLayout.LayoutParams.WRAP_CONTENT);
                holder.font_view.setVisibility(View.VISIBLE);
                lp.setMarginEnd( (int) context.getResources().getDimension(R.dimen.dp8));
                holder.items_bg.setLayoutParams(lp);
            } else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.dp85), LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMarginEnd((int) context.getResources().getDimension(R.dimen.dp8));
                holder.items_bg.setLayoutParams(lp);

            }

        } else {
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            float px1 = (float) ((widthPixels - (20 * context.getResources().getDisplayMetrics().density)) / 4);
            holder.items_bg.getLayoutParams().width = (int) px1;
            holder.items_bg.setPadding(0, 0, 0, (int) context.getResources().getDimension(R.dimen.dp5));

        }


        if (frm==1)
        {
            if (position == 3) {
                holder.name.setText(R.string.see_all);
                holder.more.setVisibility(View.VISIBLE);
                holder.cat_img.setVisibility(View.GONE);
            }
        }


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
        ImageView bg_img;
        ImageView more;
        LinearLayout font_view;

        public MyViewHolder(View itemView) {
            super(itemView);

            frame = itemView.findViewById(R.id.brand);
            name = itemView.findViewById(R.id.store_name);
            items_bg = itemView.findViewById(R.id.item_bg);
            cat_img= itemView.findViewById(R.id.cat_img);
            font_view=itemView.findViewById(R.id.font_view);
            more = itemView.findViewById(R.id.more);
        }
    }
}
