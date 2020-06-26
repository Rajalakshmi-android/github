package com.iamretailer.krishnasupermarket.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.krishnasupermarket.Address;
import com.iamretailer.krishnasupermarket.AddressList;
import com.iamretailer.krishnasupermarket.Common.Appconstatants;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.POJO.AddressPO;
import com.iamretailer.krishnasupermarket.R;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class AddressAdapter extends ArrayAdapter<AddressPO> {
    Context context;
    ArrayList<AddressPO> items;
    int res;
    LayoutInflater mInflater;
    AndroidLogger logger;
    int from;
    DBController dbController;


    public AddressAdapter(Context context, int resource, ArrayList<AddressPO> items,int from) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.res = resource;
        this.context = context;
        this.items = items;
        this.from=from;
        dbController=new DBController(context);
        Appconstatants.CUR=dbController.getCurCode();
        logger=AndroidLogger.getLogger(context, Appconstatants.LOG_ID,false);

    }

    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        FrameLayout alertView = null;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(res, alertView, true);
            convertView.setTag(holder);
            alertView = (FrameLayout) convertView;
        } else {
            alertView = (FrameLayout) convertView;
        }
        holder.cus_name = (TextView) convertView.findViewById(R.id.cus_name);
        holder.address_1=(TextView)convertView.findViewById(R.id.address_1);
        holder.city=(TextView)convertView.findViewById(R.id.city);
        holder.country=(TextView)convertView.findViewById(R.id.country);
        holder.cus_phone=(TextView)convertView.findViewById(R.id.cus_phone);
        holder.edit=(LinearLayout)convertView.findViewById(R.id.edit);
        holder.delete=(LinearLayout)convertView.findViewById(R.id.delete);
        holder.select=(ImageView) convertView.findViewById(R.id.select);
        holder.select1=(ImageView) convertView.findViewById(R.id.select1);
        holder.add=(LinearLayout)convertView.findViewById(R.id.add);
        holder.select_bg=(LinearLayout)convertView.findViewById(R.id.select_bg);
        holder.pos=(TextView)convertView.findViewById(R.id.pos);
        int pos1=position+1;
        holder.pos.setText(pos1+":");

        holder.cus_name.setText(items.get(position).getF_name()+" "+items.get(position).getL_name());

        if (items.get(position).getAdd_2()!=null || items.get(position).getAdd_2().trim().length()==0) {
            holder.address_1.setText(items.get(position).getAdd_1()+", "+items.get(position).getAdd_2());
        }else {
            holder.address_1.setText(items.get(position).getAdd_1());
        }
        holder.city.setText(items.get(position).getCity()+", "+items.get(position).getZone());

        holder.country.setText(items.get(position).getCountry()+" - "+items.get(position).getPost_code());
        holder.cus_phone.setText(items.get(position).getPhone());

        if (from==1) {
            if (items.get(position).isSelect()) {
                holder.select.setVisibility(View.VISIBLE);
                holder.select1.setVisibility(View.GONE);
            } else {
                holder.select.setVisibility(View.GONE);
                holder.select1.setVisibility(View.VISIBLE);

            }
        }
        else
        {
            holder.select.setVisibility(View.GONE);
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent i = new Intent(getContext(), Address.class);
                Bundle add = new Bundle();
                add.putString("address_id",items.get(position).getAdd_id());
                add.putString("first", items.get(position).getF_name());
                add.putString("last_name", items.get(position).getL_name());
                add.putString("company", items.get(position).getCompany());
                add.putString("addressone", items.get(position).getAdd_1());
                add.putString("addresstwo", items.get(position).getAdd_2());
                add.putString("city", items.get(position).getCity());
                add.putString("pincode", items.get(position).getPost_code());
                add.putString("country", items.get(position).getCountry());
                add.putString("state", items.get(position).getZone());
                add.putString("phone", items.get(position).getPhone());
                add.putString("company", items.get(position).getCompany());
                add.putInt("from", 1);
                i.putExtras(add);
                ((AddressList)getContext()).startActivityForResult(i, 3);


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Address_Del address_del=new Address_Del(position);
                address_del.execute(items.get(position).getAdd_id());

            }
        });




        return alertView;
    }



    private  class ViewHolder {

        public TextView cus_name,address_1,address_2,city,state,country,cus_phone,pos;
        ImageView select,select1;
        LinearLayout add,select_bg,edit,delete;

    }


    private class Address_Del extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;
        private int  pos;


        public Address_Del(int position) {
            pos=position;
        }


        @Override
        protected void onPreExecute() {

            Log.d("delete", "started");

            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getContext().getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {

            logger.info("Delete cart api"+ Appconstatants.cart_update_api);


            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                Log.d("del_session", Appconstatants.sessiondata);
                logger.info("Delete cart api"+json);
                response = connection.sendHttpDelete(Appconstatants.ADDRESS_DEL+param[0], json, Appconstatants.sessiondata,Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,getContext());
                logger.info("Delete cart api"+response);
                Log.d("del_s", Appconstatants.ADDRESS_DEL+param[0]);
                Log.d("del_r", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("cart", "Cart-->" + resp);
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        items.remove(pos);
                        notifyDataSetChanged();
                        ((AddressList)context).add_call();
                        Toast.makeText(getContext(), R.string.address_del, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray array=json.getJSONArray("error");
                        Toast.makeText(getContext(),array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                }

            } else {

                Toast.makeText(getContext(), R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }


}



