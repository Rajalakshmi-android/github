package com.iamretailer.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.R;
import com.iamretailer.WishList;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import stutzen.co.network.Connection;


public class Wish_list_adapter extends ArrayAdapter<ProductsPO> {
    private final int resource;
    private final Context context;
    private final ArrayList<ProductsPO> items;
    private final LayoutInflater mInflater;
    private final String cur_left;
    private final String cur_right;

    public Wish_list_adapter(Context context, int resource, ArrayList<ProductsPO> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.context = context;
        this.items = items;
        DBController dbController = new DBController(context);
        Appconstatants.CUR = dbController.getCurCode();
        cur_left = dbController.get_cur_Left();
        cur_right = dbController.get_cur_Right();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        LinearLayout alertView;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(resource, null, true);
            convertView.setTag(holder);
            alertView = (LinearLayout) convertView;
        } else {
            alertView = (LinearLayout) convertView;
        }

        holder.product_name = convertView.findViewById(R.id.prod_name);
        holder.p_img = convertView.findViewById(R.id.prod_img);
        holder.p_price = convertView.findViewById(R.id.prod_price);
        holder.remove = convertView.findViewById(R.id.remove);
        holder.cur_front = convertView.findViewById(R.id.cur_front);
        holder.cur_back = convertView.findViewById(R.id.cur_back);
        holder.line = convertView.findViewById(R.id.line);
        holder.product_name.setText(items.get(position).getProduct_name());
        holder.p_price.setText(items.get(position).getOff_price());

        if (items.get(position).getProducturl() != null && items.get(position).getProducturl().length() != 0)
            Picasso.with(context).load(items.get(position).getProducturl()).placeholder(R.mipmap.place_holder).into(holder.p_img);
        else
            Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.p_img);


        if (items.get(position).getOff_price().equalsIgnoreCase("0")) {
            String value = String.format(Locale.ENGLISH, "%.2f", items.get(position).getPrice());
            holder.p_price.setText(value);
            holder.cur_back.setText(cur_right);
            holder.cur_front.setText(cur_left);
        } else {
            holder.cur_back.setText("");
            holder.cur_front.setText("");
            holder.p_price.setText(items.get(position).getOff_price());
        }


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTask task = new DeleteTask(position);
                task.execute(items.get(position).getProduct_id());
            }
        });

        if (position == getCount() - 1)
            holder.line.setVisibility(View.GONE);


        return alertView;
    }

    private static class ViewHolder {
        TextView product_name;
        ImageView p_img;
        ImageView remove;
        TextView p_price;
        TextView cur_front;
        TextView cur_back;
        View line;
    }


    private class DeleteTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        final int pos;

        DeleteTask(int position) {
            pos = position;
        }

        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getContext().getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... param) {

            String response;
            try {

                Connection connection = new Connection();
                response = connection.sendHttpDelete(Appconstatants.WishList_Add + param[0], null, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.APP_DOMAIN_KEY, Appconstatants.Lang, Appconstatants.CUR, context);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;

        }

        protected void onPostExecute(String res) {
            Log.i("resd", res + "");
            pDialog.dismiss();
            if (res != null) {
                try {
                    JSONObject json = new JSONObject(res);
                    if (json.getInt("success") == 1) {
                        items.remove(pos);
                        notifyDataSetChanged();
                        ((WishList) getContext()).wish_list();
                        Toast.makeText(context, R.string.dele_wish, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, json.getString("message") + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }

            } else {

                Toast.makeText(context, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
