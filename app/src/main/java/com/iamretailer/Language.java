package com.iamretailer;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.LanguageList;
import com.iamretailer.Common.LocaleHelper;

import java.util.ArrayList;
import java.util.Locale;

public class Language extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonFunctions.updateAndroidSecurityProvider(this);
        DBController db = new DBController(Language.this);
        if (db.get_lan_c() > 0) {
            change_langs(db.get_lang_code());
        }
    }

    private void change_langs(String languageToLoad) {

        ArrayList<String> lang_list = LanguageList.getLang_list();
        String set_lan = "en";

        if (lang_list != null && lang_list.size() > 0) {
            for (int h = 0; h < lang_list.size(); h++) {
                if (languageToLoad.contains(lang_list.get(h))) {
                    set_lan = lang_list.get(h);

                }

            }

        }

        Configuration config = new Configuration();
        // API 17+ only.
        LocaleHelper.setLocale(this, set_lan);
        Locale locale = new Locale(set_lan);
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLayoutDirection(locale);
        }

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
