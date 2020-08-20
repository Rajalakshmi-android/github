package com.iamretailer.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Allen;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.Deal_list;
import com.iamretailer.Login;
import com.iamretailer.MainActivity;
import com.iamretailer.POJO.PlacePO;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.ProductFullView;
import com.iamretailer.Product_list;
import com.iamretailer.R;
import com.iamretailer.SearchActivity;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import stutzen.co.network.Connection;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<PlacePO> items;
    private final Context context;
    private final AndroidLogger logger;


    public TrackingAdapter(Context ctx, ArrayList<PlacePO> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
        logger = AndroidLogger.getLogger(context, Appconstatants.LOG_ID, false);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.track_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            //lp.setMargins((int) context.getResources().getDimension(R.dimen.dp5), (int) context.getResources().getDimension(R.dimen.dp10), 0, 0);
            //holder.items_bg.setLayoutParams(lp);
        if(items.get(position).getCommand()!=null & !items.get(position).getCommand().equalsIgnoreCase("")){
            holder.command.setVisibility(View.VISIBLE);
        }else{
            holder.command.setVisibility(View.GONE);
        }
        holder.status.setText(items.get(position).getStatus());
        holder.command.setText(items.get(position).getCommand());
        holder.date.setText(items.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        final TextView date;
        final TextView status;
        final TextView command;

        MyViewHolder(View itemView) {
            super(itemView);

             date = itemView.findViewById(R.id.date);
             status = itemView.findViewById(R.id.status);
             command = itemView.findViewById(R.id.command);


        }
    }


}


