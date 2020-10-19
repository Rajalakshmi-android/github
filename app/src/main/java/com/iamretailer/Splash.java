package com.iamretailer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.LangPO;
import com.logentries.android.AndroidLogger;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;


import stutzen.co.network.Connection;


public class Splash extends Language {

    private FrameLayout lay;
    private DBController dbCon;
    private AndroidLogger logger;
    private String pushid;
    private String pushregid;
    private String appId = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbCon = new DBController(Splash.this);
        CommonFunctions.updateAndroidSecurityProvider(this);
        setContentView(R.layout.splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Appconstatants.sessiondata = dbCon.getSession();
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        lay = findViewById(R.id.lay);
        Log.d("Session", Appconstatants.sessiondata + "Value");

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                pushid = userId;
                if (registrationId != null) {
                    pushregid = registrationId;
                }
            }
        });
        CheckTask task = new CheckTask();
        task.execute();
        printHashKey(Splash.this);
        appId = BuildConfig.APPLICATION_ID;

    }

    private class CheckTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("Get_Settings", "started:");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (CommonFunctions.isNetworkConnected(Splash.this)) {
                Log.d("session_datasss", Appconstatants.sessiondata + "");
                if (Appconstatants.sessiondata != null && Appconstatants.sessiondata.length() > 0) {
                    GETCURRENCY getcurrency = new GETCURRENCY();
                    getcurrency.execute(Appconstatants.CUR_LIST);

                } else {

                    GetSessionTask task = new GetSessionTask();
                    task.execute(Appconstatants.SESSION_API + "," + Appconstatants.LICENSE_KEY + "," + appId);
                }
            } else {
                Snackbar
                        .make(lay, R.string.error_net,
                                Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckTask task = new CheckTask();
                                task.execute();


                            }
                        })
                        .show();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // TODO Auto-generated method stub
            return null;
        }

        private class GetSessionTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                Log.i("GetSession", "started");
            }

            protected String doInBackground(String... param) {
                Log.i("Session Url :", param[0]);
                logger.info("Session Url :" + param[0]);
                String response = null;
                try {
                    Connection connection = new Connection();
                    response = connection.connStringResponse(param[0], null, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, "", Splash.this);
                    Log.d("session_url", param[0]);
                    Log.d("session_res", response + "zasfadfad");
                    logger.info("Session Resp :" + response);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return response;
            }

            protected void onPostExecute(String resp) {

                if (resp != null) {
                    Log.i("resp__", resp);

                    try {
                        final JSONObject json = new JSONObject(resp);

                        if (json.getInt("success") == 1) {


                            JSONObject jobj = json.getJSONObject("data");
                            dbCon.insertSession(jobj.getString("session"));
                            Appconstatants.sessiondata = dbCon.getSession();

                            GETCURRENCY getcurrency = new GETCURRENCY();
                            getcurrency.execute(Appconstatants.CUR_LIST);


                        } else if (json.getInt("success") == 2) {
                            show_alret();
                        } else {
                            JSONArray array = json.getJSONArray("error");
                            Toast.makeText(Splash.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Snackbar
                                .make(lay, R.string.error_msg,
                                        Snackbar.LENGTH_INDEFINITE)
                                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CheckTask task = new CheckTask();
                                        task.execute();
                                    }
                                })
                                .show();

                        e.printStackTrace();

                    }
                } else {
                    Snackbar
                            .make(lay, R.string.error_net,
                                    Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CheckTask task = new CheckTask();
                                    task.execute();
                                }
                            })
                            .show();
                }
            }
        }
    }

    private class GETCURRENCY extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }

        protected String doInBackground(String... param) {

            Log.d("url_", param[0]);
            logger.info("Session api :" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, "", Splash.this);
                logger.info("Session Resp :" + response);
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
                    if (object.getInt("success") == 1) {

                        JSONObject object2 = object.getJSONObject("data");

                        dbCon.drop_curs();
                        JSONArray cur_list = object2.getJSONArray("currencies");
                        if (cur_list.length() > 0) {
                            for (int i = 0; i < cur_list.length(); i++) {
                                JSONObject object1 = cur_list.getJSONObject(i);
                                String cur_title = object1.isNull("title") ? "" : object1.getString("title");
                                String cur_code = object1.isNull("code") ? "" : object1.getString("code");
                                String cur_left = object1.isNull("symbol_left") ? "" : object1.getString("symbol_left");
                                String cur_right = object1.isNull("symbol_right") ? "" : object1.getString("symbol_right");
                                int def = object1.isNull("default") ? 0 : object1.getInt("default");

                                if (def == 1 && dbCon.get_cur_counts() <= 0) {
                                    dbCon.drop_app_cur();
                                    dbCon.insert_app_cur(cur_title, cur_code, cur_left, cur_right);
                                }
                                dbCon.insert_currencies(cur_title, cur_code, cur_left, cur_right);
                            }
                        }

                        GETLang getLang = new GETLang();
                        getLang.execute(Appconstatants.LANG_API);

                    } else if (object.getInt("success") == 2) {
                        show_alret();
                    } else {
                        JSONArray array = object.getJSONArray("error");
                        Toast.makeText(Splash.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(lay, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CheckTask task = new CheckTask();
                                    task.execute();

                                }
                            })
                            .show();

                }

            } else {
                Snackbar.make(lay, R.string.error_net, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CheckTask task = new CheckTask();
                                task.execute();

                            }
                        })
                        .show();
            }
        }
    }


    private static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("Sha1_", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Sha1_", "printHashKey()", e);
        }
    }


    private class GETLang extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }

        protected String doInBackground(String... param) {

            Log.d("url_", param[0]);
            logger.info("Session api :" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, "", Splash.this);
                logger.info("Session Resp :" + response);
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
                    if (object.getInt("success") == 1) {
                        JSONArray array = object.getJSONArray("data");
                        ArrayList<LangPO> langPOS = new ArrayList<>();
                        dbCon.drop_lang();

                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                LangPO langPO = new LangPO();
                                JSONObject object1 = array.getJSONObject(i);
                                langPO.setLang_id(object1.isNull("language_id") ? "" : object1.getString("language_id"));
                                langPO.setLang_name(object1.isNull("name") ? "" : object1.getString("name"));
                                langPO.setLang_code(object1.isNull("code") ? "" : object1.getString("code"));
                                int def = object1.isNull("default") ? 0 : object1.getInt("default");
                                langPOS.add(langPO);

                                if (def == 1 && dbCon.get_lan_c() <= 0) {
                                    dbCon.drop_app_lang();
                                    dbCon.insert_app_lang(object1.isNull("language_id") ? "" : object1.getString("language_id"), object1.isNull("name") ? "" : object1.getString("name"), object1.isNull("code") ? "" : object1.getString("code"));
                                }

                                dbCon.insert_lang(object1.isNull("language_id") ? "" : object1.getString("language_id"), object1.isNull("name") ? "" : object1.getString("name"), object1.isNull("code") ? "" : object1.getString("code"));
                            }
                        }
                        Intent i = new Intent(Splash.this, MainActivity.class);
                        startActivity(i);

                    } else {
                        JSONArray array = object.getJSONArray("error");
                        Toast.makeText(Splash.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(lay, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CheckTask task = new CheckTask();
                                    task.execute();

                                }
                            })
                            .show();

                }

            } else {
                Snackbar.make(lay, R.string.error_net, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CheckTask task = new CheckTask();
                                task.execute();

                            }
                        })
                        .show();
            }
        }
    }


    private void show_alret() {
        final AlertDialog.Builder dial = new AlertDialog.Builder(Splash.this);
        View popUpView = View.inflate(Splash.this, R.layout.key_lay, null);
        TextView text = popUpView.findViewById(R.id.text2);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://support.iamretailer.com/support/solutions/articles/77000377844-activation-of-app-license-key"));
                startActivity(browserIntent);
            }
        });
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (popupStore.getWindow() != null)
            lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        if (popupStore.getWindow() != null)
            popupStore.getWindow().setAttributes(lp);
        popupStore.setCancelable(false);
        popupStore.show();
    }


}
