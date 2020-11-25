package com.iamretailer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.POJO.FilterPO;
import com.iamretailer.R;

import java.util.ArrayList;

public class FilterMainAdapter extends RecyclerView.Adapter<FilterMainAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<FilterPO> items;
    private final Context context;

    public FilterMainAdapter(Context ctx, ArrayList<FilterPO> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.filter_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.filter_name.setText(getCapsSentences(items.get(position).getFilter_name()));

        if (items.get(position).isSelected()) {
            holder.bg_img.setImageResource(R.mipmap.filterfilled);
            holder.bg_view.setVisibility(View.VISIBLE);
            holder.filter_name.setTextColor(context.getResources().getColor(R.color.colorAccent));

        } else {
            holder.bg_img.setImageResource(R.mipmap.filternormal);
            holder.bg_view.setVisibility(View.GONE);
            holder.filter_name.setTextColor(context.getResources().getColor(R.color.app_text_color));

        }
        int cc = 0;
        for (int h = 0; h < items.get(position).getFilterPOS().size(); h++) {
            if (items.get(position).getFilterPOS().get(h).isSelected())
                cc++;
        }
        if (cc == 0) {
            holder.count.setVisibility(View.GONE);
            holder.count_tex.setText("0");
        } else {
            holder.count.setVisibility(View.VISIBLE);
            String vv = cc + "";
            holder.count_tex.setText(vv);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView bg_img;
        final TextView filter_name;
        final TextView count_tex;
        final LinearLayout bg_view;
        final LinearLayout count;

        MyViewHolder(View itemView) {
            super(itemView);
            bg_img = itemView.findViewById(R.id.bg_image);
            filter_name = itemView.findViewById(R.id.filter_name);
            bg_view = itemView.findViewById(R.id.bg_view);
            count = itemView.findViewById(R.id.count);
            count_tex = itemView.findViewById(R.id.count_tex);

        }
    }

    private String getCapsSentences(String tagName) {
        String[] splits = tagName.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            String eachWord = splits[i];
            if (i > 0 && eachWord.length() > 0) {
                sb.append(" ");
            }
            String cap = eachWord.substring(0, 1).toUpperCase()
                    + eachWord.substring(1);
            sb.append(cap);
        }
        return sb.toString();
    }
}

