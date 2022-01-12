package com.hammad.iphoneringtones.classes;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import com.hammad.iphoneringtones.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class SetWallpaperTask extends AsyncTask<String, Void, Bitmap> {
    DialogProgressBar progressDialog;
    Context context;
    boolean isSetAsLockScreen;
    boolean isSetBoth;

    public SetWallpaperTask(Context context, boolean isSetAsLockScreen, boolean isSetBoth) {
        this.context = context;
        this.isSetAsLockScreen = isSetAsLockScreen;
        this.isSetBoth = isSetBoth;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap result = null;
        try {
            result = Picasso.get().load(params[0]).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setBitmap(result);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            if (isSetBoth) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(result, null, true, WallpaperManager.FLAG_SYSTEM);
                }
                wallpaperManager.setBitmap(result);
                Toast.makeText(context, context.getResources().getString(R.string.set_wallpaper_on_both_successfully), Toast.LENGTH_SHORT).show();
            } else {
                if (isSetAsLockScreen) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager.setBitmap(result, null, true, WallpaperManager.FLAG_LOCK);
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new DialogProgressBar(context);
        progressDialog.showSpinnerDialog();
    }
}
