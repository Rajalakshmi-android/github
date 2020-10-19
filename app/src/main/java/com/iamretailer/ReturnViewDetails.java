package com.iamretailer;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import stutzen.co.network.Connection;

public class ReturnViewDetails extends Language {
    Bundle bun;

    private FrameLayout no_network;
    private TextView order_date;

    private FrameLayout loading;
    private FrameLayout fullayout;
    private FrameLayout success;
    private TextView errortxt1;
    private TextView errortxt2;
    private String cur_left = "";
    private String cur_right = "";

    private AndroidLogger logger;
    private LinearLayout del_add_lay;
    private LinearLayout ship_info;

    private LinearLayout tracking;
    private TextView phone;
    private TextView cus_name;
    private TextView del;
    private ArrayList<PlacePO> list3;
    private LinearLayout delete;
    private RecyclerView horizontalListView;
    private TrackingAdapter featuredProduct;
    private String order_ids="";
    private String order_dated="";
    private TextView email,product_name,model,reason,open,comment,return_id;
    private TextView order_id,qty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        CommonFunctions.updateAndroidSecurityProvider(this);
        DBController db = new DBController(ReturnViewDetails.this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang= db.get_lang_code();
        Appconstatants.CUR= db.getCurCode();
        cur_left = db.get_cur_Left();
        cur_right= db.get_cur_Right();
        bun = new Bundle();
        bun = getIntent().getExtras();
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        int maxHeight= (int) (height-(height*0.80));
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ReturnViewDetails.this);
        View sheetView = ReturnViewDetails.this.getLayoutInflater().inflate(R.layout.track, null);
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

        no_network = findViewById(R.id.error_network);
        LinearLayout retry = findViewById(R.id.retry);
         order_id = findViewById(R.id.order_no);
        order_date = findViewById(R.id.order_date);
        FrameLayout calls = findViewById(R.id.call);
        TextView header = findViewById(R.id.header);
        loading= findViewById(R.id.loading);
        fullayout= findViewById(R.id.fullayout);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        del_add_lay= findViewById(R.id.del_add_lay);
        phone = findViewById(R.id.phone);
        cus_name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        product_name = findViewById(R.id.product_name);
        model = findViewById(R.id.model);
        reason = findViewById(R.id.reason);
        open = findViewById(R.id.open);
        comment = findViewById(R.id.comment);
        return_id = findViewById(R.id.return_id);
        qty = findViewById(R.id.qty);

        tracking = findViewById(R.id.tracking);
        success = findViewById(R.id.success);

        String head=getResources().getString(R.string.order_ids)+bun.getString("id");
        order_ids=bun.getString("id");
        header.setText(head);
        OrderTask task = new OrderTask();
        task.execute(Appconstatants.Return_Detail + "&id=" + bun.getString("id"));
        if(bun!=null){
            String or_id="#"+bun.getString("id");
            return_id.setText(or_id);
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
                success.setVisibility(View.GONE);
                OrderTask task = new OrderTask();
                task.execute(Appconstatants.Return_Detail + "&id=" + bun.getString("id"));

            }
        });
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);

    }

    private void showCallPopup() {
        AlertDialog.Builder dial = new AlertDialog.Builder(ReturnViewDetails.this);
        View popUpView = View.inflate(ReturnViewDetails.this,R.layout.call_popup, null);
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
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,ReturnViewDetails.this);
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

                    list3 = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success")==1)
                    {
                        JSONObject object = new JSONObject(json.getString("data"));
                        Log.i("reeee",object.toString());

                        return_id.setText(object.isNull("return_id") ? "" : object.getString("return_id"));
                        order_id.setText(object.isNull("order_id") ? "" : object.getString("order_id"));
                        order_date.setText(object.isNull("date_added") ? "" : object.getString("date_added"));
                        cus_name.setText((object.isNull("firstname") ? "" : object.getString("firstname"))+" "+(object.isNull("lastname") ? "" : object.getString("lastname")));
                        email.setText(object.isNull("email") ? "" : object.getString("email"));
                        phone.setText(object.isNull("telephone") ? "" : object.getString("telephone"));
                        product_name.setText(object.isNull("product") ? "" : object.getString("product"));
                        model.setText(object.isNull("model") ? "" : object.getString("model"));
                        qty.setText(object.isNull("quantity") ? "" : object.getString("quantity"));
                        reason.setText(object.isNull("reason") ? "" : object.getString("reason"));
                        open.setText(object.isNull("opened") ? "" : object.getString("opened"));
                        comment.setText(object.isNull("comment") ? "" : object.getString("comment"));

                        loading.setVisibility(View.GONE);
                        no_network.setVisibility(View.GONE);
                        success.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        loading.setVisibility(View.GONE);
                        success.setVisibility(View.GONE);
                        no_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        String error=array.getString(0)+"";
                        errortxt2.setText(error);
                        Toast.makeText(ReturnViewDetails.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    success.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    OrderTask task = new OrderTask();
                                    task.execute(Appconstatants.Return_Detail + "&id=" + bun.getString("id"));
                                }
                            })
                            .show();


                }

            } else {
                no_network.setVisibility(View.VISIBLE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                loading.setVisibility(View.GONE);
                success.setVisibility(View.GONE);
            }
        }
    }



}
