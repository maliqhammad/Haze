package com.hammad.iphoneringtones.ui.ringtones;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.DialogProgressBar;
import com.hammad.iphoneringtones.databinding.FragmentRingtonesBinding;
import com.hammad.iphoneringtones.dialogs.DialogBottomSheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import kotlin.jvm.internal.Ref;

public class RingtonesFragment extends Fragment {
    private static final String TAG = "RingtonesFragment";
    RingtonesViewModel ringtonesViewModel;
    private FragmentRingtonesBinding binding;
    Context context;
    RingtonesAdapter ringtonesAdapter;
    Observer<RingtoneModel> ringtoneModelObserver;
    ArrayList<RingtoneModel> ringtoneModelArrayList;
    DialogProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ringtonesViewModel = new ViewModelProvider(this).get(RingtonesViewModel.class);
        binding = FragmentRingtonesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initialize();
        setListener();
        setRecyclerView();
//        ringtonesViewModel.retrieveRingtones();
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ringtonesAdapter != null) {
            ringtonesAdapter.stopMediaPlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ringtonesAdapter != null) {
            ringtonesAdapter.stopMediaPlayer();
        }
        binding = null;
        ringtonesViewModel.getRingtones1().removeObserver(ringtoneModelObserver);
    }

    private void initialize() {
        ringtoneModelArrayList = new ArrayList<>();
        progressBar = new DialogProgressBar(context);
        progressBar.showSpinnerDialog();
    }

    private void setListener() {

    }

    private void setRecyclerView() {
        binding.recyclerViewRingtones.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ringtonesAdapter = new RingtonesAdapter(context, new RingtonesAdapter.OnSongItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "onItemClick: ");
                DialogBottomSheet dialogBottomSheet = new DialogBottomSheet(context, null, ringtoneModelArrayList.get(position));
                dialogBottomSheet.show(getChildFragmentManager(), "Dowload");
            }

            @Override
            public void onSetRingtone(int position) {
                Log.d(TAG, "onSetRingtone: ");
//                downloadFileFromRawFolder(context, ringtoneModelArrayList.get(position).getRingtoneURL(), ringtoneModelArrayList.get(position).getRingtoneTitle());
            }

            @Override
            public void onPlayRingtone(int position) {
                Log.d(TAG, "onPlayRingtone: ");
            }
        });
        binding.recyclerViewRingtones.setAdapter(ringtonesAdapter);
        ringtoneModelObserver = ringtoneModel -> {
            progressBar.cancelSpinnerDialog();
            ringtoneModelArrayList.add(ringtoneModel);
            Log.d(TAG, "setRecyclerView: " + ringtoneModel.getRingtoneTitle());
            ringtonesAdapter.updateRingtoneList(ringtoneModel);
        };
        ringtonesViewModel.getRingtones1().observe(getViewLifecycleOwner(), ringtoneModelObserver);
    }

    private void downloadFileFromRawFolder(Context context, String fileUrl, String ringtoneTitle) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.show();
        String musicNameExtra = String.valueOf(fileUrl);
        try {
            InputStream inputStream = context.getResources().openRawResource(context.getResources().getIdentifier(musicNameExtra, "raw", context.getPackageName()));
            File myMusicFilePath = new File(checkFolder(context), ringtoneTitle);
            Log.e("FILEPATH ", "fileWithinMyDir " + myMusicFilePath);
            FileOutputStream out = new FileOutputStream(myMusicFilePath);
            byte[] buff = new byte[2097152];
            Ref.IntRef read = new Ref.IntRef();
            try {
                while (true) {
                    int var6 = inputStream.read(buff);
                    read.element = var6;
                    if (var6 <= 0) {
                        break;
                    }
                    out.write(buff, 0, read.element);
                }
            } finally {
                inputStream.close();
                out.close();
            }

            requireActivity().runOnUiThread(() -> {
                progressDialog.dismiss();
                showSuccessDialog(context, myMusicFilePath);
            });
        } catch (IOException e) {
            requireActivity().runOnUiThread(() -> {
                progressDialog.dismiss();
                showDialog(
                        context.getResources().getString(R.string.failed),
                        context.getResources().getString(R.string.error_message),
                        context.getResources().getString(R.string.ok)
                );
            });
            e.printStackTrace();
        }
    }

    private File checkFolder(Context context) {
        File root = Build.VERSION.SDK_INT > 29 ? new File(context.getExternalFilesDir((String) null), "AzaanRingtone") : new File(Environment.getExternalStorageDirectory(), "AzaanRingtone");
        boolean isDirectoryCreated = root.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = root.mkdir();
            Log.d("Folder", "Created = " + isDirectoryCreated);
        }
        Log.d("Folder", "Created ? " + isDirectoryCreated);
        return root;
    }

    public void showDialog(final String title, final String message, final String buttonText) {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                AlertDialog.Builder adbb = new AlertDialog.Builder(context);
                adbb.setIcon(R.drawable.app_icon);
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

    private void showSuccessDialog(Context context, File file) {
        Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(R.layout.dialog_success);
        Button share = mDialog.findViewById(R.id._shareMusic);
        ImageView cancel = mDialog.findViewById(R.id.cancelDialog);
        share.setOnClickListener(view -> {
            mDialog.dismiss();
            shareMusicFile(context, file);
        });
        cancel.setOnClickListener(view -> mDialog.dismiss());
        mDialog.show();
    }

    private void shareMusicFile(Context context, File file) {
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
}