package com.hammad.iphoneringtones.classes;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hammad.iphoneringtones.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RingtoneHelperUtils {

    private static final String TAG = "RingtoneUtils";

    public static void downloadRingtone(Context context, int ringtoneType, String url, String name, boolean isSetAsRingtone) {
        String[] array = name.split("\\.");
        String fileName = array[0];
        Toast.makeText(context, context.getResources().getText(R.string.downloading_start), Toast.LENGTH_SHORT).show();
        Uri download_Uri = Uri.parse(url);
//        getDirectoryPath(context);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(download_Uri);
        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
//        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Downloading");
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading " + fileName);
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/WallTone/" +fileName + ".mp3");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
        //Enqueue a new download and same the referenceId
        DownloadBroadcastReceiver.downloadReference = downloadManager.enqueue(request);
//        if (isSetAsRingtone) {
//            setRingtone(context, ringtoneType, new File(getDirectoryPath(context) + "/WallTone/" + fileName + ".mp3"), fileName);
//        }
    }

    public static String getRingtoneSubPath(String fileName) {
//        return fileName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".mp3";
        return fileName + ".mp3";
    }

    public static String getDirectoryPath(Context context) {
        File directory;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        } else {
            directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Log.d(TAG, "getDirectoryPath: " + directory.exists());
        Log.d(TAG, "getDirectoryPath: " + directory.getPath());
        return directory.getPath();
    }

    public static void setRingtone(Context context, int ringtoneType, File file, String fileName) {
        Log.d(TAG, "Setting Ringtone to: " + file.getPath());
        if (!hasMarshmallow()) {
            Log.v(TAG, "On a Lollipop or below device, so go ahead and change device ringtone");
            setActualRingtone(context, ringtoneType, file, fileName);
        } else if (hasMarshmallow() && canEditSystemSettings(context)) {
            Log.v(TAG, "On a marshmallow or above device but app has the permission to edit system settings");
            setActualRingtone(context, ringtoneType, file, fileName);
        } else if (hasMarshmallow() && !canEditSystemSettings(context)) {
            Log.d(TAG, "On android Marshmallow and above but app does not have permission to" + " edit system settings. Opening the manage write settings activity...");
            showPermissionDialog(context);
        }
    }


    public static void setActualRingtone(Context context, int ringtoneType, File file, String fileName) {

        Log.d(TAG, "setRingtone: " + file.exists());
        Log.d(TAG, "setRingtone: " + fileName);
        Log.d(TAG, "setRingtone: " + file.getAbsolutePath());
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
        values.put(MediaStore.MediaColumns.SIZE, file.length());
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        //Insert it into the database
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        Uri newUri = context.getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(context, ringtoneType, newUri);
    }

    public static void setAsRingtone(Context context, String url, String title) {
        try {
            String path = Environment.getExternalStorageDirectory() + "/WallTone/";
            title = title.split("\\.")[0];
//            String[] array = title.split("\\.");
//            String fileName = array[0];
            String fileName = title + ".mp3";
            File ringtone = new File(path, fileName);
            if (!ringtone.exists()) {
                // Download Ringtone from the storage
                Toast.makeText(context, "برجاء الانتظار لحين تحميل النغمة", Toast.LENGTH_SHORT).show();
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setTitle(title);
                request.setVisibleInDownloadsUi(true);
                File download = new File(Environment.getExternalStorageDirectory() + "/WallTone/", title + ".mp3");
                Uri parse = Uri.fromFile(download);
//                request.setDestinationUri(parse);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/WallTone/" + title + ".mp3");
//                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "/WallTone/" + title + ".mp3");
                assert downloadManager != null;
                downloadManager.enqueue(request);
            }
            // Set mp3 file as ringtone
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, ringtone.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, title);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
            values.put("_size", ringtone.length());
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtone.getAbsolutePath());
//            context.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + ringtone.getAbsolutePath() + "\"", null);
            String filePathToDelete = MediaStore.MediaColumns.DATA + "=\"" + ringtone.getAbsolutePath() + "\"";
            context.getContentResolver().delete(uri, filePathToDelete, null);
            Uri newUri = context.getContentResolver().insert(uri, values);
            try {
                RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
            } catch (Exception e) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(context)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                                .setData(Uri.parse("package:" + context.getPackageName()))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean SetAsRingtoneOrNotification(Context context, String uri, int type) {
        File file = new File(uri);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, file.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
        if (RingtoneManager.TYPE_RINGTONE == type) {
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        } else if (RingtoneManager.TYPE_NOTIFICATION == type) {
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri newUri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            try (OutputStream os = context.getContentResolver().openOutputStream(newUri)) {
                int size = (int) file.length();
                byte[] bytes = new byte[size];
                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                    os.write(bytes);
                    os.close();
                    os.flush();
                } catch (IOException e) {
                    return false;
                }
            } catch (Exception ignored) {
                return false;
            }
            RingtoneManager.setActualDefaultRingtoneUri(context, type, newUri);
            return true;
        } else {
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            Uri uri1 = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
            context.getContentResolver().delete(uri1, MediaStore.MediaColumns.DATA + "=\"" + file.getAbsolutePath() + "\"", null);
            Uri newUri = context.getContentResolver().insert(uri1, values);
            RingtoneManager.setActualDefaultRingtoneUri(context, type, newUri);
            context.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()), values);
            return true;
        }


    }

    private static void setActualRingtone(@NonNull Context context, @NonNull Uri ringtoneUri) {
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, ringtoneUri);
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

    private static String getExternalFilesDirectory(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
        } else {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        }
    }

//    void createExternalStoragePublicPicture(Context context) {
//        // Create a path where we will place our picture in the user's
//        // public pictures directory.  Note that you should be careful about
//        // what you place here, since the user often manages these files.  For
//        // pictures and other media owned by the application, consider
//        // Context.getExternalMediaDir().
//        File path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File file = new File(path, "DemoPicture.jpg");
//
//        try {
//            // Make sure the Pictures directory exists.
//            path.mkdirs();
//
//            // Very simple code to copy a picture from the application's
//            // resource into the external file.  Note that this code does
//            // no error checking, and assumes the picture is small (does not
//            // try to copy it in chunks).  Note that if external storage is
//            // not currently mounted this will silently fail.
//            InputStream is = context.getResources().openRawResource(R.drawable.app_icon);
//            OutputStream os = new FileOutputStream(file);
//            byte[] data = new byte[is.available()];
//            is.read(data);
//            os.write(data);
//            is.close();
//            os.close();
//
//            // Tell the media scanner about the new file so that it is
//            // immediately available to the user.
//            MediaScannerConnection.scanFile(context,
//                    new String[] { file.toString() }, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
//                        }
//                    });
//        } catch (IOException e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }
//    }

}
