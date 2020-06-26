package com.iamretailer.krishnasupermarket.Common;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CommonFunctions {
	
	public static InputStream createfalseJson(){
		try{
		JSONObject json = new JSONObject();
		json.put("success", false);
		JSONObject lo = new JSONObject();
		lo.put("redirectToLogin", true);
		json.put("data", lo);
		return new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("ResourceAsColor")

	public static String convert_date(String date) {
		if (date != null && date.length()>2) {
			SimpleDateFormat dateFormatprev = new SimpleDateFormat("dd/MM/yyyy");
			Date d = null;
			try {
				d = dateFormatprev.parse(String.valueOf(date));
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			String changedDate = dateFormat.format(d);

			return changedDate;
		}
		else
		{
			return "";
		}
	}


	public static boolean isNetworkConnected(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null;
	}

	public static String date_format(String date)
	{
		SimpleDateFormat dateFormatprev = new SimpleDateFormat("dd/MM/yyyy");
		Date d = null;
		try {
			d = dateFormatprev.parse(String.valueOf(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy");
		String changedDate = dateFormat.format(d);

		return changedDate;
	}
	public static void updateAndroidSecurityProvider(Activity callingActivity) {
		Log.i("sgihijkghiks","skghsjkiyhg");
		try {
			ProviderInstaller.installIfNeeded(callingActivity);
		} catch (GooglePlayServicesRepairableException e) {
			GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
		} catch (GooglePlayServicesNotAvailableException e) {
			Log.e("SecurityException", "Google Play Services not available.");
		}
	}

}
