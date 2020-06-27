package com.iamretailer.krishnasupermarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.krishnasupermarket.Adapter.CartAdapter;
import com.iamretailer.krishnasupermarket.Adapter.CountryAdapter;
import com.iamretailer.krishnasupermarket.Adapter.StateAdapter;
import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.CommonFunctions;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.POJO.AddressPO;
import com.iamretailer.krishnasupermarket.POJO.CountryPO;
import com.iamretailer.krishnasupermarket.POJO.OptionsPO;
import com.iamretailer.krishnasupermarket.POJO.ProductsPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import stutzen.co.network.Connection;

public class MyCart extends Language {

    public ListView cart_list;
    CartAdapter adapter;
    FrameLayout checkout;
    DBController db;
    ArrayList<ProductsPO> list;
    public LinearLayout checkoutview;
    public LinearLayout shopnow;
    public LinearLayout button;
    double sum = 0;
    TextView subtotal;
    LinearLayout menu;
    public FrameLayout error_network;
    public LinearLayout retry;
    private FrameLayout success;
    FrameLayout loading;
    LinearLayout cart_items;
    TextView header;
    FrameLayout fullayout;

    TextView errortxt1, errortxt2;
    String cur_left = "";
    String cur_right = "";
    int out_of_stock=0;
    LinearLayout loading_bar;
    AndroidLogger logger;
    private ArrayList<Object> addressPOS;
    AlertDialog alertReviewDialog;
    LinearLayout shipping;
    private ArrayList<CountryPO> country_list;
    private ArrayList<CountryPO> state_list;
    private CountryAdapter c_adapter;
    private StateAdapter s_adapter;
    Spinner country;
    Spinner state;
    EditText pin_code;
    FrameLayout calculate;
    AlertDialog show_amount;
    int has_shopp;
    TextView rupee_front,rupee_back;
    TextView cart_count;

    @Override
    protected void onResume() {
        super.onResume();
        CartTaskCount cartTask = new CartTaskCount();
        cartTask.execute(Appconstatants.cart_api);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        CommonFunctions.updateAndroidSecurityProvider(this);
        db = new DBController(getApplicationContext());
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        success = (FrameLayout) findViewById(R.id.success);
        cart_list = (ListView) findViewById(R.id.cart_list);
        checkoutview = (LinearLayout) findViewById(R.id.checkoutview);
        subtotal = (TextView) findViewById(R.id.subtotal);
        shopnow = (LinearLayout) findViewById(R.id.shopnow);
        button = (LinearLayout) findViewById(R.id.home);
        menu = (LinearLayout) findViewById(R.id.menu);
        error_network = (FrameLayout) findViewById(R.id.error_network);
        retry = (LinearLayout) findViewById(R.id.retry);
        loading=(FrameLayout)findViewById(R.id.loading);
        cart_items=(LinearLayout)findViewById(R.id.cart_items);
        header=(TextView)findViewById(R.id.header);
        header.setText(R.string.my_Cart);
        //   cart_items.setVisibility(View.GONE);
        fullayout=(FrameLayout)findViewById(R.id.fullayout);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        shipping=(LinearLayout)findViewById(R.id.shipping);
        rupee_front=(TextView)findViewById(R.id.rupee_front);
        rupee_back=(TextView)findViewById(R.id.rupee_back);
        cart_count=(TextView)findViewById(R.id.cart_count);



        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang=db.get_lang_code();
        Appconstatants.CUR=db.getCurCode();
        list = new ArrayList<ProductsPO>();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);

        success.setVisibility(View.GONE);
        shopnow.setVisibility(View.GONE);
        cur_left = db.get_cur_Left();
        cur_right=db.get_cur_Right();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MyCart.this, MainActivity.class));

            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                success.setVisibility(View.GONE);
                error_network.setVisibility(View.GONE);
                shopnow.setVisibility(View.GONE);

                CartTask cartTask = new CartTask();
                cartTask.execute(Appconstatants.cart_api);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        checkout = (FrameLayout) findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("values__chec1",out_of_stock+"");
                for (int j=0;j<list.size();j++)
                {
                    Log.d("values__chec1",out_of_stock+"");
                    if (!list.get(j).isOut_of_stock())
                        out_of_stock++;
                    Log.d("values__chec2",out_of_stock+"");
                }

                if (out_of_stock==0) {

                    if (db.getLoginCount() > 0) {
                        AddressListTask addressListTask = new AddressListTask();
                        addressListTask.execute();


                    } else {
                        Intent i = new Intent(MyCart.this, Login.class);
                        i.putExtra("from", 2);
                        i.putExtra("list", list);
                        i.putExtra("has_ship",has_shopp);
                        startActivityForResult(i,3);
                    }

                }
                else
                {
                    Log.d("values__chec3",out_of_stock+"");
                    out_of_stock=0;
                    Toast.makeText(MyCart.this,R.string.out_stock,Toast.LENGTH_SHORT).show();
                }
            }
        });
        cart_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyCart.this, ProductFullView.class);
                Bundle mm=new Bundle();
                mm.putString("productid", list.get(i).getProduct_id());
                intent.putExtras(mm);
                startActivity(intent);

            }
        });

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyCart.this);
        View dialogView = getLayoutInflater().inflate(R.layout.ship_cal, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.create();
        alertReviewDialog=dialogBuilder.create();
        calculate=(FrameLayout)dialogView.findViewById(R.id.calculate);
        country=(Spinner)dialogView.findViewById(R.id.country);
        state=(Spinner)dialogView.findViewById(R.id.state);
        pin_code=(EditText)dialogView.findViewById(R.id.pin_code);



        shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Cal_ship(getLayoutInflater());
            }
        });

        CountryTask countryTask = new CountryTask();
        countryTask.execute();


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && data != null) {
            loading.setVisibility(View.VISIBLE);
            success.setVisibility(View.GONE);
            error_network.setVisibility(View.GONE);
            shopnow.setVisibility(View.GONE);
            CartTask cartTask = new CartTask();
            cartTask.execute(Appconstatants.cart_api);
        }
    }

    public void CartCall() {
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }

    public void sumtotal() {
        sum = 0;
        for (int h = 0; h < list.size(); h++) {
            sum = sum + (list.get(h).getTotal());
        }
        rupee_front.setText(cur_left);
        rupee_back.setText(cur_right);
        subtotal.setText(String.format("%.2f", sum));
    }


    private class AddressListTask extends AsyncTask<Object, Void, String> {
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(MyCart.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("Add_list", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("Add_list_api" + Appconstatants.addres_list);

            String response = null;
            Connection connection = new Connection();
            try {
                Log.d("Add_list_api", Appconstatants.addres_list);
                Log.d("Add_list_api", Appconstatants.sessiondata);

                response = connection.connStringResponse(Appconstatants.addres_list, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyCart.this);
                logger.info("Add_list_resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Add_list", "Add_list--->  " + resp);
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    addressPOS = new ArrayList<>();
                    if (json.getInt("success") == 1) {

                        JSONObject object = json.getJSONObject("data");
                        JSONArray array = object.getJSONArray("addresses");
                        if (array.length() > 0) {
                            for (int y = 0; y < array.length(); y++) {
                                JSONObject object1 = array.getJSONObject(y);
                                AddressPO addressPO = new AddressPO();
                                addressPO.setAdd_id(object1.isNull("address_id") ? "" : object1.getString("address_id"));
                                addressPO.setF_name(object1.isNull("firstname") ? "" : object1.getString("firstname"));
                                addressPO.setL_name(object1.isNull("lastname") ? "" : object1.getString("lastname"));
                                addressPO.setPhone(object1.isNull("phone") ? "" : object1.getString("phone"));
                                addressPO.setCompany(object1.isNull("company") ? "" : object1.getString("company"));
                                addressPO.setAdd_1(object1.isNull("address_1") ? "" : object1.getString("address_1"));
                                addressPO.setAdd_2(object1.isNull("address_2") ? "" : object1.getString("address_2"));
                                addressPO.setPost_code(object1.isNull("postcode") ? "" : object1.getString("postcode"));
                                addressPO.setCity(object1.isNull("city") ? "" : object1.getString("city"));
                                addressPO.setZone(object1.isNull("zone") ? "" : object1.getString("zone"));
                                addressPO.setCountry(object1.isNull("country") ? "" : object1.getString("country"));
                                if (object1.getString("address_1").length()>0)
                                    addressPOS.add(addressPO);

                            }

                            if (addressPOS.size()>0) {

                                Intent i = new Intent(MyCart.this, AddressList.class);
                                Bundle nn = new Bundle();
                                nn.putInt("from", 1);
                                nn.putInt("has_ship", has_shopp);
                                i.putExtras(nn);
                                startActivity(i);
                            }
                            else
                            {
                                Intent intent = new Intent(MyCart.this, Address.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("from", 2);
                                bundle.putInt("has_ship",has_shopp);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }


                        } else {
                            Intent intent = new Intent(MyCart.this, Address.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("from", 2);
                            bundle.putInt("has_ship",has_shopp);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);


                    } else {
                        success.setVisibility(View.GONE);
                        shopnow.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(MyCart.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    success.setVisibility(View.GONE);
                    shopnow.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    CartTask cartTask = new CartTask();
                                    cartTask.execute(Appconstatants.cart_api);
                                }
                            })
                            .show();

                }
            } else {
                success.setVisibility(View.GONE);
                shopnow.setVisibility(View.GONE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

        }
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
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyCart.this);
                logger.info("Cart resp"+response);
                Log.d("Cart_list_resp", response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Cart_list_resp", "CartResp--->  " + resp);

            if (resp != null) {
                list = new ArrayList<ProductsPO>();
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");


                        if (dd instanceof JSONArray) {

                            cart_list.setVisibility(View.GONE);
                            checkoutview.setVisibility(View.GONE);
                            error_network.setVisibility(View.GONE);
                            shopnow.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            cart_count.setText(0 +"");
                        } else if (dd instanceof JSONObject) {
                            // It's an object
                            JSONObject jsonObject = (JSONObject) dd;

                            has_shopp=jsonObject.isNull("has_shipping")?0:jsonObject.getInt("has_shipping");

                            JSONArray array = new JSONArray(jsonObject.getString("products"));

                            if (array.length() == 0) {

                                cart_list.setVisibility(View.GONE);
                                checkoutview.setVisibility(View.GONE);
                                error_network.setVisibility(View.GONE);
                                shopnow.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);

                            } else {
                                Log.d("size", String.valueOf(array.length()));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject1 = array.getJSONObject(i);
                                    JSONArray jarray = new JSONArray(jsonObject1.getString("option"));
                                    ProductsPO po = new ProductsPO();
                                    po.setKey(jsonObject1.isNull("key") ? "" : jsonObject1.getString("key"));
                                    po.setProducturl(jsonObject1.isNull("thumb") ? "" : jsonObject1.getString("thumb"));
                                    po.setProduct_name(jsonObject1.isNull("name") ? "" : jsonObject1.getString("name"));
                                    po.setProduct_id(jsonObject1.isNull("product_id") ? "" : jsonObject1.getString("product_id"));
                                    po.setCartvalue(Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                                    po.setProd_offer_rate(jsonObject1.getDouble("price_raw"));
                                    po.setTotal(jsonObject1.getDouble("total_raw"));
                                    po.setOut_of_stock(jsonObject1.getBoolean("stock"));
                                    Log.d("Stock_values",jsonObject1.getBoolean("stock")+"");


                                    ArrayList<OptionsPO> oplist = new ArrayList<OptionsPO>();
                                    for (int u = 0; u < jarray.length(); u++) {
                                        JSONObject ob = jarray.getJSONObject(u);
                                        OptionsPO boo = new OptionsPO();
                                        boo.setName(ob.isNull("name") ? "" : ob.getString("name"));
                                        boo.setValue(ob.isNull("value") ? "" : ob.getString("value"));
                                        oplist.add(boo);
                                    }
                                    po.setOptionlist(oplist);
                                    list.add(po);
                                }
                                int qty = 0;
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject1 = array.getJSONObject(i);
                                    ProductsPO bo = new ProductsPO();
                                    bo.setProduct_id(jsonObject1.isNull("product_id") ? "" : jsonObject1.getString("product_id"));
                                    qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                                }
                                cart_count.setText(qty+"");
                                adapter = new CartAdapter(MyCart.this, R.layout.cart_list_copy, list);
                                cart_list.setAdapter(adapter);
                                success.setVisibility(View.VISIBLE);
                                shopnow.setVisibility(View.GONE);
                                error_network.setVisibility(View.GONE);
                                loading.setVisibility(View.GONE);
                                Log.d("size", String.valueOf(list.size()));
                                sumtotal();
                            }

                        }

                    } else {
                        success.setVisibility(View.GONE);
                        shopnow.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(MyCart.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    success.setVisibility(View.GONE);
                    shopnow.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    CartTask cartTask = new CartTask();
                                    cartTask.execute(Appconstatants.cart_api);
                                }
                            })
                            .show();

                }
            } else {
                success.setVisibility(View.GONE);
                shopnow.setVisibility(View.GONE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

        }
    }

    private class CartTaskCount extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Cart api"+param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyCart.this);
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


                        } else if (dd instanceof JSONObject) {
                            // It's an object

                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));
                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                        }
                    }
                    else
                    {
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(MyCart.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }


    public void show_Cal_ship(LayoutInflater fa)
    {
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i!=0) {
                    StateTask stateTask = new StateTask();
                    stateTask.execute(Appconstatants.country_list_api + "&id=" + country_list.get(i).getCount_id());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (country.getSelectedItemPosition()>0 && state.getSelectedItemPosition()>0 && pin_code.getText().toString().length()>0)
                {
                    CalcTask calcTask=new CalcTask();
                    calcTask.execute(country_list.get(country.getSelectedItemPosition()).getCount_id()+"",state_list.get(state.getSelectedItemPosition()).getZone_id(),pin_code.getText().toString());
                }
                else {
                    if (country.getSelectedItemPosition()==0)
                    {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.select_coun),Toast.LENGTH_LONG).show();
                    }
                    if (state.getSelectedItemPosition()==0)
                    {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.select_state),Toast.LENGTH_LONG).show();
                    }
                    if (pin_code.getText().toString().trim().length()==0)
                    {
                        pin_code.setError(getResources().getString(R.string.oin_error));
                    }
                }

            }
        });

        alertReviewDialog.show();
    }

    private class CountryTask extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Country", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("Country list api"+Appconstatants.country_list_api);
            String response = null;
            Connection connection = new Connection();
            try {
                response = connection.connStringResponse(Appconstatants.country_list_api, null, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyCart.this);
                logger.info("Country list api resp"+response);

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
                    country_list = new ArrayList<CountryPO>();
                    CountryPO po1 = new CountryPO();
                    po1.setCount_id(0);
                    po1.setCount_name("Select Country");
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

                        c_adapter = new CountryAdapter(MyCart.this, R.layout.country_row, country_list);
                        country.setAdapter(c_adapter);


                        StateTask stateTask = new StateTask();
                        stateTask.execute(Appconstatants.country_list_api + "&id="+country_list.get(1).getCount_id());


                    } else {
                        error_network.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.get(0)+"");
                        Toast.makeText(MyCart.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    error_network.setVisibility(View.GONE);
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
            logger.info("State list api"+param[0]);

            String response = null;
            Connection connection = new Connection();
            try {

                response = connection.connStringResponse(param[0], null, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyCart.this);
                logger.info("State list api resp"+response);

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
                    state_list = new ArrayList<CountryPO>();
                    CountryPO po = new CountryPO();
                    po.setZone_id("0");
                    po.setCont_id("0");
                    po.setCount_name("Select State");
                    po.setCount_iso_code_2("");
                    po.setCount_iso_code_3("");
                    state_list.add(po);

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
                        s_adapter = new StateAdapter(MyCart.this, R.layout.country_row, state_list);
                        state.setAdapter(s_adapter);


                        error_network.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);


                    } else {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.get(0)+"");
                        Toast.makeText(MyCart.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    error_network.setVisibility(View.GONE);
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
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

        }
    }

    private class CalcTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            Log.d("Cal", "started");


            pDialog = new ProgressDialog(MyCart.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {
            logger.info("Cal api" + Appconstatants.login_api);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("country_id", param[0]);
                json.put("zone_id", param[1]);
                json.put("postcode", param[2]);
                Log.d("cal_format", json.toString());
                Log.d("cal_Api", Appconstatants.CAL_SHIP);

                logger.info("cal_Apireq" + json);
                response = connection.sendHttpPostjson(Appconstatants.CAL_SHIP, json, db.getSession(), Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,MyCart.this);
                logger.info("cal_Api resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("cal_Api", "cal_Api--?" + resp);
            Log.d("cal_Api", resp + "");
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONObject jsonObject = new JSONObject(json.getString("data"));
                        pin_code.setText("");
                        country.setSelection(0);
                        state.setSelection(0);
                        alertReviewDialog.dismiss();

                        show_Cal_ship_amount(jsonObject);

                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(MyCart.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    calculate.performClick();

                                }
                            })
                            .show();
                }

            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                calculate.performClick();
                            }
                        })
                        .show();
            }
        }
    }

    public void show_Cal_ship_amount(JSONObject jsonObject)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyCart.this);
        View dialogView = getLayoutInflater().inflate(R.layout.ship_cal_amt, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.create();
        dialogBuilder.setCancelable(false);
        show_amount=dialogBuilder.create();
        String cost="";

        LinearLayout ship_layer=(LinearLayout)dialogView.findViewById(R.id.ship_layer);
        FrameLayout okay=(FrameLayout)dialogView.findViewById(R.id.okay);
        ship_layer.removeAllViews();
        Log.d("sample_check",jsonObject.toString()+"");
        try {

            JSONObject ship_meth=jsonObject.getJSONObject("shipping_method");

            Iterator<String> iter = ship_meth.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONObject object=ship_meth.getJSONObject(key);
                    String title=object.isNull("title")?"":object.getString("title");
                    JSONObject object1 =object.getJSONObject("quote");
                    Iterator<String> iter1 = object1.keys();
                    while (iter1.hasNext())
                    {
                        String key1 = iter1.next();
                        JSONObject quotes_key=object1.getJSONObject(key1);
                        cost=quotes_key.isNull("text")?"":quotes_key.getString("text");
                    }
                    add_layer(ship_layer,title,cost);
                } catch (Exception e) {
                    Log.d("sample_check_ex",e.toString()+"");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_amount.dismiss();
            }
        });
        show_amount.show();
    }


    private void add_layer(LinearLayout shipping_list,String key,String cost) {
        View convertView = LayoutInflater.from(this).inflate(R.layout.ship_item, shipping_list, false);
        TextView ship_title=(TextView)convertView.findViewById(R.id.ship_title);
        TextView ship_cost=(TextView)convertView.findViewById(R.id.ship_cost);
        ship_title.setText(key+": ");
        ship_cost.setText(cost+"");
        shipping_list.addView(convertView);
    }



}
