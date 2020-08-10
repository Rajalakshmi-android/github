package com.iamretailer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.CommonAdapter;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.POJO.SingleOptionPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;

import stutzen.co.network.Connection;


public class SearchActivity extends Language {


    private DBController dbCon;
    private RecyclerView Grid;
    private LinearLayout back;
    ArrayList<ProductsPO> optionsPOArrayList1;
    CommonAdapter adapter1;
    Bundle bundle;
    private TextView cart_count;
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
    SingleProductTask productTask;
    private int start=1,limit=10;
    private ArrayList<SingleOptionPO> optionPOS;
    private ArrayList<ProductsPO> cart_item;
    GridLayoutManager mLayoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        dbCon = new DBController(SearchActivity.this);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        Appconstatants.sessiondata = dbCon.getSession();
        Appconstatants.Lang=dbCon.get_lang_code();
        Appconstatants.CUR=dbCon.getCurCode();
        Log.d("Session", Appconstatants.sessiondata + "Value");
        Grid = findViewById(R.id.grid);
        back = findViewById(R.id.menu);
        header = findViewById(R.id.header);
        cart_items = findViewById(R.id.cart_items);
        cart_count = findViewById(R.id.cart_count);
        loading = findViewById(R.id.loading);
        error_network = findViewById(R.id.error_network);
        loading_bar= findViewById(R.id.loading_bar);
        header.setText(R.string.search);
        retry = findViewById(R.id.retry);
        search_text = findViewById(R.id.search_text);
        search_loading = findViewById(R.id.search_loading);
        no_items = findViewById(R.id.no_items);
        fullayout = findViewById(R.id.fullayout);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                SingleProductTask productTask = new SingleProductTask();
                productTask.execute(Appconstatants.SEARCH + text + "&category=" + "&sub_category=" + "&description=" + "&sort=name"+"&page="+start+"&limit="+limit);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, MyCart.class));
            }
        });

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_loading.setVisibility(View.VISIBLE);
                Grid.setVisibility(View.GONE);
                no_items.setVisibility(View.GONE);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (productTask!=null)
                    productTask.cancel(true);
                search_loading.setVisibility(View.VISIBLE);
                if (s.toString().length() > 0) {
                    search_loading.setVisibility(View.VISIBLE);
                    Grid.setVisibility(View.GONE);
                    no_items.setVisibility(View.GONE);

                    text = s.toString();
                    productTask = new SingleProductTask();
                    try {
                        productTask.execute(Appconstatants.SEARCH + URLEncoder.encode(text, "UTF-8") + "&category=" + "&sub_category=" + "&description=" + "&sort=name"+"&page="+start+"&limit="+limit);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    search_loading.setVisibility(View.VISIBLE);
                    Grid.setVisibility(View.GONE);
                    no_items.setVisibility(View.GONE);

                    text = "";
                     productTask = new SingleProductTask();
                    productTask.execute(Appconstatants.SEARCH + "" + "&category=" + "&sub_category=" + "&description=" + "&sort=name"+"&page="+start+"&limit="+limit);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                search_loading.setVisibility(View.VISIBLE);
                Grid.setVisibility(View.GONE);
                no_items.setVisibility(View.GONE);
            }
        });


        loading.setVisibility(View.GONE);


    }

    @Override
    public void onResume() {
        super.onResume();

    }



    private class SingleProductTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Product list search api"+param[0]);

            Log.d("singleurl", param[0]+"");
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,SearchActivity.this);
                logger.info("Product list search resp"+response);
                Log.d("list_products", param[0]+"");
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

                    optionsPOArrayList1 = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1)
                    {

                        JSONArray jarray = new JSONArray(json.getString("data"));

                        if (jarray.length()==0)
                        {

                            search_loading.setVisibility(View.GONE);
                            no_items.setVisibility(View.VISIBLE);
                            Grid.setVisibility(View.GONE);

                        }
                        else {
                            for (int i = 0; i < jarray.length(); i++) {

                                JSONObject obj = jarray.getJSONObject(i);
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                                bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "" : obj.getString("price")));
                                bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "" : obj.getString("special")));
                                bo.setProducturl(obj.isNull("image") ? "" : obj.getString("image"));
                                bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                                bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                                bo.setWeight(obj.isNull("weight")?"":obj.getString("weight"));
                                bo.setWish_list(!obj.isNull("wish_list") && obj.getBoolean("wish_list"));
                                bo.setManufact(obj.isNull("manufacturer")?"":obj.getString("manufacturer"));
                                optionPOS = new ArrayList<>();
                                if (obj.getJSONArray("options").length() > 0) {

                                    JSONArray arr1 = obj.getJSONArray("options");
                                    for (int k = 0; k < arr1.length(); k++) {
                                        JSONObject object = arr1.getJSONObject(k);
                                        SingleOptionPO po = new SingleOptionPO();
                                        po.setProduct_option_value_id(object.isNull("product_option_id") ? 0 : object.getInt("product_option_id"));
                                        optionPOS.add(po);

                                    }
                                    bo.setSingleOptionPOS(optionPOS);
                                } else {
                                    bo.setSingleOptionPOS(optionPOS);
                                }

                                optionsPOArrayList1.add(bo);
                            }


                            CartTask cartTask = new CartTask();
                            cartTask.execute(Appconstatants.cart_api);

                        }
                        error_network.setVisibility(View.GONE);

                    } else
                        {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);

                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        errortxt2.setText(array.getString(0) + "");

                        Toast.makeText(SearchActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
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
                                    SingleProductTask productTask = new SingleProductTask();
                                    productTask.execute(Appconstatants.SEARCH + text + "&category=" + "&sub_category=" + "&description=" + "&sort=name"+"&page="+start+"&limit="+limit);
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
                Log.d("Cart_list_url", param[0]+"");
                Log.d("Cart_url_list", Appconstatants.sessiondata+"");
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,SearchActivity.this);
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
                    cart_item = new ArrayList<>();
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
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(jsonObject1.isNull("product_id") ? "" : jsonObject1.getString("product_id"));
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                                cart_item.add(bo);
                            }
                            cart_count.setText(String.valueOf(qty));

                            if (optionsPOArrayList1.size()>0 && optionsPOArrayList1!=null) {
                                for (int u = 0; u < optionsPOArrayList1.size(); u++) {
                                    for (int h = 0; h < cart_item.size(); h++) {
                                        if (Integer.parseInt(optionsPOArrayList1.get(u).getProduct_id())==Integer.parseInt(cart_item.get(h).getProduct_id())) {
                                            optionsPOArrayList1.get(u).setCart_list(true);
                                            break;
                                        }
                                        else
                                        {
                                            optionsPOArrayList1.get(u).setCart_list(false);
                                        }
                                    }
                                }
                            }

                        }
                        if (optionsPOArrayList1!=null) {
                            adapter1 = new CommonAdapter(SearchActivity.this, optionsPOArrayList1,0,3);
                            mLayoutManager=new GridLayoutManager(SearchActivity.this,2);
                            Grid.setLayoutManager(mLayoutManager);
                            Grid.setAdapter(adapter1);

                        }
                        else
                        {
                            adapter1.notifyDataSetChanged();
                        }
                        search_loading.setVisibility(View.GONE);
                        if (text.length()>0)
                            Grid.setVisibility(View.VISIBLE);
                        else
                            Grid.setVisibility(View.VISIBLE);


                        no_items.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(SearchActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }



    public void cart_inc()
    {
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }
}