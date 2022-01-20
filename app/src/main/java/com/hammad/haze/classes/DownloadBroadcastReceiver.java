package com.hammad.haze.classes;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.hammad.haze.R;

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    public static long downloadReference = 0;
    String message;
    OnDownloadCompleteListener completeListener;
    public DownloadBroadcastReceiver(String message) {
        this.message = message;
    }

    public DownloadBroadcastReceiver(String message,OnDownloadCompleteListener completeListener) {
        this.message = message;
        this.completeListener=completeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (downloadReference == downloadID) {
            Toast.makeText(context, message + " " + context.getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
            if (completeListener!=null){
                completeListener.onDownloadCompleted(downloadReference);
            }
        }
    }

    public interface OnDownloadCompleteListener{
        void onDownloadCompleted(long ref);
    }
}
