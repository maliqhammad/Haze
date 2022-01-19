package com.hammad.iphoneringtones.ui.wallpapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.hammad.iphoneringtones.R;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public final class FullWallpaperAdapter extends Adapter<FullWallpaperAdapter.FullWallpaperAdapter1ViewHolder> {
    Context context;
    List<WallpaperModel> list;

    public FullWallpaperAdapter(Context context, List<WallpaperModel> list) {
        this.context = context;
        this.list = list;
    }

    @NotNull
    public FullWallpaperAdapter.FullWallpaperAdapter1ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new FullWallpaperAdapter1ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.full_wallpaper_adapter_item, parent, false));
    }

    public void onBindViewHolder(@NotNull FullWallpaperAdapter.FullWallpaperAdapter1ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getWallpaperUri()).into(holder.iv_image_creative_view_content_item);
    }

    public int getItemCount() {
        return list.size();
    }

    public static final class FullWallpaperAdapter1ViewHolder extends ViewHolder {
        ImageView iv_image_creative_view_content_item;

        public FullWallpaperAdapter1ViewHolder(View itemView) {
            super(itemView);
            iv_image_creative_view_content_item = itemView.findViewById(R.id.iv_image_creative_view_content_item);
        }
    }

}

