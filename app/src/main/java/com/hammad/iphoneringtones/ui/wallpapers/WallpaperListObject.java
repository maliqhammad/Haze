package com.hammad.iphoneringtones.ui.wallpapers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WallpaperListObject implements Serializable {
    List<WallpaperModel> wallpaperModelList = new ArrayList<>();

    public List<WallpaperModel> getWallpaperModelList() {
        return wallpaperModelList;
    }

    public void setWallpaperModelList(List<WallpaperModel> wallpaperModelList) {
        this.wallpaperModelList = wallpaperModelList;
    }
}
