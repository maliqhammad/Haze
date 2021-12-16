package com.hammad.iphoneringtones.ui.ringtones;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.databinding.FragmentSlideshowBinding;
import com.hammad.iphoneringtones.dialogs.DialogBottomSheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import kotlin.jvm.internal.Ref;

public class RingtonesFragment extends Fragment {

    RingtonesViewModel ringtonesViewModel;
    private FragmentSlideshowBinding binding;
    RecyclerView recyclerViewRingtones;
    SongAdapter songAdapter;
    Context context;
    ArrayList<SongItem> songItemArrayList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ringtonesViewModel = new ViewModelProvider(this).get(RingtonesViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initialize();
        setIds();
        setRecyclerView();
        ringtonesViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });
        return root;
    }

    private void initialize() {
        songItemArrayList = new ArrayList<>();
        songItemArrayList.addAll(SongProvider.getAllSongs(context));
    }

    private void setIds() {
        recyclerViewRingtones = binding.recyclerViewRingtones;

    }

    private void setRecyclerView() {
        recyclerViewRingtones.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        songAdapter = new SongAdapter(context, songItemArrayList, new SongAdapter.OnSongItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(context, "onItemClick", Toast.LENGTH_SHORT).show();
                DialogBottomSheet dialogBottomSheet = new DialogBottomSheet(context, null, songItemArrayList.get(position));
                dialogBottomSheet.show(getChildFragmentManager(), "Dowload");
            }

            @Override
            public void onSetRingClick(int position) {
                Toast.makeText(context, "onSetRingClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlayerRingClick(int position) {
                Toast.makeText(context, "onPlayerRingClick", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewRingtones.setAdapter(songAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void downloadFileFromRawFolder(Context context, int mPath, String musicTitle) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.show();
        String musicNameExtra = String.valueOf(mPath);
        try {
            InputStream inputStream = context.getResources().openRawResource(context.getResources().getIdentifier(musicNameExtra, "raw", context.getPackageName()));
            File myMusicFilePath = new File(checkFolder(context), musicTitle + ".mp3");
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
                showErrorDialog(context);
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

    private void showErrorDialog(Context context) {
        Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(R.layout.dialog_error);
        Button button = mDialog.findViewById(R.id._gotIt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
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
        Uri contentUri = FileProvider.getUriForFile(context, "com.azan.ringtones.provider", file);
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