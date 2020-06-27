package com.iamretailer.krishnasupermarket;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.iamretailer.krishnasupermarket.Adapter.CurAdapter;
import com.iamretailer.krishnasupermarket.Adapter.LangAdapter;
import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.CommonFunctions;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.Common.LanguageList;
import com.iamretailer.krishnasupermarket.Common.LocaleHelper;
import com.iamretailer.krishnasupermarket.POJO.CurPO;
import com.iamretailer.krishnasupermarket.POJO.LangPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import stutzen.co.network.Connection;


public class Drawer extends Language {

    LinearLayout myorders;
    TextView login;
    TextView email;
    DBController dbCon;
    LinearLayout callus;
    LinearLayout gorateus;
    LinearLayout goshare;
    LinearLayout gologout;
    LinearLayout home;
    LinearLayout home1;
    LinearLayout wish;
    TextView cart_count1;
    LinearLayout change_pwd;
    LinearLayout track_order;
    LinearLayout my_profile;
    FrameLayout user;
    AndroidLogger logger;
    LinearLayout address;
    LinearLayout language;
    ArrayList<LangPO> langs;
    ArrayList<CurPO> curs;
    LangAdapter langAdapter;
    CurAdapter curAdapter;
    ListView lang_list;
    ListView cur_list;
    int pos=0;
    LinearLayout wallet;
    LinearLayout store;
    LinearLayout aboutus;
    LinearLayout currency;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbCon=new DBController(Drawer.this);
        CommonFunctions.updateAndroidSecurityProvider(this);
        Appconstatants.sessiondata=dbCon.getSession();
        Appconstatants.Lang=dbCon.get_lang_code();
        Appconstatants.CUR=dbCon.getCurCode();
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);

    }

    public void drawerview(FrameLayout view, final DrawerLayout layout, Context context){

        login =(TextView)view.findViewById(R.id.loginview);
        myorders =(LinearLayout) view.findViewById(R.id.myorders);
      /*  my_cart =(LinearLayout) view.findViewById(R.id.my_cart);*/
        callus = (LinearLayout)view. findViewById(R.id.callus);
        gorateus = (LinearLayout)view. findViewById(R.id.gorateus);
        goshare = (LinearLayout)view. findViewById(R.id.goshare);
        gologout = (LinearLayout)view. findViewById(R.id.gologout);
        home1 = (LinearLayout)view. findViewById(R.id.home);
        wish = (LinearLayout)view. findViewById(R.id.wish);
        email =(TextView)view.findViewById(R.id.user_mail);
        change_pwd=(LinearLayout) view.findViewById(R.id.change_pwd);
        cart_count1=(TextView)view.findViewById(R.id.cart_count1);
        track_order=(LinearLayout)view.findViewById(R.id.track_order);
        my_profile=(LinearLayout)view.findViewById(R.id.my_profile);
        address=(LinearLayout)view.findViewById(R.id.address);

        language=(LinearLayout)findViewById(R.id.language);
        wallet=(LinearLayout)findViewById(R.id.wallet);
        store=(LinearLayout)view.findViewById(R.id.store);
        aboutus=(LinearLayout)view.findViewById(R.id.aboutus);
        currency=(LinearLayout)view.findViewById(R.id.currency);
        title=(TextView)view.findViewById(R.id.title);
        String s1=getResources().getString( R.string.about);
        String s2=getResources().getString(R.string.app_name);
        title.setText(s1+" "+s2);
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        if(dbCon.getLoginCount()>0){
            Log.i("jhfg","hgfdjkghfdkghf"+dbCon.getName()+" -- "+dbCon.getEmail());
            email.setVisibility(View.VISIBLE);
            email.setText(dbCon.getEmail());
            login.setVisibility(View.GONE);
            gologout.setVisibility(View.VISIBLE);
            change_pwd.setVisibility(View.VISIBLE);
            wish.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
            //wallet.setVisibility(View.VISIBLE);
        }else{
            email.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            gologout.setVisibility(View.GONE);
            change_pwd.setVisibility(View.GONE);
            wish.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
          //  wallet.setVisibility(View.GONE);
        }

       /* Log.d("adsd",dbCon.get_lan_c()+"");
        if (dbCon.get_lan_lists()>1)
        {
            language.setVisibility(View.VISIBLE);
        }
        else
        {
            language.setVisibility(View.GONE);
        }
        Log.d("adsd",dbCon.get_cur_count()+"");
        if (dbCon.get_cur_count()>1)
        {
            currency.setVisibility(View.VISIBLE);
        }
        else
        {
            currency.setVisibility(View.GONE);

        }*/
        setListener();

        layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
  


    }
    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Cart api:"+param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Drawer.this);
                logger.info("Cart resp"+response);
                Log.d("Cart_list_resp", response+"");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Cart_list_resp", "CartResp--->  " + resp);

            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONArray) {
                            cart_count1.setText(0 + "");
                            //cart_count1.setText(0 + "");

                        } else if (dd instanceof JSONObject) {


                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));

                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_count1.setText(qty + "");
                            // cart_count1.setText(qty + "");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }


    private void setListener() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login.getText().toString().equalsIgnoreCase(getString(R.string.Log_reg))) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.putExtra("from", 1);
                    startActivity(intent);
                }
                else
                {

                }
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Wallet.class);
                startActivity(intent);

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), About_Activity.class);
                startActivity(intent);
            }
        });


        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bb=new Intent(Drawer.this,MyOrders.class);

                startActivity(bb);
            }
        });
        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),WishList.class));
            }
        });


        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChangePassword.class));
            }
        });
        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dbCon.getLoginCount()>0) {

                    startActivity(new Intent(getApplicationContext(), MyProfile.class));
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.putExtra("from", 1);
                    startActivity(intent);
                }
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddressList.class);
                Bundle bundle=new Bundle();
                bundle.putInt("from", 4);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lang_popup();
            }
        });

        currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_popup();
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreLocator.class);
                startActivity(intent);
            }
        });



        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Drawer.this,ContactForm.class));


              /*  try{
                    if (ContextCompat.checkSelfPermission(Drawer.this,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale
                                (Drawer.this, Manifest.permission.CALL_PHONE)) {
                            Snackbar.make(findViewById(android.R.id.content),
                                    "Please Grant Permissions",
                                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.i("ree8888", "inside");
                                            ActivityCompat.requestPermissions(Drawer.this,
                                                    new String[]{ Manifest.permission.CALL_PHONE},
                                                    212);
                                        }
                                    }).show();
                        } else {
                            Log.i("reevvvvv", "inside");
                            ActivityCompat.requestPermissions(Drawer.this,
                                    new String[]{  Manifest.permission.CALL_PHONE},
                                    212);
                        }
                    } else {

                        showCallPopup();
                    }
                }catch (Exception e){

                }*/

            }
        });

        gorateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.iamretailer.krishnasupermarket"));
                startActivity(browserIntent);

            }
        });
        gologout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showLogoutPopup();
            }
        });
        goshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Download app at https://play.google.com/store/apps/details?id=com.iamretailer.krishnasupermarket");
                startActivity(Intent.createChooser(intent, "Share Using"));
            }
        });


        track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bb=new Intent(Drawer.this,MyCart.class);
                Bundle aa=new Bundle();
                aa.putInt("order",2);
                bb.putExtras(aa);
                startActivity(bb);

            }
        });
        }

    private void lang_popup() {


        AlertDialog.Builder dial = new AlertDialog.Builder(Drawer.this);
        View popUpView = getLayoutInflater().inflate(R.layout.lang_popup, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity= Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();

        LinearLayout no = (LinearLayout) popUpView.findViewById(R.id.no);
        LinearLayout yes = (LinearLayout) popUpView.findViewById(R.id.yes);
        lang_list=(ListView)popUpView.findViewById(R.id.lang_list);
        langs=dbCon.get_lan_list();
        langAdapter=new LangAdapter(Drawer.this,R.layout.lang_list,langs);
        lang_list.setAdapter(langAdapter);
        if (dbCon.get_lan_c()>0)
        {
            for (int k=0;k<langs.size();k++)
            {
                if (langs.get(k).getLang_code().equals(dbCon.get_lang_code()))
                {
                    langs.get(k).setSelect_lang(true);
                }
            }

        }


        lang_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                for (int y=0;y<langs.size();y++)
                {
                    langs.get(y).setSelect_lang(false);
                }
                langs.get(position).setSelect_lang(true);
                langAdapter.notifyDataSetChanged();
            }
        });


        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
                dbCon.get_lang_code();
                dbCon.insert_app_lang(langs.get(pos).getLang_id(),langs.get(pos).getLang_name(),langs.get(pos).getLang_code());
                change_lang(dbCon.get_lang_code());



            }
        });
    }

    public void change_lang(String languageToLoad) {

        ArrayList<String> lang_list= LanguageList.getLang_list();
        String set_lan="en";

        for (int h=0;h<lang_list.size();h++)
        {
            if (languageToLoad.contains(lang_list.get(h)))
            {
                set_lan = lang_list.get(h);

            }

        }
        LocaleHelper.setLocale(this, set_lan);
        Locale locale = new Locale(set_lan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLayoutDirection(locale);
        }
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        startActivity(new Intent(Drawer.this, MainActivity.class));
    }

/*    public void showCallPopup(){
        AlertDialog.Builder dial = new AlertDialog.Builder(Drawer.this);
        View popUpView = getLayoutInflater().inflate(R.layout.call_popup, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity= Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();
        final TextView no = (TextView) popUpView.findViewById(R.id.no);
        final TextView yes = (TextView) popUpView.findViewById(R.id.yes);

        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
                String ph=getResources().getString(R.string.tel)+getResources().getString(R.string.phone_num);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(ph));
                startActivity(callIntent);
            }
        });

    }*/

    public void showLogoutPopup(){
        AlertDialog.Builder dialLo = new AlertDialog.Builder(Drawer.this);
        View popUpView = getLayoutInflater().inflate(R.layout.logout_view, null);
        LinearLayout happy = (LinearLayout) popUpView.findViewById(R.id.happy);
        LinearLayout bad = (LinearLayout) popUpView.findViewById(R.id.bad);
        dialLo.setView(popUpView);
        final AlertDialog dialog = dialLo.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCancelable(true);
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
        int px1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = px;
        lp.height = px1;
        dialog.getWindow().setAttributes(lp);
        happy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
              LogoutTask task = new LogoutTask();
              task.execute(Appconstatants.LOGOUT_URL);
            }
        });
        bad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private class LogoutTask extends AsyncTask<String, Void, String> {


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute()
        {
            pDialog = new ProgressDialog(Drawer.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("confirm_order", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Logout api"+param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.sendHttpPostLogout(param[0],Appconstatants.sessiondata,Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR);
                logger.info("Logout api resp"+response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("confirm_order", "Logout--->  "+resp);
            pDialog.dismiss();
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success")==1)
                    {
                        dbCon.dropUser();
                        LoginManager.getInstance().logOut();
                        Toast.makeText(Drawer.this,R.string.log_suc,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Drawer.this,MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(Drawer.this,R.string.log_fail,Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Drawer.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Drawer.this,R.string.log_fail,Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                Toast.makeText(Drawer.this, R.string.error_net, Toast.LENGTH_SHORT).show();
                Toast.makeText(Drawer.this,R.string.log_fail,Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 212: {
                Log.i("reeeeeee", grantResults.length+"=="+grantResults[0]);
                if ((grantResults.length == 1) && grantResults[0]  == PackageManager.PERMISSION_GRANTED) {
                    Log.i("reekkk", "inside");
                    //Intent i = getIntent();
                    //finish();
                    //startActivity(i);
                } else {
                    Log.i("reeaaaaaa", "inside");
                    Snackbar.make(findViewById(android.R.id.content), "Enable Permissions from settings",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(intent);
                                }
                            }).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbCon.getLoginCount() > 0) {
            email.setText(dbCon.getEmail());
            email.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            gologout.setVisibility(View.VISIBLE);
            change_pwd.setVisibility(View.VISIBLE);
            wish.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
            //wallet.setVisibility(View.VISIBLE);
        } else {
            gologout.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            email.setVisibility(View.GONE);
            change_pwd.setVisibility(View.GONE);
            wish.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
           // wallet.setVisibility(View.GONE);
        }
    }

    private void cur_popup() {


        AlertDialog.Builder dial = new AlertDialog.Builder(Drawer.this);
        View popUpView = getLayoutInflater().inflate(R.layout.cur_popup, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity= Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();

        LinearLayout no = (LinearLayout) popUpView.findViewById(R.id.no);
        LinearLayout yes = (LinearLayout) popUpView.findViewById(R.id.yes);
        cur_list=(ListView)popUpView.findViewById(R.id.cur_list);
        curs=dbCon.get_cur_list();
        curAdapter=new CurAdapter(Drawer.this,R.layout.lang_list,curs);
        cur_list.setAdapter(curAdapter);
        if (dbCon.get_cur_count()>0)
        {
            for (int k=0;k<curs.size();k++)
            {
                if (curs.get(k).getCur_code().equals(dbCon.getCurCode()))
                {
                    curs.get(k).setIsselected(true);
                }
            }

        }


        cur_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                for (int y=0;y<curs.size();y++)
                {
                    curs.get(y).setIsselected(false);
                }
                curs.get(position).setIsselected(true);
                curAdapter.notifyDataSetChanged();
            }
        });


        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
                dbCon.drop_app_cur();
                dbCon.insert_app_cur(curs.get(pos).getCur_title(),curs.get(pos).getCur_code(),curs.get(pos).getCur_left(),curs.get(pos).getCur_right());
                startActivity(new Intent(Drawer.this,MainActivity.class));
            }
        });
    }




}


