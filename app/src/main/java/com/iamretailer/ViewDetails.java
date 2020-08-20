package com.iamretailer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.CommonAdapter;
import com.iamretailer.Adapter.TrackingAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.OptionsPO;
import com.iamretailer.POJO.PlacePO;



import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import stutzen.co.network.Connection;

public class ViewDetails extends Language {
    Bundle bun;
    private LinearLayout view_list;

    private FrameLayout no_network;
    private TextView order_date;
    private TextView ship_method;
    private TextView paymethod;
    private LinearLayout ordertotview;
    private FrameLayout loading;
    private FrameLayout fullayout;
    private TextView errortxt1;
    private TextView errortxt2;
    private String cur_left = "";
    private String cur_right = "";
    LinearLayout loading_bar;
    private AndroidLogger logger;
    private LinearLayout del_add_lay;
    private LinearLayout ship_info;
    private LinearLayout ship_method_sec;
    private LinearLayout tracking;
    private TextView phone;
    private TextView cus_name;
    private TextView cus_address_one;
    private TextView cus_address_two;
    private TextView country_name;
    private View delivery;
    private View shipping;
    private TextView del;
    private ArrayList<PlacePO> list3;
    private LinearLayout delete;
    private RecyclerView horizontalListView;
    private TrackingAdapter featuredProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        CommonFunctions.updateAndroidSecurityProvider(this);
        DBController db = new DBController(ViewDetails.this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang= db.get_lang_code();
        Appconstatants.CUR= db.getCurCode();
        cur_left = db.get_cur_Left();
        cur_right= db.get_cur_Right();
        bun = new Bundle();
        bun = getIntent().getExtras();
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        int maxHeight= (int) (height-(height*0.80));
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ViewDetails.this);
        View sheetView = ViewDetails.this.getLayoutInflater().inflate(R.layout.track, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        FrameLayout bottomSheet = mBottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setPeekHeight(height);
        del=(TextView)mBottomSheetDialog.findViewById(R.id.del);
        horizontalListView =(RecyclerView) mBottomSheetDialog. findViewById(R.id.track_list);
        delete=(LinearLayout)mBottomSheetDialog.findViewById(R.id.delete);
        delete.setPadding(0,maxHeight,0,0);
        String s1=getResources().getString( R.string.del_by);
        String s2=getResources().getString(R.string.app_name);
        String s3=s1+" "+s2;
        del.setText(s3);
        view_list = findViewById(R.id.listview);
        no_network = findViewById(R.id.error_network);
        LinearLayout retry = findViewById(R.id.retry);
        TextView order_id = findViewById(R.id.order_no);
        order_date = findViewById(R.id.order_date);
        ship_method = findViewById(R.id.ship_method);
        ordertotview = findViewById(R.id.ordertot);
        FrameLayout calls = findViewById(R.id.call);
        paymethod = findViewById(R.id.paymethod);
        TextView status = findViewById(R.id.status);
        TextView header = findViewById(R.id.header);
        loading= findViewById(R.id.loading);
        fullayout= findViewById(R.id.fullayout);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        loading_bar= findViewById(R.id.loading_bar);
        del_add_lay= findViewById(R.id.del_add_lay);
        ship_info= findViewById(R.id.ship_info);
        ship_method_sec= findViewById(R.id.ship_method_sec);
        phone = findViewById(R.id.phone);
        cus_name = findViewById(R.id.name);
        cus_address_one = findViewById(R.id.address_one);
        cus_address_two = findViewById(R.id.address_two);
        country_name = findViewById(R.id.country);
        tracking = findViewById(R.id.tracking);

        ImageView del_image = findViewById(R.id.del_image);
        delivery= findViewById(R.id.delivery);
        shipping= findViewById(R.id.shipping);
        String head=getResources().getString(R.string.order_ids)+bun.getString("id");
        header.setText(head);
        OrderTask task = new OrderTask();
        task.execute(Appconstatants.myorder_api + "&id=" + bun.getString("id"));
        if(bun!=null){
            String val=bun.getString("status");
            if(val!=null){

                if (val.toLowerCase().equals("placed"))
                    del_image.setImageResource(R.mipmap.placed);
                else
                    del_image.setImageResource(R.mipmap.pending);

            }


            status.setText(bun.getString("status"));
            String or_id="#"+bun.getString("id");
            order_id.setText(or_id);
        }


        LinearLayout back = findViewById(R.id.menu);
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
        tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               
                mBottomSheetDialog.show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBottomSheetDialog.dismiss();
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
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);

    }

    private void showCallPopup() {
        AlertDialog.Builder dial = new AlertDialog.Builder(ViewDetails.this);
        View popUpView = View.inflate(ViewDetails.this,R.layout.call_popup, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();
        final TextView no = popUpView.findViewById(R.id.no);
        final TextView yes = popUpView.findViewById(R.id.yes);

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

    private void call_action(){

        String ph=getResources().getString(R.string.tel)+getResources().getString(R.string.phone_num);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(ph));
        startActivity(callIntent);
    }


    private boolean isPermissionGranted() {
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
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.per_grant), Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.per_den), Toast.LENGTH_SHORT).show();
                }
                break;
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
                    ArrayList<PlacePO> list1 = new ArrayList<>();
                    ArrayList<PlacePO> list2 = new ArrayList<>();
                     list3 = new ArrayList<>();
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
                        ship_info.setVisibility(View.GONE);
                        ship_method_sec.setVisibility(View.GONE);
                        delivery.setVisibility(View.GONE);
                        shipping.setVisibility(View.GONE);
                    }
                    else
                    {
                        String c_name=object.isNull("payment_firstname") ? "" : object.getString("payment_firstname") + " " + object.getString("payment_lastname");
                        String c_ad1=object.getString("payment_address_1") + ", " + object.getString("payment_address_2")+",";
                        String c_ad2=object.getString("payment_city") + ", "+object.getString("payment_zone")+",";
                        String c_country=object.getString("payment_country")+" - "+object.getString("payment_postcode")+".";

                        cus_name.setText(c_name );
                        cus_address_one.setText(c_ad1);
                        cus_address_two.setText(c_ad2);
                        country_name.setText(c_country);
                        phone.setText(object.getString("telephone"));
                        del_add_lay.setVisibility(View.VISIBLE);
                        ship_info.setVisibility(View.VISIBLE);
                        if(object.isNull("shipping_method")||object.getString("shipping_method").equalsIgnoreCase("")){
                            ship_method_sec.setVisibility(View.GONE);
                        }else{
                            ship_method_sec.setVisibility(View.VISIBLE);
                        }

                        delivery.setVisibility(View.VISIBLE);
                        shipping.setVisibility(View.VISIBLE);
                    }

                    JSONArray arr = new JSONArray(object.getString("products"));

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

                        ArrayList<OptionsPO> oplist = new ArrayList<>();
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

                        JSONArray arr1 = new JSONArray(object.getString("histories"));

                        for (int h = 0; h < arr1.length(); h++) {
                            JSONObject obj = arr1.getJSONObject(h);
                            PlacePO bo2 = new PlacePO();
                           // bo2.setDate(obj.isNull("date_added") ? "" : obj.getString("date_added"));
                            DateFormat originalFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            DateFormat targetFormat1 = new SimpleDateFormat("dd, MMM yyyy", Locale.ENGLISH);
                            Date dat1 = null;
                            if (!obj.isNull("date_added")) {
                                try {
                                    dat1 = originalFormat1.parse(obj.getString("date_added"));
                                    String formattedDate1 = targetFormat1.format(dat1);
                                    bo2.setDate(formattedDate1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                bo2.setDate("");
                            }
                            bo2.setCommand(obj.isNull("comment") ? "" : obj.getString("comment"));
                            bo2.setStatus(obj.isNull("status") ? "" : obj.getString("status"));
                            list3.add(bo2);
                        }
                        Collections.reverse(list3);
                        featuredProduct = new TrackingAdapter(ViewDetails.this, list3);
                        horizontalListView.setAdapter(featuredProduct);
                        horizontalListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                    loading.setVisibility(View.GONE);
                    no_network.setVisibility(View.GONE);
                    }
                    else
                    {
                        loading.setVisibility(View.GONE);
                        no_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        String error=array.getString(0)+"";
                        errortxt2.setText(error);
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
        TextView name = convertView.findViewById(R.id.p_name);
        ImageView imag = convertView.findViewById(R.id.place_img);
        TextView price = convertView.findViewById(R.id.p_total);
        TextView qunt = convertView.findViewById(R.id.p_qty);
        TextView amount = convertView.findViewById(R.id.total);
        TextView p_option= convertView.findViewById(R.id.p_option);

        TextView cur_left1= convertView.findViewById(R.id.cur_left);
        TextView cur_right1= convertView.findViewById(R.id.cur_right);
        TextView cur_left2= convertView.findViewById(R.id.cur_left1);
        TextView cur_right2= convertView.findViewById(R.id.cur_right1);
        cur_left1.setText(cur_left);
        cur_left2.setText(cur_left);
        cur_right1.setText(cur_right);
        cur_right2.setText(cur_right);
        name.setText(placePO.getProduct_name());
        double a = Double.parseDouble(placePO.getPrcie());
        double b = Double.parseDouble(placePO.getAmout());
        String a1=String.format(Locale.ENGLISH,"%.2f", a);
        String b1=String.format(Locale.ENGLISH,"%.2f", b);
        price.setText(a1);
        qunt.setText(placePO.getQty());
        amount.setText(b1);

        StringBuilder sb2 = new StringBuilder();

        for (int h = 0; h < placePO.getOptionlist().size(); h++) {
            sb2.append(placePO.getOptionlist().get(h).getValue());
            if (h != placePO.getOptionlist().size() - 1)
                sb2.append(",");
        }
        if (sb2.length()>0)
        {
            p_option.setVisibility(View.VISIBLE);
        }
        else
        {
            p_option.setVisibility(View.GONE);
        }
        p_option.setText(String.valueOf(sb2));

        if (placePO.getUrl() != null && placePO.getUrl().length() > 0  )
            Picasso.with(ViewDetails.this).load(placePO.getUrl()).placeholder(R.mipmap.place_holder).into(imag);

        view_list.addView(convertView);

    }


    private void addTotals(final JSONObject obj, LinearLayout view_list) {

        View convertView = LayoutInflater.from(this).inflate(R.layout.orders_totals, view_list, false);
        TextView tot_title = convertView.findViewById(R.id.amount_title);
        TextView tot_value = convertView.findViewById(R.id.amount_value);
        TextView rigth_cur= convertView.findViewById(R.id.cur_right);
        TextView left_cur= convertView.findViewById(R.id.cur_left);
        rigth_cur.setText(cur_right);
        left_cur.setText(cur_left);
        try {
            tot_title.setText(obj.isNull("title") ? "" : obj.getString("title")+" :");
            double tot = Double.parseDouble(obj.isNull("value") ? "0.00" : obj.getString("value"));
            String val=String.format(Locale.ENGLISH,"%.2f", tot);
            tot_value.setText(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        view_list.addView(convertView);

    }


}
