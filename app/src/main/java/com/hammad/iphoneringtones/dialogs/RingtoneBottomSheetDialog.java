package com.hammad.iphoneringtones.dialogs;

import static com.hammad.iphoneringtones.classes.StaticVariable.downloadRingtone;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.ui.ringtones.RingtoneModel;


public class RingtoneBottomSheetDialog extends BottomSheetDialogFragment {
    private static final String TAG = "DialogEditProfileBottom";
    LinearLayout
            linear_download_ringtone_bottom_sheet_dialog,
            linear_set_ringtone_bottom_sheet_dialog;
    TextView tv_title_ringtone_bottom_sheet_dialog;
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
        View view = inflater.inflate(R.layout.ringtone_bottom_sheet_dialog, container, false);
        Log.d(TAG, "onCreateView: ");
        setIds(view);
        setListener();
        return view;
    }

    void setIds(View view) {
        tv_title_ringtone_bottom_sheet_dialog = view.findViewById(R.id.tv_title_ringtone_bottom_sheet_dialog);
        tv_title_ringtone_bottom_sheet_dialog.setText(ringtoneModel.getRingtoneTitle());
        linear_download_ringtone_bottom_sheet_dialog = view.findViewById(R.id.linear_download_ringtone_bottom_sheet_dialog);
        linear_set_ringtone_bottom_sheet_dialog = view.findViewById(R.id.linear_set_ringtone_bottom_sheet_dialog);
    }

    void setListener() {
        linear_download_ringtone_bottom_sheet_dialog.setOnClickListener(v -> {
            downloadRingtone(context, ringtoneModel.getRingtoneURL(), ringtoneModel.getRingtoneTitle());
            dismiss();
        });
        linear_set_ringtone_bottom_sheet_dialog.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
