package com.iamretailer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.logentries.android.AndroidLogger;

import stutzen.co.network.Connection;

public class PaymentAct extends AppCompatActivity {
    WebView webView;

    LinearLayout menu;
    TextView header;
    LinearLayout cart_items;
    DBController controller;
    FrameLayout loading,error_network;
    AndroidLogger logger;
    LinearLayout loading_bar;
    FrameLayout fullayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        controller=new DBController(PaymentAct.this);
        Appconstatants.sessiondata=controller.getSession();
        Appconstatants.Lang=controller.get_lang_code();
        Appconstatants.CUR=controller.getCurCode();
        menu=(LinearLayout)findViewById(R.id.menu);
        header=(TextView)findViewById(R.id.header);
        loading=(FrameLayout)findViewById(R.id.loading);
        error_network=(FrameLayout)findViewById(R.id.error_network);
        header.setText(getResources().getString(R.string.pay));
        cart_items=(LinearLayout)findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        webView=(WebView)findViewById(R.id.webview);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        fullayout=(FrameLayout)findViewById(R.id.fullayout);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
       // webView.loadUrl(Appconstatants.PAYMENT);
        PAYMENT payment=new PAYMENT();
        payment.execute(Appconstatants.PAYMENT);

    }


    private class PAYMENT extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("payment-->" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,PaymentAct.this);
                logger.info("payment--> api:" + response);
                Log.d("payment-->api", param[0]);
                Log.d("payment-->resp", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            loading.setVisibility(View.GONE);
            webView.loadDataWithBaseURL(null, resp, "text/html", "utf-8", null);
            /*Log.i("tag", "payment-->" + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {


                        loading.setVisibility(View.GONE);
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(PaymentAct.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    PAYMENT payment=new PAYMENT();
                                    payment.execute(Appconstatants.PAYMENT);

                                }
                            })
                            .show();

                }
            } else {
                loading_bar.setVisibility(View.GONE);
                Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loading_bar.setVisibility(View.VISIBLE);
                                PAYMENT payment=new PAYMENT();
                                payment.execute(Appconstatants.PAYMENT);
                            }
                        })
                        .show();

            }*/
        }
    }



}
