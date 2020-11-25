package com.iamretailer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.Common.CommonFunctions;


public class HelpActivity extends AppCompatActivity {
    private LinearLayout menu;
    private LinearLayout list;
    private TextView header;
    private LinearLayout cart_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpactivity);
        CommonFunctions.updateAndroidSecurityProvider(this);
        menu = (LinearLayout) findViewById(R.id.menu);
        list = (LinearLayout) findViewById(R.id.info);
        header = (TextView) findViewById(R.id.header);
        cart_items = (LinearLayout) findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        header.setText(R.string.helps);
        list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, InfoActivity.class));
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
