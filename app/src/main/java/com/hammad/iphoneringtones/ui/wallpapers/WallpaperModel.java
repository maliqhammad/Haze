package com.hammad.iphoneringtones.ui.wallpapers;

import android.graphics.Bitmap;
import android.net.Uri;

public class WallpaperModel {
    private Integer image = 0;
    private String imageTitle = "";
    private Bitmap bitmap;
    private Uri uri;

    public WallpaperModel(String imageTitle, Uri uri) {
        this.imageTitle = imageTitle;
        this.uri = uri;
    }

    public WallpaperModel() {
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
