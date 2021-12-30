package com.hammad.iphoneringtones.ui.ringtones;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        context.unregisterReceiver(onDownloadComplete);
    }

    private void initialize() {
        ringtoneModelArrayList = new ArrayList<>();
        progressBar = new DialogProgressBar(context);
        progressBar.showSpinnerDialog();
        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
                String[] array = ringtoneModelArrayList.get(position).getRingtoneTitle().split("\\.");
                String fileName = array[0];
                downloadPdf(ringtoneModelArrayList.get(position).getRingtoneURL(), fileName);
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

    long downloadReference;

    public void downloadPdf(String url, String fileName) {
        Uri Download_Uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
//Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
//Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Downloading");
//Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading " + fileName);
//Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(context, getDirectoryPath(), getSubPath(fileName));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setTitle(fileName);
//Enqueue a new download and same the referenceId
        downloadReference = downloadManager.enqueue(request);
        Log.d(TAG, "downloadPdf: " + downloadReference);
    }

    public String getDirectoryPath() {
        File directory;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            directory = context.getExternalFilesDir("IPhoneRingtone");
        } else {
            directory = Environment.getExternalStoragePublicDirectory("IPhoneRingtone");
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Log.d(TAG, "saveFileName: " + directory.getPath());
        return directory.getPath();
    }

    private String getSubPath(String fileName) {
        Log.d(TAG, "getSubPath: " + fileName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".mp3");
        return fileName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".mp3";
    }

    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadReference == id) {
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private File checkFolder(Context context) {
        File root = Build.VERSION.SDK_INT > 29 ? new File(context.getExternalFilesDir((String) null), "IPhoneRingtones") : new File(Environment.getExternalStorageDirectory(), "IPhoneRingtones");
        boolean isDirectoryCreated = root.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = root.mkdir();
            Log.d(TAG, "Created = " + isDirectoryCreated);
        }
        Log.d(TAG, "Created ? " + isDirectoryCreated);
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