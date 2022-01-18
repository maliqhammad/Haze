package com.hammad.iphoneringtones.classes;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hammad.iphoneringtones.R;


public class RingtoneUtils {

    private static final String LOG_TAG = "RingtoneUtils";

    public static boolean setPhoneRingtone(@NonNull Context context, @NonNull Uri ringtoneUri) {
        Log.v(LOG_TAG, "Setting Ringtone to: " + ringtoneUri);

        if (!hasMarshmallow()) {
            Log.v(LOG_TAG, "On a Lollipop or below device, so go ahead and change device ringtone");
            setActualCallRingtone(context, ringtoneUri);
            return true;
        } else if (hasMarshmallow() && canEditSystemSettings(context)) {
            Log.v(LOG_TAG, "On a marshmallow or above device but app has the permission to edit system settings");
            setActualCallRingtone(context, ringtoneUri);
            return true;
        } else if (hasMarshmallow() && !canEditSystemSettings(context)) {
            Log.d(LOG_TAG, "On android Marshmallow and above but app does not have permission to" + " edit system settings. Opening the manage write settings activity...");
            showPermissionDialog(context);
//            Toast.makeText(context, "Please allow app to edit settings so your ringtone can be updated", Toast.LENGTH_LONG).show();
            return false;
        }

        return false;
    }

    public static boolean setNotificationRingtone(@NonNull Context context, @NonNull Uri ringtoneUri) {
        Log.v(LOG_TAG, "Setting Ringtone to: " + ringtoneUri);

        if (!hasMarshmallow()) {
            Log.v(LOG_TAG, "On a Lollipop or below device, so go ahead and change device ringtone");
            setActualNotificationRingtone(context, ringtoneUri);
            return true;
        } else if (hasMarshmallow() && canEditSystemSettings(context)) {
            Log.v(LOG_TAG, "On a marshmallow or above device but app has the permission to edit system settings");
            setActualNotificationRingtone(context, ringtoneUri);
            return true;
        } else if (hasMarshmallow() && !canEditSystemSettings(context)) {
            Log.d(LOG_TAG, "On android Marshmallow and above but app does not have permission to" +
                    " edit system settings. Opening the manage write settings activity...");
            showPermissionDialog(context);
//            Toast.makeText(context, "Please allow app to edit settings so your ringtone can be updated", Toast.LENGTH_LONG).show();
            return false;
        }

        return false;
    }

    public static boolean setAlarmRingtone(@NonNull Context context, @NonNull Uri ringtoneUri) {
        Log.v(LOG_TAG, "Setting Ringtone to: " + ringtoneUri);

        if (!hasMarshmallow()) {
            Log.v(LOG_TAG, "On a Lollipop or below device, so go ahead and change device ringtone");
            setActualAlarmRingtone(context, ringtoneUri);
            return true;
        } else if (hasMarshmallow() && canEditSystemSettings(context)) {
            Log.v(LOG_TAG, "On a marshmallow or above device but app has the permission to edit system settings");
            setActualAlarmRingtone(context, ringtoneUri);
            return true;
        } else if (hasMarshmallow() && !canEditSystemSettings(context)) {
            Log.d(LOG_TAG, "On android Marshmallow and above but app does not have permission to" +
                    " edit system settings. Opening the manage write settings activity...");
            showPermissionDialog(context);
//            Toast.makeText(context, "Please allow app to edit settings so your ringtone can be updated", Toast.LENGTH_LONG).show();
            return false;
        }

        return false;
    }

    private static void setActualCallRingtone(@NonNull Context context, @NonNull Uri ringtoneUri) {
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, ringtoneUri);
        showSuccessDialog(context, "This Ringtone is Successfully set as your Call Ringtone!");
    }

    private static void setActualNotificationRingtone(@NonNull Context context, @NonNull Uri ringtoneUri) {
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, ringtoneUri);
        showSuccessDialog(context, "This Ringtone is Successfully set as your Notification Ringtone!");
    }

    private static void setActualAlarmRingtone(@NonNull Context context, @NonNull Uri ringtoneUri) {
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM, ringtoneUri);
        showSuccessDialog(context, "This Ringtone is Successfully set as your Alarm Ringtone");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void startManageWriteSettingsActivity(@NonNull Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        // Passing in the app package here allows the settings app to open the exact app
        intent.setData(Uri.parse("package:" + context.getApplicationContext().getPackageName()));
        // Optional. If you pass in a service context without setting this flag, you will get an exception
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static boolean hasMarshmallow() {
        // returns true if the device is Android Marshmallow or above, false otherwise
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean canEditSystemSettings(@NonNull Context context) {
        // returns true if the app can edit system settings, false otherwise
        return Settings.System.canWrite(context.getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void showPermissionDialog(Context mContext) {
        Dialog mDialog = new BottomSheetDialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(R.layout.dialog_permission);

        Button btnAllow = mDialog.findViewById(R.id.allowPermission);
        assert btnAllow != null;
        btnAllow.setOnClickListener(view -> {
            startManageWriteSettingsActivity(mContext);
            mDialog.dismiss();
        });

        mDialog.show();
    }

    private static void showSuccessDialog(Context mContext, String message) {
        Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_success_ring);

        TextView titleName = mDialog.findViewById(R.id.titleName);
        titleName.setText(message);

        Button btnGotIt = mDialog.findViewById(R.id._gotIt);
        assert btnGotIt != null;
        btnGotIt.setOnClickListener(view -> {

            mDialog.dismiss();
        });

        mDialog.show();
    }

}
