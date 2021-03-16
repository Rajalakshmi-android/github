package com.iamretailer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.ScratchTextView;
import com.iamretailer.Adapter.BrandzAdapter;
import com.iamretailer.Adapter.CommonAdapter;
import com.iamretailer.Adapter.DemoInfiniteAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.LanguageList;
import com.iamretailer.Common.LocaleHelper;
import com.iamretailer.Common.LoopingViewPager;
import com.iamretailer.Common.RecyclerItemClickListener;
import com.iamretailer.POJO.BannerBo;
import com.iamretailer.POJO.BrandsPO;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.POJO.SingleOptionPO;
import com.iamretailer.slider.JazzyViewPager;
import com.iamretailer.slider.OutlineContainer;
import com.logentries.android.AndroidLogger;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import stutzen.co.network.Connection;

import static android.support.design.widget.TabLayout.OnClickListener;

public class MainActivity extends Drawer {
    private DBController db;
    private TextView cart_count;
    private DrawerLayout drawerLayout;
    private int backcount;
    private RecyclerView grid;
    private ArrayList<BrandsPO> seller_list;
    private FrameLayout fullayout;
    private ArrayList<BannerBo> banner_list;
    private ArrayList<BannerBo> banner2;
    private JazzyViewPager baner2;
    private ArrayList<ProductsPO> list;
    private RecyclerView horizontalListView;
    private RecyclerView best_selling_list;
    private FrameLayout loading;
    private TextView cart_count1;
    private ArrayList<ProductsPO> feat_list;
    private PageIndicatorView indicatorView;
    private LinearLayout view_all_feat;
    private LinearLayout view_all_best;
    private int level = 0;
    private AndroidLogger logger;
    private TextView no_items;
    private TextView no_items1;
    private CommonAdapter bestAdapters;
    private ArrayList<SingleOptionPO> optionPOS;
    private CommonAdapter featuredProduct;
    private LoopingViewPager pager;
    private LinearLayout categ_preloader;
    private LinearLayout most_preloader;
    private LinearLayout newly_preloader;
    private TextView cart_count_bot;
    private TextView cat_no_items;
    private LinearLayout category;
    private ArrayList<ProductsPO> fav_item;
    private LinearLayout helps;
    private String currentVersion;
    private String downloadsize;
    LinearLayout whatsapp;
    ImageView whats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBController(MainActivity.this);
        CommonFunctions.updateAndroidSecurityProvider(this);
        if (db.get_lan_c() > 0) {
            change_langs(db.get_lang_code());
        }
        Appconstatants.Lang = db.get_lang_code();
        setContentView(R.layout.activity_main);
        if (Appconstatants.sessiondata == null || Appconstatants.sessiondata.length() == 0)
            Appconstatants.sessiondata = db.getSession();
        Appconstatants.CUR = db.getCurCode();
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        grid = findViewById(R.id.grid);
        fullayout = findViewById(R.id.fullayout);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        drawerLayout = findViewById(R.id.drawer);
        LinearLayout sidemenu = findViewById(R.id.sidemenu);
        FrameLayout drawerview = findViewById(R.id.drawerview);
        cart_count = findViewById(R.id.cart_count);
        horizontalListView = findViewById(R.id.features_list);
        best_selling_list = findViewById(R.id.best_selling_list);
        baner2 = findViewById(R.id.baner2);
        loading = findViewById(R.id.loading);
        cart_count1 = findViewById(R.id.cart_count1);
        pager = findViewById(R.id.pager);
        LinearLayout search = findViewById(R.id.search);
        indicatorView = findViewById(R.id.dots);
        view_all_feat = findViewById(R.id.view_all_feat);
        view_all_best = findViewById(R.id.view_all_best);
        no_items = findViewById(R.id.no_items);
        no_items1 = findViewById(R.id.no_items1);
        categ_preloader = findViewById(R.id.categ_preloader);
        most_preloader = findViewById(R.id.most_preloader);
        newly_preloader = findViewById(R.id.newly_preloader);
        LinearLayout cate_bottom = findViewById(R.id.cate_bottom);
        LinearLayout contact = findViewById(R.id.contact);
        LinearLayout cart_bottom = findViewById(R.id.cart_bottom);
        LinearLayout deal_bottom = findViewById(R.id.deal_bottom);
        cart_count_bot = findViewById(R.id.cart_count_bot);
        cat_no_items = findViewById(R.id.cat_no_items);
        category = findViewById(R.id.category);
        whatsapp=findViewById(R.id.whatsapp);
        whats=findViewById(R.id.whats_img);
       //offerPopup(MainActivity.this);

        cate_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seller_list != null && seller_list.size() > 0) {

                    if (level == 0) {
                        Intent intent = new Intent(MainActivity.this, Category.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, Category_level.class);
                        Bundle pos = new Bundle();
                        pos.putInt("pos", 0);
                        intent.putExtras(pos);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.pl_loading), Toast.LENGTH_SHORT).show();
                }

            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbCon.getLoginCount() > 0) {
                    startActivity(new Intent(getApplicationContext(), MyProfile.class));
                } else {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.putExtra("from", 5);
                    startActivity(intent);
                }
            }
        });
        cart_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyCart.class));
            }
        });
        deal_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Deal_list.class));
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sidemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerview(drawerview, drawerLayout);

        grid.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 3) {

                    if (level == 0) {
                        Intent intent = new Intent(MainActivity.this, Category.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, Category_level.class);
                        Bundle pos = new Bundle();
                        pos.putInt("pos", 0);
                        intent.putExtras(pos);
                        startActivity(intent);
                    }
                } else {

                    if (seller_list.get(position).getArrayList() != null && seller_list.get(position).getArrayList().size() == 0) {
                        Intent u = new Intent(MainActivity.this, Allen.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", seller_list.get(position).getS_id());
                        bundle.putString("cat_name", seller_list.get(position).getStore_name());
                        u.putExtras(bundle);
                        startActivity(u);
                    } else {
                        Intent intent = new Intent(MainActivity.this, Category_level.class);
                        Bundle pos = new Bundle();
                        pos.putInt("pos", position);
                        intent.putExtras(pos);
                        startActivity(intent);
                    }
                }

            }
        }));


        cart_items.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyCart.class));
            }
        });

        pager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
            @Override
            public void onIndicatorProgress(int selectingPosition, float progress) {

            }

            @Override
            public void onIndicatorPageChange(int newIndicatorPosition) {
                indicatorView.setSelected(newIndicatorPosition);


            }
        });


        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });


        try {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale
                        (MainActivity.this, android.Manifest.permission.CALL_PHONE)) {
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.per_msg), Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.enable),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 212);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 212);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new GetVersionCode().execute();

        GetBannerTask task1 = new GetBannerTask();
        task1.execute(Appconstatants.BANNER_IMAGEa);

        view_all_best.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Product_list.class);
                Bundle best = new Bundle();
                best.putString("view_all", "best_sell");
                best.putString("head", getResources().getString(R.string.bests));
                best.putString("banner_id", "");
                best.putString("title", "");
                intent.putExtras(best);
                startActivity(intent);
            }
        });
        view_all_feat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Product_list.class);
                Bundle feat = new Bundle();
                feat.putString("view_all", "feat");
                feat.putString("head", getResources().getString(R.string.featuress));
                feat.putString("banner_id", "");
                feat.putString("title", "");

                intent.putExtras(feat);
                startActivity(intent);
            }
        });
        if(Appconstatants.whatsapp_mode.equalsIgnoreCase("1")){
            whatsapp.setVisibility(View.VISIBLE);
        }else{
            whatsapp.setVisibility(View.GONE);
        }

        whatsapp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });

    }
  /*  private void openWhatsApp() {
        try {
            Uri uri = Uri.parse("smsto:" +Appconstatants.whatsapp_number);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(i, ""));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("tag","whatsapp----"+e);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));

        }

    }*/
    private void openWhatsApp() {
        try {
        String smsNumber = Appconstatants.whatsapp_number;
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        boolean isWhatsappInstalled1 = whatsappInstalledOrNot("com.whatsapp.w4b");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);
        }
        else if (isWhatsappInstalled1) {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp.w4b", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);

        }

        else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();

        }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("tag","whatsapp----"+e.toString());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));

        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
    public void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        backcount = 0;


        if (db.getLoginCount() > 0) {
            WISH_LIST wish_list = new WISH_LIST();
            wish_list.execute(Appconstatants.Wishlist_Get);
        }

        if (db.getLoginCount() > 0) {

            email.setText(db.getName());
            email.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            gologout.setVisibility(View.VISIBLE);
            change_pwd.setVisibility(View.VISIBLE);
            wish.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
        } else {
            gologout.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            email.setVisibility(View.GONE);
            change_pwd.setVisibility(View.GONE);
            wish.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backcount == 1) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                backcount = 1;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.exit), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Brandtask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }

        protected String doInBackground(String... param) {

            Log.d("cat_url_", param[0]);
            logger.info("Category api :" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, MainActivity.this);
                logger.info("Category resp :" + response);
                Log.d("url response", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "category_resss--->" + resp);
            if (resp != null) {

                try {
                    seller_list = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {


                        JSONArray arr = new JSONArray(json.getString("data"));

                        if (arr.length() > 0) {

                            for (int h = 0; h < arr.length(); h++) {
                                JSONObject obj = arr.getJSONObject(h);
                                if (!obj.isNull("categories") && obj.getJSONArray("categories").length() > 0)
                                    level++;
                            }

                            int size = arr.length();
                            ArrayList<BrandsPO> cate_list2;
                            if (size <= 3) {
                                for (int h = 0; h < arr.length(); h++) {
                                    JSONObject obj = arr.getJSONObject(h);
                                    BrandsPO bo = new BrandsPO();
                                    bo.setS_id(obj.isNull("category_id") ? "" : obj.getString("category_id"));
                                    bo.setStore_name(obj.isNull("name") ? "" : obj.getString("name"));
                                    bo.setBg_img_url(obj.isNull("image") ? "" : obj.getString("image"));

                                    JSONArray array = obj.getJSONArray("categories");
                                    cate_list2 = new ArrayList<>();
                                    for (int g = 0; g < array.length(); g++) {
                                        JSONObject object = array.getJSONObject(g);
                                        BrandsPO po = new BrandsPO();
                                        po.setS_id(object.isNull("category_id") ? "" : object.getString("category_id"));
                                        po.setStore_name(object.isNull("name") ? "" : object.getString("name"));
                                        cate_list2.add(po);

                                    }
                                    bo.setArrayList(cate_list2);
                                    seller_list.add(bo);

                                }
                            } else {
                                for (int h = 0; h < 4; h++) {
                                    JSONObject obj = arr.getJSONObject(h);
                                    BrandsPO bo = new BrandsPO();
                                    bo.setS_id(obj.isNull("category_id") ? "" : obj.getString("category_id"));
                                    bo.setStore_name(obj.isNull("name") ? "" : obj.getString("name"));
                                    bo.setBg_img_url(obj.isNull("image") ? "" : obj.getString("image"));
                                    JSONArray array = obj.getJSONArray("categories");
                                    cate_list2 = new ArrayList<>();
                                    for (int g = 0; g < array.length(); g++) {
                                        JSONObject object = array.getJSONObject(g);
                                        BrandsPO po = new BrandsPO();
                                        po.setS_id(object.isNull("category_id") ? "" : object.getString("category_id"));
                                        po.setStore_name(object.isNull("name") ? "" : object.getString("name"));
                                        cate_list2.add(po);

                                    }
                                    bo.setArrayList(cate_list2);

                                    seller_list.add(bo);

                                }

                            }
                            categ_preloader.setVisibility(View.GONE);
                            cat_no_items.setVisibility(View.GONE);
                            grid.setVisibility(View.VISIBLE);
                        } else {
                            categ_preloader.setVisibility(View.GONE);
                            cat_no_items.setVisibility(View.VISIBLE);
                            grid.setVisibility(View.GONE);

                        }
                        BrandzAdapter adapter1 = new BrandzAdapter(MainActivity.this, seller_list, 1, 0, 0);
                        grid.setAdapter(adapter1);
                        GridLayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 4);
                        grid.setLayoutManager(mLayoutManager);

                        BEST_SELLING best_selling = new BEST_SELLING();
                        best_selling.execute(Appconstatants.Best_Sell + "&limit=5");

                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(MainActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    GetBannerTask task1 = new GetBannerTask();
                                    task1.execute(Appconstatants.BANNER_IMAGEa);
                                }
                            })
                            .show();

                }

            } else {
                loading.setVisibility(View.GONE);
                Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loading.setVisibility(View.VISIBLE);
                                GetBannerTask task1 = new GetBannerTask();
                                task1.execute(Appconstatants.BANNER_IMAGEa);
                            }
                        })
                        .show();
            }
        }
    }

    private class GetBannerTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }

        protected String doInBackground(String... param) {

            Log.d("url_", param[0]);
            logger.info("Banner api :" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, MainActivity.this);
                logger.info("Banner api :" + response);
                Log.d("url_response", response + "");
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
                    banner_list = new ArrayList<>();
                    banner2 = new ArrayList<>();

                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {

                        JSONArray jsonObject = json.getJSONArray("data");
                        JSONObject object = jsonObject.getJSONObject(0);
                        JSONArray array = object.getJSONArray("data");
                        if (array.length() > 0) {
                            for (int h = 0; h < array.length(); h++) {

                                JSONObject obj = array.getJSONObject(h);
                                BannerBo bo = new BannerBo();
                                bo.setImage(obj.isNull("image") ? "" : obj.getString("image"));
                                bo.setLink(obj.isNull("title") ? "" : obj.getString("title"));
                                bo.setBanner_id(obj.isNull("id") ? "" : obj.getString("id"));


                                banner_list.add(bo);
                                if (h == array.length() - 1) {
                                    BannerBo bo1 = new BannerBo();
                                    bo1.setImage(obj.isNull("image") ? "" : obj.getString("image"));
                                    bo1.setLink(obj.isNull("title") ? "" : obj.getString("title"));
                                    bo.setBanner_id(obj.isNull("id") ? "" : obj.getString("id"));

                                    banner2.add(bo);
                                }


                            }
                        }

                        loading.setVisibility(View.GONE);
                        setupJazziness();
                        Brandtask brandtask = new Brandtask();
                        brandtask.execute(Appconstatants.CAT_LIST);


                    } else {

                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(MainActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    GetBannerTask task1 = new GetBannerTask();
                                    task1.execute(Appconstatants.BANNER_IMAGEa);
                                }
                            })
                            .show();

                }

            } else {
                loading.setVisibility(View.GONE);
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loading.setVisibility(View.VISIBLE);
                                GetBannerTask task1 = new GetBannerTask();
                                task1.execute(Appconstatants.BANNER_IMAGEa);
                            }
                        })
                        .show();
            }
        }
    }


    private class GetCategoryTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }

        protected String doInBackground(String... param) {

            Log.d("cat_url_", param[0]);
            logger.info("Category api :" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, MainActivity.this);
                logger.info("Category resp :" + response);
                Log.d("url response", response + "");
                Log.d("cat_url_res", response);
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
                    ArrayList<BrandsPO> seller_lists = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONArray arr = new JSONArray(json.getString("data"));
                        if (arr.length() > 0) {
                            if (arr.length() < 5) {
                                for (int h = 0; h < arr.length(); h++) {

                                    JSONObject obj = arr.getJSONObject(h);
                                    BrandsPO bo = new BrandsPO();
                                    bo.setS_id(obj.isNull("category_id") ? "" : obj.getString("category_id"));
                                    bo.setStore_name(obj.isNull("name") ? "" : obj.getString("name"));
                                    seller_lists.add(bo);
                                }
                            } else {
                                for (int h = 0; h < 5; h++) {

                                    JSONObject obj = arr.getJSONObject(h);
                                    BrandsPO bo = new BrandsPO();
                                    bo.setS_id(obj.isNull("category_id") ? "" : obj.getString("category_id"));
                                    bo.setStore_name(obj.isNull("name") ? "" : obj.getString("name"));
                                    seller_lists.add(bo);
                                }
                            }
                            category.removeAllViews();
                            if (seller_lists != null && seller_lists.size() > 0) {
                                for (int i = 0; i < seller_lists.size(); i++) {
                                    addLayoutss(seller_lists.get(i), category);
                                }
                            } else {
                                category.setVisibility(View.GONE);
                            }
                        }


                    } else {
                        loading.setVisibility(View.GONE);
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(MainActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    category.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    GetBannerTask task1 = new GetBannerTask();
                                    task1.execute(Appconstatants.BANNER_IMAGEa);
                                }
                            })
                            .show();

                }
            } else {
                loading.setVisibility(View.GONE);
                Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loading.setVisibility(View.VISIBLE);
                                GetBannerTask task1 = new GetBannerTask();
                                task1.execute(Appconstatants.BANNER_IMAGEa);
                            }
                        })
                        .show();
            }
        }
    }

    private void addLayoutss(final BrandsPO brandsbo, LinearLayout payment_list) {
        View convertView = LayoutInflater.from(this).inflate(R.layout.categlist, payment_list, false);
        TextView namess = convertView.findViewById(R.id.name);
        LinearLayout view_all = convertView.findViewById(R.id.view_all);
        final TextView no_proditems = convertView.findViewById(R.id.no_proditems);
        final RecyclerView product_list = convertView.findViewById(R.id.product_list);
        final FrameLayout product_success = convertView.findViewById(R.id.success);
        final FrameLayout product_loading = convertView.findViewById(R.id.loading);
        final FrameLayout no_network = convertView.findViewById(R.id.error_network);
        LinearLayout retry = convertView.findViewById(R.id.retry);
        namess.setText(brandsbo.getStore_name());
        GetProductTask cattask = new GetProductTask(no_proditems, product_list, product_success, product_loading, no_network);
        cattask.execute(Appconstatants.CATEGORY_PRODUCT + brandsbo.getS_id());
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_success.setVisibility(View.GONE);
                product_loading.setVisibility(View.VISIBLE);
                no_network.setVisibility(View.GONE);
                GetProductTask cattask = new GetProductTask(no_proditems, product_list, product_success, product_loading, no_network);
                cattask.execute(Appconstatants.CATEGORY_PRODUCT + brandsbo.getS_id());

            }
        });


        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(MainActivity.this, Allen.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", brandsbo.getS_id());
                bundle.putString("cat_name", brandsbo.getStore_name());
                u.putExtras(bundle);
                startActivity(u);

            }
        });
        payment_list.addView(convertView);
    }


    private class GetProductTask extends AsyncTask<String, Void, String> {
        final TextView no_proditem;
        final RecyclerView product_lists;
        final FrameLayout product_successs;
        final FrameLayout product_loadings;
        final FrameLayout no_network;
        CommonAdapter prod_Adapters;

        GetProductTask(TextView no_proditems, RecyclerView product_list, FrameLayout product_success, FrameLayout product_loading, FrameLayout no_net) {
            no_proditem = no_proditems;
            product_lists = product_list;
            product_successs = product_success;
            product_loadings = product_loading;
            no_network = no_net;

        }

        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }

        protected String doInBackground(String... param) {

            Log.d("cat_url_", param[0]);
            logger.info("Category api :" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, MainActivity.this);
                logger.info("Category resp :" + response);
                Log.d("url response", response + "");
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
                    ArrayList<ProductsPO> productbO = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {

                        if (json.has("data")) {
                            JSONArray arr = new JSONArray(json.getString("data"));

                            if (arr.length() > 0) {
                                if (arr.length() < 10) {
                                    for (int h = 0; h < arr.length(); h++) {
                                        JSONObject obj = arr.getJSONObject(h);
                                        ProductsPO bo = new ProductsPO();
                                        bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                        bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                                        bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "0.00" : obj.getString("price")));
                                        bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "0.00" : obj.getString("special")));
                                        bo.setProducturl(obj.isNull("image") ? "" : obj.getString("image"));
                                        bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                                        bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                                        bo.setWish_list(!obj.isNull("wish_list") && obj.getBoolean("wish_list"));
                                        bo.setWeight(obj.isNull("weight") ? "" : obj.getString("weight"));
                                        bo.setManufact(obj.isNull("manufacturer") ? "" : obj.getString("manufacturer"));
                                        JSONArray dd = obj.getJSONArray("option");
                                        optionPOS = new ArrayList<>();
                                        if (dd instanceof JSONArray) {
                                            JSONArray jsonObject = obj.getJSONArray("option");
                                            if (jsonObject.length() > 0) {
                                                JSONObject object = jsonObject.getJSONObject(0);
                                                JSONArray option = object.getJSONArray("product_option_value");
                                                if (option.length() > 0) {


                                                    for (int k = 0; k < option.length(); k++) {
                                                        JSONObject object1 = option.getJSONObject(k);
                                                        SingleOptionPO po = new SingleOptionPO();
                                                        po.setProduct_option_value_id(object1.isNull("product_option_value_id") ? 0 : object1.getInt("product_option_value_id"));
                                                        optionPOS.add(po);

                                                    }
                                                    bo.setSingleOptionPOS(optionPOS);
                                                } else {
                                                    bo.setSingleOptionPOS(optionPOS);
                                                }
                                            } else {
                                                bo.setSingleOptionPOS(optionPOS);
                                            }

                                        } else {
                                            bo.setSingleOptionPOS(optionPOS);
                                        }
                                        productbO.add(bo);
                                    }
                                } else {
                                    for (int h = 0; h < 10; h++) {
                                        JSONObject obj = arr.getJSONObject(h);
                                        ProductsPO bo = new ProductsPO();
                                        bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                        bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                                        bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "0.00" : obj.getString("price")));
                                        bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "0.00" : obj.getString("special")));
                                        bo.setProducturl(obj.isNull("image") ? "" : obj.getString("image"));
                                        bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                                        bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                                        bo.setWish_list(!obj.isNull("wish_list") && obj.getBoolean("wish_list"));
                                        bo.setWeight(obj.isNull("weight") ? "" : obj.getString("weight"));
                                        bo.setManufact(obj.isNull("manufacturer") ? "" : obj.getString("manufacturer"));
                                        JSONArray dd = obj.getJSONArray("option");
                                        optionPOS = new ArrayList<>();
                                        if (dd instanceof JSONArray) {
                                            JSONArray jsonObject = obj.getJSONArray("option");
                                            JSONObject object = jsonObject.getJSONObject(0);
                                            JSONArray option = object.getJSONArray("product_option_value");
                                            if (option.length() > 0) {
                                                for (int k = 0; k < option.length(); k++) {
                                                    JSONObject object1 = option.getJSONObject(k);
                                                    SingleOptionPO po = new SingleOptionPO();
                                                    po.setProduct_option_value_id(object1.isNull("product_option_value_id") ? 0 : object1.getInt("product_option_value_id"));
                                                    optionPOS.add(po);

                                                }
                                                bo.setSingleOptionPOS(optionPOS);
                                            }
                                        } else {
                                            bo.setSingleOptionPOS(optionPOS);
                                        }
                                        productbO.add(bo);
                                    }
                                }
                            }
                            if (productbO.size() != 0) {

                                prod_Adapters = new CommonAdapter(MainActivity.this, productbO, 1, 1);
                                product_lists.setAdapter(prod_Adapters);
                                product_lists.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                product_loadings.setVisibility(View.GONE);
                                product_successs.setVisibility(View.VISIBLE);
                                no_network.setVisibility(View.GONE);
                                no_proditem.setVisibility(View.GONE);
                                product_lists.setVisibility(View.VISIBLE);
                            } else {
                                product_loadings.setVisibility(View.GONE);
                                product_successs.setVisibility(View.VISIBLE);
                                no_proditem.setVisibility(View.VISIBLE);
                                product_lists.setVisibility(View.GONE);
                                no_network.setVisibility(View.GONE);
                            }
                        } else {
                            no_network.setVisibility(View.GONE);
                            product_loadings.setVisibility(View.GONE);
                            product_successs.setVisibility(View.VISIBLE);
                            no_proditem.setVisibility(View.VISIBLE);
                            product_lists.setVisibility(View.GONE);
                        }
                    } else {
                        no_network.setVisibility(View.VISIBLE);
                        product_loadings.setVisibility(View.GONE);
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(MainActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    no_network.setVisibility(View.VISIBLE);
                    product_loadings.setVisibility(View.GONE);
                }
            } else {
                product_loadings.setVisibility(View.GONE);
                no_network.setVisibility(View.VISIBLE);
            }
        }
    }


    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("cart api" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, MainActivity.this);
                logger.info("cart resp" + response);
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
                    ArrayList<ProductsPO> cart_item = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONArray) {
                            cart_count.setText(String.valueOf(0));
                            cart_count1.setText(String.valueOf(0));
                            cart_count_bot.setText(String.valueOf(0));

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
                            cart_count1.setText(String.valueOf(qty));
                            cart_count_bot.setText(String.valueOf(qty));
                        }
                        if (cart_item.size() > 0) {
                            if (feat_list != null && feat_list.size() > 0) {
                                for (int u = 0; u < feat_list.size(); u++) {
                                    for (int h = 0; h < cart_item.size(); h++) {
                                        if (Integer.parseInt(feat_list.get(u).getProduct_id()) == Integer.parseInt(cart_item.get(h).getProduct_id())) {
                                            feat_list.get(u).setCart_list(true);
                                            break;
                                        } else {
                                            feat_list.get(u).setCart_list(false);
                                        }
                                    }
                                }
                                featuredProduct.notifyDataSetChanged();
                            }

                            if (list != null && list.size() > 0) {
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


                                bestAdapters.notifyDataSetChanged();
                            }
                        } else {
                            if (feat_list != null && feat_list.size() > 0) {
                                for (int u = 0; u < feat_list.size(); u++) {

                                    feat_list.get(u).setCart_list(false);
                                }
                                featuredProduct.notifyDataSetChanged();
                            }

                            if (list != null && list.size() > 0) {
                                for (int u = 0; u < list.size(); u++) {

                                    list.get(u).setCart_list(false);

                                }


                                bestAdapters.notifyDataSetChanged();
                            }
                        }


                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(MainActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }

    private void setupJazziness() {
        pager.setAdapter(new DemoInfiniteAdapter(MainActivity.this, banner_list, true));
        indicatorView.setCount(pager.getIndicatorCount());
    }

    private void banner2() {

        baner2.setAdapter(new MainAdapter2());
        baner2.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);

    }


    private class MainAdapter2 extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            final ImageView imgDisplay;
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.banner_roundview2, container, false);
            imgDisplay = viewLayout.findViewById(R.id.browsebackground);
            LinearLayout banner = viewLayout.findViewById(R.id.banners);
            banner.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Appconstatants.need_brand_product == 1) {
                        Intent intent = new Intent(MainActivity.this, Product_list.class);
                        Bundle best = new Bundle();
                        best.putString("view_all", "banners");
                        best.putString("head", "");
                        best.putString("banner_id", banner2.get(position).getBanner_id());
                        best.putString("title", banner2.get(position).getLink());
                        intent.putExtras(best);
                        startActivity(intent);
                    }
                }
            });

            if (banner2.get(position).getImage().length() > 0 && banner2.get(position).getImage() != null)
                Picasso.with(getApplicationContext()).load(banner2.get(position).getImage()).placeholder(R.mipmap.place_holder).into(imgDisplay);
            else
                Picasso.with(getApplicationContext()).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(imgDisplay);
            container.addView(viewLayout);
            baner2.setObjectForPosition(viewLayout, position);
            return viewLayout;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object obj) {
            container.removeView(baner2.findViewFromObject(position));
        }

        @Override
        public int getCount() {
            return banner2.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }
    }


    private class FEATURE_TASK extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Feature Product api :" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, MainActivity.this);
                Log.d("prducts_api", param[0]);
                logger.info("Feature Product api :" + response);
                Log.d("prducts_", response + "");
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

                    feat_list = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {

                        JSONArray arr = new JSONArray(json.getString("data"));
                        JSONObject object = arr.getJSONObject(0);
                        if (object.has("products")) {

                            JSONArray array = object.getJSONArray("products");

                            if (array.length() > 0) {
                                for (int h = 0; h < array.length(); h++) {
                                    JSONObject obj = array.getJSONObject(h);
                                    ProductsPO bo = new ProductsPO();
                                    bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                    bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                                    bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "" : obj.getString("price")));
                                    bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "" : obj.getString("special")));
                                    bo.setProducturl(obj.isNull("thumb") ? "" : obj.getString("thumb"));
                                    bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                                    bo.setP_rate(obj.isNull("rating") ? 0 : obj.getInt("rating"));
                                    bo.setWeight(obj.isNull("weight") ? "" : obj.getString("weight"));
                                    bo.setManufact(obj.isNull("manufacturer") ? "" : obj.getString("manufacturer"));
                                    bo.setWish_list(!obj.isNull("wish_list") && obj.getBoolean("wish_list"));
                                    bo.setCart_list(false);

                                    Object dd = obj.get("option");
                                    optionPOS = new ArrayList<>();
                                    if (dd instanceof JSONObject) {
                                        JSONObject jsonObject = obj.getJSONObject("option");
                                        JSONArray option = jsonObject.getJSONArray("product_option_value");
                                        if (option.length() > 0) {


                                            for (int k = 0; k < option.length(); k++) {
                                                JSONObject object1 = option.getJSONObject(k);
                                                SingleOptionPO po = new SingleOptionPO();
                                                po.setProduct_option_value_id(object1.isNull("product_option_value_id") ? 0 : object1.getInt("product_option_value_id"));
                                                optionPOS.add(po);

                                            }
                                            bo.setSingleOptionPOS(optionPOS);
                                        }
                                    } else {
                                        bo.setSingleOptionPOS(optionPOS);
                                    }
                                    feat_list.add(bo);
                                }

                            }

                            if (feat_list != null && feat_list.size() != 0) {
                                featuredProduct = new CommonAdapter(MainActivity.this, feat_list, 1, 1);
                                horizontalListView.setAdapter(featuredProduct);
                                horizontalListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                no_items1.setVisibility(View.GONE);
                                horizontalListView.setVisibility(View.VISIBLE);
                                view_all_feat.setVisibility(View.VISIBLE);
                                newly_preloader.setVisibility(View.GONE);
                            } else {
                                no_items1.setVisibility(View.VISIBLE);
                                horizontalListView.setVisibility(View.GONE);
                                view_all_feat.setVisibility(View.GONE);
                                newly_preloader.setVisibility(View.GONE);
                            }


                        } else {
                            no_items1.setVisibility(View.VISIBLE);
                            horizontalListView.setVisibility(View.GONE);
                            view_all_feat.setVisibility(View.GONE);
                            newly_preloader.setVisibility(View.GONE);

                        }

                        GetCategoryTask cattask = new GetCategoryTask();
                        cattask.execute(Appconstatants.CATEGORY_PRODUCT);

                        CartTask cartTask = new CartTask();
                        cartTask.execute(Appconstatants.cart_api);


                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(MainActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    GetBannerTask task1 = new GetBannerTask();
                                    task1.execute(Appconstatants.BANNER_IMAGEa);
                                }
                            })
                            .show();
                }
            } else {
                loading.setVisibility(View.GONE);
                Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loading.setVisibility(View.VISIBLE);
                                GetBannerTask task1 = new GetBannerTask();
                                task1.execute(Appconstatants.BANNER_IMAGEa);
                            }
                        })
                        .show();

            }

        }
    }


    private class BEST_SELLING extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Best selling api:" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, MainActivity.this);
                logger.info("Best selling api:" + response);
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
            if (resp != null) {
                try {
                    list = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {

                        if (json.has("data")) {
                            JSONArray arr = new JSONArray(json.getString("data"));

                            if (arr.length() > 0) {
                                for (int h = 0; h < arr.length(); h++) {
                                    JSONObject obj = arr.getJSONObject(h);
                                    ProductsPO bo = new ProductsPO();
                                    bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                    bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                                    bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "" : obj.getString("price")));
                                    bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "" : obj.getString("special")));
                                    bo.setProducturl(obj.isNull("thumb") ? "" : obj.getString("thumb"));
                                    bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                                    bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                                    bo.setWeight(obj.isNull("weight") ? "" : obj.getString("weight"));
                                    bo.setManufact(obj.isNull("manufacturer") ? "" : obj.getString("manufacturer"));
                                    bo.setWish_list(!obj.isNull("wish_list") && obj.getBoolean("wish_list"));
                                    bo.setCart_list(false);
                                    Object dd = obj.get("option");
                                    optionPOS = new ArrayList<>();
                                    if (dd instanceof JSONObject) {
                                        JSONObject jsonObject = obj.getJSONObject("option");
                                        JSONArray option = jsonObject.getJSONArray("product_option_value");
                                        if (option.length() > 0) {


                                            for (int k = 0; k < option.length(); k++) {
                                                JSONObject object1 = option.getJSONObject(k);
                                                SingleOptionPO po = new SingleOptionPO();
                                                po.setProduct_option_value_id(object1.isNull("product_option_value_id") ? 0 : object1.getInt("product_option_value_id"));
                                                optionPOS.add(po);

                                            }
                                            bo.setSingleOptionPOS(optionPOS);
                                        }
                                    } else {
                                        bo.setSingleOptionPOS(optionPOS);
                                    }
                                    list.add(bo);
                                }


                            }


                            if (list != null && list.size() != 0) {
                                bestAdapters = new CommonAdapter(MainActivity.this, list, 1, 1);
                                best_selling_list.setAdapter(bestAdapters);
                                best_selling_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                no_items.setVisibility(View.GONE);
                                best_selling_list.setVisibility(View.VISIBLE);
                                view_all_best.setVisibility(View.VISIBLE);
                                most_preloader.setVisibility(View.GONE);
                                best_selling_list.setVisibility(View.VISIBLE);
                            } else {
                                no_items.setVisibility(View.VISIBLE);
                                best_selling_list.setVisibility(View.GONE);
                                view_all_best.setVisibility(View.GONE);
                                most_preloader.setVisibility(View.GONE);

                            }

                        } else {
                            no_items.setVisibility(View.VISIBLE);
                            best_selling_list.setVisibility(View.GONE);
                            view_all_best.setVisibility(View.GONE);

                        }
                        banner2();

                        FEATURE_TASK feature_task = new FEATURE_TASK();
                        feature_task.execute(Appconstatants.Feature_api + "&limit=5");


                        loading.setVisibility(View.GONE);
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(MainActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    GetBannerTask task1 = new GetBannerTask();
                                    task1.execute(Appconstatants.BANNER_IMAGEa);

                                }
                            })
                            .show();

                }
            } else {
                loading.setVisibility(View.GONE);
                Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loading.setVisibility(View.VISIBLE);
                                GetBannerTask task1 = new GetBannerTask();
                                task1.execute(Appconstatants.BANNER_IMAGEa);

                            }
                        })
                        .show();

            }
        }
    }


    private void change_langs(String languageToLoad) {

        ArrayList<String> lang_list = LanguageList.getLang_list();
        String set_lan = "en";
        if (lang_list != null && lang_list.size() > 0) {

            for (int h = 0; h < lang_list.size(); h++) {
                if (languageToLoad.contains(lang_list.get(h))) {
                    set_lan = lang_list.get(h);

                }

            }
        }


        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            // API 17+ only.
            Locale locale = new Locale(set_lan);
            Locale.setDefault(locale);
            config.locale = locale;

        } else {
            config.locale = new Locale("en");
        }

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }


    public void cart_inc() {
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 212: {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("reekkk", "Permission_granted");

                } else {
                    Log.i("reeaaaaaa", "inside");
                    Snackbar.make(findViewById(android.R.id.content), R.string.per_enable,
                            Snackbar.LENGTH_INDEFINITE).setAction(R.string.enable,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setData(Uri.parse(R.string.packge + getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(intent);
                                }
                            }).show();
                }
                break;
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
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, getApplicationContext());
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

                            if (list != null && list.size() > 0) {
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
                                bestAdapters.notifyDataSetChanged();
                            }


                            if (feat_list != null && feat_list.size() > 0) {
                                for (int u = 0; u < feat_list.size(); u++) {
                                    for (int h = 0; h < fav_item.size(); h++) {
                                        if (Integer.parseInt(feat_list.get(u).getProduct_id()) == Integer.parseInt(fav_item.get(h).getProduct_id())) {
                                            feat_list.get(u).setWish_list(true);
                                            break;
                                        } else {
                                            feat_list.get(u).setWish_list(false);
                                        }
                                    }
                                }
                                featuredProduct.notifyDataSetChanged();
                            }

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }


    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;
            String newsize = null;
            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                    Elements element1 = document.getElementsContainingOwnText("Size");
                    for (Element ele : element1) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newsize = sibElemet.text();
                            }
                        }
                    }
                } else {
                    Log.i("gkhjfikj", document.toString() + "dsafsaff");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("gkhjfikj", e.toString() + "");
            }
            Log.d("update", "Current version " + currentVersion + "playstore version " + newVersion + "size" + newsize);
            downloadsize = newsize;

            return newVersion;
        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {

                    showDetailpopup();

                }
            }

            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

        }
    }


    private void showDetailpopup() {
        AlertDialog.Builder dial = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.update_screen, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();
        TextView update = (TextView) popUpView.findViewById(R.id.update);
        TextView nothanks = (TextView) popUpView.findViewById(R.id.nothanks);
        TextView downsize = (TextView) popUpView.findViewById(R.id.downsize);
        downsize.setText(downloadsize);
        Log.d("sdfsfsd", "fjfgjfjfj");
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(intent);
                popupStore.dismiss();
            }
        });

        nothanks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                popupStore.dismiss();
            }
        });


    }


}
