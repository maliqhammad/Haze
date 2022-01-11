package com.hammad.iphoneringtones.ui.ringtones;

import androidx.core.content.res.ResourcesCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.BaseActivity;
import com.hammad.iphoneringtones.databinding.ActivityRingtonePlayerBinding;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

public class RingtonePlayerActivity extends BaseActivity {
    private static final String TAG = "RingtonePlayerActivity";
    ActivityRingtonePlayerBinding binding;
    MediaPlayer mediaPlayer;
    RingtoneModel ringtoneModel;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
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

    private void initialize() {
        setToolbarTitle(ringtoneModel.getRingtoneTitle());
    }

    private void getIntentData() {
        ringtoneModel = (RingtoneModel) getIntent().getSerializableExtra("list");
        Log.d(TAG, "getIntentData: " + ringtoneModel.getRingtoneTitle());
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
                    mediaPlayer.start();
                }
            }
        });
    }

    private void playMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_icon, null));
        } else if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_icon, null));
            mediaPlayer.start();
        } else {
            startMediaPlayer();
        }
    }

    private void startMediaPlayer() {
        setToolbarTitle(ringtoneModel.getRingtoneTitle());
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(ringtoneModel.getRingtoneURL());
            mediaPlayer.prepare();
            mediaPlayer.setVolume(10, 10);
            mediaPlayer.start();
            update();
            mediaPlayer.setOnPreparedListener(mp -> {
                binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_icon, null));
                binding.seekerActivityRingtonePlayer.setMax(mp.getDuration());
                binding.tvTotalDurationActivityRingtonePlayer.setText(convertSecondsToHMmSs(mediaPlayer.getDuration()));
            });
            mediaPlayer.setOnCompletionListener(mp -> clearMediaPlayer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        runOnUiThread(() -> {
            binding.seekerActivityRingtonePlayer.setProgress(mediaPlayer.getCurrentPosition());
            if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() > 100) {
                binding.tvCurrentDurationActivityRingtonePlayer.setText(MessageFormat.format("{0}", convertSecondsToHMmSs(mediaPlayer.getCurrentPosition())));
            } else {
                clearMediaPlayer();
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

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }

    private void clearMediaPlayer() {
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
        binding.ivPlayActivityRingtonePlayer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_icon, null));
        binding.tvCurrentDurationActivityRingtonePlayer.setText(getResources().getString(R.string._00_00));
        binding.tvTotalDurationActivityRingtonePlayer.setText(getResources().getString(R.string._00_00));
        binding.seekerActivityRingtonePlayer.setProgress(0);
        handler.removeCallbacks(runnable);
    }

    private static String convertSecondsToHMmSs(long milliSeconds) {
        long seconds = milliSeconds / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", m, s);
    }

}