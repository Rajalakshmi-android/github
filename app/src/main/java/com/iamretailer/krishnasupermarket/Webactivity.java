package com.iamretailer.krishnasupermarket;

import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.CommonFunctions;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.POJO.OptionsPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class Webactivity extends AppCompatActivity {

    private DBController dbCon;
    private LinearLayout back;
    ArrayList<OptionsPO> optionsPOArrayList1;
    Bundle bundle;
    private TextView cart_count;
    ArrayList<OptionsPO> optionsPOArrayList;
    TextView header;
    LinearLayout cart_items;
    FrameLayout loading;
    FrameLayout error_network;
    LinearLayout retry;
    EditText search_text;
    LinearLayout search_loading;
    TextView no_items;
    FrameLayout fullayout;
    String text = "";
    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    AndroidLogger logger;
    AboutTask productTask;
    private int id=0;
    private String title="";
    private WebView description;
    private String pish="";
    private String pas="",myHtmlString="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webactivity);
        CommonFunctions.updateAndroidSecurityProvider(this);
        dbCon = new DBController(Webactivity.this);
        logger=AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID,false);
        Appconstatants.sessiondata = dbCon.getSession();
        Appconstatants.Lang=dbCon.get_lang_code();
        Appconstatants.CUR=dbCon.getCurCode();
        Log.d("Session", Appconstatants.sessiondata + "Value");
        back = (LinearLayout) findViewById(R.id.menu);
        header = (TextView) findViewById(R.id.header);
        cart_items = (LinearLayout) findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        //description = (TextView) findViewById(R.id.description);
        description = (WebView) findViewById(R.id.description);
        loading = (FrameLayout) findViewById(R.id.loading);
        error_network = (FrameLayout) findViewById(R.id.error_network);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        optionsPOArrayList = new ArrayList<>();
        retry = (LinearLayout) findViewById(R.id.retry);
        search_text = (EditText) findViewById(R.id.search_text);
        search_loading = (LinearLayout) findViewById(R.id.search_loading);
        no_items = (TextView) findViewById(R.id.no_items);
        fullayout = (FrameLayout) findViewById(R.id.fullayout);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        /* pish = "<html><body>";
        pas = "</body></html>";*/

        pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/Heebo-Regular.ttf\")}body,li,p,span {font-family: MyFont;font-size: 14px;text-align: justify;color: #415163}</style></head><body>";
        pas = "</body></html>";

        id = bundle.getInt("id");
        title = bundle.getString("title");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            header.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
        } else {
            header.setText(Html.fromHtml(title));
        }

        AboutTask   productTask = new AboutTask();
        productTask.execute(Appconstatants.ABOUT_IN+id );

        bundle = new Bundle();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                AboutTask productTask = new AboutTask();
                productTask.execute(Appconstatants.ABOUT_IN+id);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });


    }
    private class AboutTask extends AsyncTask<String, Void, String> {


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
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR,Webactivity.this);
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

                        JSONObject jsonObject = new JSONObject(json.getString("data"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            header.setText(Html.fromHtml(jsonObject.isNull("title") ? "About App" : jsonObject.getString("title"), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            header.setText(Html.fromHtml(jsonObject.isNull("title") ? "About App" : jsonObject.getString("title")));
                        }

                        myHtmlString =  pish+(jsonObject.isNull("description") ? "" : jsonObject.getString("description")) + pas;
                        Log.i("string",myHtmlString);
                        //description.setText(Html.fromHtml(myHtmlString));
                       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            description.setText(Html.fromHtml(myHtmlString, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            description.setText(Html.fromHtml(myHtmlString));
                        }*/
                        description.loadDataWithBaseURL(null, myHtmlString, "text/html", "utf-8", null);
                        description.setBackgroundColor(getResources().getColor(R.color.white));


                        search_loading.setVisibility(View.GONE);

                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);

                    } else
                    {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);

                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        errortxt2.setText(array.getString(0) + "");

                        Toast.makeText(Webactivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
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
                                    AboutTask productTask = new AboutTask();
                                    productTask.execute(Appconstatants.ABOUT_IN+id );
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
}
