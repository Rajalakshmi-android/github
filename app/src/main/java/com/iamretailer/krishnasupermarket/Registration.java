package com.iamretailer.krishnasupermarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.krishnasupermarket.Common.CommonFunctions;
import com.logentries.android.AndroidLogger;

import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.Common.Validation;



import org.json.JSONArray;
import org.json.JSONObject;

import stutzen.co.network.Connection;

public class Registration extends Language {

    EditText f_name,l_name,email,mobile,company,addressone,addressstwo,city,pincode,passone,passtwo;
    TextView cont;
    CheckBox terms;
    int checked;
    Spinner state,country;
    DBController dbController;
    private int from;
    LinearLayout fullayout;
    TextView login_page;
    AndroidLogger logger;
    int has_ship;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        f_name=(EditText)findViewById(R.id.full_name);
        l_name=(EditText)findViewById(R.id.username2);
        email=(EditText)findViewById(R.id.mailid);
        mobile=(EditText)findViewById(R.id.mobile);
        passone=(EditText)findViewById(R.id.passone);
        passtwo=(EditText)findViewById(R.id.passtwo);
        cont=(TextView)findViewById(R.id.cont);
        fullayout=(LinearLayout)findViewById(R.id.fullayout);
        login_page=(TextView)findViewById(R.id.login_page);
      /*  state=(Spinner)findViewById(R.id.state);
        country=(Spinner)findViewById(R.id.country);*/

        dbController=new DBController(Registration.this);
        Appconstatants.sessiondata=dbController.getSession();
        Appconstatants.Lang=dbController.get_lang_code();
        Appconstatants.CUR=dbController.getCurCode();
        from =getIntent().getIntExtra("from",0);
        has_ship=getIntent().getIntExtra("has_ship",0);


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
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (f_name.getText().toString().trim().equals(""))
                {
                    f_name.setError(getResources().getString(R.string.f_na));
                }
                if(!Validation.validateName(f_name.getText().toString().trim()))
                {
                    f_name.setError(getResources().getString(R.string.valid_name));
                }
                if (f_name.getText().toString().length()<=2)
                {
                    f_name.setError(getResources().getString(R.string.valid_name));
                }
                 if (l_name.getText().toString().trim().equals(""))
                {
                    l_name.setError(getResources().getString(R.string.l_na));
                }
                if (!Validation.validateName(l_name.getText().toString().trim()))
                {
                    l_name.setError(getResources().getString(R.string.valid_name));
                }
                if (email.getText().toString().trim().equals(""))
                {
                    email.setError(getResources().getString(R.string.mail_id));
                }
                 if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    email.setError(getResources().getString(R.string.valid_mail));
                }
                if (mobile.getText().toString().trim().equals(""))
                {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }
                if (mobile.getText().toString().length()!=10)
                {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }

                if (!Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches())
                {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }
                if (passone.getText().toString().trim().equals(""))
                {
                    passone.setError(getResources().getString(R.string.pass_word));
                }
                if (passone.getText().toString().trim().length()<=5)
                {
                    passone.setError(getResources().getString(R.string.pass_res));
                }
                if (passtwo.getText().toString().trim().equals(""))
                {
                    passtwo.setError(getResources().getString(R.string.conf));
                }
                 if (!passone.getText().toString().trim().equals(passtwo.getText().toString().trim()))
                {
                    Toast.makeText(Registration.this,R.string.pwd_mis,Toast.LENGTH_LONG).show();
                }

                if (!f_name.getText().toString().isEmpty()  && f_name.getText().toString().trim().length()>2 && Validation.validateName(l_name.getText().toString().trim())
                        && !l_name.getText().toString().isEmpty() && Validation.validateName(l_name.getText().toString().trim())
                       && !email.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()
                        &&mobile.getText().toString().length()==10 && !mobile.getText().toString().isEmpty() && Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()
                        &&!passone.getText().toString().isEmpty() && passone.getText().toString().trim().length()>5
                        &&!passtwo.getText().toString().isEmpty() && passtwo.getText().toString().trim().length()>5
                       && passone.getText().toString().trim().equals(passtwo.getText().toString().trim())
                )
                {
                    checked=1;

                    RegisterTask registerTask=new RegisterTask();
                    registerTask.execute(f_name.getText().toString().trim(),l_name.getText().toString().trim(),email.getText().toString().trim(),passone.getText().toString().trim(),passtwo.getText().toString().trim(),mobile.getText().toString().trim(),String.valueOf(checked));
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


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute()
        {
            pDialog = new ProgressDialog(Registration.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("Register", "started");
        }

        protected String doInBackground(Object... param) {

            logger.info("Register api"+Appconstatants.REGISTER);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("firstname",param[0]);
                json.put("lastname", param[1]);
                json.put("email", param[2]);
                json.put("password", param[3]);
                json.put("confirm", param[4]);
                json.put("telephone", param[5]);
                json.put("agree", param[6]);
                json.put("telephone_unique", Appconstatants.Mobile_Otp);
                JSONObject custom_field_obj= new JSONObject();
                JSONObject account_obj=new JSONObject();
                account_obj.put("1","+364545454");
                custom_field_obj.put("account",account_obj);
                json.put("custom_field",custom_field_obj);
               /* json.put("become_seller", "0");
                json.put("seller_storename","");*/


                Log.d("Input_format",json.toString());
                Log.d("Input_format",Appconstatants.REGISTER);
                Log.d("Input_format",Appconstatants.sessiondata);
                logger.info("Register api req"+Appconstatants.REGISTER);
                response = connection.sendHttpPostjson(Appconstatants.REGISTER,json,Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Registration.this);
                logger.info("Register api resp"+response);
                Log.d("Input_format",response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Register", "Register--->  "+resp);
            if (resp != null) {
             pDialog.dismiss();
                    try {
                        JSONObject json1 = new JSONObject(resp);
                        if (json1.getInt("success") == 1)
                        {
                            JSONObject json=json1.getJSONObject("data");
                            String cus_id = json.isNull("customer_id") ? "" : json.getString("customer_id");
                            String cus_f_name=json.isNull("firstname")?"":json.getString("firstname");
                            String cus_l_name=json.isNull("lastname")?"":json.getString("lastname");
                            String cus_email=json.isNull("email")?"":json.getString("email");
                            String cus_mobile=json.isNull("telephone")?"":json.getString("telephone");
                            String cus_grp_id=json.isNull("customer_group_id")?"":json.getString("customer_group_id");
                            Log.d("Input_format",cus_email);

                            Toast.makeText(getApplicationContext(),R.string.register, Toast.LENGTH_SHORT).show();
                            dbController.user_data(cus_id,cus_f_name+cus_l_name,cus_email,cus_mobile);
                            f_name.setText("");
                            l_name.setText("");
                            email.setText("");
                            mobile.setText("");
                            passone.setText("");
                            passtwo.setText("");
                            Intent intent = new Intent();
                            intent.putExtra("from",from);
                            setResult(3, intent);
                            finish();


                        }else if(json1.getInt("success") == 0)
                        {

                            JSONArray array=json1.getJSONArray("error");
                            Toast.makeText(Registration.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();

                        } else
                            {
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
                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cont.performClick();

                                    }
                                })
                                .show();
                    }
            }
            else
            {
                pDialog.dismiss();

                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
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
  }
