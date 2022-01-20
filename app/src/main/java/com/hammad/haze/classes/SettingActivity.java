//package com.hammad.Haze.classes;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.AlertDialog.Builder;
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Parcelable;
//import android.os.Build.VERSION;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.content.FileProvider;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//
//import kotlin.Metadata;
//import kotlin.ResultKt;
//import kotlin.Unit;
//import kotlin.coroutines.Continuation;
//import kotlin.coroutines.CoroutineContext;
//import kotlin.coroutines.intrinsics.IntrinsicsKt;
//import kotlin.jvm.functions.Function2;
//import kotlin.jvm.internal.Intrinsics;
//import kotlin.jvm.internal.Ref.IntRef;
//import kotlinx.coroutines.BuildersKt;
//import kotlinx.coroutines.CoroutineScope;
//import kotlinx.coroutines.CoroutineStart;
//import kotlinx.coroutines.GlobalScope;
//
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//public class SettingActivity extends BaseActivity {
//    private ArrayList mSongList;
//    private int mPosition;
//
//    private File myMusicFilePath;
//    private int STORAGE_PERMISSION_CODE = 3;
//    private AdsUtils adsUtils;
//
//    public int getSTORAGE_PERMISSION_CODE() {
//        return this.STORAGE_PERMISSION_CODE;
//    }
//
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.setContentView(1300100);
//        this.mPosition = this.getIntent().getIntExtra("position", 0);
//        this.mSongList = SongProvider.INSTANCE.getAllSongs((Context) this);
//        this.adsUtils = new AdsUtils((Context) this);
//        if (BaseObject.INSTANCE.getVisibilityAds((Context) this)) {
//            View var10000 = this.findViewById(1000159);
//            Intrinsics.checkNotNullExpressionValue(var10000, "findViewById(R.id.ads_container)");
//            LinearLayout adsContainer = (LinearLayout) var10000;
//            AdsUtils var3 = this.adsUtils;
//            if (var3 == null) {
//                Intrinsics.throwUninitializedPropertyAccessException("adsUtils");
//            }
//
//            var3.loadBannerAds(adsContainer);
//        }
//
//    }
//
//    public void onClickMethod(@NotNull View view) {
//        Intrinsics.checkNotNullParameter(view, "view");
//        ArrayList var10001;
//        int var2;
//        ArrayList var10002;
//        switch (view.getId()) {
//            case 1000009:
//                if (this.checkStoragePermission()) {
//                    var10001 = this.mSongList;
//                    if (var10001 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    var2 = ((SongItem) var10001.get(this.mPosition)).getSongUrl();
//                    var10002 = this.mSongList;
//                    if (var10002 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    this.downloadFileFromRawFolder(var2, ((SongItem) var10002.get(this.mPosition)).getSongTitle());
//                } else {
//                    this.requestStoragePermission();
//                }
//                break;
//            case 1000017:
//                if (this.checkStoragePermission()) {
//                    var10001 = this.mSongList;
//                    if (var10001 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    var2 = ((SongItem) var10001.get(this.mPosition)).getSongUrl();
//                    var10002 = this.mSongList;
//                    if (var10002 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    this.downloadForRing(var2, ((SongItem) var10002.get(this.mPosition)).getSongTitle(), "AlarmRingtone");
//                } else {
//                    this.requestStoragePermission();
//                }
//                break;
//            case 1000110:
//                if (this.checkStoragePermission()) {
//                    var10001 = this.mSongList;
//                    if (var10001 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    var2 = ((SongItem) var10001.get(this.mPosition)).getSongUrl();
//                    var10002 = this.mSongList;
//                    if (var10002 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    this.downloadForRing(var2, ((SongItem) var10002.get(this.mPosition)).getSongTitle(), "NotificationRingtone");
//                } else {
//                    this.requestStoragePermission();
//                }
//                break;
//            case 1000153:
//                if (this.checkStoragePermission()) {
//                    var10001 = this.mSongList;
//                    if (var10001 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    var2 = ((SongItem) var10001.get(this.mPosition)).getSongUrl();
//                    var10002 = this.mSongList;
//                    if (var10002 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    this.downloadForRing(var2, ((SongItem) var10002.get(this.mPosition)).getSongTitle(), "ShareRingtone");
//                } else {
//                    this.requestStoragePermission();
//                }
//                break;
//            case 1000193:
//                this.moreApps();
//                break;
//            case 1000262:
//                this.rateUs();
//                break;
//            case 1000286:
//                if (this.checkStoragePermission()) {
//                    var10001 = this.mSongList;
//                    if (var10001 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    var2 = ((SongItem) var10001.get(this.mPosition)).getSongUrl();
//                    var10002 = this.mSongList;
//                    if (var10002 == null) {
//                        Intrinsics.throwUninitializedPropertyAccessException("mSongList");
//                    }
//
//                    this.downloadForRing(var2, ((SongItem) var10002.get(this.mPosition)).getSongTitle(), "CallRingtone");
//                } else {
//                    this.requestStoragePermission();
//                }
//        }
//
//    }
//
//    public boolean onCreateOptionsMenu(@NotNull Menu menu) {
//        Intrinsics.checkNotNullParameter(menu, "menu");
//        MenuInflater var10000 = this.getMenuInflater();
//        Intrinsics.checkNotNullExpressionValue(var10000, "menuInflater");
//        MenuInflater inflater = var10000;
//        inflater.inflate(1400000, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
//        Intrinsics.checkNotNullParameter(item, "item");
//        boolean var10000;
//        switch (item.getItemId()) {
//            case 1000215:
//                this.shareApp();
//                var10000 = true;
//                break;
//            case 1000381:
//                this.rateUs();
//                var10000 = true;
//                break;
//            case 1000383:
//                this.privacyPolicy();
//                var10000 = true;
//                break;
//            default:
//                var10000 = super.onOptionsItemSelected(item);
//        }
//
//        return var10000;
//    }
//
//    private void privacyPolicy() {
//        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.getString(1900089))));
//    }
//
//    private void shareApp() {
//        try {
//            Intent link = new Intent("android.intent.action.SEND");
//            link.setType("text/plain");
//            link.putExtra("android.intent.extra.SUBJECT", this.getResources().getString(1900062));
//            String sAux = "https://play.google.com/store/apps/details?id=" + this.getPackageName();
//            link.putExtra("android.intent.extra.TEXT", sAux);
//            this.startActivity(Intent.createChooser(link, (CharSequence) "choose one"));
//        } catch (Exception var3) {
//        }
//
//    }
//
//    private void moreApps() {
//        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.getString(1900078))));
//    }
//
//    private void rateUs() {
//        try {
//            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.getPackageName())));
//        } catch (ActivityNotFoundException var2) {
//            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
//        }
//
//    }
//
//    private void downloadFileFromRawFolder(int mPath, final String musicTitle) {
//        final String musicNameExtra = String.valueOf(mPath);
//        try {
//            InputStream inputStream = getResources().openRawResource(getResources().getIdentifier(musicNameExtra, "raw", getPackageName()));
//            myMusicFilePath = new File(checkFolder(), musicTitle + ".mp3");
//            Log.e("FILEPATH ", "fileWithinMyDir " + myMusicFilePath);
//            FileOutputStream out = new FileOutputStream(myMusicFilePath);
//            byte[] buff = new byte[2097152];
//            int read;
//
//            try {
//                while (true) {
//                    read = inputStream.read(buff);
//                    if (read <= 0) {
//                        break;
//                    }
//                    out.write(buff, 0, read);
//                }
//            } finally {
//                inputStream.close();
//                out.close();
//            }
//
//            runOnUiThread(this::showSuccessDialog);
//        } catch (IOException var13) {
//            runOnUiThread(this::showErrorDialog);
//            var13.printStackTrace();
//        }
//    }
//
//    private void downloadForRing(int mPath, final String musicTitle, final String ringType) {
//        String musicNameExtra = String.valueOf(mPath);
//        try {
//            InputStream inputStream = getResources().openRawResource(getResources().getIdentifier(musicNameExtra, "raw", getPackageName()));
//            Log.i("information", musicNameExtra);
//            Log.i("information", inputStream.toString());
//            myMusicFilePath = new File(checkFolder(), musicTitle + ".mp3");
//            Log.e("FILEPATH ", "fileWithinMyDir " + myMusicFilePath);
//            FileOutputStream out = new FileOutputStream(myMusicFilePath);
//            byte[] buff = new byte[2097152];
//            int read;
//
//            try {
//                while (true) {
//                    read = inputStream.read(buff);
//                    if (read <= 0) {
//                        break;
//                    }
//                    out.write(buff, 0, read);
//                }
//            } finally {
//                inputStream.close();
//                out.close();
//            }
//            runOnUiThread(() -> setRingtone(mPath, musicTitle, ringType));
//        } catch (IOException e) {
//            runOnUiThread(this::showErrorDialog);
//            e.printStackTrace();
//        }
//
//    }
//
//    private void showSuccessDialog() {
//        final Dialog mDialog = new Dialog((Context) this);
//        mDialog.requestWindowFeature(1);
//        Window var10000 = mDialog.getWindow();
//        if (var10000 != null) {
//            var10000.setBackgroundDrawable((Drawable) (new ColorDrawable(0)));
//        }
//
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.setContentView(1300101);
//        Button shareBtn = (Button) mDialog.findViewById(1000282);
//        shareBtn.setOnClickListener((OnClickListener) (new OnClickListener() {
//            public void onClick(View it) {
//                mDialog.dismiss();
//                shareMusicFile();
//            }
//        }));
//        ImageView cancelDialog = (ImageView) mDialog.findViewById(1000157);
//        cancelDialog.setOnClickListener((OnClickListener) (new OnClickListener() {
//            public void onClick(View it) {
//                mDialog.dismiss();
//            }
//        }));
//        mDialog.show();
//    }
//
//    private void shareMusicFile() {
//        Context var10000 = this.getApplicationContext();
//        File var10002 = this.myMusicFilePath;
//        Intrinsics.checkNotNull(var10002);
//        Uri var3 = FileProvider.getUriForFile(var10000, "com.azan.ringtones.provider", var10002);
//        Intrinsics.checkNotNullExpressionValue(var3, "FileProvider.getUriForFi…MusicFilePath!!\n        )");
//        Uri contentUri = var3;
//        File var4 = this.myMusicFilePath;
//        Boolean var5 = var4 != null ? var4.exists() : null;
//        Intrinsics.checkNotNull(var5);
//        if (var5) {
//            Intent shareIntent = new Intent();
//            shareIntent.setAction("android.intent.action.SEND");
//            shareIntent.addFlags(1);
//            shareIntent.setDataAndType(contentUri, this.getContentResolver().getType(contentUri));
//            shareIntent.putExtra("android.intent.extra.STREAM", (Parcelable) Uri.fromFile(this.myMusicFilePath));
//            this.startActivity(Intent.createChooser(shareIntent, (CharSequence) "Choose an app"));
//        }
//
//    }
//
//    private void setRingtone(int mPath, String musicTitle, String ringType) {
//        switch (ringType) {
//            case "CallRingtone":
//                RingtoneUtils.setPhoneRingtone(this, Uri.fromFile(myMusicFilePath));
//                break;
//            case "AlarmRingtone":
//                RingtoneUtils.setAlarmRingtone(this, Uri.fromFile(myMusicFilePath));
//                break;
//            case "ShareRingtone":
//                shareMusicFile();
//                break;
//            case "NotificationRingtone":
//                RingtoneUtils.setNotificationRingtone(this, Uri.fromFile(myMusicFilePath));
//        }
//
//    }
//
//    private void showErrorDialog() {
//        final Dialog mDialog = new Dialog((Context) this);
//        mDialog.requestWindowFeature(1);
//        Window var10000 = mDialog.getWindow();
//        if (var10000 != null) {
//            var10000.setBackgroundDrawable((Drawable) (new ColorDrawable(0)));
//        }
//
//        mDialog.setCanceledOnTouchOutside(true);
//        mDialog.setContentView(1300042);
//        Button okBtn = (Button) mDialog.findViewById(1000245);
//        okBtn.setOnClickListener((OnClickListener) (new OnClickListener() {
//            public void onClick(View it) {
//                mDialog.dismiss();
//            }
//        }));
//        mDialog.show();
//    }
//
//    private File checkFolder() {
//        File root = VERSION.SDK_INT > 29 ? new File(this.getExternalFilesDir((String) null), "AzaanRingtone") : new File(Environment.getExternalStorageDirectory(), "AzaanRingtone");
//        boolean isDirectoryCreated = root.exists();
//        if (!isDirectoryCreated) {
//            isDirectoryCreated = root.mkdir();
//            Log.d("Folder", "Created = " + isDirectoryCreated);
//        }
//
//        Log.d("Folder", "Created ? " + isDirectoryCreated);
//        return root;
//    }
//
//    private boolean checkStoragePermission() {
//        return ContextCompat.checkSelfPermission((Context) this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
//    }
//
//    private void requestStoragePermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, "android.permission.WRITE_EXTERNAL_STORAGE") && ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, "android.permission.READ_EXTERNAL_STORAGE")) {
//            (new Builder((Context) this)).setTitle((CharSequence) "Permission needed").setMessage((CharSequence) "Permission is needed to download ringtone").setPositiveButton((CharSequence) "ok", (android.content.DialogInterface.OnClickListener) (new android.content.DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface $noName_0, int $noName_1) {
//                    ActivityCompat.requestPermissions((Activity) SettingActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, getSTORAGE_PERMISSION_CODE());
//                }
//            })).setNegativeButton((CharSequence) "cancel", (android.content.DialogInterface.OnClickListener) null.INSTANCE).create().show();
//        } else {
//            ActivityCompat.requestPermissions((Activity) this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, this.STORAGE_PERMISSION_CODE);
//        }
//
//    }
//
//    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
//        Intrinsics.checkNotNullParameter(permissions, "permissions");
//        Intrinsics.checkNotNullParameter(grantResults, "grantResults");
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == this.STORAGE_PERMISSION_CODE) {
//            boolean var6 = false;
//            if (grantResults.length != 0 && grantResults[0] == 0 && grantResults[1] == 0) {
//                Toast.makeText((Context) this, (CharSequence) "Permission GRANTED!", 1).show();
//            } else {
//                Toast.makeText((Context) this, (CharSequence) "Permission DENIED", 0).show();
//            }
//        }
//
//    }
//
//    protected void onDestroy() {
//        super.onDestroy();
//        AdsUtils var10000 = this.adsUtils;
//        if (var10000 == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("adsUtils");
//        }
//
//        var10000.destroyBannerAds();
//    }
//}
//
