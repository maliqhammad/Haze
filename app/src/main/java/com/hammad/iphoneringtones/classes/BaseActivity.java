package com.hammad.iphoneringtones.classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.ui.wallpapers.WallpaperModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity {

    public ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.mipmap.ic_app_launcher_suqare);
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
        AsyncTask<String, Void, Bitmap> asyncTask = new SetWallpaperTask(context, isSetAsLockScreen, isSetBoth);
        asyncTask.execute(wallpaperModel.getWallpaperUri());
    }
}
