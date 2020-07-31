package com.iamretailer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.AddressAdapter;
import com.iamretailer.Common.Appconstatants;

import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.AddressPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class AddressList extends Language {

    LinearLayout back;
    FrameLayout loading, error_network;
    LinearLayout  add_new_address;
    AndroidLogger logger;
    String address_id = "";
    FrameLayout fullayout;
    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    LinearLayout retry;
    ArrayList<AddressPO> addressPOS;
    ListView address_list;
    AddressAdapter addressAdapter;
    ProgressDialog pDialog1;
    int from;
    Bundle cc;
    int pos;
    TextView header;
    LinearLayout cart_items;
    FrameLayout cont;
    DBController dbController;
    int has_ship;
    TextView no_address;
    private TextView cart_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        back = (LinearLayout) findViewById(R.id.menu);
        loading = (FrameLayout) findViewById(R.id.loading);
        error_network = (FrameLayout) findViewById(R.id.error_network);
        cont = (FrameLayout) findViewById(R.id.cont);
        add_new_address = (LinearLayout) findViewById(R.id.add_new_address);
        fullayout = (FrameLayout) findViewById(R.id.fullayout);
        loading_bar = (LinearLayout) findViewById(R.id.loading_bar);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        retry = (LinearLayout) findViewById(R.id.retry);
        address_list = (ListView) findViewById(R.id.address_list);
        header=(TextView)findViewById(R.id.header);
        no_address=(TextView)findViewById(R.id.no_address);
        dbController = new DBController(getApplicationContext());

        cart_items=(LinearLayout)findViewById(R.id.cart_items);
        cart_count=(TextView)findViewById(R.id.cart_count);

        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressList.this, MyCart.class));
            }
        });


        cc = new Bundle();
        cc = getIntent().getExtras();
        from=cc.getInt("from");
        has_ship=cc.getInt("has_ship");
        Log.d("asdad",from+"");
        Appconstatants.Lang=dbController.get_lang_code();
        Appconstatants.sessiondata=dbController.getSession();
        Appconstatants.CUR=dbController.getCurCode();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (from!=4)
                {

                for (int u = 0; u < addressPOS.size(); u++) {
                    addressPOS.get(u).setSelect(false);
                }
                addressPOS.get(position).setSelect(true);
                addressAdapter.notifyDataSetChanged();
                address_id = addressPOS.get(position).getAdd_id();
                pos = position;
                }
            }
        });

        if (from==4)
        {
            cont.setVisibility(View.GONE);
            header.setText(R.string.my_add);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT
            );
            params.setMargins(0,  (int) getResources().getDimension(R.dimen.dp10), 0, (int) getResources().getDimension(R.dimen.dp5));
            address_list.setLayoutParams(params);
        }
        else
        {
            header.setText(R.string.del_add);
            cont.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT
            );
            params.setMargins(0, (int) getResources().getDimension(R.dimen.dp10), 0, (int) getResources().getDimension(R.dimen.dp50));
            address_list.setLayoutParams(params);
        }

        AddressListTask addressListTask = new AddressListTask();
        addressListTask.execute();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                AddressListTask addressListTask = new AddressListTask();
                addressListTask.execute();

            }
        });
        add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (from==4)
                {
                    Intent i = new Intent(AddressList.this, Address.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("from", 4);
                    i.putExtras(bundle);
                    startActivityForResult(i, 3);
                }
                else {
                    Intent intent = new Intent(AddressList.this, Address.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("from", 2);
                    bundle.putInt("has_ship",has_ship);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });


        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!address_id.equalsIgnoreCase("") && address_id != null) {

                   if (has_ship==1) {
                       AddressTask addressTask = new AddressTask();
                       addressTask.execute();
                   }
                   else
                   {
                       SaveBillAddress addressTask = new SaveBillAddress();
                       addressTask.execute();
                   }

                } else {
                    Toast.makeText(AddressList.this, R.string.select_addres, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }

    public class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        protected String doInBackground(String... param) {
            logger.info("Cart api:"+param[0]);

            String response = null;
            try {
                Connection connection = new Connection();

                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,AddressList.this);
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

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONArray) {
                            cart_count.setText(0 + "");

                        } else if (dd instanceof JSONObject) {

                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));
                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_count.setText(qty + "");
                        }
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(AddressList.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }

    private class AddressListTask extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Add_list", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("Add_list_api" + Appconstatants.addres_list);

            String response = null;
            Connection connection = new Connection();
            try {
                Log.d("Add_list_api", Appconstatants.addres_list);
                Log.d("Add_list_api", Appconstatants.sessiondata);

                response = connection.connStringResponse(Appconstatants.addres_list, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,AddressList.this);
                logger.info("Add_list_resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
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
                                addressPOS.add(addressPO);

                            }


                            if (from==4)
                            {
                                addressAdapter = new AddressAdapter(AddressList.this, R.layout.address_item, addressPOS,0);
                                address_list.setAdapter(addressAdapter);
                            }
                            else
                            {
                                addressAdapter = new AddressAdapter(AddressList.this, R.layout.address_item, addressPOS,1);
                                address_list.setAdapter(addressAdapter);
                                address_id = addressPOS.get(0).getAdd_id();
                                addressPOS.get(0).setSelect(true);

                            }
                            no_address.setVisibility(View.GONE);
                            address_list.setVisibility(View.VISIBLE);

                        } else {
                            no_address.setVisibility(View.VISIBLE);
                            address_list.setVisibility(View.GONE);
                        }
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);


                    } else {
                        error_network.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        errortxt2.setText(array.get(0) + "");
                        Toast.makeText(AddressList.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && data != null) {
            AddressListTask addressListTask = new AddressListTask();
            addressListTask.execute();

        }
    }


    private class AddressTask extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Add_Save", "started");
            if (from != 3) {
                pDialog1 = new ProgressDialog(AddressList.this);
                pDialog1.setMessage(getResources().getString(R.string.loading_wait));
                pDialog1.setCancelable(false);
                pDialog1.show();
            }
        }

        protected String doInBackground(Object... param) {


            String response = null;
            Connection connection = new Connection();
            try {
                logger.info("Address save old user api" + Appconstatants.address_save + "&existing=1");
                JSONObject object = new JSONObject();
                object.put("address_id", address_id);
                Log.d("Add_req_ex", object.toString() + "");
                logger.info("Address save old user api req" + object);
                response = connection.sendHttpPostjson(Appconstatants.address_save + "&existing=1", object, Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,AddressList.this);
                logger.info("Address save old user api resp" + response);
                Log.d("Add_respex", response);
                //}


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Add_Save", "ADD_save--->  " + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {

                        SaveBillAddress addressTask = new SaveBillAddress();
                        addressTask.execute();

                    } else {
                        if (pDialog1 != null && pDialog1.isShowing())
                            pDialog1.dismiss();
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(AddressList.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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

            if (has_ship!=1)
            {
                pDialog1 = new ProgressDialog(AddressList.this);
                pDialog1.setMessage(getResources().getString(R.string.loading_wait));
                pDialog1.setCancelable(false);
                pDialog1.show();
            }

        }

        protected String doInBackground(Object... param) {

            String response = null;
            Connection connection = new Connection();
            try {
                logger.info("Bill Address save old user api" + Appconstatants.bill_address_save + "&existing=1");
                JSONObject object = new JSONObject();
                object.put("address_id", address_id);
                Log.d("Add_req_existing", object.toString());
                logger.info("Bill Address save old user api req" + object);
                response = connection.sendHttpPostjson(Appconstatants.bill_address_save + "&existing=1", object, Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,AddressList.this);
                Log.d("Add_respex", response);
                logger.info("Bill Address save old user api resp" + response);
                // }


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

                        Intent i;
                        if (has_ship==1) {
                            i = new Intent(AddressList.this, DeliveryMethod.class);
                        }
                        else
                        {
                            i = new Intent(AddressList.this, PaymentMethod.class);
                        }
                        i.putExtra("addres_id", address_id);
                        i.putExtra("fname", addressPOS.get(pos).getF_name());
                        i.putExtra("lname", addressPOS.get(pos).getL_name());
                        i.putExtra("from", from);
                        i.putExtra("company", addressPOS.get(pos).getCompany());
                        i.putExtra("addressone", addressPOS.get(pos).getAdd_1());
                        i.putExtra("addresstwo", addressPOS.get(pos).getAdd_2());
                        i.putExtra("city", addressPOS.get(pos).getCity());
                        i.putExtra("pincode", addressPOS.get(pos).getPost_code());
                        i.putExtra("mobile", addressPOS.get(pos).getPhone());
                        i.putExtra("country", addressPOS.get(pos).getCountry());
                        i.putExtra("state", addressPOS.get(pos).getZone());
                        i.putExtra("has_ship",has_ship);
                        startActivity(i);
                        //    }

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

    public void add_call()
    {
        AddressListTask addressListTask = new AddressListTask();
        addressListTask.execute();
    }
}

