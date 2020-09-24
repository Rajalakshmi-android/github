package com.iamretailer;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.Validation;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.POJO.SingleOptionPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;


public class Return_Acticity extends Language {
    private FrameLayout loading;
    private TextView prod_name;
    private TextView tot_qty;
    private EditText qty;
    private EditText comments;
    private RadioGroup res_radio;
    private RadioGroup open_radio;
    private FrameLayout error_lay;
    private FrameLayout lay;
    private ImageView cart;
    FrameLayout update;
    private TextView errortxt1;
    private TextView errortxt2;
    private LinearLayout loading_bar;
    private DBController dbcon;
    private String cus_id;
    private AndroidLogger logger;
    private String quantity="";
    private String prod_names="";
    private String prod_model="";
    private String fname="";
    private String lname="";
    private String mob="";
    private String email="";
    private String order_date="";
    private String order_id="";
    private int open=0;
    private int reason_id=0;
    private ArrayList<ProductsPO> reason_bos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        dbcon = new DBController(Return_Acticity.this);
        Appconstatants.sessiondata=dbcon.getSession();
        Appconstatants.Lang=dbcon.get_lang_code();
        Appconstatants.CUR=dbcon.getCurCode();
        LinearLayout back = findViewById(R.id.menu);
        TextView header = findViewById(R.id.header);
        header.setText(R.string.return_fom);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        prod_name = findViewById(R.id.prod_name);
        qty = findViewById(R.id.qty);
        comments = findViewById(R.id.comments);
        res_radio = findViewById(R.id.radio);
        RadioGroup open_radio = findViewById(R.id.radiogroup);
        tot_qty = findViewById(R.id.tot_qty);
        lay = findViewById(R.id.fullayout);
        loading = findViewById(R.id.loading);
        error_lay = findViewById(R.id.error_network);
        TextView cart_count = findViewById(R.id.cart_count);
        cart_count.setVisibility(View.GONE);
        cart_items.setVisibility(View.GONE);
        if (getIntent().getExtras() != null) {
            quantity = getIntent().getExtras().getString("qty");
            prod_names = getIntent().getExtras().getString("prod_name");
            order_date = getIntent().getExtras().getString("order_date");
            order_id = getIntent().getExtras().getString("order_id");
            prod_model = getIntent().getExtras().getString("prod_model");
        }
        Log.i("Details",quantity+"---"+prod_names+"----"+prod_model+"----"+order_id+"---"+order_date);
        tot_qty.setText(quantity);
        prod_name.setText(prod_names);
        cart = findViewById(R.id.cart);
        update = findViewById(R.id.update);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        loading_bar = findViewById(R.id.loading_bar);
        LinearLayout retry = findViewById(R.id.retry);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(dbcon.getLoginCount()>0){

            String str=dbcon.getName();
            String[] arr = str.split(" ");
            fname=arr[0];
            lname=arr[1];
            Log.i("user name", str+"---"+fname+"---"+lname);
            email=dbcon.getEmail();
            mob=dbcon.getphone();
        }

        //order_date=CommonFunctions.convert_dates(order_date);


        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(s.toString().length()>0){

                        if (Integer.parseInt(s.toString()) > Integer.parseInt(quantity)) {
                            qty.setText("");
                            Toast.makeText(Return_Acticity.this, R.string.qty_toast, Toast.LENGTH_SHORT).show();

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        GET_REASON REASON = new GET_REASON();
        REASON.execute(Appconstatants.REASON);



        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_lay.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                GET_REASON REASON = new GET_REASON();
                REASON.execute(Appconstatants.REASON);
            }
        });





        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("fjdfgldgdg",qty.getText().toString().length()+"");
                if(qty.getText().toString().length()==0){
                    qty.setError(getResources().getString(R.string.qty_error));
                }
                if(qty.getText().toString().length()>0){
                    Log.i("ahadhajda",qty.getText().toString()+"");
                    if(Integer.parseInt(qty.getText().toString())<=0){
                        qty.setError(getResources().getString(R.string.qty_e));
                    }
                }
                if(reason_id==0){
                    Toast.makeText(Return_Acticity.this, R.string.qty_toast, Toast.LENGTH_SHORT).show();
                }

                if(qty.getText().toString().length()!=0&& Integer.parseInt(qty.getText().toString())>0&&reason_id!=0){

                    SAVE_RETURN update_REASON = new SAVE_RETURN();
                    update_REASON.execute(Appconstatants.RETURN_SAVE, fname, lname, email, mob,
                            order_date,order_id,prod_names,
                            prod_model,reason_id+"", open+""
                            ,qty.getText().toString(),comments.getText().toString());



                }


                   
            }
        });
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String[] value={"Yes","No"};
        Log.i("sdsadad",value.length +"");
        for (int u = 0; u < value.length; u++) {
            RadioButton rbn = new RadioButton(Return_Acticity.this);
            String values="  " + value[u];
            rbn.setId(u);
            rbn.setText(values);
            rbn.setTextColor(getResources().getColor(R.color.app_text_color));
            Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.raidobuttonstyle);
            drawable.setBounds(0, 0, 30, 30);
            rbn.setCompoundDrawablesRelative(drawable, null, null, null);
            rbn.setButtonDrawable(null);
            rbn.setBackgroundColor(Color.TRANSPARENT);
            rbn.setPaddingRelative(0,(int) getApplicationContext().getResources().getDimension(R.dimen.dp10),(int) getApplicationContext().getResources().getDimension(R.dimen.dp20),(int) getApplicationContext().getResources().getDimension(R.dimen.dp10));

            if (u==0) {
                rbn.setChecked(true);
                open=1;
            }
            else {
                rbn.setChecked(false);

            }
            rbn.setLayoutParams(params);
            rbn.setTag(u);
            open_radio.addView(rbn);
        }

        open_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                int mySelectedIndex = (int) radioButton.getTag();
                Log.i("radio_button",mySelectedIndex+"");
                if(mySelectedIndex==0){
                    open=1;

                }else{
                  open=0;

                }


            }
        });
        res_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                reason_id= res_radio.getCheckedRadioButtonId();;

            }
        });
    }

    private class GET_REASON extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("REASON api"+param[0]);

            Log.d("url_", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Return_Acticity.this);
                logger.info("REASON api res"+response);
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
                    JSONObject object = new JSONObject(resp);
                    if (object.getInt("success") == 1)
                    {


                        JSONArray jsonArray = object.getJSONArray("data");
                        reason_bos = new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            ProductsPO bo =new ProductsPO();
                            bo.setProductid(object1.isNull("return_reason_id") ? 0: object1.getInt("return_reason_id"));
                            bo.setProduct_name(object1.isNull("name") ? "" : object1.getString("name"));
                            reason_bos.add(bo);
                        }

                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        Log.i("sdsadad",reason_bos.size() +"");
                        for (int u = 0; u < reason_bos.size(); u++) {
                            RadioButton rbn = new RadioButton(Return_Acticity.this);
                            String values="  " +reason_bos.get(u).getProduct_name();
                            rbn.setId(reason_bos.get(u).getProductid());
                            rbn.setText(values);
                            rbn.setTextColor(getResources().getColor(R.color.app_text_color));
                            Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.raidobuttonstyle);
                            drawable.setBounds(0, 0, 30, 30);
                            rbn.setCompoundDrawablesRelative(drawable, null, null, null);
                            rbn.setButtonDrawable(null);
                            rbn.setBackgroundColor(Color.TRANSPARENT);
                           rbn.setPaddingRelative   (0,(int) getApplicationContext().getResources().getDimension(R.dimen.dp10),(int) getApplicationContext().getResources().getDimension(R.dimen.dp20),(int) getApplicationContext().getResources().getDimension(R.dimen.dp10));

                            if (u==0) {
                                rbn.setChecked(true);
                                reason_id=reason_bos.get(u).getProductid();

                            }
                            else {
                                rbn.setChecked(false);

                            }
                            rbn.setLayoutParams(params);
                            rbn.setTag(u);
                            res_radio.addView(rbn);
                        }

                        loading.setVisibility(View.GONE);
                        error_lay.setVisibility(View.GONE);
                        cart.setVisibility(View.VISIBLE);


                    } else {
                        JSONArray array = object.getJSONArray("error");
                        loading.setVisibility(View.GONE);
                        cart.setVisibility(View.GONE);
                        error_lay.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        String error=array.getString(0) + "";
                        errortxt2.setText(error);
                        Toast.makeText(Return_Acticity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    error_lay.setVisibility(View.GONE);
                    loading_bar.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    cart.setVisibility(View.GONE);

                    Snackbar.make(lay, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    GET_REASON REASON = new GET_REASON();
                                    REASON.execute(Appconstatants.REASON);

                                }
                            })
                            .show();
                }

            } else {
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_lay.setVisibility(View.VISIBLE);
                cart.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
            }
        }
    }


    private class SAVE_RETURN extends AsyncTask<String, Void, String> {


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Return_Acticity.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... param) {
            logger.info("Update api:"+param[0]);

            String response = null;
            try {
                JSONObject object = new JSONObject();
                object.put("firstname", param[1]);
                object.put("lastname", param[2]);
                object.put("email", param[3]);
                object.put("telephone", param[4]);
                object.put("date_ordered", param[5]);
                object.put("order_id", param[6]);
                object.put("product", param[7]);
                object.put("model", param[8]);
                object.put("return_reason_id", param[9]);
                object.put("opened", param[10]);
                object.put("quantity", param[11]);
                object.put("comment", param[12]);
                Log.d("Update_in", object.toString() + "-- "+Appconstatants.Lang+" -- "+Appconstatants.CUR+" -- "+Appconstatants.sessiondata);
                Connection connection = new Connection();
                response = connection.sendHttpPostjson(param[0], object, Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Return_Acticity.this);
                logger.info("Update api resp:"+response);
                Log.d("Update_url", response + "");


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            Log.i("tag", "save_input" + param[0]);
            return response;
        }

        protected void onPostExecute(String resp) {

            pDialog.dismiss();
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Log.d("Update_url", json.toString() + "");
                        Toast.makeText(Return_Acticity.this, R.string.return_save, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Return_Acticity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(lay, R.string.error_msg, Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    update.performClick();
                                }
                            })
                            .show();

                }

            } else {
                Snackbar.make(lay, R.string.error_net, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                update.performClick();

                            }
                        })
                        .show();

            }

        }
    }

}
