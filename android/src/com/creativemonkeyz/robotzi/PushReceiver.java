package com.creativemonkeyz.robotzi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
            }
        } catch (JSONException e) {
            SharedPreferences settingsPref = PreferenceManager.getDefaultSharedPreferences(context);

            if (settingsPref.getBoolean("DOWNLOAD_EP", false) && Utils.isDownloadManagerAvailable(context)) {
                // download
            }
        }
    }
}