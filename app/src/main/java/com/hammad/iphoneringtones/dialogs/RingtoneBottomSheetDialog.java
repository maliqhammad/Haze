package com.hammad.iphoneringtones.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.databinding.RingtoneBottomSheetDialogBinding;
import com.hammad.iphoneringtones.ui.ringtones.RingtoneModel;

public class RingtoneBottomSheetDialog extends BottomSheetDialogFragment {
    private static final String TAG = "DialogEditProfileBottom";
    RingtoneBottomSheetDialogBinding binding;
    Context context;
    RingtoneModel ringtoneModel;
    Callback callback;

    public RingtoneBottomSheetDialog(Context context, RingtoneModel ringtoneModel, Callback callback) {
        this.context = context;
        this.ringtoneModel = ringtoneModel;
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
        binding = RingtoneBottomSheetDialogBinding.inflate(getLayoutInflater());
        Log.d(TAG, "onCreateView: ");
        setListener();
        return binding.getRoot();
    }

    void setListener() {
        binding.linearDownloadRingtoneBottomSheetDialog.setOnClickListener(v -> {
            callback.onDownloadRingtone(ringtoneModel);
            dismiss();
        });
        binding.linearSetRingtoneBottomSheetDialog.setOnClickListener(v -> {
            callback.onSetAsRingtone(ringtoneModel);
            dismiss();
        });
        binding.linearSetNotificationBottomSheetDialog.setOnClickListener(v -> {
            callback.onSetAsNotification(ringtoneModel);
            dismiss();
        });
        binding.linearSetAlarmBottomSheetDialog.setOnClickListener(v -> {
            callback.onSetAsAlarm(ringtoneModel);
            dismiss();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public interface Callback {
        void onSetAsRingtone(RingtoneModel ringtoneModel);

        void onSetAsNotification(RingtoneModel ringtoneModel);

        void onSetAsAlarm(RingtoneModel ringtoneModel);

        void onDownloadRingtone(RingtoneModel ringtoneModel);
    }
}
