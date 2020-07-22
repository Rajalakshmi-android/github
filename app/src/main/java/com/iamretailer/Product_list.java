package com.iamretailer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.CommonAdapter;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.SingleOptionPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.iamretailer.Common.Appconstatants;

import com.iamretailer.POJO.ProductsPO;

import stutzen.co.network.Connection;


public class Product_list extends Language {

    FrameLayout loading,error_network,fullayout;
    ArrayList<ProductsPO> list;
    CommonAdapter bestAdapter;
    RecyclerView product_list;
    Bundle bundle;
    String from="";
    private ArrayList<ProductsPO> feat_list;
    private CommonAdapter featuredAdapter;
    LinearLayout back;
    TextView header;
    LinearLayout cart_items;
    TextView cart_counts;
    LinearLayout retry;
    private boolean scrollValue;
    private boolean scrollNeed=true;
    private int start=1,limit=10;
    LinearLayout load_more;
    int val=0;
    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    AndroidLogger logger;
    DBController dbController;
    private ArrayList<SingleOptionPO> optionPOS;
    private ArrayList<ProductsPO> fav_item;
    private TextView no_proditems;
    private ArrayList<ProductsPO> cart_item;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    GridLayoutManager mLayoutManager;
    private boolean loadin = false;
    String banner_id="";
    ArrayList<ProductsPO> banner_items;
    String title="";
    private String head="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        loading=(FrameLayout)findViewById(R.id.loading);
        error_network=(FrameLayout)findViewById(R.id.error_network);
        fullayout=(FrameLayout)findViewById(R.id.fullayout);
        product_list=(RecyclerView) findViewById(R.id.product_list);
        back=(LinearLayout)findViewById(R.id.menu);
        header=(TextView)findViewById(R.id.header);
        cart_items=(LinearLayout)findViewById(R.id.cart_items);
        cart_counts=(TextView)findViewById(R.id.cart_count);
        retry=(LinearLayout)findViewById(R.id.retry);
        load_more=(LinearLayout) findViewById(R.id.load_more);
        errortxt1=(TextView)findViewById(R.id.errortxt1);
        errortxt2=(TextView)findViewById(R.id.errortxt2);
        no_proditems=(TextView)findViewById(R.id.no_proditems);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        bundle=new Bundle();
        bundle=getIntent().getExtras();
        from=bundle.getString("view_all");
        head=bundle.getString("head");
        dbController=new DBController(Product_list.this);
        Appconstatants.sessiondata=dbController.getSession();
        Appconstatants.Lang=dbController.get_lang_code();
        Appconstatants.CUR=dbController.getCurCode();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Product_list.this, MyCart.class));
            }
        });

        if (bundle.getString("banner_id")!=null)
        {
            banner_id=bundle.getString("banner_id");
            title=bundle.getString("title");
        }






        if (from.equals("best_sell")) {
            BEST_SELLING best_selling = new BEST_SELLING();
            best_selling.execute(Appconstatants.Best_Sell+"&page="+start+"&limit="+limit);
            header.setText(head);
        }
        else if (from.equals("banners"))
        {
            BANNER banner = new BANNER();
            banner.execute(Appconstatants.BANNER_LINK+banner_id+"&page="+start+"&limit="+limit);
            header.setText(title);
        }
        else
        {
            FEATURE_TASK feature_task = new FEATURE_TASK();
            feature_task.execute(Appconstatants.Feature_api+"&page="+start+"&limit="+limit);
            header.setText(head);

        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                no_proditems.setVisibility(View.GONE);
                if (from.equals("best_sell")) {
                    BEST_SELLING best_selling = new BEST_SELLING();
                    best_selling.execute(Appconstatants.Best_Sell+"&page="+start+"&limit="+limit);
                }
                else if (from.equals("banners"))
                {
                    BANNER banner = new BANNER();
                    banner.execute(Appconstatants.BANNER_LINK+banner_id+"&page="+start+"&limit="+limit);

                }
                else
                {
                    FEATURE_TASK feature_task = new FEATURE_TASK();
                    feature_task.execute(Appconstatants.Feature_api+"&page="+start+"&limit="+limit);
                }

            }
        });

        product_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //  super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    visibleItemCount = product_list.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                    if (from.equals("best_sell"))
                    {

                        if (!loadin) {
                            if ((visibleItemCount + firstVisibleItem) >= (start-1)*limit) {
                                loadin = true;
                                val = 1;
                                load_more.setVisibility(View.VISIBLE);
                                BEST_SELLING best_selling = new BEST_SELLING();
                                best_selling.execute(Appconstatants.Best_Sell+"&page="+start+"&limit="+limit);
                            }
                        }

                    }
                    else if (from.equals("banners"))
                    {
                        if (!loadin) {
                            if ((visibleItemCount + firstVisibleItem) >= (start-1)*limit) {
                                loadin = true;
                                val = 1;
                                load_more.setVisibility(View.VISIBLE);
                                BANNER banner = new BANNER();
                                banner.execute(Appconstatants.BANNER_LINK +banner_id+ "&page=" + start + "&limit=" + limit);
                            }
                        }
                    }
                    else
                    {
                        if (!loadin) {
                            if ((visibleItemCount + firstVisibleItem) >= (start-1)*limit) {
                                loadin = true;
                                val = 1;
                                load_more.setVisibility(View.VISIBLE);
                                FEATURE_TASK feature_task = new FEATURE_TASK();
                                feature_task.execute(Appconstatants.Feature_api+"&page="+start+"&limit="+limit);
                            }
                        }
                    }

                }
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);


        if (dbController.getLoginCount()>0) {

            WISH_LIST wish_list = new WISH_LIST();
            wish_list.execute(Appconstatants.Wishlist_Get);
        }
    }

    private class BEST_SELLING extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }


        protected String doInBackground(String... param) {
            logger.info("Best sellin api"+param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Product_list.this);
                logger.info("Best sellin api"+response);
                Log.d("prducts_api", param[0]);
                Log.d("prducts_", response+"");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            scrollNeed=true;
            load_more.setVisibility(View.GONE);
            if (resp != null) {
                try {
                    if (val==0) {
                        list = new ArrayList<>();
                    }
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success")==1) {
                        JSONArray arr = new JSONArray(json.getString("data"));
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
                            bo.setWeight(obj.isNull("weight")?"":obj.getString("weight"));
                            bo.setManufact(obj.isNull("manufacturer")?"":obj.getString("manufacturer"));
                            bo.setWish_list(false);
                            bo.setCart_list(false);
                            Object dd = obj.get("option");
                            optionPOS = new ArrayList<>();
                            if (dd instanceof JSONObject)
                            {
                                JSONObject jsonObject=obj.getJSONObject("option");
                                JSONArray option=jsonObject.getJSONArray("product_option_value");
                                if (option.length()>0) {


                                    for (int k = 0; k < option.length(); k++) {
                                        JSONObject object1 = option.getJSONObject(k);
                                        SingleOptionPO po = new SingleOptionPO();
                                        po.setProduct_option_value_id(object1.isNull("product_option_value_id") ? 0 : object1.getInt("product_option_value_id"));
                                        optionPOS.add(po);

                                    }
                                    bo.setSingleOptionPOS(optionPOS);
                                }
                            }
                            else
                            {
                                bo.setSingleOptionPOS(optionPOS);
                            }

                            list.add(bo);
                        }

                        if (list.size() != 0) {
                            if (val == 0) {
                                bestAdapter = new CommonAdapter(Product_list.this,list,0,2);
                                mLayoutManager=new GridLayoutManager(Product_list.this,2);
                                product_list.setLayoutManager(mLayoutManager);
                                product_list.setAdapter(bestAdapter);

                            } else {
                                bestAdapter.notifyDataSetChanged();
                            }
                            no_proditems.setVisibility(View.GONE);
                        }else{
                            no_proditems.setVisibility(View.VISIBLE);
                            }

                        CartTask cartTask = new CartTask();
                        cartTask.execute(Appconstatants.cart_api);
                        //loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);
                        load_more.setVisibility(View.GONE);
                        Log.d("prducts_arra", arr.length() + "");

                            start = start + 1;

                    }
                    else
                    {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(Product_list.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                    Log.d("prducts_arra",scrollNeed+"   "+scrollValue);

                } catch (Exception e) {
                    e.printStackTrace();
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    BEST_SELLING best_selling = new BEST_SELLING();
                                    best_selling.execute(Appconstatants.Best_Sell+"&page="+start+"&limit="+limit);

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



    private class FEATURE_TASK extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Feature product api"+param[0]);


            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Product_list.this);
                logger.info("Feature product api resp"+response);
                Log.d("prducts_api", param[0]);
                Log.d("prducts_", response+"");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            scrollNeed=true;
            load_more.setVisibility(View.GONE);
            if (resp != null) {
                try {
                    if (val==0) {
                        feat_list = new ArrayList<>();
                    }
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success")==1)
                    {
                        JSONArray arr = new JSONArray(json.getString("data"));
                        JSONObject object = arr.getJSONObject(0);
                        JSONArray array = object.getJSONArray("products");
                        for (int h = 0; h < array.length(); h++) {
                            JSONObject obj = array.getJSONObject(h);
                            ProductsPO bo = new ProductsPO();
                            bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                            bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                            bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "" : obj.getString("price")));
                            bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "" : obj.getString("special")));
                            bo.setProducturl(obj.isNull("thumb") ? "" : obj.getString("thumb"));
                            bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                            bo.setWeight(obj.isNull("weight")?"":obj.getString("weight"));
                            bo.setManufact(obj.isNull("manufacturer")?"":obj.getString("manufacturer"));
                            bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                            bo.setWish_list(false);
                            bo.setCart_list(false);
                            Object dd = obj.get("option");
                            optionPOS = new ArrayList<>();
                            if (dd instanceof JSONObject)
                            {
                                JSONObject jsonObject=obj.getJSONObject("option");
                                JSONArray option=jsonObject.getJSONArray("product_option_value");
                                if (option.length()>0) {


                                    for (int k = 0; k < option.length(); k++) {
                                        JSONObject object1 = option.getJSONObject(k);
                                        SingleOptionPO po = new SingleOptionPO();
                                        po.setProduct_option_value_id(object1.isNull("product_option_value_id") ? 0 : object1.getInt("product_option_value_id"));
                                        optionPOS.add(po);

                                    }
                                    bo.setSingleOptionPOS(optionPOS);
                                }
                            }
                            else
                            {
                                bo.setSingleOptionPOS(optionPOS);
                            }


                            feat_list.add(bo);
                        }
                        if (feat_list.size() != 0) {
                            if (val==0) {

                                featuredAdapter = new CommonAdapter(Product_list.this, feat_list,0,2);
                                mLayoutManager=new GridLayoutManager(Product_list.this,2);
                                product_list.setLayoutManager(mLayoutManager);
                                product_list.setAdapter(featuredAdapter);
                            }
                            else
                            {
                                featuredAdapter.notifyDataSetChanged();
                            }
                            no_proditems.setVisibility(View.GONE);

                        }else{
                            no_proditems.setVisibility(View.VISIBLE);
                        }
                        //loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);

                            start = start + 1;
                        CartTask cartTask = new CartTask();
                        cartTask.execute(Appconstatants.cart_api);


                    }
                    else
                    {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt1.setText(array.getString(0)+"");
                        Toast.makeText(Product_list.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    FEATURE_TASK feature_task = new FEATURE_TASK();
                                    feature_task.execute(Appconstatants.Feature_api);

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
            logger.info("Cart api:"+param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Product_list.this);
                logger.info("Cart resp"+response);
                Log.d("Cart_list_resp", response+"");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Cart_list_resp", "CartResp--->  " + resp);
            loading.setVisibility(View.GONE);
            if (resp != null) {

                try {
                    cart_item = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONArray) {
                            cart_counts.setText(0 + "");

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
                            cart_counts.setText(qty + "");

                            if (from.equals("best_sell")) {
                                if (list.size() > 0 && list!=null) {
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
                                    bestAdapter.notifyDataSetChanged();
                                }
                            }else if( from.equals("banners")){
                                if (banner_items.size()>0 &&banner_items!=null) {
                                    for (int u = 0; u < banner_items.size(); u++) {
                                        for (int h = 0; h < cart_item.size(); h++) {
                                            if (Integer.parseInt(banner_items.get(u).getProduct_id())==Integer.parseInt(cart_item.get(h).getProduct_id())) {
                                                banner_items.get(u).setCart_list(true);
                                                break;
                                            }
                                            else
                                            {
                                                banner_items.get(u).setCart_list(false);

                                            }
                                        }
                                    }
                                    bestAdapter.notifyDataSetChanged();
                                }
                            } else {

                                if (feat_list.size() > 0 && feat_list!=null) {
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
                                    featuredAdapter.notifyDataSetChanged();
                                }
                            }

                        }
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
            logger.info("WIsh list api"+param[0]);


            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,Product_list.this);
                logger.info("WIsh list api resp"+response);
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
                        Log.d("wish_res", "ddsadsa");

                        if (array.length() > 0) {
                            for (int h = 0; h < array.length(); h++) {
                                JSONObject obj = array.getJSONObject(h);
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                fav_item.add(bo);
                            }


                            if (from.equals("best_sell")) {

                                if (list.size()>0) {
                                    for (int u = 0; u < list.size(); u++) {
                                        for (int h = 0; h < fav_item.size(); h++) {
                                            if (Integer.parseInt(list.get(u).getProduct_id())==Integer.parseInt(fav_item.get(h).getProduct_id())) {
                                                list.get(u).setWish_list(true);
                                                break;
                                            }
                                            else
                                            {
                                                list.get(u).setWish_list(false);
                                            }
                                        }
                                    }

                                    bestAdapter.notifyDataSetChanged();
                                }
                            }else if( from.equals("banners")){
                                if (banner_items.size()>0 &&banner_items!=null) {
                                    for (int u = 0; u < banner_items.size(); u++) {
                                        for (int h = 0; h < fav_item.size(); h++) {
                                            if (Integer.parseInt(banner_items.get(u).getProduct_id())==Integer.parseInt(fav_item.get(h).getProduct_id())) {
                                                banner_items.get(u).setWish_list(true);
                                                break;
                                            }
                                            else
                                            {
                                                banner_items.get(u).setWish_list(false);

                                            }
                                        }
                                    }
                                    bestAdapter.notifyDataSetChanged();
                                }
                            }
                            else
                            {
                                if (feat_list.size()>0) {
                                    for (int u = 0; u < feat_list.size(); u++) {
                                        for (int h = 0; h < fav_item.size(); h++) {
                                            if (Integer.parseInt(feat_list.get(u).getProduct_id())==Integer.parseInt(fav_item.get(h).getProduct_id())) {
                                                feat_list.get(u).setWish_list(true);
                                                break;
                                            }
                                            else
                                            {
                                                feat_list.get(u).setWish_list(false);

                                            }
                                        }
                                    }
                                    featuredAdapter.notifyDataSetChanged();
                                }
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

    public void cart_inc()
    {
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }


    private class BANNER extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }


        protected String doInBackground(String... param) {
            logger.info("Best sellin api"+param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Product_list.this);
                logger.info("Best sellin api"+response);
                Log.d("prducts_api", param[0]);
                Log.d("prducts_", response+"");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            scrollNeed=true;
            load_more.setVisibility(View.GONE);
            if (resp != null) {
                try {
                    if (val==0) {
                        banner_items = new ArrayList<>();
                    }
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success")==1) {
                        JSONArray arr = new JSONArray(json.getString("data"));
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
                            bo.setWeight(obj.isNull("weight")?"":obj.getString("weight"));
                            bo.setManufact(obj.isNull("manufacturer")?"":obj.getString("manufacturer"));
                            bo.setWish_list(false);
                            bo.setCart_list(false);
                            Object dd = obj.get("option");
                            optionPOS = new ArrayList<>();
                            if (dd instanceof JSONObject)
                            {
                                JSONObject jsonObject=obj.getJSONObject("option");
                                JSONArray option=jsonObject.getJSONArray("product_option_value");
                                if (option.length()>0) {


                                    for (int k = 0; k < option.length(); k++) {
                                        JSONObject object1 = option.getJSONObject(k);
                                        SingleOptionPO po = new SingleOptionPO();
                                        po.setProduct_option_value_id(object1.isNull("product_option_value_id") ? 0 : object1.getInt("product_option_value_id"));
                                        optionPOS.add(po);

                                    }
                                    bo.setSingleOptionPOS(optionPOS);
                                }
                            }
                            else
                            {
                                bo.setSingleOptionPOS(optionPOS);
                            }

                            banner_items.add(bo);
                        }

                        if (banner_items.size() != 0) {
                            if (val == 0) {
                                bestAdapter = new CommonAdapter(Product_list.this,banner_items,0,2);
                                mLayoutManager=new GridLayoutManager(Product_list.this,2);
                                product_list.setLayoutManager(mLayoutManager);
                                product_list.setAdapter(bestAdapter);

                            } else {
                                bestAdapter.notifyDataSetChanged();
                            }
                            no_proditems.setVisibility(View.GONE);
                        }else{
                            no_proditems.setVisibility(View.VISIBLE);
                        }

                        CartTask cartTask = new CartTask();
                        cartTask.execute(Appconstatants.cart_api);
                        //loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);
                        load_more.setVisibility(View.GONE);
                        Log.d("prducts_arra", arr.length() + "");

                        start = start + 1;

                    }
                    else
                    {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(Product_list.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                    Log.d("prducts_arra",scrollNeed+"   "+scrollValue);

                } catch (Exception e) {
                    e.printStackTrace();
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    BEST_SELLING best_selling = new BEST_SELLING();
                                    best_selling.execute(Appconstatants.Best_Sell+"&page="+start+"&limit="+limit);

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
