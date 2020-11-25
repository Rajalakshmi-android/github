package com.iamretailer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.Allen;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.BrandsPO;
import com.iamretailer.R;

import java.util.ArrayList;


public class Sub_Product_Adapter extends RecyclerView.Adapter<Sub_Product_Adapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<BrandsPO> list;
    private final Context context;

    public Sub_Product_Adapter(Context ctx, ArrayList<BrandsPO> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.list = imageModelArrayList;
        this.context = ctx;
        DBController dbController = new DBController(context);
        Appconstatants.CUR = dbController.getCurCode();
    }

    @NonNull
    @Override
    public Sub_Product_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sub_product_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Sub_Product_Adapter.MyViewHolder holder, final int position) {

        if (Build.VERSION.SDK_INT >= 24) {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name()));
        }

        int val = list.size() - 1;

        if (val == position) {
            holder.view_line.setVisibility(View.GONE);
        } else {
            holder.view_line.setVisibility(View.VISIBLE);
        }

        holder.sub_product_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(context, Allen.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", list.get(position).getS_id());
                bundle.putString("cat_name", list.get(position).getStore_name());
                u.putExtras(bundle);
                context.startActivity(u);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        final View view_line;
        final LinearLayout sub_product_list;
        final TextView name;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            sub_product_list = itemView.findViewById(R.id.sub_product_list);
            view_line = itemView.findViewById(R.id.view_line);

        }

    }


}