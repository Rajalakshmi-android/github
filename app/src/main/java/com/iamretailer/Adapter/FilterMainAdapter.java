package com.iamretailer.Adapter;

import android.content.Context;
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

    private LayoutInflater inflater;
    private ArrayList<FilterPO> items;
    Context context;


    public FilterMainAdapter(Context ctx, ArrayList<FilterPO> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.filter_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.filter_name.setText(items.get(position).getFilter_name());

        if (items.get(position).isSelected())
        {
            holder.bg_img.setImageResource(R.mipmap.filterfilled);
            holder.bg_view.setVisibility(View.VISIBLE);
            holder.filter_name.setTextColor(context.getResources().getColor(R.color.colorAccent));

        }
        else
        {
            holder.bg_img.setImageResource(R.mipmap.filternormal);
            holder.bg_view.setVisibility(View.GONE);
            holder.filter_name.setTextColor(context.getResources().getColor(R.color.app_text_color));

        }

            int cc=0;
            for (int h=0;h<items.get(position).getFilterPOS().size();h++)
            {
                if (items.get(position).getFilterPOS().get(h).isSelected())
                cc++;
            }

            if (cc==0)
            {
                holder.count.setVisibility(View.GONE);
                holder.count_tex.setText("0");
            }
            else
            {
                holder.count.setVisibility(View.VISIBLE);
                holder.count_tex.setText(cc+"");
            }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView bg_img;
        TextView filter_name,count_tex;
        LinearLayout bg_view,count;


        public MyViewHolder(View itemView) {
            super(itemView);

            bg_img = (ImageView) itemView.findViewById(R.id.bg_image);
            filter_name = (TextView) itemView.findViewById(R.id.filter_name);
            bg_view=(LinearLayout) itemView.findViewById(R.id.bg_view);
            count=(LinearLayout)itemView.findViewById(R.id.count);
            count_tex=(TextView)itemView.findViewById(R.id.count_tex);

        }
    }
}

