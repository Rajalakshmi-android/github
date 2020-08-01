package com.iamretailer.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CircleTransforms;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.BrandsPO;
import com.iamretailer.R;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class BrandzAdapter extends RecyclerView.Adapter<BrandzAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BrandsPO> items;
    Context context;
    DBController dbController;
    String cur_left = "";
    String cur_right = "";
    AndroidLogger logger;
    int from;
    int type;


    public BrandzAdapter(Context ctx, ArrayList<BrandsPO> imageModelArrayList, int from,int width, int height) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
        this.from = from;
        dbController = new DBController(context);
        cur_left = dbController.get_cur_Left();
        cur_right = dbController.get_cur_Right();
        Appconstatants.CUR = dbController.getCurCode();
        this.type = type;
        logger = AndroidLogger.getLogger(context, Appconstatants.LOG_ID, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.cat_list2copy, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (Build.VERSION.SDK_INT >= 24)
        {
            holder.name.setText(Html.fromHtml(items.get(position).getStore_name() , Html.FROM_HTML_MODE_LEGACY));
        }
        else
        {
            holder.name.setText(Html.fromHtml(items.get(position).getStore_name()));
        }

        if (from == 1) {
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            float px1 = (widthPixels - (25 * context.getResources().getDisplayMetrics().density)) / 4;
            holder.frame.getLayoutParams().height = (int) px1;
        } else if(from==3)
        {
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            float px1 = (widthPixels - (25 * context.getResources().getDisplayMetrics().density)) / 3;
            holder.frame.getLayoutParams().height = (int) px1;
        }
        else {

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.frame.getLayoutParams();
            params.width=(int)context.getResources().getDimension(R.dimen.dp80);
            params.height=(int)context.getResources().getDimension(R.dimen.dp80);
            params.setMargins(0, (int) context.getResources().getDimension(R.dimen.dp10),   0,0);
            holder.frame.setLayoutParams(params);

        }
        if (items.get(position).getBg_img_url()!=null) {
            if (items.get(position).getBg_img_url().isEmpty()) {
                Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.bg_img);
            } else {

                float density = context.getResources().getDisplayMetrics().density;
                int px = 10 * (int) Math.round(density);
                float dp = 50 / density;
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                };
                Picasso.with(context).load(items.get(position).getBg_img_url()).transform(new CircleTransforms(px, 0)).fit().placeholder(R.mipmap.place_holder).into(holder.bg_img);
                Log.i("tag", "image_url--- " + items.get(position).getBg_img_url());
            }
        }
        else
        {
            Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.bg_img);
        }
        if (from==1)
        {
            if (position == 3) {
            holder.name.setText(R.string.see_all);
            holder.more.setVisibility(View.VISIBLE);
            holder.bg_img.setVisibility(View.GONE);
         }
        }

        if (items.get(position).isSelect())
        {
            holder.frame.setBackground(context.getResources().getDrawable(R.drawable.category_bg));
            holder.name.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        else
        {
            holder.frame.setBackground(context.getResources().getDrawable(R.drawable.category_normal));
            holder.name.setTextColor(context.getResources().getColor(R.color.app_text_color));
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frame;
        ImageView bg_img;
        TextView name,sub_name;
        ImageView more;
        LinearLayout item_bg;


        public MyViewHolder(View itemView) {
            super(itemView);

            bg_img = (ImageView) itemView.findViewById(R.id.back_img);
            frame = (FrameLayout) itemView.findViewById(R.id.brand);
            name = (TextView) itemView.findViewById(R.id.store_name);
            more = (ImageView) itemView.findViewById(R.id.more);
            item_bg=(LinearLayout) itemView.findViewById(R.id.items_bg);

        }
    }
}
