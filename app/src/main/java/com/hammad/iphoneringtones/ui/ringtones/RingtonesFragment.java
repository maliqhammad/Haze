package com.hammad.iphoneringtones.ui.ringtones;

import static com.hammad.iphoneringtones.classes.RingtoneHelperUtils.setRingtone;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.BaseFragment;
import com.hammad.iphoneringtones.classes.DownloadBroadcastReceiver;
import com.hammad.iphoneringtones.databinding.FragmentRingtonesBinding;
import com.hammad.iphoneringtones.dialogs.RingtoneBottomSheetDialog;

import java.util.ArrayList;
import java.util.Map;

public class RingtonesFragment extends BaseFragment {
    private static final String TAG = "RingtonesFragment";
    RingtonesViewModel ringtonesViewModel;
    private FragmentRingtonesBinding binding;
    Context context;
    RingtonesAdapter ringtonesAdapter;
    Observer<RingtoneModel> ringtoneModelObserver;
    ArrayList<RingtoneModel> ringtoneModelArrayList;
    DownloadBroadcastReceiver receiver;
    RingtoneModel mRingtoneModel;
    int ringtoneType = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ringtonesViewModel = new ViewModelProvider(this).get(RingtonesViewModel.class);
        binding = FragmentRingtonesBinding.inflate(inflater, container, false);
        Log.d(TAG, "onCreateView: ");
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
        ringtonesViewModel.getRingtones(context).removeObserver(ringtoneModelObserver);
        context.unregisterReceiver(receiver);
    }

    private void initialize() {
        receiver = new DownloadBroadcastReceiver(context.getResources().getString(R.string.ringtone));
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        ringtoneModelArrayList = new ArrayList<>();
    }

    private void setListener() {

    }

    private void setRecyclerView() {
        binding.recyclerViewRingtones.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ringtonesAdapter = new RingtonesAdapter(context, new RingtonesAdapter.OnSongItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, RingtonePlayerActivity.class);
                intent.putExtra("list", ringtoneModelArrayList.get(position));
                startActivity(intent);
            }

            @Override
            public void onSetRingtone(int position) {
                RingtoneBottomSheetDialog ringtoneBottomSheetDialog = new RingtoneBottomSheetDialog(context, ringtoneModelArrayList.get(position), new RingtoneBottomSheetDialog.Callback() {
                    @Override
                    public void onSetAsRingtone(RingtoneModel ringtoneModel) {
                        mRingtoneModel = ringtoneModel;
                        ringtoneType = RingtoneManager.TYPE_RINGTONE;
                        startSettingRingtone(mRingtoneModel, ringtoneType, true);
                    }

                    @Override
                    public void onSetAsNotification(RingtoneModel ringtoneModel) {
                        mRingtoneModel = ringtoneModel;
                        ringtoneType = RingtoneManager.TYPE_NOTIFICATION;
                        startSettingRingtone(mRingtoneModel, ringtoneType, true);
                    }

                    @Override
                    public void onSetAsAlarm(RingtoneModel ringtoneModel) {
                        mRingtoneModel = ringtoneModel;
                        ringtoneType = RingtoneManager.TYPE_ALARM;
                        startSettingRingtone(mRingtoneModel, ringtoneType, true);
                    }

                    @Override
                    public void onDownloadRingtone(RingtoneModel ringtoneModel) {
                        mRingtoneModel = ringtoneModel;
                        ringtoneType = 0;
                        startSettingRingtone(mRingtoneModel, ringtoneType, false);
                    }
                });
                ringtoneBottomSheetDialog.show(getChildFragmentManager(), "Download");
            }
        });
        binding.recyclerViewRingtones.setAdapter(ringtonesAdapter);
        ringtoneModelObserver = ringtoneModel -> {
            ringtoneModelArrayList.add(ringtoneModel);
            ringtonesAdapter.updateRingtoneList(ringtoneModel);
        };
        ringtonesViewModel.getRingtones(context).observe(getViewLifecycleOwner(), ringtoneModelObserver);
    }

    private void startSettingRingtone(RingtoneModel ringtoneModel, int ringtoneType, boolean isSetAsRing) {
        if (checkReadWritePermissions(context)) {
            setRingtone(context, ringtoneModel.getRingtoneURL(), ringtoneModel.getRingtoneTitle(), ringtoneType, isSetAsRing);
        } else {
            activityResultLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            Log.d(TAG, "onActivityResult: ");
            boolean granted = true;
            for (Map.Entry<String, Boolean> permission : result.entrySet()) {
                if (!permission.getValue()) {
                    granted = false;
                }
            }
            if (granted) {
                if (mRingtoneModel != null) {
                    setRingtone(context, mRingtoneModel.getRingtoneURL(), mRingtoneModel.getRingtoneTitle(), ringtoneType, ringtoneType != 0);
                }
            } else {
                Toast.makeText(context, "Permissions Denied", Toast.LENGTH_LONG).show();
            }
        }
    });
}