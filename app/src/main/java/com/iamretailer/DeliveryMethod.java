package com.iamretailer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.PaymentAdapter;
import com.iamretailer.Common.CommonFunctions;
import com.logentries.android.AndroidLogger;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.TypePO;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class DeliveryMethod extends Language {
    LinearLayout next;
    private DBController db;
    private EditText comments;
    private FrameLayout error_network;
    private LinearLayout retry;
    private ArrayList<TypePO> methodlist;
    private String fname;
    private String lname;
    private String company;
    private String addressone;
    private String addresstwo;
    private String city;
    private String pincode;
    private String country;
    private String state;
    private String paymethod;
    private String mobile;
    LinearLayout menu,cart_items;
    TextView header;
    Typeface typeface;
    FrameLayout loading;
    FrameLayout fullayout;

    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    String address_id;
    AndroidLogger logger;
    private int from=0;
    private ListView payment_list;
    private PaymentAdapter payadapter;
    private String paycode="";
    private String paymentname="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_method);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        fname = getIntent().getExtras().getString("fname");
        from = getIntent().getExtras().getInt("from");
        lname = getIntent().getExtras().getString("lname");
        company = getIntent().getExtras().getString("company");
        addressone = getIntent().getExtras().getString("addressone");
        addresstwo = getIntent().getExtras().getString("addresstwo");
        city = getIntent().getExtras().getString("city");
        pincode = getIntent().getExtras().getString("pincode");
        country = getIntent().getExtras().getString("country");
        state = getIntent().getExtras().getString("state");
        //paymethod=getIntent().getExtras().getString("payment_method");
        mobile=getIntent().getExtras().getString("mobile");
        address_id=getIntent().getExtras().getString("address_id");
        next=(LinearLayout) findViewById(R.id.next);
        db = new DBController(DeliveryMethod.this);
        Appconstatants.sessiondata=db.getSession();
        Appconstatants.Lang=db.get_lang_code();
        Appconstatants.CUR=db.getCurCode();
        comments = (EditText)findViewById(R.id.comments);
        error_network = (FrameLayout) findViewById(R.id.error_network);
        retry =(LinearLayout)findViewById(R.id.retry);
        payment_list = (ListView) findViewById(R.id.payment_list);
        menu=(LinearLayout)findViewById(R.id.menu);
        cart_items=(LinearLayout)findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        header=(TextView)findViewById(R.id.header);
        loading=(FrameLayout)findViewById(R.id.loading);
        fullayout=(FrameLayout)findViewById(R.id.fullayout);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        typeface=Typeface.createFromAsset(getAssets(),"font/Heebo-Regular.ttf");



        DeliveryMethodList paymentListTask=new DeliveryMethodList();
        paymentListTask.execute();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        header.setText(R.string.delivery);

        payment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                payadapter.setChild1(arg2);
                payadapter.notifyDataSetChanged();

                //if(paylist.get(arg2).getPaymentname().equalsIgnoreCase("Check / Money order")){
                paycode = methodlist.get(arg2).getCode();
                paymentname = methodlist.get(arg2).getTitle();
                Log.i("asdsadasasdasd sadsadas",methodlist.get(arg2).getTitle()+"");

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SaveDeliveryMethod paymentMethodTask = new SaveDeliveryMethod();
                paymentMethodTask.execute(paycode,1+"","");

            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                error_network.setVisibility(View.GONE);
                DeliveryMethodList paymentListTask = new DeliveryMethodList();
                paymentListTask.execute();
            }
        });

    }
    private class DeliveryMethodList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute()
        {

        }

        protected String doInBackground(String... param) {
            logger.info("Delivery_list api"+Appconstatants.DELIVERYMETHOD_LIST);

            String response = null;
            Connection connection = new Connection();
            try {
                Log.d("Delivery_list",Appconstatants.DELIVERYMETHOD_LIST);
                Log.d("Delivery_list",Appconstatants.sessiondata);
                response = connection.connStringResponse(Appconstatants.DELIVERYMETHOD_LIST,Appconstatants.sessiondata,Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,DeliveryMethod.this);
                logger.info("Delivery_list api resp"+response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Delivery_list", "Delivery_list--"+resp);
            if (resp != null) {
                methodlist =new ArrayList<TypePO>();
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1)
                    {
                        JSONObject jsonObject = new JSONObject(json.getString("data"));
                        JSONArray object=jsonObject.getJSONArray("shipping_methods");
                        for(int i = 0; i<object.length(); i++){
                            JSONObject jsonObject1 =object.getJSONObject(i);
                            JSONArray jsonObject2 = jsonObject1.getJSONArray("quote");
                            for (int h=0;h<jsonObject2.length();h++) {
                                JSONObject jsonObject3=jsonObject2.getJSONObject(h);
                                TypePO bo = new TypePO();
                                bo.setCode(jsonObject3.isNull("code") ? "" : jsonObject3.getString("code"));
                                bo.setTitle(jsonObject3.isNull("title") ? "" : jsonObject3.getString("title"));
                                bo.setAmount(jsonObject3.isNull("text") ? "" : jsonObject3.getString("text"));
                                methodlist.add(bo);
                            }
                            Log.i("jsonObject3", jsonObject2.toString()+"");
                        }
                        payadapter = new PaymentAdapter(
                                getApplicationContext(), R.layout.payment_item, methodlist,1);
                        payment_list.setAdapter(payadapter);



                        if (methodlist != null) {
                            if(methodlist.size()>0){
                                paycode = methodlist.get(0).getCode();
                                paymentname = methodlist.get(0).getTitle();
                                payadapter.setChild1(0);
                                payadapter.notifyDataSetChanged();
                            }
                        }
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);

                    }
                    else
                    {
                        error_network.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(DeliveryMethod.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    error_network.setVisibility(View.GONE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    DeliveryMethodList paymentListTask = new DeliveryMethodList();
                                    paymentListTask.execute();

                                }
                            })
                            .show();

                }

            }
            else
            {
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

        }
    }

    public static JSONObject objectToJSONObject(Object object){
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }


    private class SaveDeliveryMethod extends AsyncTask<String, Void, String> {


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(DeliveryMethod.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("DeliveryMethodTask", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Save Delivery method api"+Appconstatants.DELIVERYMETHOD_LIST);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("shipping_method", param[0]);
                json.put("comment", param[2]);
                Log.d("delivery_format", json.toString());
                response = connection.sendHttpPostjson(Appconstatants.DELIVERYMETHOD_LIST, json, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,DeliveryMethod.this);
                logger.info("Save Delivery method api resp"+response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("DeliveryMethodTask", "DeliveryMethodTask--resp" + resp);
            pDialog.dismiss();
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {


                        Intent i =new Intent(DeliveryMethod.this,PaymentMethod.class);
                        i.putExtra("address_id",address_id);
                        i.putExtra("fname", fname);
                        i.putExtra("from", from);
                        i.putExtra("lname", lname);
                        i.putExtra("company", company);
                        i.putExtra("addressone", addressone);
                        i.putExtra("addresstwo", addresstwo);
                        i.putExtra("city", city);
                        i.putExtra("pincode", pincode);
                        i.putExtra("country", country);
                        i.putExtra("state", state);
                        i.putExtra("shipping",paymentname);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    } else {

                        JSONArray array=json.getJSONArray("error");
                        Toast.makeText(DeliveryMethod.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        next.performClick();

                                    }
                                })
                                .show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    next.performClick();

                                }
                            })
                            .show();
                }
            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                next.performClick();

                            }
                        })
                        .show();
            }
        }
    }
}
