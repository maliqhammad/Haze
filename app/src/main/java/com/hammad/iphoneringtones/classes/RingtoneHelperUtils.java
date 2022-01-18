package com.hammad.iphoneringtones.classes;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.hammad.iphoneringtones.R;

import java.io.File;

public class RingtoneHelperUtils {

    private static final String TAG = "RingtoneUtils";

    public static void downloadRingtone(Context context, String url, String name, boolean isSetAsRingtone) {
        String[] array = name.split("\\.");
        String fileName = array[0];
        Toast.makeText(context, context.getResources().getText(R.string.downloading_start), Toast.LENGTH_SHORT).show();
        Uri Download_Uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Downloading");
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading " + fileName);
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(context, getDirectoryPath(context), getRingtoneSubPath(fileName));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
        //Enqueue a new download and same the referenceId
        DownloadBroadcastReceiver.downloadReference = downloadManager.enqueue(request);
        if (isSetAsRingtone) {
            setRingtone(context, new File(getDirectoryPath(context) + "/" + getRingtoneSubPath(fileName)), fileName);
        }
    }

    public static String getRingtoneSubPath(String fileName) {
//        return fileName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".mp3";
        return fileName + ".mp3";
    }

    public static String getDirectoryPath(Context context) {
        File directory;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            directory = context.getExternalFilesDir(Environment.DIRECTORY_RINGTONES);
        } else {
            directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
        }
        return directory.getPath();
    }

    public static boolean setRingtone(Context context, Uri ringtoneUri) {
        Log.v(TAG, "Setting Ringtone to: " + ringtoneUri);

        if (!hasMarshmallow()) {
            Log.v(TAG, "On a Lollipop or below device, so go ahead and change device ringtone");
            setActualRingtone(context, ringtoneUri);
            return true;
        } else if (canEditSystemSettings(context)) {
            Log.v(TAG, "On a marshmallow or above device but app has the permission to edit system settings");
            setActualRingtone(context, ringtoneUri);
            return true;
        } else if (hasMarshmallow() && !canEditSystemSettings(context)) {
            Log.d(TAG, "On android Marshmallow and above but app does not have permission to" +
                    " edit system settings. Opening the manage write settings activity...");
            startManageWriteSettingsActivity(context);
            Toast.makeText(context, "Please allow app to edit settings so your ringtone can be updated", Toast.LENGTH_LONG).show();
            return false;
        }

        return false;
    }

    public static void setRingtone(Context context, File file, String fileName) {
        if (file.exists()) {
            file.mkdirs();
        }
        Log.d(TAG, "setRingtone: " + fileName);
        Log.d(TAG, "setRingtone: " + file.getAbsolutePath());
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        values.put(MediaStore.MediaColumns.SIZE, file.length());
//        values.put(MediaStore.Audio.Media.ARTIST, "Some Artist");

        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        //Insert it into the database
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        Uri newUri = context.getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
    }

    private static void setActualRingtone(@NonNull Context context, @NonNull Uri ringtoneUri) {
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, ringtoneUri);
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

}
