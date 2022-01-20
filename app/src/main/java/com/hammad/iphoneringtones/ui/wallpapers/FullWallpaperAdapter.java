package com.hammad.iphoneringtones.ui.wallpapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.GlideImageLoader;

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
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.home_bg)
                .error(R.drawable.home_bg)
                .priority(Priority.HIGH);

        new GlideImageLoader(holder.iv_full_wallpaper_adapter_item, holder.progress_bar_full_wallpaper_adapter_item).load(list.get(position).getWallpaperUri(), options);

    }

    public int getItemCount() {
        return list.size();
    }

    public static final class FullWallpaperAdapter1ViewHolder extends ViewHolder {
        ImageView iv_full_wallpaper_adapter_item;
        ProgressBar progress_bar_full_wallpaper_adapter_item;

        public FullWallpaperAdapter1ViewHolder(View itemView) {
            super(itemView);
            iv_full_wallpaper_adapter_item = itemView.findViewById(R.id.iv_full_wallpaper_adapter_item);
            progress_bar_full_wallpaper_adapter_item = itemView.findViewById(R.id.progress_bar_full_wallpaper_adapter_item);
        }
    }

}

