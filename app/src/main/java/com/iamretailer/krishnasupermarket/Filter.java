package com.iamretailer.krishnasupermarket;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.krishnasupermarket.Adapter.FilterMainAdapter;
import com.iamretailer.krishnasupermarket.Adapter.FilterSubAdapter;
import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.RecyclerItemClickListener;
import com.iamretailer.krishnasupermarket.POJO.BrandsPO;
import com.iamretailer.krishnasupermarket.POJO.FilterPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class Filter extends AppCompatActivity {
    LinearLayout menu,cart_items;
    ImageView back;
    TextView header;
    RecyclerView main_filter,sub_filter;
    AndroidLogger logger;
    private ArrayList<FilterPO> filter_list;
    private ArrayList<FilterPO> filter_sub_list;
    LinearLayout loading_bar;
    LinearLayout retry;
    FrameLayout loading,error_network,fullayout;
    TextView errortxt1, errortxt2;
    FilterMainAdapter mainAdapter;
    FilterSubAdapter filterSubAdapter;
    private int main_list_pos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        menu=(LinearLayout)findViewById(R.id.menu);
        back=(ImageView)findViewById(R.id.back);
        back.setImageResource(R.mipmap.filter_close);
        cart_items=(LinearLayout)findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        header=(TextView)findViewById(R.id.header);
        header.setText(getResources().getString(R.string.fil_bg));
        sub_filter=(RecyclerView) findViewById(R.id.sub_filter);
        main_filter=(RecyclerView) findViewById(R.id.main_filter);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        retry=(LinearLayout)findViewById(R.id.retry);
        loading=(FrameLayout)findViewById(R.id.loading);
        error_network=(FrameLayout)findViewById(R.id.error_network);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        fullayout=(FrameLayout)findViewById(R.id.fullayout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final FilterTask filterTask=new FilterTask();
        filterTask.execute(Appconstatants.Filter_Api);
        main_filter.addOnItemTouchListener(new RecyclerItemClickListener(Filter.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                main_list_pos=position;
                for (int u=0;u<filter_list.size();u++)
                {
                    filter_list.get(u).setSelected(false);
                }
                filter_list.get(position).setSelected(true);
                mainAdapter.notifyDataSetChanged();
                filterSubAdapter=new FilterSubAdapter(Filter.this,filter_list.get(position).getFilterPOS());
                sub_filter.setLayoutManager(new LinearLayoutManager(Filter.this,LinearLayoutManager.VERTICAL,false));
                sub_filter.setAdapter(filterSubAdapter);

            }
        }));

        sub_filter.addOnItemTouchListener(new RecyclerItemClickListener(Filter.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                for (int u=0;u<filter_list.get(main_list_pos).getFilterPOS().size();u++)
                {
                   /* if (filter_list.get(main_list_pos).getFilterPOS().get(u).isSelected()) {
                        filter_list.get(main_list_pos).getFilterPOS().get(u).setSelected(false);
                    }
                    else
                    {
                        filter_list.get(main_list_pos).getFilterPOS().get(position).setSelected(true);
                    }*/


                }
                if (filter_list.get(main_list_pos).getFilterPOS().get(position).isSelected())
                 filter_list.get(main_list_pos).getFilterPOS().get(position).setSelected(false);
                else
                    filter_list.get(main_list_pos).getFilterPOS().get(position).setSelected(true);
                filterSubAdapter.notifyDataSetChanged();

            }
        }));

    }


    private class FilterTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }
        protected String doInBackground(String... param) {
            logger.info("Filter api"+param[0]);

            Log.d("url", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Filter.this);
                logger.info("Category_level resp"+response);
                Log.d("url response", response);
                Log.d("url_resp_",response+"");
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
                    filter_list = new ArrayList<>();

                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success")==1)
                    {
                        JSONObject arr = json.getJSONObject("data");

                        JSONArray array=arr.getJSONArray("options");
                        Log.d("adsadsasd",array.length()+"");

                        for (int h=0;h<array.length();h++)
                        {
                            JSONObject jsonObject=array.getJSONObject(h);
                            FilterPO filterPO=new FilterPO();
                            filterPO.setFilter_name(jsonObject.isNull("name")?"":jsonObject.getString("name"));
                            JSONArray array1 =jsonObject.getJSONArray("option_values");
                            filter_sub_list=new ArrayList<>();
                            for (int j=0;j<array1.length();j++)
                            {
                                JSONObject jsonObject1=array1.getJSONObject(j);
                                FilterPO filterPO1=new FilterPO();
                                filterPO1.setFilter_name(jsonObject1.isNull("name")?"":jsonObject1.getString("name"));
                                filter_sub_list.add(filterPO1);
                            }
                            filterPO.setFilterPOS(filter_sub_list);
                            filter_list.add(filterPO);
                        }

                        mainAdapter=new FilterMainAdapter(Filter.this,filter_list);
                        main_filter.setLayoutManager(new LinearLayoutManager(Filter.this,LinearLayoutManager.VERTICAL,false));
                        filter_list.get(main_list_pos).setSelected(true);
                        main_filter.setAdapter(mainAdapter);


                        filterSubAdapter=new FilterSubAdapter(Filter.this,filter_list.get(0).getFilterPOS());
                        sub_filter.setLayoutManager(new LinearLayoutManager(Filter.this,LinearLayoutManager.VERTICAL,false));
                        sub_filter.setAdapter(filterSubAdapter);





                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);
                    }
                    else
                    {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(Filter.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
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
