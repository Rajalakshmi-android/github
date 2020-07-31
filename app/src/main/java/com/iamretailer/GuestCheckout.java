package com.iamretailer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.Validation;
import com.iamretailer.POJO.CountryPO;


public class GuestCheckout extends Language {
    FrameLayout cont;
    EditText f_name,l_name,email,mobile;
    ArrayList<CountryPO> country_list;
    ArrayList<CountryPO> state_list;
    DBController db;
    TextView login;
    int has_ship;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_checkout);
        CommonFunctions.updateAndroidSecurityProvider(this);
        db=new DBController(GuestCheckout.this);
        Appconstatants.sessiondata=db.getSession();
        Appconstatants.Lang=db.get_lang_code();
        f_name=(EditText)findViewById(R.id.f_name);
        l_name=(EditText)findViewById(R.id.l_name);
        email=(EditText)findViewById(R.id.mail_id);
        mobile=(EditText)findViewById(R.id.mobile);
        login=(TextView)findViewById(R.id.login);

        has_ship=getIntent().getIntExtra("has_ship",1);

        cont=(FrameLayout) findViewById(R.id.cont);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (f_name.getText().toString().trim().equals(""))
                {
                    f_name.setError(getResources().getString(R.string.f_na));
                }
                if(!Validation.validateName(f_name.getText().toString().trim()))
                {
                    f_name.setError(getResources().getString(R.string.valid_name));
                }
                if (f_name.getText().toString().length()<=2)
                {
                    f_name.setError(getResources().getString(R.string.valid_name));
                }

                if (l_name.getText().toString().trim().equals(""))
                {
                    l_name.setError(getResources().getString(R.string.l_na));
                }
                if (!Validation.validateName(l_name.getText().toString().trim()))
                {
                    l_name.setError(getResources().getString(R.string.valid_name));
                }
                 if (email.getText().toString().trim().equals(""))
                {
                    email.setError(getResources().getString(R.string.mail_id));
                }
                 if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    email.setError(getResources().getString(R.string.valid_mail));
                }
                 if(mobile.getText().toString().trim().length()!=10)
                {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }
                 if (mobile.getText().toString().trim().equals(""))
                {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }
                if (!Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches())
                {
                    mobile.setError(getResources().getString(R.string.mobl_error));
                }

                if (!f_name.getText().toString().isEmpty()  && f_name.getText().toString().trim().length()>=2 && Validation.validateName(l_name.getText().toString().trim())
                        && !l_name.getText().toString().isEmpty() && Validation.validateName(l_name.getText().toString().trim())
                        && !email.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()
                        &&mobile.getText().toString().length()==10 && !mobile.getText().toString().isEmpty() && Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches())
                {

                   Intent intent=new Intent(GuestCheckout.this,Address.class);
                   Bundle gues_data=new Bundle();
                    gues_data.putString("f_name",f_name.getText().toString().trim());
                    gues_data.putString("l_name",l_name.getText().toString().trim());
                    gues_data.putString("email",email.getText().toString().trim());
                    gues_data.putString("mobile",mobile.getText().toString().trim());
                    gues_data.putInt("from",3);
                    gues_data.putInt("has_ship",has_ship);
                    intent.putExtras(gues_data);
                    startActivity(intent);

                }

            }
        });
    }

}
