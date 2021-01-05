package com.iamretailer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.CommonAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.FilterPO;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.POJO.SingleOptionPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;


public class Allen extends Language {
    private RecyclerView category;
    CommonAdapter adapter;
    ArrayList<ProductsPO> list;
    TextView product_count;
    LinearLayout cart_items;
    DBController db;
    public TextView cart_count;
    String id;
    Bundle bundle;
    FrameLayout prog_sec;
    LinearLayout menu;
    FrameLayout error_network;
    LinearLayout retry;
    LinearLayout sort;
    private TextView sort_name;
    private String sort_option = "";
    private String sort_order = "";
    private int cat_id;
    TextView no_items;
    FrameLayout loading;
    private int start = 1, limit = 20;
    LinearLayout load_more;
    int val = 0;
    TextView header;
    FrameLayout fullayout;
    ArrayList<SingleOptionPO> optionPOS;
    TextView errortxt1, errortxt2;
    AndroidLogger logger;
    private ArrayList<ProductsPO> fav_item;
    private ArrayList<ProductsPO> cart_item;
    private boolean loadin = false;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    GridLayoutManager mLayoutManager;
    Boolean value = true;
    ArrayList<FilterPO> filterPOS;
    String manufacturer = "", option_value = "", pr = "";
    private int selected = 0;
    LinearLayout filter_show, filter_lay;
    int cancel_data = 0;
    int apply = 0;
    private boolean val1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allen);
        CommonFunctions.updateAndroidSecurityProvider(this);
        db = new DBController(getApplicationContext());
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang = db.get_lang_code();
        Appconstatants.CUR = db.getCurCode();
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        category = findViewById(R.id.category_list);
        sort_name = findViewById(R.id.sort_name);
        product_count = findViewById(R.id.product_count);
        cart_items = findViewById(R.id.cart_items);
        cart_count = findViewById(R.id.cart_count);
        menu = findViewById(R.id.menu);
        error_network = findViewById(R.id.error_network);
        retry = findViewById(R.id.retry);
        sort = findViewById(R.id.sort);
        prog_sec = findViewById(R.id.prog_sec);
        no_items = findViewById(R.id.no_items);
        loading = findViewById(R.id.loading);
        load_more = findViewById(R.id.load_more);
        header = findViewById(R.id.header);
        fullayout = findViewById(R.id.fullayout);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        filter_show = findViewById(R.id.filter_show);
        filter_lay = findViewById(R.id.filter_lay);

        bundle = new Bundle();
        bundle = getIntent().getExtras();
        cat_id = Integer.parseInt(bundle.getString("id"));
        if (Build.VERSION.SDK_INT >= 24) {
            header.setText(Html.fromHtml(bundle.getString("cat_name"), Html.FROM_HTML_MODE_LEGACY));
        } else {
            header.setText(Html.fromHtml(bundle.getString("cat_name")));
        }
        sort_option = "date_added";
        sort_order = "DESC";
        sort_name.setText(R.string.news);

        filterPOS = new ArrayList<>();

        ProductTask productTask = new ProductTask();
        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);

        category.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    visibleItemCount = category.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loadin) {
                        if ((visibleItemCount + firstVisibleItem) >= (start - 1) * limit) {
                            loadin = true;
                            val = 1;
                            val1 = false;
                            load_more.setVisibility(View.VISIBLE);
                            ProductTask productTask = new ProductTask();
                            productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);
                        }
                    }
                }
            }


        });
        filter_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(Allen.this, Filter.class);
                i2.putExtra("filter_data", filterPOS);
                i2.putExtra("cat_id", cat_id);
                i2.putExtra("apply", apply);
                startActivityForResult(i2, 1);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);


            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(Allen.this);
                View sheetView = Allen.this.getLayoutInflater().inflate(R.layout.sortview, null);
                mBottomSheetDialog.setContentView(sheetView);
                LinearLayout htl, lth, newest, poupular, nameaz, nameza, modelaz, modelza;
                lth = sheetView.findViewById(R.id.low_to_high);
                htl = sheetView.findViewById(R.id.high_to_low);
                newest = sheetView.findViewById(R.id.newest);
                nameaz = sheetView.findViewById(R.id.nameaz);
                nameza = sheetView.findViewById(R.id.nameza);
                modelaz = sheetView.findViewById(R.id.modelaz);
                modelza = sheetView.findViewById(R.id.modelza);
                poupular = sheetView.findViewById(R.id.popular);
                mBottomSheetDialog.show();

                nameaz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "name";
                        sort_order = "ASC";
                        sort_name.setText(R.string.atoz);
                        val = 0;
                        val1 = true;
                        start = 1;
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);

                        mBottomSheetDialog.dismiss();
                    }
                });

                nameza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "name";
                        sort_order = "DESC";
                        sort_name.setText(R.string.ztoa);
                        val = 0;
                        val1 = true;
                        start = 1;
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);
                        mBottomSheetDialog.dismiss();
                    }
                });

                modelaz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "model";
                        sort_order = "ASC";
                        sort_name.setText(R.string.matoz);
                        val = 0;
                        val1 = true;
                        start = 1;
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);

                        mBottomSheetDialog.dismiss();
                    }
                });

                modelza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "model";
                        sort_order = "DESC";
                        val = 0;
                        val1 = true;
                        start = 1;
                        sort_name.setText(R.string.mztoa);
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);

                        mBottomSheetDialog.dismiss();
                    }
                });

                lth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        mBottomSheetDialog.dismiss();
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "price";
                        sort_order = "ASC";
                        val = 0;
                        val1 = true;
                        start = 1;
                        sort_name.setText(R.string.lowtohigh);

                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);
                    }
                });
                htl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "price";
                        sort_order = "DESC";
                        val = 0;
                        val1 = true;
                        start = 1;
                        sort_name.setText(R.string.hightolow);
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);

                        mBottomSheetDialog.dismiss();


                    }
                });
                newest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        mBottomSheetDialog.dismiss();
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "date_added";
                        sort_order = "DESC";
                        sort_name.setText(R.string.news);
                        val = 0;
                        val1 = true;
                        start = 1;
                        ProductTask productTask = new ProductTask();

                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);


                    }
                });
                poupular.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        mBottomSheetDialog.dismiss();
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "rating";
                        sort_order = "DESC";
                        sort_name.setText(R.string.popular);
                        val = 0;
                        val1 = true;
                        start = 1;
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);

                    }
                });

            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start = 1;
                limit = 20;
                loadin = false;
                error_network.setVisibility(View.GONE);
                sort.setVisibility(View.VISIBLE);
                loading.setVisibility(View.VISIBLE);
                no_items.setVisibility(View.GONE);
                ProductTask productTask = new ProductTask();
                productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Allen.this, MyCart.class));
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            filterPOS = (ArrayList<FilterPO>) data.getSerializableExtra("filter_array");
            option_value = "";
            apply = data.getExtras().getInt("apply");
            for (int y = 0; y < filterPOS.size(); y++) {
                if (filterPOS.get(y).getFilter_name().equalsIgnoreCase("brand")) {
                    manufacturer = "";
                    for (int j = 0; j < filterPOS.get(y).getFilterPOS().size(); j++) {
                        if (filterPOS.get(y).getFilterPOS().get(j).isSelected())
                            manufacturer = manufacturer + filterPOS.get(y).getFilterPOS().get(j).getSub_id() + ",";

                    }
                } else if (filterPOS.get(y).getFilter_name().contains("price")) {
                    pr = "";
                    if (filterPOS.get(y).getFilterPOS().size() > 0) {
                        if (filterPOS.get(y).getFilterPOS().get(0).isSelected())
                            pr = Math.round(filterPOS.get(y).getFilterPOS().get(0).getSeek_min()) + "," + Math.round(filterPOS.get(y).getFilterPOS().get(0).getSeek_max());
                    }
                } else {
                    for (int k = 0; k < filterPOS.get(y).getFilterPOS().size(); k++) {
                        if (filterPOS.get(y).getFilterPOS().size() > 0) {
                            if (filterPOS.get(y).getFilterPOS().get(k).isSelected()) {
                                option_value = option_value + filterPOS.get(y).getFilterPOS().get(k).getSub_id() + ",";
                            }
                        }

                    }

                }

            }
            no_items.setVisibility(View.GONE);
            prog_sec.setVisibility(View.VISIBLE);
            val = 0;
            start = 1;

            ProductTask productTask = new ProductTask();
            productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);
            for (int j = 0; j < filterPOS.size(); j++) {
                if (filterPOS.get(j).getFilterPOS().size() > 0) {
                    for (int s = 0; s < filterPOS.get(j).getFilterPOS().size(); s++) {
                        if (filterPOS.get(j).getFilterPOS().get(s).isSelected()) {
                            selected++;
                        }
                    }
                }

            }
            if (selected == 0) {
                filter_show.setVisibility(View.GONE);
            } else {
                filter_show.setVisibility(View.VISIBLE);
            }

            if (selected == 0) {
                apply = 0;

            } else {
                apply = 1;

            }
            selected = 0;

        } else {
            cancel_data = data.getIntExtra("cancel_data", 0);
            apply = data.getIntExtra("apply", 0);

            if (cancel_data == 0) {
                for (int j = 0; j < filterPOS.size(); j++) {
                    if (filterPOS.get(j).getFilterPOS().size() > 0) {
                        for (int s = 0; s < filterPOS.get(j).getFilterPOS().size(); s++) {
                            if (filterPOS.get(j).getFilterPOS().get(s).isSelected()) {
                                selected++;
                            }
                        }
                    }

                }
                if (selected == 0) {
                    filter_show.setVisibility(View.GONE);
                    apply = 0;
                } else {
                    filter_show.setVisibility(View.VISIBLE);
                    apply = 1;
                }
                selected = 0;
            } else {

                no_items.setVisibility(View.GONE);
                prog_sec.setVisibility(View.VISIBLE);
                apply = 0;
                val = 0;
                start = 1;

                selected = 0;

                if (selected == 0) {
                    filter_show.setVisibility(View.GONE);
                } else {
                    filter_show.setVisibility(View.VISIBLE);
                }
                manufacturer = "";
                option_value = "";
                pr = "";
                cancel_data = 0;
                filterPOS = new ArrayList<>();

                ProductTask productTask = new ProductTask();
                productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        if (db.getLoginCount() > 0) {
            WISH_LIST wish_list = new WISH_LIST();
            wish_list.execute(Appconstatants.Wishlist_Get);
        }
    }


    private class ProductTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Product List api" + param[0]);
            Log.i("tag", "Allen_api--- " + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.APP_DOMAIN_KEY, Appconstatants.Lang, Appconstatants.CUR, Allen.this);
                logger.info("Product List resp" + response);
                Log.d("check_ses", Appconstatants.sessiondata + "");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            load_more.setVisibility(View.GONE);
            if (resp != null) {
                try {

                    if (val == 0) {
                        list = new ArrayList<>();
                    }

                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {

                        JSONArray arr = new JSONArray(json.getString("data"));

                        for (int h = 0; h < arr.length(); h++) {
                            JSONObject obj = arr.getJSONObject(h);
                            ProductsPO bo = new ProductsPO();
                            bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                            bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                            bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "" : obj.getString("price")));
                            bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "" : obj.getString("special")));
                            bo.setProducturl(obj.isNull("image") ? "" : obj.getString("image"));
                            bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                            bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                            bo.setWeight(obj.isNull("weight") ? "" : obj.getString("weight"));
                            bo.setManufact(obj.isNull("manufacturer") ? "" : obj.getString("manufacturer"));
                            bo.setWish_list(false);
                            bo.setCart_list(false);
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
                            list.add(bo);
                        }


                        if (list.size() != 0) {
                            if (val == 0) {
                                if (val1) {
                                    if (cart_item != null && cart_item.size() > 0) {
                                        CartTask cartTask = new CartTask();
                                        cartTask.execute(Appconstatants.cart_api);

                                    }
                                    if (fav_item != null && fav_item.size() > 0) {

                                        if (db.getLoginCount() > 0) {
                                            WISH_LIST wish_list = new WISH_LIST();
                                            wish_list.execute(Appconstatants.Wishlist_Get);
                                        }
                                    }
                                }
                                adapter = new CommonAdapter(Allen.this, list, 0, 4);
                                mLayoutManager = new GridLayoutManager(Allen.this, 2);
                                category.setLayoutManager(mLayoutManager);
                                category.setAdapter(adapter);
                            } else {
                                if (cart_item != null && cart_item.size() > 0) {
                                    for (int u = 0; u < list.size(); u++) {
                                        for (int h = 0; h < cart_item.size(); h++) {
                                            if (Integer.parseInt(list.get(u).getProduct_id()) == Integer.parseInt(cart_item.get(h).getProduct_id())) {
                                                list.get(u).setCart_list(true);
                                                break;
                                            } else {
                                                list.get(u).setCart_list(false);
                                            }
                                        }
                                    }

                                }
                                if (fav_item != null && fav_item.size() > 0) {
                                    for (int u = 0; u < list.size(); u++) {
                                        for (int h = 0; h < fav_item.size(); h++) {
                                            if (Integer.parseInt(list.get(u).getProduct_id()) == Integer.parseInt(fav_item.get(h).getProduct_id())) {
                                                list.get(u).setWish_list(true);
                                                break;
                                            } else {
                                                list.get(u).setWish_list(false);
                                            }
                                        }
                                    }
                                }

                                adapter.notifyDataSetChanged();
                                no_items.setVisibility(View.GONE);
                            }
                            product_count.setText(String.valueOf(list.size()));
                            category.setVisibility(View.VISIBLE);
                            no_items.setVisibility(View.GONE);


                        } else {
                            product_count.setText(String.valueOf(list.size()));
                            no_items.setVisibility(View.VISIBLE);
                            category.setVisibility(View.GONE);

                        }

                        error_network.setVisibility(View.GONE);
                        start = start + 1;
                        prog_sec.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);

                    } else {
                        error_network.setVisibility(View.VISIBLE);
                        prog_sec.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        errortxt2.setText(array.getString(0) + "");
                        Toast.makeText(Allen.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    prog_sec.setVisibility(View.GONE);
                    error_network.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);

                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    ProductTask productTask = new ProductTask();
                                    productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit + "&manufacturer=" + method(manufacturer) + "&option_value=" + method(option_value) + "&pr=" + pr);
                                }
                            })
                            .show();


                }
            } else {
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                prog_sec.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                category.setVisibility(View.GONE);

            }

        }
    }


    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        protected String doInBackground(String... param) {

            logger.info("Cart api:" + param[0]);
            Log.i("Cart_api:", "" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();

                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.APP_DOMAIN_KEY, Appconstatants.Lang, Appconstatants.CUR, Allen.this);
                logger.info("Cart resp" + response);
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
                    cart_item = new ArrayList<>();
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
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(jsonObject1.isNull("product_id") ? "" : jsonObject1.getString("product_id"));
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                                cart_item.add(bo);
                            }

                            cart_count.setText(qty + "");
                            if (list.size() > 0 && list != null) {
                                for (int u = 0; u < list.size(); u++) {
                                    for (int h = 0; h < cart_item.size(); h++) {
                                        if (Integer.parseInt(list.get(u).getProduct_id()) == Integer.parseInt(cart_item.get(h).getProduct_id())) {
                                            list.get(u).setCart_list(true);
                                            break;
                                        } else {
                                            list.get(u).setCart_list(false);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                        }

                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Allen.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }

    private class WISH_LIST extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("WIsh list api" + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.APP_DOMAIN_KEY, Appconstatants.Lang, Appconstatants.CUR, Allen.this);
                logger.info("WIsh list api resp" + response);
                Log.d("wish_api", param[0]);
                Log.d("wish_res", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            if (resp != null) {
                try {
                    fav_item = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONArray array = json.getJSONArray("data");

                        if (array.length() > 0) {
                            for (int h = 0; h < array.length(); h++) {
                                JSONObject obj = array.getJSONObject(h);
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                fav_item.add(bo);
                            }

                            if (list.size() > 0) {
                                for (int u = 0; u < list.size(); u++) {
                                    for (int h = 0; h < fav_item.size(); h++) {
                                        if (Integer.parseInt(list.get(u).getProduct_id()) == Integer.parseInt(fav_item.get(h).getProduct_id())) {
                                            list.get(u).setWish_list(true);
                                            break;
                                        } else {
                                            list.get(u).setWish_list(false);
                                        }
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    public void cart_inc() {
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }

    public String method(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
