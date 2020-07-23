package com.iamretailer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.iamretailer.Allen;
import com.iamretailer.Category_level;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.BrandsPO;
import com.iamretailer.R;
import com.logentries.android.AndroidLogger;


public class Sub_Category_Adapter extends RecyclerView.Adapter<Sub_Category_Adapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BrandsPO> list;
    Context context;
    DBController dbController;
    AndroidLogger logger;
    public Sub_Category_Adapter(Context ctx, ArrayList<BrandsPO> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.list = imageModelArrayList;
        this.context = ctx;
        dbController = new DBController(context);
        Appconstatants.CUR=dbController.getCurCode();
        logger = AndroidLogger.getLogger(context, Appconstatants.LOG_ID, false);
    }

    @Override
    public Sub_Category_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sub_cat_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(final Sub_Category_Adapter.MyViewHolder holder, final int position) {


        if (Build.VERSION.SDK_INT >= 24)
        {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name() , Html.FROM_HTML_MODE_LEGACY));
        }
        else
        {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name()));
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);

        }

    }


}