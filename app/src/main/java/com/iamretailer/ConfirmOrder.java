package com.iamretailer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.OptionsPO;
import com.iamretailer.POJO.PlacePO;
import com.iamretailer.Paymentgateway.Paypal.PaypalConfig;
import com.logentries.android.AndroidLogger;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;


import stutzen.co.network.Connection;

public class ConfirmOrder extends Language {

    private LinearLayout place_list;
    private FrameLayout order;
    private TextView cus_name;
    private TextView cus_address_one;
    private TextView cus_address_two;
    private TextView cus_mobile;
    private FrameLayout error_network;
    private ArrayList<PlacePO> totals;
    private String orderid;
    private LinearLayout total_list;
    private String fname;
    private String lname;
    private String company;
    private String addressone;
    private String addresstwo;
    private String city;
    private String pincode;
    private String country;
    private String state;
    private String pay, mobile;
    private FrameLayout success;
    private DBController db;
    private TextView country_name;
    private TextView subtotal;
    private FrameLayout loading;
    private TextView phone;
    private FrameLayout fullayout;

    private TextView errortxt1;
    private TextView errortxt2;
    private String cur_left = "";
    private String cur_right = "";
    private LinearLayout loading_bar;
    private String address_id;

    private AndroidLogger logger;
    private String order_id = "";
    private double pay_tot = 0.00;
    private ImageView promo_tag;
    private ImageView promo_circle;
    private TextView promo_text;

    //Paypal integration need -->Request code and Client Id

    //Paypal
    private static final int PAYPAL_REQUEST_CODE = 123;

    // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
    // or live (ENVIRONMENT_PRODUCTION)
    private static final PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);
    private Double wallet_amount = 0.00;
    private int from;
    private TextView w_amount;

    //------paypal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        //CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);

        db = new DBController(ConfirmOrder.this);
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang = db.get_lang_code();
        Appconstatants.CUR = db.getCurCode();
        cur_left = db.get_cur_Left();
        cur_right = db.get_cur_Right();
        order = findViewById(R.id.order);
        LinearLayout back = findViewById(R.id.menu);
        LinearLayout add_edit = findViewById(R.id.address_edit);
        success = findViewById(R.id.success);
        TextView header = findViewById(R.id.header);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        loading = findViewById(R.id.loading);
        TextView payment = findViewById(R.id.payment);
        phone = findViewById(R.id.phone);
        TextView flat_shipping = findViewById(R.id.flat_shipping);
        fullayout = findViewById(R.id.fullayout);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        cart_items.setVisibility(View.GONE);
        if (getIntent().getExtras() != null) {
            fname = getIntent().getExtras().getString("fname");
            from = getIntent().getExtras().getInt("from");
            lname = getIntent().getExtras().getString("lname");
            company = getIntent().getExtras().getString("company");
            addressone = getIntent().getExtras().getString("addressone");
            addresstwo = getIntent().getExtras().getString("addresstwo");
            city = getIntent().getExtras().getString("city");
            pincode = getIntent().getExtras().getString("pincode");
            country = getIntent().getExtras().getString("country");
            state = getIntent().getExtras().getString("state");
            pay = getIntent().getExtras().getString("payment_method");
        }
        mobile = getIntent().getExtras().getString("mobile");
        address_id = getIntent().getExtras().getString("address_id");
        String shipping = getIntent().getExtras().getString("shipping");
        cus_name = findViewById(R.id.name);
        cus_address_one = findViewById(R.id.address_one);
        cus_address_two = findViewById(R.id.address_two);
        country_name = findViewById(R.id.country);
        cus_mobile = findViewById(R.id.mobile);
        loading_bar = findViewById(R.id.loading_bar);
        LinearLayout promo_code = findViewById(R.id.promo_code);
        promo_circle = findViewById(R.id.promo_circle);
        promo_tag = findViewById(R.id.promo_tag);
        promo_text = findViewById(R.id.promo_text);

        header.setText(R.string.confirm);
        String c_name = fname + " " + lname;
        cus_name.setText(c_name);
        if (addresstwo.length() != 0) {
            String cus_add = addressone + ", " + addresstwo + ",";
            cus_address_one.setText(cus_add);

        } else {
            String cus_add = addressone + ",";
            cus_address_one.setText(cus_add);
        }
        String cus_add2 = city + ", " + state + ",";
        cus_address_two.setText(cus_add2);
        String con_name = country + " - " + pincode + ".";
        country_name.setText(con_name);
        //  cus_mobile.setText();
        payment.setText(pay);
        flat_shipping.setText(shipping);

        phone.setText(mobile);
        subtotal = (TextView) findViewById(R.id.subtotal);
        promo_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_promo();

            }
        });


        error_network = findViewById(R.id.error_network);
        LinearLayout retry = findViewById(R.id.retry);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        place_list = findViewById(R.id.listview);
        total_list = findViewById(R.id.total_list);
        w_amount = findViewById(R.id.amount);

        ConfirmOrderTask confirmOrderTask = new ConfirmOrderTask();
        confirmOrderTask.execute(Appconstatants.Confirm_Order);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("dfhkdhg", pay_tot + "dkghklgd" + wallet_amount + pay.toLowerCase() + "fgdhfdh" + from);
                if (pay.toLowerCase().contains(getResources().getString(R.string.cod).toLowerCase()) || pay.toLowerCase().contains("cod")) {

                    PlaceOrderTask OrderTask = new PlaceOrderTask();
                    OrderTask.execute(Appconstatants.Place_Order);

                } else if (pay.toLowerCase().contains(getResources().getString(R.string.razorpay).toLowerCase()) || pay.toLowerCase().contains("razorpay")) {
                    OrderTask OrderTask = new OrderTask();
                    OrderTask.execute(order_id);

                } else {
                    getPayment(order_id);
                }

            }
        });
        add_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConfirmOrder.this, Address.class);

                Bundle add = new Bundle();
                add.putString("address_id", address_id);
                add.putString("first", fname);
                add.putString("last_name", lname);
                add.putString("company", company);
                add.putString("addressone", addressone);
                add.putString("addresstwo", addresstwo);
                add.putString("city", city);
                add.putString("pincode", pincode);
                add.putString("country", country);
                add.putString("state", state);
                add.putString("phone", mobile);
                add.putInt("from", 3);
                i.putExtras(add);
                startActivityForResult(i, 3);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                success.setVisibility(View.GONE);
                error_network.setVisibility(View.GONE);

                ConfirmOrderTask confirmOrderTask = new ConfirmOrderTask();
                confirmOrderTask.execute(Appconstatants.Confirm_Order);
            }
        });
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

    }


    private void show_promo() {
        AlertDialog.Builder dialLo = new AlertDialog.Builder(ConfirmOrder.this, R.style.CustomAlertDialog);
        View popUpView = getLayoutInflater().inflate(R.layout.promo_code, null);
        final EditText promo_code = popUpView.findViewById(R.id.promo_code);
        LinearLayout apply = popUpView.findViewById(R.id.apply);
        LinearLayout cancel = popUpView.findViewById(R.id.cancel);

        dialLo.setView(popUpView);

        final AlertDialog dialog = dialLo.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCancelable(true);

        dialog.show();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (promo_code.getText().toString().trim().length() == 0) {
                    promo_code.setError(getResources().getString(R.string.prom_code));
                } else {
                    dialog.dismiss();
                    CouponTask couponTask = new CouponTask();
                    couponTask.execute(promo_code.getText().toString().trim());
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private class ConfirmOrderTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

            Log.d("confirm_order", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Confirm order api" + param[0]);
            Log.d("Order_url", param[0]);
            Log.i("Order_url111", " " + param[0] + " " + Appconstatants.sessiondata + " " + Appconstatants.key1 + "  " + Appconstatants.key + "  " + Appconstatants.value + "   " + Appconstatants.Lang + "  " + Appconstatants.CUR);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.sendHttpPostjson(param[0], null, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, ConfirmOrder.this);
                logger.info("Confirm order api resp" + response);
                Log.d("Order_url", response);
                Log.d("Order_url", Appconstatants.sessiondata);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            Log.i("tag", "postmethod");
            return response;


        }

        protected void onPostExecute(String resp) {
            Log.i("confirm_order", "confirm_order--->  " + resp);

            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    totals = new ArrayList<>();
                    ArrayList<PlacePO> list = new ArrayList<>();
                    if (json.getInt("success") == 1) {
                        JSONObject jsonObject = new JSONObject(json.getString("data"));
                        order_id = jsonObject.getString("order_id");
                        JSONArray array = new JSONArray(jsonObject.getString("products"));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            JSONArray jarray = new JSONArray(jsonObject1.getString("option"));
                            PlacePO po = new PlacePO();
                            po.setKey(jsonObject1.isNull("key") ? "" : jsonObject1.getString("key"));
                            po.setProduct_id(jsonObject1.isNull("product_id") ? "" : jsonObject1.getString("product_id"));
                            po.setProduct_name(jsonObject1.isNull("name") ? "" : jsonObject1.getString("name"));
                            po.setModel(jsonObject1.isNull("model") ? "" : jsonObject1.getString("model"));
                            po.setQty(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity"));
                            po.setPrcie(jsonObject1.isNull("price") ? "" : jsonObject1.getString("price"));
                            po.setAmout(jsonObject1.isNull("total") ? "" : jsonObject1.getString("total"));
                            po.setUrl(jsonObject1.isNull("image") ? "" : jsonObject1.getString("image"));

                            ArrayList<OptionsPO> oplist = new ArrayList<>();
                            for (int u = 0; u < jarray.length(); u++) {
                                JSONObject ob = jarray.getJSONObject(u);
                                OptionsPO boo = new OptionsPO();
                                boo.setName(ob.isNull("name") ? "" : ob.getString("name"));
                                boo.setValue(ob.isNull("value") ? "" : ob.getString("value"));
                                oplist.add(boo);
                            }
                            po.setOptionlist(oplist);
                            list.add(po);
                        }
                        JSONArray array1 = new JSONArray(jsonObject.getString("totals"));
                        for (int k = 0; k < array1.length(); k++) {
                            JSONObject object = array1.getJSONObject(k);
                            PlacePO placePO = new PlacePO();
                            placePO.setTot_title(object.isNull("title") ? "" : object.getString("title"));
                            placePO.setTot_amt_txt(object.isNull("text") ? "" : object.getString("text"));
                            totals.add(placePO);
                        }
                        orderid = jsonObject.getString("order_id");

                        place_list.removeAllViews();
                        for (int i = 0; i < list.size(); i++) {

                            addLayout(list.get(i), place_list);

                        }

                        total_list.removeAllViews();
                        for (int i = 0; i < totals.size(); i++) {

                            addLayout1(totals.get(i), total_list);

                        }
                        if (totals.size() > 0) {
                            subtotal.setText(totals.get(totals.size() - 1).getTot_amt_txt());
                            if (db.get_cur_Left().length() > 0)
                                pay_tot = Double.parseDouble(totals.get(totals.size() - 1).getTot_amt_txt().replace(db.get_cur_Left(), "").replace(",", ""));
                            else
                                pay_tot = Double.parseDouble(totals.get(totals.size() - 1).getTot_amt_txt().replace(db.get_cur_Right(), "").replace(",", ""));
                        }
                        success.setVisibility(View.VISIBLE);
                        error_network.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);

                    } else {
                        success.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error_msg = array.getString(0) + "";
                        errortxt2.setText(error_msg);
                        Toast.makeText(ConfirmOrder.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    error_network.setVisibility(View.GONE);
                    success.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    ConfirmOrderTask confirmOrderTask = new ConfirmOrderTask();
                                    confirmOrderTask.execute(Appconstatants.Confirm_Order);

                                }
                            })
                            .show();


                }

            } else {
                success.setVisibility(View.GONE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

        }
    }


    private class PlaceOrderTask extends AsyncTask<String, Void, String> {


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ConfirmOrder.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("confirm_order", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Place order api" + param[0]);
            Log.d("Order_url", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.sendHttpPut(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, ConfirmOrder.this);
                logger.info("Place order api resp" + response);
                Log.d("Order_Res", response + "");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            Log.i("tag", "save_input" + param[0]);
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("confirm_order", "confirm_order--->  " + resp);
            pDialog.dismiss();
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    totals = new ArrayList<>();
                    if (json.getInt("success") == 1) {
                        JSONObject jsonObject = new JSONObject(json.getString("data"));
                        orderid = jsonObject.getInt("order_id") + "";
                        details();
                    } else {

                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(ConfirmOrder.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        order.performClick();
                                    }
                                })
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    order.performClick();

                                }
                            })
                            .show();

                    Toast.makeText(ConfirmOrder.this, R.string.failed, Toast.LENGTH_LONG).show();
                }

            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                order.performClick();
                            }
                        })
                        .show();
                Toast.makeText(ConfirmOrder.this, R.string.failed, Toast.LENGTH_LONG).show();
            }

        }
    }


    private class WalletTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Product list search api" + param[0]);

            Log.d("singleurl", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, ConfirmOrder.this);
                logger.info("Product list search resp" + response);
                Log.d("list_products", param[0]);
                Log.d("list_products", Appconstatants.sessiondata);


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "SingleProducts--->  " + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {

                        wallet_amount = Double.parseDouble(json.getString("amount"));
                        String w_amt = cur_left + String.format("%.2f", wallet_amount) + cur_right;
                        w_amount.setText(w_amt);


                        success.setVisibility(View.VISIBLE);
                        error_network.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);


                    } else {
                        success.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error_msg = array.getString(0) + "";
                        errortxt2.setText(error_msg);
                        Toast.makeText(ConfirmOrder.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    error_network.setVisibility(View.GONE);
                    success.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    ConfirmOrderTask confirmOrderTask = new ConfirmOrderTask();
                                    confirmOrderTask.execute(Appconstatants.Confirm_Order);

                                }
                            })
                            .show();


                }

            } else {
                success.setVisibility(View.GONE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

        }
    }


    private void details() {
        final Dialog dialog = new Dialog(ConfirmOrder.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_order_sucess);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        TextView order_id = dialog.findViewById(R.id.orderid);
        final LinearLayout details = dialog.findViewById(R.id.details);
        TextView header = dialog.findViewById(R.id.header);
        header.setText(R.string.order_succuess);
        LinearLayout cart_items = dialog.findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        LinearLayout back = dialog.findViewById(R.id.menu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.performClick();
            }
        });
        String v1 = "#" + orderid;
        order_id.setText(v1);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ConfirmOrder.this, MainActivity.class);
                startActivity(i);
                //  dialog.dismiss();

            }
        });


        dialog.show();


    }

    private void addLayout(final PlacePO placePO, LinearLayout view_list) {

        View convertView = LayoutInflater.from(this).inflate(R.layout.place_list, view_list, false);
        TextView name = convertView.findViewById(R.id.p_name);
        ImageView imag = convertView.findViewById(R.id.place_img);
        TextView price = convertView.findViewById(R.id.p_total);
        TextView qunt = convertView.findViewById(R.id.p_qty);
        TextView amount = convertView.findViewById(R.id.total);
        TextView p_option = convertView.findViewById(R.id.p_option);


        name.setText(placePO.getProduct_name());
        price.setText(placePO.getPrcie());
        qunt.setText(placePO.getQty());
        amount.setText(placePO.getAmout());
        StringBuilder sb2 = new StringBuilder();

        for (int h = 0; h < placePO.getOptionlist().size(); h++) {
            Log.d("option_list", placePO.getOptionlist().get(h).getValue() + "");
            sb2.append(placePO.getOptionlist().get(h).getValue());
            if (h != placePO.getOptionlist().size() - 1)
                sb2.append(",");
        }
        Log.d("option_list", String.valueOf(sb2));
        if (sb2.length() > 0) {
            p_option.setVisibility(View.VISIBLE);
        } else {
            p_option.setVisibility(View.GONE);
        }
        p_option.setText(String.valueOf(sb2));
        if (placePO.getUrl().length() != 0 && placePO.getUrl() != null)
            Picasso.with(ConfirmOrder.this).load(placePO.getUrl()).placeholder(R.mipmap.place_holder).into(imag);
        Log.i("tag", "image_view" + placePO.getUrl());
        view_list.addView(convertView);


    }

    private void addLayout1(PlacePO placePO, LinearLayout total_list) {
        View convertView = LayoutInflater.from(this).inflate(R.layout.total_item, total_list, false);
        TextView amount = convertView.findViewById(R.id.amount);
        TextView name = convertView.findViewById(R.id.head);
        amount.setText(placePO.getTot_amt_txt());
        String namess = placePO.getTot_title() + " :";
        name.setText(namess);
        Log.i("tag", "Totals " + placePO.getTot_title());
        total_list.addView(convertView);
    }


    // stop Paypal Service
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    private class CouponTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            Log.d("Login", "started");


            pDialog = new ProgressDialog(ConfirmOrder.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {

            logger.info("Coupon api " + Appconstatants.COUPON_API);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("coupon", param[0]);
                logger.info("Coupon api req " + json);
                response = connection.sendHttpPostjson(Appconstatants.COUPON_API, json, db.getSession(), Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, ConfirmOrder.this);
                logger.info("Coupon api resp " + response);
                Log.d("promo_res", response + "");
                Log.d("promo_res", json.toString() + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();

            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {

                        Toast.makeText(ConfirmOrder.this, R.string.coupon_suc, Toast.LENGTH_LONG).show();
                        promo_circle.setImageResource(R.mipmap.promo_circle_fill);
                        promo_tag.setImageResource(R.mipmap.promo_tag);
                        promo_text.setTextColor(getResources().getColor(R.color.colorAccent));
                        ConfirmOrderTask confirmOrderTask = new ConfirmOrderTask();
                        confirmOrderTask.execute(Appconstatants.Confirm_Order);


                    } else {
                      /*  promo_circle.setImageResource(R.mipmap.promo_circle_fill);
                        promo_tag.setImageResource(R.mipmap.promo_tag);
                        promo_text.setTextColor(getResources().getColor(R.color.green));*/
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(ConfirmOrder.this, array.getString(0) + "", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ConfirmOrder.this, R.string.error_msg, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(ConfirmOrder.this, R.string.error_net, Toast.LENGTH_LONG).show();
            }
        }
    }

    //-----------------------Paypal integration----------------------------------


    private void getPayment(String orderid) {
        //Getting the amount from editText
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(pay_tot)), "USD", "Order ID: " + orderid, PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //get Paypal Result

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        PlaceOrderTask OrderTask = new PlaceOrderTask();
                        OrderTask.execute(Appconstatants.Place_Order);
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
                // Toast.makeText(ConfirmOrder.this, R.string.failed, Toast.LENGTH_LONG).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                //  Toast.makeText(ConfirmOrder.this, R.string.failed, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 3 && data != null) {
            fname = data.getStringExtra("first");
            lname = data.getStringExtra("last_name");
            company = data.getStringExtra("company");
            addressone = data.getStringExtra("addressone");
            addresstwo = data.getStringExtra("addresstwo");
            city = data.getStringExtra("city");
            pincode = data.getStringExtra("pincode");
            country = data.getStringExtra("country");
            state = data.getStringExtra("state");
            mobile = data.getStringExtra("phone");
            String c_names = data.getStringExtra("first") + " " + data.getStringExtra("last_name");
            cus_name.setText(c_names);
            if (data.getStringExtra("addresstwo").length() != 0) {
                String cus_add1 = data.getStringExtra("addressone") + "," + data.getStringExtra("addresstwo") + ",";
                cus_address_one.setText(cus_add1);
            } else {
                String cus_add1 = data.getStringExtra("addressone") + ",";
                cus_address_one.setText(cus_add1);
            }
            String cus_add2 = data.getStringExtra("city") + "-" + data.getStringExtra("pincode") + ",";
            cus_address_two.setText(cus_add2);
            String cus_state = data.getStringExtra("state") + ",";
            cus_mobile.setText(cus_state);
            country_name.setText(data.getStringExtra("country"));
            phone.setText(data.getStringExtra("phone"));

        }

    }

    //--------------------------------------Razor Pay integration-----------------------------


    private void startPayment(String orid) {
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));//Razorpay Corp
            options.put("description", "Order Payment");//Demoing Charges
            options.put("currency", "INR");
            // options.put("currency", db.getCurCode());
            Log.d("Cur_va", db.getCurCode());

            JSONObject preFill = new JSONObject();
            preFill.put("email", db.getEmail());
            preFill.put("contact", mobile);
            options.put("prefill", preFill);
            options.put("order_id", orid);
            options.put("amount", pay_tot * 100);
            Log.i("razorpay", options.toString() + " ");

            co.open(activity, options);
        } catch (JSONException e) {
            Log.d("sss_", e.toString());
            Toast.makeText(ConfirmOrder.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unused")
    public void onPaymentSuccess(String razorpayPaymentID) {

        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

        PaymentSuccess OrderTask = new PaymentSuccess();
        OrderTask.execute(razorpayPaymentID);


    }

    @SuppressWarnings("unused")
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Log.d("Razor_resp", response + "");
        } catch (Exception e) {
            Log.e("Pay_status_erro", "Exception in onPaymentError", e);
        }

        new AlertDialog.Builder(ConfirmOrder.this)
                .setCancelable(false)
                .setMessage("Payment Transaction Failed ")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private class PaymentSuccess extends AsyncTask<Object, Void, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ConfirmOrder.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("payment", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("payment_success_api " + Appconstatants.Payment_Success);
            Log.i("tag", "payment_success_api " + Appconstatants.Payment_Success);

            String response = null;
            Connection connection = new Connection();
            try {

                JSONObject json = new JSONObject();
                json.put("order_id", order_id);
                json.put("razorpay_payment_id", param[0]);


                Log.d("Cart_input", json.toString());
                Log.d("Cart_url_insert", Appconstatants.sessiondata + "");
                Log.i("tag", "payment_success_item" + json);
                logger.info("payment_success" + json);
                response = connection.sendHttpPostjson(Appconstatants.Payment_Success, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, ConfirmOrder.this);
                Log.i("tag", "payment_success" + response);
                logger.info("payment_success" + response);


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Cart", "payment_success11--" + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        PlaceOrderTask OrderTask = new PlaceOrderTask();
                        OrderTask.execute(Appconstatants.Place_Order);
                    } else {
                        new AlertDialog.Builder(ConfirmOrder.this)
                                .setCancelable(false)
                                .setMessage("Payment Transaction Failed .If any amount deducted from your account.Please contact +918525990990")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        JSONArray error = json.getJSONArray("error");
                        String error_msg = error.getString(0);
                        Toast.makeText(ConfirmOrder.this, error_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    new AlertDialog.Builder(ConfirmOrder.this)
                            .setCancelable(false)
                            .setMessage("Payment Transaction Failed .If any amount deducted from your account.Please contact +918525990990")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                    Toast.makeText(ConfirmOrder.this, R.string.error_msg, Toast.LENGTH_SHORT).show();

                }
            } else {
                new AlertDialog.Builder(ConfirmOrder.this)
                        .setCancelable(false)
                        .setMessage("Payment Transaction Failed .If any amount deducted from your account.Please contact +918525990990")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();
                Toast.makeText(ConfirmOrder.this, R.string.error_net, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class OrderTask extends AsyncTask<Object, Void, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ConfirmOrder.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("payment", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("payment_success_api " + Appconstatants.razorpay);
            Log.i("tag", "payment_success_api " + Appconstatants.razorpay);

            String response = null;
            Connection connection = new Connection();
            try {

                JSONObject json = new JSONObject();
                json.put("order_id", order_id);
                //  json.put("order_id", param[0]);


                Log.d("Cart_input", json.toString());
                Log.d("Cart_url_insert", Appconstatants.sessiondata + "");
                Log.i("tag", "payment_success_item" + json);
                logger.info("payment_success" + json);
                response = connection.sendHttpPostjson(Appconstatants.razorpay, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, ConfirmOrder.this);
                Log.i("tag", "payment_success" + response);
                logger.info("payment_success" + response);


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Cart", "payment_success11--" + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {

                        startPayment(json.getString("order_id"));

                    } else {
                        new AlertDialog.Builder(ConfirmOrder.this)
                                .setCancelable(false)
                                .setMessage("Payment Transaction Failed .If any amount deducted from your account.Please contact +918525990990")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        JSONArray error = json.getJSONArray("error");
                        String error_msg = error.getString(0);
                        Toast.makeText(ConfirmOrder.this, error_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    new AlertDialog.Builder(ConfirmOrder.this)
                            .setCancelable(false)
                            .setMessage("Payment Transaction Failed .If any amount deducted from your account.Please contact +918525990990")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                    Toast.makeText(ConfirmOrder.this, R.string.error_msg, Toast.LENGTH_SHORT).show();

                }
            } else {
                new AlertDialog.Builder(ConfirmOrder.this)
                        .setCancelable(false)
                        .setMessage("Payment Transaction Failed .If any amount deducted from your account.Please contact +918525990990")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();
                Toast.makeText(ConfirmOrder.this, R.string.error_net, Toast.LENGTH_SHORT).show();
            }

        }
    }

}
