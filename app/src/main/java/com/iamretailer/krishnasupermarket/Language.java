package com.iamretailer.krishnasupermarket;

import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iamretailer.krishnasupermarket.Common.CommonFunctions;
import com.iamretailer.krishnasupermarket.Common.DBController;
import com.iamretailer.krishnasupermarket.Common.LanguageList;

import java.util.ArrayList;
import java.util.Locale;

public class Language extends AppCompatActivity {


    private DBController db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonFunctions.updateAndroidSecurityProvider(this);
        //setContentView(R.layout.activity_language);


        db = new DBController(Language.this);
        // change_langs(dbCon.get_lang_code());
        if (db.get_lan_c()>0)
        {
            change_langs(db.get_lang_code());
            //change_langs("ar");
        }
    }

    private void change_langs(String languageToLoad) {

        ArrayList<String> lang_list= LanguageList.getLang_list();
        String set_lan="en";

        for (int h=0;h<lang_list.size();h++)
        {
            if (languageToLoad.contains(lang_list.get(h)))
            {
                set_lan = lang_list.get(h);

            }

        }
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            // API 17+ only.
            Locale locale = new Locale(set_lan);
            Locale.setDefault(locale);
            config.locale = locale;

        } else {
            config.locale = new Locale("en");
        }

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
