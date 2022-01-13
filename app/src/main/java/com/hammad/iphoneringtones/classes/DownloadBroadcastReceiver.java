package com.hammad.iphoneringtones.classes;

import static com.hammad.iphoneringtones.classes.WallpaperHelperUtils.downloadReference;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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
            Toast.makeText(context, message + " " + context.getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
        }
    }
}
