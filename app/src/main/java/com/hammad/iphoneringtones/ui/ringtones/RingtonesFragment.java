package com.hammad.iphoneringtones.ui.ringtones;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
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
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hammad.iphoneringtones.R;
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
    MediaPlayer mediaPlayer;
    int lastPosition = -1;
    Observer<ArrayList<RingtoneModel>> observer;

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
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        clearMediaPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        clearMediaPlayer();
        ringtonesViewModel.getRingtones().removeObserver(observer);
    }

    ArrayList<RingtoneModel> ringtoneModelArrayList;

    private void initialize() {
        ringtoneModelArrayList = new ArrayList<>();
    }

    private void setListener() {
        binding.seekBarRingtoneFragment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mediaPlayer.seekTo(progress);
                try {
                    update(mediaPlayer, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                binding.ivPlayRingtoneFragment.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        R.drawable.ic_play_icon, null));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
                binding.ivPlayRingtoneFragment.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        R.drawable.ic_pause_icon, null));
            }
        });
        binding.ivPlayRingtoneFragment.setOnClickListener(view -> {
            if (mediaPlayer != null && lastPosition != -1) {
                playRingtone(lastPosition);
            } else {
                playRingtone(0);
            }
        });
    }

    private void setRecyclerView() {
        observer = ringtoneModels -> {
            Log.d(TAG, "setRecyclerView: " + ringtoneModels.size());
            ringtoneModelArrayList.addAll(ringtoneModels);
            binding.recyclerViewRingtones.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            binding.recyclerViewRingtones.setAdapter(new RingtonesAdapter(context, ringtoneModels, new RingtonesAdapter.OnSongItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Toast.makeText(context, "onItemClick", Toast.LENGTH_SHORT).show();
                    DialogBottomSheet dialogBottomSheet = new DialogBottomSheet(context, null, ringtoneModels.get(position));
                    dialogBottomSheet.show(getChildFragmentManager(), "Dowload");
                }

                @Override
                public void onSetRingtone(int position) {
                    Toast.makeText(context, "onSetRingClick", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPlayRingtone(int position) {
                    playRingtone(position);
                    Toast.makeText(context, "onPlayerRingClick", Toast.LENGTH_SHORT).show();
                }
            }));
        };
        ringtonesViewModel.getRingtones().observe(getViewLifecycleOwner(), observer);
    }

    private void playRingtone(int position) {
        if (lastPosition == position) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                Log.d(TAG, "playRingtone: pause: lastPosition: ");
                mediaPlayer.pause();
                binding.ivPlayRingtoneFragment.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_play_icon, null));
            } else if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                Log.d(TAG, "playRingtone: resume: lastPosition: ");
                mediaPlayer.start();
                update(mediaPlayer, context);
                binding.ivPlayRingtoneFragment.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_icon, null));
            } else {
                Log.d(TAG, "playRingtone: start: lastPosition: ");
                startMediaPlayer(position);
            }
        } else {
            Log.d(TAG, "playRingtone: start: newPosition: ");
            startMediaPlayer(position);
        }
        lastPosition = position;
    }

    private void startMediaPlayer(int position) {
        clearMediaPlayer();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(ringtoneModelArrayList.get(position).getRingtoneURL());
            mediaPlayer.prepare();
            mediaPlayer.setVolume(10, 10);
            mediaPlayer.start();
            update(mediaPlayer, context);
            mediaPlayer.setOnPreparedListener(mp -> {
                Log.d(TAG, "playRingtone: new ");
                binding.ivPlayRingtoneFragment.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_icon, null));
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                clearMediaPlayer();
                binding.ivPlayRingtoneFragment.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_play_icon, null));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void update(final MediaPlayer mediaPlayer, final Context context) {
//        binding.seekBarRingtoneFragment.setMax(mediaPlayer.getDuration());
//        ((Activity) context).runOnUiThread(() -> {
//            binding.seekBarRingtoneFragment.setProgress(mediaPlayer.getCurrentPosition());
//            if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() > 100) {
//                binding.tvTimerRingtoneFragment.setText(MessageFormat.format("{0}", convertSecondsToHMmSs(mediaPlayer.getCurrentPosition() / 1000)));
//            } else {
//                binding.tvTimerRingtoneFragment.setText(convertSecondsToHMmSs(mediaPlayer.getDuration() / 1000));
//                binding.seekBarRingtoneFragment.setProgress(0);
//            }
//            Handler handler = new Handler();
//            try {
//                Runnable runnable = () -> {
//                    try {
//                        if (mediaPlayer.getCurrentPosition() > -1) {
//                            try {
//                                update(mediaPlayer, context);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                };
//                handler.postDelayed(runnable, 2);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        });
    }

    private static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", m, s);
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