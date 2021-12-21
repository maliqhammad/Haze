//package com.hammad.iphoneringtones;
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
//public final class SettingActivity extends BaseActivity {
//    private ArrayList mSongList;
//    private int mPosition;
//    @Nullable
//    private File myMusicFilePath;
//    private final int STORAGE_PERMISSION_CODE = 3;
//    private AdsUtils adsUtils;
//
//    @Nullable
//    public final File getMyMusicFilePath() {
//        return this.myMusicFilePath;
//    }
//
//    public final void setMyMusicFilePath(@Nullable File var1) {
//        this.myMusicFilePath = var1;
//    }
//
//    public final int getSTORAGE_PERMISSION_CODE() {
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
//    public final void onClickMethod(@NotNull View view) {
//        Intrinsics.checkNotNullParameter(view, "view");
//        ArrayList var10001;
//        int var2;
//        ArrayList var10002;
//        switch (view.getId()) {
//            case 1000010:
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
//            case 1000194:
//                this.moreApps();
//                break;
//            case 1000265:
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
//    private final void privacyPolicy() {
//        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.getString(1900089))));
//    }
//
//    private final void shareApp() {
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
//    private final void moreApps() {
//        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.getString(1900077))));
//    }
//
//    private final void rateUs() {
//        try {
//            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.getPackageName())));
//        } catch (ActivityNotFoundException var2) {
//            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
//        }
//
//    }
//
//    private void downloadFileFromRawFolder(Context context, int mPath, String musicTitle) {
//        Dialog progressDialog = new Dialog(context);
//        progressDialog.requestWindowFeature(1);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setContentView(R.layout.progress);
//        progressDialog.show();
//        String musicNameExtra = String.valueOf(mPath);
//
//        try {
//            InputStream inputStream = context.getResources().openRawResource(context.getResources().getIdentifier(musicNameExtra, "raw", context.getPackageName()));
//            Intrinsics.checkNotNullExpressionValue(inputStream, "resources.openRawResourc…      )\n                )");
//            SettingActivity.this.setMyMusicFilePath(new File(SettingActivity.this.checkFolder(), musicTitle + ".mp3"));
//            Log.e("FILEPATH ", "fileWithinMyDir " + SettingActivity.this.getMyMusicFilePath());
//            FileOutputStream out = new FileOutputStream(SettingActivity.this.getMyMusicFilePath());
//            byte[] buff = new byte[2097152];
//            IntRef read = new IntRef();
//
//            try {
//                while (true) {
//                    int var6 = inputStream.read(buff);
//                    read.element = var6;
//                    if (var6 <= 0) {
//                        break;
//                    }
//
//                    out.write(buff, 0, read.element);
//                }
//            } finally {
//                inputStream.close();
//                out.close();
//            }
//
//            runOnUiThread(new Runnable() {
//                public final void run() {
//                    progressDialog.dismiss();
//                    SettingActivity.this.showSuccessDialog();
//                }
//            });
//        } catch (IOException e) {
//            runOnUiThread(new Runnable() {
//                public final void run() {
//                    progressDialog.dismiss();
//                    SettingActivity.this.showErrorDialog();
//                }
//            });
//            e.printStackTrace();
//        }
//
//    }
//
//    private final void downloadForRing(final int mPath, final String musicTitle, final String ringType) {
//        final Dialog progressDialog = new Dialog((Context) this);
//        progressDialog.requestWindowFeature(1);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setContentView(1300094);
//        progressDialog.show();
//        final String musicNameExtra = String.valueOf(mPath);
//        BuildersKt.launch$default((CoroutineScope) GlobalScope.INSTANCE, (CoroutineContext) null, (CoroutineStart) null, (Function2) (new Function2((Continuation) null) {
//            int label;
//
//            @Nullable
//            public final Object invokeSuspend(@NotNull Object var1) {
//                Object var9 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
//                switch (this.label) {
//                    case 0:
//                        ResultKt.throwOnFailure(var1);
//
//                        try {
//                            InputStream var10000 = SettingActivity.this.getResources().openRawResource(SettingActivity.this.getResources().getIdentifier(musicNameExtra, "raw", SettingActivity.this.getPackageName()));
//                            Intrinsics.checkNotNullExpressionValue(var10000, "resources.openRawResourc…      )\n                )");
//                            InputStream mRawFie = var10000;
//                            Log.i("information", musicNameExtra);
//                            Log.i("information", mRawFie.toString());
//                            SettingActivity.this.setMyMusicFilePath(new File(SettingActivity.this.checkFolder(), musicTitle + ".mp3"));
//                            Log.e("FILEPATH ", "fileWithinMyDir " + SettingActivity.this.getMyMusicFilePath());
//                            FileOutputStream out = new FileOutputStream(SettingActivity.this.getMyMusicFilePath());
//                            byte[] buff = new byte[2097152];
//                            IntRef read = new IntRef();
//
//                            try {
//                                while (true) {
//                                    int var6 = mRawFie.read(buff);
//                                    int var8 = false;
//                                    read.element = var6;
//                                    if (var6 <= 0) {
//                                        break;
//                                    }
//
//                                    out.write(buff, 0, read.element);
//                                }
//                            } finally {
//                                mRawFie.close();
//                                out.close();
//                            }
//
//                            SettingActivity.this.runOnUiThread((Runnable) (new Runnable() {
//                                public final void run() {
//                                    progressDialog.dismiss();
//                                    SettingActivity.this.setRingtone(mPath, musicTitle, ringType);
//                                }
//                            }));
//                        } catch (IOException var13) {
//                            SettingActivity.this.runOnUiThread((Runnable) (new Runnable() {
//                                public final void run() {
//                                    progressDialog.dismiss();
//                                    SettingActivity.this.showErrorDialog();
//                                }
//                            }));
//                            Log.i("information", "Download Failed " + var13.getMessage());
//                            var13.printStackTrace();
//                        }
//
//                        return Unit.INSTANCE;
//                    default:
//                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
//                }
//            }
//
//            @NotNull
//            public final Continuation create(@Nullable Object value, @NotNull Continuation completion) {
//                Intrinsics.checkNotNullParameter(completion, "completion");
//                Function2 var3 = new <anonymous constructor > (completion);
//                return var3;
//            }
//
//            public final Object invoke(Object var1, Object var2) {
//                return (( < undefinedtype >) this.create(var1, (Continuation) var2)).
//                invokeSuspend(Unit.INSTANCE);
//            }
//        }), 3, (Object) null);
//    }
//
//    private final void showSuccessDialog() {
//        final Dialog mDialog = new Dialog((Context) this);
//        mDialog.requestWindowFeature(1);
//        Window var10000 = mDialog.getWindow();
//        if (var10000 != null) {
//            var10000.setBackgroundDrawable((Drawable) (new ColorDrawable(0)));
//        }
//
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.setContentView(1300101);
//        Button shareBtn = (Button) mDialog.findViewById(1000283);
//        shareBtn.setOnClickListener((OnClickListener) (new OnClickListener() {
//            public final void onClick(View it) {
//                mDialog.dismiss();
//                SettingActivity.this.shareMusicFile();
//            }
//        }));
//        ImageView cancelDialog = (ImageView) mDialog.findViewById(1000157);
//        cancelDialog.setOnClickListener((OnClickListener) (new OnClickListener() {
//            public final void onClick(View it) {
//                mDialog.dismiss();
//            }
//        }));
//        mDialog.show();
//    }
//
//    private final void shareMusicFile() {
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
//    private final void setRingtone(int mPath, String musicTitle, String ringType) {
//        switch (ringType.hashCode()) {
//            case -1004130240:
//                if (ringType.equals("CallRingtone")) {
//                    RingtoneUtils.setPhoneRingtone((Context) this, Uri.fromFile(this.myMusicFilePath));
//                }
//                break;
//            case -589600525:
//                if (ringType.equals("AlarmRingtone")) {
//                    RingtoneUtils.setAlarmRingtone((Context) this, Uri.fromFile(this.myMusicFilePath));
//                }
//                break;
//            case -517484415:
//                if (ringType.equals("ShareRingtone")) {
//                    this.shareMusicFile();
//                }
//                break;
//            case 1160724941:
//                if (ringType.equals("NotificationRingtone")) {
//                    RingtoneUtils.setNotificationRingtone((Context) this, Uri.fromFile(this.myMusicFilePath));
//                }
//        }
//
//    }
//
//    private final void showErrorDialog() {
//        final Dialog mDialog = new Dialog((Context) this);
//        mDialog.requestWindowFeature(1);
//        Window var10000 = mDialog.getWindow();
//        if (var10000 != null) {
//            var10000.setBackgroundDrawable((Drawable) (new ColorDrawable(0)));
//        }
//
//        mDialog.setCanceledOnTouchOutside(true);
//        mDialog.setContentView(1300041);
//        Button okBtn = (Button) mDialog.findViewById(1000246);
//        okBtn.setOnClickListener((OnClickListener) (new OnClickListener() {
//            public final void onClick(View it) {
//                mDialog.dismiss();
//            }
//        }));
//        mDialog.show();
//    }
//
//    private final File checkFolder() {
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
//    private final boolean checkStoragePermission() {
//        return ContextCompat.checkSelfPermission((Context) this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
//    }
//
//    private final void requestStoragePermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, "android.permission.WRITE_EXTERNAL_STORAGE") && ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, "android.permission.READ_EXTERNAL_STORAGE")) {
//            (new Builder((Context) this)).setTitle((CharSequence) "Permission needed").setMessage((CharSequence) "Permission is needed to download ringtone").setPositiveButton((CharSequence) "ok", (android.content.DialogInterface.OnClickListener) (new android.content.DialogInterface.OnClickListener() {
//                public final void onClick(DialogInterface $noName_0, int $noName_1) {
//                    ActivityCompat.requestPermissions((Activity) SettingActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, SettingActivity.this.getSTORAGE_PERMISSION_CODE());
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
