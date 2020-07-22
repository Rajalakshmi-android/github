package com.iamretailer.Common;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import com.iamretailer.AppClass;
import com.iamretailer.Splash;


public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String activityToBeOpened;
        Intent intent = new Intent(AppClass.getContext(), Splash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        AppClass.getContext().startActivity(intent);

        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            if (result.action.actionID.equals("ActionOne")) {
                Toast.makeText(AppClass.getContext(), "ActionOne Button was pressed", Toast.LENGTH_LONG).show();
            } else if (result.action.actionID.equals("ActionTwo")) {
                Toast.makeText(AppClass.getContext(), "ActionTwo Button was pressed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
