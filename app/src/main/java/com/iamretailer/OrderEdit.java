package com.iamretailer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import stutzen.co.network.Connection;

public class OrderEdit extends Language {

    private AndroidLogger logger;
    LinearLayout loading_bar;
    private FrameLayout fullayout;
    private FrameLayout loading;
    private FrameLayout error_network;
    private TextView errortxt1;
    private TextView errortxt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_edit);
        CommonFunctions.updateAndroidSecurityProvider(this);
        DBController dbController = new DBController(OrderEdit.this);
        Appconstatants.sessiondata= dbController.getSession();
        Appconstatants.Lang= dbController.get_lang_code();
        Appconstatants.CUR= dbController.getCurCode();
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        LinearLayout back = findViewById(R.id.menu);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        fullayout = findViewById(R.id.fullayout);
        loading_bar = findViewById(R.id.loading_bar);
        loading = findViewById(R.id.loading);
        error_network = findViewById(R.id.error_network);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        LinearLayout retry = findViewById(R.id.retry);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cart_items.setVisibility(View.GONE);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);

                ORDER_TASK order_task = new ORDER_TASK();
                order_task.execute(Appconstatants.ORDER_CUSTOMISE);

            }
        });

        ORDER_TASK order_task = new ORDER_TASK();
        order_task.execute(Appconstatants.ORDER_CUSTOMISE);
    }

    private class ORDER_TASK extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }


        protected String doInBackground(String... param) {
            logger.info("Best sellin api" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,OrderEdit.this);
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

            if (resp != null)
            {
                try {
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success")==1) {

                        JSONArray arr = new JSONArray(json.getString("data"));
                        Log.i("arrayss ",arr.length()+" ");
                    }
                    else
                    {
                        error_network.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        String error=array.getString(0) + "";
                        errortxt2.setText(error);
                        Toast.makeText(OrderEdit.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    loading_bar.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    error_network.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    ORDER_TASK order_task = new ORDER_TASK();
                                    order_task.execute(Appconstatants.ORDER_CUSTOMISE);


                                }
                            })
                            .show();

                }
            } else {
                loading.setVisibility(View.GONE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);

            }
        }
    }
}
