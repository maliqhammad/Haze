package com.hammad.iphoneringtones.ui.ringtones;

import static com.hammad.iphoneringtones.classes.RingtoneHelperUtils.downloadRingtone;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.BaseFragment;
import com.hammad.iphoneringtones.classes.DialogProgressBar;
import com.hammad.iphoneringtones.classes.DownloadBroadcastReceiver;
import com.hammad.iphoneringtones.databinding.FragmentRingtonesBinding;
import com.hammad.iphoneringtones.dialogs.RingtoneBottomSheetDialog;

import java.util.ArrayList;

public class RingtonesFragment extends BaseFragment {
    private static final String TAG = "RingtonesFragment";
    RingtonesViewModel ringtonesViewModel;
    private FragmentRingtonesBinding binding;
    Context context;
    RingtonesAdapter ringtonesAdapter;
    Observer<RingtoneModel> ringtoneModelObserver;
    ArrayList<RingtoneModel> ringtoneModelArrayList;
    DialogProgressBar progressBar;
    DownloadBroadcastReceiver receiver;

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
        ringtonesViewModel.getRingtones1().removeObserver(ringtoneModelObserver);
        context.unregisterReceiver(receiver);
    }

    private void initialize() {
        receiver = new DownloadBroadcastReceiver(context.getResources().getString(R.string.ringtone));
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
                Intent intent = new Intent(context, RingtonePlayerActivity.class);
                intent.putExtra("list", ringtoneModelArrayList.get(position));
                startActivity(intent);
            }

            @Override
            public void onSetRingtone(int position) {
                RingtoneBottomSheetDialog ringtoneBottomSheetDialog = new RingtoneBottomSheetDialog(context, ringtoneModelArrayList.get(position), new RingtoneBottomSheetDialog.Callback() {
                    @Override
                    public void onSetAsRingtone(RingtoneModel ringtoneModel) {
                        downloadRingtone(context, ringtoneModel.getRingtoneURL(), ringtoneModel.getRingtoneTitle(),true);
                    }

                    @Override
                    public void onDownloadRingtone(RingtoneModel ringtoneModel) {
                        downloadRingtone(context, ringtoneModel.getRingtoneURL(), ringtoneModel.getRingtoneTitle(),false);
                    }
                });
                ringtoneBottomSheetDialog.show(getChildFragmentManager(), "Download");
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
}