package com.iamretailer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.CommonFunctions;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.iamretailer.Adapter.ReviewAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.SingleOptionPO;

import stutzen.co.network.Connection;


public class Review extends Language {
    private ArrayList<SingleOptionPO> rev_list;
    private ListView review_list;
    private ReviewAdapter reviewAdapter;
    private FrameLayout loading;
    private FrameLayout fullayout;
    private FrameLayout error_network;
    String prod_id;
    private boolean scrollValue;
    private boolean scrollNeed = true;
    private int start = 0;
    private int limit = 20;
    private int val = 0;
    private LinearLayout load_more;
    private TextView errortxt1;
    private TextView errortxt2;
    private AndroidLogger logger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        LinearLayout back = findViewById(R.id.menu);
        TextView header = findViewById(R.id.header);
        header.setText(R.string.review_head);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        review_list = findViewById(R.id.review_list);
        loading = findViewById(R.id.loading);
        fullayout = findViewById(R.id.fullayout);
        error_network = findViewById(R.id.error_network);
        LinearLayout retry = findViewById(R.id.retry);
        load_more = findViewById(R.id.load_more);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        DBController db = new DBController(Review.this);
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang = db.get_lang_code();
        Appconstatants.CUR = db.getCurCode();

        Bundle mm = getIntent().getExtras();
        if (mm != null)
            prod_id = mm.getString("id");
        ReviewTASK reviewTASK = new ReviewTASK();
        reviewTASK.execute(Appconstatants.Review_LIST + prod_id + "&start=" + start + "&limit=" + limit);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                error_network.setVisibility(View.GONE);
                val = 0;
                start = 0;
                limit = 20;

                ReviewTASK reviewTASK = new ReviewTASK();
                reviewTASK.execute(Appconstatants.Review_LIST + prod_id + "&start=" + start + "&limit=" + limit);

            }
        });
        review_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollValue && scrollNeed && rev_list.size() >= start) {
                    val = 1;
                    scrollNeed = false;
                    load_more.setVisibility(View.VISIBLE);
                    ReviewTASK reviewTASK = new ReviewTASK();
                    reviewTASK.execute(Appconstatants.Review_LIST + prod_id + "&start=" + start + "&limit=" + limit);

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int count = firstVisibleItem + visibleItemCount;
                scrollValue = totalItemCount == count;
            }
        });


    }

    private class ReviewTASK extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Order", "started");
        }


        protected String doInBackground(String... param) {
            logger.info("Review api" + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.APP_DOMAIN_KEY, Appconstatants.Lang, Appconstatants.CUR, Review.this);
                logger.info("Review resp" + response);
                Log.d("Review_resp", response);
                Log.d("Review_resp", param[0]);
                Log.d("review_ses", Appconstatants.sessiondata);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Review_", "Resp--->" + resp);
            load_more.setVisibility(View.GONE);
            scrollNeed = true;
            if (resp != null) {

                try {
                    if (val == 0) {
                        rev_list = new ArrayList<>();
                    }

                    JSONObject object = new JSONObject(resp);
                    if (object.getInt("success") == 1) {
                        JSONObject object1 = object.getJSONObject("data");

                        if (Integer.parseInt(object1.getString("review_total")) != 0) {
                            JSONArray array = object1.getJSONArray("reviews");

                            for (int u = 0; u < array.length(); u++) {
                                JSONObject obj = array.getJSONObject(u);
                                SingleOptionPO bo = new SingleOptionPO();
                                bo.setRev_author(obj.isNull("author") ? "" : obj.getString("author"));
                                bo.setRev_text(obj.isNull("text") ? "" : obj.getString("text"));
                                bo.setRev_date(obj.isNull("date_added") ? "" : obj.getString("date_added"));
                                bo.setRating(obj.isNull("rating") ? 0 : obj.getInt("rating"));
                                rev_list.add(bo);

                            }
                            if (val == 0) {
                                reviewAdapter = new ReviewAdapter(Review.this, R.layout.review_list, rev_list);
                                review_list.setAdapter(reviewAdapter);
                            } else {
                                reviewAdapter.notifyDataSetChanged();
                            }
                        }
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);

                        start = start + 20;

                    } else {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = object.getJSONArray("error");
                        String error = array.getString(0) + "";
                        errortxt2.setText(error);
                        Toast.makeText(Review.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    error_network.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    ReviewTASK reviewTASK = new ReviewTASK();
                                    reviewTASK.execute(Appconstatants.Review_LIST + prod_id + "&start=" + start + "&limit=" + limit);
                                }
                            })
                            .show();


                }

            } else {
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }
    }


}
