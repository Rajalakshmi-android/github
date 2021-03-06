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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.Validation;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import stutzen.co.network.Connection;

public class Registration extends Language {

    private EditText f_name;
    private EditText l_name;
    private EditText email;
    private EditText mobile;
    private EditText passone;
    private EditText passtwo;
    TextView cont;
    private int checked;
    private DBController dbController;
    private int from;
    private LinearLayout fullayout;
    private AndroidLogger logger;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        f_name = findViewById(R.id.full_name);
        l_name = findViewById(R.id.username2);
        email = findViewById(R.id.mailid);
        mobile = findViewById(R.id.mobile);
        passone = findViewById(R.id.passone);
        passtwo = findViewById(R.id.passtwo);
        cont = findViewById(R.id.cont);
        fullayout = findViewById(R.id.fullayout);
        TextView login_page = findViewById(R.id.login_page);

        dbController = new DBController(Registration.this);
        Appconstatants.sessiondata = dbController.getSession();
        Appconstatants.Lang = dbController.get_lang_code();
        Appconstatants.CUR = dbController.getCurCode();
        from = getIntent().getIntExtra("from", 0);


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
                if (f_name.getText().toString().trim().equals("")) {
                    f_name.setError(getResources().getString(R.string.f_na));
                }
                if (!Validation.validateName(f_name.getText().toString().trim())) {
                    f_name.setError(getResources().getString(R.string.valid_name));
                }
                if (l_name.getText().toString().trim().equals("")) {
                    l_name.setError(getResources().getString(R.string.l_na));
                }
                if (!Validation.validateName(l_name.getText().toString().trim())) {
                    l_name.setError(getResources().getString(R.string.valid_name));
                }
                if (email.getText().toString().trim().equals("")) {
                    email.setError(getResources().getString(R.string.mail_id));
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError(getResources().getString(R.string.valid_mail));
                }
                if (mobile.getText().toString().trim().equals("")) {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }
                if (mobile.getText().toString().length() <= 7) {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }

                if (!Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()) {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }
                if (passone.getText().toString().trim().equals("")) {
                    passone.setError(getResources().getString(R.string.pass_word));
                }
                if (passone.getText().toString().trim().length() <= 5) {
                    passone.setError(getResources().getString(R.string.pass_res));
                }
                if (passtwo.getText().toString().trim().equals("")) {
                    passtwo.setError(getResources().getString(R.string.conf));
                }
                if (!passone.getText().toString().trim().equals(passtwo.getText().toString().trim())) {
                    Toast.makeText(Registration.this, R.string.pwd_mis, Toast.LENGTH_LONG).show();
                }

                if (!f_name.getText().toString().isEmpty() && Validation.validateName(f_name.getText().toString().trim())
                        && !l_name.getText().toString().isEmpty() && Validation.validateName(l_name.getText().toString().trim())
                        && !email.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()
                        && mobile.getText().toString().length() > 7 && !mobile.getText().toString().isEmpty() && Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()
                        && !passone.getText().toString().isEmpty() && passone.getText().toString().trim().length() > 5
                        && !passtwo.getText().toString().isEmpty() && passtwo.getText().toString().trim().length() > 5
                        && passone.getText().toString().trim().equals(passtwo.getText().toString().trim())
                        ) {
                    checked = 1;

                    RegisterTask registerTask = new RegisterTask();
                    registerTask.execute(f_name.getText().toString().trim(), l_name.getText().toString().trim(), email.getText().toString().trim(), passone.getText().toString().trim(), passtwo.getText().toString().trim(), mobile.getText().toString().trim(), String.valueOf(checked));
                }


            }
        });

        login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.putExtra("from", from);
                startActivity(intent);
                finish();
            }
        });

    }

    private class RegisterTask extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Registration.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("Register", "started");
        }

        protected String doInBackground(Object... param) {

            logger.info("Register api" + Appconstatants.REGISTER);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("firstname", param[0]);
                json.put("lastname", param[1]);
                json.put("email", param[2]);
                json.put("password", param[3]);
                json.put("confirm", param[4]);
                json.put("telephone", param[5]);
                json.put("agree", param[6]);
                json.put("telephone_unique", Appconstatants.Mobile_Otp);
                JSONObject custom_field_obj = new JSONObject();
                JSONObject account_obj = new JSONObject();
                account_obj.put("1", "+364545454");
                custom_field_obj.put("account", account_obj);
                json.put("custom_field", custom_field_obj);
                Log.d("Input_format", json.toString());
                Log.d("Input_format", Appconstatants.REGISTER);
                Log.d("Input_format", Appconstatants.sessiondata);
                logger.info("Register api req" + Appconstatants.REGISTER);
                response = connection.sendHttpPostjson(Appconstatants.REGISTER, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Registration.this);
                logger.info("Register api resp" + response);
                Log.d("Input_format", response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Register", "Register--->  " + resp);
            if (resp != null) {

                try {
                    JSONObject json1 = new JSONObject(resp);
                    if (json1.getInt("success") == 1) {
                        pDialog.dismiss();
                        JSONObject json = json1.getJSONObject("data");
                        String cus_id = json.isNull("customer_id") ? "" : json.getString("customer_id");
                        String cus_f_name = json.isNull("firstname") ? "" : json.getString("firstname");
                        String cus_l_name = json.isNull("lastname") ? "" : json.getString("lastname");
                        String cus_email = json.isNull("email") ? "" : json.getString("email");
                        String cus_mobile = json.isNull("telephone") ? "" : json.getString("telephone");

                        Toast.makeText(getApplicationContext(), R.string.register, Toast.LENGTH_SHORT).show();
                        dbController.user_data(cus_id, cus_f_name + " " + cus_l_name, cus_email, cus_mobile);
                        f_name.setText("");
                        l_name.setText("");
                        email.setText("");
                        mobile.setText("");
                        passone.setText("");
                        passtwo.setText("");
                        Intent intent = new Intent();
                        intent.putExtra("from", from);
                        setResult(3, intent);
                        finish();


                    } else if (json1.getInt("success") == 0) {

                        JSONArray array = json1.getJSONArray("error");
                        if(array.getString(0).contains("User is logged")){
                            LogoutTask task = new LogoutTask();
                            task.execute(Appconstatants.LOGOUT_URL);
                        }else{
                            pDialog.dismiss();
                        }
                        Toast.makeText(Registration.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    } else {
                        pDialog.dismiss();
                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
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
                    pDialog.dismiss();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cont.performClick();

                                }
                            })
                            .show();
                }
            } else {
                pDialog.dismiss();

                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
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

    private class LogoutTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

            Log.d("confirm_order", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Logout api" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.sendHttpPostLogout(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR);
                logger.info("Logout api resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("confirm_order", "Logout--->  " + resp);
            if (pDialog != null)
                pDialog.dismiss();
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        dbController.dropUser();
                        LoginManager.getInstance().logOut();
                        Intent i = new Intent(Registration.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(Registration.this, R.string.log_fail, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Registration.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Registration.this, R.string.log_fail, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(Registration.this, R.string.error_net, Toast.LENGTH_SHORT).show();
                Toast.makeText(Registration.this, R.string.log_fail, Toast.LENGTH_LONG).show();
            }

        }
    }
}
