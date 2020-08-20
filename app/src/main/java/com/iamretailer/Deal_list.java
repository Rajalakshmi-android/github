package com.iamretailer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.CommonAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.POJO.SingleOptionPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;


public class Deal_list extends Language {

    private FrameLayout loading;
    private FrameLayout error_network;
    private FrameLayout fullayout;
    private ArrayList<ProductsPO> list;
    private CommonAdapter bestAdapter;
    private RecyclerView product_list;
    private TextView cart_counts;
    private int start = 1, limit = 10;
    private LinearLayout load_more;
    private int val = 0;
    private TextView errortxt1;
    private TextView errortxt2;
    private LinearLayout loading_bar;
    private AndroidLogger logger;
    private boolean loadin = false;
    private int firstVisibleItem;
    private int visibleItemCount;
    private GridLayoutManager mLayoutManager;
    private TextView no_proditems;
    private DBController db;
    private ArrayList<ProductsPO> fav_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        db = new DBController(Deal_list.this);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        loading = findViewById(R.id.loading);
        error_network = findViewById(R.id.error_network);
        fullayout = findViewById(R.id.fullayout);
        product_list = findViewById(R.id.product_list);
        LinearLayout back = findViewById(R.id.menu);
        TextView header = findViewById(R.id.header);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_counts = findViewById(R.id.cart_count);
        LinearLayout retry = findViewById(R.id.retry);
        load_more = findViewById(R.id.load_more);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        loading_bar = findViewById(R.id.loading_bar);
        no_proditems=findViewById(R.id.no_proditems);
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang = db.get_lang_code();
        Appconstatants.CUR = db.getCurCode();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Deal_list.this, MyCart.class));
            }
        });
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);

        DEAL_LIST deal = new DEAL_LIST();
        deal.execute(Appconstatants.Deal_List + "&page=" + start + "&limit=" + limit);
        header.setText(R.string.deal);


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                start = 1;
                limit = 10;
                loadin = false;
                no_proditems.setVisibility(View.GONE);
                product_list.setVisibility(View.GONE);
                CartTask cartTask = new CartTask();
                cartTask.execute(Appconstatants.cart_api);
                DEAL_LIST deal = new DEAL_LIST();
                deal.execute(Appconstatants.Deal_List + "&page=" + start + "&limit=" + limit);

            }
        });
        product_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = product_list.getChildCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loadin) {
                        if ((visibleItemCount + firstVisibleItem) >= (start - 1) * limit) {
                            loadin = true;
                            val = 1;
                            load_more.setVisibility(View.VISIBLE);
                            DEAL_LIST deal = new DEAL_LIST();
                            deal.execute(Appconstatants.Deal_List + "&page=" + start + "&limit=" + limit);
                        }
                    }
                }
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        if (db.getLoginCount() > 0) {
            WISH_LIST wish_list = new WISH_LIST();
            wish_list.execute(Appconstatants.Wishlist_Get);
        }
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
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, getApplicationContext());
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
                    fav_item = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONArray array = json.getJSONArray("data");

                        if (array.length() > 0) {
                            for (int h = 0; h < array.length(); h++) {
                                JSONObject obj = array.getJSONObject(h);
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                fav_item.add(bo);
                            }

                            if (list!=null&&list.size() > 0) {
                                for (int u = 0; u < list.size(); u++) {
                                    for (int h = 0; h < fav_item.size(); h++) {
                                        if (Integer.parseInt(list.get(u).getProduct_id()) == Integer.parseInt(fav_item.get(h).getProduct_id())) {
                                            list.get(u).setWish_list(true);
                                            break;
                                        } else {
                                            list.get(u).setWish_list(false);
                                        }
                                    }
                                }
                                bestAdapter.notifyDataSetChanged();
                            }


                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }
    private class DEAL_LIST extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }


        protected String doInBackground(String... param) {
            logger.info("Best sellin api" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Deal_list.this);
                logger.info("Best sellin api" + response);
                Log.d("prducts_api", param[0]);
                Log.d("prducts_", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            load_more.setVisibility(View.GONE);
            if (resp != null) {
                try {
                    if (val == 0) {
                        list = new ArrayList<>();
                    }
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {
                        JSONArray arr = new JSONArray(json.getString("data"));
                        for (int h = 0; h < arr.length(); h++) {
                            JSONObject obj = arr.getJSONObject(h);
                            ProductsPO bo = new ProductsPO();
                            bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                            bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                            bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "" : obj.getString("price")));
                            bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "" : obj.getString("special")));
                            bo.setProducturl(obj.isNull("thumb") ? "" : obj.getString("thumb"));
                            bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                            bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                            bo.setWish_list(!obj.isNull("wish_list") && obj.getBoolean("wish_list"));
                            bo.setWeight(obj.isNull("weight") ? "" : obj.getString("weight"));
                            bo.setManufact(obj.isNull("manufacturer") ? "" : obj.getString("manufacturer"));
                            Object dd = obj.get("option");
                            ArrayList<SingleOptionPO> optionPOS = new ArrayList<>();
                            if (dd instanceof JSONObject) {
                                JSONObject jsonObject = obj.getJSONObject("option");
                                JSONArray option = jsonObject.getJSONArray("product_option_value");
                                if (option.length() > 0) {


                                    for (int k = 0; k < option.length(); k++) {
                                        JSONObject object1 = option.getJSONObject(k);
                                        SingleOptionPO po = new SingleOptionPO();
                                        po.setProduct_option_value_id(object1.isNull("product_option_value_id") ? 0 : object1.getInt("product_option_value_id"));
                                        optionPOS.add(po);

                                    }
                                    bo.setSingleOptionPOS(optionPOS);
                                }
                            } else {
                                bo.setSingleOptionPOS(optionPOS);
                            }

                            list.add(bo);
                        }
                        if (list!=null&&list.size() != 0) {
                            if (val == 0) {
                                bestAdapter = new CommonAdapter(Deal_list.this, list, 0, 5);
                                mLayoutManager = new GridLayoutManager(Deal_list.this, 2);
                                product_list.setLayoutManager(mLayoutManager);
                                product_list.setAdapter(bestAdapter);
                            } else {
                                bestAdapter.notifyDataSetChanged();
                            }
                            no_proditems.setVisibility(View.GONE);
                            product_list.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            no_proditems.setVisibility(View.VISIBLE);
                            product_list.setVisibility(View.GONE);
                        }
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);
                        load_more.setVisibility(View.GONE);

                        start = start + 1;

                    } else {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        no_proditems.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error_msg=array.getString(0) + "";
                        errortxt2.setText(error_msg);
                        Toast.makeText(Deal_list.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading_bar.setVisibility(View.GONE);
                    no_proditems.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    DEAL_LIST deal = new DEAL_LIST();
                                    deal.execute(Appconstatants.Deal_List + "&page=" + start + "&limit=" + limit);
                                }
                            })
                            .show();

                }
            } else {
                loading.setVisibility(View.GONE);
                no_proditems.setVisibility(View.GONE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
            }
        }
    }


    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Cart api:" + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Deal_list.this);
                logger.info("Cart resp" + response);
                Log.d("Cart_list_resp", response + "");

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
                            cart_counts.setText(String.valueOf(0));

                        } else if (dd instanceof JSONObject) {


                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));

                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_counts.setText(String.valueOf(qty));

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }

    public void cart_inc() {
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }

}
