package com.iamretailer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.AboutAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.OptionsPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class About_Activity extends Language {

    private GridView Grid;
    private ArrayList<OptionsPO> optionsPOArrayList1;
    private Bundle bundle;
    private FrameLayout loading;
    private FrameLayout error_network;
    private TextView no_items;
    private FrameLayout full_layout;
    private TextView errortxt1;
    private TextView errortxt2;
    private AndroidLogger logger;
    private AboutTask productTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        CommonFunctions.updateAndroidSecurityProvider(this);
        DBController dbCon = new DBController(About_Activity.this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        Appconstatants.sessiondata = dbCon.getSession();
        Appconstatants.Lang = dbCon.get_lang_code();
        Appconstatants.CUR = dbCon.getCurCode();
        Log.d("Session", Appconstatants.sessiondata + "Value");
        Grid = findViewById(R.id.grid);
        LinearLayout back = findViewById(R.id.menu);
        TextView header = findViewById(R.id.header);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        loading = findViewById(R.id.loading);
        error_network = findViewById(R.id.error_network);
        String s1 = getResources().getString(R.string.about);
        String s2 = getResources().getString(R.string.app_name);
        String s3 = s1 + " " + s2;
        header.setText(s3);
        LinearLayout retry = findViewById(R.id.retry);
        no_items = findViewById(R.id.no_items);
        full_layout = findViewById(R.id.fullayout);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        productTask = new AboutTask();
        productTask.execute(Appconstatants.ABOUT);

        bundle = new Bundle();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                productTask = new AboutTask();
                productTask.execute(Appconstatants.ABOUT);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });

        Grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(About_Activity.this, Webactivity.class);
                bundle.putInt("id", optionsPOArrayList1.get(i).getId());
                bundle.putString("title", optionsPOArrayList1.get(i).getTitle());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    private class AboutTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Product list search api" + param[0]);

            String response;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, About_Activity.this);
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
                    optionsPOArrayList1 = new ArrayList<>();
                    if (json.getInt("success") == 1) {
                        JSONArray jarray = new JSONArray(json.getString("data"));

                        if (jarray.length() == 0) {
                            no_items.setVisibility(View.VISIBLE);
                            Grid.setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject jsonObject = jarray.getJSONObject(i);
                                OptionsPO item = new OptionsPO();
                                item.setId(jsonObject.isNull("id") ? 0 : jsonObject.getInt("id"));
                                item.setTitle(jsonObject.isNull("title") ? "" : jsonObject.getString("title"));
                                optionsPOArrayList1.add(item);
                            }
                            AboutAdapter adapter1 = new AboutAdapter(About_Activity.this, R.layout.about_item, optionsPOArrayList1);
                            Grid.setAdapter(adapter1);
                            Grid.setVisibility(View.VISIBLE);
                            no_items.setVisibility(View.GONE);

                        }
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);

                    } else {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error = array.getString(0) + "";
                        errortxt2.setText(error);
                        Toast.makeText(About_Activity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    Snackbar.make(full_layout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    productTask = new AboutTask();
                                    productTask.execute(Appconstatants.ABOUT);
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
