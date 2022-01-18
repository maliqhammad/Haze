package com.hammad.iphoneringtones.classes;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hammad.iphoneringtones.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RingtoneHelperUtils {

    private static final String TAG = "RingtoneUtils";
    private static final String DIRECTORYNAME = "WallTone";
    public static int REQ_PERMISSION = 987;

    public static void downloadRingtone(Activity activity, Context context, String fileURL, String ringtoneTitle, int ringtoneType, boolean isSetAsRing) {
        if (checkReadWritePermissions(context)) {
            String dirPath = String.valueOf(context.getExternalFilesDir(DIRECTORYNAME));
            String fileName = ringtoneTitle.split("\\.")[0];
            Log.d(TAG, "down: \nfileURL: " + fileURL + "\ndirPath: " + dirPath + "\nfileName: " + fileName);
            int downloadId = PRDownloader.download(fileURL, dirPath, ringtoneTitle)
                    .build()
                    .setOnStartOrResumeListener(() -> Toast.makeText(context, context.getResources().getText(R.string.downloading_start), Toast.LENGTH_SHORT).show())
                    .setOnPauseListener(() -> {

                    })
                    .setOnCancelListener(() -> {

                    })
                    .setOnProgressListener(progress -> Log.d(TAG, "onProgress: " + progress.currentBytes))
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            Log.d(TAG, "onDownloadComplete: ");
                            Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                            if (isSetAsRing) {
                                setRingtone(context, fileName, ringtoneType);
                            } else {
                                Toast.makeText(context, "Ringtone saved", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Error error) {
                            Log.d(TAG, "onError: ");
                        }
                    });
        } else {
            askReadWritePermissions(activity);
        }
    }

    public static void setRingtone(Context context, String fileName, int ringType) {
//        fileName = fileName.split("\\.")[0];
        String filePath = context.getExternalFilesDir(DIRECTORYNAME) + "/" + fileName + ".mp3";
        Log.d(TAG, "downloadForRing: " + filePath);
//        Log.d(TAG, "downloadForRing: " + FileUtils.getPath(context, Uri.parse(filePath)));
        try {
            InputStream inputStream = new FileInputStream(filePath);
            File myMusicFilePath = new File(checkFolder(context), fileName + ".mp3");
            FileOutputStream out = new FileOutputStream(myMusicFilePath);
            byte[] buff = new byte[1024 * 1024 * 2];
            int read;
            try {
                while (true) {
                    read = inputStream.read(buff);
                    if (read <= 0) {
                        break;
                    }
                    out.write(buff, 0, read);
                }
            } finally {
                inputStream.close();
                out.close();
            }
            ((Activity) context).runOnUiThread(() -> setActualRingtoneUri(context, Uri.fromFile(myMusicFilePath), ringType));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File checkFolder(Context context) {
        File root = Build.VERSION.SDK_INT > 29 ? new File(context.getExternalFilesDir(null), DIRECTORYNAME) : new File(Environment.getExternalStorageDirectory(), DIRECTORYNAME);
        boolean isDirectoryCreated = root.exists();
        Log.d(TAG, "checkFolder isDirectoryCreated: " + isDirectoryCreated);
        if (!isDirectoryCreated) {
            isDirectoryCreated = root.mkdir();
        }
        Log.d(TAG, "checkFolder isDirectoryCreated: " + isDirectoryCreated);
        return root;
    }

    public static void setActualRingtoneUri(@NonNull Context context, @NonNull Uri ringtoneUri, int ringtoneType) {
        Log.d(TAG, "Setting Ringtone to: " + ringtoneUri);
        if (!hasMarshmallow()) {
            Log.d(TAG, "On a Lollipop or below device, so go ahead and change device ringtone");
            RingtoneManager.setActualDefaultRingtoneUri(context, ringtoneType, ringtoneUri);
            showDialog(context,
                    "This Ringtone is Successfully set as your Call Ringtone!",
                    "Ok",
                    false,
                    ResourcesCompat.getDrawable(context.getResources(), R.drawable.app_icon_simple, null));
        } else if (hasMarshmallow() && canEditSystemSettings(context)) {
            Log.d(TAG, "On a marshmallow or above device but app has the permission to edit system settings");
            RingtoneManager.setActualDefaultRingtoneUri(context, ringtoneType, ringtoneUri);
            showDialog(context,
                    "This Ringtone is Successfully set as your Call Ringtone!",
                    "Ok",
                    false,
                    ResourcesCompat.getDrawable(context.getResources(), R.drawable.app_icon_simple, null));
        } else if (hasMarshmallow() && !canEditSystemSettings(context)) {
            Log.d(TAG, "On android Marshmallow and above but app does not have permission to" + " edit system settings. Opening the manage write settings activity...");
            showDialog(context,
                    context.getResources().getString(R.string.permission_message),
                    context.getResources().getString(R.string.allow_permission),
                    true,
                    ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_permission_24, null));
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean checkReadWritePermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void askReadWritePermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERMISSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void showDialog(Context mContext, String message, String buttonText, boolean withListener, Drawable drawable) {
        Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_success_ring);
        ImageView iv_dialog_success = mDialog.findViewById(R.id.iv_dialog_success);
        TextView tv_message_dialog_success = mDialog.findViewById(R.id.tv_message_dialog_success);
        Button btn_dialog_success = mDialog.findViewById(R.id.btn_dialog_success);
        iv_dialog_success.setImageDrawable(drawable);
        tv_message_dialog_success.setText(message);
        btn_dialog_success.setText(buttonText);
        btn_dialog_success.setOnClickListener(view -> {
            if (withListener) {
                startManageWriteSettingsActivity(mContext);
            }
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
