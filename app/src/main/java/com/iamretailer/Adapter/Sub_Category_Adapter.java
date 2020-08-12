package com.iamretailer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.Allen;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.BrandsPO;
import com.iamretailer.R;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Sub_Category_Adapter extends RecyclerView.Adapter<Sub_Category_Adapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<BrandsPO> list;
    private final Context context;
    private Sub_Product_Adapter productAdapter;

    public Sub_Category_Adapter(Context ctx, ArrayList<BrandsPO> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.list = imageModelArrayList;
        this.context = ctx;
        DBController dbController = new DBController(context);
        Appconstatants.CUR = dbController.getCurCode();
    }

    @NonNull
    @Override
    public Sub_Category_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sub_cat_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Sub_Category_Adapter.MyViewHolder holder, final int position) {


        if (Build.VERSION.SDK_INT >= 24) {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.name.setText(Html.fromHtml(list.get(position).getStore_name()));
        }


        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.get(position).getArrayList1()!=null && list.get(position).getArrayList1().size()>0) {
                    if(holder.arrow.getVisibility()==View.VISIBLE){
                        holder.arrow.setVisibility(View.GONE);
                        holder.arrow1.setVisibility(View.VISIBLE);
                        holder.sub_product_list.setVisibility(View.VISIBLE);
                    }else{
                        holder.arrow.setVisibility(View.VISIBLE);
                        holder.arrow1.setVisibility(View.GONE);
                        holder.sub_product_list.setVisibility(View.GONE);
                    }
                    productAdapter = new Sub_Product_Adapter(context, list.get(position).getArrayList1());
                    Log.i("tag", "valuessss----- " + list.get(position).getArrayList1());
                    holder.sub_product_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    holder.sub_product_list.setAdapter(productAdapter);
                }else{
                    Intent u = new Intent(context, Allen.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", list.get(position).getS_id());
                    bundle.putString("cat_name", list.get(position).getStore_name());
                    u.putExtras(bundle);
                   context.startActivity(u);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        final RecyclerView sub_product_list;
        final TextView name;
        final ImageView arrow;
        final ImageView arrow1;
        final LinearLayout sub;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            arrow = itemView.findViewById(R.id.arrow);
            arrow1 = itemView.findViewById(R.id.arrow1);
            sub = itemView.findViewById(R.id.sub);
            sub_product_list = itemView.findViewById(R.id.sub_product_list);

        }

    }


}