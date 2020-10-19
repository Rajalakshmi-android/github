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
    private WebView webView;
    private FrameLayout loading;
    private AndroidLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        DBController controller = new DBController(PaymentAct.this);
        Appconstatants.sessiondata = controller.getSession();
        Appconstatants.Lang = controller.get_lang_code();
        Appconstatants.CUR = controller.getCurCode();
        LinearLayout menu = findViewById(R.id.menu);
        TextView header = findViewById(R.id.header);
        loading = findViewById(R.id.loading);
        header.setText(getResources().getString(R.string.pay));
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        webView = findViewById(R.id.webview);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        PAYMENT payment = new PAYMENT();
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
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, PaymentAct.this);
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

        }
    }


}
