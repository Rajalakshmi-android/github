package com.iamretailer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.ReturnlistAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.Validation;
import com.iamretailer.POJO.BrandsPO;
import com.iamretailer.POJO.OrdersPO;
import com.iamretailer.POJO.PlacePO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import stutzen.co.network.Connection;


public class ContactForm extends Language {

    private EditText full_name;
    private EditText mailid;
    private EditText comments;
    private FrameLayout submit;
    private FrameLayout fullayout;
    private AndroidLogger logger;
    private DBController dbController;
    private ArrayList<OrdersPO> list;
    private FrameLayout loading;
    private LinearLayout store_list;
    private FrameLayout error_network;
    private ArrayList<OrdersPO> list3;
    private TextView poss,store_name,store_address,store_mob,store_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        dbController = new DBController(ContactForm.this);
        Appconstatants.sessiondata = dbController.getSession();
        Appconstatants.Lang = dbController.get_lang_code();
        Appconstatants.CUR = dbController.getCurCode();
        LinearLayout menu = findViewById(R.id.menu);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        TextView header = findViewById(R.id.header);
        full_name = findViewById(R.id.full_name);
        mailid = findViewById(R.id.mailid);
        comments = findViewById(R.id.comments);
        submit = findViewById(R.id.submit);
        fullayout = findViewById(R.id.fullayout);
        loading = findViewById(R.id.loading);
        store_list = findViewById(R.id.store_list);

        header.setText(R.string.contactus);
        cart_items.setVisibility(View.GONE);
        Storelist orderTask = new Storelist();
        orderTask.execute(Appconstatants.Store_list, Appconstatants.sessiondata);

        if (dbController.getLoginCount() > 0) {
            mailid.setText(dbController.getEmail());
            full_name.setText(dbController.getName());
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (full_name.getText().toString().trim().isEmpty() || full_name.getText().toString().trim().length() <= 2 || !Validation.validateName(full_name.getText().toString().trim())) {
                    full_name.setError(getResources().getString(R.string.valid_name));
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mailid.getText().toString().trim()).matches() || mailid.getText().toString().trim().isEmpty()) {
                    mailid.setError(getResources().getString(R.string.valid_mail));
                }
                if (comments.getText().toString().isEmpty()) {
                    comments.setError(getResources().getString(R.string.comt_err));
                }
                if (!full_name.getText().toString().trim().isEmpty() && full_name.getText().toString().trim().length() > 2 && Validation.validateName(full_name.getText().toString().trim())
                        && Patterns.EMAIL_ADDRESS.matcher(mailid.getText().toString().trim()).matches() && !mailid.getText().toString().trim().isEmpty() &&
                        !comments.getText().toString().isEmpty()) {
                    ContactTASK contactTASK = new ContactTASK();
                    contactTASK.execute(full_name.getText().toString().trim(), mailid.getText().toString().trim(), comments.getText().toString().trim());

                }
            }
        });


    }

    private class ContactTASK extends AsyncTask<Object, Void, String> {


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ContactForm.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("Contact_form", "started");
        }

        protected String doInBackground(Object... param) {

            logger.info("Contact_form api" + Appconstatants.CONTACT_API);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("name", param[0]);
                json.put("email", param[1]);
                json.put("enquiry", param[2]);

                Log.d("Input_format", json.toString());
                Log.d("Input_format", Appconstatants.CONTACT_API);
                Log.d("Input_format", Appconstatants.sessiondata);
                logger.info("Contact_form api req" + Appconstatants.CONTACT_API);
                response = connection.sendHttpPostjson(Appconstatants.CONTACT_API, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.APP_DOMAIN_KEY, Appconstatants.Lang, Appconstatants.CUR, ContactForm.this);
                logger.info("Contact_form api resp" + response);
                Log.d("Input_format", response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            if (pDialog != null)
                pDialog.dismiss();
            Log.i("Contact_form", "Contact_form--->  " + resp);
            if (resp != null) {

                try {
                    JSONObject json1 = new JSONObject(resp);
                    if (json1.getInt("success") == 1) {
                        showCallPopup();
                        if (dbController.getLoginCount() > 0) {
                            mailid.setText(dbController.getEmail());
                            full_name.setText(dbController.getName());
                        }
                        comments.setText("");

                    } else if (json1.getInt("success") == 0) {

                        JSONArray array = json1.getJSONArray("error");
                        Toast.makeText(ContactForm.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    } else {
                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        submit.performClick();

                                    }
                                })
                                .show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    submit.performClick();
                                }
                            })
                            .show();
                }
            } else {

                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                submit.performClick();
                            }
                        })
                        .show();

            }

        }
    }

    private void showCallPopup() {
        final AlertDialog.Builder dial = new AlertDialog.Builder(ContactForm.this);
        View popUpView = getLayoutInflater().inflate(R.layout.contact_sucess, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();
        popupStore.setCancelable(false);
        LinearLayout okay = popUpView.findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupStore.dismiss();
                startActivity(new Intent(ContactForm.this, MainActivity.class));
            }
        });


    }

    private class Storelist extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Order", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Order list api" + Appconstatants.Return_List);
            Log.d("Order_url", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.APP_DOMAIN_KEY, Appconstatants.Lang, Appconstatants.CUR, ContactForm.this);
                logger.info("Order list api resp" + response);
                Log.d("Order_resp", response);
                Log.d("url", Appconstatants.Lang);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Myorder", "order_Resp--->" + resp);

            if (resp != null) {

                try {
                    list = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {

                            JSONArray arr = new JSONArray(json.getString("data"));

                            if (arr.length() == 0) {
                                
                                loading.setVisibility(View.GONE);
                            } else {

                                for (int h = 0; h < arr.length(); h++) {
                                    JSONObject obj = arr.getJSONObject(h);
                                    OrdersPO bo = new OrdersPO();
                                    bo.setOrder_id(obj.isNull("store_id") ? "" : obj.getString("store_id"));
                                    list.add(bo);
                                }

                            }

                        store_list.removeAllViews();
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                addLayoutss(list.get(i), store_list,i);
                              
                                }

                        }


                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(ContactForm.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                           loading.setVisibility(View.VISIBLE);
                                        Storelist orderTask = new Storelist();
                                        orderTask.execute(Appconstatants.Store_list, Appconstatants.sessiondata);

                                    }
                                })
                                .show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    Storelist orderTask = new Storelist();
                                    orderTask.execute(Appconstatants.Store_list, Appconstatants.sessiondata);

                                }
                            })
                            .show();


                }

            } else {
                loading.setVisibility(View.GONE);
                Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                  loading.setVisibility(View.VISIBLE);
                                Storelist orderTask = new Storelist();
                                orderTask.execute(Appconstatants.Store_list, Appconstatants.sessiondata);

                            }
                        })
                        .show();

            }
        }
    }

    private void addLayoutss(final OrdersPO ordersPO, LinearLayout store_list, int i) {
        View convertView = LayoutInflater.from(this).inflate(R.layout.store_list, store_list, false);
        store_name = convertView.findViewById(R.id.store_name);
        store_address = convertView.findViewById(R.id.store_address);
        store_mob = convertView.findViewById(R.id.store_mob);
        store_email = convertView.findViewById(R.id.store_email);

        OrderTask task = new OrderTask();
        task.execute(Appconstatants.Store_Detail +ordersPO.getOrder_id());
        
        store_list.addView(convertView);
    }

   

    private class OrderTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("View_Orders", "started");
        }
        protected String doInBackground(String... param) {


            logger.info("View_Order_ api"+param[0]);
            Log.d("View_Order_rl", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.APP_DOMAIN_KEY,Appconstatants.Lang, Appconstatants.CUR,ContactForm.this);
                logger.info("View_Order_api resp"+response);
                Log.d("View_Order_r", response);
                Log.d("View_Order_r", Appconstatants.sessiondata);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            if (resp != null) {

                try {
                    list3 = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success")==1)
                    {
                        JSONObject object = new JSONObject(json.getString("data"));
                        Log.i("reeee",object.toString());

                        store_name.setText(object.isNull("store_name") ? "" : object.getString("store_name"));
                        store_address.setText(object.isNull("store_address") ? "" : object.getString("store_address"));
                        store_mob.setText(object.isNull("store_telephone") ? "" : object.getString("store_telephone"));
                        store_email.setText(object.isNull("store_email") ? "" : object.getString("store_email"));
                       

                    }
                    else
                    {

                        JSONArray array=json.getJSONArray("error");
                        String error=array.getString(0)+"";
                        Toast.makeText(ContactForm.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }
                    loading.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    Storelist orderTask = new Storelist();
                                    orderTask.execute(Appconstatants.Store_list, Appconstatants.sessiondata);

                                }
                            })
                            .show();

                }

            } else {
                loading.setVisibility(View.GONE);
                Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loading.setVisibility(View.VISIBLE);
                                Storelist orderTask = new Storelist();
                                orderTask.execute(Appconstatants.Store_list, Appconstatants.sessiondata);

                            }
                        })
                        .show();

            }
        }
    }
}
