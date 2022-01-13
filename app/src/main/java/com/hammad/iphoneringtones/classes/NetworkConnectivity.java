package com.hammad.iphoneringtones.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class NetworkConnectivity {

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!networkInfo.isConnected()) {
            ShowSingletonAlertDialog.newInstance(context).showDialog();
        }
        return networkInfo.isConnected();
    }
}
