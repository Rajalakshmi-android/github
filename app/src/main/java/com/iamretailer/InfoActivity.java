package com.iamretailer;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.iamretailer.Common.CommonFunctions;


public class InfoActivity extends AppCompatActivity {


    private TextView cur_version;
    private TextView app_name;
    private String currentVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        CommonFunctions.updateAndroidSecurityProvider(this);
        cur_version = (TextView) findViewById(R.id.cur_version);
        app_name = (TextView) findViewById(R.id.app_name);
        app_name.setText(R.string.app_name);
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String mystring = getResources().getString(R.string.app_version);
            cur_version.setText(mystring + " " + currentVersion + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent, this.getTheme()));

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));


        }

    }

}
