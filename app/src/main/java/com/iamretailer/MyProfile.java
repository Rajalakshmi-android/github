package com.iamretailer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.CommonFunctions;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.iamretailer.Common.Appconstatants;

import com.iamretailer.Common.DBController;
import com.iamretailer.Common.Validation;

import stutzen.co.network.Connection;


public class MyProfile extends Language {
    LinearLayout back;
    TextView header;
    LinearLayout cart_items;
    FrameLayout loading;
    FrameLayout error_lay;
    EditText f_name, l_name, email, mobile;
    FrameLayout lay;
    TextView cart_count;
    ImageView cart;
    FrameLayout update;
    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    LinearLayout retry;
    DBController dbcon;
    String cus_id;
    AndroidLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        dbcon = new DBController(MyProfile.this);
        Appconstatants.sessiondata=dbcon.getSession();
        Appconstatants.Lang=dbcon.get_lang_code();
        Appconstatants.CUR=dbcon.getCurCode();
        back = (LinearLayout) findViewById(R.id.menu);
        header = (TextView) findViewById(R.id.header);
        header.setText(R.string.my_Acc);
        cart_items = (LinearLayout) findViewById(R.id.cart_items);
        f_name = (EditText) findViewById(R.id.f_name);
        l_name = (EditText) findViewById(R.id.l_name);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        lay = (FrameLayout) findViewById(R.id.fullayout);
        loading = (FrameLayout) findViewById(R.id.loading);
        error_lay = (FrameLayout) findViewById(R.id.error_network);
        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_count.setVisibility(View.GONE);
        cart = (ImageView) findViewById(R.id.cart);
        cart.setImageResource(R.mipmap.edi_pencil);
        update = (FrameLayout) findViewById(R.id.update);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        loading_bar = (LinearLayout) findViewById(R.id.loading_bar);
        retry = (LinearLayout) findViewById(R.id.retry);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GET_PROFILE profile = new GET_PROFILE();
        profile.execute(Appconstatants.MY_PROFILE);


        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f_name.setEnabled(true);
                l_name.setEnabled(true);
                mobile.setEnabled(true);
                email.setEnabled(true);
                cart.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);

            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_lay.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                GET_PROFILE profile = new GET_PROFILE();
                profile.execute(Appconstatants.MY_PROFILE);
            }
        });




        f_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if( f_name.getText().length()>0)
                {
                    f_name.setError(null);
                }

            }
        });
        l_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if( l_name.getText().length()>0)
                {
                    l_name.setError(null);
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (f_name.getText().toString().trim().length() == 0) {
                    f_name.setError(getResources().getString(R.string.f_na));
                }
                if (!Validation.validateName(f_name.getText().toString().trim())) {
                    f_name.setError(getResources().getString(R.string.valid_name));
                }
                if (f_name.getText().toString().trim().length() <= 2) {
                    f_name.setError(getResources().getString(R.string.valid_name));
                }
                if (l_name.getText().toString().trim().length() == 0) {
                    l_name.setError(getResources().getString(R.string.l_na));
                }


                if (email.getText().toString().trim().length() == 0) {
                    email.setError(getResources().getString(R.string.mail_id));
                }
                if (!Validation.isValidMail(email.getText().toString())) {
                    email.setError(getResources().getString(R.string.valid_mail));
                }
                if (mobile.getText().toString().trim().length() == 0) {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }
                if (mobile.getText().toString().trim().length() != 10) {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }

                if (!Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()) {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }


                if (!f_name.getText().toString().isEmpty() && f_name.getText().toString().trim().length() > 2 && Validation.validateName(l_name.getText().toString().trim())
                        && !l_name.getText().toString().isEmpty() && Validation.validateName(l_name.getText().toString().trim())
                        && !email.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()
                        && mobile.getText().toString().length() == 10 && !mobile.getText().toString().isEmpty() && Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()
                        ) {
                    UPDATE_PROFILE update_profile = new UPDATE_PROFILE();
                    update_profile.execute(Appconstatants.MY_PROFILE, f_name.getText().toString().trim(), l_name.getText().toString().trim(), email.getText().toString().trim(), mobile.getText().toString().trim());
                }

            }
        });


    }

    private class GET_PROFILE extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Profile api"+param[0]);

            Log.d("url_", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyProfile.this);
                logger.info("Profile api res"+response);
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

                        JSONObject object1 = object.getJSONObject("data");
                        cus_id = object1.isNull("customer_id") ? "" : object1.getString("customer_id");
                        f_name.setText(object1.isNull("firstname") ? "" : object1.getString("firstname"));
                        l_name.setText(object1.isNull("lastname") ? "" : object1.getString("lastname"));
                        email.setText(object1.isNull("email") ? "" : object1.getString("email"));
                        mobile.setText(object1.isNull("telephone") ? "" : object1.getString("telephone"));
                        f_name.setEnabled(false);
                        l_name.setEnabled(false);
                        email.setEnabled(false);
                        mobile.setEnabled(false);
                        loading.setVisibility(View.GONE);
                        error_lay.setVisibility(View.GONE);
                        cart.setVisibility(View.VISIBLE);


                    } else {
                        JSONArray array = object.getJSONArray("error");
                        loading.setVisibility(View.GONE);
                        cart.setVisibility(View.GONE);
                        error_lay.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        errortxt2.setText(array.getString(0) + "");
                        Toast.makeText(MyProfile.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
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
                                    GET_PROFILE profile = new GET_PROFILE();
                                    profile.execute(Appconstatants.MY_PROFILE);

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


    private class UPDATE_PROFILE extends AsyncTask<String, Void, String> {


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MyProfile.this);
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
                Log.d("Update_in", object.toString() + "");
                Connection connection = new Connection();
                response = connection.sendHttpPutjson1(param[0], object, Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyProfile.this);
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
                        Toast.makeText(MyProfile.this, R.string.user, Toast.LENGTH_LONG).show();
                        GET_PROFILE profile = new GET_PROFILE();
                        profile.execute(Appconstatants.MY_PROFILE);
                        dbcon.user_data(cus_id, f_name.getText().toString() +" "+ l_name.getText().toString(), email.getText().toString(), mobile.getText().toString());
                        cart.setVisibility(View.VISIBLE);
                        update.setVisibility(View.GONE);

                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(MyProfile.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
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
