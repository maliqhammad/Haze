package com.hammad.iphoneringtones.dialogs;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.ui.ringtones.RingtoneModel;
import com.hammad.iphoneringtones.ui.home.HomeModel;


public class DialogBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "DialogEditProfileBottom";
    LinearLayout
            linear_download_dialog_bottom_sheet,
            linear_set_as_wallpaper_dialog_bottom_sheet,
            linear_add_to_favourite_dialog_bottom_sheet;
    Context context;
    WallpaperManager wallpaperManager;
    HomeModel homeModel;
    RingtoneModel ringtoneModel;

    public DialogBottomSheet(Context context, HomeModel homeModel, RingtoneModel ringtoneModel) {
        this.context = context;
        this.homeModel = homeModel;
        this.ringtoneModel = ringtoneModel;
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
        View view = inflater.inflate(R.layout.dialog_bottom_sheet, container, false);
        getIntentData();
        setIds(view);
        setListener();
        return view;
    }

    private void getIntentData() {
        if (getArguments() != null) {

        }
    }

    void setIds(View view) {
        linear_download_dialog_bottom_sheet = view.findViewById(R.id.linear_download_dialog_bottom_sheet);
        linear_set_as_wallpaper_dialog_bottom_sheet = view.findViewById(R.id.linear_set_as_wallpaper_dialog_bottom_sheet);
        linear_add_to_favourite_dialog_bottom_sheet = view.findViewById(R.id.linear_add_to_favourite_dialog_bottom_sheet);
    }

    void setListener() {
        linear_download_dialog_bottom_sheet.setOnClickListener(v -> {
            if (isRingtones()) {
                Log.d(TAG, "setListener: ring");
            } else {
                Log.d(TAG, "setListener: wall: ");
            }
        });
        linear_set_as_wallpaper_dialog_bottom_sheet.setOnClickListener(v -> {
            if (checkPermission()) {

            } else {
                askForPermission();
            }
        });
        linear_add_to_favourite_dialog_bottom_sheet.setOnClickListener(v -> {

        });
    }

    private boolean isRingtones() {
        return ringtoneModel != null && homeModel == null;
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
