package com.hammad.haze.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.hammad.haze.R;

import java.io.File;

public class RingtoneHelperUtils {

    private static final String TAG = "RingtoneUtils";
    static int downloadId = 0;

    public static void setRingtone(Context context, String fileURL, String ringtoneTitle, int ringtoneType, boolean isSetAsRing) {
        if (!hasMarshmallow()) {
            downloadRingtone(context, fileURL, ringtoneTitle, ringtoneType, isSetAsRing);
        } else if (canEditSystemSettings(context)) {
            downloadRingtone(context, fileURL, ringtoneTitle, ringtoneType, isSetAsRing);
        } else if (hasMarshmallow() && !canEditSystemSettings(context)) {
            showDialog_with_listener(
                    context,
                    context.getResources().getString(R.string.system_settings),
                    context.getResources().getString(R.string.permission_message),
                    context.getResources().getString(R.string.close),
                    context.getResources().getString(R.string.allow_permission),
                    true,
                    (dialogInterface, i) -> {
                        if (DialogInterface.BUTTON_POSITIVE == i) {
                            startManageWriteSettingsActivity(context);
                        }
                    });
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private static void downloadRingtone(Context context, String fileURL, String ringtoneTitle, int ringtoneType, boolean isSetAsRing) {
        DialogProgressBar progressBar = new DialogProgressBar(context);
        progressBar.showSpinnerDialog();
        String fileName = ringtoneTitle.split("\\.")[0];
        downloadId = PRDownloader.download(fileURL, Utils.getRootDirPath(context), ringtoneTitle)
                .build()
                .setOnStartOrResumeListener(() -> {
                })
                .setOnPauseListener(progressBar::cancelSpinnerDialog)
                .setOnCancelListener(progressBar::cancelSpinnerDialog)
                .setOnProgressListener(progress -> Log.d(TAG, "onProgress: " + progress.currentBytes))
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        progressBar.cancelSpinnerDialog();
                        if (isSetAsRing) {
                            ((Activity) context).runOnUiThread(() -> setActualRingtoneUri(context, Uri.fromFile(new File(Utils.getRingtoneFilePath(context, fileName))), ringtoneType));
                        } else {
                            Toast.makeText(context, "Ringtone saved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        progressBar.cancelSpinnerDialog();
                    }
                });
    }

    public static void setActualRingtoneUri(Context context, Uri ringtoneUri, int ringtoneType) {
        String message;
        switch (ringtoneType) {
            case RingtoneManager.TYPE_RINGTONE:
                message = "Ringtone successfully set as your call ringtone";
                break;
            case RingtoneManager.TYPE_ALARM:
                message = "Ringtone successfully set as your alarm ringtone";
                break;
            case RingtoneManager.TYPE_NOTIFICATION:
                message = "Ringtone successfully set as your notification ringtone";
                break;
            default:
                message = "Ringtone successfully set";
        }
        RingtoneManager.setActualDefaultRingtoneUri(context, ringtoneType, ringtoneUri);
        showDialog(context,
                "Ringtone",
                message,
                "Ok");
    }

    public static void showDialog_with_listener(Context context, final String title, final String message, final String negativeBtnText, final String positiveBtnText, final boolean isCancelAble, final DialogInterface.OnClickListener listener) {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_permission_24);
                builder.setTitle(title);
                builder.setMessage(message);
                if (positiveBtnText != null && !positiveBtnText.equals("")) {
                    builder.setPositiveButton(positiveBtnText, listener);
                }
                if (negativeBtnText != null && !negativeBtnText.equals("")) {
                    builder.setNegativeButton(negativeBtnText, listener);
                }
                try {
                    builder.setCancelable(isCancelAble);
                    Dialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(isCancelAble);
                    dialog.setCancelable(isCancelAble);
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(Context context, final String title, final String message, final String buttonText) {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.app_icon_simple);
                builder.setTitle(title);
                if (message != null)
                    builder.setMessage(message);
                builder.setPositiveButton(buttonText, null);
                try {
                    Dialog dialog;
                    dialog = builder.create();
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startManageWriteSettingsActivity(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            // Passing in the app package here allows the settings app to open the exact app
            intent.setData(Uri.parse("package:" + context.getApplicationContext().getPackageName()));
            // Optional. If you pass in a service context without setting this flag, you will get an exception
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
//            ((Activity)context).startActivityForResult(intent,REQ_SYSTEM_PERMISSION);
        }
    }

    private static boolean hasMarshmallow() {
        // returns true if the device is Android Marshmallow or above, false otherwise
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean canEditSystemSettings(Context context) {
        // returns true if the app can edit system settings, false otherwise
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(context.getApplicationContext());
        }
        return false;
    }

}
