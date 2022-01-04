package com.hammad.iphoneringtones.ui.wallpapers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hammad.iphoneringtones.R;
import com.tbuonomo.creativeviewpager.adapter.CreativePagerAdapter;

import java.util.ArrayList;

public class Adapter implements CreativePagerAdapter {
    Context context;
    ArrayList<WallpaperModel> wallpaperModelArrayList;

    public Adapter(Context context, ArrayList<WallpaperModel> wallpaperModelArrayList) {
        this.context = context;
        this.wallpaperModelArrayList = wallpaperModelArrayList;
    }

    @Override
    public int getCount() {
        return wallpaperModelArrayList.size();
    }

    @NonNull
    @Override
    public View instantiateContentItem(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.creative_view_header_item, viewGroup, false);
        ImageView iv_popular_wallpaper_adapter_item = view.findViewById(R.id.iv_popular_wallpaper_adapter_item);
        Glide.with(view).load(wallpaperModelArrayList.get(i).getWallpaperUri()).into(iv_popular_wallpaper_adapter_item);
        return view;
    }

    @NonNull
    @Override
    public View instantiateHeaderItem(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.creative_view_content_item, viewGroup, false);
        ImageView iv_popular_wallpaper_adapter_item = view.findViewById(R.id.iv_popular_wallpaper_adapter_item);
        Glide.with(view).load(wallpaperModelArrayList.get(i).getWallpaperUri()).into(iv_popular_wallpaper_adapter_item);
        return view;
    }

    @Override
    public boolean isUpdatingBackgroundColor() {
        return true;
    }

    @Nullable
    @Override
    public Bitmap requestBitmapAtPosition(int i) {
        return null;
    }
}
