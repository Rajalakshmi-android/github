package com.iamretailer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.iamretailer.Adapter.Wish_list_adapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.POJO.ProductsPO;

import stutzen.co.network.Connection;

public class WishList extends Language {

    private TextView cart_count;
    private ListView wish_list;
    private ArrayList<ProductsPO> feat_list;
    private FrameLayout error_network;
    private FrameLayout loading;
    private FrameLayout fullayout;
    private FrameLayout success;
    private TextView errortxt1;
    private TextView errortxt2;
    private FrameLayout no_items;
    private AndroidLogger logger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        LinearLayout back = findViewById(R.id.menu);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_count = findViewById(R.id.cart_count);
        no_items = findViewById(R.id.no_items);
        wish_list = findViewById(R.id.wish_list);
        error_network = findViewById(R.id.error_network);
        success = findViewById(R.id.success);
        loading = findViewById(R.id.loading);
        fullayout = findViewById(R.id.fullayout);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        TextView header = findViewById(R.id.header);
        header.setText(R.string.wish_list);
        DBController dbController = new DBController(WishList.this);
        Appconstatants.Lang = dbController.get_lang_code();
        Appconstatants.CUR = dbController.getCurCode();
        LinearLayout retry = findViewById(R.id.retry);
        Button shop = findViewById(R.id.shop);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WishList.this, MainActivity.class));
            }
        });

        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WishList.this, MyCart.class));
            }
        });


        wish_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(WishList.this, ProductFullView.class);
                Bundle mm = new Bundle();
                mm.putString("productid", feat_list.get(i).getProduct_id());
                intent.putExtras(mm);
                startActivity(intent);

            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network.setVisibility(View.GONE);
                success.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                CartTask cartTask = new CartTask();
                cartTask.execute(Appconstatants.cart_api);
                WISH_LIST wish_list = new WISH_LIST();
                wish_list.execute(Appconstatants.Wishlist_Get);

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        WISH_LIST wish_list = new WISH_LIST();
        wish_list.execute(Appconstatants.Wishlist_Get);
    }

    private class WISH_LIST extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("WIsh list api" + param[0]);


            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, WishList.this);
                logger.info("WIsh list api resp" + response);
                Log.d("wish_api", param[0]);
                Log.d("wish_res", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            if (resp != null) {
                try {
                    feat_list = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONArray array = json.getJSONArray("data");

                        if (array.length() > 0) {
                            for (int h = 0; h < array.length(); h++) {
                                JSONObject obj = array.getJSONObject(h);
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                                bo.setPrice(obj.isNull("price") ? 0 : obj.getDouble("price"));
                                bo.setOff_price(obj.isNull("special") ? "" : obj.getString("special"));
                                bo.setProducturl(obj.isNull("thumb") ? "" : obj.getString("thumb"));
                                feat_list.add(bo);
                            }
                            if (feat_list.size() != 0) {

                                Wish_list_adapter wishListAdapter = new Wish_list_adapter(WishList.this, R.layout.wish_list_item, feat_list);
                                wish_list.setAdapter(wishListAdapter);

                            }
                            no_items.setVisibility(View.GONE);
                            wish_list.setVisibility(View.VISIBLE);


                        } else {
                            no_items.setVisibility(View.VISIBLE);
                            wish_list.setVisibility(View.GONE);

                        }

                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);
                        success.setVisibility(View.VISIBLE);

                    } else {
                        loading.setVisibility(View.GONE);
                        success.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error = array.getString(0) + "";
                        errortxt1.setText(error);
                        Toast.makeText(WishList.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    success.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    WISH_LIST wish_list = new WISH_LIST();
                                    wish_list.execute(Appconstatants.Wishlist_Get);

                                }
                            })
                            .show();
                }
            } else {
                loading.setVisibility(View.GONE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                success.setVisibility(View.GONE);

            }

        }
    }


    class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        protected String doInBackground(String... param) {
            logger.info("Cart api:" + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();

                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, WishList.this);
                logger.info("Cart resp" + response);
                Log.d("Cart_list_resp", response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Cart_list_resp", "CartResp--->  " + resp);

            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONArray) {
                            cart_count.setText(String.valueOf(0));

                        } else if (dd instanceof JSONObject) {

                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));
                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_count.setText(String.valueOf(qty));
                        }
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(WishList.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }

    public void wish_list() {
        WISH_LIST wish_list = new WISH_LIST();
        wish_list.execute(Appconstatants.Wishlist_Get);
    }

}
