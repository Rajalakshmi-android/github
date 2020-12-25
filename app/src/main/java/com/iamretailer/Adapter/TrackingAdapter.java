package com.iamretailer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.POJO.PlacePO;
import com.iamretailer.R;
import com.logentries.android.AndroidLogger;

import java.util.ArrayList;

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

        if (items.get(position).getCommand() != null & items.get(position).getCommand().length() > 0) {
            holder.command.setVisibility(View.VISIBLE);
        } else {
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


