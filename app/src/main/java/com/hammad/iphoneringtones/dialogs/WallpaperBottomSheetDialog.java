package com.hammad.iphoneringtones.dialogs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.databinding.WallpaperBottomSheetDialogBinding;
import com.hammad.iphoneringtones.ui.wallpapers.WallpaperModel;

public class WallpaperBottomSheetDialog extends BottomSheetDialogFragment {
    private static final String TAG = "DialogEditProfileBottom";
    WallpaperBottomSheetDialogBinding binding;
    Context context;
    WallpaperModel wallpaperModel;
    Callback callback;

    public WallpaperBottomSheetDialog(Context context, WallpaperModel wallpaperModel, Callback callback) {
        this.context = context;
        this.wallpaperModel = wallpaperModel;
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WallpaperBottomSheetDialogBinding.inflate(getLayoutInflater());
        Log.d(TAG, "onCreateView: ");
        setListener();
        return binding.getRoot();
    }

    void setListener() {
        binding.linearDownloadWallpaperBottomSheetDialog.setOnClickListener(v -> {
            callback.onDownloadWallpaper(wallpaperModel);
            dismiss();
        });
        binding.linearSetWallpaperBottomSheetDialog.setOnClickListener(v -> {
            if (checkPermission()) {
                callback.onSetWallpaper(wallpaperModel);
                dismiss();
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

    public interface Callback {
        void onDownloadWallpaper(WallpaperModel wallpaperModel);

        void onSetWallpaper(WallpaperModel wallpaperModel);
    }
}
