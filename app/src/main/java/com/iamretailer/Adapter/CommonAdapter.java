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

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<ProductsPO> items;
    private final Context context;
    private final DBController dbController;
    private final String cur_left;
    private final String cur_right;
    private final AndroidLogger logger;
    private final int from;
    private final int cart;

    public CommonAdapter(Context ctx, ArrayList<ProductsPO> imageModelArrayList, int from, int cart) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
        this.from = from;
        dbController = new DBController(context);
        cur_left = dbController.get_cur_Left();
        cur_right = dbController.get_cur_Right();
        Appconstatants.CUR = dbController.getCurCode();
        this.cart = cart;
        logger = AndroidLogger.getLogger(context, Appconstatants.LOG_ID, false);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.featured_item2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        if (from == 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.items_bg.getLayoutParams();
            params.width = (int) context.getResources().getDimension(R.dimen.dp150);
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            if (position == 0) {
                params.setMarginStart((int) context.getResources().getDimension(R.dimen.dp10));

            }
            params.setMarginEnd((int) context.getResources().getDimension(R.dimen.dp10));
            holder.items_bg.setLayoutParams(params);


        } else {
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            lp.setMargins((int) context.getResources().getDimension(R.dimen.dp5), (int) context.getResources().getDimension(R.dimen.dp10), 0, 0);
            holder.items_bg.setLayoutParams(lp);
        }
        holder.product_name.setText(items.get(position).getProduct_name());

        if (items.get(position).getProd_offer_rate() > 0) {
            String special = cur_left + String.format(Locale.ENGLISH, "%.2f", items.get(position).getProd_offer_rate()) + cur_right;
            holder.p_price.setText(special);
            String or_price = cur_left + String.format(Locale.ENGLISH, "%.2f", items.get(position).getProd_original_rate()) + cur_right;
            holder.orginal.setText(or_price);
        } else {
            String special = cur_left + String.format(Locale.ENGLISH, "%.2f", items.get(position).getProd_original_rate()) + cur_right;
            holder.p_price.setText(special);
            holder.orginal.setText("");
        }

        if (items.get(position).getProducturl() != null && items.get(position).getProducturl().length() != 0)
            Picasso.with(context).load(items.get(position).getProducturl()).placeholder(R.mipmap.place_holder).into(holder.p_img);
        else
            Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.p_img);

        if (items.get(position).isWish_list()) {
            holder.like.setVisibility(View.VISIBLE);
            holder.un_like.setVisibility(View.GONE);
        } else {
            holder.un_like.setVisibility(View.VISIBLE);
            holder.like.setVisibility(View.GONE);
        }
        holder.items_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductFullView.class);
                Bundle nn = new Bundle();
                nn.putString("productid", items.get(position).getProduct_id());
                intent.putExtras(nn);
                context.startActivity(intent);
            }
        });
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbController.getLoginCount() > 0) {
                    if (holder.un_like.getVisibility() == View.VISIBLE) {
                        items.get(position).setWish_list(true);
                        notifyDataSetChanged();
                        WishlistAddTask task = new WishlistAddTask(position);
                        task.execute(items.get(position).getProduct_id());
                    } else {
                        items.get(position).setWish_list(false);
                        notifyDataSetChanged();
                        DeleteContactTask task = new DeleteContactTask(position);
                        task.execute(items.get(position).getProduct_id());
                    }
                } else {
                    Intent intent = new Intent(context, Login.class);
                    Bundle login = new Bundle();
                    login.putInt("from", 4);
                    intent.putExtras(login);
                    context.startActivity(intent);
                }
            }
        });
        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (items.get(position).getSingleOptionPOS().size() > 0) {
                    Intent intent = new Intent(context, ProductFullView.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("productid", items.get(position).getProduct_id());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {
                    if (items.get(position).getQty() > 0) {
                        CartSaveTask cartSaveTask = new CartSaveTask();
                        cartSaveTask.execute(items.get(position).getProduct_id());
                    } else {
                        Toast.makeText(context, R.string.out_of_stock, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        if (items.get(position).getP_rate() == 0) {
            holder.r1.setImageResource(R.mipmap.un_fill);
            holder.r2.setImageResource(R.mipmap.un_fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 1) {

            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.un_fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 2) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 3) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 4) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.fill);
            holder.r4.setImageResource(R.mipmap.fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 5) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.fill);
            holder.r4.setImageResource(R.mipmap.fill);
            holder.r5.setImageResource(R.mipmap.fill);
        } else {
            holder.r1.setImageResource(R.mipmap.un_fill);
            holder.r2.setImageResource(R.mipmap.un_fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView p_price;
        final TextView orginal;
        final TextView product_name;
        final ImageView like;
        final ImageView un_like;
        final ImageView p_img;
        final LinearLayout items_bg;
        final LinearLayout add_to_cart;
        final LinearLayout fav;
        final ImageView r1;
        final ImageView r2;
        final ImageView r3;
        final ImageView r4;
        final ImageView r5;

        MyViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.prod_name);
            p_img = itemView.findViewById(R.id.prod_img);
            p_price = itemView.findViewById(R.id.prod_price);
            like = itemView.findViewById(R.id.like);
            un_like = itemView.findViewById(R.id.unlike);
            fav = itemView.findViewById(R.id.fav);
            items_bg = itemView.findViewById(R.id.product);
            add_to_cart = itemView.findViewById(R.id.add_to_cart);
            orginal = itemView.findViewById(R.id.orginal);
            r1 = itemView.findViewById(R.id.r1);
            r2 = itemView.findViewById(R.id.r2);
            r3 = itemView.findViewById(R.id.r3);
            r4 = itemView.findViewById(R.id.r4);
            r5 = itemView.findViewById(R.id.r5);
        }
    }

    private class WishlistAddTask extends AsyncTask<String, Void, String> {
        final int pos;

        WishlistAddTask(int position) {
            pos = position;
        }

        @Override
        protected void onPreExecute() {
        }

        protected String doInBackground(String... param) {
            logger.info("Wish list add api :" + Appconstatants.WishList_Add + param[0]);
            String response;
            Connection connection = new Connection();
            try {
                response = connection.sendHttpPostjson(Appconstatants.WishList_Add + param[0], null, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, context);
                logger.info("Wish list add api resp :" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        try {
                            items.get(pos).setWish_list(true);
                            notifyDataSetChanged();
                            Toast.makeText(context, R.string.wish_add, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.d("ee", e.toString());
                        }
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        try {
                            items.get(pos).setWish_list(false);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.d("ee", e.toString());
                        }
                        Toast.makeText(context, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    try {
                        items.get(pos).setWish_list(false);
                        notifyDataSetChanged();
                    } catch (Exception ex) {
                        Log.d("ee", e.toString());
                    }
                }
            } else {
                try {
                    items.get(pos).setWish_list(false);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    Log.d("ee", e.toString());
                }
                Toast.makeText(context, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteContactTask extends AsyncTask<String, Void, String> {
        final int pos;

        DeleteContactTask(int position) {
            pos = position;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... param) {
            logger.info("Delete wish list api" + Appconstatants.WishList_Add + param[0]);
            String response;
            try {
                Connection connection = new Connection();
                response = connection.sendHttpDelete(Appconstatants.WishList_Add + param[0], null, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, context);
                logger.info("Delete wish list api resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String res) {
            Log.i("resd", res + "");
            if (res != null) {
                try {
                    JSONObject json = new JSONObject(res);
                    if (json.getInt("success") == 1) {
                        try {
                            items.get(pos).setWish_list(false);
                            notifyDataSetChanged();
                            Toast.makeText(context, R.string.dele_wish, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.d("ee", e.toString());
                        }
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        try {
                            items.get(pos).setWish_list(true);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.d("ee", e.toString());

                        }
                        Toast.makeText(context, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        items.get(pos).setWish_list(true);
                        notifyDataSetChanged();
                    } catch (Exception ex) {
                        Log.d("ee", ex.toString());

                    }
                    Toast.makeText(context, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                try {
                    items.get(pos).setWish_list(true);
                    notifyDataSetChanged();
                } catch (Exception ex) {
                    Log.d("ee", ex.toString());
                }
                Toast.makeText(context, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CartSaveTask extends AsyncTask<Object, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(context.getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Object... param) {
            logger.info("Cart Save api" + Appconstatants.cart_api);
            String response;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("product_id", param[0]);
                json.put("quantity", "1");
                JSONObject object = new JSONObject();
                json.put("option", object);
                Log.d("Cart_input", json.toString());
                Log.d("Cart_url_insert", Appconstatants.sessiondata + "");
                logger.info("Cart Save req" + json);
                response = connection.sendHttpPostjson(Appconstatants.cart_api, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, context);
                logger.info("Cart Save resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Cart", "Cart_resp  " + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Toast.makeText(context, context.getResources().getString(R.string.cart_Add_text), Toast.LENGTH_SHORT).show();
                        switch (cart) {
                            case 1:
                                ((MainActivity) context).cart_inc();
                                break;
                            case 2:
                                ((Product_list) context).cart_inc();
                                break;
                            case 3:
                                ((SearchActivity) context).cart_inc();
                                break;
                            case 4:
                                ((Allen) context).cart_inc();
                                break;
                            case 5:
                                ((Deal_list) context).cart_inc();
                                break;
                            case 6:
                                ((ProductFullView) context).cart_inc();
                                break;
                        }


                    } else {
                        JSONArray error = json.getJSONArray("error");
                        String error_msg = error.getString(0);
                        Toast.makeText(context, error_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }
}


