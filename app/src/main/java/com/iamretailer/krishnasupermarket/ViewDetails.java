package com.iamretailer.krishnasupermarket;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.CommonFunctions;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.POJO.OptionsPO;
import com.iamretailer.krishnasupermarket.POJO.OrdersPO;
import com.iamretailer.krishnasupermarket.POJO.PlacePO;



import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class ViewDetails extends Language {
    Bundle bun;
    LinearLayout view_list;

    LinearLayout back;
    ArrayList<PlacePO> list1;
    ArrayList<PlacePO> list2;
    ArrayList<OrdersPO> list;
    FrameLayout no_network;
    LinearLayout retry;
    TextView order_id, order_date, ship_method;
    private DBController db;
    private FrameLayout calls;
    TextView cancel;
    private TextView status;
    private TextView paymethod;
    private LinearLayout ordertotview;
    FrameLayout loading;
    FrameLayout fullayout;
    TextView errortxt1, errortxt2;
    String cur_left = "";
    String cur_right = "";
    LinearLayout loading_bar;
    AndroidLogger logger;
    LinearLayout del_add_lay;
    LinearLayout ship_info,ship_method_sec;
    TextView phone;
    TextView cus_name, cus_address_one, cus_address_two, cus_mobile;
    TextView country_name;
    ImageView del_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        CommonFunctions.updateAndroidSecurityProvider(this);
        db = new DBController(ViewDetails.this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang=db.get_lang_code();
        Appconstatants.CUR=db.getCurCode();
        cur_left = db.get_cur_Left();
        cur_right=db.get_cur_Right();
        bun = new Bundle();
        bun = getIntent().getExtras();
        view_list = (LinearLayout) findViewById(R.id.listview);
        no_network = (FrameLayout) findViewById(R.id.error_network);
        retry = (LinearLayout) findViewById(R.id.retry);
        order_id = (TextView) findViewById(R.id.order_no);
        order_date = (TextView) findViewById(R.id.order_date);
        ship_method = (TextView) findViewById(R.id.ship_method);
        ordertotview = (LinearLayout) findViewById(R.id.ordertot);
        calls = (FrameLayout) findViewById(R.id.call);
        paymethod = (TextView) findViewById(R.id.paymethod);
        status = (TextView) findViewById(R.id.status);
        TextView header = (TextView) findViewById(R.id.header);
        loading=(FrameLayout)findViewById(R.id.loading);
        fullayout=(FrameLayout)findViewById(R.id.fullayout);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        del_add_lay=(LinearLayout)findViewById(R.id.del_add_lay);
       // status_img=(LinearLayout)findViewById(R.id.status_img);
        ship_info=(LinearLayout)findViewById(R.id.ship_info);
        ship_method_sec=(LinearLayout)findViewById(R.id.ship_method_sec);
        phone = (TextView) findViewById(R.id.phone);
        cus_name = (TextView) findViewById(R.id.name);
        cus_address_one = (TextView) findViewById(R.id.address_one);
        cus_address_two = (TextView) findViewById(R.id.address_two);
        country_name = (TextView) findViewById(R.id.country);
        cus_mobile = (TextView) findViewById(R.id.mobile);
        del_image=(ImageView)findViewById(R.id.del_image);
        header.setText(getResources().getString(R.string.order_ids)+bun.getString("id"));
        OrderTask task = new OrderTask();
        task.execute(Appconstatants.myorder_api + "&id=" + bun.getString("id"));
        if (bun.getString("status").toLowerCase().equals("placed"))
            del_image.setImageResource(R.mipmap.placed);
        else
            del_image.setImageResource(R.mipmap.pending);
        status.setText(bun.getString("status"));
        order_id.setText("#"+bun.getString("id"));


        back = (LinearLayout) findViewById(R.id.menu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        calls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCallPopup();
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                no_network.setVisibility(View.GONE);
                OrderTask task = new OrderTask();
                task.execute(Appconstatants.myorder_api + "&id=" + bun.getString("id"));

            }
        });
        LinearLayout cart_items = (LinearLayout) findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);

    }

    public void showCallPopup() {
        AlertDialog.Builder dial = new AlertDialog.Builder(ViewDetails.this);
        View popUpView = getLayoutInflater().inflate(R.layout.call_popup, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
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

                if(isPermissionGranted()){
                    call_action();
                }

            }
        });

    }

    public void call_action(){

        String ph=getResources().getString(R.string.tel)+getResources().getString(R.string.phone_num);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(ph));
        startActivity(callIntent);
    }


    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
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
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,ViewDetails.this);
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
                    list1 = new ArrayList<>();
                    list2 = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success")==1)
                    {
                    JSONObject object = new JSONObject(json.getString("data"));
                    Log.i("reeee",object.toString());
                    paymethod.setText(object.isNull("payment_method") ? "" : object.getString("payment_method"));
                    order_date.setText(CommonFunctions.convert_date(object.isNull("date_added") ? "" : object.getString("date_added")));
                    ship_method.setText(object.isNull("shipping_method") ? "" : object.getString("shipping_method"));


                    String shipping_firstname=object.isNull("shipping_firstname")?"":object.getString("shipping_firstname");
                    if (shipping_firstname.equalsIgnoreCase(""))
                    {
                        del_add_lay.setVisibility(View.GONE);
                        //status_img.setVisibility(View.GONE);
                        ship_info.setVisibility(View.GONE);
                        ship_method_sec.setVisibility(View.GONE);
                    }
                    else
                    {
                      //  del_add.setText(object.isNull("payment_firstname") ? "" : object.getString("payment_firstname") + "" + object.getString("payment_lastname") + "\n" + object.getString("payment_address_1") + "\n" + object.getString("payment_address_2") + "\n" + object.getString("payment_city") + " - " + object.getString("payment_postcode") + "\n" + object.getString("payment_zone") + "\n" + object.getString("payment_country")+ "\n"+object.getString("telephone")+"");
                        cus_name.setText(object.isNull("payment_firstname") ? "" : object.getString("payment_firstname") + " " + object.getString("payment_lastname") );
                        cus_address_one.setText(object.getString("payment_address_1") + ", " + object.getString("payment_address_2")+",");
                        cus_address_two.setText(object.getString("payment_city") + ", "+object.getString("payment_zone")+",");
                        country_name.setText(object.getString("payment_country")+" - "+object.getString("payment_postcode")+".");
                        phone.setText(object.getString("telephone"));
                        del_add_lay.setVisibility(View.VISIBLE);
                      //  status_img.setVisibility(View.VISIBLE);
                        ship_info.setVisibility(View.VISIBLE);
                        ship_method_sec.setVisibility(View.VISIBLE);
                    }

                    JSONArray arr = new JSONArray(object.getString("products"));
                    Log.d("Order_size", String.valueOf(arr.length()));

                    for (int h = 0; h < arr.length(); h++) {
                        JSONObject obj = arr.getJSONObject(h);
                        PlacePO bo = new PlacePO();
                        bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                        bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                        bo.setOrder_id(obj.isNull("order_product_id") ? "" : obj.getString("order_product_id"));
                        bo.setModel(obj.isNull("model") ? "" : obj.getString("model"));
                        bo.setQty(obj.isNull("quantity") ? "" : obj.getString("quantity"));
                        bo.setPrcie(obj.isNull("price_raw") ? "" : obj.getString("price_raw"));
                        bo.setAmout(obj.isNull("total_raw") ? "" : obj.getString("total_raw"));
                        bo.setUrl(obj.isNull("image") ? "" : obj.getString("image"));
                        JSONArray option_array = new JSONArray(obj.getString("option"));
                        for (int y = 0; y < option_array.length(); y++) {
                            JSONObject obj1 = arr.getJSONObject(h);
                            PlacePO bo1 = new PlacePO();
                            bo1.setProduct_name(obj1.isNull("name") ? "" : obj1.getString("name"));
                            bo1.setProduct_option_id(obj1.isNull("product_option_id") ? 0 : obj1.getInt("product_option_id"));
                            bo1.setOption_id(obj1.isNull("option_id") ? 0 : obj1.getInt("option_id"));
                            bo1.setType(obj1.isNull("type") ? "" : obj1.getString("type"));
                            bo1.setRequired(obj1.isNull("required") ? "" : obj1.getString("required"));
                            list2.add(bo1);
                        }

                        ArrayList<OptionsPO> oplist = new ArrayList<OptionsPO>();
                        for (int u = 0; u < option_array.length(); u++) {
                            JSONObject ob = option_array.getJSONObject(u);
                            OptionsPO boo = new OptionsPO();
                            boo.setName(ob.isNull("name") ? "" : ob.getString("name"));
                            boo.setValue(ob.isNull("value") ? "" : ob.getString("value"));
                            oplist.add(boo);
                        }
                        bo.setOptionlist(oplist);
                        bo.setValuelists(list2);
                        list1.add(bo);
                    }
                    view_list.removeAllViews();
                    for (int i = 0; i < list1.size(); i++) {

                        addLayout(list1.get(i), view_list);

                    }
                    JSONArray totalarray = new JSONArray(object.getString("totals"));
                    for (int k = 0; k < totalarray.length(); k++) {
                        JSONObject obj = totalarray.getJSONObject(k);
                        addTotals(obj, ordertotview);
                    }
                    loading.setVisibility(View.GONE);
                    no_network.setVisibility(View.GONE);
                    }
                    else
                    {
                        loading.setVisibility(View.GONE);
                        no_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(ViewDetails.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    OrderTask task = new OrderTask();
                                    task.execute(Appconstatants.myorder_api + "&id=" + bun.getString("id"));
                                }
                            })
                            .show();


                }

            } else {
                no_network.setVisibility(View.VISIBLE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                loading.setVisibility(View.GONE);
            }
        }
    }

    private void addLayout(final PlacePO placePO, LinearLayout view_list) {

        View convertView = LayoutInflater.from(this).inflate(R.layout.place_list, view_list, false);
        TextView name = (TextView) convertView.findViewById(R.id.p_name);
        ImageView imag = (ImageView) convertView.findViewById(R.id.place_img);
        TextView price = (TextView) convertView.findViewById(R.id.p_total);
        TextView qunt = (TextView) convertView.findViewById(R.id.p_qty);
        TextView amount = (TextView) convertView.findViewById(R.id.total);
        TextView p_option=(TextView)convertView.findViewById(R.id.p_option);

        TextView cur_left1=(TextView)convertView.findViewById(R.id.cur_left);
        TextView cur_right1=(TextView)convertView.findViewById(R.id.cur_right);
        TextView cur_left2=(TextView)convertView.findViewById(R.id.cur_left1);
        TextView cur_right2=(TextView)convertView.findViewById(R.id.cur_right1);
        cur_left1.setText(cur_left);
        cur_left2.setText(cur_left);
        cur_right1.setText(cur_right);
        cur_right2.setText(cur_right);
        name.setText(placePO.getProduct_name());
        double a = Double.parseDouble(placePO.getPrcie());
        double b = Double.parseDouble(placePO.getAmout());
        price.setText(String.format("%.2f", a));
        qunt.setText(placePO.getQty());
        amount.setText(String.format("%.2f", b));
        Log.d("images_s", placePO.getUrl() + "");

        StringBuilder sb2 = new StringBuilder();

        for (int h = 0; h < placePO.getOptionlist().size(); h++) {
            Log.d("option_list", placePO.getOptionlist().get(h).getValue() + "");
            sb2.append(placePO.getOptionlist().get(h).getValue());
            if (h != placePO.getOptionlist().size() - 1)
                sb2.append(",");
        }
        Log.d("option_list", String.valueOf(sb2));
        if (sb2.length()>0)
        {
            p_option.setVisibility(View.VISIBLE);
        }
        else
        {
            p_option.setVisibility(View.GONE);
        }
        p_option.setText(String.valueOf(sb2));

        if (placePO.getUrl().length() > 0 && placePO.getUrl() != null)
            Picasso.with(ViewDetails.this).load(placePO.getUrl()).placeholder(R.mipmap.place_holder).into(imag);

        view_list.addView(convertView);

    }


    private void addTotals(final JSONObject obj, LinearLayout view_list) {

        View convertView = LayoutInflater.from(this).inflate(R.layout.orders_totals, view_list, false);
        TextView tot_title = (TextView) convertView.findViewById(R.id.amount_title);
        TextView tot_value = (TextView) convertView.findViewById(R.id.amount_value);
        TextView rigth_cur=(TextView)convertView.findViewById(R.id.cur_right);
        TextView left_cur=(TextView)convertView.findViewById(R.id.cur_left);
        rigth_cur.setText(cur_right);
        left_cur.setText(cur_left);
        try {
            tot_title.setText(obj.isNull("title") ? "" : obj.getString("title")+" :");
            double tot = Double.parseDouble(obj.isNull("value") ? "0.00" : obj.getString("value"));
            tot_value.setText(String.format("%.2f", tot));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        view_list.addView(convertView);

    }


}
