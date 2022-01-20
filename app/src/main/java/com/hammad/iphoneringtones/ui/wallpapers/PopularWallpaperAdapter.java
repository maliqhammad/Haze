package com.hammad.iphoneringtones.ui.wallpapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.GlideImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PopularWallpaperAdapter extends RecyclerView.Adapter<PopularWallpaperAdapter.HomeAdapterViewHolder> {
    Context context;
    ArrayList<WallpaperModel> arrayList;
    PopularWallpaperAdapterCallback callback;

    public PopularWallpaperAdapter(Context context, PopularWallpaperAdapterCallback callback) {
        this.context = context;
        this.callback = callback;
        arrayList = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public HomeAdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new HomeAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.wallpaper_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeAdapterViewHolder holder, int position) {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.home_bg)
                .error(R.drawable.home_bg)
                .priority(Priority.HIGH);

        new GlideImageLoader(holder.iv_popular_wallpaper_adapter_item, holder.progress_bar_popular_wallpaper_adapter_item).load(arrayList.get(position).getWallpaperUri(), options);

        holder.tv_name_popular_wallpaper_adapter_item.setText(arrayList.get(position).getWallpaperTitle());
        holder.iv_download_popular_wallpaper_adapter_item.setOnClickListener(view -> callback.onDownloadWallpaper(arrayList.get(position)));
        holder.itemView.setOnClickListener(view -> callback.onItemClick(view, arrayList, position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class HomeAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_popular_wallpaper_adapter_item, iv_download_popular_wallpaper_adapter_item;
        TextView tv_name_popular_wallpaper_adapter_item;
        ProgressBar progress_bar_popular_wallpaper_adapter_item;

        public HomeAdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            progress_bar_popular_wallpaper_adapter_item = itemView.findViewById(R.id.progress_bar_popular_wallpaper_adapter_item);
            iv_popular_wallpaper_adapter_item = itemView.findViewById(R.id.iv_popular_wallpaper_adapter_item);
            iv_download_popular_wallpaper_adapter_item = itemView.findViewById(R.id.iv_download_popular_wallpaper_adapter_item);
            tv_name_popular_wallpaper_adapter_item = itemView.findViewById(R.id.tv_name_popular_wallpaper_adapter_item);
        }
    }

    public void updateWallpapersList(WallpaperModel wallpaperModel) {
        arrayList.add(wallpaperModel);
        notifyDataSetChanged();
    }

    public interface PopularWallpaperAdapterCallback {
        default void onDownloadWallpaper(WallpaperModel wallpaperModel) {
        }

        ;

        void onItemClick(View view, ArrayList<WallpaperModel> wallpaperModel, int position);
    }
}
