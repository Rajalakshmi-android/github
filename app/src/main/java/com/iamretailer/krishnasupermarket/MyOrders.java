package com.iamretailer.krishnasupermarket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.krishnasupermarket.Common.CommonFunctions;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.iamretailer.krishnasupermarket.Adapter.OrderAdapter;
import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.POJO.OrdersPO;

import stutzen.co.network.Connection;


public class MyOrders extends Language {

    ListView order;
    OrderAdapter adapter;
    LinearLayout back;
    ArrayList<OrdersPO> list;
    FrameLayout error_network;
    LinearLayout retry;
    DBController db;
    LinearLayout empty;
    TextView cart_count;
    LinearLayout shopnow;
    FrameLayout loading;
    FrameLayout fullayout;
    Bundle bundle;
    int from=0;
    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    AndroidLogger logger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);


        bundle=new Bundle();
        bundle=getIntent().getExtras();

        back = (LinearLayout) findViewById(R.id.menu);
        error_network = (FrameLayout) findViewById(R.id.error_network);
        db = new DBController(MyOrders.this);
        retry = (LinearLayout) findViewById(R.id.retry);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cart_count = (TextView) findViewById(R.id.cart_count);
        TextView header = (TextView) findViewById(R.id.header);
        header.setText(R.string.myorder);
        order = (ListView) findViewById(R.id.order_list);
        loading=(FrameLayout)findViewById(R.id.loading);
        empty = (LinearLayout) findViewById(R.id.empty);
        shopnow = (LinearLayout) findViewById(R.id.shopnow);
        fullayout=(FrameLayout)findViewById(R.id.fullayout);
        empty.setVisibility(View.GONE);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        Appconstatants.Lang=db.get_lang_code();
        if (Appconstatants.sessiondata == null)
            Appconstatants.sessiondata = db.getSession();
        Appconstatants.CUR=db.getCurCode();

            OrderTask orderTask = new OrderTask();
            orderTask.execute(Appconstatants.myorder_api, Appconstatants.sessiondata);
        shopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrders.this, MainActivity.class));
            }
        });


        order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyOrders.this, ViewDetails.class);

                Bundle bundle = new Bundle();
                bundle.putString("id", list.get(i).getOrder_id());
                bundle.putString("status", list.get(i).getOrder_status());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                error_network.setVisibility(View.GONE);
                CartTask cartTask = new CartTask();
                cartTask.execute(Appconstatants.cart_api);
                OrderTask orderTask = new OrderTask();
                orderTask.execute(Appconstatants.myorder_api, Appconstatants.sessiondata);
            }
        });
        LinearLayout cart_items = (LinearLayout) findViewById(R.id.cart_items);
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyOrders.this, MyCart.class));
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && data != null) {

            loading.setVisibility(View.VISIBLE);
            error_network.setVisibility(View.GONE);
            CartTask cartTask = new CartTask();
            cartTask.execute(Appconstatants.cart_api);
            OrderTask orderTask = new OrderTask();
            orderTask.execute(Appconstatants.myorder_api, Appconstatants.sessiondata);
        }
    }
    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Cart api:"+param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyOrders.this);
                logger.info("Cart resp"+response);
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
                            cart_count.setText(0 + "");

                        } else if (dd instanceof JSONObject) {

                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));

                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_count.setText(qty + "");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    private class OrderTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Order", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Order list api"+Appconstatants.myorder_api);
            Log.d("Order_url", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(Appconstatants.myorder_api, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyOrders.this);
                logger.info("Order list api resp"+response);
                Log.d("Order_resp", response);
                Log.d("url", Appconstatants.Lang);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Myorder", "order_Resp--->" + resp);

            if (resp != null) {

                try {
                    list = new ArrayList<OrdersPO>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONObject) {
                            error_network.setVisibility(View.GONE);
                            empty.setVisibility(View.VISIBLE);
                            Log.i("tag", "empty.....");
                        } else if (dd instanceof JSONArray) {
                            Log.i("tag", "data.....");
                            JSONArray arr = new JSONArray(json.getString("data"));
                            Log.d("Order_size", String.valueOf(arr.length()));

                            if (arr.length() == 0) {

                                error_network.setVisibility(View.GONE);
                                empty.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                                Log.i("tag", "empty.....");
                            } else {

                                for (int h = 0; h < arr.length(); h++) {
                                    JSONObject obj = arr.getJSONObject(h);
                                    OrdersPO bo = new OrdersPO();
                                    bo.setOrder_id(obj.isNull("order_id") ? "" : obj.getString("order_id"));
                                    bo.setOrder_name(obj.isNull("name") ? "" : obj.getString("name"));
                                    bo.setOrder_status(obj.isNull("status") ? "" : obj.getString("status"));
                                    // bo.setOrder_date(obj.isNull("date_added") ? "" : obj.getString("date_added"));
                                    if (!obj.isNull("date_added")) {
                                        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
                                        Date date = df1.parse(obj.getString("date_added"));
                                        SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yyyy");
                                        String dateText = df2.format(date);

                                        bo.setOrder_date(dateText);
                                    } else {
                                        bo.setOrder_date("");
                                    }

                                    bo.setOrder_products(obj.isNull("products") ? "" : obj.getString("products"));
                                    bo.setOrder_total(obj.isNull("total") ? "" : obj.getString("total"));
                                    bo.setPayment(obj.isNull("payment_method") ? "" : obj.getString("payment_method"));
                                    bo.setOrder_cur(obj.isNull("currency_code") ? "" : obj.getString("currency_code"));
                                    bo.setOrder_cur_val(obj.isNull("currency_value") ? "" : obj.getString("currency_value"));
                                    bo.setOrder_total_raw(obj.isNull("total_raw") ? "" : obj.getString("total_raw"));
                                    bo.setOrder_time(obj.isNull("timestamp") ? "" : obj.getString("timestamp"));
                                    JSONObject object = obj.getJSONObject("currency");
                                    bo.setCur_id(object.isNull("currency_id") ? "" : object.getString("currency_id"));
                                    bo.setSym_left(object.isNull("symbol_left") ? "" : object.getString("symbol_left"));
                                    bo.setSym_right(object.isNull("symbol_right") ? "" : object.getString("symbol_right"));
                                    bo.setDecimal(object.isNull("decimal_place") ? "" : object.getString("decimal_place"));
                                    bo.setValue(object.isNull("value") ? "" : object.getString("value"));
                                    list.add(bo);
                                }
                                adapter = new OrderAdapter(MyOrders.this, R.layout.order_list, list);
                                order.setAdapter(adapter);
                            }
                            loading.setVisibility(View.GONE);
                            error_network.setVisibility(View.GONE);
                        }
                    } else {
                        JSONArray array=json.getJSONArray("error");

                        if (array.getString(0).equalsIgnoreCase("User is not logged.")) {

                            loading.setVisibility(View.GONE);
                            Intent i = new Intent(MyOrders.this, Login.class);
                            i.putExtra("from", 3);
                            startActivityForResult(i,3);
                            Toast.makeText(MyOrders.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            loading.setVisibility(View.GONE);
                            error_network.setVisibility(View.VISIBLE);
                            errortxt1.setText(R.string.error_msg);
                            errortxt2.setText(array.getString(0)+"");
                            Toast.makeText(MyOrders.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    error_network.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    OrderTask orderTask = new OrderTask();
                                    orderTask.execute(Appconstatants.myorder_api, Appconstatants.sessiondata);

                                }
                            })
                            .show();


                }

            } else {
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }
        }

    @Override
    protected void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);

    }
}
