package com.hammad.haze.ui.ringtones;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.hammad.haze.classes.BaseActivity;
import com.hammad.haze.classes.DownloadBroadcastReceiver;
import com.hammad.haze.classes.RingtoneHelperUtils;
import com.hammad.haze.dialogs.RingtoneBottomSheetDialog;
import com.hammad.haze.R;
import com.hammad.haze.databinding.ActivityRingtonePlayerBinding;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

public class RingtonePlayerActivity extends BaseActivity {
    private static final String TAG = "RingtonePlayerActivity";
    ActivityRingtonePlayerBinding binding;
    MediaPlayer mediaPlayer;
    RingtoneModel ringtoneModel;
    Handler handler;
    Runnable runnable;
    DownloadBroadcastReceiver receiver;
    RingtoneModel mRingtoneModel;
    int ringtoneType = 0;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + ringtoneType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        binding = ActivityRingtonePlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentData();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_icon, null));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        clearMediaPlayer(true);
    }

    private void initialize() {
        setToolbarTitle(capitalize(ringtoneModel.getRingtoneTitle()));
        startMediaPlayer();
        receiver = new DownloadBroadcastReceiver(getResources().getString(R.string.ringtone));
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void getIntentData() {
        ringtoneModel = (RingtoneModel) getIntent().getSerializableExtra("list");
    }

    private void setListener() {
        binding.ivPlayActivityRingtonePlayer.setOnClickListener(view -> playMediaPlayer());
        binding.seekerActivityRingtonePlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    update();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_icon, null));
                    mediaPlayer.start();
                }
            }
        });
        binding.ivDownloadActivityRingtonePlayer.setOnClickListener(view -> {
            RingtoneBottomSheetDialog ringtoneBottomSheetDialog = new RingtoneBottomSheetDialog(this, ringtoneModel, new RingtoneBottomSheetDialog.Callback() {
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
            ringtoneBottomSheetDialog.show(getSupportFragmentManager(), "Download");
        });
    }

    private void startSettingRingtone(RingtoneModel ringtoneModel, int ringtoneType, boolean isSetAsRing) {
        if (checkReadWritePermissions(this)) {
            RingtoneHelperUtils.setRingtone(RingtonePlayerActivity.this, ringtoneModel.getRingtoneURL(), ringtoneModel.getRingtoneTitle(), ringtoneType, isSetAsRing);
        } else {
            activityResultLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    private void playMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_icon, null));
        } else {
            binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_icon, null));
            mediaPlayer.start();
            update();
        }
    }

    private void startMediaPlayer() {
        binding.progressBarActivityRingtonePlayer.setVisibility(View.VISIBLE);
        binding.ivPlayActivityRingtonePlayer.setEnabled(false);
        setToolbarTitle(ringtoneModel.getRingtoneTitle());
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(ringtoneModel.getRingtoneURL());
            mediaPlayer.prepareAsync();
            mediaPlayer.setVolume(10, 10);
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                update();
                binding.progressBarActivityRingtonePlayer.setVisibility(View.INVISIBLE);
                binding.ivPlayActivityRingtonePlayer.setEnabled(true);
                binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_icon, null));
            });
            mediaPlayer.setOnCompletionListener(mp -> clearMediaPlayer(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        if (mediaPlayer != null) {
            binding.seekerActivityRingtonePlayer.setMax(mediaPlayer.getDuration());
            binding.tvTotalDurationActivityRingtonePlayer.setText(convertSecondsToHMmSs(mediaPlayer.getDuration()));
        }
        runOnUiThread(() -> {
            binding.seekerActivityRingtonePlayer.setProgress(mediaPlayer.getCurrentPosition());
            if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() > 100) {
                binding.tvCurrentDurationActivityRingtonePlayer.setText(MessageFormat.format("{0}", convertSecondsToHMmSs(mediaPlayer.getCurrentPosition())));
            } else {
                clearMediaPlayer(false);
            }
            handler = new Handler();
            try {
                runnable = () -> {
                    try {
                        if (mediaPlayer.getCurrentPosition() > -1) {
                            try {
                                update();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                handler.postDelayed(runnable, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    private void clearMediaPlayer(boolean clearMedia) {
        if (clearMedia) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
        binding.progressBarActivityRingtonePlayer.setVisibility(View.INVISIBLE);
        binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_icon, null));
        binding.tvCurrentDurationActivityRingtonePlayer.setText(getResources().getString(R.string._00_00));
        binding.tvTotalDurationActivityRingtonePlayer.setText(getResources().getString(R.string._00_00));
        binding.seekerActivityRingtonePlayer.setProgress(0);
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private static String convertSecondsToHMmSs(long milliSeconds) {
        long seconds = milliSeconds / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", m, s);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    RingtoneHelperUtils.setRingtone(RingtonePlayerActivity.this, mRingtoneModel.getRingtoneURL(), mRingtoneModel.getRingtoneTitle(), ringtoneType, ringtoneType != 0);
                }
            } else {
                Toast.makeText(RingtonePlayerActivity.this, "Permissions Denied", Toast.LENGTH_LONG).show();
            }
        }
    });

}