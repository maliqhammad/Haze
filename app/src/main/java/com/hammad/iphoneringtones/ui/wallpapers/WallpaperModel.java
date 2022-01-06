package com.hammad.iphoneringtones.ui.wallpapers;

import java.io.Serializable;

public class WallpaperModel implements Serializable {
    private int image = 0;
    private String wallpaperTitle = "";
    private String wallpaperUri;

    public WallpaperModel(String wallpaperTitle, String wallpaperUri) {
        this.wallpaperTitle = wallpaperTitle;
        this.wallpaperUri = wallpaperUri;
    }

    public WallpaperModel() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getWallpaperTitle() {
        return wallpaperTitle;
    }

    public void setWallpaperTitle(String wallpaperTitle) {
        this.wallpaperTitle = wallpaperTitle;
    }

    public String getWallpaperUri() {
        return wallpaperUri;
    }

    public void setWallpaperUri(String wallpaperUri) {
        this.wallpaperUri = wallpaperUri;
    }
}
