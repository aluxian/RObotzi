package com.creativemonkeyz.robotzi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import java.util.List;

public class Utils {

    public static void launchPlayStorePubPage(Activity host) {
        try {
            host.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Aluxian Apps")));
        } catch (android.content.ActivityNotFoundException anfe) {
            host.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Aluxian%20Apps")));
        }
    }

    public static void launchPlayStoreAppPage(Activity host) {
        try {
            host.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.creativemonkeyz.robotzi")));
        } catch (android.content.ActivityNotFoundException anfe) {
            host.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.creativemonkeyz.robotzi")));
        }
    }

	public static void showToast(final String message, final Activity activity, final Style style) {
		SharedPreferences appPref = activity.getSharedPreferences("APP", Context.MODE_PRIVATE);

		if ((System.currentTimeMillis() - appPref.getLong("MISC_LAST_TOAST", 0)) > 2000 || !appPref.getString("MISC_LAST_TOAST_MESSAGE", null).equals(message)) {
			activity.runOnUiThread(new Runnable() {
                public void run() {
                    Crouton.makeText(activity, message, style).show();
                }
            });

			appPref.edit().putLong("MISC_LAST_TOAST", System.currentTimeMillis()).putString("MISC_LAST_TOAST_MESSAGE", message).commit();
		}
	}

	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD)
				return false;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}
}