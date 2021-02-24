package com.iamretailer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamretailer.Common.CommonFunctions;

public class SuccessActivity extends AppCompatActivity {


    private Bundle bun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sucess);
        CommonFunctions.updateAndroidSecurityProvider(this);
        TextView order_id =findViewById(R.id.orderid);
        final LinearLayout details =findViewById(R.id.details);
        TextView header = findViewById(R.id.header);
        header.setText(R.string.order_succuess);
        LinearLayout cart_items =findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        LinearLayout back = findViewById(R.id.menu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.performClick();
            }
        });
        bun = new Bundle();
        bun = getIntent().getExtras();
        String v1 = "#" + bun.getString("id");
        order_id.setText(v1);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SuccessActivity.this, MainActivity.class);
                startActivity(i);
                //  dialog.dismiss();

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));


        }

    }

}