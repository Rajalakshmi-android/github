package com.iamretailer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.WalletAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.OptionsPO;
import com.iamretailer.Paymentgateway.Paypal.PaypalConfig;
import com.logentries.android.AndroidLogger;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import stutzen.co.network.Connection;

public class Wallet extends Language {
    LinearLayout menu;
    TextView header,cart_count;
    LinearLayout cart_items;
    AndroidLogger logger;
    FrameLayout fullayout;
    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    FrameLayout error_network;
    FrameLayout loading;
    private ArrayList<OptionsPO> optionsPOArrayList1;
    TextView no_items;
    FrameLayout list_view;
    private WalletAdapter adapter1;
    ListView wallet_list;
    LinearLayout retry;
    private DBController dbCon;

    public static final int PAYPAL_REQUEST_CODE = 123;

    // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
    // or live (ENVIRONMENT_PRODUCTION)
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);
    private String mobile="";
    private ProgressDialog pDialog;
    private String wallet_amount="0.00";
    private TextView w_amount;
    private int pos=-1;
    String cur_left = "";
    String cur_right = "";
    private LinearLayout cont;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallent);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        menu= findViewById(R.id.menu);
        header= findViewById(R.id.header);
        cart_items= findViewById(R.id.cart_items);
        fullayout= findViewById(R.id.fullayout);
        cont= findViewById(R.id.cont);
        header.setText(R.string.wallet_head);
        cart_count= findViewById(R.id.cart_count);
        loading_bar= findViewById(R.id.loading_bar);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        w_amount = findViewById(R.id.amount);
        error_network = findViewById(R.id.error_network);
        no_items= findViewById(R.id.no_items);
        loading = findViewById(R.id.loading);
        list_view= findViewById(R.id.success);
        wallet_list= findViewById(R.id.wallet_list);
        retry = findViewById(R.id.retry);

        dbCon = new DBController(Wallet.this);
        Appconstatants.sessiondata = dbCon.getSession();
        Appconstatants.Lang=dbCon.get_lang_code();
        Appconstatants.CUR=dbCon.getCurCode();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pDialog = new ProgressDialog(Wallet.this);
        pDialog.setMessage(getResources().getString(R.string.loading_wait));
        pDialog.setCancelable(false);
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wallet.this,MyCart.class));
            }
        });
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        WALLETTASK wallettask=new WALLETTASK();
        wallettask.execute(Appconstatants.WALLET );
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_items.setVisibility(View.GONE);
                list_view.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                error_network.setVisibility(View.GONE);
                CartTask cartTask = new CartTask();
                cartTask.execute(Appconstatants.cart_api);
                WALLETTASK wallettask=new WALLETTASK();
                wallettask.execute(Appconstatants.WALLET );
            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos != -1){
                    WalletSaveTask task=new WalletSaveTask(optionsPOArrayList1.get(pos).getProduct_id());
                    task.execute(optionsPOArrayList1.get(pos).getProduct_id(), "cod", "Cash On Delivery");
                }else{
                    Toast.makeText(Wallet.this, R.string.select_wallet, Toast.LENGTH_SHORT).show();

                }

            }
        });
        cur_left = dbCon.get_cur_Left();
        cur_right=dbCon.get_cur_Right();
        wallet_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // startPayment(optionsPOArrayList1.get(position).getProduct_id());
                for (int u = 0; u < optionsPOArrayList1.size(); u++) {
                    optionsPOArrayList1.get(u).setSelect(false);
                }
                optionsPOArrayList1.get(position).setSelect(true);
                adapter1.notifyDataSetChanged();
                pos = position;
            }
        });

      /*  Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);*/
    }

    public void startPayment(String orderid) {
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", R.string.app_name);//Razorpay Corp
            options.put("description", "Order Payment");//Demoing Charges
            options.put("currency", "INR");
            // options.put("currency", db.getCurCode());
            Log.d("Cur_va",dbCon.getCurCode());

            JSONObject preFill = new JSONObject();
            preFill.put("email", dbCon.getEmail());
            preFill.put("contact",mobile );

            options.put("prefill", preFill);

            //   options.put("amount",pay_tot*100);
            options.put("amount",100);

            co.open(activity, options);
        } catch (JSONException e) {
            Log.d("sss_",e.toString());
            Toast.makeText(Wallet.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unused")
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Pay_status_success", "Exception in onPaymentSuccess", e);
        }

        new AlertDialog.Builder(Wallet.this)
                .setCancelable(false)
                .setMessage("Payment Completed successfully ")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @SuppressWarnings("unused")
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Log.d("Razor_resp",response+"");
        } catch (Exception e) {
            Log.e("Pay_status_erro", "Exception in onPaymentError", e);
        }

        new AlertDialog.Builder(Wallet.this)
                .setCancelable(false)
                .setMessage("Payment Transaction Failed ")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }

    private class WalletSaveTask extends AsyncTask<Object, Void, String> {

    String product_id;

        public WalletSaveTask(String productid) {
            product_id= productid;
        }

        @Override
        protected void onPreExecute() {

            pDialog.show();
            Log.d("Cart", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("Cart Save api"+Appconstatants.cart_api);

            String response = null;
            Connection connection = new Connection();
            try {


                JSONObject json = new JSONObject();
                json.put("product_id", param[0]);
                json.put("payment_code", param[1]);
                json.put("payment_method", param[2]);

                Log.d("Cart_input", json.toString());
                Log.d("Cart_url_insert", Appconstatants.sessiondata + "");
                logger.info("Cart Save req"+json);
                response = connection.sendHttpPostjson(Appconstatants.Wallet_api, json, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,Wallet.this);
                logger.info("Cart Save resp"+response);


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
                        JSONObject obj =json.getJSONObject("data");
                        showCallPopup(product_id, obj.getString("order_id"));
                    } else {

                        JSONArray error = json.getJSONArray("error");
                        String error_msg = error.getString(0);
                        Toast.makeText(Wallet.this, error_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg,
                            Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wallet_list.performClick();
                        }
                    })
                            .show();
                    Toast.makeText(Wallet.this, R.string.error_msg, Toast.LENGTH_SHORT).show();

                }
            } else {
                Snackbar.make(fullayout, R.string.error_net,
                        Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wallet_list.performClick();
                    }
                }).show();
                Toast.makeText(Wallet.this, R.string.error_net, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class WALLETSAVE extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            pDialog.show();
        }

        protected String doInBackground(String... param) {
            String response = null;
            try {
                JSONObject json = new JSONObject();
                json.put("product_id", param[0]);
                Connection connection = new Connection();
                response = connection.sendHttpPutjson1(Appconstatants.Wallet_api , json, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,Wallet.this);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            Log.i("tag", "save_input" + param[0]);
            return response;
        }

        protected void onPostExecute(String resp) {

            pDialog.dismiss();
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {


                        Toast.makeText(getApplicationContext(),"Wallet added Successfully ", Toast.LENGTH_SHORT).show();

                        WALLETTASK wallettask=new WALLETTASK();
                        wallettask.execute(Appconstatants.WALLET );
                        pos=-1;


                    }  else {

                        JSONArray error = json.getJSONArray("error");
                        String error_msg = error.getString(0);
                        Toast.makeText(Wallet.this, error_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg,
                            Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wallet_list.performClick();
                        }
                    })
                            .show();
                    Toast.makeText(Wallet.this, R.string.error_msg, Toast.LENGTH_SHORT).show();

                }
            } else {
                Snackbar.make(fullayout, R.string.error_net,
                        Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wallet_list.performClick();
                    }
                }).show();
                Toast.makeText(Wallet.this, R.string.error_net, Toast.LENGTH_SHORT).show();
            }

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
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,Wallet.this);
                logger.info("Cart resp"+response);
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
                        Toast.makeText(Wallet.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
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
                    /*    PlaceOrderTask OrderTask = new PlaceOrderTask();
                        OrderTask.execute(Appconstatants.Place_Order);*/
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
        }
    }
    private class WALLETTASK extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Product list search api"+param[0]);

            Log.d("singleurl", param[0]);
            Log.d("adsadad",Appconstatants.Lang);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,Wallet.this);
                logger.info("Product list search resp"+response);
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
                    optionsPOArrayList1 = new ArrayList<>();
                    if (json.getInt("success") == 1)
                    {

                        JSONArray jarray = new JSONArray(json.getString("data"));

                        if (jarray.length()==0)
                        {

                            no_items.setVisibility(View.VISIBLE);
                            list_view.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            error_network.setVisibility(View.GONE);
                        }
                        else {
                            for (int i = 0; i < jarray.length(); i++) {

                                JSONObject jsonObject = jarray.getJSONObject(i);

                                OptionsPO item = new OptionsPO();
                                item.setP_id(jsonObject.isNull("id") ? "" : jsonObject.getString("id"));
                                item.setProduct_id(jsonObject.isNull("product_id") ? "" : jsonObject.getString("product_id"));
                                item.setName(jsonObject.isNull("name") ? "" : jsonObject.getString("name"));
                                item.setRate(jsonObject.isNull("price") ? 0 : jsonObject.getDouble("price"));
                                item.setImage(jsonObject.isNull("image") ? "" : jsonObject.getString("image"));
                                item.setOffer_rate(jsonObject.isNull("special") ? 0 : jsonObject.getDouble("special"));
                                item.setModel(jsonObject.isNull("model") ? "" : jsonObject.getString("model"));
                                item.setTax(jsonObject.isNull("price_excluding_tax_formated") ? "" : jsonObject.getString("price_excluding_tax_formated"));
                                item.setPrice(jsonObject.isNull("price") ? "" : jsonObject.getString("price"));
                                item.setProductrate(jsonObject.isNull("price_formated") ? "" : jsonObject.getString("price_formated"));
                                item.setDescription(jsonObject.isNull("description") ? "" : jsonObject.getString("description"));
                                item.setQty(jsonObject.isNull("quantity") ? 0 : jsonObject.getInt("quantity"));
                                item.setWish_list(!jsonObject.isNull("wish_list") && jsonObject.getBoolean("wish_list"));

                                optionsPOArrayList1.add(item);
                            }

                            if (optionsPOArrayList1!=null) {
                                adapter1 = new WalletAdapter(Wallet.this, R.layout.wallet_list, optionsPOArrayList1,1);
                                wallet_list.setAdapter(adapter1);
                            }
                            else
                            {
                                adapter1.notifyDataSetChanged();
                            }


                        }

                        WalletTasks productTask = new WalletTasks();
                        productTask.execute(Appconstatants.Wallet_Amount );


                    } else
                    {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);

                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error=array.getString(0) + "";
                        errortxt2.setText(error);

                        Toast.makeText(Wallet.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    WALLETTASK wallettask=new WALLETTASK();
                                    wallettask.execute(Appconstatants.WALLET );
                                }
                            })
                            .show();
                }
            } else {
                error_network.setVisibility(View.VISIBLE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                loading.setVisibility(View.GONE);

            }
        }
    }

    private class WalletTasks extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Product list search api"+param[0]);

            Log.d("singleurl", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,Wallet.this);
                logger.info("Product list search resp"+response);
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
                    if (json.getInt("success") == 1)
                    {

                        JSONArray jarray = new JSONArray(json.getString("data"));
                        wallet_amount= json.getString("amount");
                        String value=cur_left+String.format(Locale.getDefault(),"%.2f", Double.parseDouble(wallet_amount))+cur_right;
                        w_amount.setText(value);
                        list_view.setVisibility(View.VISIBLE);
                        no_items.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);




                    } else
                    {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);

                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error=array.getString(0) + "";
                        errortxt2.setText(error);

                        Toast.makeText(Wallet.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    WALLETTASK wallettask=new WALLETTASK();
                                    wallettask.execute(Appconstatants.WALLET );
                                }
                            })
                            .show();
                }
            } else {
                error_network.setVisibility(View.VISIBLE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                loading.setVisibility(View.GONE);

            }
        }
    }

    public void showCallPopup(final String product_id, String order){
        final android.app.AlertDialog.Builder dial = new android.app.AlertDialog.Builder(Wallet.this);
        final ViewGroup parent = null;
        View popUpView = getLayoutInflater().inflate(R.layout.pay_sucess, parent,false);
        dial.setView(popUpView);
        final android.app.AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity= Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();
        popupStore.setCancelable(false);
        LinearLayout okay= popUpView.findViewById(R.id.okay);
        TextView text1= popUpView.findViewById(R.id.text1);
        String txt="ORDER ID: "+order;
        text1.setText(txt);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupStore.dismiss();
                WALLETSAVE task=new WALLETSAVE();
                task.execute(product_id);
            }
        });
        LinearLayout notokay= popUpView.findViewById(R.id.notokay);
        notokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupStore.dismiss();
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
        });

    }
}
