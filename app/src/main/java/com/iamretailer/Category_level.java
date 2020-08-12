package com.iamretailer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.BrandzAdapter;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.RecyclerItemClickListener;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.iamretailer.Adapter.Sub_Category_Adapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.POJO.BrandsPO;

import stutzen.co.network.Connection;


public class Category_level extends Language {

    private TextView cart_count;
    private RecyclerView main_list;
    private ArrayList<BrandsPO> cate_list;
    private BrandzAdapter adapter1;
    private FrameLayout loading;
    private FrameLayout error_network;
    private FrameLayout fullayout;
    private TextView errortxt1;
    private TextView errortxt2;
    private RecyclerView sub_cat_list;
    private Sub_Category_Adapter categoryAdapter;
    private TextView cat_name;
    private LinearLayout loading_bar;
    private AndroidLogger logger;
    private int pos;
    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_level);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        LinearLayout back = findViewById(R.id.menu);
        TextView header = findViewById(R.id.header);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_count = findViewById(R.id.cart_count);
        main_list = findViewById(R.id.main_cat_list);
        loading = findViewById(R.id.loading);
        error_network = findViewById(R.id.error_network);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        fullayout = findViewById(R.id.fullayout);
        sub_cat_list = findViewById(R.id.sub_cat_list);
        cat_name = findViewById(R.id.cat_name);
        loading_bar = findViewById(R.id.loading_bar);
        LinearLayout retry = findViewById(R.id.retry);
        DBController controller = new DBController(Category_level.this);
        Appconstatants.sessiondata = controller.getSession();
        Appconstatants.Lang = controller.get_lang_code();
        Appconstatants.CUR = controller.getCurCode();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pos = bundle.getInt("pos");
        }
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        header.setText(R.string.all_cat);
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Category_level.this, MyCart.class));
            }
        });

        Cat_Task cat_task = new Cat_Task();
        cat_task.execute(Appconstatants.CAT_LIST);
        main_list.addOnItemTouchListener(new RecyclerItemClickListener(Category_level.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                cat_name.setText(cate_list.get(position).getStore_name());
                for (int j = 0; j < cate_list.size(); j++) {
                    cate_list.get(j).setSelect(false);
                }
                cate_list.get(position).setSelect(true);
                adapter1.notifyDataSetChanged();
                if (cate_list.get(position).getArrayList().size() > 0) {
                    categoryAdapter = new Sub_Category_Adapter(Category_level.this, cate_list.get(position).getArrayList());
                    sub_cat_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    sub_cat_list.setAdapter(categoryAdapter);
                    sub_cat_list.setVisibility(View.VISIBLE);
                } else {
                    sub_cat_list.setVisibility(View.GONE);
                    Intent u = new Intent(Category_level.this, Allen.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", cate_list.get(position).getS_id());
                    bundle.putString("cat_name", cate_list.get(position).getStore_name());
                    u.putExtras(bundle);
                    startActivity(u);
                }

            }
        }));

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                error_network.setVisibility(View.GONE);
                CartTask cartTask = new CartTask();
                cartTask.execute(Appconstatants.cart_api);
                Cat_Task cat_task = new Cat_Task();
                cat_task.execute(Appconstatants.CAT_LIST);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }

    private class Cat_Task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Category_level api" + param[0]);

            Log.d("url", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Category_level.this);
                logger.info("Category_level resp" + response);
                Log.d("url response", response);
                Log.d("url_resp_Cat", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "Hai--->" + resp);
            if (resp != null) {

                try {
                    cate_list = new ArrayList<>();

                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {
                        JSONArray arr = new JSONArray(json.getString("data"));
                        for (int h = 0; h < arr.length(); h++) {
                            JSONObject obj = arr.getJSONObject(h);
                            BrandsPO bo = new BrandsPO();
                            bo.setS_id(obj.isNull("category_id") ? "" : obj.getString("category_id"));
                            bo.setStore_name(obj.isNull("name") ? "" : obj.getString("name"));
                            bo.setBg_img_url(obj.isNull("image") ? "" : obj.getString("image"));

                            JSONArray array = obj.getJSONArray("categories");
                            ArrayList<BrandsPO> cate_list2 = new ArrayList<>();
                            for (int g = 0; g < array.length(); g++) {
                                JSONObject object = array.getJSONObject(g);
                                BrandsPO po = new BrandsPO();
                                Log.d("url_resp_Catname", object.getString("name") + "");
                                po.setS_id(object.isNull("category_id") ? "" : object.getString("category_id"));
                                po.setStore_name(object.isNull("name") ? "" : object.getString("name"));

                                JSONArray array1 = object.getJSONArray("categories");
                                ArrayList<BrandsPO> cate_list3 = new ArrayList<>();
                                for (int i = 0; i < array1.length(); i++) {
                                    JSONObject object1 = array1.getJSONObject(i);
                                    BrandsPO po1 = new BrandsPO();
                                    Log.d("url_resp_Catname111", object1.getString("name") + "");
                                    po1.setS_id(object1.isNull("category_id") ? "" : object1.getString("category_id"));
                                    po1.setStore_name(object1.isNull("name") ? "" : object1.getString("name"));
                                    cate_list3.add(po1);
                                }
                                po.setArrayList1(cate_list3);

                                cate_list2.add(po);

                            }

                            bo.setArrayList(cate_list2);
                            Log.d("url_resp_Cats", cate_list2.size() + "");
                            cate_list.add(bo);

                        }

                        adapter1 = new BrandzAdapter(Category_level.this, cate_list, 2, width, height);
                        main_list.setAdapter(adapter1);
                        main_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        cat_name.setText(cate_list.get(pos).getStore_name());
                        categoryAdapter = new Sub_Category_Adapter(Category_level.this, cate_list.get(pos).getArrayList());
                        sub_cat_list.setAdapter(categoryAdapter);
                        sub_cat_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        cate_list.get(pos).setSelect(true);
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);
                    } else {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error_msg=array.getString(0) + "";
                        errortxt2.setText(error_msg);
                        Toast.makeText(Category_level.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
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
                                    Cat_Task cat_task = new Cat_Task();
                                    cat_task.execute(Appconstatants.CAT_LIST);

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


    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Cart api" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Category_level.this);
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
                        Toast.makeText(Category_level.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }

}
