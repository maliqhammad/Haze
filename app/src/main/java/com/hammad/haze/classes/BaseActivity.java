package com.hammad.haze.classes;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.hammad.haze.R;
import com.hammad.haze.ui.wallpapers.WallpaperModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.mipmap.ic_app_launcher_square);
            actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.toolbar_bg, null));
        }
    }

    public void setToolbarTitle(String title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    public void shareRingtoneLink(Context context, String ringtoneLink, String ringToneName) {
        ringtoneLink = ringtoneLink.replaceAll(" ", "%20");
        ringtoneLink = "Check out what i have found on the " + context.getResources().getString(R.string.app_name) + ".\nI think you will like \"" + ringToneName + "\"\n\n" + ringtoneLink;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, ringToneName);
        share.putExtra(Intent.EXTRA_TEXT, ringtoneLink);
        startActivity(Intent.createChooser(share, context.getResources().getString(R.string.app_name)));
    }

    public String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, Objects.requireNonNull(capMatcher.group(1)).toUpperCase() + Objects.requireNonNull(capMatcher.group(2)).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setAsWallpaper(Context context, WallpaperModel wallpaperModel, boolean isSetAsLockScreen, boolean isSetBoth) {
        new BackgroundTask(this) {
            Bitmap result;
            DialogProgressBar progressDialog;

            @Override
            public void onPreExeucte() {
                progressDialog = new DialogProgressBar(context);
                progressDialog.showSpinnerDialog();
            }

            @Override
            public void doInBackground() {
                try {
                    result = Picasso.get().load(wallpaperModel.getWallpaperUri()).get();
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                    wallpaperManager.setBitmap(result);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onPostExecute() {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                try {
                    if (isSetBoth) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            wallpaperManager.setBitmap(result, null, false, WallpaperManager.FLAG_LOCK);
                        }
                        wallpaperManager.setBitmap(result);
                        Toast.makeText(context, context.getResources().getString(R.string.set_wallpaper_on_both_successfully), Toast.LENGTH_SHORT).show();
                    } else {
                        if (isSetAsLockScreen) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                wallpaperManager.setBitmap(result, null, false, WallpaperManager.FLAG_LOCK);
                                Toast.makeText(context, context.getResources().getString(R.string.set_lock_screen_successfully), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            wallpaperManager.setBitmap(result);
                            Toast.makeText(context, context.getResources().getString(R.string.set_wallpaper_successfully), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressDialog.cancelSpinnerDialog();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    progressDialog.cancelSpinnerDialog();
                    Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public void showDialog(final String title, final String message, final String buttonText) {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                AlertDialog.Builder adbb = new AlertDialog.Builder(this);
                adbb.setIcon(R.mipmap.ic_app_launcher_square);
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

    public boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkReadWritePermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


}
