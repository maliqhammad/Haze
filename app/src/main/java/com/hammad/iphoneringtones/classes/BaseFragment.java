package com.hammad.iphoneringtones.classes;

import static com.hammad.iphoneringtones.classes.StaticVariable.downloadReference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.hammad.iphoneringtones.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showDialog(Context context, final String title, final String message, final String buttonText) {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                AlertDialog.Builder adbb = new AlertDialog.Builder(context);
                adbb.setIcon(R.mipmap.ic_app_launcher_suqare);
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

    public void showSuccessDialog(Context context) {
        Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(R.layout.dialog_success);
        Button shareButton = mDialog.findViewById(R.id.shareButton);
        shareButton.setVisibility(View.GONE);
        Button closeButton = mDialog.findViewById(R.id.closeButton);
        shareButton.setOnClickListener(view -> {
            mDialog.dismiss();
//            shareMusicFile(context, file);
        });
        closeButton.setOnClickListener(view -> mDialog.dismiss());
        mDialog.show();
    }

    public void shareMusicFile(Context context, File file) {
        Uri contentUri = FileProvider.getUriForFile(context, "com.hammad.iphoneringtones.provider", file);
        if (file.exists()) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }

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
}
