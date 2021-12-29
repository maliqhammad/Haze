package com.hammad.iphoneringtones.ui.wallpapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.hammad.iphoneringtones.R;

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
        return new HomeAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.popular_wallpaper_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeAdapterViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).getUri()).into(holder.iv_popular_wallpaper_adapter_item);
//        Picasso.get().load(arrayList.get(position).getUri()).into(holder.iv_popular_wallpaper_adapter_item);
//        holder.iv_popular_wallpaper_adapter_item.setImageURI(arrayList.get(position).getUri());
        holder.tv_name_popular_wallpaper_adapter_item.setText(arrayList.get(position).getImageTitle());
        holder.iv_download_popular_wallpaper_adapter_item.setOnClickListener(view -> callback.onItemClickListener(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class HomeAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_popular_wallpaper_adapter_item, iv_download_popular_wallpaper_adapter_item;
        TextView tv_name_popular_wallpaper_adapter_item;

        public HomeAdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
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
        void onItemClickListener(WallpaperModel wallpaperModel);
    }
}
