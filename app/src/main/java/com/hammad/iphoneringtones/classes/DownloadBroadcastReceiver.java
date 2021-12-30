package com.hammad.iphoneringtones.classes;

import static com.hammad.iphoneringtones.classes.StaticVariable.downloadReference;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.hammad.iphoneringtones.R;

public class DownloadBroadcastReceiver extends BroadcastReceiver {

    String message;

    public DownloadBroadcastReceiver(String message) {
        this.message = message;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (downloadReference == downloadID) {
            showDialog(
                    context,
                    context.getResources().getString(R.string.download),
                    message,
                    context.getResources().getString(R.string.ok));
        }
    }

    public void showDialog(Context context, final String title, final String message, final String buttonText) {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                AlertDialog.Builder adbb = new AlertDialog.Builder(context);
                adbb.setIcon(R.mipmap.ic_app_launcher_suqare);
                adbb.setTitle(title);
                if (message != null)
                    adbb.setMessage(message);
                adbb.setPositiveButton(buttonText, null);
                try {
                    adbb.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
