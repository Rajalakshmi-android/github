package com.iamretailer;


import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;



import java.io.IOException;

public class InfoActivity  extends AppCompatActivity {


    private TextView cur_version;
    private TextView app_name;
    private String currentVersion;
    private String downloadsize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        cur_version = (TextView) findViewById(R.id.cur_version);
        app_name = (TextView) findViewById(R.id.app_name);
        app_name.setText(R.string.app_name);
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            cur_version.setText("App Version: "+currentVersion+"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Log.i("currentversion",currentVersion+"");
    }

}
