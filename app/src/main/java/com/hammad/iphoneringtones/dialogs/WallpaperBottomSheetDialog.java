package com.hammad.iphoneringtones.dialogs;

import static com.hammad.iphoneringtones.classes.StaticVariable.downloadWallpaper;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.ui.wallpapers.WallpaperModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class WallpaperBottomSheetDialog extends BottomSheetDialogFragment {
    private static final String TAG = "DialogEditProfileBottom";
    LinearLayout
            linear_download_wallpaper_bottom_sheet_dialog,
            linear_set_wallpaper_bottom_sheet_dialog;
    TextView tv_title_wallpaper_bottom_sheet_dialog;
    Context context;
    WallpaperManager wallpaperManager;
    WallpaperModel wallpaperModel;

    public WallpaperBottomSheetDialog(Context context, WallpaperModel wallpaperModel) {
        this.context = context;
        this.wallpaperModel = wallpaperModel;
        wallpaperManager = WallpaperManager.getInstance(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallpaper_bottom_sheet_dialog, container, false);
        Log.d(TAG, "onCreateView: ");
        setIds(view);
        setListener();
        return view;
    }

    void setIds(View view) {
        tv_title_wallpaper_bottom_sheet_dialog = view.findViewById(R.id.tv_title_wallpaper_bottom_sheet_dialog);
        tv_title_wallpaper_bottom_sheet_dialog.setText(wallpaperModel.getWallpaperTitle());
        linear_download_wallpaper_bottom_sheet_dialog = view.findViewById(R.id.linear_download_wallpaper_bottom_sheet_dialog);
        linear_set_wallpaper_bottom_sheet_dialog = view.findViewById(R.id.linear_set_wallpaper_bottom_sheet_dialog);
    }

    void setListener() {
        linear_download_wallpaper_bottom_sheet_dialog.setOnClickListener(v -> {
            downloadWallpaper(context, wallpaperModel.getWallpaperUri().toString(), wallpaperModel.getWallpaperTitle());
            dismiss();
        });
        linear_set_wallpaper_bottom_sheet_dialog.setOnClickListener(v -> {
            if (checkPermission()) {
                try {
                    Bitmap result = Picasso.get().load(wallpaperModel.getWallpaperUri()).get();
                    wallpaperManager.setBitmap(result);
                    dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                askForPermission();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.SET_WALLPAPER) == PackageManager.PERMISSION_GRANTED;
    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SET_WALLPAPER}, 123);
    }
}
