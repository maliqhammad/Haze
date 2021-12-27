package com.hammad.iphoneringtones.ui.ringtones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.hammad.iphoneringtones.R;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;

import org.jetbrains.annotations.NotNull;

class RingtonesAdapter extends Adapter<RingtonesAdapter.SongViewHolder> {
    ArrayList<RingtoneModel> mSongList;
    OnSongItemClickListener mListener;
    Context context;

    public RingtonesAdapter(Context context, ArrayList<RingtoneModel> songList, OnSongItemClickListener listener) {
        this.mSongList = songList;
        this.context = context;
        this.mListener = listener;
    }

    @NotNull
    public SongViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        View v = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        RingtoneModel ringtoneModel = mSongList.get(position);
        holder.itemView.setOnClickListener(view -> {
            if (position != -1) {
                mListener.onItemClick(position);
            }
        });
        holder.iv_play_ringtone_song_item.setOnClickListener(view -> {
            if (position != -1) {
                mListener.onPlayRingtone(position);
            }
        });
        holder.iv_set_ringtone_song_item.setOnClickListener(view -> {
            if (position != -1) {
                mListener.onSetRingtone(position);
            }
        });
        holder.tv_title_ringtone_song_item.setText(ringtoneModel.getSongTitle());
//        holder.tv_duration_ringtone_song_item.setText(SongProvider.getDurationMilliToSec(songItem.getSongDuration()));
    }

    public void setPlayPauseIcon(int lastPosition, int currentPosition) {
        if (lastPosition != -1) {

        }
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public static class SongViewHolder extends ViewHolder {
        TextView tv_title_ringtone_song_item;
        TextView tv_duration_ringtone_song_item;
        ImageView iv_play_ringtone_song_item;
        ImageView iv_set_ringtone_song_item;

        public SongViewHolder(View itemView) {
            super(itemView);
            tv_title_ringtone_song_item = itemView.findViewById(R.id.tv_title_ringtone_song_item);
            tv_duration_ringtone_song_item = itemView.findViewById(R.id.tv_duration_ringtone_song_item);
            iv_play_ringtone_song_item = itemView.findViewById(R.id.iv_play_ringtone_song_item);
            iv_set_ringtone_song_item = itemView.findViewById(R.id.iv_set_ringtone_song_item);
        }
    }

    public interface OnSongItemClickListener {
        void onItemClick(int position);

        void onSetRingtone(int position);

        void onPlayRingtone(int position);
    }
}
