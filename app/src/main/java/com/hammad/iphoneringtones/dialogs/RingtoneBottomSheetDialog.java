package com.hammad.iphoneringtones.dialogs;

import static com.hammad.iphoneringtones.classes.StaticVariable.downloadRingtone;

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

    public RingtoneBottomSheetDialog(Context context, RingtoneModel ringtoneModel) {
        this.context = context;
        this.ringtoneModel = ringtoneModel;
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
            downloadRingtone(context, ringtoneModel.getRingtoneURL(), ringtoneModel.getRingtoneTitle());
            dismiss();
        });
        binding.linearSetRingtoneBottomSheetDialog.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
