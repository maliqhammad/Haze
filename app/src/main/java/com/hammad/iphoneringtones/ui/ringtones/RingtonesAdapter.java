package com.hammad.iphoneringtones.ui.ringtones;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
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

import kotlin.jvm.internal.Intrinsics;

import org.jetbrains.annotations.NotNull;

class RingtonesAdapter extends Adapter<RingtonesAdapter.SongViewHolder> {
    ArrayList<RingtoneModel> ringtoneModelArrayList;
    OnSongItemClickListener mListener;
    Context context;
    public MediaPlayer mediaPlayer;
    ImageView iv_pause;
    int lastPosition = -1;

    public RingtonesAdapter(Context context, OnSongItemClickListener listener) {
        ringtoneModelArrayList = new ArrayList<>();
        this.context = context;
        this.mListener = listener;
        mediaPlayer = new MediaPlayer();
    }

    @NotNull
    public SongViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        View v = LayoutInflater.from(context).inflate(R.layout.ringtone_item, parent, false);
        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RingtoneModel ringtoneModel = ringtoneModelArrayList.get(position);
        if (ringtoneModel.isPlaying) {
            holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_icon, null));
        } else {
            holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_play_icon, null));
        }
        holder.itemView.setOnClickListener(view -> mListener.onItemClick(position));
//        holder.iv_play_ringtone_song_item.setOnClickListener(view -> mListener.onPlayRingtone(position));
        holder.iv_set_ringtone_song_item.setOnClickListener(view -> mListener.onSetRingtone(position));
        holder.tv_title_ringtone_song_item.setText(ringtoneModel.getRingtoneTitle());
        holder.iv_play_ringtone_song_item.setOnClickListener(view -> {
            playRingtone(holder, position);
            iv_pause = holder.iv_play_ringtone_song_item;
        });
    }

    private void playRingtone(SongViewHolder holder, int position) {
        holder.progress_bar_play_ringtone_song_item.setVisibility(View.VISIBLE);
        holder.iv_play_ringtone_song_item.setVisibility(View.GONE);
        if (lastPosition == position) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                holder.progress_bar_play_ringtone_song_item.setVisibility(View.GONE);
                holder.iv_play_ringtone_song_item.setVisibility(View.VISIBLE);
                holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_play_icon, null));
            } else if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                holder.progress_bar_play_ringtone_song_item.setVisibility(View.GONE);
                holder.iv_play_ringtone_song_item.setVisibility(View.VISIBLE);
                holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_icon, null));
                mediaPlayer.start();
                update(mediaPlayer, holder.tv_duration_ringtone_song_item, context);
            } else {
                startMediaPlayer(position, holder);
            }
            lastPosition = position;
        } else {
            notifyItemChangedOnPosition(lastPosition);
            lastPosition = position;
            clearMediaPlayer();
            startMediaPlayer(position, holder);
        }
    }

    private void startMediaPlayer(int position, SongViewHolder holder) {

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(ringtoneModelArrayList.get(position).getRingtoneURL());
            mediaPlayer.prepare();
            mediaPlayer.setVolume(10, 10);
            mediaPlayer.start();
            update(mediaPlayer, holder.tv_duration_ringtone_song_item, context);
            mediaPlayer.setOnPreparedListener(mp -> {
                holder.tv_duration_ringtone_song_item.setText(MessageFormat.format("{0}", convertSecondsToHMmSs(mp.getDuration() / 1000)));
                holder.progress_bar_play_ringtone_song_item.setVisibility(View.GONE);
                holder.iv_play_ringtone_song_item.setVisibility(View.VISIBLE);
                holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pause_icon, null));
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                holder.progress_bar_play_ringtone_song_item.setVisibility(View.GONE);
                holder.iv_play_ringtone_song_item.setVisibility(View.VISIBLE);
                holder.iv_play_ringtone_song_item.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_play_icon, null));
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

    private void notifyItemChangedOnPosition(int position) {
        notifyItemChanged(position);
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
//        long h = (seconds / (60 * 60)) % 24;
//        return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s);
        return String.format(Locale.getDefault(), "%02d:%02d", m, s);
    }

    private void update(final MediaPlayer mediaPlayer, final TextView tv_time, final Context context) {
        ((Activity) context).runOnUiThread(() -> {
            if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() > 100) {
                tv_time.setText(MessageFormat.format("{0}", convertSecondsToHMmSs(mediaPlayer.getCurrentPosition() / 1000)));
            } else {
                tv_time.setText(convertSecondsToHMmSs(mediaPlayer.getDuration() / 1000));
            }
            Handler handler = new Handler();
            try {
                Runnable runnable = () -> {
                    try {
                        if (mediaPlayer.getCurrentPosition() > -1) {
                            try {
                                update(mediaPlayer, tv_time, context);
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
    public int getItemCount() {
        return ringtoneModelArrayList.size();
    }

    public static class SongViewHolder extends ViewHolder {
        TextView tv_title_ringtone_song_item;
        TextView tv_duration_ringtone_song_item;
        ImageView iv_play_ringtone_song_item;
        ImageView iv_set_ringtone_song_item;
        ProgressBar progress_bar_play_ringtone_song_item;

        public SongViewHolder(View itemView) {
            super(itemView);
            tv_title_ringtone_song_item = itemView.findViewById(R.id.tv_title_ringtone_song_item);
            tv_duration_ringtone_song_item = itemView.findViewById(R.id.tv_duration_ringtone_song_item);
            iv_play_ringtone_song_item = itemView.findViewById(R.id.iv_play_ringtone_song_item);
            iv_set_ringtone_song_item = itemView.findViewById(R.id.iv_set_ringtone_song_item);
            progress_bar_play_ringtone_song_item = itemView.findViewById(R.id.progress_bar_play_ringtone_song_item);
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

