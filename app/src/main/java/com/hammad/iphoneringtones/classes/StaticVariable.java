package com.hammad.iphoneringtones.classes;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.ui.wallpapers.WallpaperModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class StaticVariable {
    private static final String TAG = "StaticVariable";
    public static long downloadReference = 0;

    public static void downloadRingtone(Context context, String url, String name) {
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
        downloadReference = downloadManager.enqueue(request);
    }

    public static String getRingtoneSubPath(String fileName) {
        return fileName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".mp3";
    }

    public static void downloadWallpaper(Context context, String url, String name) {
        String[] array = name.split("\\.");
        String fileName = array[0];
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
        request.setDestinationInExternalFilesDir(context, getDirectoryPath(context), getWallpaperSubPath(fileName));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
        //Enqueue a new download and same the referenceId
        downloadReference = downloadManager.enqueue(request);
    }

    public static String getWallpaperSubPath(String fileName) {
        return fileName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".jpg";
    }

    public static String getDirectoryPath(Context context) {
        File directory;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            directory = context.getExternalFilesDir("IPhoneRingtone");
        } else {
            directory = Environment.getExternalStoragePublicDirectory("IPhoneRingtone");
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Log.d(TAG, "saveFileName: " + directory.getPath());
        return directory.getPath();
    }

}
