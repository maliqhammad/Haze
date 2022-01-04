package com.hammad.iphoneringtones.ui.wallpapers;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class WallpaperModel implements Serializable {
    private Integer image = 0;
    private String wallpaperTitle = "";
    private Bitmap bitmap;
    private Uri wallpaperUri;

    public WallpaperModel(String wallpaperTitle, Uri wallpaperUri) {
        this.wallpaperTitle = wallpaperTitle;
        this.wallpaperUri = wallpaperUri;
    }

    public WallpaperModel() {
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getWallpaperTitle() {
        return wallpaperTitle;
    }

    public void setWallpaperTitle(String wallpaperTitle) {
        this.wallpaperTitle = wallpaperTitle;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getWallpaperUri() {
        return wallpaperUri;
    }

    public void setWallpaperUri(Uri wallpaperUri) {
        this.wallpaperUri = wallpaperUri;
    }
}
