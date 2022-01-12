package com.hammad.iphoneringtones.ui.ringtones;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.hammad.iphoneringtones.R;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.jvm.internal.Intrinsics;

import org.jetbrains.annotations.NotNull;

class RingtonesAdapter extends Adapter<RingtonesAdapter.SongViewHolder> {
    private static final String TAG = "RingtonesAdapter";
    ArrayList<RingtoneModel> ringtoneModelArrayList;
    OnSongItemClickListener mListener;
    Context context;
    public MediaPlayer mediaPlayer;
    ImageView iv_pause;
    int lastPosition = -1;
    Handler handler;
    Runnable runnable;

    public RingtonesAdapter(Context context, OnSongItemClickListener listener) {
        ringtoneModelArrayList = new ArrayList<>();
        this.context = context;
        this.mListener = listener;
        mediaPlayer = new MediaPlayer();
    }

    @NotNull
    public SongViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        return new SongViewHolder(LayoutInflater.from(context).inflate(R.layout.ringtone_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RingtoneModel ringtoneModel = ringtoneModelArrayList.get(position);
        holder.itemView.setOnClickListener(view -> mListener.onItemClick(position));
        holder.iv_set_ringtone_song_item.setOnClickListener(view -> mListener.onSetRingtone(position));
        holder.tv_title_ringtone_song_item.setText(capitalize(ringtoneModel.getRingtoneTitle()));
        holder.iv_play_ringtone_song_item.setOnClickListener(view -> {
            playRingtone(holder, position);
            iv_pause = holder.iv_play_ringtone_song_item;
        });
        if (ringtoneModel.isPlaying) {
            holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_icon, null));
        } else {
            holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_play_icon, null));
        }
    }

    private void playRingtone(SongViewHolder holder, int position) {
        Log.d(TAG, "playRingtone: ");
        if (lastPosition == position) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                Log.d(TAG, "playRingtone: pause");
                mediaPlayer.pause();
                holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_play_icon, null));
            } else if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                Log.d(TAG, "playRingtone: start");
                holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_icon, null));
                mediaPlayer.start();
                update(mediaPlayer, holder.tv_duration_ringtone_song_item, holder.progressBar, context);
            } else {
                Log.d(TAG, "playRingtone: startMediaPlayer");
                startMediaPlayer(position, holder, 0);
            }
            lastPosition = position;
        } else {
            notifyItemChanged(lastPosition);
            lastPosition = position;
            clearMediaPlayer();
            startMediaPlayer(position, holder, 1000);
        }
    }

    private void startMediaPlayer(int position, SongViewHolder holder, int timer) {
        new Handler().postDelayed(() -> {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(ringtoneModelArrayList.get(position).getRingtoneURL());
                mediaPlayer.prepare();
                mediaPlayer.setVolume(10, 10);
                mediaPlayer.start();
                holder.progressBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.setOnPreparedListener(mp -> {
                    update(mediaPlayer, holder.tv_duration_ringtone_song_item, holder.progressBar, context);
                    holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_icon, null));
                });
                mediaPlayer.setOnCompletionListener(mp -> {
                    handler.removeCallbacks(runnable);
                    holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_play_icon, null));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, timer);
    }

    private void clearMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void stopMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                iv_pause.performClick();
            }
        }
    }

    private static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", m, s);
    }

    private void update(final MediaPlayer mediaPlayer, final TextView tv_time, ProgressBar progressBar, final Context context) {
        ((Activity) context).runOnUiThread(() -> {
            progressBar.setProgress(mediaPlayer.getCurrentPosition());
            if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() > 100) {
                tv_time.setText(MessageFormat.format("{0}", convertSecondsToHMmSs(mediaPlayer.getCurrentPosition())));
            } else {
                tv_time.setText(context.getResources().getString(R.string._00_00));
                progressBar.setProgress(0);
            }
            handler = new Handler();
            try {
                runnable = () -> {
                    try {
                        if (mediaPlayer.getCurrentPosition() > -1) {
                            try {
                                update(mediaPlayer, tv_time, progressBar, context);
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

    //    private void update1(final MediaPlayer mediaPlayer, final TextView tv_time, ProgressBar progressBar, final Context context) {
//        tv_time.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
//        ((Activity) context).runOnUiThread(() -> {
//            Log.d(TAG, "update: " + mediaPlayer.getDuration() + " | " + mediaPlayer.getCurrentPosition() + " | " + (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));
//            if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() > 100) {
//                tv_time.setText(MessageFormat.format("{0}", convertSecondsToHMmSs(mediaPlayer.getCurrentPosition() / 1000)));
//                long finishedSeconds = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
//                int total = (int) (((float) finishedSeconds / (float) mediaPlayer.getDuration()) * 100.0);
//                progressBar.setProgress(total);
//            } else {
//                tv_time.setVisibility(View.INVISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//            handler = new Handler();
//            try {
//                runnable = () -> {
//                    try {
//                        if (mediaPlayer.getCurrentPosition() > -1) {
//                            try {
//                                update(mediaPlayer, tv_time, progressBar, context);
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
//    }
    public String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, Objects.requireNonNull(capMatcher.group(1)).toUpperCase() + Objects.requireNonNull(capMatcher.group(2)).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    @Override
    public int getItemCount() {
        return ringtoneModelArrayList.size();
    }

    public static class SongViewHolder extends ViewHolder {
        TextView tv_title_ringtone_song_item;
        TextView tv_duration_ringtone_song_item;
        ImageView iv_play_ringtone_song_item;
        ImageView iv_set_ringtone_song_item;
        ProgressBar progressBar;

        public SongViewHolder(View itemView) {
            super(itemView);
            tv_title_ringtone_song_item = itemView.findViewById(R.id.tv_title_ringtone_song_item);
            tv_duration_ringtone_song_item = itemView.findViewById(R.id.tv_duration_ringtone_song_item);
            iv_play_ringtone_song_item = itemView.findViewById(R.id.iv_play_ringtone_song_item);
            iv_set_ringtone_song_item = itemView.findViewById(R.id.iv_set_ringtone_song_item);
            progressBar = itemView.findViewById(R.id.progress_bar_play_ringtone_song_item);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateRingtoneList(RingtoneModel ringtoneModel) {
        ringtoneModelArrayList.add(ringtoneModel);
        notifyDataSetChanged();
    }

    public interface OnSongItemClickListener {
        void onItemClick(int position);

        void onSetRingtone(int position);
    }
}

