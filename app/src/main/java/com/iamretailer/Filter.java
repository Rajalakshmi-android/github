package com.iamretailer;

import android.app.Activity;
import android.content.Intent;
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

import com.iamretailer.Adapter.FilterMainAdapter;
import com.iamretailer.Adapter.FilterSubAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.RecyclerItemClickListener;
import com.iamretailer.POJO.BrandsPO;
import com.iamretailer.POJO.FilterPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import stutzen.co.network.Connection;

public class Filter extends AppCompatActivity {
    LinearLayout menu,cart_items;
    ImageView back;
    TextView header;
    RecyclerView main_filter,sub_filter;
    AndroidLogger logger;
    private ArrayList<FilterPO> filter_list;
    private ArrayList<FilterPO> filter_sub_list;
    private ArrayList<FilterPO> check_list;
    LinearLayout retry;
    FrameLayout loading,error_network,fullayout;
    TextView errortxt1, errortxt2;
    FilterMainAdapter mainAdapter;
    FilterSubAdapter filterSubAdapter;
    private int main_list_pos=0;
    FrameLayout apply_filter,blur_lay;
    int selected=0,clear_data=0;
    LinearLayout clear;
    int cancel_check=0;
    int cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        menu= findViewById(R.id.menu);
        back= findViewById(R.id.back);
        back.setImageResource(R.mipmap.filter_close);
        cart_items= findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        header= findViewById(R.id.header);
        header.setText(getResources().getString(R.string.fil_bg));
        sub_filter= findViewById(R.id.sub_filter);
        main_filter= findViewById(R.id.main_filter);
        retry= findViewById(R.id.retry);
        loading= findViewById(R.id.loading);
        error_network= findViewById(R.id.error_network);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        fullayout= findViewById(R.id.fullayout);
        apply_filter= findViewById(R.id.apply_filter);
        blur_lay= findViewById(R.id.blur_lay);
        clear= findViewById(R.id.clear);
        Intent intent = getIntent();



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        filter_list=new ArrayList<>();
        if (intent.getExtras()!=null) {
            filter_list= (ArrayList<FilterPO>) intent.getExtras().getSerializable("filter_data");
            cat_id=intent.getIntExtra("cat_id",0);
        }
        if (filter_list!=null&&filter_list.size()>0)
        {
            mainAdapter=new FilterMainAdapter(Filter.this,filter_list);
            main_filter.setLayoutManager(new LinearLayoutManager(Filter.this,LinearLayoutManager.VERTICAL,false));
            filter_list.get(main_list_pos).setSelected(true);
            main_filter.setAdapter(mainAdapter);


            filterSubAdapter=new FilterSubAdapter(Filter.this,filter_list.get(main_list_pos).getFilterPOS());
            sub_filter.setLayoutManager(new LinearLayoutManager(Filter.this,LinearLayoutManager.VERTICAL,false));
            sub_filter.setAdapter(filterSubAdapter);
            loading.setVisibility(View.GONE);
        }
        else
        {
            FilterTask filterTask=new FilterTask();
            filterTask.execute(Appconstatants.Filter_Api+cat_id);
        }

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
                filterSubAdapter=new FilterSubAdapter(Filter.this,filter_list.get(main_list_pos).getFilterPOS());
                sub_filter.setLayoutManager(new LinearLayoutManager(Filter.this,LinearLayoutManager.VERTICAL,false));
                sub_filter.setAdapter(filterSubAdapter);
            }
        }));

        sub_filter.addOnItemTouchListener(new RecyclerItemClickListener(Filter.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (filter_list.get(main_list_pos).getFilterPOS().get(position).isSelected())
                    filter_list.get(main_list_pos).getFilterPOS().get(position).setSelected(false);
                else
                    filter_list.get(main_list_pos).getFilterPOS().get(position).setSelected(true);

                filterSubAdapter.notifyDataSetChanged();
                mainAdapter.notifyDataSetChanged();

                for (int j=0;j<filter_list.size();j++)
                {
                    if (filter_list.get(j).getFilterPOS()!=null&&filter_list.get(j).getFilterPOS().size()>0) {
                        for (int s = 0; s < filter_list.get(j).getFilterPOS().size(); s++) {
                            if (filter_list.get(j).getFilterPOS().get(s).isSelected()) {
                                selected++;
                                clear_data++;
                            }
                        }
                    }

                }
                if (selected==0)
                {
                    blur_lay.setVisibility(View.VISIBLE);
                }
                else
                {
                    blur_lay.setVisibility(View.GONE);
                }

                if (clear_data==0)
                {
                    clear.setVisibility(View.GONE);
                }
                else
                {
                    clear.setVisibility(View.VISIBLE);
                }

                selected=0;
                clear_data=0;
            }
        }));

        apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_list_pos=0;
                for (int u=0;u<filter_list.size();u++)
                {
                    filter_list.get(u).setSelected(false);
                }
                filter_list.get(main_list_pos).setSelected(true);

                Intent intent = new Intent(Filter.this,Allen.class);
                intent.putExtra("filter_array",filter_list);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        blur_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (selected==0)
        {
            blur_lay.setVisibility(View.VISIBLE);
        }
        else
        {
            blur_lay.setVisibility(View.GONE);
        }


        for (int j=0;j<filter_list.size();j++)
        {
            if (filter_list.get(j).getFilterPOS()!=null&&filter_list.get(j).getFilterPOS().size()>0) {
                for (int s = 0; s < filter_list.get(j).getFilterPOS().size(); s++) {
                    if (filter_list.get(j).getFilterPOS().get(s).isSelected()) {
                        clear_data++;
                    }
                }
            }

        }
        if (clear_data==0)
        {
            clear.setVisibility(View.GONE);
        }
        else
        {
            clear.setVisibility(View.VISIBLE);
        }

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_list_pos=0;
                clear_data=0;
                selected=0;
                cancel_check=1;
                loading.setVisibility(View.VISIBLE);
                if (selected==0)
                {
                    blur_lay.setVisibility(View.VISIBLE);
                }
                else
                {
                    blur_lay.setVisibility(View.GONE);
                }
                if (clear_data==0)
                {
                    clear.setVisibility(View.GONE);
                }
                else
                {
                    clear.setVisibility(View.VISIBLE);
                }

                FilterTask filterTask=new FilterTask();
                filterTask.execute(Appconstatants.Filter_Api+cat_id);

            }
        });


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                error_network.setVisibility(View.GONE);
                FilterTask filterTask=new FilterTask();
                filterTask.execute(Appconstatants.Filter_Api+cat_id);


            }
        });


    }


    public void change_bg()
    {
        for (int j=0;j<filter_list.size();j++)
        {
            if (filter_list.get(j).getFilterPOS()!=null&&filter_list.get(j).getFilterPOS().size()>0) {
                for (int s = 0; s < filter_list.get(j).getFilterPOS().size(); s++) {
                    if (filter_list.get(j).getFilterPOS().get(s).isSelected()) {
                        selected++;
                        clear_data++;
                    }
                }
            }

        }
        if (selected==0)
        {
            blur_lay.setVisibility(View.VISIBLE);
        }
        else
        {
            blur_lay.setVisibility(View.GONE);
        }
        if (clear_data==0)
        {
            clear.setVisibility(View.GONE);
        }
        else
        {
            clear.setVisibility(View.VISIBLE);
        }

        selected=0;
        clear_data=0;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Filter.this,Allen.class);
        intent.putExtra("cancel_data",cancel_check);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
        super.onBackPressed();
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
                Log.d("url Api", param[0]+"");
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Filter.this);
                logger.info("Category_level resp"+response);
                Log.d("url response", response+"");
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
                                filterPO1.setSub_id(jsonObject1.isNull("option_value_id")?"":jsonObject1.getString("option_value_id"));
                                filter_sub_list.add(filterPO1);
                            }
                            filterPO.setFilterPOS(filter_sub_list);
                            filter_list.add(filterPO);
                        }


                        Iterator<String> keys = arr.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            System.out.println("Key :" + key);

                            FilterPO filterPO=new FilterPO();
                            filterPO.setFilter_name(key);
                            if (filterPO.getFilter_name().equalsIgnoreCase("brand") || filterPO.getFilter_name().equalsIgnoreCase("price_range"))
                            {
                                if (filterPO.getFilter_name().equalsIgnoreCase("brand"))
                                {
                                    JSONArray array1=arr.getJSONArray("brand");
                                    filter_sub_list=new ArrayList<>();
                                    for (int p=0;p<array1.length();p++)
                                    {
                                        JSONObject jsonObject1=array1.getJSONObject(p);
                                        FilterPO filterPO1=new FilterPO();
                                        filterPO1.setFilter_name(jsonObject1.isNull("name")?"":jsonObject1.getString("name"));
                                        filterPO1.setSub_id(jsonObject1.isNull("manufacturer_id")?"":jsonObject1.getString("manufacturer_id"));
                                        filter_sub_list.add(filterPO1);

                                    }
                                    filterPO.setFilterPOS(filter_sub_list);
                                    if (array1.length()>0)
                                    filter_list.add(filterPO);
                                }

                                if (filterPO.getFilter_name().equalsIgnoreCase("price_range"))
                                {
                                    JSONArray array1=arr.getJSONArray("price_range");
                                        filter_sub_list=new ArrayList<>();
                                        FilterPO filterPO1=new FilterPO();
                                        filterPO1.setFilter_name("price_range");
                                        filterPO1.setSeek_min(Float.parseFloat(String.valueOf(array1.get(0))));
                                        filterPO1.setSeek_max(Float.parseFloat(String.valueOf(array1.get(1))));
                                        filterPO1.setPrice_values(Float.parseFloat(String.valueOf(array1.get(0))));
                                        filterPO1.setPrice_values0(Float.parseFloat(String.valueOf(array1.get(1))));
                                        filter_sub_list.add(filterPO1);


                                    filterPO.setFilterPOS(filter_sub_list);
                                    if (filterPO1.getSeek_min()>0 &&filterPO1.getSeek_max()>filterPO1.getSeek_min())
                                    filter_list.add(filterPO);
                                }

                            }

                         /*   if (key.equalsIgnoreCase("brand")) ;
                            {

                                FilterPO filterPO=new FilterPO();
                                filterPO.setFilter_name(key);
                                filter_list.add(filterPO);
                            }
                            if (key.equalsIgnoreCase("price_range"));
                            {

                                FilterPO filterPO=new FilterPO();
                                filterPO.setFilter_name(key);
                                filter_list.add(filterPO);
                            }*/
                        }

                        check_list=new ArrayList<>();
                        check_list=filter_list;

                        if (filter_list!=null&&filter_list.size()>0) {

                            mainAdapter = new FilterMainAdapter(Filter.this, filter_list);
                            main_filter.setLayoutManager(new LinearLayoutManager(Filter.this, LinearLayoutManager.VERTICAL, false));
                            filter_list.get(main_list_pos).setSelected(true);
                            main_filter.setAdapter(mainAdapter);


                            filterSubAdapter = new FilterSubAdapter(Filter.this, filter_list.get(main_list_pos).getFilterPOS());
                            sub_filter.setLayoutManager(new LinearLayoutManager(Filter.this, LinearLayoutManager.VERTICAL, false));
                            sub_filter.setAdapter(filterSubAdapter);
                        }else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_filter),Toast.LENGTH_LONG).show();
                        }





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
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    FilterTask filterTask=new FilterTask();
                                    filterTask.execute(Appconstatants.Filter_Api+cat_id);

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
