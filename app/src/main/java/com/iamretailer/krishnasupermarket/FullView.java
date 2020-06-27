package com.iamretailer.krishnasupermarket;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.krishnasupermarket.Common.CommonFunctions;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.iamretailer.krishnasupermarket.Adapter.PointerAdapter;
import com.iamretailer.krishnasupermarket.Common.Appconstatants;

import com.iamretailer.krishnasupermarket.Fragments.HorizontalListView;
import com.iamretailer.krishnasupermarket.Fragments.Imagefragment;
import com.iamretailer.krishnasupermarket.POJO.ImgBo;



import java.util.ArrayList;

import stutzen.co.network.Connection;

public class FullView extends Language implements Imagefragment.OnFragmentInteractionListener {
    public String[] mImageIds = {};
    HorizontalListView horizontalListView;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    PointerAdapter pointerAdapter;
    private ArrayList<ImgBo> datalist;
    Bundle bundle;
    ArrayList<String> img;
    TextView cart_count;
    LinearLayout cart_items;
    AndroidLogger logger;
    DBController dbController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);
        CommonFunctions.updateAndroidSecurityProvider(this);
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        img = bundle.getStringArrayList("url");
        Log.d("Image", img.toString());
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        dbController=new DBController(FullView.this);
        Appconstatants.sessiondata=dbController.getSession();
        Appconstatants.Lang=dbController.get_lang_code();
        Appconstatants.CUR=dbController.getCurCode();
        LinearLayout menu = (LinearLayout) findViewById(R.id.menu);
        horizontalListView = (HorizontalListView) findViewById(R.id.horizlist);
        cart_count=(TextView)findViewById(R.id.cart_count);
        cart_items=(LinearLayout)findViewById(R.id.cart_items);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        LinearLayout cart_items = (LinearLayout) findViewById(R.id.cart_items);
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FullView.this, MyCart.class));
            }
        });
        datalist = new ArrayList<ImgBo>();
        for (int i = 0; i < img.size(); i++) {
            ImgBo bo = new ImgBo();
            bo.setUrl(img.get(i));
            datalist.add(bo);
        }
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FullView.this,MyCart.class));
            }
        });

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        pointerAdapter = new PointerAdapter(FullView.this, R.layout.image_item, datalist, 0);
        horizontalListView.setAdapter(pointerAdapter);
        datalist.get(0).setImgSel(true);

        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewPager.setCurrentItem(i);
                horizontalListView.computeScroll();
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < datalist.size(); i++)
                    datalist.get(i).setImgSel(false);
                datalist.get(arg0).setImgSel(true);
                pointerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class PagerAdapter extends FragmentPagerAdapter {


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return img.size();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            fragment = (Fragment) Imagefragment.newInstance(img.get(position));
            return fragment;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
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
                Log.d("Cart_url_list", Appconstatants.sessiondata + "");
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,FullView.this);
                logger.info("Cart resp"+response);
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
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONArray) {
                            cart_count.setText(0 + "");


                        } else if (dd instanceof JSONObject) {

                            JSONObject jsonObject = new JSONObject(json.getString("data"));
                            JSONArray array = new JSONArray(jsonObject.getString("products"));
                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_count.setText(qty + "");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }
            }

        }
    }

}
