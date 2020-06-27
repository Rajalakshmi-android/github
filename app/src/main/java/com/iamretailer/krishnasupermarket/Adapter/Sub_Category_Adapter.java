package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.iamretailer.krishnasupermarket.Allen;
import com.iamretailer.krishnasupermarket.Category_level;
import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.POJO.BrandsPO;
import com.iamretailer.krishnasupermarket.R;
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
        holder.name.setText(list.get(position).getStore_name());


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