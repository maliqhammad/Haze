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

import java.util.List;

public class Adapter implements CreativePagerAdapter {
    Context context;
    List<WallpaperModel> wallpaperModelArrayList;
    int CURRENT_POSITION;

    public Adapter(Context context, int CURRENT_POSITION, List<WallpaperModel> wallpaperModelArrayList) {
        this.context = context;
        this.CURRENT_POSITION = CURRENT_POSITION;
        this.wallpaperModelArrayList = wallpaperModelArrayList;
    }

    @Override
    public int getCount() {
        return wallpaperModelArrayList.size();
    }

    @NonNull
    @Override
    public View instantiateContentItem(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, int position) {
        View view = layoutInflater.inflate(R.layout.creative_view_header_item, viewGroup, false);
        ImageView iv_creative_view_header_item = view.findViewById(R.id.iv_creative_view_header_item);
        Glide.with(view).load(wallpaperModelArrayList.get(position).getWallpaperUri()).into(iv_creative_view_header_item);
        return view;
    }

    @NonNull
    @Override
    public View instantiateHeaderItem(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, int position) {
        View view = layoutInflater.inflate(R.layout.creative_view_content_item, viewGroup, false);
        ImageView iv_image_creative_view_content_item = view.findViewById(R.id.iv_image_creative_view_content_item);
        Glide.with(view).load(wallpaperModelArrayList.get(position).getWallpaperUri()).into(iv_image_creative_view_content_item);
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
