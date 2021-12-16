package com.hammad.iphoneringtones.ui.slideshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.hammad.iphoneringtones.R;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;

import org.jetbrains.annotations.NotNull;

class SongAdapter extends Adapter<SongAdapter.SongViewHolder> {
    private ArrayList<SongItem> mSongList;
    private OnSongItemClickListener mListener;
    Context context;

    public SongAdapter(Context context, ArrayList<SongItem> songList, OnSongItemClickListener listener) {
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
        SongItem songItem = mSongList.get(position);
        holder.itemView.setOnClickListener((OnClickListener) (it -> {
            if (position != -1) {
                mListener.onItemClick(position);
            }
        }));
        holder.musicPlay.setOnClickListener((OnClickListener) (it -> {
            if (position != -1) {
                mListener.onPlayerRingClick(position);
            }
        }));
        holder.setRingtone.setOnClickListener((OnClickListener) (it -> {
            if (position != -1) {
                mListener.onSetRingClick(position);
            }
        }));
        holder.tvSongTitle.setText(songItem.getSongTitle());
        holder.tvSongDuration.setText(SongProvider.getDurationMilliToSec(songItem.getSongDuration()));
        holder.songItemLayout.setBackgroundResource(songItem.getSongBg());
        holder.musicPlay.setImageResource(songItem.getSongPlayImage());
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public static class SongViewHolder extends ViewHolder {
        TextView tvSongTitle;
        TextView tvSongDuration;
        ImageView musicPlay;
        ImageView setRingtone;
        ConstraintLayout songItemLayout;

        public SongViewHolder(View itemView) {
            super(itemView);
            tvSongTitle = itemView.findViewById(R.id.tvSongTitle);
            tvSongDuration = itemView.findViewById(R.id.tvSongDuration);
            musicPlay = itemView.findViewById(R.id.musicPlay);
            setRingtone = itemView.findViewById(R.id.setRingtone);
            songItemLayout = itemView.findViewById(R.id.songItemLayout);
        }
    }
}

