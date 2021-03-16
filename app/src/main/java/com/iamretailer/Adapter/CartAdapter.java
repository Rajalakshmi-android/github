package com.iamretailer.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.MyCart;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.R;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import stutzen.co.network.Connection;

public class CartAdapter extends ArrayAdapter<ProductsPO> {

    private final Context context;
    private final ArrayList<ProductsPO> items;
    private final int res;
    private final LayoutInflater mInflater;
    private final String cur_left;
    private final String cur_right;
    private final AndroidLogger logger;
    private AlertDialog alertReviewDialog;
    private final LayoutInflater inflater;

    public CartAdapter(Activity context, int resource, ArrayList<ProductsPO> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.res = resource;
        this.context = context;
        this.items = items;
        DBController db = new DBController(getContext());
        cur_left = db.get_cur_Left();
        cur_right = db.get_cur_Right();
        Appconstatants.CUR = db.getCurCode();
        logger = AndroidLogger.getLogger(context, Appconstatants.LOG_ID, false);
        inflater = context.getLayoutInflater();

    }

    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        LinearLayout alertView;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(res, null, true);
            convertView.setTag(holder);
            alertView = (LinearLayout) convertView;
        } else {
            alertView = (LinearLayout) convertView;
        }
        holder.cart_img = convertView.findViewById(R.id.cart_img);
        holder.cart_prod_name = convertView.findViewById(R.id.cart_prod_name);
        holder.cart_prod_or_rate = convertView.findViewById(R.id.tot);
        holder.cart_ins = convertView.findViewById(R.id.add);
        holder.cart_dec = convertView.findViewById(R.id.subtract);
        holder.cart_value = convertView.findViewById(R.id.cart_value);
        holder.remove = convertView.findViewById(R.id.remove);
        holder.option_list = convertView.findViewById(R.id.option_list);
        holder.out_of_stock = convertView.findViewById(R.id.out_of_stock);
        holder.qty = convertView.findViewById(R.id.qty);
        holder.option = convertView.findViewById(R.id.option);
        holder.rupee_front = convertView.findViewById(R.id.rupee_front);
        holder.rupee_back = convertView.findViewById(R.id.rupee_back);


        if (items.get(position).getProducturl().isEmpty())
            Picasso.with(getContext()).load(R.mipmap.place_holder).into(holder.cart_img);
        else
            Picasso.with(getContext()).load(items.get(position).getProducturl()).into(holder.cart_img);

        if (items.get(position).isOut_of_stock()) {
            holder.out_of_stock.setVisibility(View.GONE);

        } else {
            holder.out_of_stock.setVisibility(View.VISIBLE);

        }

        if (Build.VERSION.SDK_INT >= 24) {
            holder.cart_prod_name.setText(Html.fromHtml(items.get(position).getProduct_name(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.cart_prod_name.setText(Html.fromHtml(items.get(position).getProduct_name()));
        }
        holder.rupee_back.setText(cur_right);
        holder.rupee_front.setText(cur_left);
        String val = String.format(Locale.ENGLISH, "%.2f", items.get(position).getTotal());
        holder.cart_prod_or_rate.setText(val);
        StringBuilder op = new StringBuilder();
        for (int j = 0; j < items.get(position).getOptionlist().size(); j++) {
            if (items.get(position).getOptionlist().size() - 1 == j)
                op.append(items.get(position).getOptionlist().get(j).getName()).append(": ").append(items.get(position).getOptionlist().get(j).getValue());
            else {
                op.append(items.get(position).getOptionlist().get(j).getName()).append(": ").append(items.get(position).getOptionlist().get(j).getValue()).append(", ");
            }

        }

        StringBuilder sb2 = new StringBuilder();

        for (int h = 0; h < items.get(position).getOptionlist().size(); h++) {
            if(items.get(position).getOptionlist().get(h).getName().contains(context.getResources().getString(R.string.date))||items.get(position).getOptionlist().get(h).getName().toLowerCase().contains(context.getResources().getString(R.string.time))){
                sb2.append("<br/>"+items.get(position).getOptionlist().get(h).getName() +" : "+items.get(position).getOptionlist().get(h).getValue());
            }else{
                sb2.append(items.get(position).getOptionlist().get(h).getValue());
            }
            if (h != items.get(position).getOptionlist().size() - 1)
                sb2.append(",");
        }

        if (sb2.length() > 0) {
            holder.option.setVisibility(View.VISIBLE);
        } else {
            holder.option.setVisibility(View.GONE);
        }
       // holder.option_list.setText(String.valueOf(sb2));
        if(String.valueOf(sb2)!=null && String.valueOf(sb2).length()!=0) {
            if (Build.VERSION.SDK_INT >= 24) {
                holder.option_list.setText(Html.fromHtml(String.valueOf(sb2), Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.option_list.setText(Html.fromHtml(String.valueOf(sb2)));
            }
        }
        holder.cart_value.setText(String.valueOf(items.get(position).getCartvalue()));
        holder.cart_ins.setTag(position);


        holder.cart_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartUpdate cartUpdate = new CartUpdate(position);
                cartUpdate.execute(items.get(position).getKey(), String.valueOf(items.get(position).getCartvalue() + 1));


            }
        });
        holder.cart_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CartUpdate cartUpdate = new CartUpdate(position);
                cartUpdate.execute(items.get(position).getKey(), String.valueOf(items.get(position).getCartvalue() - 1));

            }
        });


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cartdel cartdel = new Cartdel(position);
                cartdel.execute(items.get(position).getKey(), 0 + "");
            }
        });


        holder.cart_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_change(position, items.get(position).getCartvalue() + "", items.get(position).getProduct_name());

            }
        });


        return alertView;
    }

    private static class ViewHolder {
        ImageView cart_img;
        ImageView cart_ins;
        ImageView cart_dec;
        TextView cart_prod_name;
        TextView cart_prod_or_rate;
        TextView cart_value;
        TextView option_list;
        ImageView remove;
        TextView out_of_stock, rupee_front, rupee_back;
        LinearLayout qty, option;

    }

    private void quantity_change(final int pos, String qtys, String name) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        View dialogView = inflater.inflate(R.layout.show_qty, null, false);
        dialogBuilder.setView(dialogView);
        dialogBuilder.create();
        alertReviewDialog = dialogBuilder.create();
        alertReviewDialog.setCancelable(false);
        LinearLayout cancel = dialogView.findViewById(R.id.cancel);
        final EditText qty = dialogView.findViewById(R.id.qty);
        LinearLayout apply = dialogView.findViewById(R.id.apply);
        TextView product_names = dialogView.findViewById(R.id.product_name);
        qty.setText(qtys);
        product_names.setText(name);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertReviewDialog.dismiss();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty.getText().length() > 0 && Integer.parseInt(qty.getText().toString()) > 0) {
                    alertReviewDialog.dismiss();
                    CartUpdate cartUpdate = new CartUpdate(pos);
                    cartUpdate.execute(items.get(pos).getKey(), qty.getText().toString());
                } else {
                    qty.setError(context.getResources().getString(R.string.qty_e));
                }
            }
        });
        alertReviewDialog.show();


    }


    private class CartUpdate extends AsyncTask<String, Void, String> {
        private final int pos;
        private ProgressDialog pDialog;
        private String qty;


        CartUpdate(int position) {
            pos = position;
        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getContext().getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {
            logger.info("Update cart api" + Appconstatants.cart_update_api);
            String response;
            qty = param[1];
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("key", param[0]);
                json.put("quantity", param[1]);


                Log.d("cart", json.toString());
                Log.d("session", Appconstatants.sessiondata);
                logger.info("Update cart api req" + json);
                response = connection.sendHttpPutjson1(Appconstatants.cart_update_api, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, getContext());
                logger.info("Update cart api res" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("cart", "Cart-->" + resp);
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        items.get(pos).setCartvalue(Integer.parseInt(qty));
                        notifyDataSetChanged();
                        ((MyCart) getContext()).CartCall();
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.items_update), Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(getContext(), array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Cartdel extends AsyncTask<String, Void, String> {

        private final int pos1;
        private ProgressDialog pDialog;


        Cartdel(int position) {
            pos1 = position;
        }


        @Override
        protected void onPreExecute() {


            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getContext().getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {

            logger.info("Delete cart api" + Appconstatants.cart_update_api);

            String response;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("key", param[0]);
                json.put("key", param[0]);
                json.put("quantity", param[1]);


                Log.d("del_", json.toString());
                Log.d("del_session", Appconstatants.sessiondata);
                logger.info("Delete cart api" + json);
                response = connection.sendHttpPutjson1(Appconstatants.cart_update_api, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, getContext());
                logger.info("Delete cart api" + response);
                Log.d("del_s", Appconstatants.cart_update_api);
                Log.d("del_r", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("cart", "Cart-->" + resp);
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        items.remove(pos1);
                        notifyDataSetChanged();
                        ((MyCart) getContext()).CartCall();
                        Toast.makeText(getContext(), R.string.remove, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(getContext(), array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                }

            } else {

                Toast.makeText(getContext(), R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
