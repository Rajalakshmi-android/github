package com.iamretailer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.CountryAdapter;
import com.iamretailer.Adapter.StateAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.Validation;
import com.iamretailer.POJO.CountryPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;


public class Address extends Language {
    private EditText f_name;
    private EditText l_name;
    private EditText mobile;
    private EditText company;
    private EditText addressone;
    private EditText addressstwo;
    private EditText city;
    private EditText pincode;
    private FrameLayout cont;
    private Spinner state;
    private Spinner country;
    private FrameLayout error_network;
    private ArrayList<CountryPO> country_list;
    private ArrayList<CountryPO> state_list;
    private int from = 0;
    private FrameLayout loading;
    private ProgressDialog pDialog1;
    private Bundle cc;
    private FrameLayout fullayout;
    private TextView errortxt1;
    private TextView errortxt2;
    private LinearLayout loading_bar;
    private String address_id = "";
    private AndroidLogger logger;
    private StateAdapter s_adapter;
    private int has_ship;
    private int add_ids = 0;
    private LinearLayout success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        CommonFunctions.updateAndroidSecurityProvider(this);
        DBController dbController = new DBController(Address.this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        if (Appconstatants.sessiondata == null || Appconstatants.sessiondata.length() <= 0)
            Appconstatants.sessiondata = dbController.getSession();
        Appconstatants.Lang = dbController.get_lang_code();
        Appconstatants.CUR = dbController.getCurCode();
        cc = new Bundle();
        cc = getIntent().getExtras();
        from = cc != null ? cc.getInt("from") : 0;
        if (cc != null) {
            address_id = cc.getString("address_id");
        }
        Log.d("from_values", from + "");
        has_ship = cc.getInt("has_ship");
        Appconstatants.Lang = dbController.get_lang_code();
        f_name = findViewById(R.id.full_name);
        l_name = findViewById(R.id.username2);
        mobile = findViewById(R.id.mobile);
        company = findViewById(R.id.company);
        addressone = findViewById(R.id.address_one);
        addressstwo = findViewById(R.id.address_two);
        city = findViewById(R.id.city);
        pincode = findViewById(R.id.pin_code);
        cont = findViewById(R.id.cont);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        error_network = findViewById(R.id.error_network);
        LinearLayout retry = findViewById(R.id.retry);
        LinearLayout back = findViewById(R.id.menu);
        TextView header = findViewById(R.id.header);
        success=findViewById(R.id.success);
        header.setText(R.string.delivery_add);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        fullayout = findViewById(R.id.fullayout);
        cart_items.setVisibility(View.GONE);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        loading_bar = findViewById(R.id.loading_bar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loading = findViewById(R.id.loading);


        CountryTask countryTask = new CountryTask();
        countryTask.execute();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                error_network.setVisibility(View.GONE);
                success.setVisibility(View.GONE);
                CountryTask countryTask = new CountryTask();
                countryTask.execute();

            }
        });


        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    StateTask stateTask = new StateTask();
                    stateTask.execute(Appconstatants.country_list_api + "&id=" + country_list.get(i).getCount_id());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                if (f_name.getText().length() > 0) {
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

                if (l_name.getText().length() > 0) {
                    l_name.setError(null);
                }
            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from == 1) {

                    if (f_name.getText().toString().trim().equals("")) {
                        f_name.setError(getResources().getString(R.string.f_na));
                    }
                    if (!Validation.validateName(f_name.getText().toString().trim())) {
                        f_name.setError(getResources().getString(R.string.valid_name));
                    }
                    if (f_name.getText().toString().trim().length() <= 2) {
                        f_name.setError(getResources().getString(R.string.valid_name));
                    }
                    if (l_name.getText().toString().trim().equals("")) {
                        l_name.setError(getResources().getString(R.string.l_na));
                    }
                    if (!Validation.validateName(l_name.getText().toString().trim())) {
                        l_name.setError(getResources().getString(R.string.valid_name));
                    }
                    if (addressone.getText().toString().trim().equals("")) {
                        addressone.setError(getResources().getString(R.string.add_de));
                    }
                    if (addressone.getText().toString().length() <= 3) {
                        addressone.setError(getResources().getString(R.string.add_de_valid));
                    }
                    if (city.getText().toString().trim().equals("")) {
                        city.setError(getResources().getString(R.string.city_error));
                    }
                    if (city.getText().toString().trim().length() <= 2) {
                        city.setError(getResources().getString(R.string.valid_city));
                    }

                    if (!Validation.validateName(city.getText().toString().trim())) {
                        city.setError(getResources().getString(R.string.valid_city));
                    }
                    if (pincode.getText().toString().trim().equals("")) {
                        pincode.setError(getResources().getString(R.string.oin_error));
                    }
                    if (pincode.getText().toString().length() != 6) {
                        pincode.setError(getResources().getString(R.string.pin_error1));
                    }
                    if (mobile.getText().toString().length() != 10) {
                        mobile.setError(getResources().getString(R.string.mobl_error));
                    }

                    if (!Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()) {
                        mobile.setError(getResources().getString(R.string.mobl_error));
                    }
                    if (country.getSelectedItemPosition() == 0) {
                        Toast.makeText(Address.this, R.string.cont_Sele, Toast.LENGTH_SHORT).show();
                    }
                    if (state.getSelectedItemPosition() == 0) {
                        Toast.makeText(Address.this, R.string.stat, Toast.LENGTH_SHORT).show();
                    }

                    if (!f_name.getText().toString().trim().isEmpty() && Validation.validateName(f_name.getText().toString().trim()) && f_name.getText().toString().trim().length() >= 2

                            && !l_name.getText().toString().trim().isEmpty() && Validation.validateName(l_name.getText().toString().trim())
                            && !addressone.getText().toString().trim().isEmpty() && addressone.getText().toString().length() >= 3
                            && !city.getText().toString().trim().isEmpty() && Validation.validateName(city.getText().toString().trim()) && city.getText().toString().trim().length() > 2
                            && !pincode.getText().toString().trim().isEmpty() && pincode.getText().toString().length() == 6
                            && mobile.getText().toString().length() == 10 && !mobile.getText().toString().trim().isEmpty() && Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()
                            && country.getSelectedItemPosition() != 0 && state.getSelectedItemPosition() != 0
                            ) {

                        UPDATE_ADD addressTask = new UPDATE_ADD();
                        addressTask.execute(f_name.getText().toString().trim(), l_name.getText().toString().trim(), city.getText().toString().trim(), addressone.getText().toString().trim(), addressstwo.getText().toString().trim(), country_list.get(country.getSelectedItemPosition()).getCount_id() + "", pincode.getText().toString().trim(), state_list.get(state.getSelectedItemPosition()).getZone_id(), mobile.getText().toString(), company.getText().toString().trim());


                    }
                } else if (from == 2) {

                    if (f_name.getText().toString().trim().equals("")) {
                        f_name.setError(getResources().getString(R.string.f_na));
                    }
                    if (!Validation.validateName(f_name.getText().toString().trim())) {
                        f_name.setError(getResources().getString(R.string.valid_name));
                    }
                    if (f_name.getText().toString().trim().length() <= 2) {
                        f_name.setError(getResources().getString(R.string.valid_name));
                    }
                    if (l_name.getText().toString().trim().equals("")) {
                        l_name.setError(getResources().getString(R.string.l_na));
                    }
                    if (!Validation.validateName(l_name.getText().toString().trim())) {
                        l_name.setError(getResources().getString(R.string.valid_name));
                    }
                    if (addressone.getText().toString().trim().equals("")) {
                        addressone.setError(getResources().getString(R.string.add_de));
                    }
                    if (addressone.getText().toString().length() <= 3) {
                        addressone.setError(getResources().getString(R.string.add_de_valid));
                    }
                    if (city.getText().toString().trim().equals("")) {
                        city.setError(getResources().getString(R.string.city_error));
                    }
                    if (city.getText().toString().trim().length() <= 2) {
                        city.setError(getResources().getString(R.string.valid_city));
                    }

                    if (!Validation.validateName(city.getText().toString().trim())) {
                        city.setError(getResources().getString(R.string.valid_city));
                    }
                    if (pincode.getText().toString().trim().equals("")) {
                        pincode.setError(getResources().getString(R.string.oin_error));
                    }
                    if (pincode.getText().toString().length() != 6) {
                        pincode.setError(getResources().getString(R.string.pin_error1));
                    }
                    if (mobile.getText().toString().length() != 10) {
                        mobile.setError(getResources().getString(R.string.mobl_error));
                    }

                    if (!Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()) {
                        mobile.setError(getResources().getString(R.string.mobl_error));
                    }
                    if (country.getSelectedItemPosition() == 0) {
                        Toast.makeText(Address.this, R.string.cont_Sele, Toast.LENGTH_SHORT).show();
                    }
                    if (state.getSelectedItemPosition() == 0) {
                        Toast.makeText(Address.this, R.string.stat, Toast.LENGTH_SHORT).show();
                    }
                    if (add_ids != 0) {
                        if (!f_name.getText().toString().trim().isEmpty() && Validation.validateName(f_name.getText().toString().trim()) && f_name.getText().toString().trim().length() >= 2

                                && !l_name.getText().toString().trim().isEmpty() && Validation.validateName(l_name.getText().toString().trim())
                                && !addressone.getText().toString().trim().isEmpty() && addressone.getText().toString().length() >= 3
                                && !city.getText().toString().trim().isEmpty() && Validation.validateName(city.getText().toString().trim()) && city.getText().toString().trim().length() > 2
                                && !pincode.getText().toString().trim().isEmpty() && pincode.getText().toString().length() == 6
                                && mobile.getText().toString().length() == 10 && !mobile.getText().toString().trim().isEmpty() && Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()
                                && country.getSelectedItemPosition() != 0 && state.getSelectedItemPosition() != 0
                                ) {

                            UPDATE_ADD addressTask = new UPDATE_ADD();
                            addressTask.execute(f_name.getText().toString().trim(), l_name.getText().toString().trim(), city.getText().toString().trim(), addressone.getText().toString().trim(), addressstwo.getText().toString().trim(), country_list.get(country.getSelectedItemPosition()).getCount_id() + "", pincode.getText().toString().trim(), state_list.get(state.getSelectedItemPosition()).getZone_id(), mobile.getText().toString(), company.getText().toString().trim());


                        }
                    } else {
                        if (!f_name.getText().toString().trim().isEmpty() && Validation.validateName(f_name.getText().toString().trim()) && f_name.getText().toString().trim().length() > 2

                                && !l_name.getText().toString().trim().isEmpty() && Validation.validateName(l_name.getText().toString().trim())
                                && !addressone.getText().toString().trim().isEmpty() && addressone.getText().toString().length() >= 3
                                && !city.getText().toString().trim().isEmpty() && Validation.validateName(city.getText().toString().trim()) && city.getText().toString().trim().length() >= 2
                                && !pincode.getText().toString().trim().isEmpty() && pincode.getText().toString().length() == 6
                                && mobile.getText().toString().length() == 10 && !mobile.getText().toString().trim().isEmpty() && Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()
                                && country.getSelectedItemPosition() != 0 && state.getSelectedItemPosition() != 0
                                ) {


                            AddressTask addressTask = new AddressTask();
                            addressTask.execute(f_name.getText().toString().trim(),
                                    l_name.getText().toString().trim(),
                                    city.getText().toString().trim(),
                                    addressone.getText().toString().trim(),
                                    addressstwo.getText().toString().trim(),
                                    country_list.get(country.getSelectedItemPosition()).getCount_id() + "",
                                    pincode.getText().toString().trim(),
                                    state_list.get(state.getSelectedItemPosition()).getZone_id(),
                                    mobile.getText().toString(), company.getText().toString().trim());

                        }
                    }
                } else {

                    if (f_name.getText().toString().trim().equals("")) {
                        f_name.setError(getResources().getString(R.string.f_na));
                    }
                    if (!Validation.validateName(f_name.getText().toString().trim())) {
                        f_name.setError(getResources().getString(R.string.valid_name));
                    }

                    if (f_name.getText().toString().trim().length() <= 2) {
                        f_name.setError(getResources().getString(R.string.valid_name));
                    }
                    if (l_name.getText().toString().trim().equals("")) {
                        l_name.setError(getResources().getString(R.string.l_na));
                    }
                    if (!Validation.validateName(l_name.getText().toString().trim())) {
                        l_name.setError(getResources().getString(R.string.valid_name));
                    }
                    if (addressone.getText().toString().trim().equals("")) {
                        addressone.setError(getResources().getString(R.string.add_de));
                    }
                    if (addressone.getText().toString().length() <= 3) {
                        addressone.setError(getResources().getString(R.string.add_de_valid));
                    }
                    if (city.getText().toString().trim().equals("")) {
                        city.setError(getResources().getString(R.string.city_error));
                    }

                    if (!Validation.validateName(city.getText().toString().trim())) {
                        city.setError(getResources().getString(R.string.valid_city));
                    }
                    if (city.getText().toString().trim().length() <= 2) {
                        city.setError(getResources().getString(R.string.valid_city));
                    }
                    if (pincode.getText().toString().trim().equals("")) {
                        pincode.setError(getResources().getString(R.string.oin_error));
                    }
                    if (pincode.getText().toString().length() != 6) {
                        pincode.setError(getResources().getString(R.string.pin_error1));
                    }
                    if (mobile.getText().toString().length() != 10) {
                        mobile.setError(getResources().getString(R.string.mobl_error));
                    }
                    if (!Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()) {
                        mobile.setError(getResources().getString(R.string.mobl_error));
                    }
                    if (country.getSelectedItemPosition() == 0) {
                        Toast.makeText(Address.this, R.string.cont_Sele, Toast.LENGTH_SHORT).show();
                    }
                    if (state.getSelectedItemPosition() == 0) {
                        Toast.makeText(Address.this, R.string.stat, Toast.LENGTH_SHORT).show();
                    }

                    if (!f_name.getText().toString().trim().isEmpty() && Validation.validateName(f_name.getText().toString().trim()) && f_name.getText().toString().trim().length() > 2

                            && !l_name.getText().toString().trim().isEmpty() && Validation.validateName(l_name.getText().toString().trim())
                            && !addressone.getText().toString().trim().isEmpty() && addressone.getText().toString().length() >= 3
                            && !city.getText().toString().trim().isEmpty() && Validation.validateName(city.getText().toString().trim()) && city.getText().toString().trim().length() >= 2
                            && !pincode.getText().toString().trim().isEmpty() && pincode.getText().toString().length() == 6
                            && mobile.getText().toString().length() == 10 && !mobile.getText().toString().trim().isEmpty() && Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()
                            && country.getSelectedItemPosition() != 0 && state.getSelectedItemPosition() != 0
                            ) {

                        Log.d("from_valuess_s", from + "");

                        if (has_ship == 1 || from == 4) {
                            AddressTask addressTask = new AddressTask();
                            addressTask.execute(f_name.getText().toString().trim(),
                                    l_name.getText().toString().trim(),
                                    city.getText().toString().trim(),
                                    addressone.getText().toString().trim(),
                                    addressstwo.getText().toString().trim(),
                                    country_list.get(country.getSelectedItemPosition()).getCount_id() + "",
                                    pincode.getText().toString().trim(),
                                    state_list.get(state.getSelectedItemPosition()).getZone_id(),
                                    mobile.getText().toString(), company.getText().toString().trim());
                        } else {
                            SaveBillAddress1 addressTask = new SaveBillAddress1();
                            addressTask.execute(f_name.getText().toString().trim(),
                                    l_name.getText().toString().trim(),
                                    city.getText().toString().trim(),
                                    addressone.getText().toString().trim(),
                                    addressstwo.getText().toString().trim(),
                                    country_list.get(country.getSelectedItemPosition()).getCount_id(),
                                    pincode.getText().toString().trim(),
                                    state_list.get(state.getSelectedItemPosition()).getZone_id(),
                                    mobile.getText().toString().trim());
                        }
                    }

                }
            }


        });

    }

    private class AddressTask extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Add_Save", "started");
            pDialog1 = new ProgressDialog(Address.this);
            pDialog1.setMessage(getResources().getString(R.string.loading_wait));
            pDialog1.setCancelable(false);
            pDialog1.show();

        }

        protected String doInBackground(Object... param) {


            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("firstname", param[0]);
                json.put("lastname", param[1]);
                json.put("city", param[2]);
                json.put("address_1", param[3]);
                json.put("address_2", param[4]);
                json.put("country_id", param[5]);
                json.put("postcode", param[6]);
                json.put("zone_id", param[7]);
                json.put("phone", param[8]);
                json.put("company", param[9]);
                Log.d("Add_save", json.toString());
                Log.d("Add_save", Appconstatants.address_save);
                Log.d("Add_save", Appconstatants.sessiondata);

                Log.d("From", from + "");

                if (from == 3) {
                    logger.info("Address save guest api" + Appconstatants.guest_api);
                    Log.d("Add_save", Appconstatants.guest_api);
                    json.put("email", cc.getString("email"));
                    json.put("telephone", param[8]);
                    Log.d("guest_url", Appconstatants.guest_api);
                    Log.d("guest_req", json.toString());
                    logger.info("Address save guest api req" + json);
                    response = connection.sendHttpPostjson(Appconstatants.guest_api, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);
                    logger.info("Address save guest api resp" + response);
                    Log.d("guest_resp", response);
                } else if (from == 2) {
                    logger.info("Address save new user api" + Appconstatants.address_save);

                    JSONObject custom_field_obj = new JSONObject();
                    JSONObject address = new JSONObject();
                    // address.put("3","demo address 3");
                    custom_field_obj.put("address", address);
                    json.put("custom_field", custom_field_obj);
                    Log.d("Add_resp_new", json.toString() + "");
                    logger.info("Address save new user api req" + json);


                    response = connection.sendHttpPostjson(Appconstatants.address_save, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);

                    logger.info("Address save new user api resp" + response);
                    Log.d("Add_resp_new", response);
                } else if (from == 1) {
                    logger.info("Address save old user api" + Appconstatants.address_save + "&existing=1");
                    JSONObject object = new JSONObject();
                    object.put("address_id", address_id);
                    Log.d("Add_req_ex", object.toString() + "");
                    logger.info("Address save old user api req" + json);
                    response = connection.sendHttpPostjson(Appconstatants.address_save + "&existing=1", object, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);
                    logger.info("Address save old user api resp" + response);
                    Log.d("Add_respex", response);
                } else if (from == 4) {

                    logger.info("Address save new user api" + Appconstatants.address_save);

                    JSONObject custom_field_obj = new JSONObject();
                    JSONObject address = new JSONObject();
                    // address.put("3","demo address 3");
                    custom_field_obj.put("address", address);
                    json.put("custom_field", custom_field_obj);
                    Log.d("Add_resp_new", json.toString() + "");
                    logger.info("Address save new user api req" + json);


                    response = connection.sendHttpPostjson(Appconstatants.SAVE_ADD_API, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);

                    logger.info("Address save new user api resp" + response);
                    Log.d("Add_resp_new", response);

                } else {
                    logger.info("Address save new user apisssssssssssssss" + Appconstatants.address_save);

                    JSONObject custom_field_obj = new JSONObject();
                    JSONObject address = new JSONObject();
                    // address.put("3","demo address 3");
                    custom_field_obj.put("address", address);
                    json.put("custom_field", custom_field_obj);
                    Log.d("Add_resp_newsssssss", json.toString() + "");
                    logger.info("Address save new user api req" + json);


                    response = connection.sendHttpPostjson(Appconstatants.address_save, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);

                    logger.info("Address save new user api resp" + response);
                    Log.d("Add_resp_new", response);

                }


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Add_Save", "ADD_save--->  " + resp);
            if (resp != null) {
                Log.i("werhgi", resp + "");
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {

                        if (from == 4) {
                            Toast.makeText(Address.this, R.string.address_save, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            setResult(3, intent);
                            finish();

                        } else {

                            if (from == 3) {
                                SaveBillAddress addressTask = new SaveBillAddress();
                                addressTask.execute(f_name.getText().toString().trim(),
                                        l_name.getText().toString().trim(),
                                        city.getText().toString().trim(),
                                        addressone.getText().toString().trim(),
                                        addressstwo.getText().toString().trim(),
                                        country_list.get(country.getSelectedItemPosition()).getCount_id(),
                                        pincode.getText().toString().trim(),
                                        state_list.get(state.getSelectedItemPosition()).getZone_id(),
                                        mobile.getText().toString().trim());

                            } else {

                                String add_id = json.getString("id");
                                add_ids = json.getInt("id");
                                SaveBillAddress addressTask = new SaveBillAddress();
                                addressTask.execute(add_id);


                            }


                        }

                    } else {
                        if (pDialog1 != null && pDialog1.isShowing())
                            pDialog1.dismiss();
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Address.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("hgkfhk", e.toString() + "");
                    if (pDialog1 != null && pDialog1.isShowing())
                        pDialog1.dismiss();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cont.performClick();
                                }
                            })
                            .show();

                }

            } else {
                if (pDialog1 != null && pDialog1.isShowing())
                    pDialog1.dismiss();
                Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cont.performClick();
                            }
                        })
                        .show();

            }
        }
    }

    private class SaveBillAddress extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Add_Save", "started");

        }

        protected String doInBackground(Object... param) {

            String response = null;
            Connection connection = new Connection();
            try {

                Log.d("Add_save", Appconstatants.bill_address_save);
                Log.d("Add_save", Appconstatants.sessiondata);
                if (from == 3) {
                    JSONObject json = new JSONObject();
                    json.put("firstname", param[0]);
                    json.put("lastname", param[1]);
                    json.put("city", param[2]);
                    json.put("address_1", param[3]);
                    json.put("address_2", param[4]);
                    json.put("country_id", param[5]);
                    json.put("postcode", param[6]);
                    json.put("zone_id", param[7]);
                    json.put("phone", param[8]);
                    logger.info("Bill Address save guest api" + Appconstatants.guest_shipping_api);
                    Log.d("guest_sav_req", json.toString());
                    Log.d("guest_sav_url", Appconstatants.guest_shipping_api);
                    logger.info("Bill Address save guest api req" + json);
                    response = connection.sendHttpPostjson(Appconstatants.guest_shipping_api, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);
                    logger.info("Bill Address save guest api resp" + response);
                    Log.d("guest_sav_resp", response);
                } else if (from == 2) {
                    logger.info("Bill Address save new  user api" + Appconstatants.bill_address_save + "&existing=1");
                    JSONObject object = new JSONObject();
                    object.put("address_id", param[0]);
                    logger.info("Bill Address save new  user api req" + object);


                    response = connection.sendHttpPostjson(Appconstatants.bill_address_save + "&existing=1", object, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);


                    logger.info("Bill Address save new  user api resp" + response);
                    Log.d("Add_resp_new", object.toString());
                } else if (from == 1) {
                    logger.info("Bill Address save old user api" + Appconstatants.bill_address_save + "&existing=1");
                    JSONObject object = new JSONObject();
                    object.put("address_id", address_id);
                    Log.d("Add_req_existing", object.toString());
                    logger.info("Bill Address save old user api req" + object);
                    response = connection.sendHttpPostjson(Appconstatants.bill_address_save + "&existing=1", object, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);
                    Log.d("Add_respex", response);
                    logger.info("Bill Address save old user api resp" + response);
                }


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            Log.i("Add_Save", "ADD_save--->  " + resp);
            pDialog1.dismiss();
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Toast.makeText(Address.this, R.string.address_save, Toast.LENGTH_LONG).show();
                        Intent i;
                        if (has_ship == 1) {
                            i = new Intent(Address.this, DeliveryMethod.class);
                        } else {
                            i = new Intent(Address.this, PaymentMethod.class);
                        }

                        i.putExtra("addres_id", address_id);
                        i.putExtra("fname", f_name.getText().toString());
                        i.putExtra("lname", l_name.getText().toString());
                        i.putExtra("from", from);
                        i.putExtra("company", company.getText().toString());
                        i.putExtra("addressone", addressone.getText().toString());
                        i.putExtra("addresstwo", addressstwo.getText().toString());
                        i.putExtra("city", city.getText().toString());
                        i.putExtra("pincode", pincode.getText().toString());
                        i.putExtra("mobile", mobile.getText().toString());
                        i.putExtra("country", country_list.get(country.getSelectedItemPosition()).getCount_name());
                        i.putExtra("state", state_list.get(state.getSelectedItemPosition()).getCount_name());
                        startActivity(i);
                        //  }

                    } else {
                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cont.performClick();
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
                                    cont.performClick();
                                }
                            })
                            .show();

                }
            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cont.performClick();
                            }
                        })
                        .show();

            }
        }
    }

    private class SaveBillAddress1 extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Add_Save", "started");
            pDialog1 = new ProgressDialog(Address.this);
            pDialog1.setMessage(getResources().getString(R.string.loading_wait));
            pDialog1.setCancelable(false);
            pDialog1.show();
        }

        protected String doInBackground(Object... param) {

            String response = null;
            Connection connection = new Connection();
            try {

                JSONObject json = new JSONObject();
                json.put("firstname", param[0]);
                json.put("lastname", param[1]);
                json.put("city", param[2]);
                json.put("address_1", param[3]);
                json.put("address_2", param[4]);
                json.put("country_id", param[5]);
                json.put("postcode", param[6]);
                json.put("zone_id", param[7]);
                json.put("phone", param[8]);
                Log.d("Add_save", Appconstatants.bill_address_save);
                Log.d("Add_save", Appconstatants.sessiondata);
                if (from == 3) {
                    logger.info("Bill Address save guest api" + Appconstatants.guest_api);
                    Log.d("guest_sav_req", json.toString());
                    Log.d("guest_sav_url", Appconstatants.guest_shipping_api);
                    logger.info("Bill Address save guest api req" + json);
                    json.put("email", cc.getString("email"));
                    json.put("telephone", param[8]);

                    response = connection.sendHttpPostjson(Appconstatants.guest_api, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);
                    logger.info("Bill Address save guest api resp" + response);
                    Log.d("guest_sav_resp", response);
                } else /*(from==2)*/ {
                    logger.info("Bill Address save new  user api" + Appconstatants.bill_address_save);
                    JSONObject custom_field_obj = new JSONObject();
                    JSONObject address = new JSONObject();
                    // address.put("3","demo address 3");
                    custom_field_obj.put("address", address);
                    json.put("custom_field", custom_field_obj);

                    response = connection.sendHttpPostjson(Appconstatants.bill_address_save, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);

                    logger.info("Bill Address save new  user api resp" + response);
                }


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            Log.i("Add_Save", "ADD_save--->  " + resp);
            pDialog1.dismiss();
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Toast.makeText(Address.this, R.string.address_save, Toast.LENGTH_LONG).show();
                        Intent i;
                        i = new Intent(Address.this, PaymentMethod.class);
                        i.putExtra("addres_id", address_id);
                        i.putExtra("fname", f_name.getText().toString());
                        i.putExtra("lname", l_name.getText().toString());
                        i.putExtra("from", from);
                        i.putExtra("company", company.getText().toString());
                        i.putExtra("addressone", addressone.getText().toString());
                        i.putExtra("addresstwo", addressstwo.getText().toString());
                        i.putExtra("city", city.getText().toString());
                        i.putExtra("pincode", pincode.getText().toString());
                        i.putExtra("mobile", mobile.getText().toString());
                        i.putExtra("country", country_list.get(country.getSelectedItemPosition()).getCount_name());
                        i.putExtra("state", state_list.get(state.getSelectedItemPosition()).getCount_name());
                        startActivity(i);

                    } else {
                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cont.performClick();
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
                                    cont.performClick();
                                }
                            })
                            .show();

                }
            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cont.performClick();
                            }
                        })
                        .show();

            }


        }
    }


    private class CountryTask extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Country", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("Country list api" + Appconstatants.country_list_api);
            String response = null;
            Connection connection = new Connection();
            try {
                response = connection.connStringResponse(Appconstatants.country_list_api, null, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);
                logger.info("Country list api resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            Log.i("Country", "Country--->  " + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    country_list = new ArrayList<>();
                    CountryPO po1 = new CountryPO();
                    po1.setCount_id(0);
                    po1.setCount_name(getApplication().getResources().getString(R.string.selc_oun));
                    po1.setCount_iso_code_2("");
                    po1.setCount_iso_code_3("");
                    country_list.add(po1);


                    if (json.getInt("success") == 1) {
                        JSONArray jsonArray = new JSONArray(json.getString("data"));
                        for (int y = 0; y < jsonArray.length(); y++) {
                            JSONObject object = jsonArray.getJSONObject(y);
                            CountryPO po = new CountryPO();
                            po.setCount_id(object.getInt("country_id"));
                            po.setCount_name(object.isNull("name") ? "" : object.getString("name"));
                            po.setCount_iso_code_2(object.isNull("iso_code_2") ? "" : object.getString("iso_code_2"));
                            po.setCount_iso_code_3(object.isNull("iso_code_3") ? "" : object.getString("iso_code_3"));

                            country_list.add(po);
                        }
                        CountryAdapter c_adapter = new CountryAdapter(Address.this, R.layout.country_row, country_list);
                        country.setAdapter(c_adapter);

                        if (from == 1) {
                            f_name.setText(cc.getString("first"));
                            l_name.setText(cc.getString("last_name"));
                            company.setText(cc.getString("company"));
                            addressone.setText(cc.getString("addressone"));
                            addressstwo.setText(cc.getString("addresstwo"));
                            city.setText(cc.getString("city"));
                            pincode.setText(cc.getString("pincode"));
                            mobile.setText(cc.getString("phone"));
                            company.setText(cc.getString("company"));
                            for (int y = 0; y < country_list.size(); y++) {
                                if (cc.getString("country").equalsIgnoreCase(country_list.get(y).getCount_name())) {
                                    country.setSelection(y);
                                }
                            }

                        }

                        state_list = new ArrayList<>();
                        CountryPO po = new CountryPO();
                        po.setZone_id("0");
                        po.setCont_id("0");
                        po.setCount_name(getResources().getString(R.string.sel_sta));
                        po.setCount_iso_code_2("");
                        po.setCount_iso_code_3("");
                        state_list.add(po);
                        s_adapter = new StateAdapter(Address.this, R.layout.country_row, state_list);
                        state.setAdapter(s_adapter);
                        success.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                    } else {
                        error_network.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        success.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error_msg = array.get(0) + "";
                        errortxt2.setText(error_msg);
                        Toast.makeText(Address.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    error_network.setVisibility(View.GONE);
                    success.setVisibility(View.GONE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    CountryTask countryTask = new CountryTask();
                                    countryTask.execute();

                                }
                            })
                            .show();

                }
            } else {
                errortxt1.setText(R.string.no_con);
                success.setVisibility(View.GONE);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

        }
    }

    private class StateTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        protected String doInBackground(String... param) {
            logger.info("State list api" + param[0]);
            Log.i("State list api", param[0] + "");

            String response = null;
            Connection connection = new Connection();
            try {

                response = connection.connStringResponse(param[0], null, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);
                logger.info("State list api resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            Log.i("Country", "Country--->  " + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1)

                    {
                        JSONObject jsonObject = new JSONObject(json.getString("data"));
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("zone"));
                        for (int y = 0; y < jsonArray.length(); y++) {
                            JSONObject object = jsonArray.getJSONObject(y);
                            CountryPO po1 = new CountryPO();
                            po1.setZone_id(object.isNull("zone_id") ? "" : object.getString("zone_id"));
                            po1.setCont_id(object.isNull("country_id") ? "" : object.getString("country_id"));
                            po1.setCount_name(object.isNull("name") ? "" : object.getString("name"));
                            po1.setCount_iso_code_2(object.isNull("iso_code_2") ? "" : object.getString("iso_code_2"));
                            po1.setCount_iso_code_3(object.isNull("iso_code_3") ? "" : object.getString("iso_code_3"));

                            state_list.add(po1);
                        }


                         s_adapter = new StateAdapter(Address.this, R.layout.country_row, state_list);
                          state.setAdapter(s_adapter);

                        if (from == 1) {

                            for (int y = 0; y < state_list.size(); y++) {
                                if (cc.getString("state").equalsIgnoreCase(state_list.get(y).getCount_name())) {
                                    state.setSelection(y);
                                }
                            }
                        }
                        success.setVisibility(View.VISIBLE);
                        error_network.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);

                    } else {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        success.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error_msg = array.get(0) + "";
                        errortxt2.setText(error_msg);
                        Toast.makeText(Address.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    error_network.setVisibility(View.GONE);
                    success.setVisibility(View.GONE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    CountryTask countryTask = new CountryTask();
                                    countryTask.execute();

                                }
                            })
                            .show();
                }

            } else {
                errortxt1.setText(R.string.no_con);
                success.setVisibility(View.GONE);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

        }
    }

    private class UPDATE_ADD extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            pDialog1 = new ProgressDialog(Address.this);
            pDialog1.setMessage(getResources().getString(R.string.loading_wait));
            pDialog1.setCancelable(false);
            pDialog1.show();
        }

        protected String doInBackground(String... param) {
            String response = null;
            try {
                JSONObject json = new JSONObject();
                json.put("firstname", param[0]);
                json.put("lastname", param[1]);
                json.put("city", param[2]);
                json.put("address_1", param[3]);
                json.put("address_2", param[4]);
                json.put("country_id", param[5]);
                json.put("postcode", param[6]);
                json.put("zone_id", param[7]);
                json.put("phone", param[8]);
                json.put("company", param[9]);
                Log.d("Add_save", json.toString());
                Log.d("Add_save", Appconstatants.address_save);
                Log.d("Add_save", Appconstatants.sessiondata);
                Connection connection = new Connection();
                if (from == 2) {
                    response = connection.sendHttpPutjson1(Appconstatants.UPDATE_ADD_API + add_ids, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);
                } else {
                    response = connection.sendHttpPutjson1(Appconstatants.UPDATE_ADD_API + address_id, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Address.this);
                }

                Log.d("Add_resp", response + "");
                Log.d("Add_url", Appconstatants.UPDATE_ADD_API + address_id + "");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            Log.i("tag", "save_input" + param[0]);
            return response;
        }

        protected void onPostExecute(String resp) {

            pDialog1.dismiss();
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {

                       /* AddressListTask addressListTask = new AddressListTask();
                        addressListTask.execute();*/
                        if (from == 2) {
                            SaveBillAddress addressTask = new SaveBillAddress();
                            addressTask.execute(add_ids);
                        } else {
                            Toast.makeText(Address.this, R.string.address_update, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.putExtra("first", f_name.getText().toString());
                            intent.putExtra("from", from);
                            intent.putExtra("last_name", l_name.getText().toString());
                            intent.putExtra("company", company.getText().toString());
                            intent.putExtra("addressone", addressone.getText().toString());
                            intent.putExtra("addresstwo", addressstwo.getText().toString());
                            intent.putExtra("city", city.getText().toString());
                            intent.putExtra("pincode", pincode.getText().toString());
                            intent.putExtra("country", country_list.get(country.getSelectedItemPosition()).getCount_name());
                            intent.putExtra("state", state_list.get(state.getSelectedItemPosition()).getCount_name());
                            intent.putExtra("phone", mobile.getText().toString());
                            setResult(3, intent);
                            finish();
                        }


                    } else {

                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Address.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cont.performClick();
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
                                    cont.performClick();

                                }
                            })
                            .show();

                    Toast.makeText(Address.this, R.string.failed, Toast.LENGTH_LONG).show();
                }

            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cont.performClick();
                            }
                        })
                        .show();
                Toast.makeText(Address.this, R.string.failed, Toast.LENGTH_LONG).show();
            }

        }
    }


}
