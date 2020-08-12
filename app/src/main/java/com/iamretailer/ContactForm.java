package com.iamretailer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.Validation;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import stutzen.co.network.Connection;


public class ContactForm extends Language {

    private EditText full_name;
    private EditText mailid;
    private EditText comments;
    private FrameLayout submit;
    private FrameLayout fullayout;
    private AndroidLogger logger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        DBController dbController = new DBController(ContactForm.this);
        Appconstatants.sessiondata= dbController.getSession();
        Appconstatants.Lang= dbController.get_lang_code();
        Appconstatants.CUR= dbController.getCurCode();
        LinearLayout menu = findViewById(R.id.menu);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        TextView header = findViewById(R.id.header);
        full_name = findViewById(R.id.full_name);
        mailid = findViewById(R.id.mailid);
        comments = findViewById(R.id.comments);
        submit = findViewById(R.id.submit);
        fullayout = findViewById(R.id.fullayout);

        header.setText(R.string.contactus);
        cart_items.setVisibility(View.GONE);

        if(dbController.getLoginCount()>0){
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
                response = connection.sendHttpPostjson(Appconstatants.CONTACT_API, json, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,ContactForm.this);
                logger.info("Contact_form api resp" + response);
                Log.d("Input_format", response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Contact_form", "Contact_form--->  " + resp);
            if (resp != null) {

                try {
                    JSONObject json1 = new JSONObject(resp);
                    if (json1.getInt("success") == 1) {
                        showCallPopup();
                        full_name.setText("");
                        mailid.setText("");
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

    private void showCallPopup(){
        final AlertDialog.Builder dial = new AlertDialog.Builder(ContactForm.this);
        View popUpView = getLayoutInflater().inflate(R.layout.contact_sucess, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity= Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();
        popupStore.setCancelable(false);
        LinearLayout okay= popUpView.findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupStore.dismiss();
                startActivity(new Intent(ContactForm.this,MainActivity.class));
            }
        });


    }
}
